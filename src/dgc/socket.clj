(ns dgc.socket
  (:use [dgc socket-utils socket-frames]
        [gloss core io]
        [lamina core]
        [clojure pprint])
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader InputStream)
           (java.nio ByteBuffer ByteOrder)
           (dfproto CoreProtocol$CoreRunCommandRequest
                    CoreProtocol$CoreBindRequest
                    CoreProtocol$CoreTextNotification
                    BasicApi$ListUnitsIn
                    BasicApi$ListUnitsOut
                    BasicApi$ListSquadsIn
                    BasicApi$ListSquadsOut)))
;;; Socket Stuff
(declare conn-handler)

(defn connect [server]
  (let [socket  (Socket. (:name server) (:port server))
        in      (.getInputStream socket)
        out     (.getOutputStream socket)
        chan    (channel)
        conn    (ref {:in in :out out :chan chan})]
    (doto (Thread. #(conn-handler conn)) (.start))
    conn))

(defn print-byte [b]
  (println "|" b                                ; original byte (or -1)
    \tab (if (> b 127) (- b 256) b)             ; signed byte
    \tab (pr-str (char (abs b)))                ; char of abs value
    \tab (pad (Integer/toBinaryString b) 8)))   ; padded binary

(defn encode-write [conn frame vals]
  ;(println "Sending:")
  ;(doall (map print-byte (byte-buffer-seq (contiguous (encode-all frame vals)))))

  (encode-to-stream frame (:out @conn) vals)
  (.flush (:out @conn)))

(defn conn-handler [conn]
  (while (nil? (:exit @conn))
    (let [b   (.read (:in @conn))
          sb  (if (> b 127) (- b 256) b)]
      ;(print-byte b)
      (cond
        ; exit
        (= b -1)
          (dosync (alter conn merge {:exit true}))
        ; add to channel
        :else
          (enqueue (:chan @conn) (byte-buffer [sb]))))))

(defn open-dfhack [c]
  (encode-write c handshake-frame [{:magic "DFHack?\n" :version (endify-int 1)}])
  (let [chan  (decode-channel-headers (:chan @c) handshake-frame)
        hs    (wait-for-message chan)]
    ;(println "Handshake Reply:" (prn-str (assoc hs :version (endify-int (:version hs)))))
    (dosync (alter c merge {:msgs (decode-channel chan message-frame)}))))

(defn close-dfhack [c]
  (encode-write c send-message-frame [{:type (endify-int -4) :message []}]))


(defn get-function-id [c fn-name in out plugin callback]
  (let [m (proto-bytes  CoreProtocol$CoreBindRequest
                       {:method     fn-name
                        :input_msg  (str "dfproto." in)   ;eg. ListUnitsOut / EmptyMessage
                        :output_msg (str "dfproto." out) ;eg. ListUnitsIn / EmptyMessage
                        :plugin     plugin})]
    (encode-write c send-message-frame [{:type (endify-int 0) :message m}])
    (callback (wait-for-message (:msgs @c)))))

(defn run-command [c fn-name fn-args callback]
  (let [m (proto-bytes  CoreProtocol$CoreRunCommandRequest
                       {:command "probe"
                       :arguments ""})]
    (encode-write c send-message-frame [{:type (endify-int 1) :message m}]) 
    (loop [ message (wait-for-message (:msgs @c))]
        (callback message)
        (if (= (:type message) :text)
          (recur (wait-for-message (:msgs @c)))))))

(defn call-function [c fn-id in args callback]
  (let [m (proto-bytes in args)]
    (encode-write c send-message-frame [{:type (endify-int fn-id) :message m}]) 
    (loop [ message (wait-for-message (:msgs @c))]
        (callback message)
        (if (= (:type message) :text)
          (recur (wait-for-message (:msgs @c)))))))

(defn notif-text
  "Given the message-bytes of a text notification return the actual text."
  [message-bytes]
  (apply str (map :text (:fragments (bytes-proto CoreProtocol$CoreTextNotification message-bytes)))))

(defn handler
  "Create a message handler"
  [& {:keys [on-result on-fail on-text on-quit]
      :or { on-result (constantly nil)
            on-fail (constantly nil)
            on-text (constantly nil)
            on-quit (constantly nil)}}]
  (fn [m]
    (case (:type m)
      :result
        (on-result (:message m))
      :fail
        (on-fail (endify-int (:error-code m)))
      :text
        (on-text (:message m))
      :quit
        (on-quit))))



;;; Connect
(println "Opening connection.")
(def c (connect {:name "localhost" :port 5000}))

;;; Handshake
(println "Sending handshake.")
(open-dfhack c)
(println "Handshake response recieved.")

;;;
;;; Call Commands
;;;

;(run-command c "probe" "" (handler :on-text (comp println notif-text)))

;;;
;;; Get Functions IDs
;;;

(def func-id-handler (handler :on-result #(println "Function Id:" (second %))))

;(get-function-id c "ListUnits" "ListUnitsIn" "ListUnitsOut" "" func-id-handler)
;(get-function-id c "ListSquads" "ListSquadsIn" "ListSquadsOut" "" func-id-handler)
;(get-function-id c "RenameUnit" "RenameUnitIn" "EmptyMessage" "rename" func-id-handler)


;;;
;;; Call Functions
;;;

(def func-handler (handler :on-result #(call-function
                                          c
                                          (second %)
                                          BasicApi$ListUnitsIn
                                          ;BasicApi$ListUnitsOut
                                          {:id_list [0]
                                           :scan_all true}
                                          (handler
                                            :on-result (comp pprint (partial bytes-proto BasicApi$ListUnitsOut))
                                            :on-text (comp println notif-text)
                                            :on-fail (partial println "Fail: ")))))

;(get-function-id c "ListUnits" "ListUnitsIn" "ListUnitsOut" "" func-handler)

;(def func-handler (handler :on-result #(call-function
;                                          c
;                                          (second %)
;                                          BasicApi$ListSquadsIn
;                                          ;BasicApi$ListSquadsOut
;                                          {}
;                                          (handler
;                                            :on-text (comp println notif-text)
;                                            :on-fail (partial println "Fail: ")))))
;
;(get-function-id c "ListSquads" "ListSquadsIn" "ListSquadsOut" "" func-handler)

;;; Close
(println "Closing connection.")
(close-dfhack c)
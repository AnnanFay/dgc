(ns dgc.socket-frames
  (:use [dgc socket-utils]
        [gloss core]))

(def handshake-frame
  (ordered-map  :magic    (string :ascii :length 8)
                :version  :int32))

(defcodec message-type (enum :int16 {
  :result (endify-short -1)
  :fail   (endify-short -2)
  :text   (endify-short -3)
  :quit   (endify-short -4)}))

(defcodec resultc (ordered-map
  :type     :result
  :padding  [:byte :byte]
  :message  (repeated :byte
                      :prefix (prefix
                                :int32
                                endify-int
                                endify-int))))
(defcodec failc (ordered-map
  :type       :fail
  :padding    [:byte :byte]
  :error-code :int32))

(defcodec textc (ordered-map
  :type     :text
  :padding  [:byte :byte]
  :message  (repeated :byte
                      :prefix (prefix
                                :int32
                                endify-int
                                endify-int))))
(defcodec quitc (ordered-map
  :type     :quit
  :padding  [:byte :byte]
  :message  :int32))

(defcodec message-frame
  (header
    message-type
    {:result resultc, :fail failc, :text textc, :quit quitc}
    #(endify-short (:type %))))

(def send-message-frame
  (ordered-map  :type     :int32
                :message  (repeated :byte
                                    :prefix (prefix
                                              :int32
                                              endify-int
                                              endify-int))))
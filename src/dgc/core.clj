(ns dgc.core
  ""
  (:use [dgc config read]
        [seesaw core table color mig]
        [cheshire.core]
        [clojure.pprint])
  (:require [clojure.string :as s]))

;;;;
;;;; Utilities
;;;;

; Like map but works on a maps values leaving keys unchanged
(defn map-vals [f m]
    (zipmap (keys m) (map f (vals m))))

(defn not-nil? [foo]
  (not (nil? foo)))

(defn in? 
  "true if seq contains elm"
  [elm seq]  
  (some #(= elm %) seq))


;;;;
;;;; Menus
;;;;

(def file-menu (menu  :text "File"
                      :mnemonic \F
                      :items [(seesaw.action/action :name "Open"    :tip "Open JSON export" :mnemonic \o :key (seesaw.keystroke/keystroke "menu O"))
                              (seesaw.action/action :name "Reload"  :tip "Reload file"      :mnemonic \r :key (seesaw.keystroke/keystroke "menu R"))]))
(def help-menu (menu  :text "Help"
                      :mnemonic \H
                      :items [(seesaw.action/action :name "Info"    :tip "What to do"       :mnemonic \i :key (seesaw.keystroke/keystroke "menu I"))
                              (seesaw.action/action :name "About"   :tip "Version info"     :mnemonic \a :key (seesaw.keystroke/keystroke "menu A"))]))

(def top-menubar (menubar :items [file-menu help-menu]))

;;;;
;;;; Make a table out of a list of maps
;;;;

(defn base-header [k]
  {:key k :text (s/capitalize (name k))})

; [:foo :bar :baz] -> [{:key :foo :text "Foo" } {...}]
(defn key-seq-to-header [key-seq & [extend]]
  (map #(merge extend (base-header %)) (remove nil? key-seq)))

(defn map-to-header [m & [extend]]
  (map #(merge extend (hash-map :key %1 :text (s/capitalize %2)))
        (keys m) (vals m)))

(defn set-column-renderers [col head]
  (.setHeaderRenderer col (new darrylbu.renderer.VerticalTableHeaderCellRenderer))
  
  (if (not-nil? (:width head))
    (.setMaxWidth col (:width head)))

  (if (not-nil? (:renderer head))
    (.setCellRenderer col (proxy [javax.swing.table.DefaultTableCellRenderer] []
                            (getTableCellRendererComponent [tbl obj isSelected hasFocus r c]
                              ((:renderer head) this obj))))))

(defn make-table [data to-row & [to-header cell-renderer]]
  (let [header  (if (nil? to-header)
                  (key-seq-to-header  (keys (first data)))
                  (to-header          (first data)))
        table   (table  :id           :table
                        :auto-resize  :off
                        :model        [ :columns  header
                                        :rows     (map to-row data)])
        columns   (enumeration-seq (-> table .getColumnModel .getColumns))]
    (.setAutoCreateRowSorter table true)
    (doall (map set-column-renderers columns header))
    ;(.setBackground table (color 255 142 10))
    ;setForeground
    table))


(defn labor-cell-renderer [this obj]
  (if obj 
    (.setBackground this (color 130 110 160))
    (.setBackground this (color :white)))
  ;(.setText this (str "foo"))
  this)






;;;;
;;;; Dwarf transformers
;;;;

;; REMEMBER: Rows are maps, a header is a sequences of {:key :foo :text "Foo"} maps



;;;; LABORS
(defn puffball-labor-header [puffball]
  (cons {:key :name :text "Name"} (key-seq-to-header labors { :renderer labor-cell-renderer
                                                              :width 14})))

; name and checkboxes for labors
(defn puffball-labors [puffball]
  (merge  {:name (get-full-name puffball)}
          (-> puffball :labors)))


;;;; SKILLS
(defn puffball-skill-header [puffball]
  (cons {:key :name :text "Name"} (map-to-header skills  {:width 14})))

(defn puffball-skills [puffball]
  (merge  {:name (get-full-name puffball)}
          (-> puffball :soul :skills)))


;;;; BODY
(defn puffball-body [puffball]
  (merge {:name (get-full-name puffball)} (-> puffball :body :physical)))

(defn puffball-soul [puffball]
  (merge {:name (get-full-name puffball)} (-> puffball :soul :mental)))


(defn dwarf-list [puffballs]
  (map #(str "[" (:sex %) "] " (get-full-name %)) puffballs))

(defn profession-list []
  ["Miner" "Leader" "Marksdwarf"])


(defn profession-info [profession]
  (mig-panel
      :constraints ["insets 0" "" ""]
      :items [
        [(label "Miner") "dock north, shrink 0"]
         ]))


(defn puffball-info [puffball]
  (mig-panel
      :constraints ["insets 0" "" ""]
      :items [
        [(label (get-full-name puffball)) "dock north, shrink 0"]
         ]))


(defn -main [& args]
 (let [height        800
       data          (parse-string (slurp "Dwarves.json") true)
       raw-puffballs (:root data)
       puffballs     (get-puffballs data raw-puffballs)
       puffballs     (filter #(= (:race %) "DWARF") puffballs) 
       tabs          [{:title    "Dwarf"
                       :tip      ""
                      :icon     ""
                      :content  (puffball-info (first puffballs))}
                      {:title    "Dwarves"
                      :tip      ""
                      :icon     ""
                      :content  (scrollable (make-table puffballs identity))}
                      {:title   "Labors"
                      :tip      ""
                      :icon     ""
                      :content  (scrollable (make-table puffballs puffball-labors puffball-labor-header))}
                      {:title   "Skills"
                      :tip      ""
                      :icon     ""
                      :content  (scrollable (make-table puffballs puffball-skills puffball-skill-header))}
                            {:title   "Body"
                            :tip      ""
                            :icon     ""
                            :content  (scrollable (make-table puffballs puffball-body))}
                            {:title   "Soul"
                            :tip      ""
                            :icon     ""
                            :content  (scrollable (make-table puffballs puffball-soul))}]
        dwarf-list      (listbox :model (dwarf-list puffballs)) 
        content         (mig-panel
                          :constraints ["insets 0" "[]12[grow]12[]" "[]12[grow]12[]"]
                          :items [
                            [(scrollable dwarf-list) "dock west"]
                            [(scrollable (listbox :model (profession-list)))      "dock east"]
                            [top-menubar "dock north, shrink 0"]
                            [(label :id :sel :text "Status Bar: ...") "dock south"]
                            [(tabbed-panel
                                :placement  :top
                                :overflow   :scroll
                                :tabs tabs) "growx, wrap"]])]
    ;(pprint (first puffballs))
    (listen dwarf-list :selection 
      (fn [e]
        (if-let [s (selection e {:multi? true})]
          (alert s))))
    (-> (frame 
          :title      "Dwarven Guidance Councilor" 
          :height     height
          :width      (* height 1.618)
          ;:menubar   top-menubar
          :content    content
          :on-close   :hide) ;:exit)
        ;pack!
        show!)))

; If called on the command line
(if *command-line-args*
  (apply -main *command-line-args*))

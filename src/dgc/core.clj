(ns dgc.core
  ""
  (:use [dgc util config read compat]
        [seesaw core table color mig font chooser keystroke]
        [seesaw.event :only [events-for]]
        [cheshire.core]
        [clojure.pprint])
  (:require [clojure.string :as s]))

(native!)

;;;;
;;;; Action Handlers
;;;;

(defn sort-by-name [e] (alert "foo"))
(defn sort-by-age  [e] (alert "bar"))

(defn load-export [e]
    (if-let [f (choose-file :filters [["JSON Files" ["json"]]])]
        (alert f)))

(defn reload-export [e]
    (if-let [f (choose-file)]
        (alert f)))


(defn selection-change[e]
  (let [root          (to-root e)
        dwarf-list (select root [:#dwarf-list])
        prof-list  (select root [:#prof-list])
        puffballs  (selection dwarf-list {:multi? true})
        profs      (selection prof-list {:multi? true})]
    (prn :profs)
    (pprint profs)

    (prn :puffballs)
    (pprint puffballs)
    (cond
      (nil? profs)
        (cond
          (nil? puffballs)        ""
          (= (count puffballs) 1) ""
          :else                   "")
      (= (count profs) 1)
        (cond
          (nil? puffballs)        ""
          (= (count puffballs) 1) ""
          :else                   "")
 
      :else
        (cond
          (nil? puffballs)        ""
          (= (count puffballs) 1) ""
          :else                   ""))))
        ;(display-puffball-info (first prof))
        ;(value! content {:main (str (:name (first prof)))})


; puffball selection changes
(defn puffball-selection-change[e]
  (selection-change e))
    
(defn prof-selection-change[e]
  (selection-change e))


;;;
;;; Selections
;;;

; No puff    / No profs
; display help message

; A puff     / No profs
; display puff info

; Multi puff / No profs
; Display summary info of selected / comparison

; No puff    / A prof
; display prof info

; A puff     / A prof
; display single compatability

; Multi puff / A prof
; display compatability for all puffs

; No puff    / Multi profs
; display symmary info of selected / comparison

; A puff     / Multi profs
; display compatability for all profs

; Multi puff / Multi profs
; ???????????????????
; display Not Implemented message






;;;;
;;;; Actions
;;;;

; Menubar
(def load-export-action   (action :name "Open"   :tip "Open JSON export"
                                  :mnemonic \o   :key (keystroke "menu O")
                                  :handler load-export))
(def reload-export-action (action :name "Reload" :tip "Reload file"      
                                  :mnemonic \r   :key (keystroke "menu R")
                                  :handler reload-export))
(def show-help-action     (action :name "Info"   :tip "What to do"       
                                  :mnemonic \i   :key (keystroke "menu I")))
(def show-version-action  (action :name "About"  :tip "Version info"     
                                  :mnemonic \a   :key (keystroke "menu A")))

;Dwarf list
(def sort-by-name-action (action :name "Sort By Name" :handler sort-by-name))
(def sort-by-age-action  (action :name "Sort By Age"  :handler sort-by-age))

;;;;
;;;; Menus
;;;;

(def file-menu (menu  :text "File"
                      :mnemonic \F
                      :items [load-export-action reload-export-action]))
(def help-menu (menu  :text "Help"
                      :mnemonic \H
                      :items [show-help-action show-version-action]))

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

(defn sex-symbol [s]
    (if s "♂" "♀"))

(defn prof-list-renderer [this {prof :value}]
    (.setText this (str (s/capitalize (name (first prof)))))
    this)

(defn puffball-list-renderer [this {puffball :value}]
    (.setText this (str "[" (sex-symbol (:sex puffball)) "] " (get-full-name puffball)))
    this)

(defn -main [& args]
  (let [height        800
       data          (parse-string (slurp "Dwarves.json") true)
       raw-puffballs (:root data)
       puffballs     (get-puffballs data raw-puffballs)
       puffballs     (filter #(= (:race %) "DWARF") puffballs) 
        prof-list       (listbox :id        :prof-list
                                 :model     professions
                                 :renderer  prof-list-renderer
                                 :popup     (fn [e] [sort-by-name-action sort-by-age-action]))
        dwarf-list      (listbox :id        :dwarf-list
                                 :model     puffballs
                                 :renderer  puffball-list-renderer
                                 :popup     (fn [e] [sort-by-name-action sort-by-age-action]))
        title           (label :id :title :text "Title" :h-text-position :center)
        content         (mig-panel
                          :constraints ["insets 0" "[]12[grow]12[]" "[]12[grow]12[]"]
                          :items [
                            [(scrollable dwarf-list)                            "dock west"]
                            [(scrollable prof-list)                             "dock east"]
                            [top-menubar                                        "dock north, shrink 0"]
                            [title                                              "dock north, shrink 0, growx"]
                            [(label :id :sel          :text "Status Bar: ...")  "dock south"]
                            [(label :id :main         :text "Foo")              "growx, wrap"]
                            [test-chart                                         "growx, wrap"]])]
    ;(pprint (first puffballs))
    ;(prn (compat (first puffballs) (:miner professions)))
    (pprint (events-for dwarf-list))

    (config! title :font (font :name :monospaced
                               :style #{:bold :italic}
                               :size 24)
                   :background :pink)

    (listen dwarf-list :selection puffball-selection-change)
    (listen  prof-list :selection     prof-selection-change)
    
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

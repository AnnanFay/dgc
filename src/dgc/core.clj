(ns dgc.core
  ""
  (:use [dgc util config read compat presets]
        [seesaw core table color mig font chooser keystroke]
        [seesaw.event :only [events-for]]
        [cheshire.core]
        [clojure.pprint])
  (:require [clojure.string :as s]))

(native!)

;;;;
;;;; ???????????????????????????????????????????????
;;;;

(defn key-title [k]
  (if (keyword? k)
    (s/capitalize (name k))
    (str k)))

(defn title [s]
  (config! (label :id :title :text (str s) :h-text-position :center)
           :font (font
             :name :monospaced
             :style #{:bold :italic}
             :size 24)
           :background :pink))


(defn ul [s]
  ;(prn s)
  (cond
    (map? s)
      (mig-panel
        :constraints ["insets 0" "" ""]
        :items (interleave
                (map #(vector (ul %) "") (keys s))
                (map #(vector (ul %) "wrap") (vals s)) ))
    (coll? s)
      (mig-panel
        :constraints ["insets 0" "" ""]
        :items (map #(vector (ul %) "wrap") s))
    :else
      (label :text (key-title s))))

(defn profession-info [profession]
  (let [prof-name (first profession)
        prof      (second profession)]
   (mig-panel
      :id :main
      :constraints ["insets 0, fill" "" ""]
      :items [[(title prof-name) "dock north, shrink 0, growx"]
             [(ul prof)          ""]])))

(defn puffball-info [puffball]
    (mig-panel
        :id :main
        :constraints ["insets 0" "" ""]
        :items [[(title (get-full-name puffball))            "dock north, shrink 0, growx"]
                [(ul puffball)]]))

(defn compat-info [puffball prof]
    (mig-panel
        :id :main
        :constraints ["insets 0" "" ""]
        :items [[(title (get-full-name puffball))       "dock north, shrink 0, growx"]
                [(label :text (str "Compatability: " (compat puffball (second prof))))]]))


(defn puffball-compats [puffball profs]
  (let [compats        (map #(vector (compat puffball (second %)) (first %)) profs)
        sorted-compats (reverse (sort compats))]
    (mig-panel
        :id :main
        :constraints ["insets 0" "" ""]
        :items [[(title (str (get-full-name puffball) "'s Profession Compatability")) "dock north, shrink 0, growx"]
               [(listbox :model (map #(str (second %) ":" (first %)) sorted-compats))]])))

; like map however returns a matrix of f applied to every combintion of c1 and c2
(defn map-prod [f c1 c2]
  (map #(map (partial f %) c2) c1))

(defn vv-to-map [vv]
    (apply hash-map (flatten vv)))

(defn compat-cell-renderer [this obj]
  (prn obj)
  (prn (compat-colour obj))
  (.setBackground this (compat-colour obj))
  (if (float? obj)
    (.setText this (format "%.2f" obj))
    (.setText this (str obj)))
  this)

(declare key-seq-to-header make-table)

(defn compat-header [compats]
  (key-seq-to-header (keys compats) { :renderer compat-cell-renderer}))

(defn puffball-compats-table [puffballs profs]
  ;(prn puffballs profs)
  (let [data    (map-prod #(vector (first %2) (compat %1 (second %2))) puffballs profs)
        data    (map vv-to-map data)
        data    (map #(merge %1 (hash-map :name (get-full-name %2))) data puffballs)
        ;data    (map #(hash-map :foo %) (range 0 0.6 0.10001))
        tablith (make-table data identity compat-header)
        mp      (mig-panel
          :id :main
          :constraints ["insets 0, fill" "" "[pref!]r[]"]
          :items [[(title  "Compatability Matrix")       "wrap"]
                  [(scrollable tablith) "grow"]])]
  (config! mp :background :orange)))


;;;
;;; Selections
;;;

; No puff    / No profs
; display help message

(defn no-puff-no-prof [root]
    nil)

; A puff     / No profs
; display puff info

(defn a-puff-no-prof [root puffball]
  (let [p (select root [:#container])
        c (select root [:#smain])]
    (replace! p c (scrollable (puffball-info puffball) :id :smain))))

; Multi puff / No profs
; Display summary info of selected / comparison

(defn m-puff-no-prof [root puffballs]
    nil)

; No puff    / A prof
; display prof info

(defn no-puff-a-prof [root prof]
  (let [p (select root [:#container])
        c (select root [:#smain])]
    (replace! p c (scrollable (profession-info prof) :id :smain))))

; A puff     / A prof
; display single compatability

(defn a-puff-a-prof [root puffball prof]
  (let [p (select root [:#container])
        c (select root [:#smain])]
    (replace! p c (scrollable (compat-info puffball prof) :id :smain))))

; Multi puff / A prof
; display compatability for all puffs

(defn m-puff-a-prof [root puffballs prof]
    nil)

; No puff    / Multi profs
; display symmary info of selected / comparison

(defn no-puff-m-prof [root profs]
    nil)

; A puff     / Multi profs
; display compatability for all selected profs

(defn a-puff-m-prof [root puffball profs]
  (let [p (select root [:#container])
        c (select root [:#smain])]
    (replace! p c (scrollable (puffball-compats puffball profs) :id :smain))))

; Multi puff / Multi profs
; Table of dwarf/profession compatabilities.

(defn m-puff-m-prof [root puffballs profs]
  (let [p (select root [:#container])
        c (select root [:#smain])]
    (replace! p c (scrollable (puffball-compats-table puffballs profs) :id :smain))))

;;;;
;;;; Action Handlers
;;;;

(defn sort-by-name [e] (prn "sort-by-name" e) (alert "Sorry, not implemented"))
(defn sort-by-age  [e] (prn "sort-by-age" e)  (alert "Sorry, not implemented"))

(defn load-export [e]
    (if-let [f (choose-file :filters [["JSON Files" ["json"]]])]
        (alert f)))

(defn reload-export [e]
    (if-let [f (choose-file)]
        (alert f)))


(defn selection-change [e]
  (let [root          (to-root e)
        dwarf-list (select root [:#dwarf-list])
        prof-list  (select root [:#prof-list])
        puffballs  (selection dwarf-list {:multi? true})
        profs      (selection prof-list {:multi? true})]
    (cond
      (nil? profs)
        (cond
          (nil? puffballs)        (no-puff-no-prof root)
          (= (count puffballs) 1) (a-puff-no-prof root (first puffballs))
          :else                   (m-puff-no-prof root puffballs))
      (= (count profs) 1)
        (cond
          (nil? puffballs)        (no-puff-a-prof root (first profs))
          (= (count puffballs) 1) (a-puff-a-prof root (first puffballs) (first profs))
          :else                   (m-puff-a-prof root puffballs (first profs)))
      :else
        (cond
          (nil? puffballs)        (no-puff-m-prof root profs)
          (= (count puffballs) 1) (a-puff-m-prof root (first puffballs) profs)
          :else                   (m-puff-m-prof root puffballs profs)))))


; puffball selection changes
(defn puffball-selection-change [e]
  (selection-change e))
    
(defn prof-selection-change [e]
  (selection-change e))

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
(def sort-by-name-action      (action :name "Sort by Name" :handler sort-by-name))
(def sort-by-age-action       (action :name "Sort by Age"  :handler sort-by-age))

(def add-prof-preset-action   (action :name "Add selection as preset" :handler add-prof-preset))
(def add-dwarf-preset-action  (action :name "Add selection as preset" :handler add-dwarf-preset))

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

(defn make-table [data to-row & [to-header]]
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

(defn sex-symbol [s]
    (if s "♂" "♀"))

(defn prof-list-renderer [this {prof :value}]
  (.setText this (str (s/capitalize (name (first prof)))))
  this)

(defn puffball-list-renderer [this {puffball :value}]
  (.setText this (str "[" (sex-symbol (:sex puffball)) "] " (get-full-name puffball)))
  this)

(defn preset-list-renderer [this {preset :value}]
  (.setText this (str (first preset)))
  this)

(defn -main [& args]
  (let [height            800
        data              (parse-string (slurp "Dwarves.json") true)
        raw-puffballs     (:root data)
        puffballs         (get-puffballs data raw-puffballs)
        puffballs         (filter #(= (:race %) "DWARF") puffballs) 
        prof-list         (listbox :id        :prof-list
                                   :model     (sort professions)
                                   :renderer  prof-list-renderer
                                   :popup     (fn [e] [sort-by-name-action sort-by-age-action add-prof-preset-action]))
        dwarf-list        (listbox :id        :dwarf-list
                                   :model     (sort-by get-full-name puffballs)
                                   :renderer  puffball-list-renderer
                                   :popup     (fn [e] [sort-by-name-action sort-by-age-action add-dwarf-preset-action]))
        default-presets   {:none nil}
        dwarf-preset-list (combobox :id       :dwarf-presets
                                    :model    (conj (get-presets "dwarves") default-presets)
                                    :renderer preset-list-renderer
                                    :listen   [:selection (partial change-dwarf-preset puffballs)])
        prof-preset-list  (combobox :id       :prof-presets
                                    :model    (conj (get-presets "profs") default-presets)
                                    :renderer preset-list-renderer
                                    :listen   [:selection (partial change-prof-preset professions)])
        button-rem-dwarf-preset (button :text "-" :listen [:action rem-dwarf-preset])
        button-rem-prof-preset  (button :text "-" :listen [:action rem-prof-preset])
        content           (mig-panel
                            :id          :container
                            :constraints ["insets 0, fill" "[pref!]r[grow]r[pref!]" "[pref!]r[grow]r[pref!]"]
                            :items [
                              [dwarf-preset-list                                  "split 2"]
                              [button-rem-dwarf-preset                            ""]
                              [top-menubar                                        ""]
                              [prof-preset-list                                   "split 2"]
                              [button-rem-prof-preset                             "wrap"]

                              [(scrollable dwarf-list)                            "span 1 2, growy"]
                              [(scrollable (mig-panel :id :main) :id :smain)      "grow"]
                              [(scrollable prof-list)                             "span 1 2, growy, wrap"]
                              [(label :id :status :text "Status Bar: ...")        ""]])]
                              ;[test-chart                                         "growx, wrap"]])]


    (listen dwarf-list :selection puffball-selection-change)
    (listen  prof-list :selection     prof-selection-change)
    
    (-> (frame 
          :title      "Dwarven Guidance Councilor" 
          :height     height
          :width      (* height 1.618)
          :content    content
          :on-close   :hide) ;:exit)
        show!)
    (selection! dwarf-list {:multi? true} [(first puffballs) (second puffballs)])
    (selection! prof-list {:multi? true} [(first professions) (second professions)])
))

; If called on the command line
(if *command-line-args*
  (apply -main *command-line-args*))

(ns dgc.core
  ""
  (:use [dgc.config]
        [seesaw core table color]
        [cheshire.core]
        [clojure.pprint])
  (:require [clojure.string :as s]))
;(:import [darrylbu.renderer.VerticalTableHeaderCellRenderer])

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
;;;; Restructuring the JSON input
;;;;

(defn get-value [data p]
  (if (nil? p)
    nil
    ((keyword p) data)))

(defn get-body [data body]
  {:physical (zipmap  physical-attributes
                      (map #(:unk1 (get-value data %)) (:physical_attrs body)))})

(defn get-skills [data raw-soul]
  (let [soul        (get-value data raw-soul)
        raw-skills  (:skills soul)
        skills      (map (partial get-value data) raw-skills)]
  (zipmap (map #(keyword (:id %)) skills) (map :rating skills))))
          

(defn get-soul [data raw-soul]
  { :traits       (zipmap traits
                          (->> raw-soul (get-value data) :traits))
    :skills       (get-skills data raw-soul)
    :mental       (zipmap mental-attributes
                    (map #(:unk1 (get-value data %)) (->> raw-soul (get-value data) :mental_attrs)))
    :preferences  (->> raw-soul (get-value data) :preferences)})

(defn get-full-name [puffball]
  (str (-> puffball :name :first) " " (-> puffball :name :nick) " " (-> puffball :name :last)))

(defn get-name [data puffball]
  (let [name (->> puffball :status :current_soul (get-value data) :name (get-value data))]
    { :first  (:first_name name)
      :last   (apply str (:translation name))
      :nick   (:nickname name)}))

(defn get-puffball [data raw-puffball]
  (let [puffball (get-value data raw-puffball)]
    {
      :id         (:id puffball)
      :name       (get-name data puffball)
      :race       (:race_name puffball)
      :civ        (:civ_id puffball)
      :sex        (:sex puffball)
      :age        (-> puffball :relations :old_year)
      :profession (or (:custom_profession puffball) (:profession puffball))
      :labors     (dissoc (zipmap labors
                          (-> puffball :status :labors)) nil)
      :happiness  (-> puffball :status :happiness)
      :appearance (-> puffball :appearance)
      :body       (get-body data (:body puffball))
      :soul       (get-soul data (-> puffball :status :current_soul))}))

(defn get-puffballs [data raw-puffballs]
  (map (partial get-puffball data) raw-puffballs))


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

(def menu-bar (menubar :items [file-menu help-menu]))

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
  ;(prn (merge {:name (get-full-name puffball)} (-> puffball :body :physical)))
  (merge {:name (get-full-name puffball)} (-> puffball :body :physical)))

(defn puffball-soul [puffball]
  ;(prn (merge {:name (get-full-name puffball)} (-> puffball :body :physical)))
  (merge {:name (get-full-name puffball)} (-> puffball :soul :mental)))


(defn -main
  []
  (let [height        800
        data          (parse-string (slurp "Dwarves.json") true)
        raw-puffballs (:root data)
        puffballs     (get-puffballs data raw-puffballs)
        ;creatures     (filter #(= (:race %) "") puffballs)
        tabs          [{:title    "Dwarves"
                        :tip      ""
                        :icon     ""
                        :content  (scrollable (make-table puffballs identity))}
                        ;{:title    "Creatures"
                        ;:tip      ""
                        ;:icon     ""
                        ;:content  (scrollable (make-table creatures identity))}
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
                        :content  (scrollable (make-table puffballs puffball-soul))}]]
    ;(pprint (first puffballs))
    (-> (frame 
          :title    "Dwarven Guidance Councilor" 
          :height   height
          :width    (* height 1.618)
          :menubar  menu-bar
          :content  (border-panel
                      ;:north (text :multi-line? true :wrap-lines? false :rows 3 :text (apply str (-> puffballs first :soul :mental)))
                      :center (tabbed-panel
                                :placement  :top
                                :overflow   :scroll
                                :tabs tabs)

                      ;:center (scrollable (make-table data to-dwarf))
                      :south  (label :id :sel :text "Selection: "))
          :on-close :exit)
        ;pack!
        show!)))

; If called on the command line
(if *command-line-args*
  (apply -main *command-line-args*))

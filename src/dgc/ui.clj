(ns dgc.ui
  ""
  (:use [dgc util config read compat presets menus]
        [seesaw core table color mig font]
        [seesaw.event :only [events-for]]
        [clojure.pprint])
  (:require [clojure.string :as s]))



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
  (let [panel (cond
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
                  (label :text (key-title s)))]
    (config! panel
     :background :white)))

(defn profession-info [profession]
  (let [prof-name (first profession)
        prof      (second profession)]
   (mig-panel
      :id :main
      :constraints ["insets 0, fill" "" ""]
      :items [[(title prof-name) "dock north, shrink 0, growx"]
             [(ul prof)          ""]])))

; tabbed panel
(defn puffball-info [puffball]
  (let [general-puff  (dissoc puffball :soul :body :labors)
        attr-puff     {:physical  (-> puffball :body :physical)
                       :mental    (-> puffball :soul :mental)}
        trait-puff    (-> puffball :soul :traits)
        skill-puff    (-> puffball :soul :skills)
        labors-puff   (:labors puffball)
        tabs [{:title   "General"
               :content (ul general-puff)}
              {:title   "Attributes"
               :content (ul attr-puff)}
              {:title   "Traits"
               :content (ul trait-puff)}
              {:title   "Skills"
               :content (ul skill-puff)}
              {:title   "Labours"
               :content (ul labors-puff)}
              {:title   "Equipment"
               :content (mig-panel)}]]
  (mig-panel
    :constraints ["insets 0, fill" "" ""]
    :items [[(title (get-full-name puffball)) "dock north, shrink 0, growx"]
            [(tabbed-panel
                :placement :top
                :overflow  :scroll
                :tabs      tabs)              "grow"]])))

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

(defn compat-header [compats]
  (conj (key-seq-to-header (keys (dissoc compats :name)) { :renderer compat-cell-renderer :width 36}) {:key :name :text "Name"}))

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


;;;;
;;;; Action Handlers
;;;;
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
; display compatability for all selected profs
; Multi puff / Multi profs
; Table of dwarf/profession compatabilities.


(defn rep! [root screen]
  (let [p           (select root [:#container])
        c           (select root [:#smain])]
    (replace! p c (scrollable screen :id :smain))))

(defn selection-change [e]
  (let [root          (to-root e)
        dwarf-list  (select root [:#dwarf-list])
        prof-list   (select root [:#prof-list])
        puffballs   (selection dwarf-list {:multi? true})
        profs       (selection prof-list {:multi? true})]
    (cond
      (nil? profs) (cond
              (nil? puffballs)          nil
              (= (count puffballs) 1)   (rep! root (puffball-info (first puffballs)))
              :else                     nil)
      (= (count profs) 1) (cond
              (nil? puffballs)          (rep! root (profession-info (first profs)))
              (= (count puffballs) 1)   (rep! root (compat-info (first puffballs) (first profs)))
              :else                     nil)
      :else (cond
              (nil? puffballs)          nil
              (= (count puffballs) 1)   (rep! root (puffball-compats (first puffballs) profs))
              :else                     (rep! root (puffball-compats-table puffballs profs))))))


; puffball selection changes
(defn puffball-selection-change [e]
  (selection-change e))
    
(defn prof-selection-change [e]
  (selection-change e))


(defn labor-cell-renderer [this obj]
  (if obj 
    (.setBackground this (color 130 110 160))
    (.setBackground this (color :white)))
  ;(.setText this (str "foo"))
  this)

;;; List Renderers

(defn sex-symbol [s]
  (if (= s 1) "♂" "♀"))

(defn prof-list-renderer [this {prof :value}]
  (.setText this (str (s/capitalize (name (first prof)))))
  (config! this :font (font :size 12))
  this)

(defn puffball-list-renderer [this {puffball :value}]
  (.setText this (str "[" (sex-symbol (:sex puffball)) "] " (get-full-name puffball)))
  (config! this :font (font :size 12))
  this)

(defn preset-list-renderer [this {preset :value}]
  (.setText this (str (first preset)))
  this)

(defn make-content [puffballs]
  (let [prof-list         (listbox  :id       :prof-list
                                    :model    (sort professions)
                                    :renderer prof-list-renderer
                                    :popup    (fn [e] [sort-by-name-action sort-by-age-action add-prof-preset-action])
                                    :listen   [:selection prof-selection-change])
        dwarf-list        (listbox  :id       :dwarf-list
                                    :model    (sort-by get-full-name puffballs)
                                    :renderer puffball-list-renderer
                                    :popup    (fn [e] [sort-by-name-action sort-by-age-action add-dwarf-preset-action])
                                    :listen   [:selection puffball-selection-change])
        default-presets   {:none nil}
        dwarf-preset-list (combobox :id       :dwarf-presets
                                    :model    (conj (get-presets "dwarves") default-presets)
                                    :renderer preset-list-renderer
                                    :listen   [:selection (partial change-dwarf-preset puffballs)])
        prof-preset-list  (combobox :id       :prof-presets
                                    :model    (conj (get-presets "profs") default-presets)
                                    :renderer preset-list-renderer
                                    :listen   [:selection (partial change-prof-preset professions)])
        button-rem-dwarf-preset (button :text "-" :icon "" :listen [:action rem-dwarf-preset])
        button-rem-prof-preset  (button :text "-" :listen [:action rem-prof-preset])]
    (mig-panel
      :id          :container
      :constraints ["insets 0, fill, hidemode 3" "[pref!]r[grow]r[pref!]" "[pref!]r[grow]r[pref!]"]
      :items [
        [dwarf-preset-list                                  "split 2"]
        [button-rem-dwarf-preset                            ""]
        [top-menubar                                        ""]
        [prof-preset-list                                   "split 2"]
        [button-rem-prof-preset                             "wrap"]

        [(scrollable dwarf-list :id :dwarf-list-scrollable) "span 1 2, growy"]
        [(scrollable (mig-panel :id :main) :id :smain)      "grow"]
        [(scrollable prof-list  :id :prof-list-scrollable)  "span 1 2, growy, wrap"]

        [(label :id :status :text "Status Bar: ...")        ""]])))

(defn update-content! [frame puffballs]
  (let [content (make-content puffballs)]
    (doto frame
      (.setContentPane content)
      .invalidate
      .validate)))
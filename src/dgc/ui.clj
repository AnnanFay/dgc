(ns dgc.ui
  ""
  (:use [dgc util config read compat presets]
        [seesaw core table color mig font keystroke chooser]
        [seesaw.event :only [events-for]]
        [clojure.pprint]
        [cheshire.core])
  (:import [java.awt GraphicsEnvironment]
           [javax.swing ImageIcon])
  (:require [clojure.string :as s]))


;;;;
;;;; Full Screen Mode
;;;;

;TODO: Refactor!

(defn set-full-screen-frame! [f]
  (-> (GraphicsEnvironment/getLocalGraphicsEnvironment)
    .getDefaultScreenDevice
    (.setFullScreenWindow f)))

(defn set-full-screen-none! [f]
  (-> (GraphicsEnvironment/getLocalGraphicsEnvironment)
    .getDefaultScreenDevice
    (.setFullScreenWindow nil)))

(defn toggle-full-screen [e]
  (if (.isUndecorated (to-root e))
    ;disable full screen
    (do
      (doto (to-root e)
        .dispose
        (.setUndecorated false)
        (.setResizable true)
        set-full-screen-none!
        show!)
      ;(show! (select (to-root e) [:#dwarf-list-scrollable]))
      ;(show! (select (to-root e) [:#prof-list-scrollable]))
      )
    ;enable full screen
    (do
      (doto (to-root e)
        .dispose
        (.setUndecorated true)
        (.setResizable false)
        set-full-screen-frame!
        show!)
      ;(hide! (select (to-root e) [:#dwarf-list-scrollable]))
      ;(hide! (select (to-root e) [:#prof-list-scrollable]))
      )))


;;;;
;;;; Action Handlers
;;;;

(defn sort-by-name [e] (prn "sort-by-name" e) (alert "Sorry, not implemented"))
(defn sort-by-age  [e] (prn "sort-by-age" e)  (alert "Sorry, not implemented"))

(declare update-content!)

(defn load-export [e]
    (if-let [f (choose-file :dir "." :filters [["JSON Files" ["json"]]])]
      (update-content! (to-root e) (get-content f))))

(defn reload-export [e]
    (update-content! (to-root e) (get-content @export-filename)))

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

(def full-screen-action  (action  :name "Full Screen" :tip "Full Screen"     
                                  :mnemonic \f        :key (keystroke "F11")
                                  :handler toggle-full-screen))

;Dwarf list
(def sort-by-name-action      (action :name "Sort by Name" :handler sort-by-name))
(def sort-by-age-action       (action :name "Sort by Age"  :handler sort-by-age))

(def add-prof-preset-action   (action :name "Add selection as preset" :handler add-prof-preset))
(def add-dwarf-preset-action  (action :name "Add selection as preset" :handler add-dwarf-preset))

(def add-prof-action  (action :name "Add selection as preset" :handler add-prof))

;;;;
;;;; Menus
;;;;

(def file-menu (menu  :text "File"
                      :mnemonic \f
                      :items [load-export-action reload-export-action]))
(def view-menu (menu  :text "View"
                      :mnemonic \v
                      :items [full-screen-action]))
(def help-menu (menu  :text "Help"
                      :mnemonic \h
                      :items [show-help-action show-version-action]))

(def top-menubar (menubar :items [file-menu view-menu help-menu]))



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
  (.setBackground this (compat-colour obj))
  (if (float? obj)
    (.setText this (format "%.0f" obj))
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

(defn update-status! [root & msg]
  (let [s (select root [:#status])]
    (value! s (apply str msg))))

(defn selection-change [e]
  (let [root          (to-root e)
        dwarf-list  (select root [:#dwarf-list])
        prof-list   (select root [:#prof-list])
        puffballs   (selection dwarf-list {:multi? true})
        profs       (selection prof-list {:multi? true})
        profs       (remove keyword? profs)]
    (prn profs)
    (update-status! root "Dwarves: " (count puffballs) "/" (-> dwarf-list .getModel .getSize) ", Professions: " (count profs))
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
  (if (keyword? prof)
    (do
      (.setText this (str (s/capitalize (name prof))))
      (config! this :foreground :red))
    (.setText this (str (s/capitalize (name (first prof))))))
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
  (let [profs             (apply concat (map #(cons (first %) (sort (second %))) professions))
        prof-list         (listbox  :id       :prof-list
                                    :model    profs
                                    :renderer prof-list-renderer
                                    :popup    (fn [e] [sort-by-name-action sort-by-age-action add-prof-action add-prof-preset-action])
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
                                    :listen   [:selection (partial change-prof-preset profs)])
        rem-icon          (ImageIcon. "icons/list-remove-16.png")
        button-rem-dwarf-preset (button :margin 0 :icon rem-icon :listen [:action rem-dwarf-preset])
        button-rem-prof-preset  (button :margin 0 :icon rem-icon :listen [:action rem-prof-preset])]
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



(ns dgc.ui
  ""
  (:use [dgc util config read compat presets form]
        [seesaw table color mig font keystroke chooser]
        [seesaw.core :exclude [listbox tree]]
        [seesaw.event :only [events-for]]
        [clojure pprint walk]
        [cheshire.core])
  (:import [java.awt GraphicsEnvironment]
           [javax.swing ImageIcon UIManager]
           [javax.swing.plaf ColorUIResource])
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
      (update-content! (to-root e) f)))

(defn reload-export [e]
    (update-content! (to-root e) @export-filename))

(defn view-settings [e]
  (doto (frame 
          :title      "DGC - Settings" 
          :height     600
          :width      800
          :content    (mig-panel
                        :id :settings-panel
                        :constraints ["insets 0, fill" "" ""]
                        :items [[(title "Settings")    "dock north, shrink 0, growx"]
                               [(form {:a 1 :b 2 :c 3 :flag true :bar false} #(prn %1 %2))  ""]])
          :on-close   :exit)
    show!))

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

(def view-settings-action (action :name "View Settings" :tip "View Settings"     
                                  :mnemonic \s        :key (keystroke "F6")
                                  :handler view-settings))

;Dwarf list
(def sort-by-name-action      (action :name "Sort by Name"            :handler sort-by-name))
(def sort-by-age-action       (action :name "Sort by Age"             :handler sort-by-age))

(def add-prof-preset-action   (action :name "Add selection as preset" :handler add-prof-preset))
(def add-dwarf-preset-action  (action :name "Add selection as preset" :handler add-dwarf-preset))

(def add-prof-action          (action :name "Add new profession"      :handler add-prof))
(def rem-profs-action         (action :name "Remove selected"         :handler rem-profs))

;;;;
;;;; Menus
;;;;

(def file-menu (menu  :text "File"
                      :mnemonic \f
                      :items [load-export-action reload-export-action]))
(def view-menu (menu  :text "View"
                      :mnemonic \v
                      :items [full-screen-action view-settings-action]))
(def help-menu (menu  :text "Help"
                      :mnemonic \h
                      :items [show-help-action show-version-action]))

(def top-menubar (menubar :items [file-menu view-menu help-menu]))



;;;;
;;;; ???????????????????????????????????????????????
;;;;

(defn find-assoc [m k v]
  (postwalk #(if (and (map? %) (in? k (keys %))) (assoc % k v) %) m))

(defn set-prof [profession prof e]
  (let [prof-name   (first profession)
        old-prof    (second profession)
        prof-list   (select (to-root e) [:#prof-list])
        list-model  (-> prof-list .getModel)
        i           (.indexOf list-model profession)]

    ; edit in list
    (.set list-model i [prof-name prof])

    ; assoc professions
    (swap! professions #(find-assoc % prof-name prof))

    ; write to file
    (out "professions.clj" @professions)))

(defn profession-info [profession]
  (let [prof-name (first profession)
        prof      (second profession)]
   (mig-panel
      :id :main
      :constraints ["insets 0, fill" "" ""]
      :items [[(title prof-name)    "dock north, shrink 0, growx"]
             [(form prof #(set-prof profession %1 %2))  ""]])))

(def current-tab (atom 0))

; tabbed panel
(defn puffball-info [puffball]
  (let [general-puff  (dissoc puffball :soul :body :labors)
        attr-puff     {:physical  (-> puffball :body :physical)
                       :mental    (-> puffball :soul :mental)}
        trait-puff    (-> puffball :soul :traits)
        skill-puff    (-> puffball :soul :skills)
        labors-puff   (:labors puffball)
        tabs          [ {:title   "General"
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
                         :content (mig-panel)}
                        {:title   "Relations"
                         :content (mig-panel)}]
        tabbed-panel  (tabbed-panel
                        :placement :top
                        :overflow  :scroll
                        :tabs      tabs)]

  (selection! tabbed-panel @current-tab)

  (listen tabbed-panel :change (fn [e] (swap! current-tab (fn [t] (:index (selection (.getSource e)))))))

  (mig-panel
    :constraints ["insets 0, fill" "" ""]
    :items [[(title (get-full-name puffball)) "dock north, shrink 0, growx"]
            [tabbed-panel                     "grow"]])))

(defn compat-info [puffball prof]
    (mig-panel
        :id :main
        :constraints ["insets 0" "" ""]
        :items [[(title (get-full-name puffball))       "dock north, shrink 0, growx"]
                [(label :text (compat-tooltip (compat puffball (second prof))))]]))


(defn puffball-compats [puffball profs]
  (let [compats        (map #(vector (compat puffball (second %)) (first %)) profs)
        sorted-compats (reverse (sort-by :total compats ))]
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
  (UIManager/put "ToolTip.background" (ColorUIResource. 255 255 255))
  (let [total (int (:total obj))]
    (doto this
      (.setBackground   (compat-colour total))
      (.setText         (str total))
      (.setHorizontalAlignment (javax.swing.JLabel/CENTER))
      (.setToolTipText  (compat-tooltip obj)))))

(defn compat-header [compats]
  (conj
    (key-seq-to-header  (keys (dissoc compats :name))
                        { :vertical true
                          :width    30
                          :renderer compat-cell-renderer})
    { :key :name
      :text "Name"
      :width 216}))

(defn puffball-compats-table [puffballs profs]
  ;(prn puffballs profs)
  (let [data    (map-prod #(vector (first %2) (compat %1 %2)) puffballs profs)
        data    (map vv-to-map data)
        data    (map #(merge %1 (hash-map :name (get-full-name %2))) data puffballs)
        ;data    (map #(hash-map :foo %) (range 0 0.6 0.10001))
        tablith (make-table data identity compat-header 30)]
  (mig-panel
    :id :main
    :constraints ["insets 0, fill" "" "[pref!]r[]"]
    :items [[(title  "Compatability Matrix")       "wrap"]
            [(scrollable tablith) "grow"]])))


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


(defn no-combination []
    (mig-panel
        :id :main
        :constraints ["insets 0" "" ""]
        :items [[(title "No Combination")       "dock north, shrink 0, growx"]]))

(defn selection-change [e]
  (let [root          (to-root e)
        dwarf-list  (select root [:#dwarf-list])
        prof-list   (select root [:#prof-list])
        puffballs   (selection dwarf-list {:multi? true})
        profs       (selection prof-list {:multi? true})
        profs       (remove keyword? profs)]
    (update-status! root "Dwarves: " (count puffballs) "/" (-> dwarf-list .getModel .getSize) ", Professions: " (count profs))
    (cond
      (empty? profs) (cond
              (empty? puffballs)        (rep! root (no-combination))
              (= (count puffballs) 1)   (rep! root (puffball-info (first puffballs)))
              :else                     (rep! root (no-combination)))
      (= (count profs) 1) (cond
              (empty? puffballs)        (rep! root (profession-info (first profs)))
              (= (count puffballs) 1)   (rep! root (compat-info (first puffballs) (first profs)))
              :else                     (rep! root (no-combination)))
      :else (cond
              (empty? puffballs)        (rep! root (no-combination))
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

(def male-icon    (ImageIcon. "icons/male-16.png"))
(def female-icon  (ImageIcon. "icons/female-16.png"))

(defn sex-icon [s]
  (if (= s 1) male-icon female-icon))

(defn prof-list-renderer [this {prof :value}]
  (if (keyword? prof)
    (do
      (.setText this (str (s/capitalize (name prof))))
      (config! this :foreground :red))
    (.setText this (str (s/capitalize (name (first prof))))))
  (config! this :font (font :size 12))
  this)

(defn puffball-list-renderer [this {puffball :value}]
  (doto this
    (.setIcon (sex-icon (puffball :sex)))
    (.setText (get-full-name puffball))
    (config! :font (font :size 12))))

(defn preset-list-renderer [this {preset :value}]
  (.setText this (str (first preset)))
  this)


(defn make-content [puffball-filename]
  (let [prof-list         (listbox  :id       :prof-list
                                    :model    (fn [] (apply concat (map #(cons (first %) (sort (second %))) @professions)))
                                    :renderer prof-list-renderer
                                    :popup    (fn [e] [ sort-by-name-action
                                                        sort-by-age-action
                                                        add-prof-action
                                                        rem-profs-action
                                                        add-prof-preset-action])
                                    :listen   [:selection prof-selection-change])
        dwarf-list        (listbox  :id       :dwarf-list
                                    :model    #(get-content puffball-filename)
                                    :renderer puffball-list-renderer
                                    :popup    (fn [e] [sort-by-name-action sort-by-age-action add-dwarf-preset-action])
                                    :listen   [:selection puffball-selection-change])
        default-presets   {:none nil}
        dwarf-preset-list (combobox :id       :dwarf-presets
                                    :model    (conj (get-presets "dwarves") default-presets)
                                    :renderer preset-list-renderer
                                    :listen   [:selection change-dwarf-preset])
        prof-preset-list  (combobox :id       :prof-presets
                                    :model    (conj (get-presets "profs") default-presets)
                                    :renderer preset-list-renderer
                                    :listen   [:selection change-prof-preset])
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

(defn update-content! [frame puffball-filename]
  (let [content   (make-content puffball-filename)]
    (doto frame
      (.setContentPane content)
      .invalidate
      .validate)))



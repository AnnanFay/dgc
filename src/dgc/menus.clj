(ns dgc.menus
  ""
  (:use [dgc util presets read]
        [seesaw core keystroke chooser]
        [cheshire.core])
  (:import java.awt.GraphicsEnvironment))


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

(defn load-export [e]
    (if-let [f (choose-file :filters [["JSON Files" ["json"]]])]
        (alert f)))

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


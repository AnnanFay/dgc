(ns dgc.core
  ""
  (:use [dgc ui read util]
        [seesaw.core :exclude [listbox tree]])
  (:gen-class))

(native!)

(defn -main [& args]
  (let [height    800
        width     (* height 1.618)
        on-close  (if (in? :hide args) :hide :exit)
        f         (frame 
                    :title      "Dwarven Guidance Councilor" 
                    :height     height
                    :width      width
                    :on-close   on-close)]

        (show! f)

        ; load puffballs
        (invoke-later (update-content! f "Dwarves.json"))
    
    ; some initial selections for easy debugging
    ;(selection! (select f [:#dwarf-list]) {:multi? true} [(first puffballs)])
    ;(selection! (select f [:#dwarf-list]) {:multi? true} [(first puffballs) (second puffballs)])

    ;return the frame
    f))

; If called on the command line
(if *command-line-args*
  (apply -main *command-line-args*))

(defn run []
  (do 
    (use '[dgc compat config core form presets read ui util] :reload)
    (-main :hide)))
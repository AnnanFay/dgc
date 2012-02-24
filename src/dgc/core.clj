(ns dgc.core
  ""
  (:use [dgc ui util config read compat presets menus]
        [seesaw core])
  (:gen-class))

(native!)

(defn -main [& args]
  (let [puffballs []
        height    800
        content   (make-content puffballs)]
    (doto (frame 
          :title      "Dwarven Guidance Councilor" 
          :height     height
          :width      (* height 1.618)
          :content    content
          :on-close   :hide) ;:exit)
        show!
        ; load puffballs
        (update-content! "Dwarves.json"))
    
    ;(selection! dwarf-list {:multi? true} [(first puffballs)])
    ;(selection! prof-list {:multi? true} [(first professions) (second professions)])
))

; If called on the command line
(if *command-line-args*
  (apply -main *command-line-args*))

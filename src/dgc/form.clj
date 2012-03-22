(ns dgc.form
  "Generates forms for structures"
  (:use [dgc util config read presets]
        [seesaw core table color mig font keystroke chooser]
        [seesaw.event :only [events-for]]
        [clojure.pprint]
        [cheshire.core])
  (:require [clojure.string :as s]))

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

(defn html-ul [s]
  (apply str
  	(cond (map? s)  (interleave
							        (map #(str "<p>" (html-ul %) " ") (keys s))
							        (map #(str (html-ul %) "</p>") (vals s)))
			    (coll? s) (map #(str "<p>" (html-ul %) "</p>") s)
			    :else 		(key-title s))))

(defn html [s]
  (str "<html><b>" (html-ul s) "</b></html>"))

; TODO: Think of a good name
(defn foo [thingie]
  (vector
    (vector (label :text (key-title (first thingie))) "")
    (vector (text :id (first thingie) :columns 72 :text (str (second thingie))) "wrap")))

(defn form-content [m]
  (mig-panel
    :constraints  ["insets 0" "r[]r[]r" ""]
    :items        (reduce #(into %1 (foo %2)) [] m)))

(defn form-to-obj [m e]
  (let [source (.getSource e)
        parent (.getParent source)
        inputs (select parent [:JTextField])
        inputs (apply hash-map (interleave (map id-of inputs) (map #(.getText %) inputs)))
        obj    (map-map #(or (read-string ((first %) inputs)) (second %)) m)]
    obj))

;returns a form  that will call the callback with the modified version if it's modified
(defn form [m callback]  
  (mig-panel
    :constraints  ["insets 0" "r[center]r" "r[center]r"]
    :items        [ [(form-content m)                  "wrap"]
                    [(button :text "Save!" :listen [:action #(callback (form-to-obj m %) %)]) ""]]))
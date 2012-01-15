(ns DGC.core
  ""
  (:use [seesaw core table]
        seesaw.color
        [cheshire.core]))

(defn header [k]
  {:key k :text (name k)})

(defn to-dwarf [dwarf]
  dwarf)

(defn to-info [dwarf]
  {:name (str (:First (:Name dwarf)) " " (:TranName (:Name dwarf)))
   :sex  (:Sex dwarf)
   :profession (:Profession dwarf)})

(defn make-table [data to-row]
  (table :id :table
    :model [
      :columns (map header (keys (to-row (first data))))
      :rows (map to-row data)]))


(defn -main
  []
  (let [height 400
        data (parse-string (slurp "Dwarves.json") true)]
    (-> (frame 
          :title    "Dwarven Guidance Councilor" 
          :height   height
          :width    (* height 1.618)
          :content  (border-panel
                      ;:north (text :multi-line? true :wrap-lines? false :rows 3 :text (str data))
                      :north (scrollable (make-table data to-info))
                      :center (scrollable (make-table data to-dwarf))
                      :south  (label :id :sel :text "Selection: "))
          :on-close :exit)
        pack!
        show!)))
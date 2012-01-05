(ns DGC.core
  ""
  (:use [seesaw core table]
        seesaw.color
        [cheshire.core]))

(defn make-table-example []
  (table :id :table
    :model [
      :columns [ { :key :name :text "Name" } 
                 { :key :town :text "Town" } 
                 { :key :interest :text "Interest" }]
      :rows [{ :name "Kupzog" :town "Cologne" :interest "programming" :id 1234}
             { :name "Hansson" :town "Ystadt" :interest "Hunting" :id 2234}
             { :name "Walter" :town "London" :interest "Rafting" :id 12345}]]))

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
        data (parse-string (slurp "Dwarves.json") true)
        test-data [{ :name "Kupzog" :town "Cologne" :interest "programming" :id 1234}
                   { :name "Hansson" :town "Ystadt" :interest "Hunting" :id 2234}
                   { :name "Walter" :town "London" :interest "Rafting" :id 12345}]]
    (-> (frame 
          :title    "Dwarven Guidance Councilor" 
          :height   height
          :width    (* height 1.618)
          :content  (border-panel
                      ;:north (text :multi-line? true :wrap-lines? false :rows 3 :text (str data))
                      :north  (scrollable (make-table test-data to-dwarf))
                      :center (scrollable (make-table data to-info))
                      :south  (label :id :sel :text "Selection: "))
          :on-close :exit)
        pack!
        show!)))
(ns dgc.compat
  ""
  (:use [dgc util form])
  (:import  [java.awt Component Color Dimension]
            [javax.swing JFrame UIManager]
            [javax.swing.plaf ColorUIResource])
            ;[org.jfree.chart ChartPanel]
  (:use
    ;[incanter core stats charts]
    [seesaw color]
    [clojure pprint]))

(defrecord Compat [prof-ref puff-id skills attributes traits labors total]
  java.lang.Comparable
    (compareTo [this other]
      (compare total (:total other))))

(def compat-bounds (atom {}))

(defn average [data]
  (if (empty? data)
    0
    (/ (apply + data) (count data))))

(defn compat-tooltip [compat]
  ;(html compat)
  (str "<html>
        <h2>" (key-title (:prof-ref compat)) " - " (percent (:total compat)) "</h3>
        <p>
            <strong>Attributes:</strong>
            <ul>"
                (apply str (map #(str "<li>[" (int (* (second %) 50)) "] " (key-title (first %)) " -> " (percent (second %)) "</li>") (:attributes compat)))
            "</ul>
        </p>
        <p>
            <strong>Skills:</strong>
            <ul>"
                (apply str (map #(str "<li>" (key-title (first %)) " - " (second %) "</li>") (:skills compat)))
            "</ul>
        </p>
        <p>
            <strong>Traits:</strong>
            <ul>"
                (apply str (map #(str "<li>" (key-title (first %)) " - " (second %) "</li>") (:traits compat)))
            "</ul>
        </p>
    </html>"))

(defn ajust-bounds
  ""
  [prof-key total]
  (swap! compat-bounds
    #(cond
        (not (contains? % prof-key))
          (assoc % prof-key [total total])
        (< total (get-in % [prof-key 0])) ;lt min
          (assoc-in % [prof-key 0] total)
        (> total (get-in % [prof-key 1])) ;gt max
          (assoc-in % [prof-key 1] total)
        :else %)))

; returns a Compat campatability between dwarf and profession
(defn compat [puffball prof]
  ; Select all dwarf attributes that are in the profession
  (let [all-attributes  (merge (-> puffball :soul :mental) (-> puffball :body :physical))
        all-skills      (-> puffball :soul :skills)
        all-traits      (-> puffball :soul :traits)
        all-labors      (-> puffball :labors)

        prof-attributes (select-keys all-attributes (:attributes  prof))
        prof-skills     (select-keys all-skills     (:skills      prof))
        prof-traits     (select-keys all-traits     (:traits      prof))
        prof-labors     (select-keys all-labors     (:labors      prof))

        attr-scores     (map-map #(double (/ (second %) 50)) prof-attributes)
        skill-scores    prof-skills
        trait-scores    prof-traits
        labor-scores    prof-labors

        total           (average (vals attr-scores))]

    (ajust-bounds (:name prof) total)
    
    ;(prn :compat)
    ;(pprint all-skills)
    ;(pprint (:skills prof))
    ;(pprint trait-scores)

    (->Compat (:name prof) :puff-id skill-scores attr-scores trait-scores labor-scores total)))

(defn compat-colour
  "Maps 0->100 to red->orange->yellow->green->blue rainbow [java.awt.Color]."
  [compat]
  (if (number? compat)
    (Color. (Color/HSBtoRGB (/ compat 166) 1 1))))

(defn compat-cell-renderer [this compat]
  (UIManager/put "ToolTip.background" (ColorUIResource. 255 255 255))
  ;(prn (:name compat) @compat-bounds)
  (let [total (:total compat)
        total (map-range ((:prof-ref compat) @compat-bounds) [0 100] total)
        total (int total)]
    (doto this
      (.setBackground           (compat-colour total))
      (.setText                 (str total))
      (.setHorizontalAlignment  (javax.swing.JLabel/CENTER))
      (.setToolTipText          (compat-tooltip compat)))))



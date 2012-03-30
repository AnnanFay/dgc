(ns dgc.puffball
  ""
  (:use [dgc util read]))


(defn puffball-tooltip [puffball]
  (str "<html>
    <h2>" (get-full-name puffball) "</h3>
    <p>
      <strong>Attributes:</strong>
      <ul>"
        (apply str (map #(str "<li>" (key-title (first %))  "</li>") (:attributes puffball)))
      "</ul>
    </p>
    <p>
      <strong>Skills:</strong>
      <ul>"
        (apply str (map #(str "<li>" (key-title (first %)) "</li>") (:skills puffball)))
      "</ul>
    </p>
    <p>
      <strong>Traits:</strong>
      <ul>"
        (apply str (map #(str "<li>" (key-title (first %)) "</li>") (:traits puffball)))
      "</ul>
    </p>
    </html>"))

(defn puffball-cell-renderer [this puffball]
  (doto this
    (.setText                 (get-full-name puffball))
    (.setHorizontalAlignment  (javax.swing.JLabel/CENTER))
    (.setToolTipText          (puffball-tooltip puffball))))
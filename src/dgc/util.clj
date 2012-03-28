(ns dgc.util
  "A few generic often used functions"
  (:use [seesaw [core :exclude [listbox tree]] table color mig font options]
        [clojure.pprint])
  (:require [clojure.string :as s])
  (:import [javax.swing.table TableRowSorter DefaultTableCellRenderer AbstractTableModel]))


;;;;
;;;; Serialization
;;;;
(def save-dir "data")

; Creates parent directories
(defn mkpath
  "Creates parent directories of filename."
  [filename]
  (.mkdirs (.getParentFile (java.io.File. filename)))
  filename)

(defn out
  "Save a clojure form to a file."
  [filename data]
  (spit (mkpath (str save-dir "/" filename)) (with-out-str (pprint data)))
  filename)

; Reads data from file, returning it
(defn in
  "Load a clojure form from file."
  [filename & [default]]
  (try
    (read-string (slurp (str save-dir "/" filename)))
    (catch Exception e
      (or default (throw e)))))

;;;;
;;;; Utilities
;;;;

; Like map but works on a maps values leaving keys unchanged
(defn map-vals [f m]
    (zipmap (keys m) (map f (vals m))))

; Like map-vals but passes in [key val]
(defn map-map [f m]
    (zipmap (keys m) (map f m)))

(defn not-nil? [foo]
  (not (nil? foo)))

(defn in? 
  "true if seq contains elm"
  [elm seq]  
  (some #(= elm %) seq))


(defn bool?
  "True if a boolean"
  [x]
  (= (type x) java.lang.Boolean))

;;;
;;; Tables
;;; 

; Make a table out of a list of maps
(defn base-header [k]
  {:key k :text (s/capitalize (name k))})

; [:foo :bar :baz] -> [{:key :foo :text "Foo" } {...}]
(defn key-seq-to-header [key-seq & [extend]]
  (map #(merge extend (base-header %)) (remove nil? key-seq)))

(defn map-to-header [m & [extend]]
  (map #(merge extend (hash-map :key %1 :text (s/capitalize %2)))
        (keys m) (vals m)))

(defn set-column-renderers [col head]

  (if (:vertical head)
    (.setHeaderRenderer col (new darrylbu.renderer.VerticalTableHeaderCellRenderer)))
  
  (if (not-nil? (:width head))
    (.setPreferredWidth col (:width head)))

  (if (not-nil? (:renderer head))
    (.setCellRenderer col (proxy [DefaultTableCellRenderer] []
                            (getTableCellRendererComponent [tbl obj isSelected hasFocus r c]
                              ((:renderer head) this obj))))))

(defn sortTableColumns [table row-index]
  (let [col-num     (.getColumnCount table)
        col-model   (-> table .getColumnModel)
        row         (map #(.getValueAt table row-index %) (range col-num))
        columns     (enumeration-seq (-> col-model .getColumns))
        foo         (rest (map vector row columns))
        sorted      (reverse (sort-by #(:total (first %)) foo))
        sorted-cols (map second sorted)]
    ; remove all columns
    (doall (map #(.removeColumn col-model %) sorted-cols))

    ; add all calumns
    (doall (map #(.addColumn col-model %) sorted-cols))))

(defn make-table [data to-row & [to-header row-height]]
  (let [header  (if (nil? to-header)
                  (key-seq-to-header  (keys (first data)))
                  (to-header          (first data)))
        model   (proxy [AbstractTableModel] []
                  (getColumnCount []        (count header))
                  (getRowCount    []        (count data))
                  (getColumnName  [col]     (:text (nth header col)))
                  (getColumnClass [column]
                    (.getClass (.getValueAt this 0 column)))
                  (getValueAt     [row col]
                    ((:key (nth header col)) (nth data row))))
        table   (table :model model)
        columns (enumeration-seq (-> table .getColumnModel .getColumns))]

    ; Set row height
    (if (not-nil? row-height)
      (.setRowHeight table row-height))

    ; Enable Row Sorting
    (.setAutoCreateRowSorter table true)

    ; Add Column Sorting
    (listen table :mouse-clicked  (fn [e]
                                    (let [point (.getPoint e)
                                          row   (.rowAtPoint table point)
                                          col   (.columnAtPoint table point)]
                                      (prn :point point :row row)
                                      (if (= col 0)
                                        (sortTableColumns table row)))))

    ; So we don't end up with rediculous column sizes
    (.setAutoResizeMode table javax.swing.JTable/AUTO_RESIZE_OFF)

    ; Add the custom column renderers
    (doall (map set-column-renderers columns header))
    
    table))


;;;;
;;;; Seesaw
;;;;

; make a tree but update content later
(defn tree
  [& args]
  ;(prn :listbox-args args)
  (if (fn? (:model (apply hash-map args)))
    (let [hargs         (apply hash-map args)
          model-handler (:model hargs)
          args          (reduce into (seq (dissoc hargs :model)))
          t             (apply seesaw.core/tree args)]

      (invoke-later (apply-options t [:model (model-handler)]))
      t)
    (apply seesaw.core/tree args)))


; make a listbox but update content later
(defn listbox
  [& args]
  ;(prn :listbox-args args)
  (if (fn? (:model (apply hash-map args)))
    (let [hargs         (apply hash-map args)
          model-handler (:model hargs)
          args          (reduce into (seq (dissoc hargs :model)))
          lb            (apply seesaw.core/listbox args)]

      (invoke-later (apply-options lb [:model (model-handler)]))
      lb)
    (apply seesaw.core/listbox args)))

;;;;
;;;; Formatting
;;;;

(defn key-title [k]
  (if (keyword? k)
    (s/capitalize (name k))
    (str k)))

(defn percent [p]
  (format "%.2f%%" p))

;;;;
;;;; Swing
;;;;

(defn get-model-elements [m]
  (for [x (range (.getSize m))] (.getElementAt m x)))
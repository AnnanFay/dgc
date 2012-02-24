(ns dgc.util
  "A few generic often used functions"
  (:use [seesaw core table color mig font])
  (:require [clojure.string :as s]))

;;;;
;;;; Utilities
;;;;

; Like map but works on a maps values leaving keys unchanged
(defn map-vals [f m]
    (zipmap (keys m) (map f (vals m))))

(defn not-nil? [foo]
  (not (nil? foo)))

(defn in? 
  "true if seq contains elm"
  [elm seq]  
  (some #(= elm %) seq))


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
  (.setHeaderRenderer col (new darrylbu.renderer.VerticalTableHeaderCellRenderer))
  
  (if (not-nil? (:width head))
    (.setMaxWidth col (:width head)))

  (if (not-nil? (:renderer head))
    (.setCellRenderer col (proxy [javax.swing.table.DefaultTableCellRenderer] []
                            (getTableCellRendererComponent [tbl obj isSelected hasFocus r c]
                              ((:renderer head) this obj))))))

(defn make-table [data to-row & [to-header]]
  (let [header  (if (nil? to-header)
                  (key-seq-to-header  (keys (first data)))
                  (to-header          (first data)))
        table   (table  :id           :table
                        :auto-resize  :off
                        :model        [ :columns  header
                                        :rows     (map to-row data)])
        columns   (enumeration-seq (-> table .getColumnModel .getColumns))]
    (.setAutoCreateRowSorter table true)
    (doall (map set-column-renderers columns header))
    ;(.setBackground table (color 255 142 10))
    ;setForeground
    table))
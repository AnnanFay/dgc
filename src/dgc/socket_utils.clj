(ns dgc.socket-utils
  (:use [protobuf core])
  (:import (java.nio ByteBuffer ByteOrder)))

; Strings

(defn pad
  [x n]
  (cond
    (> n (.length (str x)))
      (pad (str 0 x) n)
    (< n (.length (str x)))
      (pad (subs x 1) n)
    :else
      (str x)))

; Maths

(defn abs [n] (if (neg? n) (- n) n))

; Swapping Endianness

(defn endify-int
  "Switch the endian-ness of an int."
  [i]
  (-> (ByteBuffer/allocate 4)
    (.order ByteOrder/BIG_ENDIAN)
    (.putInt i)
    (.order ByteOrder/LITTLE_ENDIAN)
    (.getInt 0)))

(defn endify-short
  "Switch the endian-ness of an int."
  [i]
  (-> (ByteBuffer/allocate 4)
    (.order ByteOrder/BIG_ENDIAN)
    (.putShort i)
    (.order ByteOrder/LITTLE_ENDIAN)
    (.getShort 0)))

; Byte Buffers

(defn byte-buffer-seq [b]
  (map #(.get b %) (range (.limit b))))

(defn byte-buffer
  [s]
  (loop [s s buffer (ByteBuffer/allocate (count s))]
    (if (empty? s)
      buffer
      (recur (rest s) (.put buffer (- (.capacity buffer) (count s)) (first s))))))

; Protobufs

(defn bytes-proto
  [proto-class bytes]
    (protobuf-load (protodef proto-class) (byte-array bytes)))

(defn proto-bytes
  "Given a proto class and map of values will return a sequence of bytes representing its serialization."
  [proto-class m]
  ;(prn proto-class m (vec (protobuf-dump (apply (partial protobuf (protodef proto-class)) (reduce into [] m)))))
  (vec (protobuf-dump (apply (partial protobuf (protodef proto-class)) (reduce into [] m)))))
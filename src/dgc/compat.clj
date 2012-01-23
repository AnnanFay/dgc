(ns dgc.compat
  ""
  (:import [org.jfree.chart ChartPanel])
  (:import [java.awt Component Dimension])
  (:import [javax.swing JFrame])
  (:use [incanter core stats charts]))



(defn avgattrtopct [x] 
    (cond
        (<= x 749)
            (- (* 0.030357 x) 6.0714)
        (and (>= x 750) (<= x 899))
            (- (* 0.111852 x) 67.2223)
        (and (>= x 900) (<= x 999))
            (- (* 0.168333 x) 118.166)
        (and (>= x 1000) (<= x 1099))
            (- (* 0.168343 x) 118.343)
        (and (>= x 1100) (<= x 1299))
            (- (* 0.0837538 x) 25.4621) 
        (>= x 1300)
            (+ (* 0.0238412 x) 52.3404)))

(defn minusattrtopct [x]
    (cond
    (<= x 599)
        (- (* 0.0370356 x) 5.51835)
    (and (>= x 600) (<= x 799))
        (- (* 0.0837487 x) 33.5822)
    (and (>= x 800) (<= x 899))
        (- (* 0.168333 x) 101.333)
    (and (>= x 900) (<= x 999))
        (- (* 0.168343 x) 101.509)
    (and (>= x 1000) (<= x 1099))
        (- (* 0.168343 x) 101.676)
    (>= x 1100)
        (+ (* 0.0417669 x) 37.3904)))

(defn plusattrtopct [x]
    (cond
    (<= x 949)
        (- (* 0.0333988 x) 15.0295)
    (and (>= x 950) (<= x 1149))
        (- (* 0.0837487 x) 62.8943)
    (and (>= x 1150) (<= x 1249))
        (- (* 0.168333 x) 160.249)
    (and (>= x 1250) (<= x 1349))
        (- (* 0.168343 x) 160.429)
    (and (>= x 1350) (<= x 1549))
        (- (* 0.0837538 x) 46.4006)
    (>= x 1550)
        (+ (* 0.0238412 x) 46.3801)))

(defn dubplusattrtopct [x]
    (cond
    (<= x 1199)
        (- (* 0.0333988 x) 23.3792)
    (and (>= x 1200) (<= x 1399))
        (- (* 0.0837487 x) 83.8315)
    (and (>= x 1400) (<= x 1499))
        (- (* 0.168333 x) 202.333)
    (and (>= x 1500) (<= x 1599))
        (- (* 0.168343 x) 202.515)
    (and (>= x 1600) (<= x 1799))
        (- (* 0.0837538 x) 67.339)
    (>= x 1800)
        (+ (* 0.0238412 x) 40.4198)))

(def attributeFunctionMap {
	:strength plusattrtopct
	:agility minusattrtopct
	:toughness plusattrtopct
	:endurance avgattrtopct
	:disease-resistance avgattrtopct
	:recuperation avgattrtopct
	:analytical-ability plusattrtopct
	:memory plusattrtopct
	:creatvity plusattrtopct
	:intuition avgattrtopct
	:focus dubplusattrtopct
	:willpower avgattrtopct
	:patience plusattrtopct
	:spatial-sense dubplusattrtopct
	:kinaesthetic-sense avgattrtopct
	:linguistic-ability avgattrtopct
	:musicality avgattrtopct
	:empathy avgattrtopct
	:social-awareness avgattrtopct
})

(defn average [data]
  (if (empty? data)
    0
    (/ (apply + data) (count data))))

(defn compat [puffball prof]
  (let [attributes      (merge (-> puffball :soul :mental) (-> puffball :body :physical))
        prof-attributes (select-keys attributes (:attributes prof))]
    ;(prn attributes prof-attributes)
    (average (map #(((first %) attributeFunctionMap) (second %)) prof-attributes ))))


(def test-chart (ChartPanel. (doto (function-plot  avgattrtopct     0 5000)
                                   (add-function   plusattrtopct    0 5000)
                                   (add-function   minusattrtopct   0 5000)
                                   (add-function   dubplusattrtopct 0 5000))))



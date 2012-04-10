(ns dgc.read
  ""
  (:use [dgc config settings]
        [cheshire.core]
        [clojure.java.shell :only [sh]])
  (:require [clojure.string :as s]))

;;;;
;;;; Restructuring the JSON input
;;;;

(defn get-value [data p]
  (if (nil? p)
    nil
    ((keyword p) data)))

(defn get-body [data body]
  {:physical (zipmap  physical-attributes
                      (map #(:unk1 (get-value data %)) (:physical_attrs body)))})

(defn get-skills [data raw-soul]
  (let [soul        (get-value data raw-soul)
        raw-skills  (:skills soul)
        skills      (map (partial get-value data) raw-skills)]
  (zipmap (map #(keyword (clojure.string/replace (clojure.string/lower-case (:id %)) "_" "-")) skills) (map :rating skills))))
          

(defn get-soul [data raw-soul]
  { :traits       (zipmap traits
                          (->> raw-soul (get-value data) :traits))
    :skills       (get-skills data raw-soul)
    :mental       (zipmap mental-attributes
                    (map #(:unk1 (get-value data %)) (->> raw-soul (get-value data) :mental_attrs)))
    :preferences  (->> raw-soul (get-value data) :preferences)})

(defn get-full-name [puffball]
  (str (-> puffball :name :first) " " (-> puffball :name :nick) " " (-> puffball :name :last)))

(defn get-name [data puffball]
  (let [name (->> puffball :status :current_soul (get-value data) :name (get-value data))]
    { :first  (s/capitalize (:first_name name))
      :last   (s/capitalize (s/join " " (:translation name)))
      :nick   (s/capitalize (:nickname name))}))

(defn get-puffball [data raw-puffball]
  (let [puffball (get-value data raw-puffball)]
    {
      :id         (:id puffball)
      :name       (get-name data puffball)
      :race       (:race_name puffball)
      :civ        (:civ_id puffball)
      :sex        (:sex puffball)
      :age        (-> puffball :relations :old_year)
      :profession (or (:custom_profession puffball) (:profession puffball))
      :labors     (dissoc (zipmap labors
                          (-> puffball :status :labors)) nil)
      :happiness  (-> puffball :status :happiness)
      :appearance (-> puffball :appearance)
      :body       (get-body data (:body puffball))
      :soul       (get-soul data (-> puffball :status :current_soul))}))

(defn get-puffballs [data raw-puffballs]
  (map (partial get-puffball data) raw-puffballs))

(defn get-file-data [filename]
  (if filename
    (update-setting! :export-filename filename))

  (parse-string (slurp (@settings :export-filename)) true))

(defn get-dfhack-data []
  ; FIXME: what should the path be?
  ;(sh "../df_34_07_win_s/dfhack-run.exe" "prospect" "all")
  (:out (sh "../df_34_07_win_s/dfhack-run.exe" "dgc" "raw")))

(defn get-content [& [filename]]
  (let [data          (if (@settings :load-from-file)
                        (get-file-data filename)
                        (get-dfhack-data))
        raw-puffballs (:root data)
        puffballs     (get-puffballs data raw-puffballs)
        puffballs     (filter #(= (:race %) "DWARF") puffballs)]
    puffballs))




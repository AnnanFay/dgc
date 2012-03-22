(ns dgc.presets
  ""
  (:use [dgc util config]
        [seesaw.core]
        [cheshire.core]
        [clojure.walk])
  (:import [java.io File]))

;;;; Preset handlers

; get presets from a file, returns presets
(defn get-presets [filename]
  (in filename {}))

; writes presets to file
; returns presets
(defn set-presets [filename presets]
  (out filename presets)
  presets)

; appends preset to preset file
; returns preset
(defn append-preset [filename preset]
  (try
    (set-presets filename (assoc (get-presets filename) (first preset) (second preset)))
    (catch Exception e
      (prn e)
      (set-presets filename {(first preset) (second preset)})))
  preset)


; retrieves the preset selection and asks the user for a preset name
; writes this info to file and adds the preset to the combobox
(defn add-preset [e list-id plist-id filename get-id]
  (let [preset-list     (select (to-root e) [plist-id])
        selection-list  (select (to-root e) [list-id])
        selected        (selection selection-list {:multi? true})
        preset          (map get-id selected)
        preset-dialog   (dialog :content (flow-panel  :items ["Enter preset name" (text :id :preset-name :columns 32)])
                              :option-type :ok-cancel
                              :success-fn (fn [p]
                                (let [preset [(text (select (to-root p) [:#preset-name])) preset]]
                                  ; write preset to file
                                  (append-preset filename preset)
                                  ;add preset to combobox
                                  (.addItem preset-list preset))))]
    ; display dialog
    (-> preset-dialog pack! show!)))

(defn rem-preset [e plist-id filename]
  (let [preset-list     (select (to-root e) [plist-id])
        current-preset  (selection preset-list)]

    ; write preset to file
    (set-presets filename (dissoc (get-presets filename) (first current-preset)))

    ;remove preset from combobox
    (.removeItem preset-list current-preset)))

(defn add-prof-preset [e]
  (add-preset e :#prof-list :#prof-presets "profs" first))

(defn rem-prof-preset [e]
  (rem-preset e :#prof-presets "profs"))

(defn add-dwarf-preset [e]
  (add-preset e :#dwarf-list :#dwarf-presets "dwarves" :id))

(defn rem-dwarf-preset [e]
  (rem-preset e :#dwarf-presets "dwarves"))

(defn add-prof [e]
  (prn "adding prof")
  (let [prof-list (select (to-root e) [:#prof-list])
        prof      [ :new-profession
                    {:traits {},
                     :skills [],
                     :attributes []}]]
    ; add to interface
    (-> prof-list 
        .getModel
        (.addElement prof))
    ; update professions
    (swap! professions #(assoc % :misc (assoc (:misc %) (first prof) (second prof))))
    ; write to file
    (out "professions.clj" @professions)))

; recursivly search map for a key and dissoc it
(defn find-dissoc-all [m ks]
  (postwalk #(if (map? %) (apply (partial dissoc %) ks) %) m))

(defn rem-profs [e]
  (let [prof-list       (select (to-root e) [:#prof-list])
        list-model      (-> prof-list .getModel)
        selected        (selection prof-list {:multi? true})
        selected-profs  (filter vector? selected)
        selected-keys   (map first selected-profs)]
    (prn selected-keys)
    ; remove from list
    (doall (map #(.removeElement list-model %) selected-profs))
    ; remove from professions
    (swap! professions #(find-dissoc-all % selected-keys))
    ; write to file
    (out "professions.clj" @professions)))

;TODO: Refactor these
(defn change-dwarf-preset [puffballs e]
  (let [source          (.getSource e)
        selected        (second (selection source))
        selected-dwarfs (filter #(in? (:id %) selected) puffballs)
        preset          (second selected)
        selection-list  (select (to-root e) [:#dwarf-list])]
    (selection! selection-list {:multi? true} selected-dwarfs)))

(defn change-prof-preset [profs e]
  (let [profs           (remove keyword? profs)
        source          (.getSource e)
        selected        (second (selection source))
        selected-profs  (filter #(in? (first %) selected) profs)
        preset          (second selected)
        selection-list  (select (to-root e) [:#prof-list])]
    (selection! selection-list {:multi? true} selected-profs)))

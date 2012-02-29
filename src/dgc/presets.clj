(ns dgc.presets
  ""
  (:use [dgc util]
        [seesaw.core]
        [cheshire.core]))

;;;; Preset handlers

; get presets from a file
(defn get-presets [filename]
  (try
    (parse-string (slurp (str "presets/" filename ".json")) true)
    (catch Exception e {})))

; writes presets to file
; returns presets
(defn set-presets [filename presets]
  (spit (str "presets/" filename ".json") (generate-string presets))
  presets)

; appends preset to preset file
; returns preset
(defn append-preset [filename preset]
  (try
    (set-presets filename (assoc (get-presets filename) (first preset) (second preset)))
    (catch Exception e
      (set-presets filename [preset])))
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

(defn add-dwarf-preset [e]
  (add-preset e :#dwarf-list :#dwarf-presets "dwarves" :id))


(defn rem-prof-preset [e]
  (rem-preset e :#prof-presets "profs"))

(defn rem-dwarf-preset [e]
  (rem-preset e :#dwarf-presets "dwarves"))

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
        selected-profs  (filter #(in? (name (first %)) selected) profs)
        preset          (second selected)
        selection-list  (select (to-root e) [:#prof-list])]
    ;(prn selected-profs)
    (selection! selection-list {:multi? true} selected-profs)))

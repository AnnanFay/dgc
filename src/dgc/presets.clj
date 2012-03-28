(ns dgc.presets
  ""
  (:use [dgc util config]
        [seesaw.core :exclude [listbox tree]]
        [cheshire.core]
        [clojure.walk])
  (:import [java.io File]))

;;;; Preset handlers

(defn get-presets
  "Load presets from a file. Returns loaded presets."
  [filename]
  (in filename {}))

(defn set-presets
  "Writes list of presets to a file. Returns presets."
  [filename presets]
  (out filename presets)
  presets)

(defn append-preset
  "Appends a preset to a presets file. Returns preset."
  [filename preset]
  (set-presets filename (assoc (get-presets filename) (first preset) (second preset))))


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
(defn change-dwarf-preset [e]
  (let [selection-list  (select (to-root e) [:#dwarf-list])
        puffballs       (get-model-elements (.getModel selection-list))

        source          (.getSource e)
        preset          (second (selection source))

        selected-dwarfs (filter #(in? (:id %) preset) puffballs)]
    (selection! selection-list {:multi? true} selected-dwarfs)))

(defn change-prof-preset [e]
  (let [selection-list  (select (to-root e) [:#prof-list])
        profs           (get-model-elements (.getModel selection-list))
        profs           (remove keyword? profs)

        source          (.getSource e)
        selected        (second (selection source))
        
        selected-profs  (filter #(in? (first %) selected) profs)
        preset          (second selected)]
    (selection! selection-list {:multi? true} selected-profs)))

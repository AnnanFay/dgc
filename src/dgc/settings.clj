(ns dgc.settings
  ""
  (:use [dgc util form]
        [seesaw.core :only [frame show! action]]
        [seesaw.keystroke]
        [seesaw.mig  :only [mig-panel]]))

(def settings (atom (merge  (in-default "settings.clj")
                            (in "settings.clj" {}))))

(declare update-settings!)

(defn update-setting!
  "Update a single setting."
  [k v]
  (update-settings! (assoc @settings k v)))

(defn update-settings!
  ""
  [new-settings & [event]]

    ; update atom
    (swap! settings (constantly new-settings))

    ; write to file
    (out "settings.clj" @settings))

(defn view-settings
  "Displays editable settings in a new window."
  [e]
  (doto (frame 
          :title      "DGC - Settings"
          :height     600
          :width      800
          :on-close   :dispose
          :content    (mig-panel
                        :id :settings-panel
                        :constraints ["insets 0, fill" "" ""]
                        :items [[(title "Settings")             "dock north, shrink 0, growx"]
                                [(form @settings update-settings!) ""]]))
    show!))

(def view-settings-action (action :name     "View Settings" :tip "View Settings"     
                                  :mnemonic \s              :key (keystroke "F6")
                                  :handler  view-settings))
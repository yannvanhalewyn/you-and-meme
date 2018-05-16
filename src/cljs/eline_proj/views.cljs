(ns eline-proj.views
  (:require [re-frame.core :as re-frame]
            [eline-proj.subs :as subs]
            ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div "Hello from " @name]))

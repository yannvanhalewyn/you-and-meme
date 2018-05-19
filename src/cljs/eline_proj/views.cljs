(ns eline-proj.views
  (:require [re-frame.core :as re-frame]
            [eline-proj.subs :as subs]
            ))

(defn main-panel []
  [:div
   [:div.btn-wrapper
    [:button.btn
     {:on-click #(js/alert "hoi")}
     "Click voor een random video!"]]])

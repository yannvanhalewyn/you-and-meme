(ns eline-proj.views
  (:require [eline-proj.subs :as subs]
            [eline-proj.views.button :as button]
            [re-frame.core :refer [dispatch subscribe]]))

(defn main-panel []
  (let [panel @(subscribe [::subs/panel])
        video @(subscribe [::subs/current-video])
        status @(subscribe [::subs/request-status])]

    (case panel
      :panel/awaiting-download
      [:div
       [:div.flex-centered
        [:h1 "Random video gevonden"]
        [:div
         [:img {:src (:video/thumb video)}]]
        [:h3 (:video/title video)]
        [:span "Downloaden.. " [:div.spinning {:style {:margin-left "4px"}} "▶"]]]]

      :panel/download-ready
      [:div
       [:div.flex-centered
        [:h1 "Download klaar!"]
        [:div
         [:img {:src (:video/thumb video)}]]
        [:h3 (:video/title video)]
        [:a {:href "#" :on-click #(dispatch [:route/home])} "Nieuwe video aanvragen"]]]

      [:div
       [:div.flex-centered
        [:h2 "[ YOU AND MEME ]"]

        [:button.btn {:class (when (= :request/pending status) "spinning")
                      :on-click #(dispatch [:video/request-random])}
         [button/component]]
        [:h3 "Push the button to add a random video"]]])))

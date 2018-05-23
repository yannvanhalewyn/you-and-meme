(ns eline-proj.views
  (:require [re-frame.core :refer [dispatch subscribe]]
            [eline-proj.subs :as subs]))

(defn main-panel []
  (let [panel @(subscribe [::subs/panel])
        video @(subscribe [::subs/current-video])]
    (case panel
      :panel/awaiting-download
      [:div
       [:div.text-centered
        [:h1 "Random video gevonden:"]
        [:div
         [:img {:src (:video/thumb video)}]]
        [:h2 (:video/title video)]
        "downloaden..."]]

      :panel/download-ready
      [:div
       [:div.text-centered
        [:h1 "Download klaar!"]
        [:div
         [:img {:src (:video/thumb video)}]]
        [:h2 (:video/title video)]
        [:button.btn {:on-click #(dispatch [:route/home])}
         "Klik"]]]

      [:div
       [:div.flex-centered
        [:h2 "[ YOU AND MEME ]"]
        [:button.btn {:class (when (= :request/pending status) "spinning")
                      :on-click #(dispatch [:video/request-random])}
         [:span.btn__icon "▶"]]
        [:h3 "Push the button to add a random video"]]])))

(ns you-and-meme.views
  (:require [you-and-meme.subs :as subs]
            [you-and-meme.views.button :as button]
            [re-frame.core :refer [dispatch subscribe]]))

(defn- video-title [{:keys [video/title]}]
  (str "[ " title " ]"))

(defn main-panel []
  (let [panel @(subscribe [::subs/panel])
        video @(subscribe [::subs/current-video])
        status @(subscribe [::subs/request-status])]

    (case panel
      :panel/awaiting-download
      [:div
       [:div.flex-centered
        [:h1 "Video Found!"]
        [:div
         [:img {:src (:video/thumb video)}]]
        [:h3 (video-title video)]
        [:span "Downloaden.. "
         [:div.spinning.small-icon
          [button/component]]]]]

      :panel/download-ready
      [:div
       [:div.flex-centered
        [:a {:href "#" :on-click #(dispatch [:route/home])} "<< Go back to main page"]
        [:div [:img {:src (:video/thumb video)}]]
        [:h3 (video-title video)]
        [:h2 "Download klaar."]]]

      [:div
       [:div.flex-centered
        [:h2 "[ YOU AND MEME ]"]
        [:button.btn {:class (when (= :request/pending status) "spinning")
                      :on-click #(dispatch [:video/request-random])}
         [button/component]]
        [:h3 "Push the button to add a random video"]]])))

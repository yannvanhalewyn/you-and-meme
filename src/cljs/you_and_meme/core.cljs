(ns you-and-meme.core
  (:require [you-and-meme.chsk :as chsk]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [you-and-meme.events :as events]
            [you-and-meme.views :as views]
            [you-and-meme.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
    (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (chsk/start!)
  (mount-root))

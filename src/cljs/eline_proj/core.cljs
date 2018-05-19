(ns eline-proj.core
  (:require [eline-proj.chsk :as chsk]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [eline-proj.events :as events]
            [eline-proj.views :as views]
            [eline-proj.config :as config]))

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

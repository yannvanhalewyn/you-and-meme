(ns you-and-meme.fx
  (:require [you-and-meme.chsk :as chsk]
            [re-frame.core :as rf]
            [clojure.data :refer [diff]]))

(rf/reg-fx :socket chsk/sock-fx)

(def debug-logger
  (rf/->interceptor
   :id :debug-logger
   :after
   (fn [{:keys [effects coeffects] :as context}]
     (let [new-db (:db effects)
           old-db (:db coeffects)
           event (:event coeffects)
           group-name (str "Dispatch: " (first event))]
       (.groupCollapsed js/console group-name)
       (.info js/console "%c Event" "color: #03A9F4; font-weight: bold" event)
       (if new-db
         (do
           (.info js/console "%c New DB" "color: #9E9E9E; font-weight: bold" (sort new-db))
           (let [diff (diff old-db new-db)]
             (.info js/console "%c Removed" "color: #FF6259; font-weight: bold" (first diff))
             (.info js/console "%c Added" "color: #29D042; font-weight: bold" (second diff))))
         (.info js/console "No db changes"))
       (.groupEnd js/console group-name "color: grey"))
     context)))

(defn reg-event-fx
  [id handler]
  (rf/reg-event-fx
   id
   [(when ^boolean goog.DEBUG debug-logger)]
   handler))

(defn reg-event-db [id handler]
  (reg-event-fx id (fn [{:keys [db]} event] {:db (handler db event)})))

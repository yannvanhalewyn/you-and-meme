(ns you-and-meme.events
  (:require [you-and-meme.fx :refer [reg-event-db reg-event-fx]]
            [you-and-meme.db :as db]))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-fx
 :video/request-random
 (fn [{:keys [db]} [_ & data]]
   {:db (assoc db :db/request-status :request/pending)
    :socket [:video/request-random]}))

(reg-event-db
 :route/home
 (fn [db _]
   (assoc db :db/panel :panel/main)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CHSK

(defn- assoc-failure [db]
  (assoc db
    :db/panel :panel/main
    :db/request-status :request/failed))

(reg-event-db
 :video-request/download-started
 (fn [db [_ video]]
   (assoc db
     :db/current-video video
     :db/panel :panel/awaiting-download)))

(reg-event-db
 :chsk/state
 (fn [db [_ [_ state]]]
   (if (:first-open? state)
     (assoc db :db/chsk-ready true)
     db)))

(reg-event-fx :chsk/handshake (fn [_ evt]))

(reg-event-db
 :chsk/recv
 (fn [db [_ [evt]]]
   (case evt
     :video-request/download-ready
     (assoc db
       :db/panel :panel/download-ready
       :db/request-status :status/success)

     :video-request/download-failed
     (assoc-failure db)

     db)))

(reg-event-db
 :chsk/failed
 (fn [db [evt data]]
   (.error js/console "CHSK fail:" data)
   (assoc-failure db)))

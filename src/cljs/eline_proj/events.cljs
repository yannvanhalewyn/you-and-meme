(ns eline-proj.events
  (:require [eline-proj.fx :refer [reg-event-db reg-event-fx]]
            [eline-proj.db :as db]))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-fx
 :video/request-random
 (fn [_ [_ & data]]
   {:socket [:video/request-random]}))

(reg-event-db
 :route/home
 (fn [db _]
   (assoc db :db/panel :panel/main)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CHSK

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
     (assoc db :db/panel :panel/download-ready)

     :video-request/download-failed
     (assoc db :db/panel :panel/main)

     db)))

(reg-event-fx
 :chsk/failed
 (fn [_ [evt data]]
   (.error js/console "CHSK fail:" data)))

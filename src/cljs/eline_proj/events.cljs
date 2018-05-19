(ns eline-proj.events
  (:require [eline-proj.fx :refer [reg-event-db reg-event-fx]]
            [eline-proj.db :as db]))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-db
 :chsk/state
 (fn [db [_ [_ state]]]
   (.log js/console "Socket state" state)
   (if (:first-open? state)
     (assoc db :db/chsk-ready true)
     db)))

(reg-event-fx
 :chsk/handshake
 (fn [_ evt]
   (.log js/console "Handshake" evt)))

(reg-event-db
 :chsk/recv
 (fn [db evt]
   (.log js/console "CHSK RECEIVE" evt)
   db))

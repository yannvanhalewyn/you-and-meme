(ns eline-proj.chsk
  (:require [eline-proj.routes :refer [chsk-handler]]
            [com.stuartsierra.component :as c]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer [get-sch-adapter]]))

(defn broadcast [chsk msg]
  (doseq [uid (:any @(:connected-uids chsk))]
    ((:send-fn chsk) uid msg)))

(def SENTE_KEYS [:ajax-get-or-ws-handshake-fn
                 :ajax-post-fn :ch-recv :send-fn])

(defrecord CHSK []
  c/Lifecycle
  (start [this]
    (println "Starting Channel Sockets listener...")
    (let [chsk (sente/make-channel-socket! (get-sch-adapter) {})
          stop-fn (sente/start-chsk-router! (:ch-recv chsk) #'chsk-handler)]
      (assoc (select-keys chsk SENTE_KEYS)
        :server stop-fn)))

  (stop [this]
    (println "Stopping Channel Sockets listener...")
    (if-let [stop (:stop-fn this)]
      (stop)
      (println "No stop-fn found for Channel Sockets listener. Doing nothing."))
    (apply dissoc this SENTE_KEYS :stop-fn)))

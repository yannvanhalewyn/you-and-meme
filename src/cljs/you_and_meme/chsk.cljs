(ns you-and-meme.chsk
  (:require-macros [cljs.core.async.macros :as asyncm :refer [go go-loop]])
  (:require [clojure.core.async :as async :refer [<! >! put! chan]]
            [taoensso.sente  :as sente :refer (cb-success?)]
            [re-frame.core :refer [dispatch]]))

(def TIMEOUT 8000)
(defonce chsk (atom nil))

(defn- send! [msg cb] ((:send-fn @chsk) msg TIMEOUT cb))

(defn- event-handler [{:keys [event] :as all}]
  (dispatch event))

(defn start! []
  (let [_chsk (sente/make-channel-socket! "/chsk" {:type :auto})
        stop-fn (sente/start-chsk-router! (:ch-recv _chsk) event-handler)]
    (reset! chsk (assoc _chsk :stop-fn stop-fn))))

(defn sock-fx [msg]
  (send! msg #(if (and (sente/cb-success? %) (not (:error %)))
                (dispatch %)
                (dispatch [:chsk/failed %]))))

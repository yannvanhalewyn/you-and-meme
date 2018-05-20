(ns eline-proj.routes
  (:require [eline-proj.youtube :as yt]
            [eline-proj.download :as dl]
            [clojure.java.io :as io]
            [compojure.route :as route]
            [compojure.core :as comp :refer [GET POST defroutes]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; HTTP

(def index-view
  {:status 200
   :headers {"Cache-Control" "max-age=0, private, must-revalidate"
             "Content-Type" "text/html; charset=UTF-8"}
   :body (slurp (io/resource "public/index.html"))})

(defroutes app-routes
  (GET "/" [] index-view)
  (route/not-found "<h1>NOT FOUND</h1>"))

(defn wrap-chsk-routes [routes chsk]
  (comp/routes
   (GET "/chsk" [] (:ajax-get-or-ws-handshake-fn chsk))
   (POST "/chsk" [] (:ajax-post-fn chsk))
   routes))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CHSK

(defmulti socket-handler "Multimethod for handling socket messages" :id)

(defmethod socket-handler :default [{:keys [event ?reply-fn]}]
  (println "Unhandled event" event))

(defmethod socket-handler :chsk/ws-ping [evt] nil)

(defmethod socket-handler :video/request-random
  [{:keys [send-fn uid] :as event}]
  (let [video (yt/get-random-video)]
    (future
      (try
        (dl/download (:video/id video) "videos/")
        (send-fn uid [:video-request/download-ready])
        (catch Exception e
          (println "Er ging iets mis!" e)
          (send-fn uid [:video-request/download-failed]))))
    [:video-request/download-started video]))

(defn- wrap-reply
  "Wraps a handler and calls the reply fn if any"
  [handler]
  (fn [msg]
    (if-let [reply (:?reply-fn msg)]
      (reply (handler msg))
      (handler msg))))

(def chsk-handler (wrap-reply socket-handler))

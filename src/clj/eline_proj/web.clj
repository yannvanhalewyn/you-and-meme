(ns eline-proj.web
  (:require [eline-proj.routes :as routes]
            [com.stuartsierra.component :as c]
            [ring.middleware.defaults :refer [api-defaults
                                              wrap-defaults]]
            [org.httpkit.server :as http-kit]))

(def app-defaults
  (assoc api-defaults
    :params {:urlencoded true
             :multipart  true
             :nested     true
             :keywordize true}
    :static {:resources "public"}))

(defn make-handler [chsk]
  (-> routes/app-routes
      (routes/wrap-chsk-routes chsk)
      (wrap-defaults app-defaults)))

(defrecord Web [options chsk]
  c/Lifecycle
  (start [this]
    (if (:server this)
      this
      (let [handler (make-handler chsk)
            port (:port options)]
        (println (format "Starting server on port %s" port))
        (assoc this :server (http-kit/run-server handler {:port port})))))

  (stop [this]
    (if-let [stop (:server this)]
      (do
        (println "Stopping web server...")
        (stop)
        (assoc this :server nil))
      this)))

(defn make [opts] (map->Web {:options opts}))

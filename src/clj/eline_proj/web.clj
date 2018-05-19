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

(defn make-handler [options]
  (wrap-defaults routes/app-routes app-defaults))

(defrecord Web [options]
  c/Lifecycle
  (start [this]
    (if (:server this)
      this
      (let [handler (make-handler options)
            port (:port options)]
        (println (format "Starting server on port %s" port))
        (assoc this :server (http-kit/run-server handler {:port port})))))

  (stop [this]
    (if (:server this)
      (do
        (println "Stopping web server...")
        (.stop (:server this))
        (assoc this :server nil))
      this)))

(ns eline-proj.system
  (:require [eline-proj.web :as web]
            [com.stuartsierra.component :as c]))

(defn new-system []
  (c/system-map
   :web (web/->Web {:port 8080})))

(ns eline-proj.system
  (:require [eline-proj.web :as web]
            [eline-proj.chsk :as chsk]
            [com.stuartsierra.component :as c]))

(defn new-system []
  (c/system-map
   :web (c/using (web/make {:port 8080}) [:chsk])
   :chsk (chsk/->CHSK)))

(ns you-and-meme.system
  (:require [you-and-meme.web :as web]
            [you-and-meme.chsk :as chsk]
            [com.stuartsierra.component :as c]))

(defn new-system []
  (c/system-map
   :web (c/using (web/make {:port 8080}) [:chsk])
   :chsk (chsk/->CHSK)))

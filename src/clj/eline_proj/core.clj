(ns eline-proj.core
  (:gen-class)
  (:require [eline-proj.youtube :as yt]
            [eline-proj.download :refer [download]]))

(defn -main [& args]
  (let [v (yt/get-random-video)]
    (download (:video/id v) "videos/")))

(ns user
  (:require [you-and-meme.system :refer [new-system]]
            [figwheel-sidecar.system :as sys]
            [com.stuartsierra.component :as c]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [reloaded.repl :refer [system go set-init! start stop reset]]))

(defn dev-system []
  (assoc (new-system)
    :figwheel (sys/figwheel-system
               (assoc-in (sys/fetch-config) [:data :build-ids] ["dev"]))
    :css-watcher  (c/using
                   (sys/css-watcher {:watch-paths ["resources/public/css"]})
                   {:figwheel-server :figwheel})))

(set-init! dev-system)

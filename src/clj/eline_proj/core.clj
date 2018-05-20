(ns eline-proj.core
  (:gen-class)
  (:require [eline-proj.system :refer [new-system]]
            [com.stuartsierra.component :as c]
            [clojure.java.browse :refer [browse-url]]))

(defn -main [& args]
  (c/start (new-system))
  (browse-url "http://localhost:8080"))

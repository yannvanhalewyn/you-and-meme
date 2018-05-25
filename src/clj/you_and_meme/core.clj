(ns you-and-meme.core
  (:gen-class)
  (:import [java.net InetAddress])
  (:require [you-and-meme.system :refer [new-system]]
            [com.stuartsierra.component :as c]
            [clojure.java.browse :refer [browse-url]]
            [clojure.string :as str]))

(defn- host-url-connection-message []
  (let [local-ip (.getHostAddress (InetAddress/getLocalHost))
        line (format "== Verbind je tablet of telefoon op: http://%s:8080 =="
                     local-ip)
        sep-outer (str/join (repeat (count line) \=))
        sep-inner (format "==%s==" (str/join (repeat (- (count line) 4) " ")))]
    (str/join "\n" [sep-outer sep-inner line sep-inner sep-outer])))

(defn -main [& args]
  (c/start (new-system))
  (println (host-url-connection-message))
  (browse-url "http://localhost:8080"))

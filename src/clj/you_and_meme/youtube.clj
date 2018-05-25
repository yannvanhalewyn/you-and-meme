(ns you-and-meme.youtube
  (:require [clj-http.client :as http]))

(def API_KEY "AIzaSyAyRpnXbxrluKuxLqWDEq3RCoIk-M9Iizs")

(defn- rand-str [size]
  (let [chars (map char (concat (range 48 58) (range 65 91)))]
    (apply str (take size (repeatedly #(rand-nth chars))))))

(def URL_FMT  "https://www.googleapis.com/youtube/v3/search?key=%s&maxResults=%s&part=snippet&type=video&q=%s")

(def url #(format URL_FMT API_KEY %1 %2))

(defn- get-random-videos [count]
  (let [q (rand-str 3)]
    (println q)
    (get-in
     (http/get (url 20 q) {:accept :json :as :json})
     [:body :items])))

(defn- parse-item [{:keys [snippet id]}]
  {:video/id (:videoId id)
   :video/title (:title snippet)
   :video/thumb (get-in (:thumbnails snippet) [:high :url])})

(defn get-random-video []
  (parse-item (first (get-random-videos 1))))

(ns eline-proj.download
  (:require [clj-http.client :as http]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:import java.net.URLDecoder))

(def URL_FMT "http://www.youtube.com/get_video_info?video_id=%s&el=embedded&ps=default")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Decoding

(defn- decode-kv-pairs [raw]
  (reduce #(let [[k v] (str/split %2 #"=")]
             (assoc %1 (keyword k) (when v (URLDecoder/decode v))))
          {} (str/split raw #"&")))

(defn- parse-stream-map [raw]
  (as-> (decode-kv-pairs raw) $
    (get $ :url_encoded_fmt_stream_map)
    (str/split $ #",")
    (map decode-kv-pairs $)))

(defn- select-candidate [streams]
  (let [dict (group-by :quality streams)]
    (or (first (get dict "hd720"))
        (first (get dict "large"))
        (first (get dict "medium"))
        (first (get dict "small")))))

(defn- video-stream-map [video-id]
  (println "Searching for downloadable video for " video-id)
  (parse-stream-map (:body (http/get (format URL_FMT video-id)))))

(defn- download* [url output-file]
  (printf "Downloading %s to %s" url output-file)
  (some-> (:body (http/get url {:as :byte-array}))
          (io/copy output-file)))

(defn download [video-id dir]
  (-> (video-stream-map video-id)
      select-candidate :url
      (download* (io/file (str dir video-id ".mov")))))

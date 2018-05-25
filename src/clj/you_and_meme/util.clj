(ns you-and-meme.util)

(defn map-vals
  "Applies function f to each item in the map s and returns a map."
  [f coll]
  (persistent!
   (reduce-kv #(assoc! %1 %2 (f %3)) (transient {}) coll)))

(defn find-where [pred coll]
  (first (filter pred coll)))

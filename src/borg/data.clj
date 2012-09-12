(ns borg.data
  (:use borg.math
        [clojure.tools.logging :only (info error)]))

(def ^:dynamic cached-data [])
(def ^:dynamic target-position 240)

(defn clear-data []
  (def cached-data []))

(defn balls [data]
  (map :ball data))

(defn ball-positions [balls]
  (map :pos balls))

(defn ball-x-positions [balls]
  (map :x (ball-positions balls)))

(defn ball-y-positions [balls]
  (map :y (ball-positions balls)))

(defn ball-x-delta [balls]
  (- (first (ball-x-positions balls)) (last (ball-x-positions balls))))

(defn ball-is-moving-right [balls]
  (neg? (ball-x-delta balls)))

(defn calculate-target-position [data]
  (info (str "calculating target position"))
  (info (ball-x-positions (balls data)))
  (if (ball-is-moving-right (balls data))
    240
    (last (ball-y-positions (balls data)))))

(defn update-target-position [data]
  (def target-position (calculate-target-position data)))

(defn update-data [data]
  (def cached-data (conj cached-data data))
  (if (> (count cached-data) 1)
    (update-target-position cached-data)))

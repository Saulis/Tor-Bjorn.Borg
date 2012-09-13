(ns borg.data
  (:use borg.math
        [clojure.tools.logging :only (info error)]))

(def ^:dynamic cached-data [])
(def ^:dynamic previous-position)
(def ^:dynamic target-height 240)

(defn save-previous-position [data]
  (def previous-position data))

(defn balls [data]
  (map :ball data))

(defn ball-positions []
  (map :pos (balls cached-data)))

(defn new-target-position [p1 p2]
  (landing-point (:x p2) (:y p2) (slope p1 p2)))

(defn previous-ball-position []
  (first (take-last 2 (ball-positions ))))

(defn current-ball-position []
  (last (ball-positions)))

(defn refresh-data [data]
  (info data)
  (info (str "Target: " target-height))
  (def cached-data (conj cached-data data))
  (if (> (count cached-data) 2)
    (def target-height (:y (new-target-position (previous-ball-position) (current-ball-position))))))

(defn clear-data []
  (def cached-data []))





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





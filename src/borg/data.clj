(ns borg.data
  (:use borg.math
        [clojure.tools.logging :only (info error)]))

(defstruct time-direction :time :direction)

(def ^:dynamic cached-data [])
(def ^:dynamic cached-moves [])
(def ^:dynamic previous-position)
(def ^:dynamic target-height 240)

(defn clear-data []
  (def cached-data []))

(defn clear-moves []
  (def cached-moves []))

(defn save-move [direction]
  (def cached-moves (conj cached-moves (struct time-direction (System/currentTimeMillis) direction))))

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

(defn velocity [p1 p2]
  (Math/sqrt (+ (Math/pow (x-delta p1 p2) 2) (Math/pow (y-delta p1 p2) 2))))

(defn refresh-data [data]
  ;(info data)
  ;(info (str "Target: " target-height))
  (if (> (count cached-data) 2)
    (info (str "Velocity: " (velocity (previous-ball-position) (current-ball-position) ))))
  (def cached-data (conj cached-data data))
  (if (> (count cached-data) 2)
    (def target-height (:y (new-target-position (previous-ball-position) (current-ball-position))))))







(defn ball-x-positions [balls]
  (map :x (ball-positions balls)))

(defn ball-y-positions [balls]
  (map :y (ball-positions balls)))

(defn ball-x-delta [previous-ball-position current-ball-position]
  (- (:x previous-ball-position) (:x current-ball-position)))

(defn ball-is-moving-right [previous-ball-position current-ball-position]
  (neg? (ball-x-delta previous-ball-position current-ball-position)))

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





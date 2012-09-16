(ns borg.data
  (:use borg.math
        borg.moves
        [clojure.tools.logging :only (info error)]))

(defstruct time-direction :time :direction)

(def ^:dynamic cached-data [])
(def ^:dynamic sent-messages [])
(def ^:dynamic previous-position)

(defn clear-data []
  (def cached-data []))

(defn clear-messages []
  (def sent-messages []))

(defn save-message [direction]
  (def sent-messages (conj sent-messages (struct time-direction (System/currentTimeMillis) direction))))

(defn reset-direction []
  (save-message 0)) ;TODO timestamp should be same as in last message

(defn last-message []
  (last sent-messages))

(defn save-previous-position [data]
  (def previous-position data))

(defn balls [data]
  (map :ball data))

(defn ball-positions []
  (map :pos (balls cached-data)))

(defn previous-ball-position []
  (first (take-last 2 (ball-positions))))

(defn current-ball-position []
  (last (ball-positions)))

(defn last-three-ball-positions []
  (take-last 3 (ball-positions)))

;for debugging
(defn velocity [p1 p2]
  (Math/sqrt (+ (Math/pow (x-delta p1 p2) 2) (Math/pow (y-delta p1 p2) 2))))



(defn refresh-data [data]
  (info data)
  ;(info (str "Target: " target-height))
  ;(if (> (count cached-data) 2)
    ;(info (str "Velocity: " (velocity (previous-ball-position) (current-ball-position) ))))
  (def cached-data (conj cached-data data)))





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





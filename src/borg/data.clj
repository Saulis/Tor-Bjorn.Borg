(ns borg.data
  (:use borg.geometry
        borg.moves))

(defstruct time-direction :time :direction)

(def ^:dynamic saved-data [])
(def ^:dynamic saved-messages [])

(defn clear-data []
  (def saved-data []))

(defn save-data [data]
  (def saved-data (conj saved-data data)))

(defn clear-messages []
  (def saved-messages []))

(defn save-message [direction]
  (def saved-messages (conj saved-messages (struct time-direction (System/currentTimeMillis) direction))))

(defn- balls [data]
  (map :ball data))

(defn- ball-positions [data]
  (map :pos (balls data)))

(defn previous-ball-position [data]
  (first (take-last 2 (ball-positions data))))

(defn current-ball-position [data]
  (last (ball-positions data)))

(defn last-three-ball-positions [data]
  (take-last 3 (ball-positions data)))

(defn current-height [data]
  (:y (:left (last data))))


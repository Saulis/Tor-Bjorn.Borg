(ns borg.data.repository)

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
  (def saved-messages (conj (vec (take-last 9 saved-messages)) (struct time-direction (System/currentTimeMillis) direction))))


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

(defn- add-message [message]
  (def saved-messages (conj (vec (take-last 19 saved-messages)) message)))

(defn save-message [direction]
  (add-message (struct time-direction (System/currentTimeMillis) direction)))

(defn reset-direction [last-message]
  (if (not (nil? last-message))
    (add-message {:time (:time last-message) :direction 0.0})))

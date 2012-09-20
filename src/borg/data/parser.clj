(ns borg.data.parser)

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

(defn current-opponent-height [data]
  (:y (:right (last data))))

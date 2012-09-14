(ns borg.moves
    (use borg.constants
         borg.math))

(def min-direction -1.0)
(def max-direction 1.0)

(defn target-height-with-paddle [target-height]
  (- target-height (/ paddle-height 2)))

(defn distance-to-target [target-height current-height]
  (- (target-height-with-paddle target-height) current-height))

(defn direction-to-target [target-height current-height]
  (/ (distance-to-target target-height current-height) paddle-height))

(defn trim-max-direction [direction]
  (if (> direction max-direction)
    max-direction
    direction))

(defn trim-min-direction [direction]
  (if (< direction min-direction)
    min-direction
    direction))

(defn trim-direction [target-height current-height]
  (trim-min-direction (trim-max-direction (direction-to-target target-height current-height))))

(defn direction [target-height current-height]
  (trim-direction target-height current-height))

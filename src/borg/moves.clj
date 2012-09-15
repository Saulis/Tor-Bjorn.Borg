(ns borg.moves
    (use borg.constants
         borg.math))

(def min-direction -1.0)
(def max-direction 1.0)

(defn target-height-with-paddle [target-height]
  (- target-height 3)) ;TODO

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

(defn new-target-height [p1 p2]
  (landing-height (:x p2) (:y p2) (slope p1 p2)))

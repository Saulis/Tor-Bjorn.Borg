(ns borg.logic.direction
  (:use borg.constants
        clojure.contrib.math))

(def min-direction -1.0)
(def max-direction 1.0)
(defn distance-to-target-factor [] (/ paddle-height 2)) ;;; smaller factor makes acceleration sharper - larger factor makes it smoother (and longer)
(defn target-height-offset [] (/ paddle-height 2)) ;;; defines the relative position of the paddle on the target height

(defn- round-with-two-decimals [number]
 (/ (round (* number 100)) 100.0))

(defn- target-height-with-paddle [target-height]
  (- target-height (target-height-offset)))

(defn- distance-to-target [target-height current-height]
  (- (target-height-with-paddle target-height) current-height))

(defn- direction-to-target [target-height current-height]
  (/ (distance-to-target target-height current-height) (distance-to-target-factor)))

(defn- rounded-direction-to-target [target-height current-height]
  (round-with-two-decimals (direction-to-target target-height current-height)))

(defn- trim-max-direction [direction]
  (if (> direction max-direction)
    max-direction
    direction))

(defn- trim-min-direction [direction]
  (if (< direction min-direction)
    min-direction
    direction))

(defn- trim-direction [target-height current-height]
  (trim-min-direction (trim-max-direction (rounded-direction-to-target target-height current-height))))

(defn direction [target-height current-height]
  (trim-direction target-height current-height))


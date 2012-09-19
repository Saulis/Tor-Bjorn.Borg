(ns borg.logic.direction
  (:use borg.constants))

(def min-direction -1.0)
(def max-direction 1.0)
(def distance-to-target-factor (/ paddle-height 2)) ;;; smaller factor makes acceleration sharper - larger factor makes it smoother (and longer)
(def target-height-offset (/ paddle-height 2)) ;;; defines the relative position of the paddle on the target height

(defn- target-height-with-paddle [target-height]
  (- target-height target-height-offset))

(defn- distance-to-target [target-height current-height]
  (- (target-height-with-paddle target-height) current-height))

(defn- direction-to-target [target-height current-height]
  (/ (distance-to-target target-height current-height) distance-to-target-factor))

(defn- trim-max-direction [direction]
  (if (> direction max-direction)
    max-direction
    direction))

(defn- trim-min-direction [direction]
  (if (< direction min-direction)
    min-direction
    direction))

(defn- trim-direction [target-height current-height]
  (trim-min-direction (trim-max-direction (direction-to-target target-height current-height))))

(defn direction [target-height current-height]
  (trim-direction target-height current-height))


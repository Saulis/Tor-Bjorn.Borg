(ns borg.logic.strategy
  (:use borg.logic.geometry
        borg.logic.trajectories
        borg.data.parser
        borg.constants
        borg.logic.offense
        clojure.contrib.math))

(def slope-difference-threshold 0.05) ;;; maximum difference of slopes (to be considered equal) between two consecutive pairs of positions

(defn- there-are-more-than-two-positions [ball-positions]
  (> (count ball-positions) 2))

(defn- previous-position-exists [data]
  (>= (count data) 2))

;;; slope calculation

(defn- slope-can-be-calculated-from-two-positions [ball-positions]
  (and
    (= (count ball-positions) 2)
    (slope-can-be-calculated (first ball-positions) (second ball-positions))))

(defn- previous-slope [ball-positions]
  (slope (first ball-positions) (second ball-positions)))

(defn- current-slope [ball-positions]
  (slope (second ball-positions) (nth ball-positions 2)))

(defn- difference-between-slopes [ball-positions]
  (abs (- (previous-slope ball-positions) (current-slope ball-positions))))

(defn- slope-is-steady [ball-positions]
  (< (difference-between-slopes ball-positions) slope-difference-threshold))

(defn- slopes-can-be-calculated [ball-positions]
  (and
   (slope-can-be-calculated (first ball-positions) (second ball-positions))
   (slope-can-be-calculated (second ball-positions) (nth ball-positions 2))))

;;; direction is accurate if atleast three consecutive balls are heading the same way. This prevents errors during bounces

(defn direction-is-accurate [ball-positions]
    (or
       (slope-can-be-calculated-from-two-positions ball-positions)
       (and
            (there-are-more-than-two-positions ball-positions)
            (slopes-can-be-calculated ball-positions)
            (slope-is-steady ball-positions))))

;;; target height for the paddle

(defn target-height [data]
  (if (previous-position-exists data)
    (let [ previous-position (previous-ball-position data)
           current-position (current-ball-position data)
           current-landing-height (landing-height previous-position current-position)
           current-opponent-height (current-opponent-height data)]
    (trimmed-landing-height-with-offset previous-position current-position current-landing-height current-opponent-height))
    mid-height))

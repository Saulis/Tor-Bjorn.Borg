(ns borg.logic.strategy
  (:use borg.logic.geometry
        borg.logic.trajectories
        borg.data.parser
        borg.constants
        borg.logic.offense
        clojure.contrib.math))

(def slope-difference-threshold 0.5) ;;; maximum difference of slopes (to be considered equal) between two consecutive pairs of positions

(defn- there-are-enough-positions-to-estimate-direction [ball-positions]
  (> (count ball-positions) 2))

(defn- previous-position-exists [data]
  (>= (count data) 2))

(defn- there-are-only-two-positions [ball-positions]
  (= (count ball-positions) 2))

(defn- previous-slope [ball-positions]
  (slope (first ball-positions) (second ball-positions)))

(defn- current-slope [ball-positions]
  (slope (second ball-positions) (nth ball-positions 2)))

(defn- difference-between-slopes [ball-positions]
  (abs (- (previous-slope ball-positions) (current-slope ball-positions))))

(defn- slope-is-steady [ball-positions]
  (< (difference-between-slopes ball-positions) slope-difference-threshold))

(defn direction-is-accurate [ball-positions]
  (or
     (there-are-only-two-positions ball-positions)
     (and
          (there-are-enough-positions-to-estimate-direction ball-positions)
          (slope-is-steady ball-positions))))

(defn target-height [data]
  (if (previous-position-exists data)
    (let [ previous-position (previous-ball-position data)
           current-position (current-ball-position data)
           current-landing-height (landing-height previous-position current-position)
           current-opponent-height (current-opponent-height data)]
    (landing-height-with-offset previous-position current-position current-landing-height current-opponent-height))
    mid-height))

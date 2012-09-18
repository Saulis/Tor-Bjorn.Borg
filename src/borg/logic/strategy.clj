(ns borg.logic.strategy
  (:use borg.logic.geometry
        borg.logic.trajectories
        borg.data.parser
        clojure.contrib.math))

(def slope-difference-threshold 0.5) ;;; maximum difference of slopes (to be considered equal) between two consecutive pairs of positions

(defn- there-are-not-enough-positions-to-estimate-direction [ball-positions]
  (or
    (empty? ball-positions)
    (< (count ball-positions) 2)))

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
  (if (there-are-not-enough-positions-to-estimate-direction ball-positions)
    false
    (if (there-are-only-two-positions ball-positions)
      true
      (slope-is-steady ball-positions))))



(defn- ball-is-close [ball-position]
  (< (:x ball-position) 50))       ;TODO

(defn target-height [data]
  (if (> (count data ) 2)
    (if (ball-is-close (current-ball-position data))
      (- (landing-height (previous-ball-position data) (current-ball-position data)) 50) ;TODO
      (landing-height (previous-ball-position data) (current-ball-position data)))
    200))

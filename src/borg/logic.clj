(ns borg.logic
  (:use borg.geometry
        borg.constants
        clojure.contrib.math))

(def slope-difference-threshold 0.5)   ;TODO find optimal value

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

(defn new-direction-is-accurate [ball-positions]
  (if (there-are-not-enough-positions-to-estimate-direction ball-positions)
    false
    (if (there-are-only-two-positions ball-positions)
      true
      (slope-is-steady ball-positions))))

(declare landing-point-on-left)
(declare landing-point-on-right)

(defn- landing-point-on-left-via-bottom [x y slope]
  (landing-point-on-left (hit-width-on-bottom x y slope) (bottom-hit-height) (invert slope)))

(defn- landing-point-on-left-via-top [x y slope]
  (landing-point-on-left (hit-width-on-top x y slope) (top-hit-height) (invert slope)))

(defn- landing-point-on-left-via-top-or-bottom [x y slope]
  (if (ball-lands-on-bottom x y slope)
    (landing-point-on-left-via-bottom x y slope)
    (landing-point-on-left-via-top x y slope)))

(defn- landing-point-on-left [x y slope]
  (if (ball-lands-on-left x y slope)
    (hit-point-on-left x y slope)
    (landing-point-on-left-via-top-or-bottom x y slope)))

(defn- landing-point-on-right-via-bottom [x y slope]
  (landing-point-on-right (hit-width-on-bottom x y slope) (bottom-hit-height) (invert slope)))

(defn- landing-point-on-right-via-top [x y slope]
  (landing-point-on-right (hit-width-on-top x y slope) (top-hit-height) (invert slope)))

(defn- landing-point-on-right-via-top-or-bottom [x y slope]
  (if (ball-lands-on-bottom x y slope)
    (landing-point-on-right-via-bottom x y slope)
    (landing-point-on-right-via-top x y slope)))

(defn- landing-point-on-right [x y slope]
  (if (ball-lands-on-right x y slope)
    (hit-point-on-right x y slope)
    (landing-point-on-right-via-top-or-bottom x y slope)))

(defn- landing-point-on-left-via-right [x y slope]
  (landing-point-on-left (:x (landing-point-on-right x y slope)) (:y (landing-point-on-right x y slope)) (invert slope)))

(defn- landing-point-on-right [x y slope]
  (if (ball-lands-on-right x y slope)
    (hit-point-on-right x y slope)
    (landing-point-on-right-via-top-or-bottom x y slope)))

(defn- landing-height-on-left [p1 p2]
  (:y (landing-point-on-left (:x p2) (:y p2) (slope p1 p2))))

(defn- landing-height-on-left-via-right [p1 p2]
  (:y (landing-point-on-left-via-right (:x p2) (:y p2) (slope p1 p2))))

(defn- ball-is-going-left [p1 p2]
  (> (:x p1) (:x p2)))

(defn landing-height [p1 p2]
  (if (ball-is-going-left p1 p2)
    (landing-height-on-left p1 p2)
    (landing-height-on-left-via-right p1 p2)))

(defn- ball-is-close [ball-position]
  (< (:x ball-position) 50))       ;TODO

(defn target-height [data]
  (if (> (count data ) 2)
    (if (ball-is-close (current-ball-position data))
      (- (landing-height (previous-ball-position data) (current-ball-position data)) 50) ;TODO
      (landing-height (previous-ball-position data) (current-ball-position data)))
    200))

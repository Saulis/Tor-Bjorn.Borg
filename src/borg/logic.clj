(ns borg.logic
  (:use borg.math
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

(declare landing-point)

(defn- landing-point-via-bottom [x y slope]
  (landing-point (hit-width-on-bottom x y slope) (bottom-hit-height) (invert slope)))

(defn- landing-point-via-top [x y slope]
  (landing-point (hit-width-on-top x y slope) (top-hit-height) (invert slope)))

(defn- landing-point-via-right [x y slope]
  (landing-point (right-hit-width) (hit-height-on-right x y slope) (invert slope)))

(defn- landing-point-via-bounce [x y slope]
  (if (ball-is-going-down slope)
    (landing-point-via-bottom x y slope)
    (landing-point-via-top x y slope)))

(defn- landing-point [x y slope]
  (if (ball-lands-on-left x y slope)
    (hit-point-on-left x y slope)
    (landing-point-via-bounce x y slope)))

(defn landing-height
  ([x y slope] (:y (landing-point x y slope)))
  ([p1 p2] (landing-height (:x p2) (:y p2) (slope p1 p2))))

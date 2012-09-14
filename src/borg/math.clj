(ns borg.math
  (:use borg.constants))

(defstruct point :x :y)

(defn to-point [x y]
  (struct point x y))

(defn invert [x]
  (* -1.0 x))

(defn x-delta [p1 p2]
  (- (:x p1) (:x p2)))

(defn y-delta [p1 p2]
  (- (:y p1) (:y p2)))

(defn slope [p1 p2]
  (/ (y-delta p1 p2) (x-delta p1 p2)))

(defn distance-to-left-hit [x]
  (- (left-hit-width) x))

(defn distance-to-right-hit [x]
  (- x (right-hit-width)))

(defn distance-to-top-hit [y]
  (- (top-hit-height) y))

(defn distance-to-bottom-hit [y]
  (- (bottom-hit-height) y))

(defn hit-height-on-left [x y slope]
  (+ (* slope (distance-to-left-hit x)) y))

(defn hit-height-on-right [x y slope]
  (+ (* slope (distance-to-right-hit x)) y))

(defn hit-width [x distance-to-y slope]
  (+ (/ distance-to-y slope) x))

(defn hit-width-on-top [x y slope]
  (hit-width x (distance-to-top-hit y) slope))

(defn hit-width-on-bottom [x y slope]
  (hit-width x (distance-to-bottom-hit y) slope))

(defn ball-lands-on-left [x y slope]
  (is-hit-height-inside (hit-height-on-left x y slope)))

(defn ball-lands-on-right [x y slope]
  (is-hit-height-inside (hit-height-on-right x y slope)))

(defn hit-point-on-left [x y slope]
  (to-point (left-hit-width) (hit-height-on-left x y slope)))

;NOTE! THIS ONLY APPLIES WHEN BALL IS GOING LEFT
(defn ball-is-going-down [slope]
  (pos? slope))

(declare landing-point)

(defn landing-point-via-bottom [x y slope]
  (landing-point (hit-width-on-bottom x y slope) (bottom-hit-height) (invert slope)))

(defn landing-point-via-top [x y slope]
  (landing-point (hit-width-on-top x y slope) (top-hit-height) (invert slope)))

(defn landing-point-via-right [x y slope]
  (landing-point (right-hit-width) (hit-height-on-right x y slope) (invert slope)))

(defn landing-point-via-bounce [x y slope]
    (if (ball-is-going-down slope)
      (landing-point-via-bottom x y slope)
      (landing-point-via-top x y slope)))

(defn landing-point [x y slope]
  (if (ball-lands-on-left x y slope)
    (hit-point-on-left x y slope)
    (landing-point-via-bounce x y slope)))
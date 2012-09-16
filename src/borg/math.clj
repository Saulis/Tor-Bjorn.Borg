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
  (- (right-hit-width) x))

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

(defn hit-point-on-left [x y slope]
  (to-point (left-hit-width) (hit-height-on-left x y slope)))

(defn hit-point-on-right [x y slope]
  (to-point (right-hit-width) (hit-height-on-right x y slope)))

(defn- is-not-already-on-the-left [x]
  (> x (left-hit-width)))

(defn ball-lands-on-left [x y slope]
  (and
    (is-not-already-on-the-left x)
    (is-hit-height-inside (hit-height-on-left x y slope))))

(defn- is-not-already-on-the-right [x]
  (< x (right-hit-width)))

(defn ball-lands-on-right [x y slope]
  (and
    (is-not-already-on-the-right x)
    (is-hit-height-inside (hit-height-on-right x y slope))))

(defn is-not-already-at-the-bottom [y]
  (> y (bottom-hit-height)))

(defn ball-lands-on-bottom [x y slope]
  (and
    (is-not-already-at-the-bottom y)
    (is-hit-width-inside (hit-width-on-bottom x y slope))))

(defn- is-not-already-at-the-top [y]
  (< y (top-hit-height)))

(defn ball-lands-on-top [x y slope]
  (and
    (is-not-already-at-the-top y)
    (is-hit-width-inside (hit-width-on-top x y slope))))







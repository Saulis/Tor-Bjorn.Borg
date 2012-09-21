(ns borg.logic.geometry
  (:use borg.constants))

(defstruct point :x :y)

(defn to-point [x y]
  (struct point x y))

(defn invert [x]
  (* -1.0 x))

;;; slope calculation

(defn- x-delta [p1 p2]
  (- (:x p1) (:x p2)))

(defn- y-delta [p1 p2]
  (- (:y p1) (:y p2)))

(defn slope [p1 p2]
  (/ (y-delta p1 p2) (x-delta p1 p2)))

(defn attack-slope [factor current-slope]
  (+ current-slope (* 5/7 factor))) ;;; 5/7 is the magic number here!

;;; distance to boundary hit calculation

(defn- distance-to-left-hit [x]
  (- (left-hit-width) x))

(defn- distance-to-right-hit [x]
  (- (right-hit-width) x))

(defn- distance-to-top-hit [y]
  (- (top-hit-height) y))

(defn- distance-to-bottom-hit [y]
  (- (bottom-hit-height) y))

;;; boundary hit height and width calculation

(defn- hit-height [distance-to-x y slope]
  (+ (* slope distance-to-x) y))

(defn hit-height-on-left [x y slope]
  (hit-height (distance-to-left-hit x) y slope))

(defn hit-height-on-right [x y slope]
  (hit-height (distance-to-right-hit x) y slope))

(defn- hit-width [x distance-to-y slope]
  (+ (/ distance-to-y slope) x))

(defn hit-width-on-top [x y slope]
  (hit-width x (distance-to-top-hit y) slope))

(defn hit-width-on-bottom [x y slope]
  (hit-width x (distance-to-bottom-hit y) slope))

;;; boundary hit points for left and right

(defn hit-point-on-left [x y slope]
  (to-point (left-hit-width) (hit-height-on-left x y slope)))

(defn hit-point-on-right [x y slope]
  (to-point (right-hit-width) (hit-height-on-right x y slope)))

;;; helper methods for landing point calculation

(defn- is-not-already-on-the-left-paddle [x]
  (not= x (left-hit-width)))

(defn- is-not-already-on-the-right-paddle [x]
  (not= x (right-hit-width)))

(defn- is-not-already-at-the-bottom [y]
  (< y (bottom-hit-height)))

(defn- is-not-already-at-the-top [y]
  (> y (top-hit-height)))

;;; landing height and width calculation for each boundary

(defn ball-lands-on-left [x y slope]
  (and
    (is-not-already-on-the-left-paddle x)
    (is-hit-height-inside (hit-height-on-left x y slope))))

(defn ball-lands-on-right [x y slope]
  (and
    (is-not-already-on-the-right-paddle x)
    (is-hit-height-inside (hit-height-on-right x y slope))))

(defn ball-lands-on-bottom [x y slope]
  (and
    (is-not-already-at-the-bottom y)
    (is-hit-width-inside (hit-width-on-bottom x y slope))))

(defn ball-lands-on-top [x y slope]
  (and
    (is-not-already-at-the-top y)
    (is-hit-width-inside (hit-width-on-top x y slope))))

(defn ball-lands-near-upper-corner [landing-height]
  (<= landing-height half-paddle-height))

(defn ball-lands-near-lower-corner [landing-height]
  (>= landing-height (- max-height half-paddle-height)))








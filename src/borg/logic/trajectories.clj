(ns borg.logic.trajectories
  (:use borg.constants
        borg.logic.geometry))

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

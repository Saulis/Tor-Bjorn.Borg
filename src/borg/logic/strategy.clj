(ns borg.logic.strategy
  (:use borg.logic.geometry
        borg.logic.trajectories
        borg.data.parser
        borg.constants
        [clojure.tools.logging :only (info error)] ;debugging TODO
        clojure.contrib.math))

(def slope-difference-threshold 0.5) ;;; maximum difference of slopes (to be considered equal) between two consecutive pairs of positions
(def offset (- half-paddle-height 4))
(def offset-factors [-3/7 -2/7 -1/7 1/7 2/7 3/7])

(defn- there-are-not-enough-positions-to-estimate-direction [ball-positions]
  (or
    (empty? ball-positions)
    (< (count ball-positions) 2)))

(defn- there-are-two-positions [data]
  (>= (count data)))

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

(defn- modify-slope [offset-factor current-slope]
  (+ current-slope (* 5/7 offset-factor))) ;;; 5/7 is the magic number here!


(defn- landing-height-on-opponents-side [modified-slope landing-height]
  (landing-height-on-right (left-hit-width) landing-height (invert modified-slope)))

(defn- distance-to-opponent [modified-slope landing-height opponent-height]
  (abs (- (landing-height-on-opponents-side modified-slope landing-height) opponent-height)))


(defn- distances-to-opponent [current-slope landing-height opponent-height]
  (for [f offset-factors]
    {:factor f :distance (distance-to-opponent (modify-slope f current-slope) landing-height opponent-height) }))




(defn- offset-factor-with-longest-distance-to-opponent [current-slope landing-height opponent-height]
  (let [distances (distances-to-opponent current-slope landing-height opponent-height)]
    ;(info (:factor (first (sort-by :distance distances))))     ;TODO DEBUG
    (:factor (last (sort-by :distance distances)))))

(defn- target-height-offset [current-slope landing-height opponent-height]
  ;(let [factor (offset-factor-with-smallest-distance-to-lower-corner current-slope landing-height)]
  ;(info (str "Selected factor: " factor)
;    " Slope: " (invert (modify-slope factor current-slope))
;    " Landing: " (landing-height-on-right (left-hit-width) landing-height (invert (modify-slope factor current-slope)))))
  (* paddle-height (offset-factor-with-longest-distance-to-opponent current-slope landing-height opponent-height)))
  ;(* paddle-height -2/7))

(defn- ball-lands-near-corners [landing-height]
  (or (ball-lands-near-upper-corner landing-height)
      (ball-lands-near-lower-corner landing-height)))

(defn- landing-height-with-offset [previous-ball-position current-ball-position landing-height opponent-height]
  (if (ball-lands-near-corners landing-height)
    landing-height
    (+ landing-height (target-height-offset (slope previous-ball-position current-ball-position) landing-height opponent-height))))

(defn target-height [data]
  (if (there-are-two-positions data)
    (landing-height-with-offset (previous-ball-position data) (current-ball-position data) (landing-height (previous-ball-position data) (current-ball-position data)) (current-opponent-height data))
    mid-height))

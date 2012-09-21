(ns borg.logic.offense
  (:use borg.logic.trajectories
        borg.constants
        borg.logic.geometry
        clojure.contrib.math))

(def offset (- half-paddle-height 4))
(def offset-factors [-3/7 -2/7 -1/7 1/7 2/7 3/7])

(defn- modify-slope [offset-factor current-slope]
  (+ current-slope (* 5/7 offset-factor))) ;;; 5/7 is the magic number here!

(defn- distance-to-lower-corner [modified-slope landing-height]
  (let [height (landing-height-on-right (left-hit-width) landing-height (invert modified-slope))]
    (- max-height height)))

(defn- distance-to-upper-corner [modified-slope landing-height]
  (landing-height-on-right (left-hit-width) landing-height (invert modified-slope)))

(defn- shortest-distance-to-corner [modified-slope landing-height]
  (min (distance-to-lower-corner modified-slope landing-height)
    (distance-to-upper-corner modified-slope landing-height)))

(defn- distances-to-corners [current-slope landing-height]
  (for [f offset-factors]
	(let [slope (modify-slope f current-slope)]
    {:factor f :distance (shortest-distance-to-corner slope landing-height) :slope slope })))
	
(defn- with-good-slope [x]
	(<= (abs (:slope x)) 1))
	
(defn- distances-to-corners-with-good-slope [current-slope landing-height]
	(filter with-good-slope (distances-to-corners current-slope landing-height)))

(defn- offset-factor-with-shortest-distance-to-corner [current-slope landing-height]
  (let [distances (distances-to-corners-with-good-slope current-slope landing-height)]
  (if (empty? distances)
	(:factor (first (sort-by :distance (distances-to-corners current-slope landing-height))))
    (:factor (first (sort-by :distance distances))))))

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
  (* paddle-height (offset-factor-with-shortest-distance-to-corner current-slope landing-height)))
;(* paddle-height -2/7))

(defn- ball-lands-near-corners [landing-height]
  (or (ball-lands-near-upper-corner landing-height)
    (ball-lands-near-lower-corner landing-height)))

(defn landing-height-with-offset [previous-ball-position current-ball-position landing-height opponent-height]
  (if (ball-lands-near-corners landing-height)
    landing-height
    (+ landing-height (target-height-offset (slope previous-ball-position current-ball-position) landing-height opponent-height))))

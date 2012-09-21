(ns borg.logic.offense
  (:use borg.logic.trajectories
        borg.constants
        borg.logic.geometry
        borg.logic.corner-offense
        clojure.contrib.math))

(def offset-factors [-3/7 -2/7 -1/7 0 1/7 2/7 3/7])
(def gamble-slope-threshold 1/6) ;;; minimum slope for gamble shot
(def too-vertical-slope-threshold 5/2) ;;; minimum slope for playing it safe
(def gamble-factor 1/2)

(defn- it-is-time-to-gamble [current-slope]
  (or
    (< current-slope (invert gamble-slope-threshold))
    (> current-slope gamble-slope-threshold)))

(defn- gamble-factor [current-slope]
  (if (neg? current-slope)
    gamble-factor
    (invert gamble-factor)))

(defn- offset-factors-for [current-slope]
  (if (it-is-time-to-gamble current-slope)
    (conj offset-factors (gamble-factor current-slope))
    offset-factors))

(defn- distance-to-lower-corner [modified-slope landing-height]
  (let [height (landing-height-on-right (left-hit-width) landing-height (invert modified-slope))]
    (- max-height height)))

(defn- distance-to-upper-corner [modified-slope landing-height]
  (landing-height-on-right (left-hit-width) landing-height (invert modified-slope)))

(defn- distance-to-nearest-corner [modified-slope landing-height]
  (min (distance-to-lower-corner modified-slope landing-height)
    (distance-to-upper-corner modified-slope landing-height)))

(defn- distances-to-corners [current-slope landing-height]
  (for [f (offset-factors-for current-slope)]
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
  (for [f (offset-factors-for current-slope)]
    (let [slope (modify-slope f current-slope)]
    {:factor f :distance (distance-to-opponent slope landing-height opponent-height) :slope slope })))

(defn- distances-to-opponent-with-good-slope [current-slope landing-height opponent-height]
  (filter with-good-slope (distances-to-opponent current-slope landing-height opponent-height)))

;;DUPLICATE
(defn- longest-distance-to-opponent [current-slope landing-height opponent-height]
  (let [distances (distances-to-opponent-with-good-slope current-slope landing-height opponent-height)]
    (if (empty? distances)
      (:distance (last (sort-by :distance (distances-to-opponent current-slope landing-height opponent-height))))
      (:distance (last (sort-by :distance distances))))))

(defn- offset-factor-with-longest-distance-to-opponent [current-slope landing-height opponent-height]
  (let [distances (distances-to-opponent-with-good-slope current-slope landing-height opponent-height)]
   (if (empty? distances)
    (:factor (last (sort-by :distance (distances-to-opponent current-slope landing-height opponent-height))))
    (:factor (last (sort-by :distance distances))))))

(defn- offset-factor-with-best-shot [current-slope landing-height opponent-height]
  (let [longest-distance-to-opponent (longest-distance-to-opponent current-slope landing-height opponent-height)]
  (if (< mid-height longest-distance-to-opponent)
    (:factor longest-distance-to-opponent)
    (:factor (shortest-distance-to-corner (offset-factors-for current-slope) (modify-slope ) landing-height))))

(defn- landing-height-offset [current-slope landing-height opponent-height]
  (* paddle-height (offset-factor-with-best-shot current-slope landing-height opponent-height)))

(defn- slope-is-getting-too-vertical [slope]
 (> (abs slope) too-vertical-slope-threshold))

(defn- it-is-time-to-defense [previous-ball-position current-ball-position]
  (slope-is-getting-too-vertical (slope previous-ball-position current-ball-position)))

(defn- trim-landing-height-lower-end [landing-height]
  (if (> landing-height (- max-height half-paddle-height))
    (- max-height half-paddle-height)
    landing-height))

(defn- trim-landing-height-upper-end [landing-height]
  (if (< landing-height half-paddle-height)
    half-paddle-height
    landing-height))

(defn- trim-landing-height [landing-height]
  (trim-landing-height-upper-end (trim-landing-height-lower-end landing-height)))

(defn- landing-height-with-offset [previous-ball-position current-ball-position landing-height opponent-height]
  (+ landing-height (landing-height-offset (slope previous-ball-position current-ball-position) landing-height opponent-height)))

(defn trimmed-landing-height-with-offset [previous-ball-position current-ball-position landing-height opponent-height]
  (if (it-is-time-to-defense previous-ball-position current-ball-position)
    (trim-landing-height landing-height)
    (trim-landing-height (landing-height-with-offset previous-ball-position current-ball-position landing-height opponent-height))))


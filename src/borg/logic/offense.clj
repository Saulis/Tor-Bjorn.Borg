(ns borg.logic.offense
  (:use borg.logic.trajectories
        borg.constants
        borg.logic.geometry
        borg.logic.corner-offense
        borg.logic.distance-offense
        clojure.contrib.math))

(def offset-factors [-3/7 -2/7 -1/7 0 1/7 2/7 3/7])
(def gamble-slope-threshold 1/6) ;;; minimum slope for gamble shot
(def too-vertical-slope-threshold 5/2) ;;; minimum slope for playing it safe
(def gamble-factor 1/2)

(defn- it-is-time-to-gamble [current-slope]
  (or
    (< current-slope (invert gamble-slope-threshold))
    (> current-slope gamble-slope-threshold)))

(defn- gamble-factor-for [current-slope]
  (if (neg? current-slope)
    gamble-factor
    (invert gamble-factor)))

(defn- offset-factors-for [current-slope]
  (if (it-is-time-to-gamble current-slope)
    (conj offset-factors (gamble-factor-for current-slope))
    offset-factors))

(defn- offset-factor-with-best-shot [current-slope landing-height opponent-height]
  (let [longest-distance-to-opponent (longest-distance-to-opponent (offset-factors-for current-slope) current-slope landing-height opponent-height)]
  (if (< mid-height (:distance longest-distance-to-opponent))
    (:factor longest-distance-to-opponent)
    (:factor (shortest-distance-to-corner (offset-factors-for current-slope) current-slope landing-height)))))

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


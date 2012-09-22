(ns borg.logic.offense
  (:use borg.logic.trajectories
        borg.constants
        borg.logic.geometry
        borg.logic.corner-offense
        borg.logic.distance-offense
        clojure.contrib.math))

(def attack-factors [-3/7 -2/7 -1/7 0 1/7 2/7 3/7]) ;;; attack factors, for each part of the paddle. Loosely adopted from the original Pong :-)
(def gamble-slope-threshold 1/6) ;;; minimum slope for gamble shot
(def vertical-slope-threshold 5/2) ;;; minimum slope for playing it safe
(def gamble-factor 1/2) ;;; additional attack factor for risky shots

;;; if the ball is coming in from a nice angle -> let's add a risky corner shot to the table

(defn- it-is-time-to-gamble [current-slope]
  (or
    (< current-slope (invert gamble-slope-threshold))
    (> current-slope gamble-slope-threshold)))

(defn- gamble-factor-for [current-slope]
  (if (neg? current-slope)
    gamble-factor
    (invert gamble-factor)))

(defn- attack-factors-for [current-slope]
  (if (it-is-time-to-gamble current-slope)
    (conj attack-factors (gamble-factor-for current-slope))
    attack-factors))

;;; if the ball can be shot far enough from the opponent -> let do this. Otherwise, shoot for the corners.

(defn- ball-can-be-shot-far-enough-from-opponent [current-slope landing-height opponent-height]
  (< mid-height (:distance (longest-distance-to-opponent (attack-factors-for current-slope) current-slope landing-height opponent-height))))

(defn- attack-factor-with-best-shot [current-slope landing-height opponent-height]
  (if (ball-can-be-shot-far-enough-from-opponent current-slope landing-height opponent-height)
    (:factor (longest-distance-to-opponent (attack-factors-for current-slope) current-slope landing-height opponent-height))
    (:factor (shortest-distance-to-corner (attack-factors-for current-slope) current-slope landing-height))))

;;; if the slope gets too vertical, it's best not to add offset to landing height. Just try to hit the damn ball any way you can.

(defn- slope-is-getting-too-vertical [slope]
 (> (abs slope) vertical-slope-threshold))

(defn- it-is-time-to-defense [previous-ball-position current-ball-position]
  (slope-is-getting-too-vertical (slope previous-ball-position current-ball-position)))

;;; landing height trimming to prevent the paddle from bouncing on top and bottom

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

;;; landing height offset is an offset that is added to the landing height of the ball to control the paddle position

(defn- landing-height-offset [current-slope landing-height opponent-height]
  (* paddle-height (attack-factor-with-best-shot current-slope landing-height opponent-height)))

(defn- landing-height-with-offset [previous-ball-position current-ball-position landing-height opponent-height]
  (+ landing-height (landing-height-offset (slope previous-ball-position current-ball-position) landing-height opponent-height)))

(defn trimmed-landing-height-with-offset [previous-ball-position current-ball-position landing-height opponent-height]
  (if (it-is-time-to-defense previous-ball-position current-ball-position)
    (trim-landing-height landing-height)
    (trim-landing-height (landing-height-with-offset previous-ball-position current-ball-position landing-height opponent-height))))


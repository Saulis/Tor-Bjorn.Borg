(ns borg.logic.offense
  (:use borg.logic.trajectories
        borg.constants
        borg.logic.geometry))

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
    {:factor f :distance (shortest-distance-to-corner (modify-slope f current-slope) landing-height) }))

(defn- offset-factor-with-shortest-distance-to-corner [current-slope landing-height]
  (let [distances (distances-to-corners current-slope landing-height)]
    (:factor (first (sort-by :distance distances)))))

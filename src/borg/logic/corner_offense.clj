(ns borg.logic.corner-offense
  (:use borg.constants
        borg.logic.geometry
        borg.logic.trajectories))

(defn- distance-to-lower-corner [attack-slope landing-height]
  (let [height (landing-height-on-right (left-hit-width) landing-height (invert attack-slope))]
    (- max-height height)))

(defn- distance-to-upper-corner [attack-slope landing-height]
  (landing-height-on-right (left-hit-width) landing-height (invert attack-slope)))

(defn- distance-to-nearest-corner [attack-slope landing-height]
  (min (distance-to-lower-corner attack-slope landing-height)
    (distance-to-upper-corner attack-slope landing-height)))

(defn- distances-to-corners [factors current-slope landing-height]
  (for [f factors]
    (let [attack-slope (attack-slope f current-slope)]
      {:factor f :distance (distance-to-nearest-corner attack-slope landing-height) :slope attack-slope })))

(defn- with-good-slope [x]
  (<= (abs (:slope x)) 1))

(defn- distances-to-corners-with-good-slope [factors current-slope landing-height]
  (filter with-good-slope (distances-to-corners factors current-slope landing-height)))

(defn shortest-distance-to-corner [factors current-slope landing-height]
  (let [distances (distances-to-corners-with-good-slope factors current-slope landing-height)]
    (if (empty? distances)
      (first (sort-by :distance (distances-to-corners factors current-slope landing-height)))
      (first (sort-by :distance distances)))))


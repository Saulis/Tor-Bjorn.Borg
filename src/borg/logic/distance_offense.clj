(ns borg.logic.distance-offense
  (:use borg.constants
        borg.logic.geometry
        borg.logic.trajectories
        clojure.contrib.math))

(defn- with-good-slope [x]
  (<= (abs (:slope x)) 1))

(defn- landing-height-on-opponents-side [attack-slope landing-height]
  (landing-height-on-right (left-hit-width) landing-height (invert attack-slope)))

(defn- distance-to-opponent [attack-slope landing-height opponent-height]
  (abs (- (landing-height-on-opponents-side attack-slope landing-height) opponent-height)))

(defn- distances-to-opponent [factors current-slope landing-height opponent-height]
  (for [f factors]
    (let [attack-slope (attack-slope f current-slope)]
      {:factor f :distance (distance-to-opponent attack-slope landing-height opponent-height) :slope attack-slope })))

(defn- distances-to-opponent-with-good-slope [factors current-slope landing-height opponent-height]
  (filter with-good-slope (distances-to-opponent factors current-slope landing-height opponent-height)))

(defn longest-distance-to-opponent [factors current-slope landing-height opponent-height]
  (let [distances (distances-to-opponent-with-good-slope factors current-slope landing-height opponent-height)]
    (if (empty? distances)
      (last (sort-by :distance (distances-to-opponent factors current-slope landing-height opponent-height)))
      (last (sort-by :distance distances)))))


(ns borg.logic.distance-offense
  (:use borg.constants
        borg.logic.geometry
        borg.logic.trajectories
        clojure.contrib.math))

(defn- landing-height-on-opponents-side [attack-slope landing-height]
  (landing-height-on-right (left-hit-width) landing-height (invert attack-slope)))

(defn- distance-to-opponent [attack-slope landing-height opponent-height]
  (abs (- (landing-height-on-opponents-side attack-slope landing-height) opponent-height)))

(defn- distances-to-opponent [attack-factors current-slope landing-height opponent-height]
  (for [f attack-factors]
    (let [attack-slope (attack-slope f current-slope)]
      {:factor f :distance (distance-to-opponent attack-slope landing-height opponent-height) :slope attack-slope })))

(defn- with-fast-slope [x]
  (<= (abs (:slope x)) 3/2)) ;;; using 3/2 for distance offense - magic number by empiric testing

;;; filtering out attacks with poor slopes to emphasize faster shots

(defn- distances-to-opponent-with-fast-slope [attack-factors current-slope landing-height opponent-height]
  (filter with-fast-slope (distances-to-opponent attack-factors current-slope landing-height opponent-height)))

(defn longest-distance-to-opponent [attack-factors current-slope landing-height opponent-height]
  (let [distances-with-fast-slope (distances-to-opponent-with-fast-slope attack-factors current-slope landing-height opponent-height)]
    (if (empty? distances-with-fast-slope)
      (last (sort-by :distance (distances-to-opponent attack-factors current-slope landing-height opponent-height)))
      (last (sort-by :distance distances-with-fast-slope)))))


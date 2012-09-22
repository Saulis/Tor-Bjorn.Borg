(ns borg.logic.corner-offense
  (:use borg.constants
        borg.logic.geometry
        borg.logic.trajectories
        clojure.contrib.math))

(defn- distance-to-lower-corner [attack-slope landing-height]
    (- max-height (landing-height-on-right (left-hit-width) landing-height (invert attack-slope))))

(defn- distance-to-upper-corner [attack-slope landing-height]
  (landing-height-on-right (left-hit-width) landing-height (invert attack-slope)))

(defn- distance-to-nearest-corner [attack-slope landing-height]
  (min
    (distance-to-lower-corner attack-slope landing-height)
    (distance-to-upper-corner attack-slope landing-height)))

(defn- distances-to-corners [attack-factors current-slope landing-height]
  (for [f attack-factors]
    (let [attack-slope (attack-slope f current-slope)]
      {:factor f :distance (distance-to-nearest-corner attack-slope landing-height) :slope attack-slope })))

(defn- with-fast-slope [x]
  (<= (abs (:slope x)) 3/2)) ;;; using 3/2 for corner offense - magic number by empiric testing

;;; filtering out attacks with poor slopes to emphasize faster shots

(defn- distances-to-corners-with-fast-slope [attack-factors current-slope landing-height]
  (filter with-fast-slope (distances-to-corners attack-factors current-slope landing-height)))

(defn shortest-distance-to-corner [attack-factors current-slope landing-height]
  (let [distances-with-fast-slope (distances-to-corners-with-fast-slope attack-factors current-slope landing-height)]
    (if (empty? distances-with-fast-slope)
      (first (sort-by :distance (distances-to-corners attack-factors current-slope landing-height)))
      (first (sort-by :distance distances-with-fast-slope)))))


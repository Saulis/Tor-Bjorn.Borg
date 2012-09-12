(ns borg.math
  (:use clojure.contrib.generic.math-functions
        borg.constants))

(defstruct point :x :y)

(defn to-point [x y]
  (struct point x y))

(defn tan-degrees [angle]
  (tan (toRadians angle)))

(defn max-x [x angle]
  (round (/ (- court-width x) (tan-degrees angle))))

(defn max-y [y angle]
  (round (* (- court-height y) (tan-degrees angle))))

(defn next-impact-x [x angle]
  (if (> (max-x x angle) court-width)
    court-width
    (max-x x angle)))

(defn next-impact-y [y angle]
  (if (> (max-y y angle) court-height)
    court-height
    (max-y y angle)))

(defn next-impact-point [x y angle]
  (to-point (next-impact-x x angle) (next-impact-y y angle)))

(println (next-impact-point 0 0 45))
(println (next-impact-point 0 50 45))
(println (next-impact-point 50 0 45))
(println (next-impact-point 0 100 135))




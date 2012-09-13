(ns borg.math
  (:use clojure.contrib.generic.math-functions
        borg.constants))

(defstruct point :x :y)

(defn x-delta [p1 p2]
  (- (:x p1) (:x p2)))

(defn y-delta [p1 p2]
  (- (:y p1) (:y p2)))

(defn slope [p1 p2]
  (/ (y-delta p1 p2) (x-delta p1 p2)))

(defn distance-to-left-hit [x]
  (- (left-hit-width) x))

(defn distance-to-top-hit [y]
  (- (top-hit-height y)))

(defn distance-to-bottom-hit [y]
  (- (bottom-hit-height y)))

(defn hit-height-on-left [x y slope]
  (+ (* slope (distance-to-left-hit x)) y))

(defn hit-width [x distance-to-y slope]
  (+ (/ distance-to-y slope) x))

(defn hit-width-on-top [x y slope]
  (hit-width x (distance-to-top-hit y) slope))

(defn hit-width-on-bottom [x y slope]
  (hit-width x (distance-to-bottom-hit y) slope))






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



(defn negative-slope [p1 p2]
  (* -1 (slope p1 p2)))

(defn impact-height-on-left-wall [p1 p2]
  (+ (* (slope p1 p2) (- 0 (:x p2))) (:y p2)))

(defn solve-x [p1 p2 target-y]
  (+ (/ (- target-y (:y p2)) (slope p1 p2) (:x p2))))

(defn ball-lands-on-left-wall [p1 p2]
  (let [impact-height (impact-height-on-left-wall p1 p2)]
  (and
    (>= 0 impact-height)
    (<= court-height impact-height))))

(defn landing-point-on-left-wall [p1 p2]
  (to-point 0 (impact-height-on-left-wall p1 p2)))

(defn ball-will-bounce-via-bottom [p1 p2]
  (< 0 (impact-height-on-left-wall p1 p2)))

(defn bottom-bounce-position [p1 p2]
  (to-point (solve-x p1 p2 0) 0))

(defn impact-height-on-left-wall-after-bottom-bounce [p1 p2]
  (let [bottom-position (bottom-bounce-position p1 p2)]
      (* (negative-slope p1 p2) (:x bottom-position))))

(defn landing-point-via-bottom-bounce [p1 p2]
  (to-point 0 (impact-height-on-left-wall-after-bottom-bounce p1 p2)))

;TODO
(defn landing-point-via-top-bounce [p1 p2]
  (to-point 0 0))

(defn landing-point-via-bounce [p1 p2]
  (if (ball-will-bounce-via-bottom p1 p2)
    (landing-point-via-bottom-bounce p1 p2)
    (landing-point-via-top-bounce p1 p2)))

(defn landing-point [p1 p2]
  (if (ball-lands-on-left-wall p1 p2)
    (landing-point-on-left-wall p1 p2)
    (landing-point-via-bounce p1 p2)))

(defn down-impact-point [p1 p2]
  (landing-point (to-point (solve-x p1 p2 0) 0)))

(defn up-impact-point [p1 p2]
  (landing-point (to-point (solve-x p1 p2 480) 480)))

(defn impact-point [p1 p2]
  (let [y (impact-height-on-left-wall p1 p2)]
    (if (<= 0 y)
      (down-impact-point p1 p2)
      (up-impact-point p1 p2))))

(defn angle-in-radians [p1 p2]
  (if
    (or (nil? (x-delta p1 p2))
    (= (x-delta p1 p2) 0))
    0
  (atan (/ (y-delta p1 p2) (x-delta p1 p2)))))

(defn to-degrees [angle]
  (toDegrees angle))

(defn angle-in-degrees [p1 p2]
  (to-degrees (angle-in-radians p1 p2)))

(defn last-two-balls [data]
  (map :ball (take-last 2 data)))

(defn last-two-positions [data]
  (map :pos (last-two-balls data)))

(defn angle-from-last-two-positions [data]
    (angle-in-degrees (second (last-two-positions data)) (first (last-two-positions data))))

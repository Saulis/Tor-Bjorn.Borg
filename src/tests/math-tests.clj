(ns tests.math-tests
  (:use clojure.test
        borg.constants
        borg.math))

(def center (to-point (/ court-width 2) (/ court-height 2)))
(def right-middle (to-point court-width (/ court-height 2)))
(def left-middle (to-point 0 (/ court-height 2)))
(def left-bottom-corner (to-point 0 0))
(def right-bottom-corner (to-point court-width 0))

(deftest slope-tests
  (is (= (slope (to-point 5 5) (to-point 5 10)) 666))        ;?
  (is (= (slope (to-point 0 0) (to-point 5 5 )) 1))
  (is (= (slope (to-point 0 0) (to-point -5 -5 )) 1))
  (is (= (slope (to-point 0 0) (to-point -5 5 )) -1)))

(deftest hit-height-on-left-tests
  (is (= (hit-height-on-left 320 0 -1) 320)))

(deftest distance-to-top-hit-tests
  (is (= (distance-to-top-hit 0) 475)))

(deftest landing-point-tests
  (is (= (landing-point 640 480 1.0) (to-point 0 480))))

(deftest impact-height-on-left-wall-tests
  (is (= (impact-height-on-left-wall right-middle center) (:y left-middle))))

(run-all-tests)

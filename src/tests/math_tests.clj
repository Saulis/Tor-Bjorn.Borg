(ns tests.math-tests
  (:use clojure.test
        borg.math
        borg.constants))

;(deftest slope-tests
;  (is (= (slope (to-point 5 5) (to-point 5 10)) 666))        ;?
;  (is (= (slope (to-point 0 0) (to-point 5 5 )) 1))
;  (is (= (slope (to-point 0 0) (to-point -5 -5 )) 1))
;  (is (= (slope (to-point 0 0) (to-point -5 5 )) -1)))

(deftest ball-lands-on-left-tests
  (is (true? (ball-lands-on-left (left-hit-width) (bottom-hit-height) 0))) ;from corner, flat scope
  (is (true? (ball-lands-on-left (left-hit-width) (bottom-hit-height) 1))) ;from corner, 45 degree scope
  (is (true? (ball-lands-on-left (left-hit-width) (bottom-hit-height) -1))) ;from corner, inverse 45 degree scope
  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) 0))) ;from center, flat scope
  (is (false? (ball-lands-on-left (/ max-width 2) (/ max-height 2) 1))) ;from center, 45 degree scope
  (is (false? (ball-lands-on-left (/ max-width 2) (/ max-height 2) -1))) ;from center, inverse 45 degree scope
  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) 480/640))) ;from center, diagonal scope
  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) -480/640))) ;from center, inverse diagonal scope
  )

(deftest ball-lands-on-right-tests
  (is (true? (ball-lands-on-right (right-hit-width) (bottom-hit-height) 0))) ;from corner, flat scope
  (is (true? (ball-lands-on-right (right-hit-width) (bottom-hit-height) 1))) ;from corner, 45 degree scope
  (is (true? (ball-lands-on-right (right-hit-width) (bottom-hit-height) -1))) ;from corner, inverse 45 degree scope
  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) 0))) ;from center, flat scope
  (is (false? (ball-lands-on-right (/ max-width 2) (/ max-height 2) 1))) ;from center, 45 degree scope
  (is (false? (ball-lands-on-right (/ max-width 2) (/ max-height 2) -1))) ;from center, inverse 45 degree scope
  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) 480/640))) ;from center, diagonal scope
  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) -480/640))) ;from center, inverse diagonal scope
  )
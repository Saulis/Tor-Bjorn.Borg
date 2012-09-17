(ns tests.geometry-tests
  (:use clojure.test
        borg.geometry
        borg.constants))

(deftest slope-tests
  (is (= (slope (to-point 0 0) (to-point 5 5 )) 1))
  (is (= (slope (to-point 0 0) (to-point -5 -5 )) 1))
  (is (= (slope (to-point 0 0) (to-point -5 5 )) -1)))

(deftest ball-lands-on-left-tests
  (is (false? (ball-lands-on-left (left-hit-width) (bottom-hit-height) 0))) ;from corner, flat scope
  (is (false? (ball-lands-on-left (left-hit-width) (bottom-hit-height) 1))) ;from corner, 45 degree scope
  (is (false? (ball-lands-on-left (left-hit-width) (bottom-hit-height) -1))) ;from corner, inverse 45 degree scope
  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) 0))) ;from center, flat scope
  (is (false? (ball-lands-on-left (/ max-width 2) (/ max-height 2) 1))) ;from center, 45 degree scope
  (is (false? (ball-lands-on-left (/ max-width 2) (/ max-height 2) -1))) ;from center, inverse 45 degree scope
  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) 480/640))) ;from center, diagonal scope
  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) -480/640))) ;from center, inverse diagonal scope
  )

(deftest ball-lands-on-right-tests
  (is (false? (ball-lands-on-right (right-hit-width) (bottom-hit-height) 0))) ;from corner, flat scope
  (is (false? (ball-lands-on-right (right-hit-width) (bottom-hit-height) 1))) ;from corner, 45 degree scope
  (is (false? (ball-lands-on-right (right-hit-width) (bottom-hit-height) -1))) ;from corner, inverse 45 degree scope
  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) 0))) ;from center, flat scope
  (is (false? (ball-lands-on-right (/ max-width 2) (/ max-height 2) 1))) ;from center, 45 degree scope
  (is (false? (ball-lands-on-right (/ max-width 2) (/ max-height 2) -1))) ;from center, inverse 45 degree scope
  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) 480/640))) ;from center, diagonal scope
  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) -480/640))) ;from center, inverse diagonal scope
  (is (true? (ball-lands-on-right (left-hit-width) (top-hit-height) -480/640)))
  )

(deftest ball-lands-on-bottom-tests
  (is (true? (ball-lands-on-bottom (left-hit-width) (top-hit-height) -1)))
  (is (false? (ball-lands-on-bottom (left-hit-width) (top-hit-height) -480/640)))
  (is (false? (ball-lands-on-bottom (left-hit-width) (top-hit-height) 1)))
  (is (false? (ball-lands-on-bottom (left-hit-width) (bottom-hit-height) 1))) ;already on bottom
  )

(deftest ball-landings-bug-tests
  (is (true? (ball-lands-on-right 636 422 9/16)))
  (is (false? (ball-lands-on-bottom 636 422 9/16)))
  (is (false? (ball-lands-on-top 636 422 9/16)))
  )


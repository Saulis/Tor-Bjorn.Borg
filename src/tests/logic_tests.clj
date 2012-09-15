(ns tests.logic-tests
  (use clojure.test
       borg.logic))

(deftest new-direction-is-accurate-tests
  (is (not (new-direction-is-accurate nil))) ;nil - false
  (is (not (new-direction-is-accurate []))) ;empty - false
  (is (not (new-direction-is-accurate [{:x 1 :y 2}]))) ;one position - false
  (is (new-direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}])) ;two positions - true
  (is (new-direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 3 :y 6}])) ;three positions with steady slope - true
  (is (not (new-direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 13 :y 123}])))) ;three positions with unsteady slope - false

(deftest landing-height-tests
  (is (= (landing-height 0 240 0) 240)) ;straight across the field
  (is (= (landing-height 0 240 640/480) 260)) ;one bounce left to right
  (is (= (landing-height 640 240 -640/480) 260)) ;one bounce right to left
  (is (= (landing-height 0 240 320/480) 250)) ;three bounces left to right
  (is (= (landing-height 640 240 320/480) 250)) ;three bounces right to left
  (is (= (landing-height 0 0 640/480) 20)) ;from corner to corner left to right
  (is (= (landing-height 640 0 -640/480) 20))) ;from corner to corner right to left


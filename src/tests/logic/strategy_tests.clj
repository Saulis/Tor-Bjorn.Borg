(ns tests.logic.strategy-tests
  (:use clojure.test
        borg.logic.strategy))

(deftest new-direction-is-accurate-tests
  (is (not (direction-is-accurate nil))) ;nil - false
  (is (not (direction-is-accurate []))) ;empty - false
  (is (not (direction-is-accurate [{:x 1 :y 2}]))) ;one position - false
  (is (direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}])) ;two positions - true
  (is (direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 3 :y 6}])) ;three positions with steady slope - true
  (is (not (direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 13 :y 123}])))) ;three positions with unsteady slope - false


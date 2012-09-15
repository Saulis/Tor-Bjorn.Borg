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



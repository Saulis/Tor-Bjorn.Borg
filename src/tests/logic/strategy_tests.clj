(ns tests.logic.strategy-tests
  (:use clojure.test
        borg.constants
        borg.logic.strategy))

(deftest new-direction-is-accurate-tests
  (is (not (direction-is-accurate nil))) ;nil - false
  (is (not (direction-is-accurate []))) ;empty - false
  (is (not (direction-is-accurate [{:x 1 :y 2}]))) ;one position - false
  (is (direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}])) ;two positions - true
  (is (direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 3 :y 6}])) ;three positions with steady slope - true
  (is (not (direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 13 :y 123}]))) ;three positions with unsteady slope - false
  (is (not (direction-is-accurate [{:x 1 :y 2} {:x 1 :y 3} {:x 2 :y 4}]))) ; invalid slope with three points
  (is (not (direction-is-accurate [{:x 1 :y 2} {:x 2 :y 3} {:x 2 :y 4}]))) ; invalid slope with three points
  (is (not (direction-is-accurate [{:x 1 :y 2} {:x 1 :y 4}]))) ; invalid slope with three points
)

(deftest target-height-tests
  (is (= (target-height nil)) mid-height)
  (is (= (target-height [])) mid-height)
  (is (= (target-height [0])) mid-height)
  (is (= (target-height [0 1])) mid-height)
  )

(def game-data-1 [{:time 1348244790110, :left {:y 344.10063065067453, :playerName "Saulis"}, :right {:y 40.6829479057853, :playerName "karp"}, :ball {:pos {:x 413.033371709369, :y 135.996419031424}}}
                  {:time 1348244790260, :left {:y 333.0008943729551, :playerName "Saulis"}, :right {:y 54.6829479057853, :playerName "karp"}, :ball {:pos {:x 356.4251060866989, :y 169.6717361090724}}}
                  {:time 1348244790350, :left {:y 328.56509480188856, :playerName "Saulis"}, :right {:y 64.6829479057853, :playerName "karp"}, :ball {:pos {:x 315.99063064193456, :y 193.7255340216784}}}])

(deftest game-tests
  (is (= (target-height game-data-1) -1)))
(ns tests.logic.direction-tests
  (:use clojure.test
        borg.logic.direction))

;;; remember, target height offset is added to target height (-25)
(deftest direction-tests
  (is (= (direction 100 0) 1.0))
  (is (= (direction 100 50) 1))
  (is (= (direction 100 62.5) 0.5))
  (is (= (direction 100 75) 0))
  (is (= (direction 112.5 100) -0.5))
  (is (= (direction 100 100) -1))
  (is (= (direction 75 100) -1.0))
  (is (= (direction 50 100) -1.0))
  (is (= (direction 0 100) -1.0)))

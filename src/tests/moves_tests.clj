(ns tests.moves-tests
  (use clojure.test
       borg.moves))

(deftest direction-tests
  (is (= (direction 100 0) -1.0))
  (is (= (direction 100 50) -1.0))
  (is (= (direction 100 75) -0.5))
  (is (= (direction 100 100) 0))
  (is (= (direction 75 100) 0.5))
  (is (= (direction 50 100) 1.0))
  (is (= (direction 0 100) 1.0)))

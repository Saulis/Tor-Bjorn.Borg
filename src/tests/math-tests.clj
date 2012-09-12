(ns tests.math-tests
  (:use clojure.test
        borg.math))

(deftest one-hit-impact
  (testing "one hit impact")
  (is (= (:x (next-impact-point (to-point 0 0) 45)) 100)))

(run-all-tests)

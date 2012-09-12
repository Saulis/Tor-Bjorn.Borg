(ns tests.trajectories-tests
  (:use clojure.test
        borg.trajectories
        borg.math))

(deftest thirty-degree-angle-impact
  (testing "foobar"
    (is (= (next-impact-x 0 0 30) 0))))

(deftest forty-five-degree-angle-impact
  (testing "foobar"
    (is (= (:x (next-impact-x 0 0 45)) 100))
    (is (= (:y (next-impact-y 0 0 45)) 100))))

(run-all-tests )

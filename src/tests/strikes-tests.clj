(ns tests.strikes-tests
  (:use clojure.test
       borg.strikes))

(deftest nice
  (testing "foobar"
    (is 2 1)))

(run-all-tests)

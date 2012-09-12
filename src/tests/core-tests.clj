(ns tests.core-tests
  (:use borg.core
        clojure.test))

(deftest difference-between-tests
  (is (= difference-between 0 5) 5)
  (is (= difference-between 5 0) 5))

(deftest move-towards-target-position-tests
  (is (= (move-towards-target-position 50 0) move-up-full-speed))
  (is (= (move-towards-target-position 50 40) move-up-half-speed))
  (is (= (move-towards-target-position 50 50) stop))
  (is (= (move-towards-target-position 50 55) move-down-half-speed))
  (is (= (move-towards-target-position 50 100) move-down-full-speed)))

(deftest target-position-is-away-tests
  (is (false? (target-position-is-away 0 5)))
  (is (target-position-is-away 0 6)))

;(run-all-tests )
(-main (str "Tor-Bjorn.Borg") (str "boris.helloworldopen.fi") (str "9090"))

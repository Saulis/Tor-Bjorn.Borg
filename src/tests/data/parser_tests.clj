(ns tests.data.parser-tests
  (:use clojure.test
        borg.data.parser))

(defn- ball [x y]
  {:ball {:pos {:x x :y y}}})

(def data [(ball 1 1) (ball 2 2) (ball 3 3) (ball 4 4)])

(deftest previous-ball-position-tests
  (is (= (previous-ball-position data) {:x 3 :y 3})))

(deftest current-ball-position-tests
  (is (= (current-ball-position data) {:x 4 :y 4})))

(deftest last-three-ball-positions-tests
  (is (= (last-three-ball-positions data) [{:x 2 :y 2} {:x 3 :y 3} {:x 4 :y 4}])))




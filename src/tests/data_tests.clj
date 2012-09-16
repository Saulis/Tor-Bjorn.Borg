(ns tests.data-tests
  (:use clojure.test
        borg.data))

(defn- ball [x y]
  {:ball {:pos {:x x :y y}}})

(def data [(ball 1 1) (ball 2 2) (ball 3 3) (ball 4 4)])

(deftest previous-ball-position-tests
  (is (= (previous-ball-position data) {:x 3 :y 3})))

(deftest current-ball-position-tests
  (is (= (current-ball-position data) {:x 4 :y 4})))

(deftest last-three-ball-positions-tests
  (is (= (last-three-ball-positions data) [{:x 2 :y 2} {:x 3 :y 3} {:x 4 :y 4}])))

(deftest saving-and-clearing-data
  (clear-data)
    (is (empty? saved-data))
  (save-data (ball 1 2))
    (is (= (count saved-data) 1))
  (clear-data)
    (is (empty? saved-data))
  )

(deftest saving-and-clearing-messages
  (clear-messages)
    (is (empty? saved-messages))
  (save-message 1.0)
    (is (= (count saved-messages) 1))
    (is (= (:time (first saved-messages))) (System/currentTimeMillis))
  (clear-messages)
    (is (empty? saved-messages))
  )



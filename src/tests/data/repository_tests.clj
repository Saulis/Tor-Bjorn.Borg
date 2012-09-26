(ns tests.data.repository-tests
  (:use clojure.test
        borg.data.repository))

(defn- ball [x y]
  {:ball {:pos {:x x :y y}}})

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

(deftest reset-direction-tests
  (clear-messages)
  (reset-direction nil)
  (is (= (count saved-messages) 0))
  (reset-direction {:time 1000 :direction 1.0})
  (is ( = (count saved-messages) 1))
  (is (= (:time (last saved-messages)) 1000))
  (is (= (:direction (last saved-messages)) 0.0))
)


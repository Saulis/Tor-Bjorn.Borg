(ns tests.messages-tests
  (use clojure.test
      borg.messages
      borg.data))

(def nine-messages (range 9))
(def ten-messages (range 10))
(def five-messages (range 5))

(defn into-time-direction [x]
  (struct time-direction (- (System/currentTimeMillis) x) 1.0))

(deftest ninth-message-tests
  (is (= (ninth-message nine-messages) 0))
  (is (= (ninth-message ten-messages) 1))
  (is (= (ninth-message five-messages) 0))
  (is (= (ninth-message []) nil)))

(deftest time-since-ninth-message-tests
  (is (= (time-since-ninth-message nil) nil))
  (is (= (time-since-ninth-message []) nil))
  (is (= (time-since-ninth-message (map into-time-direction (repeat 5 123))) 123))
  (is (= (time-since-ninth-message (map into-time-direction (repeat 9 123))) 123))
  (is (= (time-since-ninth-message (map into-time-direction (repeat 10 123))) 123)))

(deftest atleast-one-second-has-passed-since-ninth-message-tests
  (is (false? (atleast-one-second-has-passed-since-ninth-message nil)))
  (is (false? (atleast-one-second-has-passed-since-ninth-message [])))
  (is (false? (atleast-one-second-has-passed-since-ninth-message (map into-time-direction (repeat 9 995)))))
  (is (true? (atleast-one-second-has-passed-since-ninth-message (map into-time-direction (repeat 9 1000))))))

(deftest nine-messages-have-not-been-sent-under-one-second-tests
  (is (true? (nine-messages-have-not-been-sent-under-one-second (map into-time-direction (repeat 8 995)))))
  (is (false? (nine-messages-have-not-been-sent-under-one-second (map into-time-direction (repeat 9 995)))))
  (is (true? (nine-messages-have-not-been-sent-under-one-second (map into-time-direction (repeat 9 1000))))))



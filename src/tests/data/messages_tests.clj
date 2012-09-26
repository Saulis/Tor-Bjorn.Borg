(ns tests.data.messages-tests
  (use clojure.test
      borg.data.messages
      borg.data.repository))

(def nine-messages (map inc (reverse (range 9))))
(def ten-messages (map inc (reverse (range 10))))
(def twenty-messages (map inc (reverse (range 20))))
(def nineteen-messages (map inc (reverse (range 19))))
(def five-messages (map inc (reverse (range 5))))

(defn into-time-direction [x]
  (struct time-direction (- (System/currentTimeMillis) x) 1.0))

(deftest nineteenth-message-tests
  (is (= (nineteenth-message nine-messages) 9))
  (is (= (nineteenth-message ten-messages) 10))
  (is (= (nineteenth-message five-messages) 5))
  (is (= (nineteenth-message nineteen-messages) 19))
  (is (= (nineteenth-message twenty-messages) 19))
  (is (= (nineteenth-message []) nil)))

(deftest time-since-nineteenth-message-tests
  (is (= (time-since-nineteenth-message nil) nil))
  (is (= (time-since-nineteenth-message []) nil))
  (is (= (time-since-nineteenth-message (map into-time-direction (repeat 5 123))) 123))
  (is (= (time-since-nineteenth-message (map into-time-direction (repeat 9 123))) 123))
  (is (= (time-since-nineteenth-message (map into-time-direction (repeat 10 123))) 123))
  (is (= (time-since-nineteenth-message (map into-time-direction (repeat 18 123))) 123))
  (is (= (time-since-nineteenth-message (map into-time-direction (repeat 19 123))) 123))
  (is (= (time-since-nineteenth-message (map into-time-direction (repeat 20 123))) 123))
  )

(deftest atleast-two-seconds-has-passed-since-nineteenth-message-tests
  (is (false? (atleast-two-seconds-has-passed-since-nineteenth-message nil)))
  (is (false? (atleast-two-seconds-has-passed-since-nineteenth-message [])))
  (is (false? (atleast-two-seconds-has-passed-since-nineteenth-message (map into-time-direction (repeat 9 995)))))
  (is (false? (atleast-two-seconds-has-passed-since-nineteenth-message (map into-time-direction (repeat 9 1000)))))
  (is (true? (atleast-two-seconds-has-passed-since-nineteenth-message (map into-time-direction (repeat 18 2000)))))
  (is (false? (atleast-two-seconds-has-passed-since-nineteenth-message (map into-time-direction (repeat 19 1995)))))
  (is (true? (atleast-two-seconds-has-passed-since-nineteenth-message (map into-time-direction (repeat 19 2000)))))
  (is (true? (atleast-two-seconds-has-passed-since-nineteenth-message (map into-time-direction (repeat 20 2000)))))
  )

(deftest nineteen-messages-have-not-been-sent-under-two-seconds-tests
  (is (true? (nineteen-messages-have-not-been-sent-under-two-seconds (map into-time-direction (repeat 8 995)))))
  (is (true? (nineteen-messages-have-not-been-sent-under-two-seconds (map into-time-direction (repeat 9 995)))))
  (is (true? (nineteen-messages-have-not-been-sent-under-two-seconds (map into-time-direction (repeat 9 1000)))))
  (is (false? (nineteen-messages-have-not-been-sent-under-two-seconds (map into-time-direction (repeat 19 1995)))))
  (is (true? (nineteen-messages-have-not-been-sent-under-two-seconds (map into-time-direction (repeat 19 2000)))))
  )




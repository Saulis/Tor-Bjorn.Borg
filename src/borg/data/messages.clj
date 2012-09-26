(ns borg.data.messages)

(defn nineteen-messages-have-not-been-sent [messages]
  (< (count messages) 19))

(defn nineteenth-message [messages]
  (first (take-last 19 messages)))

(defn time-of-nineteenth-message [messages]
  (:time (nineteenth-message messages)))

(defn time-since-nineteenth-message [messages]
  (if (empty? messages)
    nil
    (- (System/currentTimeMillis) (time-of-nineteenth-message messages))))

(defn atleast-two-seconds-has-passed-since-nineteenth-message [messages]
  (if (empty? messages)
    false
    (>= (time-since-nineteenth-message messages) 2000)))

; Using ten messages per sec was kinda pushing it - got kicked from the server a few times. Let's go with nineteen msgs / two seconds.
(defn nineteen-messages-have-not-been-sent-under-two-seconds [messages]
  (or
    (nineteen-messages-have-not-been-sent messages)
    (atleast-two-seconds-has-passed-since-nineteenth-message messages)))
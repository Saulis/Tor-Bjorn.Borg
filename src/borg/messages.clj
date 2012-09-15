(ns borg.messages)

(defn nine-messages-have-not-been-sent [messages]
  (< (count messages) 9))

(defn ninth-message [messages]
  (first (take-last 9 messages)))

(defn time-of-ninth-message [messages]
  (:time (ninth-message messages)))

(defn time-since-ninth-message [messages]
  (if (empty? messages)
    nil
    (- (System/currentTimeMillis) (time-of-ninth-message messages))))

(defn atleast-one-second-has-passed-since-ninth-message [messages]
  (if (empty? messages)
    false
    (>= (time-since-ninth-message messages) 1000)))

;Using ten messages was kinda pushing it - got kicked from the server a few times. Let's go with nine msgs / second.
(defn nine-messages-have-not-been-sent-under-one-second [messages]
  (or
    (nine-messages-have-not-been-sent messages)
    (atleast-one-second-has-passed-since-ninth-message messages)))
(ns borg.data.messages)

(defn- time-of-message [message]
  (:time message))

(defn nineteen-messages-have-not-been-sent [messages]
  (< (count messages) 19))

(defn nineteenth-message [messages]
  (first (take-last 19 messages)))

(defn- time-since-message [message]
  (- (System/currentTimeMillis) (time-of-message message)))

(defn time-of-nineteenth-message [messages]
  (time-of-message (nineteenth-message messages)))

(defn time-since-nineteenth-message [messages]
  (if (empty? messages)
    nil
    (time-since-message (nineteenth-message messages))))

(defn atleast-two-seconds-has-passed-since-nineteenth-message [messages]
  (if (empty? messages)
    false
    (>= (time-since-nineteenth-message messages) 2000)))

; Using ten messages per sec was kinda pushing it - got kicked from the server a few times. Let's go with nineteen msgs / two seconds.
(defn nineteen-messages-have-not-been-sent-under-two-seconds [messages]
  (or
    (nineteen-messages-have-not-been-sent messages)
    (atleast-two-seconds-has-passed-since-nineteenth-message messages)))

(defn time-since-oldest-message [messages]
  (if (empty? messages)
    0
    (time-since-message (first messages))))

(defn time-since-previous-message [messages]
  (if (< (count messages) 2)
    0
    (time-since-message (second (reverse messages)))))
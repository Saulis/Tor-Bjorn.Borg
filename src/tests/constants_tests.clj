(ns tests.constants-tests
  (:use clojure.test
        borg.constants))

(def data {:time 1348299989618, :left {:y 240.0, :playerName "Tor-Bjorn.Borg"}, :right {:y 236.0, :playerName "becker"}, :ball {:pos {:x 88.39222656867298, :y 161.3650985198049}}, :conf {:maxWidth 320, :maxHeight 120, :paddleHeight 25, :paddleWidth 10, :ballRadius 15, :tickInterval 30}})
(def default-data {:time 1348299989618, :left {:y 240.0, :playerName "Tor-Bjorn.Borg"}, :right {:y 236.0, :playerName "becker"}, :ball {:pos {:x 88.39222656867298, :y 161.3650985198049}}, :conf {:maxWidth 640, :maxHeight 480, :paddleHeight 50, :paddleWidth 10, :ballRadius 5, :tickInterval 30}})

(deftest update-constants-tests
  (update-constants data)
  (is (= max-height 120))
  (is (= max-width 320))
  (is (= paddle-height 25))
  (is (= paddle-width 10))
  (is (= ball-radius 15))
  (is (= half-paddle-height 25/2))
  (is (= mid-height 120/2))
  )

(deftest contants-tests
  (update-constants default-data)
  (is (= (bottom-hit-height) 475))
  (is (= (top-hit-height) 5))
  (is (= (left-hit-width) 15))
  (is (= (right-hit-width) 625)))

(ns tests.constants-tests
  (:use clojure.test
        borg.constants))

(def data {:time 1348299989618, :left {:y 240.0, :playerName "Tor-Bjorn.Borg"}, :right {:y 236.0, :playerName "becker"}, :ball {:pos {:x 88.39222656867298, :y 161.3650985198049}}, :conf {:maxWidth 320, :maxHeight 120, :paddleHeight 25, :paddleWidth 10, :ballRadius 15, :tickInterval 30}})

(deftest update-constants-tests
  (update-constants data)
  (is (= max-height 120))
  (is (= max-width 320))
  (is (= paddle-height 25))
  (is (= paddle-width 10))
  (is (= ball-radius 15))

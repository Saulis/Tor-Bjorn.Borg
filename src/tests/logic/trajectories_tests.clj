(ns tests.logic.trajectories-tests
  (:use clojure.test
        borg.logic.trajectories))

(deftest landing-height-tests
  (is (= (landing-height {:x 0 :y 240} {:x 200 :y 240}) 240.0)) ;straight across the field
  (is (= (landing-height {:x 0 :y 240} {:x 320 :y 480}) 251.25)) ;one top bounce left to right
  (is (= (landing-height {:x 0 :y 240} {:x 320 :y 0}) 228.75)) ;one bottom bounce left to right
  )

(deftest landing-height-bug-tests
  (testing "games where bot has crashed previously")
  (is (pos? (landing-height {:x 109.52451670545771, :y 371.66836747085847} {:x 129.16086527855018, :y 358.00311467753477})))
  (is (pos? (landing-height {:x 35.944477605908055, :y 139.73909717189375} {:x 50.743817428259156, :y 122.08013824877912})))
  (is (pos? (landing-height {:x 620.6255119707167, :y 413.1384143251785} {:x 636.2962115583583, :y 422.4917330408214})))
  )

(defn- test-landing-height [p]
  ;(info p)
  (landing-height {:x 320 :y 240} p))

(defn- points-for [x]
  (for [y (range 5 475)]
    {:x x :y y}))

(defn- foo []
  (map points-for (range 15 319)))


;(deftest crash-test
;  (info (map test-landing-height (foo))))

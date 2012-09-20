(ns tests.logic.trajectories-crash-tests
  (:use clojure.test
        borg.logic.trajectories))

(defn- points-for [x]
  (for [y (range 0 480)]
    {:x x :y y}))

(defn- all-points-until [until]
  (for [x (range 0 until)]
    (points-for x)))

(deftest landing-height-crash-left-from-middle-point-test
  (doseq [p2 (flatten (all-points-until 319))]
    (try
      (let [h (landing-height {:x 320, :y 240} p2)]
      (is (pos? h)))
      (catch Exception e (is (= "" (str p2 "- caught exception: " (.getMessage e))))))))


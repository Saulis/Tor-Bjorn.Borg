(ns tests.logic-tests
  (use clojure.test
       borg.logic
       borg.constants))

(deftest new-direction-is-accurate-tests
  (is (not (new-direction-is-accurate nil))) ;nil - false
  (is (not (new-direction-is-accurate []))) ;empty - false
  (is (not (new-direction-is-accurate [{:x 1 :y 2}]))) ;one position - false
  (is (new-direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}])) ;two positions - true
  (is (new-direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 3 :y 6}])) ;three positions with steady slope - true
  (is (not (new-direction-is-accurate [{:x 1 :y 2} {:x 2 :y 4}, {:x 13 :y 123}])))) ;three positions with unsteady slope - false

;(deftest landing-height-tests
;  (is (= (landing-height (left-hit-width) 240 0) 240)) ;straight across the field
;  (is (= (landing-height (left-hit-width) 240 480/640) 260)) ;one bounce left to right
;  (is (= (landing-height (right-hit-width) 240 -480/640) 260)) ;one bounce right to left
;  (is (= (landing-height (left-hit-width) 240 480/320) 250)) ;three bounces left to right
;  (is (= (landing-height (right-hit-width) 240 480/320) 250)) ;three bounces right to left
;  (is (= (landing-height (left-hit-width) (bottom-hit-height) 480/640) 20)) ;from corner to corner left to right
;  (is (= (landing-height (right-hit-width) (bottom-hit-height) -480/640) 20))) ;from corner to corner right to left

(deftest landing-height-bug-tests
  (testing "games where bot has crashed previously")
  (is (pos? (landing-height {:x 109.52451670545771, :y 371.66836747085847} {:x 129.16086527855018, :y 358.00311467753477})))
  (is (pos? (landing-height {:x 35.944477605908055, :y 139.73909717189375} {:x 50.743817428259156, :y 122.08013824877912})))
  (is (pos? (landing-height {:x 620.6255119707167, :y 413.1384143251785} {:x 636.2962115583583, :y 422.4917330408214})))
  )



;(deftest ball-lands-on-corners-from-corner-tests
;  (testing "making sure corner hits won't overlap functions")
;  (is (true? (ball-lands-on-left (left-hit-width) (top-hit-height) 480/640)))
;  (is (false? (ball-lands-on-bottom (left-hit-width) (top-hit-height) 480/640)))
;  (is (false? (ball-lands-on-right (left-hit-width) (top-hit-height) 480/640)))
;  (is (true? (ball-lands-on-left (left-hit-width) (top-hit-height) -480/640)))
;  (is (false? (ball-lands-on-bottom (left-hit-width) (top-hit-height) -480/640)))
;  (is (true? (ball-lands-on-right (left-hit-width) (top-hit-height) -480/640)))
;  )

;(deftest ball-lands-on-corners-from-center-tests
;  (testing "making sure corner hits won't overlap functions")
;  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) 480/640))) ;to left upper corner
;  (is (false? (ball-lands-on-bottom (/ max-width 2) (/ max-height 2) 480/640))) ;to right down corner
;  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) 480/640))) ;to right down corner
;  (is (true? (ball-lands-on-left (/ max-width 2) (/ max-height 2) -480/640))) ;to left down corner
;  (is (false? (ball-lands-on-bottom (/ max-width 2) (/ max-height 2) -480/640))) ;to left down corner
;  (is (true? (ball-lands-on-right (/ max-width 2) (/ max-height 2) -480/640))) ;to right upper corner
;  )

(run-all-tests)
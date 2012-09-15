(ns borg.constants)

(def ball-velocity 100)
(def court-height 480)
(def court-width 640)

(def max-width 640)
(def max-height 480)
(def ball-radius 5)
(def paddle-width 10)
(def paddle-height 50)

(defn top-hit-height []
  (- max-height ball-radius))

(defn bottom-hit-height []
  (+ 0 ball-radius ))

(defn left-hit-width []
  (+ 0 (+ paddle-width ball-radius)))

(defn right-hit-width []
  (- max-width (+ paddle-width ball-radius)))

(defn is-hit-height-inside [height]
  (and
    (>= height (bottom-hit-height))
    (<= height (top-hit-height))))

(defn is-hit-width-inside [width]
  (and
    (>= width (left-hit-width))
    (<= width (right-hit-width))))

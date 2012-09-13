(ns borg.core
  (:use [clojure.data.json :only (read-json json-str)]
        [clojure.tools.logging :only (info error)]
        clojure.contrib.math
        borg.data
        borg.math
        borg.constants)
  (:import [java.net Socket]
           [java.io PrintWriter InputStreamReader BufferedReader])
  (:gen-class :main true))

(def move-up-full-speed {:msgType "changeDir" :data -1.0})
(def move-up-half-speed {:msgType "changeDir" :data -0.5})
(def move-down-full-speed {:msgType "changeDir" :data 1.0})
(def move-down-half-speed {:msgType "changeDir" :data 0.5})
(def stop {:msgType "changeDir" :data 0})

(defn move [direction]
  {:msgType "changeDir" :data direction})

(defn write [conn data]
  (doto (:out @conn)
    (.println (json-str data))
    (.flush)))

(defn is-it-time-to-move [{time :time}]
  (>= (mod time 200) 100))

(defn ball-is-up [{y :y} {ball :pos}]
  (< y (:y ball)))

(defn to-direction [{left :left ball :ball}]
  (if (ball-is-up left ball)
    -1.0
    1.0))

(defn enough-data-to-move []
  (>= (count cached-data) 3))

(defn difference-between [target-position current-position]
  (abs (- target-position current-position)))

(defn move-up-at-speed [target-position current-position]
  (if (> (difference-between target-position current-position) 5)
    move-up-full-speed
    move-up-half-speed))

(defn move-down-at-speed [target-position current-position]
  (if (> (difference-between target-position current-position) 5)
    move-down-full-speed
    move-down-half-speed))

(defn move-at-speed [target-position current-position]
  (if (> target-position current-position)
    (move-up-at-speed target-position current-position)
    (move-down-at-speed target-position current-position)))


(defn target-position-is-away [target-position current-position]
  (>= (difference-between target-position current-position) 5))

(defn move-towards-target-height [target-height current-height]
  (if (> target-height current-height)
    move-down-full-speed
    move-up-full-speed))

;(defn move-towards-target-position [target-position current-position]
;    (info (str "Current position: " current-position))
;    (if (target-position-is-away target-position current-position)
;      (move-at-speed target-position current-position)
;      stop))

(defn current-height [{left :left}]
  (:y left))

(defn get-target-position [previous-data current-data]
  )

(defn foo-target-height []
  (if (> (count cached-data ) 2)
  (:y (new-target-position (previous-ball-position) (current-ball-position)))
  200))

(defn next-move [data]
  (move-towards-target-height (foo-target-height) (current-height data)))

(defn ball-is-too-close-to-borders [{pos :pos}]
  (or (<= (:x pos) 50)
    (<= (:y pos) 50)
    (>= (:x pos) 600)
    (>= (:y pos) 430)))

(defn gather-data [data]
  (info (str "Target position: " target-position ))
  (update-data data))


(defn handle-data [conn data]
  (refresh-data data)
  (if (is-it-time-to-move data)
    (write conn (next-move data))))


(defn start-playing [data]
  (info (str "Game started: " (nth data 0) " vs. " (nth data 1)))
  (clear-data))

(defn handle-message [conn {msgType :msgType data :data}]
  (case msgType
    joined (info (str "Game joined successfully. Use following URL for visualization: " data))
    gameStarted (info (str "Game started: " (nth data 0) " vs. " (nth data 1)))
    gameIsOn (handle-data conn data)
    gameIsOver (info (str "Game ended. Winner: " data))
    error (error data)
    'pass))

(defn parse-message [data]
  (try
    (let [msg (read-json data)]
      {:msgType (symbol (:msgType msg))
       :data    (:data msg)})
    (catch Throwable e {:msgType 'error :data (. e getMessage)})))

(defn conn-handler [conn]
  (while (nil? (:exit @conn))
    (let [msg (.readLine (:in @conn))]
      (cond
        (nil? msg) (dosync (alter conn merge {:exit true}))
        :else (handle-message conn (parse-message msg))))))

(defn connect [server]
  (let [socket (Socket. (:name server) (:port server))
        in (BufferedReader. (InputStreamReader. (.getInputStream socket)))
        out (PrintWriter. (.getOutputStream socket))
        conn (ref {:in in :out out})]
    (doto (Thread. #(conn-handler conn)) (.start))
    conn))

(defn -main [team-name hostname port]
  (let [s (connect {:name hostname :port (read-string port)})
        join-message {:msgType "join" :data team-name}]
    (write s join-message)))
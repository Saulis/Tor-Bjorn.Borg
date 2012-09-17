(ns borg.core
  (:use [clojure.data.json :only (read-json json-str)]
        [clojure.tools.logging :only (info error)]
        clojure.contrib.math
        borg.logic
        borg.data
        borg.geometry
        borg.constants
        borg.moves
        borg.messages)
  (:import [java.net Socket]
           [java.io PrintWriter InputStreamReader BufferedReader])
  (:gen-class :main true))

(defn move-to [direction]
  {:msgType "changeDir" :data direction})

(defn write [conn data]
  (doto (:out @conn)
    (.println (json-str data))
    (.flush)))

(defn new-direction []
  (direction (target-height saved-data) (current-height saved-data)))

(defn it-is-time-to-change-direction []
  (and
    (new-direction-is-accurate (last-three-ball-positions saved-data))
    (nine-messages-have-not-been-sent-under-one-second saved-messages)))

(defn change-direction [conn]
  (save-message (new-direction))
  (info (str "Direction: " (System/currentTimeMillis) " " (new-direction))) ;TODO debug
  (write conn (move-to (new-direction))))

(defn handle-data [conn data]
  (info data) ;TODO debug
  (save-data data)
  (if (it-is-time-to-change-direction)
    (change-direction conn)))

(defn start-playing [data]
  (info (str "Game started: " (nth data 0) " vs. " (nth data 1)))
  (clear-data))

(defn handle-message [conn {msgType :msgType data :data}]
  (case msgType
    joined (info (str "Game joined successfully. Use following URL for visualization: " data))
    gameStarted (start-playing data)
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
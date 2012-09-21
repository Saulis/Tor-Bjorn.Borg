(ns borg.core
  (:use [clojure.data.json :only (read-json json-str)]
        [clojure.tools.logging :only (info error)]
        borg.data.repository
        borg.data.parser
        borg.data.messages
        borg.logic.strategy
        borg.logic.direction)
  (:import [java.net Socket]
           [java.io PrintWriter InputStreamReader BufferedReader])
  (:gen-class :main true))

(defn move-to [direction]
  {:msgType "changeDir" :data direction})

(defn write [conn data]
  (doto (:out @conn)
    (.println (json-str data))
    (.flush)))

(defn new-direction [data]
  (direction (target-height data) (current-height data)))

(defn it-is-time-to-send-direction-message []
  (and
    (direction-is-accurate (last-three-ball-positions saved-data))
    (nine-messages-have-not-been-sent-under-one-second saved-messages)))

(defn send-direction-message [conn]
  (save-message (new-direction saved-data))
  (println (str "Direction: " (System/currentTimeMillis) " " (new-direction saved-data))) ;debug
  (write conn (move-to (new-direction saved-data))))

(defn handle-data [conn data]
  (println (str "Data: " data)) ;debug
  (save-data data)
  (if (it-is-time-to-send-direction-message)
    (send-direction-message conn)))

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

(defn -main
  ([team-name opponent-name hostname port]
  (let [s (connect {:name hostname :port (read-string port)})
        join-message {:msgType "requestDuel" :data [team-name opponent-name]}]
    (write s join-message)))

  ([team-name hostname port]
  (let [s (connect {:name hostname :port (read-string port)})
        join-message {:msgType "join" :data team-name}]
    (write s join-message))))
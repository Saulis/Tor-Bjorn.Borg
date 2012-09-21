(ns tests.core-tests
  (:use borg.core
        borg.data.repository
        clojure.test))

(deftest start-playing-clears-saved-data
  (is (empty? saved-data))
  (save-data "foo")
  (is (not (empty? saved-data)))
  (start-playing ["JohnMcEnroe", "BorisBecker"])
  (is (empty? saved-data))
)

(deftest parse-message-invalid-message-tests
  (is (= (:msgType (parse-message "foobar")) 'error))
  (is (not (empty? (:data (parse-message "foobar")))))
  )

(-main (str "Tor-Bjorn.Borg") (str "boris.helloworldopen.fi") (str "9090"))
(ns lang-service.utils
  (:require [cheshire.core :refer [generate-string parse-string]]))


(defn json-response
  [m]
  {:status 200 :headers {"Content-Type" "application/json"} :body (generate-string m)})



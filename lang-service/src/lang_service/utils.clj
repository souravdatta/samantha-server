(ns lang-service.utils
  (:require [cheshire.core :refer [generate-string parse-string]])
  (:import (java.security MessageDigest)
           (java.math BigInteger)))


(defn json-response
  [m]
  {:status 200 :headers {"Content-Type" "application/json"} :body (generate-string m)})


(defn md5 [^String s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        raw (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))


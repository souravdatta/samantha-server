(ns lang-service.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [lang-service.utils :refer [json-response md5]]
            [lang-service.rules :refer [match-all-rules]]
            [lang-service.db :refer [save-query]])
  (:import (java.util Date)))


(defn query-handler
  [query]
  (let [rule-match (match-all-rules query)
        response (if rule-match rule-match {:reply :fail})
        result (merge 
                 {:query query} 
                 {:md5 (md5 (clojure.string/join " " (response :data)))}
                 {:datetime (Date.)}
                 response)]
    (save-query result)
    (json-response result)))


(defroutes app-routes
  (GET "/" [] "Lang service up and running")
  (GET "/hello/:name" [name] (json-response {:reply (str "hello, " name) :context :simple-reply}))
  (GET "/query/:query" [query] (query-handler query))
  (route/not-found "Not Found"))


(def app
  (wrap-defaults app-routes site-defaults))


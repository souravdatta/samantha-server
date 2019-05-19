(ns lang-service.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [lang-service.utils :refer [json-response md5]]
            [lang-service.rules :refer [match-all-rules]]
            [lang-service.db :refer [open-mongo-connection! save-query! save-process-queue!]])
  (:import (java.util Date)
           (java.util UUID)))


(defn query-handler
  [query]
  (let [rule-match (match-all-rules query)
        response (if rule-match rule-match {:reply :fail})
        result (merge 
                 {:query query} 
                 {:md5 (md5 (clojure.string/join " " (response :data)))}
                 {:datetime (Date.)}
                 {:text_id (.toString (UUID/randomUUID))}
                 response)]
    (dosync
      (save-query! result)
      (save-process-queue! (result :text_id)))
    (json-response result)))


(defroutes app-routes
  (GET "/" [] "Lang service up and running")
  (GET "/hello/:name" [name] (json-response {:reply (str "hello, " name) :context :simple-reply}))
  (GET "/query/:query" [query] (query-handler query))
  (route/not-found "Not Found"))


(def app
  (do
    (open-mongo-connection!)
    (wrap-defaults app-routes site-defaults)))


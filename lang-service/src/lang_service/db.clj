(ns lang-service.db
  (:require [lang-service.configs :refer [cfg]]
            [monger.core :as mg]
            [monger.collection :as mc]))


(def mongo-con (atom nil))


(defn open-mongo-connection!
  []
  (swap! mongo-con mg/connect))


(defn save-query!
  [response]
  (let [db (mg/get-db @mongo-con (cfg :mongodb))
        uid (if (response :uid) (response :uid) 1)]
    (mc/insert db "queries" (merge response {:uid uid} {:processed false}))))


(defn save-process-queue!
  [uuid]
  (let [db (mg/get-db @mongo-con (cfg :mongodb))]
    (mc/insert db "process_queue" {:text_id uuid})))
    




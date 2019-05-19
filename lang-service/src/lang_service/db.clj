(ns lang-service.db
  (:require [lang-service.configs :refer [cfg]]
            [monger.core :as mg]
            [monger.collection :as mc]))


(defn save-query
  [response]
  (let [con (mg/connect)
        db (mg/get-db con (cfg :mongodb))
        uid (if (response :uid) (response :uid) 1)]
    (mc/insert db "queries" (merge response {:uid uid} {:processed false}))))


    




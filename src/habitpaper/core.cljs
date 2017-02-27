(ns habitpaper.core
  (:refer-clojure :exclude [get update])
  (:require [cljs-lambda.macros :refer-macros [defgateway]]
            [cljs.core.async :refer [<! put! close! chan]]
            [cljs.nodejs :as node]
            [cljs.pprint :as pprint])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def https (node/require "https"))

(defn get [host path headers]
  (let [c (chan)
        http-c (chan)]
    (.get https #js {:host host
                     :path path
                     :headers (clj->js headers)}
          (fn [response]
            (.on response "data" #(put! http-c %))
            (.on response "end" #(close! http-c))))
    (go-loop [resp ""]
      (if-let [next-packet (<! http-c)]
        (recur (str resp next-packet))
        (put! c resp)))
    c))

(defgateway update [{{:keys [habitica-user-id habitica-api-token] :as stage-variables} :stage-variables} ctx]
  (let [c (chan)
        get-c (get "habitica.com" "/api/v3/tasks/user?type=todos"
                   {:x-api-user habitica-user-id
                    :x-api-key habitica-api-token})]
    (go
      (let [resp (<! get-c)]
        (js/console.log resp))
      (put! c {:status 200}))
    c))

(ns habitpaper.core
  (:refer-clojure :exclude [get update])
  (:require [cljs-lambda.macros :refer-macros [defgateway]]
            [cljs.core.async :refer [<! chan close! put!]]
            [cljs.nodejs :as nodejs])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def https (nodejs/require "https"))

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

(def Dropbox (nodejs/require "dropbox"))

(defgateway update [_ _]
  (let [habitica-user-id process.env.HABITICA_USER_ID
        habitica-api-token process.env.HABITICA_API_TOKEN
        dropbox-access-token process.env.DROPBOX_ACCESS_TOKEN
        c (chan)
        get-c (get "habitica.com" "/api/v3/tasks/user?type=todos"
                   {:x-api-user habitica-user-id
                    :x-api-key habitica-api-token})]
    (go
      (let [resp (<! get-c)]
        (js/console.log resp))
      (-> (Dropbox. #js {:accessToken dropbox-access-token})
          (.filesDownload #js {:path "/TaskPaper.taskpaper"})
          (.then #(do
                    (js/console.log (.-fileBinary %))
                    (done))))
      (put! c {:status 200}))
    c))

(defgateway dropbox-verify [{:keys [query]} _]
  {:status 200
   :body (:challenge query)})

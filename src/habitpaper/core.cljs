(ns habitpaper.core
  (:require [cljs-lambda.macros :refer-macros [deflambda defgateway]]
            [cljs.pprint :as pprint]))

(defgateway log [{:keys [body] :as req} ctx]
  (let [data (js/JSON.parse body)]
    (js/console.log (with-out-str (pprint/pprint data)))
    {:status  200
     :headers {:content-type "text/plain"}
     :body    (with-out-str (pprint/pprint req))}))

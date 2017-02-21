(ns habitpaper.core
  (:require [cljs-lambda.macros :refer-macros [deflambda defgateway]]
            [cljs.pprint :as pprint]))

(defgateway log [{:keys [body query] :as req} ctx]
  (js/console.log (with-out-str (pprint/pprint (js/JSON.parse body))))
  (if-let [dropbox-challenge (:challenge query)]
    {:status 200
     :headers {:content-type "text/plain"}
     :body dropbox-challenge}
    {:status 200
     :headers {:content-type "text/plain"}
     :body (with-out-str (pprint/pprint req))}))

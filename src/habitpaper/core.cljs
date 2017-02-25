(ns habitpaper.core
  (:require [cljs-lambda.macros :refer-macros [defgateway]]
            [cljs.pprint :as pprint]))

(defgateway update [{:keys [stage-variables]} ctx]
  (js/console.log (with-out-str (pprint/pprint stage-variables)))
  {:status 200})

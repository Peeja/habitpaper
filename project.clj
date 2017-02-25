(defproject habitpaper "0.1.0-SNAPSHOT"
  :description "FIXME"
  :url "http://please.FIXME"
  :dependencies [[org.clojure/clojure       "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [org.clojure/core.async    "0.2.395"]
                 [io.nervous/cljs-lambda    "0.3.4"]]
  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-npm       "0.6.0"]
            [lein-doo       "0.1.7"]
            [io.nervous/lein-cljs-lambda "0.6.4"]]
  :npm {:dependencies [[source-map-support "0.4.0"]]}
  :source-paths ["src"]
  :cljs-lambda
  {:defaults      {:role "arn:aws:iam::559676143065:role/cljs-lambda-default"}
   :resource-dirs ["static"]
   :functions
   [{:name   "habitpaper_update"
     :invoke habitpaper.core/update}]}
  :cljsbuild
  {:builds [{:id "habitpaper"
             :source-paths ["src"]
             :compiler {:output-to     "target/habitpaper/habitpaper.js"
                        :output-dir    "target/habitpaper"
                        :source-map    "target/habitpaper/habitpaper.js.map"
                        :target        :nodejs
                        :language-in   :ecmascript5
                        :optimizations :advanced}}
            {:id "habitpaper-test"
             :source-paths ["src" "test"]
             :compiler {:output-to     "target/habitpaper-test/habitpaper.js"
                        :output-dir    "target/habitpaper-test"
                        :target        :nodejs
                        :language-in   :ecmascript5
                        :optimizations :none
                        :main          habitpaper.test-runner}}]}

  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.10"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})

(defproject habitpaper "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure       "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [io.nervous/cljs-lambda    "0.3.5"]]
  :plugins [[lein-npm                    "0.6.2"]
            [lein-doo       "0.1.7"]
            [io.nervous/lein-cljs-lambda "0.6.5"]]
  :npm {:dependencies [[serverless-cljs-plugin "0.1.2"]
                       [dropbox "2.5.2"]]}
  :cljs-lambda {:compiler
                {:inputs  ["src"]
                 :options {:output-to     "target/habitpaper/habitpaper.js"
                           :output-dir    "target/habitpaper"
                           :target        :nodejs
                           :language-in   :ecmascript5
                           :optimizations :none}}}

  :cljsbuild {:builds [{:id "habitpaper-test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to     "target/habitpaper-test/habitpaper.js"
                                   :output-dir    "target/habitpaper-test"
                                   :target        :nodejs
                                   :language-in   :ecmascript5
                                   :optimizations :none
                                   :main          habitpaper.test-runner}}]}
  :doo {:build "habitpaper-test"
        :alias {:default [:node]}})

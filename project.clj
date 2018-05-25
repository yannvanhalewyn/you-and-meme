(defproject you-and-meme "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [clj-http "3.9.0"]
                 [cheshire "5.8.0"]
                 [compojure "1.6.1"]
                 [com.stuartsierra/component "0.3.2"]
                 [compojure "1.6.1"]
                 [http-kit "2.2.0"]
                 [com.taoensso/sente "1.12.0"]
                 [ring/ring-defaults "0.3.1"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.4"]
                                  [com.cemerick/piggieback "0.2.2"]
                                  [figwheel-sidecar "0.5.16"]
                                  [reloaded.repl "0.2.4"]]
                   :source-paths ["src/clj" "env/dev"]}
             :uberjar {:main you-and-meme.core
                       :prep-tasks ["compile" ["cljsbuild" "once" "prod"]]
                       :aot :all}}

  :uberjar-name "you-and-meme.jar"

  :jvm-opts ["--add-modules" "java.xml.bind"]

  :cljsbuild {:builds
              [{:id           "dev"
                :source-paths ["src/cljs"]
                :figwheel     {:on-jsload "you_and_meme.core/mount-root"}
                :compiler     {:main                 you_and_meme.core
                               :output-to            "resources/public/js/compiled/app.js"
                               :output-dir           "resources/public/js/compiled/out"
                               :asset-path           "js/compiled/out"
                               :source-map-timestamp true
                               :preloads             [devtools.preload]
                               :external-config      {:devtools/config {:features-to-install :all}}}}

               {:id           "prod"
                :source-paths ["src/cljs"]
                :compiler     {:main            you_and_meme.core
                               :output-to       "resources/public/js/compiled/app.js"
                               :optimizations   :advanced
                               :closure-defines {goog.DEBUG false}
                               :pretty-print    false}}]})

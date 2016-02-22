(def project 'avenir-cljs)
(def version "0.2.0")
(def description "ClojureScript example for avenir")
(def project-url "https://github.com/tmarble/avenir")

(set-env! :resource-paths #{"src"}
  :dependencies   '[[org.clojure/clojure "1.8.0"]
                    [org.clojure/clojurescript "1.7.228"]
                    [avenir "0.2.0"]

                    ;; cljs-dev
                    [com.cemerick/piggieback "0.2.1"     :scope "test"]
                    [weasel                 "0.7.0"      :scope "test"]
                    [org.clojure/tools.nrepl "0.2.12"    :scope "test"]
                    [adzerk/boot-reload     "0.4.5"      :scope "test"]
                    [pandeiro/boot-http "0.7.2" :scope "test"]
                    [adzerk/boot-cljs       "1.7.228-1"  :scope "test"]
                    [adzerk/boot-cljs-repl  "0.3.0"      :scope "test"]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-reload    :refer [reload]])

(task-options!
  cljs {:source-map true})

(deftask cljs-dev
  "ClojureScript Browser REPL for CIDER"
  []
  (comp
    (sift :add-resource #{"html"})
    (cider)
    (serve :dir "target")
    (watch)
    (reload)
    (cljs-repl) ;; before cljs
    (cljs)
    (target :dir #{"target"})))

(deftask run
  "Run the project. Open http://localhost:3000"
  []
  (comp
    (sift :add-resource #{"html"})
    (serve :dir "target")
    (cljs)
    (target :dir #{"target"})
    (wait)))

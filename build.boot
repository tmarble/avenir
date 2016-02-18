(def project 'avenir)
(def version "0.1.2")
(def description "Clojure utilities which may find a proper home in the future")
(def project-url "https://github.com/tmarble/avenir")

(set-env! :resource-paths #{"src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.8.0" :scope "provided"]
                            [org.clojure/clojurescript "1.7.228" :scope "provided"]
                            ;; cljs-dev
                            [com.cemerick/piggieback "0.2.1"     :scope "test"]
                            [weasel                 "0.7.0"      :scope "test"]
                            [org.clojure/tools.nrepl "0.2.12"    :scope "test"]
                            [adzerk/boot-reload     "0.4.5"      :scope "test"]
                            [pandeiro/boot-http "0.7.2" :scope "test"]
                            [adzerk/boot-cljs       "1.7.228-1"  :scope "test"]
                            [adzerk/boot-cljs-repl  "0.3.0"      :scope "test"]

                            ;; testing/development
                            [adzerk/boot-test "1.1.0" :scope "test"]
                            [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT" :scope "test"]
                            [adzerk/bootlaces "0.1.13" :scope "test"]

                            ;; api docs
                            [funcool/boot-codeina "0.1.0-SNAPSHOT" :scope "test"]
                            ])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-reload    :refer [reload]]
  '[adzerk.boot-test :refer [test]]
  '[crisptrutski.boot-cljs-test :refer [test-cljs prep-cljs-tests run-cljs-tests]]
  '[adzerk.bootlaces :refer :all]
  '[funcool.boot-codeina :refer [apidoc]])

(bootlaces! version)

(task-options!
  pom {:project     project
       :version     version
       :description description
       :url         project-url
       :scm         {:url project-url}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}}
  cljs {:source-map true}
  test-cljs {:js-env :phantom}
  apidoc {:version version
          :title (name project)
          :sources #{"src"}
          :format :markdown
          :description  description})

(deftask clj-dev
  "Clojure REPL for CIDER"
  []
  (comp
    (cider)
    (repl :server true)
    (wait)))

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

(deftask test-both
  "Run both CLJ tests and CLJS tests"
  []
  (comp
    (test)
    (test-cljs)))

(deftask build
  "Build jar and install to local repo."
  []
  (comp
    (sift :include #{#".*~$"} :invert true) ;; don't include emacs backups
    (pom)
    (jar)
    (install)))

(deftask build-target
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp
      (sift :include #{#".*~$"} :invert true) ;; don't include emacs backups
      ;; (aot)
      (pom)
      ;; (uber)
      (jar)
      (target :dir dir))))

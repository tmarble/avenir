(def project 'avenir)
(def version "0.2.1")
(def description "Clojure utilities which may find a proper home in the future")
(def project-url "https://github.com/tmarble/avenir")

(set-env! :resource-paths #{"src"}
  :source-paths   #{"test"}
  :dependencies   '[[org.clojure/clojure "1.8.0" :scope "provided"]
                    [org.clojure/clojurescript "1.8.51" :scope "provided"]
                    ;; cljs-dev
                    [com.cemerick/piggieback "0.2.1"     :scope "test"]
                    [weasel                 "0.7.0"      :scope "test"]
                    [org.clojure/tools.nrepl "0.2.12"    :scope "test"]
                    [adzerk/boot-reload     "0.4.7"      :scope "test"]
                    [pandeiro/boot-http "0.7.3" :scope "test"]
                    [adzerk/boot-cljs       "1.7.228-1"  :scope "test"]
                    [adzerk/boot-cljs-repl  "0.3.0"      :scope "test"]

                    ;; testing/development
                    [adzerk/boot-test "1.1.1" :scope "test"]
                    [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT" :scope "test"]
                    [adzerk/bootlaces "0.1.13" :scope "test"]

                    ;; api docs
                    [net.info9/boot-codeina "0.2.1-SNAPSHOT" :scope "test"]])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-reload    :refer [reload]]
  '[adzerk.boot-test :refer [test]]
  '[crisptrutski.boot-cljs-test :refer [test-cljs]]
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
  test-cljs {:js-env :phantom
             :suite-ns 'testing.doo
             ;; :namespaces #{"testing.avenir.utils" "testing.avenir.math"}
             :optimizations :none
             :exit? true}
  apidoc {:title (name project)
          :sources #{"src"}
          :description description
          :version version
          :format :markdown
          :reader :cljc
          :src-uri "https://github.com/tmarble/avenir/blob/master/"
          :src-uri-prefix "#L"})

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
    (sift :include #{#"~$"} :invert true) ;; don't include emacs backups
    (cider)
    (serve :dir "target")
    (watch)
    (reload)
    (cljs-repl) ;; before cljs
    (cljs)
    (target :dir #{"target"})))

(deftask cider-boot
  "Cider boot params task"
  []
  (cljs-dev))

(deftask testing
  "merge source paths in for testing"
  []
  (merge-env! :source-paths #{"test"})
  identity)

(deftask tests
  "Test CLJS and leave artifacts in target for debugging"
  [e js-env VAL kw "Set the :js-env for test-cljs (:phantom)."]
  (comp
    ;; (sift :add-resource #{"html"})
    (testing)
    (test-cljs
      :js-env (or js-env :phantom)
      ;; :out-file "avenir.js"
      ;; :cljs-opts {:asset-path "avenir.out"
      ;;             :output-dir "avenir.out"
      ;;             :verbose true}
      ;; :update-fs? true
      )))

(deftask testc
  "Run both CLJ tests and CLJS tests"
  [e js-env VAL kw "Set the :js-env for test-cljs (:phantom)."]
  (comp
    (test)
    (tests :js-env (or js-env :phantom))))

(deftask build
  "Build jar and install to local repo."
  []
  (comp
    (sift :include #{#"~$"} :invert true) ;; don't include emacs backups
    (pom)
    (jar)
    (install)))

(deftask build-target
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp
      (sift :include #{#"~$"} :invert true) ;; don't include emacs backups
      (pom)
      (jar)
      (target :dir dir))))

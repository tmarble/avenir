(def project 'avenir)
(def version "0.2.2")
(def description "Clojure utilities which may find a proper home in the future")
(def project-url "https://github.com/tmarble/avenir")

(set-env! :resource-paths #{"src"}
  :source-paths   #{"test"}
  :dependencies   '[[org.clojure/clojure "1.8.0" :scope "provided"]
                    [org.clojure/clojurescript    "1.9.473" :scope "provided"]
                    ;; cljs-dev
                    [com.cemerick/piggieback "0.2.1"     :scope "test"]
                    [weasel                  "0.7.0"     :scope "test"]
                    [org.clojure/tools.nrepl "0.2.12"    :scope "test"]
                    [adzerk/boot-reload      "0.5.1"     :scope "test"]
                    [pandeiro/boot-http      "0.7.6"     :scope "test"]
                    [adzerk/boot-cljs        "1.7.228-2" :scope "test"]
                    [adzerk/boot-cljs-repl   "0.3.3"     :scope "test"]

                    ;; testing/development
                    [adzerk/boot-test "1.2.0" :scope "test"]
                    [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]
                    [adzerk/bootlaces "0.1.13" :scope "test"]

                    ;; api docs
                    ;; [net.info9/boot-codeina "0.2.1-SNAPSHOT" :scope "test"]
                    [boot-codox "0.10.3" :scope "test"]
                    ])

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl repl-env]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-reload    :refer [reload]]
  '[adzerk.boot-test :refer [test]]
  '[crisptrutski.boot-cljs-test :refer [test-cljs]]
  ;; '[funcool.boot-codeina :refer [apidoc]]
  '[codox.boot :refer [codox]]
  '[adzerk.bootlaces :refer :all])

(bootlaces! version)

(task-options!
  cljs {:source-map true}
  test-cljs {:js-env :phantom
             :namespaces #{"testing.avenir.utils" "testing.avenir.math"}
             :optimizations :none
             :exit? true}
  ;; apidoc {:title (name project)
  ;;         :sources #{"src"}
  ;;         :description description
  ;;         :version version
  ;;         :format :markdown
  ;;         :reader :cljc
  ;;         :src-uri "https://github.com/tmarble/avenir/blob/master/"
  ;;         :src-uri-prefix "#L"}
  codox {:language :clojure
         :source-paths ["src"]
         ;; :project {:name (name project)
         ;;           :version version
         ;;           :description description}
         :name (name project)
         :version version
         ;; :description description
         :source-uri (str project-url "/blob/master/{filepath}#L{line}")}
  pom {:project     project
       :version     version
       :description description
       :url         project-url
       :scm         {:url project-url}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask testing
  "merge source paths in for testing"
  []
  (merge-env! :source-paths #{"test"})
  identity)

(deftask tests
  "Test CLJS and leave artifacts in target for debugging"
  [e js-env VAL kw "Set the :js-env for test-cljs (:phantom)."]
  (comp
    (testing)
    (test-cljs :js-env (or js-env :phantom))))

(deftask testc
  "Run both CLJ tests and CLJS tests"
  [e js-env VAL kw "Set the :js-env for test-cljs (:phantom)."]
  (comp
    (test)
    (tests :js-env (or js-env :phantom))))

(deftask build-cljs
  "Compile ClojureScript"
  []
  (comp
    (notify
      :visual true
      :title "CLJS"
      :messages {:success "http://localhost:3000 is ready\n"})
    (speak)
    (sift :include #{#"~$"} :invert true) ;; don't include emacs backups
    (cljs)
    (target :dir #{"target"})))

(deftask development
  "Example task to change ClojureScript options for development"
  [p port PORT int "The port for the ClojureScript nREPL"]
  (merge-env! :resource-paths #{"html"})
  (task-options!
    cljs {:optimizations :none :source-map true}
    reload {;; :ws-port 9001
            }
    repl {:port (or port 8082)
          :middleware '[cemerick.piggieback/wrap-cljs-repl]}
    serve {:dir "target"})
  identity)

;; This will start an nREPL server on 8082 that a remote IDE
;; can connect to for the CLJS REPL.
(deftask cljs-dev
  "Starts CLJS nREPL"
  [p port PORT int "The port for the ClojureScript nREPL"]
  (comp
    (development :port port)
    (serve)
    (watch)
    (reload)
    (cljs-repl)
    (build-cljs)))

;; This will start an nREPL server on 8081 that a remote IDE
;; can connect to for the CLJ REPL.
(deftask clj-dev
  "Starts CLJ nREPL"
  [p port PORT int "The port for the Clojure nREPL"]
  (comp
    (repl
      :port (or port 8081)
      :server true
      :init "src/avenir/utils.cljc"
      :init-ns 'avenir.utils)
    (wait)))

;; For Emacs if you Customize your Cider Boot Parameters to 'cider-boot'
;; then this task will be invoked upon M-x cider-jack-in-clojurescript
;; which is on C-c M-J
;; CIDER will then open two REPL buffers for you, one for CLJS
;; and another for CLJ. FFI:
;; https://cider.readthedocs.io/en/latest/up_and_running/#clojurescript-usage
;; https://cider.readthedocs.io/en/latest/installation/
;; https://github.com/boot-clj/boot/wiki/Cider-REPL

;; This task may be commented out here for users that have not copied
;; a profile.boot file to ~/.boot/ which defines the cider task:

(deftask cider-boot
  "Cider boot params task"
  []
  (cider)
  (cljs-dev))

(deftask build-install
  "Build the project locally as a JAR and install it in the local repo."
  []
  (comp
    (sift :include #{#"~$"} :invert true) ;; don't include emacs backups
    (pom)
    (jar)
    (install)))

(def project 'avenir-clj)
(def version "0.2.0")
(def description "Clojure example for avenir")
(def project-url "https://github.com/tmarble/avenir")
(def main (symbol (str (name project) ".core")))

(set-env! :resource-paths #{"src"}
  :source-paths   #{"test"}
  :dependencies   '[[org.clojure/clojure "1.8.0"]
                    [avenir "0.2.0"]])

(task-options!
  pom {:project     project
       :version     version
       :description description
       :url         project-url
       :scm         {:url project-url}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}}
  aot {:namespace   #{main}}
  jar {:main        main
       :file        (str (name project) ".jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require [main :as 'app])
  (apply (resolve 'app/-main) args))

(ns testing.avenir.utils
  (:require [clojure.string :as string]
            #?(:cljs [cljs.pprint :refer [float?]])
            #?(:clj [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :as test :refer-macros [deftest testing is]])
            #?(:clj [avenir.utils :as u]
               :cljs [avenir.utils :as u :refer [format]])))

(deftest test-assoc-if
  (testing "test assoc-if"
    (is (= {:a 1 :b 2}
          (-> {:a 1}
            (u/assoc-if :b 2)
            (u/assoc-if :c nil))))
    (is (= {:a 4 :b 2}
          (u/assoc-if {:a 1} :b 2 :c false :d nil :a 4)))))

(deftest test-str-append
  (testing "test str-append"
    (is (= "Hello World!" (u/str-append "Hello " "World!")))
    (is (= "World!" (u/str-append nil "World!")))))

(defn hello [] (println "hello"))
(defn goodbye [] (println "hello"))

(def myfns {:greetings [hello goodbye]
            :hello hello
            :goodbye goodbye
            :nested {:hello-again hello}})

(deftest test-remove-fn
  (testing "test remove-fn"
    (is (= {:greetings ["<fn>" "<fn>"],
            :hello "<fn>",
            :goodbye "<fn>",
            :nested {:hello-again "<fn>"}}
          (u/remove-fn myfns)))))

(def ^{:foo :anything} var-with-meta 42)
(def ^{:type :anything} var-with-type 666)
(def mymap {:a #'var-with-meta
            :b #'var-with-type
            :c ^{:doc "an annotated vector"} [123]})

(deftest test-ppmeta
  (testing "test ppmeta"
    (let [m-newlines (with-out-str (u/ppmeta mymap))
          m (string/replace m-newlines "\n" "")]
      (is (re-find #":a.*\:foo :anything" m))
      (is (re-find #":a.*var-with-meta" m))
      (is (re-find #":b.*:type :anything" m))
      (is (re-find #":b.*var-with-type" m))
      ;; This seems wrong... shouldn't it be...
      ;; (is (re-find #":c \^\{:doc \"an annotated vector\"} \[123\]" m)
      (is (re-find #":c \[123\]" m)))))

(def three-true [true true true])
(def mixed-truth [true false true])
(def three-false [false false false])

(deftest boolean-fns
  (testing "test boolean functions"
    (is (reduce u/and-fn three-true))
    (is (not (reduce u/and-fn mixed-truth)))
    (is (not (reduce u/and-fn three-false)))
    (is (reduce u/or-fn three-true))
    (is (reduce u/or-fn mixed-truth))
    (is (not (reduce u/or-fn three-false)))
    (is (= [false true false] (mapv u/not-fn mixed-truth)))
    (is (u/implies true true))
    (is (not (u/implies true false)))
    (is (u/implies false true))
    (is (u/implies false false))
    (is (u/xor true))
    (is (not (u/xor false)))
    (is (not (u/xor false false)))
    (is (u/xor false true))
    (is (u/xor true false))
    (is (not (u/xor true true)))
    (is (not (u/xor false false true true false)))
    ))

(deftest test-concatv
  (testing "test concatv"
    (is (= [1 2 3 4 5 6]
          (u/concatv '(1 2 3) [4 5 6])))))

(deftest test-vec-index-of
  (testing "test vec-index-of"
    (is (= 5
          (u/vec-index-of (vec (range 10)) 5)))))

(deftest test-errors
  (testing "test errors"
    (is (= "Caught bad args: a b c 123"
          (try (u/throw-args "Caught bad args:" "a" "b" "c" 123)
               #?(:clj
                  (catch Exception e (.getMessage e))
                  :cljs
                  (catch js/Error e (.-message e))))))))

(deftest test-cljs-only-functions
  (testing "test CLJS only functions"
    (is (float? 3.14))
    (is (not (float? 42)))
    (is (= "123.450" (format "%6.3f" 123.45)))))

(deftest test-as-boolean
  (testing "test as-boolean"
    (is (= [true true true true false false]
          (mapv u/as-boolean [:a 123 "hi" 0 nil false])))))

(deftest test-as-int
  (testing "test as-int"
    (is (= 42
          (u/as-int 42)
          (u/as-int 42.1)
          (u/as-int "42")))))

(deftest test-as-float
  (testing "test as-float"
    (is (= 3.14
          (u/as-float 3.14)
          (+ (u/as-float 3) 0.14)
          (u/as-float "3.14")))))

(deftest test-as-keyword
  (testing "test as-keyword"
    (is (= :foo (u/as-keyword :foo)))
    (is (= :bar (u/as-keyword "bar")))
    (is (= :baz (u/as-keyword ":baz")))
    (is (= :42 (u/as-keyword 42)))
    (is (= :println (u/as-keyword 'println)))))

(def somemap {:foo "foo"
              "bar" "bar"
              ":baz" "baz"
              42 "42"
              'println "println"})

(deftest test-keywordize
  (testing "test keywordize"
    (is (= {:foo "foo"
            :bar "bar"
            :baz "baz"
            :42 "42"
            :println "println"}
          (u/keywordize somemap)))))

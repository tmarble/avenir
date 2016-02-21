(ns testing.avenir.math
  (:require  #?(:clj [clojure.test :refer [deftest testing is]]
                :cljs [cljs.test :as test :refer-macros [deftest testing is]])
             [avenir.utils :as u]
             [avenir.math :as m]))

(deftest test-constants
  (testing "test constants"
    (is (= 9007199254740991 m/js-max-int))
    (is (= -9007199254740991 m/js-min-int))
    (is (u/approx= 2.718 m/E))))

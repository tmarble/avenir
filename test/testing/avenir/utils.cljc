(ns testing.avenir.utils
  (:require  #?(:clj [clojure.test :refer [deftest testing is]]
                :cljs [cljs.test :as test :refer-macros [deftest testing is]])
            [avenir.utils :refer [assoc-if]]))

(deftest test-assoc-if
  (testing "test assoc-if"
    (is (= {:a 1 :b 2}
          (-> {:a 1}
            (assoc-if :b 2)
            (assoc-if :c nil))))))

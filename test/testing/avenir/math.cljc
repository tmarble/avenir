(ns testing.avenir.math
  (:require [clojure.string :as string]
            #?(:clj [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :as test :refer-macros [deftest testing is]])
             [avenir.math :as m]))

(def user-agent
  #?(:clj "clojure"
     :cljs (.-userAgent js/navigator)))

(def clojure? (= user-agent "clojure"))
(def phantomjs? (string/index-of user-agent "PhantomJS"))

;; if selenium then test  else constant
(deftest test-constants
  (testing "test constants"
    (is #?(:clj true
           :cljs (= "force a" "cljs bug")))
    (is (= m/js-max-int
          #?(:clj 9007199254740991
             :cljs (if phantomjs?
                     9007199254740991
                     (.-MAX_SAFE_INTEGER js/Number)))))
    (is (= m/js-min-int
          #?(:clj -9007199254740991
             :cljs (if phantomjs?
                     -9007199254740991
                     (.-MIN_SAFE_INTEGER js/Number)))))
    (is (m/approx= 2.7182818 m/E))
    (is (m/approx= 0.6931471 m/LN2))
    (is (m/approx= 2.3025850 m/LN10))
    (is (m/approx= 1.4426950 m/LOG2E))
    (is (m/approx= 0.4342944 m/LOG10E))
    (is (m/approx= 3.1415926 m/PI))
    (is (m/approx= 1.5707963 m/PI_2))
    (is (m/approx= 0.7071067 m/SQRT1_2))
    (is (m/approx= 1.4142135 m/SQRT2))))

(def PI_3 (/ m/PI 3))
(def PI_4 (/ m/PI 4))
(def PI_6 (/ m/PI 6))

(deftest test-math-functions
  (testing "test math functions"
    (when (or clojure? (not phantomjs?))
      (is (= 5 (m/abs -5)))
      (is (m/approx= PI_3 (m/acos 0.5)))
      (is (m/approx= 1.6002868 (m/cosh PI_3)))
      (is (not (m/approx= 0.123456 0.123457)))
      (is (m/approx= 0.1234567 0.1234568))
      (is (m/approx= 0.123 0.124 1e-2))
      (is (m/approx= PI_6 (m/asin 0.5)))
      (is (m/approx= PI_4 (m/atan 1.0)))
      (is (m/approx= (- (/ (* m/PI 3) 4)) (m/atan2 -1 -1)))
      (is (= 2.0 (m/cbrt 8)))
      (is (= 2 (m/ceil 1.3)))
      (is (m/approx= 0.5 (m/cos PI_3)))
      (is (m/approx= 7.3890560 (m/exp 2)))
      (is (m/approx= PI_3 (m/degrees->radians 60.0)))
      (is (m/approx= 6.3890560 (m/expm1 2)))
      (is (= 1 (m/floor 1.3)))
      (is (= 5.0 (m/hypot 3 4)))
      (is (m/approx= 4.6051701 (m/log 100)))
      (is (= 3.0 (m/log10 1000)))
      (is (m/approx= 4.6151205 (m/log1p 100)))
      (is (= 81.0 (m/pow 3 4)))
      (is (m/approx= 30.0 (m/radians->degrees PI_6)))
      (is (= 2 (m/round 1.5)))
      (is (= -1 (m/sign -123.45)))
      (is (m/approx= 0.5 (m/sin PI_6)))
      (is (m/approx= 0.5478534 (m/sinh PI_6)))
      (is (= 3.0 (m/sqrt 9)))
      (is (m/approx= 1.0 (m/tan PI_4)))
      (is (m/approx= 0.6557942 (m/tanh PI_4))))))

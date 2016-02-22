(ns avenir.math
  "Math functions from *l'avenir* (the future)!"
  #?(:clj (:import [java.lang
                    Math])))

;; constants

;;; https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER
;; NOTE: (.-MAX_SAFE_INTEGER js/Number) may not be available in phantomjs
(def #^{:added "0.2.0"}
  js-max-int
  "Maximum safe integer in JavaScript (2^53 - 1)"
  9007199254740991)

(def #^{:added "0.2.0"}
  js-min-int
  "Miniumum safe integer in JavaScript - (2^53 - 1)"
  -9007199254740991)

(def #^{:added "0.2.0"}
  E
  "Euler's constant and the base of natural logarithms, approximately 2.718"
  #?(:clj Math/E
     :cljs (.-E js/Math)))

(def #^{:added "0.2.0"}
  LN2
  "Natural logarithm of 2, approximately 0.693"
  #?(:clj (Math/log 2.0)
     :cljs (.-LN2 js/Math)))

(def #^{:added "0.2.0"}
  LN10
  "Natural logarithm of 10, approximately 2.30"
  #?(:clj (Math/log 10.0)
     :cljs (.-LN10 js/Math)))

(def #^{:added "0.2.0"}
  LOG2E
  "Base 2 logarithm of E, approximately 1.443"
  #?(:clj (/ (Math/log Math/E) (Math/log 2.0))
     :cljs (.-LOG2E js/Math)))

(def #^{:added "0.2.0"}
  LOG10E
  "Base 10 logarithm of E, approximately 0.434"
  #?(:clj (Math/log10 Math/E)
     :cljs (.-LOG10E js/Math)))

(def #^{:added "0.2.0"}
  PI
  "The value of π ( *pi* ), approximately 3.14159"
  #?(:clj Math/PI
     :cljs (.-PI js/Math)))

(def #^{:added "0.2.0"}
  PI_2
  "π/2 = one half the value of π ( *pi* ), approximately 1.57079"
  (/ PI 2.0))

(def #^{:added "0.2.0"}
  SQRT1_2
  "Square root of 1/2; equivalently, 1 over the square root of 2, approximately 0.707"
  #?(:clj (Math/sqrt 0.5)
     :cljs (.-SQRT1_2 js/Math)))

(def #^{:added "0.2.0"}
  SQRT2
  "Square root of 2, approximately 1.414"
  #?(:clj (Math/sqrt 2)
     :cljs (.-SQRT2 js/Math)))

;; functions

(defn abs
  "Returns absolute value"
  {:added "0.2.0"}
  [a]
  (if (neg? a) (- a) a))

(defn acos
  "Returns the arc cosine of a value; the returned angle is in the range 0.0 through π ( *pi* )"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/acos a)
     :cljs (.acos js/Math a)))

(defn approx=
  "Approximate equality test within epsilon ( *default epsilon* 1e-6)"
  {:added "0.2.0"}
  ([x y]
   (approx= x y 1e-6))
  ([x y epsilon]
   (< (abs (- x y)) epsilon)))

(defn asin
  "Returns the arc sine of a value; the returned angle is in the range -π/2 through π/2"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/asin a)
     :cljs (.asin js/Math a)))

(defn atan
  "Returns the arc tangent of a value; the returned angle is in the range -π/2 through π/2"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/atan a)
     :cljs (.atan js/Math a)))

(defn atan2
  "Returns the angle θ ( *theta* ) from the conversion of rectangular coordinates (x, y) to polar coordinates (r, θ)."
  {:added "0.2.0"}
  [y x]
  #?(:clj (Math/atan2 y x)
     :cljs (.atan2 js/Math y x)))

(defn cbrt
  "Returns the cube root of a"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/cbrt a)
     :cljs (.cbrt js/Math a)))

(defn ceil
  "Returns the smallest integer greater than or equal to a number"
  {:added "0.2.0"}
  [a]
  (int
    #?(:clj (Math/ceil a)
       :cljs (.ceil js/Math a))))

(defn cos
  "Returns the cosine of a number"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/cos a)
     :cljs (.cos js/Math a)))

(defn cosh
  "Returns the hyperbolic cosine of a number"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/cosh a)
     :cljs (.cosh js/Math a)))

(defn exp
  "Returns E^x, where x is the argument, and E is Euler's constant (2.718…), the base of the natural logarithm."
  {:added "0.2.0"}
  [x]
  #?(:clj (Math/exp x)
     :cljs (.exp js/Math x)))

(defn degrees->radians
  "Convert from degrees to radians"
  {:added "0.2.0"}
  [d]
  (* (/ d 360.0) PI 2.0))

(defn expm1
  "Returns E^x -1"
  {:added "0.2.0"}
  [x]
  #?(:clj (Math/expm1 x)
     :cljs (.expm1 js/Math x)))

(defn floor
  "Returns the largest integer less than or equal to a number"
  {:added "0.2.0"}
  [a]
  (int
    #?(:clj (Math/floor a)
       :cljs (.floor js/Math a))))

(defn hypot
  "Returns (sqrt (* x x) (* y y))"
  {:added "0.2.0"}
  [x y]
  #?(:clj (Math/hypot x y)
     :cljs (.hypot js/Math x y)))

(defn log
  "Returns the natural logarithm (base E) of a"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/log a)
     :cljs (.log js/Math a)))

(defn log10
  "Returns logarithm (base 10) of a"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/log10 a)
     :cljs (.log10 js/Math a)))

(defn log1p
  "Returns the natural logarithm of the sum of the argument and 1."
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/log1p a)
     :cljs (.log1p js/Math a)))

(defn log2
  "Returns logarithm (base 2) of a"
  {:added "0.2.0"}
  [a]
  #?(:clj (/ (Math/log a) (Math/log 2.0))
     :cljs (.log2 js/Math a)))

(defn pow
  "Returns x^y"
  {:added "0.2.0"}
  [x y]
  #?(:clj (Math/pow x y)
     :cljs (.pow js/Math x y)))

(defn radians->degrees
  "Convert from radians to degrees"
  {:added "0.2.0"}
  [r]
  (* (/ r PI 2.0) 360.0))

(defn round
  "Returns the value of a number rounded to the nearest integer."
  {:added "0.2.0"}
  [a]
  (int
    #?(:clj (Math/round a)
       :cljs (.round js/Math a))))

(defn sign
  "Returns -1 if a < 0, 0 if a = 0, 1 if a > 0"
  {:added "0.2.0"}
  [a]
  (int
    #?(:clj (Math/signum a)
       :cljs (.sign js/Math a))))

(defn sin
  "Returns the sine of a number"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/sin a)
     :cljs (.sin js/Math a)))

(defn sinh
  "Returns the hyperbolic sine of a number"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/sinh a)
     :cljs (.sinh js/Math a)))

(defn sqrt
  "Returns the positive square root of a number"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/sqrt a)
     :cljs (.sqrt js/Math a)))

(defn tan
  "Returns the tangent of a number"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/tan a)
     :cljs (.tan js/Math a)))

(defn tanh
  "Returns the hyperbolic tangent of a number"
  {:added "0.2.0"}
  [a]
  #?(:clj (Math/tanh a)
     :cljs (.tanh js/Math a)))

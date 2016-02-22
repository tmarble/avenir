(ns avenir-cljs.core
  (:require [cljs.pprint :refer [float?]]
            [goog.dom :as gdom]
            [avenir.utils :refer [and-fn format]]
            [avenir.math :refer [hypot atan radians->degrees]]))

(enable-console-print!)

(defn main
  "ClojureScript example for avenir."
  [& args]
  (let [nums (range -2 3)
        evens (map even? nums)
        positives (map pos? nums)
        b 4
        a 3
        h (hypot a b)
        theta (radians->degrees (atan (/ a b)))
        is-a-float (fn [x]
                     (str "(is" (if (float? x) " " " not ") "a float)"))]
    (println "nums.........." nums)
    (println "evens........." evens)
    (println "positives....." positives)
    (println "even positives" (map and-fn evens positives))
    (println "The hypotenuse (h) of a right triangle with sides (a, b)...")
    (println "a =" a (is-a-float a))
    (println "b =" b (is-a-float b))
    (println "h =" h (is-a-float h))
    (println "The interior angle (theta) of that triangle in degrees is...")
    (println "theta =" (format "%4.2f" theta) (is-a-float theta))))

(defn output!
  "Set the output box to msg"
  [msg]
  (set! (.-innerHTML (gdom/getElement "output")) msg))

(defn initialize
  "Initialize the application on window load"
  []
  (output! (with-out-str (main))))

(set! (.-onload js/window) initialize)

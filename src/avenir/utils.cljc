(ns avenir.utils
  "Utilties from *l'avenir* (the future)!"
  (:require [clojure.string :as string]
            #?(:clj [clojure.pprint :refer [pprint]]
               :cljs [cljs.pprint :refer [pprint float?]])
            #?@(:cljs [[goog.string :as gstring]
                       [goog.string.format]])))

(defn assoc-if
  "assoc k v in m *iff* v

assoc[iate] if v is truthy. Returns a new map of the
same (hashed/sorted) type, that contains the mapping of key(s) to
val(s) for those val(s) that are truthy."
  {:added "0.1.0"}
  ([m k v]
   (if v
     (assoc m k v)
     m))
  ([m k v & kvs]
   (apply assoc-if (assoc-if m k v) kvs)))

(defn str-append
  "Append b to a (if a is not nil), else return b"
  {:added "0.2.0"}
  [a b]
  (if a
    (str a b)
    b))

(defn remove-fn
  "Replaces any **(fn? v)** in the map m with `\"<fn>\"`, recursively"
  {:added "0.2.0"}
  ([v]
   (cond
     (fn? v) "<fn>"
     (vector? v) (mapv remove-fn v)
     (map? v) (reduce-kv remove-fn {} v)
     :else v))
  ([m k v]
   (assoc m k
     (cond
       (fn? v) "<fn>"
       (vector? v) (mapv remove-fn v)
       (map? v) (reduce-kv remove-fn {} v)
       :else v))))

(defn ppmeta
  "Pretty print with metadata"
  {:added "0.2.0"}
  [x]
  (binding [*print-meta* true]
    (pprint x)))

(defn and-fn
  "and as a function (to be used with reduce)"
  {:tag boolean :added "0.2.0"}
  [& args]
  (when-let [[a & more-as] (not-empty args)]
    (if (and a more-as)
      (recur more-as)
      a)))

(defn or-fn
  "or as a function (to be used with reduce)"
  {:tag boolean :added "0.2.0"}
  [& args]
  (when (not-empty args)
    (if-let [a (first args)]
      a
      (recur (rest args)))))

(defn not-fn
  "not as a function"
  {:tag boolean :added "0.2.0"}
  [arg]
  (if arg false true))

(defn implies
  "logical implies"
  {:tag boolean :added "0.2.0"}
  ([] true)
  ([a] true)
  ([a b] (or (not a) b))
  ([a b & more] (if-not b
                  (if a
                    false
                    (case (count more)
                      1 true
                      2 (implies (first more) (second more))
                      (apply implies (implies (first more) (second more)) (rest (rest more)))))
                  (apply and-fn more))))

(defn xor
  "Exclusive or"
  {:added "0.2.1"}
  ([a]
   a)
  ([a b]
   (or (and a (not b)) (and (not a) b)))
  ([a b & cs]
   (xor (xor a b)
     (apply xor cs))))

(defn concatv
  "Return the concat of args as a vector"
  {:added "0.2.0"}
  [& args]
  (vec (apply concat args)))

(defn vec-index-of
  "Find the index of o in v"
  {:added "0.2.0"}
  [v o]
  (first
    (remove nil?
      (for [i (range (count v))]
        (if (= (get v i) o) i)))))

;; errors

(defn throw-msg
  "Throw an error with a variable number of arguments.
The exception type:
* For CLJ throws an Exception
* For CLJS throws a js/Error"
  {:added "0.2.0"}
  [msg]
  #?(:clj (throw (Exception. msg))
     :cljs (throw (js/Error. msg))))

(defn throw-err
  "Throw an error with all the args (not interspersed with spaces)"
  {:added "0.2.0"}
  [& args]
  (throw-msg (apply str args)))

(defn throw-args
  "Throw an error with all the args (interspersed with spaces)"
  {:added "0.2.0"}
  [& args]
  (throw-msg (apply str (interpose " " args))))

;; coercions

#?(:cljs
(defn format
  "Formats a string using **printf** style syntax

  **NOTE**: *only defined for ClojureScript* see [CLJS-324](http://dev.clojure.org/jira/browse/CLJS-324)"
  {:added "0.2.0"}
  [& args]
  (apply gstring/format args)
  )
)

(defn ^boolean as-boolean
  "Coerce the argument to a boolean"
  {:added "0.2.0"}
  [x]
  (if x true false))

(defn as-int
  "Coerce the argument to an int"
  {:added "0.2.0"}
  [x]
  (cond
    (nil? x) 0
    (integer? x) x
    (float? x) (int x)
    (string? x)
    #?(:clj (Long. x)
       :cljs (js/parseInt x 10))
    :else
    (throw-args "as-int cannot coerce" (type x) "to int:" x)))

(defn as-float
  "Coerce the argument to a float"
  {:added "0.2.0"}
  [x]
  (cond
    (nil? x) 0.0
    (float? x) x
    (integer? x) (float x)
    (string? x)
    #?(:clj (Double. x)
       :cljs (js/parseFloat x))
    :else
    (throw-args "as-float cannot coerce" (type x) "to float:" x)))

(defn as-keyword
  "coerce k to a keyword"
  {:added "0.2.0"}
  [k]
  (cond
    (keyword? k) k
    (string? k) (keyword (if (string/starts-with? k ":") (subs k 1) k))
    (number? k) (keyword (str k))
    (symbol? k) (keyword (name k))
    :else
    (throw-args "as-keyword called with bad type for k:" (type k))))

(defn keywordize
  "coerce the keys of the map to be keywords"
  {:added "0.2.0"}
  ([v]
   (cond
     (map? v) (reduce-kv keywordize {} v)
     :else v))
  ([m k v]
   (assoc m (as-keyword k)
     (cond
       (map? v) (reduce-kv keywordize {} v)
       :else v))))

(ns avenir.utils
  "Utilties from *l'avenir* (the future)!")

(defn assoc-if
  "assoc k v in m *iff* v

 * FIXME: should be extended to **multiple** *kvs*"
  {:added "0.1.0"}
  [m k v]
  (if v
    (assoc m k v)
    m))

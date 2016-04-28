(ns testing.doo
  (:require [doo.runner  :refer-macros [doo-tests]]
            [testing.avenir.math]
            [testing.avenir.utils]))

(doo-tests
  'testing.avenir.math
  'testing.avenir.utils)

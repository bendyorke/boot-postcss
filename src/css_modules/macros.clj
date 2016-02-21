(ns css-modules.macros
  (:refer-clojure :exclude [slurp])
  (:require [clojure.java.io :as io]))

(defmacro slurp [file]
  (clojure.core/slurp (io/resource file)))

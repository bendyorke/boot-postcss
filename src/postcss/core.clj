(ns postcss.core
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json])
  (:import [javax.script ScriptEngineManager]))

(defonce nashorn (.getEngineByName (ScriptEngineManager.) "nashorn"))

(defn eval-files
  "(eval-files engine & filenames)
  Repeatedly open each file and evaluate it in the given engine"
  [engine & filenames]
  (doseq [filename filenames]
    (.eval nashorn
      (-> filename io/resource io/reader)))
  engine)

(defn create []
  (eval-files nashorn "nashorn/context.js" "nashorn/bundle.js" "postcss/bundle.js"))

(defn process [css]
  (.invokeFunction nashorn "process" (object-array [css])))

(defn parse [css]
  (json/read-str
    (.invokeFunction nashorn "parse" (object-array [css]))
    :key-fn #(keyword %)))

(defn stringify [css]
  (.invokeFunction nashorn "stringify"
                           (object-array [(json/write-str css)])))

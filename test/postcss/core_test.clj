(ns postcss.core-test
  (:require [postcss.core :refer [create process]])
  (:require [clojure.string :refer [includes?]])
  (:use clojure.test))

(create)

(deftest process-css
  (testing "Simple CSS (no transforms)"
    (is (= "a { top: 0; }"
           (process "a { top: 0; }"))))
  (testing "autoprefixer"
    (is (includes? (process "a { display: flex; }")
                   "webkit")))
  (testing "postcss-nested"
    (is (includes? (process "a { top: 0; & b { top: 0; } }")
                   "\na b {")))
  (testing "postcss-modules-values"
    (is (includes? (process "@value b: c; a { top: b; }")
                   "top: c")))
  (testing "postcss-modules-local-by-default"
    (is (includes? (process ".a {}")
                   ":export {\n  a:")))
  (testing "postcss-modules-extract-imports"
    (println "postcss-modules-extract-imports is pending"))
  (testing "postcss-modules-scope"
    (println "postcss-modules-scope is pending")))


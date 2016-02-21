(ns css-modules.loader-test
  (:require [postcss.core :refer [create]]
            [css-modules.loader :refer [translate]])
  (:use clojure.test))

(deftest first-test
  (testing "translating string"
    (is (= (translate "foo" {"foo" "bar"})
           "bar")))
  (testing "translating declaration"
    (is (= (translate {:type "decl" :nodes ["foo"]} {"foo" "bar"})
           {:type "decl" :nodes ["bar"]}))))

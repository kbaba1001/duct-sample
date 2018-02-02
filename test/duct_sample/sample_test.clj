(ns duct-sample.sample-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]))

(deftest square-test-example01
  (testing "自乗"
    (is (= 4 (+ 2 2)))))

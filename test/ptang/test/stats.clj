(ns ptang.test.stats
  (:use [ptang.stats])
  (:use [midje.sweet])
  (:require [incanter.core :as incanter]))

;(deftest perf-summary-test
;  (let [ ds (incanter/dataset [:t] [{:t 5}{:t 5}] )
;	result (perf-summary ds)]
;    (println ds)
;    (println result)
;    (is (not (= nil result)))
;    (is (= true (map? result)))
;    (is (= 2 (:count result)))
;    (is (= 5.0 (:mean result)))
;    (is (= 0.0 (:sd result)))
;    (is (= 5.0 (:min result)))
;    (is (= 5.0 (:q95 result)))
;    (is (= 5.0 (:max result)))
;    ))

(def sample-dataset 
  (incanter/dataset [:t] [{:t 5}{:t 5}] ))

;;; assumes that incanter computation is correct. Only test the data structure
(fact "perf summary when 2 lines and both values are 5.0"
      (perf-summary sample-dataset)  =>
      {:count 2 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0})

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

;;; assumes that incanter functions are correct. Only test the data structure
(fact "response time summary when 2 lines and both values are 5.0"
      (let [ds (incanter/dataset [:t] [{:t 5}{:t 5}] )]
	(response-time-summary ds)  =>
	{:count 2 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0}))

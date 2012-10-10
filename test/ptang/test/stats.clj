(ns ptang.test.stats
  (:use [ptang.stats])
  (:use [midje.sweet])
  (:require [incanter.core :as incanter]))

(fact "run summary when 2 lines and both are success"
      (let [ds (incanter/dataset [:t :s :rc]
				 [ {:t 5 :s true :rc 200}
				   {:t 5 :s true :rc 200}]) ]
	(run-summary ds)  =>
	{:count 2 :errorCount 0 :assertErrorCount 0 :httpErrorCount 0}))

(fact "run summary when 2 lines and 1 is an assert failure"
      (let [ds (incanter/dataset [:t :s :rc]
				 [ {:t 5 :s true :rc 200}
				   {:t 5 :s false :rc 200}]) ]
	(run-summary ds)  =>
	{:count 2 :errorCount 1 :assertErrorCount 1 :httpErrorCount 0}))

(fact "run summary when 2 lines and 1 is an http failure"
      (let [ds (incanter/dataset [:t :s :rc]
				 [ {:t 5 :s true :rc 200}
				   {:t 5 :s true :rc 500}]) ]
	(run-summary ds)  =>
	{:count 2 :errorCount 1 :assertErrorCount 0 :httpErrorCount 1}))

;;; assumes that incanter functions are correct. Only test the data structure
(fact "response time summary when 2 lines and both values are 5.0"
      (let [ds (incanter/dataset [:t] [{:t 5}{:t 5}] )]
	(response-time-summary ds)  =>
	{:count 2 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0}))


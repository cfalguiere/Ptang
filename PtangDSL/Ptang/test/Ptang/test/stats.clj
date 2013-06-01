(ns ptang.test.stats
  (:use [ptang.filters])
  (:use [ptang.stats])
  (:use [midje.sweet])
  (:require [incanter.core :as incanter] [clj-time.coerce :as coerce]))

;; fixtures
(def min-ts 1330419301862)
(def max-ts 1330421179091)

(fact "run summary when 2 lines and both are success"
      (let [ds (incanter/dataset [:s :rc]
				 [ { :s "true" :rc 200}
				   {:s "true" :rc 200}]) ]
	(run-summary ds)  =>
	{:count 2 :errorCount 0 :assertErrorCount 0 :httpErrorCount 0}))

(fact "run summary when 2 lines and 1 is an assert failure"
      (let [ds (incanter/dataset [:s :rc]
				 [ {:s "true" :rc 200}
				   {:s "true" :rc 200} 
				   {:s "false" :rc 200}]) ]
	(run-summary ds)  =>
	{:count 3 :errorCount 1 :assertErrorCount 1 :httpErrorCount 0}))

(fact "run summary when 2 lines and 1 is an http failure"
      (let [ds (incanter/dataset [:s :rc]
				 [ {:s "true" :rc 200}
				   {:s "true" :rc 500}]) ]
	(run-summary ds)  =>
	{:count 2 :errorCount 1 :assertErrorCount 0 :httpErrorCount 1}))

(fact "run summary when 2 lines and 1 is an http failure and an assert failure"
      (let [ds (incanter/dataset [:s :rc]
				 [ {:s "true" :rc 200}
				   {:s "false" :rc 500}]) ]
	(run-summary ds)  =>
	{:count 2 :errorCount 1 :assertErrorCount 1 :httpErrorCount 1}))

;;; assumes that incanter functions are correct. Only test the data structure
(fact "response time summary when 2 lines and both values are 5.0"
      (let [ds (incanter/dataset [:t] [{:t 5}{:t 5}] )]
	(response-time-summary ds)  =>
	{:count 2 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0}))

(fact "response time summary with filter when 3 lines and 1 error"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 5 :s "true" :rc 200} 
                                              {:t 5 :s "true" :rc 200}
                                              {:t 0 :s "false" :rc 200}] )]
	(response-time-summary ds asserted-filter)  =>
	{:count 2 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0}))

(fact "http codes summary when 2 code 200 and 1 code 500"
      (let [ds (incanter/dataset [:rc]
				 [ {:rc 200}
				   {:rc 200}
				   {:rc 500}])
	    result (http-codes-summary ds)]
	(incanter/$ :count (incanter/$where {:code 200} result))  => 2
	(incanter/$ :count (incanter/$where {:code 500} result))  => 1
	))


(fact "duration summary"
     (let [ds (incanter/dataset [:ts] [ { :ts min-ts} {:ts max-ts } ]) 
           summary (duration-summary ds)]
     (:start-ts summary)  =>	min-ts 
     (:end-ts summary)  =>	max-ts 
     (:duration-ms summary)  => 1877229
     (coerce/to-string (:start-date summary))  => "2012-02-28T08:55:01.862Z" 
     (coerce/to-string (:end-date summary)) => "2012-02-28T09:26:19.091Z"
     (:duration-mn summary)  => 31
     ))
        ;;TODO filter


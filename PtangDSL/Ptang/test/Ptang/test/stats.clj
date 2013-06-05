(ns ptang.test.stats
  (:use [ptang.filters])
  (:use [ptang.stats])
  (:use [midje.sweet])
  (:use [incanter.core :only [$ $where nrow]])
  (:use [clj-time.core :only [date-time]])
  (:require [incanter.core :as incanter] [clj-time.coerce :as coerce]))

;; fixtures
(def min-ts 1330419301862)
(def max-ts 1330421179091)

(fact "run summary when 2 lines and both are success"
      (let [ds (incanter/dataset [:s :rc]
				 [ { :s "true" :rc 200}
				   {:s "true" :rc 200}]) ]
	(run-summary ds)  =>
	{:count 2 :successCount 2 :errorCount 0 :httpErrorCount 0 :assertionErrorCount 0 :durationLimitErrorCount 0}))

(fact "run summary when 2 lines and 1 is an assert failure"
      (let [ds (incanter/dataset [:s :rc]
				 [ {:s "true" :rc 200}
				   {:s "true" :rc 200} 
				   {:s "false" :rc 200}]) ]
	(run-summary ds)  =>
	{:count 3 :successCount 2 :errorCount 1 :httpErrorCount 0 :assertionErrorCount 1 :durationLimitErrorCount 0}))

(fact "run summary when 2 lines and 1 is an http failure"
      (let [ds (incanter/dataset [:s :rc]
				 [ {:s "true" :rc 200}
				   {:s "true" :rc 500}]) ]
	(run-summary ds)  =>
	{:count 2 :successCount 1 :errorCount 1 :httpErrorCount 1 :assertionErrorCount 0 :durationLimitErrorCount 0}))

(fact "run summary when 2 lines and 1 is an http failure and an assert failure"
      (let [ds (incanter/dataset [:s :rc]
				 [ {:s "true" :rc 200}
				   {:s "false" :rc 500}]) ]
	(run-summary ds)  =>
	{:count 2 :successCount 1 :errorCount 1 :httpErrorCount 1 :assertionErrorCount 1 :durationLimitErrorCount 0}))

(fact "run summary when 4 lines and each kind of failure"
      (let [ds (incanter/dataset [:t :s :rc]
				 [ {:t 300 :s "true" :rc 200}  ;; ok
				   {:t 300 :s "false" :rc 500}
				   {:t 300 :s "false" :rc 200}
				   {:t 30000 :s "true" :rc 200}
       ]) ]
	(run-summary ds)  =>
	{:count 4 :successCount 1 :errorCount 3 :httpErrorCount 1 :assertionErrorCount 2 :durationLimitErrorCount 1}))

;;; assumes that incanter functions are correct. Only test the data structure
(fact "response time summary when 2 lines and both values are 5.0"
      (let [ds (incanter/dataset [:t] [{:t 5}{:t 5}] )]
	(response-time-summary ds)  =>
	{:count 2 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0}))

(fact "response time summary with filter when 3 lines and 1 error"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 5 :s "true" :rc 200} 
	                                              {:t 5 :s "true" :rc 200}
	                                              {:t 0 :s "false" :rc 200}] )]
        (response-time-summary ds (asserted-condition))  =>
        {:count 2 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0}))

(fact "response time summary with filter and interval"
    (let [ds (incanter/dataset [:ts :s :rc :t] [ { :ts 1330419301862 :s "true" :rc 200 :t 3000} ;;before interval
                                          { :ts 1330419401862 :s "true" :rc 200 :t 5}
                                          { :ts 1330419501862 :s "true" :rc 500 :t 0} ;; ko
                                          { :ts 1330419601862 :s "true" :rc 200 :t 5}
                                          {:ts 1330421079091 :s "true" :rc 200 :t 5}
                                          {:ts 1330421179091 :s "true" :rc 200 :t 100} ]) ;;after interval
            ]
      (response-time-summary  ds 
                         (asserted-condition) 
                         (from-to-condition {:from (date-time 2012 2 28 8 56 ) 
                                             :to (date-time 2012 2 28 9 26)}) )  =>
      {:count 3 :mean 5.0 :sd 0.0 :min 5.0 :q95 5.0 :max 5.0}))

(fact "http codes summary when 2 code 200 and 1 code 500"
      (let [ds (incanter/dataset [:rc]
				 [ {:rc 200}
				   {:rc 200}
				   {:rc 500}])
	    result (http-codes-summary ds)]
	($ :count ($where {:code 200} result))  => 2
	($ :count ($where {:code 500} result))  => 1
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
   
;; last sample is not relevant
(fact "duration summary with filter"
     (let [ds (incanter/dataset [:ts :s :rc] [ { :ts min-ts :s "true" :rc 200}
                                          { :ts (+ min-ts 100000) :s "true" :rc 200}
                                          {:ts max-ts :s "false" :rc 200} ]) 
           summary (duration-summary ds (success-condition))]
     (:start-ts summary)  =>	min-ts 
     (:end-ts summary)  =>	1330419401862
     (:duration-ms summary)  => 100000
     (coerce/to-string (:start-date summary))  => "2012-02-28T08:55:01.862Z" 
     (coerce/to-string (:end-date summary)) => "2012-02-28T08:56:41.862Z" 
     (:duration-mn summary)  => 1
     ))
      
(fact "select first reading of ds"
     (let [ds (incanter/dataset [:ts :t :s :rc] [ { :ts min-ts :s "true" :rc 200 :t 1}
                                          { :ts (+ min-ts 100000) :s "true" :rc 200 :t 2}
                                          {:ts max-ts :s "false" :rc 200 :t 3} ]) 
           ]
     (:ts (select-first ds))  =>	min-ts 
     ))

(fact "select last reading of ds"
     (let [ds (incanter/dataset [:ts :t :s :rc] [ { :ts min-ts :s "true" :rc 200 :t 1}
                                          { :ts (+ min-ts 100000) :s "true" :rc 200 :t 2}
                                          {:ts max-ts :s "false" :rc 200 :t 3} ]) 
           ]
     (:ts (select-last ds))  =>	max-ts 
     ))
      

(fact "select first n readings"
     (let [ds (incanter/dataset [:ts :t :s :rc] [ { :ts min-ts :s "true" :rc 200 :t 1}
                                          { :ts (+ min-ts 100000) :s "true" :rc 200 :t 2}
                                          {:ts max-ts :s "false" :rc 200 :t 3} ]) 
           output (select-first 2 ds)  
           ]
     (nrow output)  =>	2
     ($ 0 :ts output)  => min-ts
     ))
      
;; top n
(fact "top n of a dataset default to sorted by :t and size is 5 and order desc"
     (let [ds (incanter/dataset [:ts :t :s :rc] [ { :ts min-ts :s "true" :rc 200 :t 6}
                                          { :ts (+ min-ts 100000) :s "true" :rc 200 :t 2}
                                          { :ts (+ min-ts 200000) :s "true" :rc 200 :t 7}
                                          { :ts (+ min-ts 300000) :s "true" :rc 200 :t 1}
                                          { :ts (+ min-ts 400000) :s "true" :rc 200 :t 9}
                                          { :ts (+ min-ts 500000) :s "true" :rc 200 :t 3}
                                          { :ts (+ min-ts 600000) :s "true" :rc 200 :t 5}
                                          { :ts (+ min-ts 700000) :s "true" :rc 200 :t 4}
                                          {:ts(+ min-ts 800000) :s "false" :rc 200 :t 8} ]) 
           output (top ds)  
           ]
     (nrow output)  =>	5
     ($ 0 :t output)  => 9
     ($ 1 :t output)  => 8
     ))
      


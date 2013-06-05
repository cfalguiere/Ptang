(ns ptang.stats
  (:use [ptang.filters])
  (:use [ptang.internal.common-stats])
  (:use [incanter.core :only [$ $where $order nrow with-data $data col-names $rollup dataset?]])
  (:use [incanter.stats :only [mean sd quantile]])
  (:require [clj-time.coerce :as coerce] [clj-time.core :as clj-time]))


;; returns a map of run information (excepted duration, check duration-summary)
;; total number of samples, number of errors and error cause (assertion, http code)
;; may use an optional list of filters (check filters.clj)
(defn run-summary 
	 ([ds] 
		  (let [allCount (nrow ds)
		        successCount ($where (success-condition) ds) ] 
      {  :count allCount
			   :successCount  (nrow successCount) 
			   :errorCount (- allCount (nrow successCount)) 
			   :httpErrorCount (- allCount (nrow ($where httpSuccessCondition ds))) 
			   :assertionErrorCount (- allCount (nrow ($where assertSuccessCondition ds)))
			   :durationLimitErrorCount (- allCount (nrow ($where durationLimitSuccessCondition ds))) }
		  )) 
	  ([ds & filters] 
	    (filter-and-execute run-summary ds filters)))

;; returns a map of response time statistics (mean, sd, min, max, quantile 95)
;; may use an optional list of filters (check filters.clj)
(defn response-time-summary 
	  ([ds] 
			  (zipmap [ :count :mean :sd :min :q95 :max]
				  (flatten (with-data ($ :t ds)
				    [ (count $data) (mean $data) (sd $data) (quantile $data :probs[0 0.95 1]) ] )))) 
	  ([ds & filters] 
	    (filter-and-execute response-time-summary ds filters)))

;; returns a dataset consisting of the number of samples by HTTP code
;; may use an optional list of filters (check filters.clj)
(defn http-codes-summary 
	  ([ds]
	    (col-names ($rollup count :t :rc ds) [:code :count]))
	  ([ds & filters] 
	    (filter-and-execute http-codes-summary ds filters)))
    
;; returns a map of duration information (start time, end time, duration in s, duration in huma format)
;; may use an optional list of filters (check filters.clj)
(defn duration-summary 
	  ([ds]
		  ( let [ timestamps ($ :ts ds)
		          start-ts (apply min timestamps) 
		          end-ts (apply max timestamps) 
		          start-date (coerce/from-long start-ts) 
		          end-date (coerce/from-long end-ts) 
		          ] 
			   { :start-ts start-ts
			   :end-ts end-ts
			   :duration-ms (- end-ts start-ts)
			   :start-date start-date
			   :end-date end-date
		     :duration-mn (clj-time/in-minutes (clj-time/interval start-date  end-date))
		    }))
	  ([ds & filters] 
	    (filter-and-execute duration-summary ds filters)))

(defn select-first
  { :doc "first readings of the dataset and return a set or a dataset"}
  ([ds]  
    (first (:rows ds)))
  ([ds & options]
    (let [ {n :n, filter :filter, :or {n 1, filter {}}} options
          filtered-ds (apply-filter-if-any ds filter) ]
      (cond (= n 1) (first (:rows filtered-ds))
        :else ($ (range n) :all filtered-ds)))))
  
(defn select-last
  { :doc "last reading of the dataset"}
  [ds] (last (:rows ds)))

(defn top
  { :doc "take first :n sorted by col in :desc/:asc order.May have a filter"}
  ([ds] (select-first  ($order :t :desc ds) :n 5 ))
  ([ds & options]
    (let [ {n :n, filter :filter, col :col, order :order
            :or {n 5, filter {}, col :t, order :desc}} options
          filtered-ds (apply-filter-if-any ds filter) ]
      (select-first  ($order col order filtered-ds ) :n n ))))

  

;; pretty print summary information
;; datasets yield regular dataset output (a table)
;; map yield a map output with each key-value pair on a separate line
(defn pretty-print-summary [title m] 
  (println (str title " {")) 
  (cond (dataset? m) (println m)
    :else  (doall (for [[k v] m] (println k v)))) 
  (println "}"))
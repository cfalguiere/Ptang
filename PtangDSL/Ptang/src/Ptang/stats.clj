(ns ptang.stats
  (:use [ptang.filters])
  (:use [incanter.core :only [$ $where nrow with-data $data col-names $rollup dataset?]])
  (:use [incanter.stats :only [mean sd quantile]])
  (:require [clj-time.coerce :as coerce] [clj-time.core :as clj-time]))

;; show the total number of samples, number of errors and error cause (assertion, http code)
(defn run-summary [ds]
  (let [ 	allCount (nrow ds)
        successCount (success-filter ds)] 
  { :count allCount
   :successCount  (nrow successCount) 
   :errorCount (- allCount (nrow successCount)) 
   :httpErrorCount (- allCount (nrow ($where httpSuccessCondition ds))) 
   :assertionErrorCount (- allCount (nrow ($where assertSuccessCondition ds)))
   :durationLimitErrorCount (- allCount (nrow ($where durationLimitSuccessCondition ds))) }
  )) 

;; show the response time statistics (mean, sd, min, max, quantile 95)
;; may use an optional filter (check filters.clj)
(defn response-time-summary 
  ( [ds] 
		  (zipmap [ :count :mean :sd :min :q95 :max]
			  (flatten (with-data ($ :t ds)
			    [ (count $data) (mean $data) (sd $data) (quantile $data :probs[0 0.95 1]) ] )))) 
  ( [ds filter-fct] 
    (println (str "response-time-summary:  applying filter " filter-fct))
    (response-time-summary  (filter-fct ds) )))

;; show the number of samples by HTTP code
(defn http-codes-summary [ds]
  (col-names ($rollup count :t :rc ds) [:code :count]))
    
;; returns a map of duration information (start time, end time, duration in s, duration in huma format)
(defn duration-summary [ds]
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
    }
     ))


(defn pretty-print-summary [title m] 
  (println (str title " {")) 
  (cond (dataset? m) (println m)
    :else  (doall (for [[k v] m] (println k v)))) 
  (println "}"))
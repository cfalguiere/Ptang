(ns ptang.stats
  (:use [incanter.core :only [$ $where nrow with-data $data col-names $rollup]])
  (:use [incanter.stats :only [mean sd quantile]]))

(defn run-summary [ds]
  (let [ assertSuccessCondition {:s "true"}
	httpSuccessCondition {:rc {:$in #{200 304}}}
	assertSuccess ($where assertSuccessCondition ds)
	httpSuccess ($where httpSuccessCondition ds)
	success ($where (merge assertSuccessCondition httpSuccessCondition)  ds)
	allCount (nrow ds)] 
  { :count allCount
   :errorCount (- allCount (nrow success))
   :assertErrorCount (- allCount (nrow assertSuccess))
   :httpErrorCount (- allCount (nrow httpSuccess)) }
  )) 

(defn response-time-summary [ds]
  (zipmap [ :count :mean :sd :min :q95 :max]
	  (flatten (with-data ($ :t ds)
	    [ (count $data) (mean $data) (sd $data) (quantile $data :probs[0 0.95 1]) ] )))) 

(defn http-codes-summary [ds]
  (col-names ($rollup count :t :rc ds) [:code :count]))
    

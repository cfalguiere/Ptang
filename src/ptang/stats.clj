(ns ptang.stats
  (:use [incanter.core :only [$ $where $data nrow with-data sel view $rollup]])
  (:use [incanter.io :only [read-dataset]])
  (:use [incanter.stats :only [mean sd quantile]])
  (:use [incanter.charts :only [bar-chart histogram time-series-plot add-lines set-stroke-color set-stroke]]))

(defn run-summary [ds]
  (let [ success ($where {:s true :rc {:$in #{200 304}} } ds)
	nbErrors (- (nrow ds) (nrow success)) ] 
    { :count 2 :errorCount nbErrors}
  )) 

(defn response-time-summary [ds]
  (zipmap [ :count :mean :sd :min :q95 :max]
	  (flatten (with-data ($ :t ds)
	    [ (count $data) (mean $data) (sd $data) (quantile $data :probs[0 0.95 1]) ] )))) 

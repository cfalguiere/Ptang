(ns ptang.core
  (:use [ptang.core])
  (:use [ptang.stats]) 
  (:use [ptang.charts]) 
  (:use [ptang.aggregators]) 
  (:use [incanter.core :only [view]])
  (:use [incanter.io :only [read-dataset]]))

(defn -main [& args]
  (let [filename "test-resources/readings.csv"
	ds (read-dataset filename :header true) ]
    (println "Run Summary")
    (println (run-summary ds))
    (println "Response Time Summary")
    (println (response-time-summary ds))
    (println "HTTP Code Summary")
    (http-codes-summary ds)
    (println "Duration")
    (println (duration-summary ds)) 
    
    (view (perf-time-series-plot ds) )
    (view (perf-time-series-plot ds 3000) )
    (view (perf-histogram ds) )
    (view (count-bar-chart ds :lb))
    (view (mean-time-bar-chart ds :lb))
    (view (horizontal-bar-chart ds :max :lb))
    (view (horizontal-bar-chart ds q95 :lb))
    (view (horizontal-bar-chart ds :min :lb  :light-blue))
    ))
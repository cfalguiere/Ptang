(ns ptang.core
  (:use [ptang.core])
  (:use [ptang.stats]) 
  (:use [ptang.charts]) 
  (:use [ptang.filters]) 
  (:use [ptang.aggregators]) 
  (:use [incanter.core :only [view]])
  (:use [incanter.io :only [read-dataset]])
  (:use [clj-time.core :only [minutes]]))

(defn -main [& args]
  (let [filename "test-resources/readings.csv"
        ds (read-dataset filename :header true) 
        plateau (interval-condition (duration-summary ds) {:from-start (minutes 15) 
                                                :to-end (minutes 5)  }) ]
    (view ds)
    
    (pretty-print-summary "Run Summary" (run-summary ds))
    (pretty-print-summary "Response Time Summary" (response-time-summary ds))
   (pretty-print-summary "HTTP Code Summary" (http-codes-summary ds))
    (pretty-print-summary "Duration" (duration-summary ds)) 

    ;; summary with filters    
    (pretty-print-summary "Response Time Summary" (response-time-summary ds (asserted-condition)))
    (pretty-print-summary "Response Time Summary" (response-time-summary ds (asserted-condition) plateau))
    (pretty-print-summary "Duration before errors" (duration-summary ds (success-condition))) 
   
    (view (perf-time-series-plot ds) )
    (view (perf-time-series-plot ds 3000) )
    (view (perf-histogram ds) )
    (view (count-bar-chart ds :lb))
    (view (mean-time-bar-chart ds :lb))
    (view (horizontal-bar-chart ds :max :lb))
    (view (horizontal-bar-chart ds q95 :lb))
    (view (horizontal-bar-chart ds :min :lb  :light-blue))
    ))
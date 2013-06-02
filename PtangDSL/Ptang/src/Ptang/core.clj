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
        ds (read-dataset filename :header true) ]
;    (view ds)
    
    (pretty-print-summary "Run Summary" (run-summary ds))
    (pretty-print-summary "Response Time Summary" (response-time-summary ds))
    (pretty-print-summary "HTTP Code Summary" (http-codes-summary ds))
    (pretty-print-summary "Duration" (duration-summary ds)) 

    ;; summary with filters    
    (let [asserted (asserted-condition)
          success (success-condition )
          peak (interval-condition (duration-summary ds) 
                                      {:from-start (minutes 15)  :to-end (minutes 5)  }) ]
	    (pretty-print-summary "Response Time Summary" (response-time-summary ds asserted))
	    (pretty-print-summary "Response Time Summary" (response-time-summary ds asserted peak))
    )
   
;    (view (perf-time-series-plot ds) )
    (view (perf-time-series-plot ds 3000) )
    (view (perf-histogram ds) )
    (view (count-bar-chart ds :lb))
    (view (mean-time-bar-chart ds :lb))
    (view (horizontal-bar-chart ds :sumf q95 :by :lb))
    (view (horizontal-bar-chart ds :sumf :min :by :lb :color :light-blue))

    ;; chart with filter
    ;; for test purpose. A better implementation would be to factorize the filterd dataset
    (let [peak (interval-condition (duration-summary ds) 
                                      {:from-start (minutes 20)  :to-end (minutes 20)  }) ]
      (view (horizontal-bar-chart  ds :sumf :max :by :lb :filter peak))
      (view (count-bar-chart ds :lb peak)))
     ))
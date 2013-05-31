Ptang
=====

Analysis tool for performance test outputs (namely JMeter)

Clojure DSL
-----------
A set of clojure functions based on Incanter to analyse JMeter's test output, compute statistics and generate charts. 

* read-dataset : read the test output as a csv file (Incanter function)

* run-summary : show the total number of samples, number of errors and error cause (assertion, HTTP code)
* response-time-summary : show the response time statistics (mean, sd, min, max, quantile 95)
* http-codes-summary : show the number of samples by HTTP code

* perf-time-series-plot : plot the response time over time
* perf-histogram : histogram of response times
* count-bar-chart : draw a bar chart of the number of samples grouped by a factor (e.g. the label)
* mean-time-bar-chart : draw a bar chart of the mean time grouped by a factor (e.g. the label)

Check core.clj for a sample script

	(defn -main [& args]
  		(let [filename "test-resources/readings.csv"
			ds (read-dataset filename :header true) ]
    	(println "Response Time Summary")
    	(println (response-time-summary ds))
    	
    	(view (perf-time-series-plot ds 3000) )
    	(view (mean-time-bar-chart ds :lb))
    	))

License
-------

Copyright (C) 2012 

Distributed under the Eclipse Public License, the same as Clojure.

Ptang DSL
=========

Overview 
--------
Ptang DSL is a set of functions based on Incanter and Clojure to analyze JMeter's test output, compute statistics and generate charts.

Examples of functions provided are listed below 
- load a JMeter test output
- compute summaries such as number of samples and samples rate, number and cause of errors, response time statistics (min, mean, sd, quantile 95, max)
- generate usual charts such as response time over time, bar chart of samples counts for each sample label, ...

The DSL doesn't stick to JMeter. The goal is to provide the same tool and analyzes whatever the source is. However, only JMeter source is available at the moment. 

[More information on the Wiki](https://github.com/cfalguiere/Ptang/wiki/PtangDSL).

Getting Started
----------------
The folder [Ptang](Ptang) is a lein project

TODO link to Lein getting started

To start analyzing your test output :
* clone the [git repository Ptang] (https://github.com/cfalguiere/Ptang)
* from the repository root move to PtangDSL/Ptang

You may run the script provided with the project 

	lein run

Check [core.clj](Ptang/src/Ptang/core.clj) for the sample script

	(defn -main [& args]
  		(let [filename "test-resources/readings.csv"
				ds (read-dataset filename :header true) ]
			(pretty-print-summary "Run Summary" (run-summary ds))  
	    	(view (perf-time-series-plot ds) )
	    	))

Alternatively, you may run a REPL and analyze the dataset interactively

	lein repl
	REPL started; server listening on localhost port 10603
	ptang.core=> (def filename "test-resources/readings.csv")
	#'ptang.core/filename
	ptang.core=> (def ds (read-dataset filename :header true) )
	#'ptang.core/ds
	ptang.core=> (pretty-print-summary "Run Summary" (run-summary ds))
	Run Summary {
	:count 4066
	:successCount 4056
	:errorCount 10
	:httpErrorCount 0
	:assertionErrorCount 0
	:durationLimitErrorCount 10
	}
	ptang.core=> (view (perf-time-series-plot ds) )

<img src="https://github.com/cfalguiere/Ptang/wiki/PtangDSLClojure/images/ResponseTimeOverTime.png" width="250" height="190" />
	
The Lein REPL inherits from the project dependencies. 

Documentation
----------------

[Check full documentation](https://github.com/cfalguiere/Ptang/wiki/PtangDSL) on the wiki.


License
-------

Copyright (C) 2012 

Distributed under the Eclipse Public License, the same as Clojure.

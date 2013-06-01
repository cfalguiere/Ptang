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

Here is a sample chart (click to enlarge)

<img src="https://github.com/cfalguiere/Ptang/wiki/PtangDSLClojure/images/ResponseTimeOverTime.png" width="250" height="190" />

Incanter charts rely on JFreeChart. The chart viewer lets you edit some attributes (title, etc), zoom in and out, and save the chart as an image.

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
    	(println "Response Time Summary")
    	(println (response-time-summary ds))
    	
    	(view (perf-time-series-plot ds 3000) )
    	(view (mean-time-bar-chart ds :lb))
    	))

Alternatively, you may run a REPL and analyze the dataset interactively

	lein repl
	REPL started; server listening on localhost port 10603
	ptang.core=> (def filename "test-resources/readings.csv")
	#'ptang.core/filename
	ptang.core=> (def ds (read-dataset filename :header true) )
	#'ptang.core/ds
	ptang.core=> (println (response-time-summary ds))
	{:max 16696.0, :q95 2196.0, :min 17.0, :sd 998.3607924056075, :mean 982.5533694048205, :count 4066}
	nil
	ptang.core=> (view (mean-time-bar-chart ds :lb))
	
The Lein REPL inherits from the project dependencies. 

Documentation
----------------

[Check full documentation](https://github.com/cfalguiere/Ptang/wiki/PtangDSL) on the wiki.


License
-------

Copyright (C) 2012 

Distributed under the Eclipse Public License, the same as Clojure.

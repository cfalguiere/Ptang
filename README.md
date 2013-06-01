Ptang
=====

Ptang is a set of tools for performance test management and analyzes.

The project doesn't stick to JMeter. However, only JMeter is available at the moment. 

Ptang stands for Performance Test Analyzes N' Graphs. 
It has been tricky to find a name. There are very few names with letters PT. 
The project's name is inspired from [P'tang P'tang](http://wiki.lspace.org/mediawiki/index.php/P'tang_P'tang)  a minor god in [Discworld](http://en.wikipedia.org/wiki/Discworld) written by [Terry Pratchett](http://en.wikipedia.org/wiki/Terry_Pratchett).

 
Ptang DSL
-----------
Ptang DSL is a set of functions based on Incanter and Clojure to analyze JMeter's test output, compute statistics and generate charts.

Examples of functions provided are listed below 
- load a JMeter test output
- compute summaries such as number of samples and samples rate, number and cause of errors, response time statistics (min, mean, sd, quantile 95, max)
- generate usual charts such as response time over time, bar chart of samples counts for each sample label, ...

Here is a sample chart (click to enlarge)

<img src="https://github.com/cfalguiere/Ptang/wiki/PtangDSLClojure/images/ResponseTimeOverTime.png" width="250" height="190" />

Here is a sample script

	(defn -main [& args]
  		(let [filename "test-resources/readings.csv"
			ds (read-dataset filename :header true) ]
    	(println "Response Time Summary")
    	(println (response-time-summary ds))
    	
    	(view (perf-time-series-plot ds 3000) )
    	(view (mean-time-bar-chart ds :lb))
    	))
	
Check the project's Readme and the wiki for more information
* [PtangDSL introduction and code](PtangDSL)
* [Check full documentation](https://github.com/cfalguiere/Ptang/wiki/PtangDSL)

License
-------

Copyright (C) 2012 

Distributed under the Eclipse Public License, the same as Clojure.

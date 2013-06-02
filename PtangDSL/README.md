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
Ptang is a Leiningen project. However, an uberjar is also provided and you may use it without Leiningen.

[Getting Started](DSLgettingStarted)



License
-------

Copyright (C) 2012 

Distributed under the Eclipse Public License, the same as Clojure.

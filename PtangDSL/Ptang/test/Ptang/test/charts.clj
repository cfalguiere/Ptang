(ns ptang.test.charts
  (:use [ptang.charts])
  (:use [ptang.aggregators])
  (:use [ptang.filters])
  (:use [ptang.stats])
  (:use [midje.sweet])
  (:use [incanter.io :only [read-dataset]])
  (:use [clj-time.core :only [date-time minutes]])
  (:require [incanter.core :as incanter]))

;; fixtures
(def filename "test-resources/readings.csv")
(def ds-fixture (read-dataset filename :header true) )
  
;; only checks whether an non nil is returned
(fact "perf-time-series-plot with ds and threshold"
      (perf-time-series-plot  ds-fixture  3000)  => truthy)

;; only checks whether an non nil is returned
(fact "perf-time-series-plot with ds"
      (perf-time-series-plot  ds-fixture)  => truthy)

;; only checks whether an non nil is returned
(fact "perf-histogram"
      (perf-histogram  ds-fixture)  => truthy)

;; only checks whether an non nil is returned
(fact "count-bar-chart"
      (count-bar-chart  ds-fixture :lb)  => truthy)

;; only checks whether an non nil is returned
(fact "mean-time-bar-chart"
      (mean-time-bar-chart  ds-fixture :lb)  => truthy)

;; only checks whether an non nil is returned
(fact "horizontal-bar-chart with max"
      (horizontal-bar-chart ds-fixture :sumf :max :by :lb)  => truthy)

;; only checks whether an non nil is returned
(fact "horizontal-bar-chart with custom function"
      (horizontal-bar-chart   ds-fixture :sumf q95 :by :lb)  => truthy)

;; only checks whether an non nil is returned
(fact "horizontal-bar-chart with custom function and color"
      (horizontal-bar-chart  ds-fixture :sumf q95 :by :lb :color :red)  => truthy)

;; only checks whether an non nil is returned
(fact "horizontal-bar-chart with custom function and color"
      (let [ peak (interval-condition (duration-summary ds-fixture) 
                                          {:from-start (minutes 20) :to-end (minutes 20)  })  ]
      (horizontal-bar-chart  ds-fixture :sumf q95 :by :lb :filter peak)  => truthy))

(ns ptang.test.charts
  (:use [ptang.aggregators])
  (:use [ptang.charts])
  (:use [midje.sweet])
  (:use [incanter.io :only [read-dataset]])
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
(fact "horizontal-bar-chart with max"
      (horizontal-bar-chart  ds-fixture :max :lb)  => truthy)

;; only checks whether an non nil is returned
(fact "horizontal-bar-chart with custom function"
      (horizontal-bar-chart  ds-fixture q95 :lb)  => truthy)

;; only checks whether an non nil is returned
(fact "horizontal-bar-chart with custom function and color"
      (horizontal-bar-chart  ds-fixture q95 :lb :red)  => truthy)

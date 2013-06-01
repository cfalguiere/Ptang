(ns ptang.test.aggregators
  (:use [ptang.aggregators])
  (:use [midje.sweet])
  )

(fact "quantile 95"
      (let [ds [ 5 5] ]
        (q95 ds)  => 5.0 ))

(fact "summary-name with keyword" 
  (summary-name :count) => "count" )
 
(fact "summary-name with function" 
   (summary-name q95) => "q95" )
 
 
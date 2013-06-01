(ns ptang.filters
  (:use [incanter.core :only [$where]])
)

(def assertSuccessCondition {:s "true"} )
(def httpSuccessCondition {:rc {:$in #{200 304}}} )
(def durationLimitSuccessCondition {:t {:$lt 10000}} )
 
(defn received-filter [ds]
  ($where httpSuccessCondition ds))
 
(defn asserted-filter [ds]
  ($where (merge assertSuccessCondition httpSuccessCondition)  ds))
 
(defn success-filter [ds]
  ($where (merge assertSuccessCondition httpSuccessCondition durationLimitSuccessCondition)  ds))
 
(defn identity-filter [ds] ds)
 


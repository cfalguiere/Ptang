(ns ptang.filters
  (:use [incanter.core :only [$where]])
  (:require [clj-time.coerce :as coerce] [clj-time.core :as clj-time]))


(def assertSuccessCondition {:s "true"} )
(def httpSuccessCondition {:rc {:$in #{200 304}}} )
(def durationLimitSuccessCondition {:t {:$lt 10000}} )
 
;; example of dt : 1986 10 14 4 3 27 456
(defn from-condition [dt] 
  { :ts {:$gte (coerce/to-long dt) } } )

(defn to-condition [dt] 
  { :ts {:$lte (coerce/to-long dt) } } )

(defn from-to-condition [dt1 dt2] 
  { :ts {:$gte (coerce/to-long dt1) :$lte (coerce/to-long dt2) } } )
  
;; has been received
(defn received-filter 
  ( [ds]
    ($where httpSuccessCondition ds))
  ( [ds interval-condition & more]
    ($where (merge httpSuccessCondition interval-condition)  ds)))
 
;; has been received and verifies the assertions
(defn asserted-filter 
  ( [ds]
    ($where (merge assertSuccessCondition httpSuccessCondition)  ds))
  ( [ds interval-condition & more]
    ($where (merge assertSuccessCondition httpSuccessCondition interval-condition)  ds)))
    
 
;; has been received, verifies the assertion and duration is acceptable
(defn success-filter 
  ( [ds]
  ($where (merge assertSuccessCondition httpSuccessCondition durationLimitSuccessCondition) ds))
  ( [ds interval-condition & more]
    ($where (merge assertSuccessCondition httpSuccessCondition durationLimitSuccessCondition interval-condition)  ds)))
 
;; no filter
(defn identity-filter [ds] ds)

;;=> (plus (date-time 1986 10 14) (months 1) (weeks 3))
;;#<DateTime 1986-12-05T00:00:00.000Z>
;; minus
;;#<DateTime 1986-10-14T04:03:27.456Z>
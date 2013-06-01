(ns ptang.filters
  (:use [incanter.core :only [$where]])
)

(def assertSuccessCondition {:s "true"} )
(def httpSuccessCondition {:rc {:$in #{200 304}}} )
(def durationLimitSuccessCondition {:t {:$lt 10000}} )
 
;; has been received
(defn received-filter [ds]
  ($where httpSuccessCondition ds))
 
;; has been received and verifies the assertions
(defn asserted-filter [ds]
  ($where (merge assertSuccessCondition httpSuccessCondition)  ds))
 
;; has been received, verifies the assertion and duration is acceptable
(defn success-filter [ds]
  ($where (merge assertSuccessCondition httpSuccessCondition durationLimitSuccessCondition)  ds))
 
;; no filter
(defn identity-filter [ds] ds)

;;=> (plus (date-time 1986 10 14) (months 1) (weeks 3))
;;#<DateTime 1986-12-05T00:00:00.000Z>
;; minus

;;=> (date-time 1986 10 14 4 3 27 456)
;;#<DateTime 1986-10-14T04:03:27.456Z>
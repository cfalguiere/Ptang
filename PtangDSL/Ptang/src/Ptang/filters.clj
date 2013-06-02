(ns ptang.filters
  (:use [incanter.core :only [$where]])
  (:require [clj-time.coerce :as coerce] [clj-time.core :as clj-time]))


(def assertSuccessCondition {:s "true"} )
(def httpSuccessCondition {:rc {:$in #{200 304}}} )
(def durationLimitSuccessCondition {:t {:$lt 10000}} )
 

;; example of dt : (date-time 1986 10 14 4 3 27 456)
(defn from-condition [dt] 
  { :ts {:$gte (coerce/to-long dt) } } )

(defn to-condition [dt] 
  { :ts {:$lte (coerce/to-long dt) } } )

(defn from-to-condition [dt1 dt2] 
  { :ts {:$gte (coerce/to-long dt1) :$lte (coerce/to-long dt2) } } )
  
;; has been received, verifies the assertion and duration is acceptable
(defn success-condition []
  (merge assertSuccessCondition httpSuccessCondition durationLimitSuccessCondition)) 
  
;; has been received and verifies the assertions
(defn asserted-condition []
  (merge assertSuccessCondition httpSuccessCondition)) 
  
;; has been received
(defn received-condition []
  (merge httpSuccessCondition)) 



 

;;=> (plus (date-time 1986 10 14) (months 1) (weeks 3))
;;#<DateTime 1986-12-05T00:00:00.000Z>
;; minus
;;#<DateTime 1986-10-14T04:03:27.456Z>
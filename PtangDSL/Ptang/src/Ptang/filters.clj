(ns ptang.filters
  (:use [clojure.set])
  (:use [incanter.core :only [$where]])
  (:use [clj-time.core :only [date-time plus minus]])
  (:require [clj-time.coerce :as coerce] [clj-time.core :as clj-time]))


(def assertSuccessCondition {:s "true"} )
(def httpSuccessCondition {:rc {:$in #{200 304}}} )
(def durationLimitSuccessCondition {:t {:$lt 10000}} )
 

;; example of dt : (date-time 1986 10 14 4 3 27 456)
(defn from-to-condition [bounds]
  { :ts 
   (into {} (for [[k v] (rename-keys bounds {:from :$gte, :to :$lte})] 
                [k (coerce/to-long v)]))} )

  
(defn interval-condition [summary bounds]
 { :ts 
   (into {} (for [[k v] (rename-keys bounds {:from-start :$gte, :to-end :$lte})] 
                [k (coerce/to-long 
                     (cond (= k :$gte) (plus (:start-date summary) v) 
                           (= k :$lte) (minus (:end-date summary) v) 
                           :else nil))
                       ]))} )
  
;; has been received, verifies the assertion and duration is acceptable
(defn success-condition []
  (merge assertSuccessCondition httpSuccessCondition durationLimitSuccessCondition)) 
  
;; has been received and verifies the assertions
(defn asserted-condition []
  (merge assertSuccessCondition httpSuccessCondition)) 
  
;; has been received
(defn received-condition []
  (merge httpSuccessCondition)) 

;; utility functions

(defn apply-filter-if-any [ds filter]
    (cond (empty? filter) ds :else ($where filter ds) ))

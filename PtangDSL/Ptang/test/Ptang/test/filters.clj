(ns ptang.test.filters
  (:use [ptang.filters])
  (:use [ptang.stats])
  (:use [midje.sweet])
  (:use [incanter.core :only [$ $where nrow]])
  (:use [clj-time.core :only [date-time minutes]])
  (:require [incanter.core :as incanter] 
            [clj-time.core :as clj-time] [clj-time.coerce :as coerce]))

(fact "success filter when 4 lines and 3 error (both assert and http and time limit)"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 100 :s "true" :rc 200}
                                       {:t 200 :s "true" :rc 500}
                                       {:t 300 :s "false" :rc 200}
                                       {:t 20000 :s "true" :rc 200}] )]
        (nrow ($where (success-condition) ds))  => 1 ))

(fact "asserted filter when 3 lines and 2 error (both assert and http)"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 1 :s "true" :rc 200}
                                       {:t 2 :s "true" :rc 500}
                                       {:t 3 :s "false" :rc 200}] )]
        (nrow ($where (asserted-condition) ds))  => 1 ))

(fact "received filter http only when 3 lines and 1 http error"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 1 :s "true" :rc 200}
                                       {:t 2 :s "true" :rc 500}
                                       {:t 3 :s "false" :rc 200}] )]
        (nrow ($where (received-condition) ds))  => 2 ))


(fact "from condition"
     (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330419301862 :s "true" :rc 200}
                                          { :ts 1330419401862 :s "true" :rc 200}
                                          {:ts 1330421179091 :s "true" :rc 200} ])
           ;;interval-condition (from-to-condition {:from (date-time 2012 2 28 9)})  ]
           interval-condition (from-to-condition {:from (date-time 2012 2 28 9)})  ]
        (nrow ($where interval-condition ds))  => 1 ))

(fact "to condition"
     (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330419301862 :s "true" :rc 200}
                                          { :ts 1330419401862 :s "true" :rc 200}
                                          {:ts 1330421079091 :s "true" :rc 200}
                                          {:ts 1330421179091 :s "true" :rc 200} ])
           interval-condition (from-to-condition {:to (date-time 2012 2 28 9 25 0 0)})  ]
        (nrow ($where interval-condition ds))  => 3 ))


(fact "from to condition"
      (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330419301862 :s "true" :rc 200}
                                          { :ts 1330419401862 :s "true" :rc 200}
                                          {:ts 1330421079091 :s "true" :rc 200}
                                          {:ts 1330421179091 :s "true" :rc 200} ])
           interval-condition (from-to-condition {:from (date-time 2012 2 28 9 0 0 0) 
                                                :to (date-time 2012 2 28 9 25 0 0)})  ]
       (nrow ($where interval-condition ds))  => 1 ))

(fact "interval condition"
      (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330318301862 :s "true" :rc 200} ;; beforeinterval
                                          { :ts 1330419401862 :s "true" :rc 200}
                                          {:ts 1330420079091 :s "true" :rc 200}
                                          {:ts 1330521179091 :s "true" :rc 200} ]) ;; after interval
           interval-condition (interval-condition (duration-summary ds) {:from-start (minutes 1) 
                                                :to-end (minutes 1)  })  ]
       (nrow ($where interval-condition ds))  => 2 ))


(fact "success condition"
        (success-condition)  =>  {:s "true" :rc {:$in #{200 304}} :t {:$lt 10000}} )



(ns ptang.test.filters
  (:use [ptang.filters])
  (:use [midje.sweet])
  (:use [clj-time.core :only [date-time]])
  (:require [incanter.core :as incanter] 
            [clj-time.core :as clj-time] [clj-time.coerce :as coerce]))

(fact "success filter when 4 lines and 3 error (both assert and http and time limit)"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 100 :s "true" :rc 200}
                                       {:t 200 :s "true" :rc 500}
                                       {:t 300 :s "false" :rc 200}
                                       {:t 20000 :s "true" :rc 200}] )]
        (incanter/nrow (success-filter ds))  => 1 ))

(fact "asserted filter when 3 lines and 2 error (both assert and http)"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 1 :s "true" :rc 200}
                                       {:t 2 :s "true" :rc 500}
                                       {:t 3 :s "false" :rc 200}] )]
        (incanter/nrow (asserted-filter ds))  => 1 ))

(fact "received filter http only when 3 lines and 1 http error"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 1 :s "true" :rc 200}
                                       {:t 2 :s "true" :rc 500}
                                       {:t 3 :s "false" :rc 200}] )]
        (incanter/nrow (received-filter ds))  => 2 ))

(fact "identity filter  when 3 lines"
      (let [ds (incanter/dataset [:t :s :rc] [{:t 1 :s "true" :rc 200}
                                       {:t 2 :s "true" :rc 500}
                                       {:t 3 :s "false" :rc 200}] )]
        (incanter/nrow (identity-filter ds))  => 3 ))

(fact "from condition"
     (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330419301862 :s "true" :rc 200}
                                          { :ts 1330419401862 :s "true" :rc 200}
                                          {:ts 1330421179091 :s "true" :rc 200} ])
           interval-condition (from-condition (date-time 2012 2 28 9))  ]
        (incanter/nrow (incanter/$where interval-condition ds))  => 1 ))

(fact "to condition"
     (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330419301862 :s "true" :rc 200}
                                          { :ts 1330419401862 :s "true" :rc 200}
                                          {:ts 1330421079091 :s "true" :rc 200}
                                          {:ts 1330421179091 :s "true" :rc 200} ])
           interval-condition (to-condition (date-time 2012 2 28 9 25))  ]
        (incanter/nrow (incanter/$where interval-condition ds))  => 3 ))

(fact "from to condition"
     (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330419301862 :s "true" :rc 200}
                                          { :ts 1330419401862 :s "true" :rc 200}
                                          {:ts 1330421079091 :s "true" :rc 200}
                                          {:ts 1330421179091 :s "true" :rc 200} ])
           interval-condition (from-to-condition (date-time 2012 2 28 9 ) 
                                                 (date-time 2012 2 28 9 25))  ]
        (incanter/nrow (incanter/$where interval-condition ds))  => 1 ))

(fact "to condition plus status"
     (let [ds (incanter/dataset [:ts :s :rc] [ { :ts 1330419301862 :s "true" :rc 200}
                                          { :ts 1330419401862 :s "false" :rc 200}
                                          {:ts 1330421079091 :s "true" :rc 200}
                                          {:ts 1330421179091 :s "true" :rc 200} ]) ;; after interval
           interval-condition (to-condition (date-time 2012 2 28 9 25))  ]
        (incanter/nrow (asserted-filter interval-condition ds))  => 2 ))

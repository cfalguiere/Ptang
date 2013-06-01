(ns ptang.test.filters
  (:use [ptang.filters])
  (:use [midje.sweet])
  (:require [incanter.core :as incanter] [clj-time.coerce :as coerce]))

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

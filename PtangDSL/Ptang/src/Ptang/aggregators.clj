(ns ptang.aggregators
  (:use [incanter.stats :only [mean sd quantile]])
  (:use [clojure.string :only (join split)])
  )

;; compute the quantile 95 of a series
(defn q95 [s] 
  (first (quantile s :probs[0.95])) )

(def function-name-pattern (re-pattern #".+$(.+)"))

(defn summary-name [summary-fct]
  (cond 
    (keyword? summary-fct) (name summary-fct)
    :else (last (split (.getName (type summary-fct)) #"\$"))))
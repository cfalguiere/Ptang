(ns ptang.internal.common-stats
  (:use [incanter.core :only [$where]]))

(defn filter-and-execute [f ds filters]
   (let [filter (reduce merge filters) ]
     ;(println (str f ":  applying filter " filter))
     (f  ($where filter ds) )))

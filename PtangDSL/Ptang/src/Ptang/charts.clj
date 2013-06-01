(ns ptang.charts
  (:use [ptang.aggregators])
  (:use [incanter.core :only [$ $data with-data sel view $rollup]])
  (:use [incanter.stats :only [mean]])
  (:use [incanter.charts :only [bar-chart histogram time-series-plot add-lines set-stroke-color set-stroke]]))

(def colors { :light-green (java.awt.Color. 121 209 24)
              :red java.awt.Color/red  
              :light-blue (java.awt.Color. 23 184 239)
              :orange (java.awt.Color. 252 145 27) 
              :light-gray (java.awt.Color. 242 242 242) })

;; plot the response time over time
;; second parameter let draw a line at the specified response time
(defn perf-time-series-plot-with-threshold  [ds thresholdMs] 
 	  (def ts (sel ds :cols :ts))
	  (doto  (time-series-plot :ts :t :data ds :title "Response time over time"
				   :x-label "time" :y-label "resp. time (ms)"
				   :legend true :series-label "duration ms"
				   )
	    (set-stroke-color (:light-blue colors)) 
	    (set-stroke :width 1) 
      (add-lines  ts (repeat thresholdMs) :series-label "threshold")
			(set-stroke :dataset 1 :width 2)
			(set-stroke-color java.awt.Color/red :dataset 1) ))


;; plot the response time over time
;; may have an optional second parameter
(defn perf-time-series-plot 
  ( [ds thresholdMs] (perf-time-series-plot-with-threshold ds thresholdMs))
  ( [ds] 
	  (def ts (sel ds :cols :ts))
	  (doto  (time-series-plot :ts :t :data ds :title "Response time over time"
				   :x-label "time" :y-label "resp. time (ms)"
				   :legend true :series-label "duration ms"
				   )
	    (set-stroke-color (:light-blue colors)) 
	    (set-stroke :width 1) 
 	    )))

 

;; histogram of response times
(defn perf-histogram [ds]
    (let [plot (histogram :t
			  :title "Response time distribution"
			  :nbins 15 
			  :x-label "resp. time (ms)"
			  :data ds )
	  renderer (.getRenderer (.getPlot plot))]
      (.setPaint renderer  (:light-blue colors)) 
      (.setDrawBarOutline renderer true)
      (.setSeriesOutlinePaint renderer 0 (:light-gray colors))
      (.setSeriesOutlineStroke renderer 0 (java.awt.BasicStroke. 2))
    plot)
    )


;; draw a bar chart of the output of the function grouped by a factor (e.g. the label)
;; available summary-fct are defined in $rollup documentation http://clojuredocs.org/incanter/incanter.core/$rollup
;; aggregator function defined in aggregators should work as well
(defn horizontal-bar-chart 
  ( [ds summary-fct factor ] (horizontal-bar-chart ds summary-fct factor :light-green))
  ( [ds summary-fct factor color-key] 
	  (doto
	      (bar-chart factor :t :vertical false
				 :title (str (summary-name summary-fct)  " by " (name factor))
				 :x-label (name factor)
				 :y-label nil
				 :data  ($rollup summary-fct :t factor ds))
	      (set-stroke-color (color-key colors) :series 0) 
	    )))


;; draw a bar chart of the number of samples grouped by a factor (e.g. the label)
(defn count-bar-chart [ds factor ] ;;TODO function factorization
  (doto
      (bar-chart factor :t :vertical false
		 :title (str "Count by " (name factor))
		 :x-label (name factor)
		 :y-label nil
		 :data  ($rollup count :t factor ds))
    (set-stroke-color (:light-green colors) :series 0) 
    ))

;; draw a bar chart of the mean time grouped by a factor (e.g. the label)
(defn mean-time-bar-chart [ds factor]
  (doto
      (bar-chart factor :t :vertical false
		 :title (str "Mean by " (name factor))
		 :x-label (name factor)
		 :y-label "resp. time (ms)"
		 :data  ($rollup mean :t factor ds))
    (set-stroke-color  (:orange colors) :series 0) 
    ))

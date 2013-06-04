(ns ptang.charts
  (:use [ptang.aggregators])
  (:use [ptang.filters])
  (:use [incanter.core :only [$ $data with-data sel view $rollup $where]])
  (:use [incanter.stats :only [mean]])
  (:use [incanter.charts :only [bar-chart histogram time-series-plot add-lines set-stroke-color set-stroke]]))

(def colors { :light-green (java.awt.Color. 121 209 24)
              :red java.awt.Color/red  
              :light-blue (java.awt.Color. 23 184 239)
              :orange (java.awt.Color. 252 145 27) 
              :light-gray (java.awt.Color. 242 242 242) })

;; plot the response time over time
;; second parameter let draw a line at the specified response time
(defn perf-time-series-plot-with-threshold  [ds thresholdMs & filters] ;; TODO factorization
	 (let [filter (reduce merge filters) 
	       data (apply-filter-if-any ds filter)
        ts (sel data  :cols :ts) ]
	  (doto  (time-series-plot :ts :t :data data :title "Response time over time"
				   :x-label "time" :y-label "resp. time (ms)"
				   :legend true :series-label "duration ms"
				   )
	    (set-stroke-color (:light-blue colors)) 
	    (set-stroke :width 1) 
      (add-lines  ts (repeat thresholdMs) :series-label "threshold")
			(set-stroke :dataset 1 :width 2)
			(set-stroke-color java.awt.Color/red :dataset 1) )))
;; TODO documentation 

;; plot the response time over time
;; may have an optional second parameter
(defn perf-time-series-plot 
  ( [ds & filters] 
	 (let [filter (reduce merge filters) 
	       data (apply-filter-if-any ds filter)
        ts (sel data  :cols :ts) ]
		  (doto  (time-series-plot :ts :t :data data :title "Response time over time"
					   :x-label "time" :y-label "resp. time (ms)"
					   :legend true :series-label "duration ms"
					   )
		    (set-stroke-color (:light-blue colors)) 
		    (set-stroke :width 1) 
	 	    ))))

 

;; histogram of response times
(defn perf-histogram [ds & filters]
 (let [filter (reduce merge filters) 
       data (apply-filter-if-any ds filter)
       plot (histogram :t
							  :title "Response time distribution"
							  :nbins 15 
							  :x-label "resp. time (ms)"
							  :data data )
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
;; colors-key are listed in the colors variable
(defn horizontal-bar-chart 
    [ds & {:keys [sumf by color filter] :or {color :light-green filter {}} } ] 
    (let [data (apply-filter-if-any ds filter)]  
		  (doto
		      (bar-chart by :t :vertical false
					 :title (str (summary-name sumf)  " by " (name by))
					 :x-label (name by)
					 :y-label nil
					 :data  ($rollup sumf :t by data))
		      (set-stroke-color (color colors) :series 0) 
		    )))
;; TODO pre
;;(defn foo [{:keys [a b c]}]
;;  {:pre [(not (nil? c))]}
;;  (list a b c))

;; draw a bar chart of the number of samples grouped by a factor (e.g. the label)
(defn count-bar-chart [ds factor & filters] 
  (let [filter (reduce merge filters) ]
    (horizontal-bar-chart  ds :sumf :count :by factor :color :light-green :filter filter)))
  
;; draw a bar chart of the mean time grouped by a factor (e.g. the label)
(defn mean-time-bar-chart [ds factor & filters]
 (let [filter (reduce merge filters) ]
    (horizontal-bar-chart  ds :sumf :mean :by factor :color :orange :filter filter)))
 

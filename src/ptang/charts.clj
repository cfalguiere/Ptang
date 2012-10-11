(ns ptang.charts
  (:use [incanter.core :only [$ $data with-data sel view $rollup]])
  (:use [incanter.stats :only [mean]])
  (:use [incanter.charts :only [bar-chart histogram time-series-plot add-lines set-stroke-color set-stroke]]))

(defn perf-time-series-plot [ds thresholdMs]
  (def ts (sel ds :cols :ts))
  (doto  (time-series-plot :ts :t :data ds :title "Response time over time"
			   :x-label "time" :y-label "resp. time (ms)"
			   :legend true :series-label "duration ms"
			   )
    (set-stroke-color (java.awt.Color. 23 184 239)) 
    (set-stroke :width 1) 
    (add-lines  ts (repeat thresholdMs) :series-label "threshold")
    (set-stroke :dataset 1 :width 2)
    (set-stroke-color java.awt.Color/red :dataset 1) 
    )) 

(defn perf-histogram [ds]
    (let [plot (histogram :t
			  :title "Response time distribution"
			  :nbins 15 
			  :x-label "resp. time (ms)"
			  :data ds )
	  renderer (.getRenderer (.getPlot plot))]
      (.setPaint renderer  (java.awt.Color. 23 184 239))
      (.setDrawBarOutline renderer true)
      (.setSeriesOutlinePaint renderer 0 (java.awt.Color. 242 242 242))
      (.setSeriesOutlineStroke renderer 0 (java.awt.BasicStroke. 2))
    plot)
    )

(defn count-bar-chart [ds factor ]
  (doto
      (bar-chart factor :t :vertical false
		 :title (str "Count by " (name factor))
		 :x-label (name factor)
		 :y-label nil
		 :data  ($rollup count :t factor ds))
    (set-stroke-color (java.awt.Color. 121 209 24) :series 0) 
    ))

(defn mean-time-bar-chart [ds factor]
  (doto
      (bar-chart factor :t :vertical false
		 :title (str "Mean by " (name factor))
		 :x-label (name factor)
		 :y-label "resp. time (ms)"
		 :data  ($rollup mean :t factor ds))
    (set-stroke-color (java.awt.Color. 252 145 27) :series 0) 
    ))

(ns ptang.core
  (:use [ptang.core])
  (:use [ptang.stats]) 
  (:use [incanter.io :only [read-dataset]]))

(defn -main [& args]
  (let [filename "test-resources/readings.csv"
	ds (read-dataset filename :header true) ]
    (println "Run Summary")
    (println (run-summary ds))
    (println "Response Time Summary")
    (println (response-time-summary ds))
    (println "HTTP Code Summary")
    (http-codes-summary ds)
    ))
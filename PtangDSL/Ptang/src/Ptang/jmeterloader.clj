(ns ptang.jmeterloader
  (:use ptang.internal.xmlloader) 
  (:require [clojure.xml :as xml]
	    [incanter.core :as incanter])) 

(def pagepattern #"SU[^-]+-[^-]+-Page")
(defn is-page [sample] (re-matches pagepattern (:lb sample)))

(defn page-samples-filter [el] (and (= :sample (:tag el)) (is-page (:attrs el)) ))  

(defn read-dataset-jmeter-file
  { :doc "read a jmeter file and build a dataset"}
  [xml-file-name]
  (let [columns [:t :lb :ts :s :userId :rc  :shop :search__phrase :productId  ]]
    (incanter/dataset columns (extract-data (xml/parse xml-file-name)
					   page-samples-filter
					   columns) )))

(defn -main [& args]
  (let [ds (read-dataset-jmeter-file  "test-resources/data/jmeter-sample.jtl")]
    (println "read " (incanter/nrow ds)) 
    (incanter/save ds "output/readings.csv")))


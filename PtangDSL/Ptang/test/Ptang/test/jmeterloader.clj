(ns ptang.test.jmeterloader
  (:use clojure.test)
  (:use ptang.jmeterloader) ; code under test
  (:require [clojure.xml :as xml]
	    [incanter.core :as incanter] ))

(defn parseSample [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))
  
(def xmldata (parseSample (str "<testResults>"
			       " <sample t='976' ts='1330418007548' s='true' lb='SU01-HomePage-Page'"
			       " rc='200' search__phrase='bac plastique' productId='' userId='PT1022809331'"
			       " shop='FAB' username=''/>"
			       " <sample t='974' ts='1330418007552' s='true' lb='SU1-Access'"
			       " rc='200' search__phrase='robot' productId='' userId='PT1022809331'"
			       " shop='FAB' username=''/>"
			       " <httpSample t='976' ts='1330418007555' s='true' lb='/product'"
			       " rc='200' search__phrase='gant' productId='' userId='PT1022809331'"
			       " shop='FAB' username='' />"
			       " </testResults>")))


(deftest test-is-page 
  (is (= nil
	 (is-page {:ts "1330418007548", :shop "FAB", :lb "SU1-Access"}) )) 
  (is (= "SU01-HomePage-Page"
	 (is-page {:ts "1330418007548", :shop "FAB", :lb "SU01-HomePage-Page"}) )) )


(deftest test-selected-filter 
  (is (= 1
	 (count (filter page-samples-filter (xml-seq xmldata)) ))) 
  (is (= {:tag :sample, :attrs {:t "976" :ts "1330418007548" :s "true" :lb "SU01-HomePage-Page" :shop "FAB" 
	  :rc "200" :search__phrase "bac plastique" :productId "" :userId "PT1022809331" :username ""} :content nil}
	 (first (filter page-samples-filter (xml-seq xmldata)) ))) )

(deftest test-read-dataset-jmeter-file
  (is (= 1738
	 (incanter/nrow (read-dataset-jmeter-file "test-resources/data/jmeter-sample.jtl")) )))

(run-tests)

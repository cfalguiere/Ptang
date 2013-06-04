(ns ptang.test.internal.xmlloader
  (:use clojure.test)
  (:use ptang.internal.xmlloader) ; code under test
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


(def threecolumns [:t :lb :productId] )

(deftest test-select-keys 
  (is (= {:t "976"}
	 ( #(select-keys {:t "976" :ts "1330418007548"  :lb "SU01-HomePage-Page" :productId ""} [:t] )) )) 
  (is (= {:lb "SU01-HomePage-Page"}
	 ( #(select-keys {:t "976" :ts "1330418007548"  :lb "SU01-HomePage-Page" :productId ""} [:lb] )) )) 
  (is (= {:productId ""}
	 ( #(select-keys {:t "976" :ts "1330418007548"  :lb "SU01-HomePage-Page" :productId ""} [:productId] )) )) 
  (is (= {:t "976" :lb "SU01-HomePage-Page"}
	 ( #(select-keys {:t "976" :ts "1330418007548"  :lb "SU01-HomePage-Page" :productId ""} [:t :lb] )) )) )


(deftest test-coerce-if-num
  (is (=  2  (coerce-if-num "2"))) 
  (is (=  "a"  (coerce-if-num "a"))) 
  (is (=  ""  (coerce-if-num ""))) 
  (is (=  "149b008ns"  (coerce-if-num "149b008ns"))) )

(defn page-samples-filter [el] (= :sample (:tag el) ))  


(deftest test-extract-data
  (is (= 2
	 (count (extract-data xmldata page-samples-filter [:t :lb :productId] ) )))
  (is (= {:t 976  :lb "SU01-HomePage-Page" :productId ""}
	 (first (extract-data xmldata page-samples-filter [:t :lb :productId] )))) )


(deftest test-read-dataset-xml
  (is (= 976
	 (first (incanter/$ :t (read-dataset-xml xmldata  page-samples-filter threecolumns ) ))))
  (is (= 2
	 (incanter/nrow (read-dataset-xml xmldata  page-samples-filter threecolumns ) ))))



(deftest test-read-dataset-xml-file
  (is (= 3879
	 (incanter/nrow (read-dataset-xml (xml/parse "test-resources/data/jmeter-sample.jtl") 
				 page-samples-filter
				 [:t :lb :ts :s :userId :rc  :shop :search__phrase :productId  ]) ))))

(run-tests)



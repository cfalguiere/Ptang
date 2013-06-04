(ns ptang.internal.xmlloader
  (:require [clojure.xml :as xml]
	    [clojure.algo.generic.functor :as functor]	    
	    [incanter.core :as incanter])) 

(defn coerce-if-num [s] (if (and (not-empty s) (every? #(Character/isDigit %) (seq s))) ;number? evaluates 149b008ns to true
			  (Long/parseLong s) s ))

(defn extract-data [xml selected-filter columns]
  (map
   #(functor/fmap coerce-if-num  (select-keys (:attrs %) columns))
   (filter selected-filter (xml-seq xml))))


(defn read-dataset-xml
  { :doc "read an xml file and build a dataset"}
   [xml element-filter columns]
  (incanter/dataset columns (extract-data xml element-filter columns) ))

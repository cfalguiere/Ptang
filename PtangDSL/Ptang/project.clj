(defproject ptang "0.1.1-SNAPSHOT"
  :description "Analysis tool for performance test results"
  :url "https://github.com/cfalguiere/Ptangs"
  :license {:name "Eclipse Public License 1.0"
            :url "http://opensource.org/licenses/eclipse-1.0.php"
            :distribution "repo"
            :comments "Same license as Clojure"}
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [incanter "1.3.0"] ;; statistics
     [clj-time "0.4.4"] ;; joda time
   ]
  :dev-dependencies [[swank-clojure "1.4.2"][midje "1.4.0"]
		     [lein-midje "1.0.10"][com.stuartsierra/lazytest "1.2.3"]]
  :repositories {"stuart" "http://stuartsierra.com/maven2"}
  :main ptang.core
  ;:profiles {:dev {:plugins [[lein-midje "1.0.10"]]}
  ;:plugins [[lein-midje "1.0.10"]]
 )
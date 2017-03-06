(defproject com.yetanalytics/ring-etag-middleware "0.1.1"
  :description "A bold new etag middleware"
  :url "https://github.com/yetanalytics/ring-etag-middleware"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [pandect "0.6.1"]]

  :profiles {:dev
             {:dependencies [[org.clojure/test.check "0.9.0"]
                             [me.raynes/fs "1.4.6"]]}})

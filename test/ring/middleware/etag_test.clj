(ns ring.middleware.etag-test
  (:require [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.java.io :as io]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [pandect.algo.sha1 :refer :all]
            [me.raynes.fs :as fs]
            [ring.middleware.etag :refer :all])
  (:import java.net.URL
           java.io.File))

(defmulti valid-etag? class)

(defmethod valid-etag? String [s]
  (= (calculate-etag s) (sha1 s)))

(defmethod valid-etag? File [f]
  (= (calculate-etag f) (str (.lastModified f) "-" (.length f))))

#_(def file-gen
  (gen/fmap fs/temp-file gen/string-ascii))

(def file-gen
  (gen/let [s gen/string-ascii]
    (fs/temp-file s)))

(defspec test-string-etags 1000
  (prop/for-all [s gen/string-alphanumeric]
                (let [etag (calculate-etag s)]
                  (and (= etag (sha1 s))
                       (valid-etag? s)))))

(defspec test-file-etags 1000
  (prop/for-all [f file-gen]
                (let [etag (calculate-etag f)]
                  (and (= etag (str (.lastModified f) "-" (.length f)))
                       (valid-etag? f)))))



;(def afile (fs/temp-file "tmp"))

;(tc/quick-check 1000 property)

;(gen/fmap println gen/string-ascii)

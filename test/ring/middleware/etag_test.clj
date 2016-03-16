(ns ring.middleware.etag-test
  (:require [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [pandect.algo.sha1 :refer :all]
            [me.raynes.fs :as fs]
            [ring.middleware.etag :refer :all])
  (:import (java.io File)))

(defmulti valid-etag class)

(defmethod valid-etag? String [s]
  (= (calculate-etag s) (sha1 s)))

(defmethod valid-etag?)

(def file-gen
  (gen/fmap fs/temp-file gen/string-ascii))

(def file-gen-2
  (gen/let [s gen/string-ascii]
    (fs/temp-file s)))

(def property
  (prop/for-all [s gen/string-alphanumeric]
                (let [etag (calculate-etag s)]
                  (and (= etag (sha1 s))
                       (valid-etag? etag)))))

(def afile (fs/temp-file "tmp"))

(tc/quick-check 1000 property)

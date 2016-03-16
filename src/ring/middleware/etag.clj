(ns ring.middleware.etag
  (:require [clojure.string :as str]
            [pandect.algo.sha1 :refer :all])
  (:import (java.io File)))

(defmulti calculate-etag class)

(defmethod calculate-etag String [s]
  (sha1 s))

(defmethod calculate-etag File [f]
  (str (.lastModified f) "-" (.length f)))

(defn- not-modified [etag]
  {:status 304 :body "" :headers {"etag" etag}})

(defn wrap-etag [handler]
  (fn [req]
    (let [{body :body
           status :status
           {etag "etag"} :headers
           :as resp} (handler req)
          if-none-match (get-in req [:headers "if-none-match"])
          dispatch-value (class body)]
      (if (and etag (not= status 304))
        (if (= etag if-none-match)
          (not-modified etag)
          resp)
        (if-let [method-fn (get-method calculate-etag dispatch-value)]
          (let [etag (method-fn body)]
            (if (= etag if-none-match)
              (not-modified etag)
              (assoc-in resp [:headers "etag"] etag)))
          resp)))))

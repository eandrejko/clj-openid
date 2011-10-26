;;
;; clj-openid - clojure wrapper for OpenID4Java (http://code.google.com/p/openid4java)
;;
;; Copyright (C) 2011 Steve Lindsay
;;
;; Initially based on example from:
;; http://techylinguist.com/posts/add-google-openid-your-clojure-ringmoustache-app
;;

(ns clj-openid.core
  (:import  [org.openid4java.consumer ConsumerManager]
            [org.openid4java.message ParameterList])
  (:require [ring.util.response :as resp]))

(def google-identifier "https://www.google.com/accounts/o8/id")
(def yahoo-identifier "https://me.yahoo.com")

(defn- auth-request
  [identifier return-url]
  (let [cm              (ConsumerManager.)
        discoveries     (.discover cm identifier)
        discovery-info  (.associate cm discoveries)
        auth-req        (.authenticate cm discovery-info return-url)
        destination-url (.getDestinationUrl auth-req true)]
    {:manager cm
     :discovery-info discovery-info
     :destination-url destination-url}))

(def google-request (partial auth-request google-identifier))
(def yahoo-request (partial auth-request yahoo-identifier))

(defn redirect
  [identifier session return-url]
  (let [req  (auth-request identifier return-url)]
    (assoc (resp/redirect (:destination-url req)) 
              :session (merge session {:openid-req req}))))

(def google-redirect (partial redirect google-identifier))
(def yahoo-redirect (partial redirect yahoo-identifier))

(defn rebuild-request-url
  [{:keys [uri server-port scheme query-string server-name] :as req}]
  ;(println req)
  (str (name scheme) "://" server-name (if server-port (str ":" server-port)) uri "?" query-string))

(defn map->hashmap
  [map]
  (reduce (fn [m [k v]]
            (.put m (name k) (str v)) m) (java.util.HashMap.) map))

(defn validate
  [{:keys [params session] :as req}]
  (let [openid-req   (:openid-req session)
        {:keys [manager discovery-info]} openid-req
        request-url  (rebuild-request-url req)
        param-list   (ParameterList. (map->hashmap params))
        verification (.verify manager request-url param-list discovery-info)]
    (.getVerifiedId verification)))
    
        



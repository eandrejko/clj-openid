;;
;; clj-openid - clojure wrapper for OpenID4Java (http://code.google.com/p/openid4java)
;;
;; Copyright (C) 2011 Steve Lindsay
;;
;; Initially based on example from:
;; http://techylinguist.com/posts/add-google-openid-your-clojure-ringmoustache-app
;;

(ns clj-openid.helpers
  (:import  [org.openid4java.consumer ConsumerManager]
            [org.openid4java.message ParameterList])
  (:require [clj-openid.core :as core]
            [ring.util.response :as resp]))

(def google-identifier "https://www.google.com/accounts/o8/id")
(def yahoo-identifier "https://me.yahoo.com")

(def google-redirect (partial core/redirect google-identifier))
(def yahoo-redirect (partial core/redirect yahoo-identifier))


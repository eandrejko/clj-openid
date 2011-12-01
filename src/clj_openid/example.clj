(ns clj-openid.example
  (:use compojure.core hiccup.core hiccup.form-helpers hiccup.page-helpers)
  (:require [ring.util.response :as resp]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clj-openid.core :as openid]
            [clj-openid.helpers :as helpers]))

(def callback-url "http://localhost:3000/resp")

(defn show-response
  [params]
  (let [mode        (:openid.mode params)
        claimed-id  (:openid.claimed_id params)]
    (html
      [:table
       [:tr
        [:td "Mode       : "] [:td mode]]
       [:tr
        [:td "Claimed ID : "] [:td claimed-id]]]
      [:div (link-to "../.." "Back")])))      

(defn demo-page []
  (html
    [:div#login-box
     [:p "Test with your..."]
     [:div (link-to "/google" "Google account")]
     [:div (link-to "/yahoo" "Yahoo account")]
     [:div "or enter a valid OpenID url"]
     [:div (form-to [:post "/urllogin"] 
                    (text-field "openid-url" "<OpenID URL>")
                    (submit-button "Login"))]]))
  
(defroutes the-routes
  (GET "/" _ (demo-page))
  (GET "/google" [session] (helpers/google-redirect session callback-url))
  (GET "/yahoo" [session] (helpers/yahoo-redirect session callback-url))
  (GET "/resp" [:as r]        
       (if (openid/validate r)
         (show-response (:params r))
         (html [:p "Invalid request"])))
  (POST "/urllogin" [session openid-url] (openid/redirect openid-url session callback-url))
  (route/resources "/"))

(def app
  (handler/site the-routes))



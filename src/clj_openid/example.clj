(ns clj-openid.example
  (:use compojure.core hiccup.core hiccup.form-helpers hiccup.page-helpers)
  (:require [ring.util.response :as resp]
            [compojure.handler :as handler]
            [clj-openid.core :as openid]))

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
  
(defroutes the-routes
  (GET "/foo" [:as r] (println r) "hello")
  (GET "/" _        
       (html
         [:div (link-to "google/test" "Google")]
         [:div (link-to "yahoo/test" 
                        [:img {:src "http://l.yimg.com/a/i/reg/openid/buttons/1_new.png"}])]
         [:div (form-to [:post "/urllogin"]
                        (text-field "openid_url" "<OpenID URL>")
                        (submit-button "Login"))]))           
  (GET "/google/test" [session] 
       (openid/google-redirect session "http://localhost:3000/resp"))
  (GET "/yahoo/test" [session] 
       (openid/yahoo-redirect session "http://localhost:3000/resp"))
  (GET "/resp" [:as r]        
       (if (openid/validate r)
         (show-response (:params r))
         (html [:p "Invalid request"])))
  (POST "/urllogin" {p :params s :session} 
        (openid/redirect (:openid_url p) "http://localhost:3000/resp")))

(def app
  (handler/site the-routes))





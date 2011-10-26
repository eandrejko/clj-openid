(defproject org.pretendcow/clj-openid "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.openid4java/openid4java "0.9.5"]]
  :dev-dependencies [[compojure "0.6.5"]
                     [hiccup "0.3.7"]
                     [lein-ring "0.4.5"]]
  :ring {:handler clj-openid.example/app})

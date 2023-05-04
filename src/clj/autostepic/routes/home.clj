(ns autostepic.routes.home
  (:require
   [autostepic.layout :as layout]
   [autostepic.db.core :as db]
   [clojure.java.io :as io]
   [autostepic.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

;---------------------------------------------------------------------------------
; --- POKRETANJE STRANICA --------------------------------------------------------
;---------------------------------------------------------------------------------

;POCETNA
(defn home-page [request]
  (layout/render request "home.html"))

;------------------------------------------------

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}

   ;--STRANICE------
   ["/" {:get home-page}]
   ["/skladiste" {:get skladiste-page}]


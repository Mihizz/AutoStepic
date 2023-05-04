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

;MUSTERIJE
(defn musterije-page [request]
  (layout/render
   request
   "musterije.html"
   {:musterije (db/get-musterije)}))

;DODAJ MUSTERIJU
(defn createMusterija-page [{:keys [flash] :as request}]
  (layout/render
     request
     "create/createMusterija.html"
     (select-keys flash [:ime :prezime :email :errors])))

;---------------------------------------------------------------------------------
; --- SHEMA ----------------------------------------------------------------------
;---------------------------------------------------------------------------------

;MUSTERIJA (INSERT)
(def musterijaI-schema
  	[[:ime
  		st/required
  		st/string]
  	[:prezime
  		st/required
  		st/string]
  	[:email
  		st/required
  		st/email]])

;---------------------------------------------------------------------------------
; --- VALIDACIJA -----------------------------------------------------------------
;---------------------------------------------------------------------------------

;MUSTERIJA(INSERT)
(defn validate-musterijaI [params]
(first (st/validate params musterijaI-schema)))

;---------------------------------------------------------------------------------
; --- METODE ---------------------------------------------------------------------
;---------------------------------------------------------------------------------

;KREIRANJE MUSTERIJE
(defn create-musterija! [{:keys [params]}]
  (if-let [errors (validate-musterijaI params)]
    (-> (response/found "/dodavanjeMusterije")
        (assoc :flash (assoc params :errors errors)))
    (do
        (db/create-musterija! params)
        (response/found "/musterije"))))

;-----------------------------------------------------------------------------------------
; --- RUTIRANJE --------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------


(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}

   ;--API---------
   ["/dodajMusteriju" {:post create-musterija!}]

   ;--STRANICE------
   ["/" {:get home-page}]
   ;-------
   ["/musterije" {:get musterije-page}]
   ["/dodavanjeMusterije" {:get createMusterija-page}]


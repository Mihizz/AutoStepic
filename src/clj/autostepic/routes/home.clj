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

;IZMENI MUSTERIJU
(defn updateMusterija-page [{:keys [flash] :as request}]
  (layout/render
     request
     "edit/editMusterija.html"
     (select-keys flash [:id :ime :prezime :email :errors])))

;IZBRISI MUSTERIJU
(defn deleteMusterija-page [{:keys [flash] :as request}]
  (layout/render
     request
     "delete/deleteMusterija.html"
     (select-keys flash [:id :errors])))

;------------------------------------------------

;MESTA
(defn mesta-page [request]
  (layout/render
   request
   "mesta.html"
   {:mesta (db/get-mesta)}))

;DODAJ MESTO
(defn createMesto-page [{:keys [flash] :as request}]
  (layout/render
     request
     "create/createMesto.html"
     (select-keys flash [:red :kolona :sprat :errors])))

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

;MUSTERIJA(UPDATE)
(def musterijaU-schema
    [[:id
  		st/required
  		{:message "ID must be positive number!"
        :validate (fn [id] (> (Integer/parseInt (re-find #"\A-?\d+" id)) 0))}]
  	[:ime
  		st/required
  		st/string]
  	[:prezime
  		st/required
  		st/string]
  	[:email
  		st/required
  		st/email]])

;-----------------------------------------

 ;(DELETE)
 (def delete-schema
     [[:id
   		st/required
   		{:message "ID must be positive number!"
         :validate (fn [id] (> (Integer/parseInt (re-find #"\A-?\d+" id)) 0))}]])

;-----------------------------------------

;MESTO (INSERT)
(def mestoI-schema
  	[[:red
  		st/required
  		st/string
  		{:message "Row must be single character (A-Z)"
         :validate (fn [red] (= (count red) 1))}]
  	[:kolona
  		st/required
  		{:message "Column must be positive number!"
         :validate (fn [kolona] (> (Integer/parseInt (re-find #"\A-?\d+" kolona)) 0))}]
  	[:sprat
  		st/required
  		{:message "Floor must be positive number!"
         :validate (fn [sprat] (> (Integer/parseInt (re-find #"\A-?\d+" sprat)) 0))}]])


;---------------------------------------------------------------------------------
; --- VALIDACIJA -----------------------------------------------------------------
;---------------------------------------------------------------------------------

;MUSTERIJA(INSERT)
(defn validate-musterijaI [params]
(first (st/validate params musterijaI-schema)))

;MUSTERIJA(UPDATE)
(defn validate-musterijaU [params]
(first (st/validate params musterijaU-schema)))



;(DELETE)
(defn validate-delete [params]
(first (st/validate params delete-schema)))

;-----------------------------

;MESTO(INSERT)
(defn validate-mestoI [params]
(first (st/validate params mestoI-schema)))

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

;UPDATE MUSTERIJE
(defn update-musterija! [{:keys [params]}]
   (let [errors (validate-musterijaU params)]
     (if errors
       (-> (response/found "/izmeniMusteriju")
           (assoc :flash (assoc params :errors errors)))
       (let [musterija (db/get-musterija-by-id {:id (:id params)})]
         (if-not musterija
           (-> (response/found "/izmeniMusteriju")
               (assoc :flash (assoc params :errors {:id "User with ID doesnt exist!!"})))
           (do
             (db/update-musterija! params)
             (response/found "/musterije")))))))

;OBRISI MUSTERIJU
(defn delete-musterija! [{:keys [params]}]
   (let [errors (validate-delete params)]
     (if errors
       (-> (response/found "/izbrisiMusteriju")
           (assoc :flash (assoc params :errors errors)))
       (let [musterija (db/get-musterija-by-id {:id (:id params)})]
         (if-not musterija
           (-> (response/found "/izbrisiMusteriju")
               (assoc :flash (assoc params :errors {:id "User with ID doesnt exist!!"})))
           (let [gumeMusterije (db/proveri-musteriju {:idMusterije (:id params)})]
                (if gumeMusterije
                    (-> (response/found "/izbrisiMusteriju")
                    (assoc :flash (assoc params :errors {:id "User still has tiers left in stock!!"})))
                (do
                (db/delete-musterija! params)
                (response/found "/musterije")))))))))

;---------------------------------------------------------------------------------

;KREIRANJE MESTA
(defn create-mesto! [{:keys [params]}]
  (if-let [errors (validate-mestoI params)]
    (-> (response/found "/dodavanjeMesta")
        (assoc :flash (assoc params :errors errors)))
    (do
        (db/create-mesto! params)
        (response/found "/mesta"))))


;-----------------------------------------------------------------------------------------
; --- RUTIRANJE --------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------


(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}

   ;--API---------
   ["/dodajMusteriju" {:post create-musterija!}]
   ["/editujMusteriju" {:post update-musterija!}]
   ["/obrisiMusteriju" {:post delete-musterija!}]
   ;-------
   ["/dodajMesto" {:post create-mesto!}]

   ;--STRANICE------
   ["/" {:get home-page}]
   ;-------
   ["/musterije" {:get musterije-page}]
   ["/dodavanjeMusterije" {:get createMusterija-page}]
   ["/izmeniMusteriju" {:get updateMusterija-page}]
   ["/izbrisiMusteriju" {:get deleteMusterija-page}]
   ;-------
   ["/mesta" {:get mesta-page}]
   ["/dodavanjeMesta" {:get createMesto-page}]


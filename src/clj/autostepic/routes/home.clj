(ns autostepic.routes.home
  (:require
   [autostepic.layout :as layout]
   [autostepic.db.core :as db]
   [clojure.java.io :as io]
   [autostepic.middleware :as middleware]
   [ring.util.response]
   [struct.core :as st]
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

;IZMENI MESTO
(defn updateMesto-page [{:keys [flash] :as request}]
  (layout/render
     request
     "edit/editMesto.html"
     (select-keys flash [:id :red :kolona :sprat :errors])))

;IZBRISI MESTO
(defn deleteMesto-page [{:keys [flash] :as request}]
  (layout/render
     request
     "delete/deleteMesto.html"
     (select-keys flash [:id :errors])))

;------------------------------------------------

;SKLADISTE
(defn skladiste-page [request]
  (layout/render
   request
   "skladiste.html"
   {:gume (db/get-gume)}))

;DODAJ GUME
(defn createGume-page [{:keys [flash] :as request}]
  (layout/render
     request
     "create/createSkladiste.html"
     (merge {:musterije (db/get-musterije)}
            {:mesta (db/get-slobodna-mesta)}
     (select-keys flash [:sirina :profil :precnik :sezona :dot :kolicina :idMesto :idMusterije :errors]))))

;IZMENI GUME
(defn updateGume-page [{:keys [flash] :as request}]
  (layout/render
     request
     "edit/editSkladiste.html"
     (merge {:musterije (db/get-musterije)}
                 {:mesta (db/get-slobodna-mesta)}
          (select-keys flash [:id :sirina :profil :precnik :sezona :dot :kolicina :idMesto :idMusterije :errors]))))

;IZBRISI MESTO
(defn deleteGume-page [{:keys [flash] :as request}]
  (layout/render
     request
     "delete/deleteSkladiste.html"
     (select-keys flash [:id :errors])))

;---------------------------------------------------------------------------------
; --- SHEMA ----------------------------------------------------------------------
;---------------------------------------------------------------------------------

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

;-----------------------------------------

 ;(DELETE)
 (def delete-schema
     [[:id
   		st/required
   		{:message "ID must be positive number!"
         :validate (fn [id] (> (Integer/parseInt (re-find #"\A-?\d+" id)) 0))}]])

; --------------------------------------

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

;MESTO(UPDATE)
(def mestoU-schema
    [[:id
  		st/required
  		{:message "ID must be positive number!"
        :validate (fn [id] (> (Integer/parseInt (re-find #"\A-?\d+" id)) 0))}]
  	[:red
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

; ----------------------------------

;GUME (INSERT)
(def gumeI-schema
  	[[:sirina
        st/required
      	{:message "Column must be positive number!"
         :validate (fn [sirina] (> (Integer/parseInt (re-find #"\A-?\d+" sirina)) 0))}]
    [:profil
            st/required
          	{:message "Column must be positive number!"
             :validate (fn [profil] (> (Integer/parseInt (re-find #"\A-?\d+" profil)) 0))}]
    [:precnik
            st/required
          	{:message "Column must be positive number!"
             :validate (fn [precnik] (> (Integer/parseInt (re-find #"\A-?\d+" precnik)) 0))}]
    [:dot
                st/required
              	{:message "Column must be positive number!"
                 :validate (fn [dot] (> (Integer/parseInt (re-find #"\A-?\d+" dot)) 0))}
                {:message "Year must be a 4 digit number!"
                 :validate (fn [dot] (= (count dot) 4))}]
   [:sezona
               st/required
               st/string]
   [:idMusterije
                  st/required
                	{:message "Column must be positive number!"
                   :validate (fn [idMusterije] (> (Integer/parseInt (re-find #"\A-?\d+" idMusterije)) 0))}]
   [:idMesto
                  st/required
                	{:message "Column must be positive number!"
                   :validate (fn [idMesto] (> (Integer/parseInt (re-find #"\A-?\d+" idMesto)) 0))}]])

;GUME (INSERT)
(def gumeU-schema
    [[:id
  		st/required
  		{:message "ID must be positive number!"
        :validate (fn [id] (> (Integer/parseInt (re-find #"\A-?\d+" id)) 0))}]
  	[:sirina
        st/required
      	{:message "Column must be positive number!"
         :validate (fn [sirina] (> (Integer/parseInt (re-find #"\A-?\d+" sirina)) 0))}]
    [:profil
            st/required
          	{:message "Column must be positive number!"
             :validate (fn [profil] (> (Integer/parseInt (re-find #"\A-?\d+" profil)) 0))}]
    [:precnik
            st/required
          	{:message "Column must be positive number!"
             :validate (fn [precnik] (> (Integer/parseInt (re-find #"\A-?\d+" precnik)) 0))}]
    [:dot
                st/required
              	{:message "Column must be positive number!"
                 :validate (fn [dot] (> (Integer/parseInt (re-find #"\A-?\d+" dot)) 0))}
                {:message "Year must be a 4 digit number!"
                 :validate (fn [dot] (= (count dot) 4))}]
   [:idMusterije
                  st/required
                	{:message "Column must be positive number!"
                   :validate (fn [idMusterije] (> (Integer/parseInt (re-find #"\A-?\d+" idMusterije)) 0))}]
   [:idMesto
                  st/required
                	{:message "Column must be positive number!"
                   :validate (fn [idMesto] (> (Integer/parseInt (re-find #"\A-?\d+" idMesto)) 0))}]])

;---------------------------------------------------------------------------------
; --- VALIDACIJA -----------------------------------------------------------------
;---------------------------------------------------------------------------------

;MUSTERIJA(INSERT)
(defn validate-musterijaI [params]
(first (st/validate params musterijaI-schema)))

;MUSTERIJA(UPDATE)
(defn validate-musterijaU [params]
(first (st/validate params musterijaU-schema)))

;-----------------------------

;(DELETE)
(defn validate-delete [params]
(first (st/validate params delete-schema)))

; ---------------------------

;MESTO(INSERT)
(defn validate-mestoI [params]
(first (st/validate params mestoI-schema)))

;MESTO(UPDATE)
(defn validate-mestoU [params]
(first (st/validate params mestoU-schema)))

; ---------------------------

;MESTO(INSERT)
(defn validate-gumeI [params]
(first (st/validate params gumeI-schema)))

;MESTO(UPDATE)
(defn validate-gumeU [params]
(first (st/validate params gumeU-schema)))

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

;UPDATE MESTO
(defn update-mesto! [{:keys [params]}]
   (let [errors (validate-mestoU params)]
     (if errors
       (-> (response/found "/izmeniMesto")
           (assoc :flash (assoc params :errors errors)))
       (let [mesto (db/get-mesto-by-id {:id (:id params)})]
         (if-not mesto
           (-> (response/found "/izmeniMesto")
               (assoc :flash (assoc params :errors {:id "Place with ID doesnt exist!!"})))
           (do
             (db/update-mesto! params)
             (response/found "/mesta")))))))

;OBRISI MESTO
(defn delete-mesto! [{:keys [params]}]
   (let [errors (validate-delete params)]
     (if errors
       (-> (response/found "/izbrisiMesto")
           (assoc :flash (assoc params :errors errors)))
       (let [mesto (db/get-mesto-by-id {:id (:id params)})]
         (if-not mesto
           (-> (response/found "/izbrisiMesto")
               (assoc :flash (assoc params :errors {:id "Place with ID doesnt exist!!"})))
           (let [gumeMesta (db/proveri-mesto {:idMesto (:id params)})]
                (if gumeMesta
                    (-> (response/found "/izbrisiMesto")
                    (assoc :flash (assoc params :errors {:id "Place still has tiers left in it!!"})))
                (do
                (db/delete-mesto! params)
                (response/found "/mesta")))))))))

;---------------------------------------------------------------------------------

;SKLADISTE
(defn create-gume! [{:keys [params]}]
   (let [errors (validate-gumeI params)]
     (if errors
       (-> (response/found "/dodavanjeGuma")
           (assoc :flash (assoc params :errors errors)))
       (let [mesto (db/get-slobodna-mesta-by-id {:id (:idMesto params)})]
         (if-not mesto
           (-> (response/found "/dodavanjeGuma")
               (assoc :flash (assoc params :errors {:idMesto "Pick ID that is in listView places!!!"})))
           (let [musterija (db/get-musterija-by-id {:id (:idMusterije params)})]
             (if-not musterija
               (-> (response/found "/dodavanjeGuma")
                   (assoc :flash (assoc params :errors {:idMusterije "Pick ID that is in listView customers!!!"})))
               (do
                 (db/create-gume! params)
                 (db/popuni-mesto! {:id (:idMesto params) :kolicina (:kolicina params)})
                 (response/found "/skladiste")))))))))

;SKLADISTE
(defn update-gume! [{:keys [params]}]
  (let [errors (validate-gumeU params)]
    (if errors
      (-> (response/found "/izmeniGume")
          (assoc :flash (assoc params :errors errors)))
      (let [mesto (db/get-slobodna-mesta-by-id {:id (:idMesto params)})]
        (if-not mesto
          (-> (response/found "/izmeniGume")
              (assoc :flash (assoc params :errors {:idMesto "Pick ID that is in listView places!!!"})))
          (let [musterija (db/get-musterija-by-id {:id (:idMusterije params)})]
            (if-not musterija
              (-> (response/found "/izmeniGume")
                  (assoc :flash (assoc params :errors {:idMusterije "Pick ID that is in listView customers!!!"})))
              (let [gume (db/get-gume-by-id {:id (:id params)})]
                (if-not gume
                  (-> (response/found "/izmeniGume")
                      (assoc :flash (assoc params :errors {:id "Reservation with ID doesnt exist!!!"})))
                  (do
                    (db/update-gume! params)
                    (db/oslobodi-mesto! {:id (:idmesto gume)})
                    (db/popuni-mesto! {:id (:idMesto params) :kolicina (:kolicina params)})
                    (response/found "/skladiste")))))))))))

;OBRISI GUME
(defn delete-gume! [{:keys [params]}]
   (let [errors (validate-delete params)]
     (if errors
       (-> (response/found "/izbrisiGume")
           (assoc :flash (assoc params :errors errors)))
       (let [gume (db/get-gume-by-id {:id (:id params)})]
         (if-not gume
           (-> (response/found "/izbrisiGume")
               (assoc :flash (assoc params :errors {:id "Place with ID doesnt exist!!"})))
           (do
             (db/oslobodi-mesto! {:id (:idmesto gume)})
             (db/delete-gume! params)
             (response/found "/skladiste")))))))

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
   ["/editujMesto" {:post update-mesto!}]
   ["/obrisiMesto" {:post delete-mesto!}]
   ;-------
   ["/dodajGume" {:post create-gume!}]
   ["/editujGume" {:post update-gume!}]
   ["/obrisiGume" {:post delete-gume!}]


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
   ["/izmeniMesto" {:get updateMesto-page}]
   ["/izbrisiMesto" {:get deleteMesto-page}]
   ;-------
   ["/skladiste" {:get skladiste-page}]
   ["/dodavanjeGuma" {:get createGume-page}]
   ["/izmeniGume" {:get updateGume-page}]
   ["/izbrisiGume" {:get deleteGume-page}]
   ])
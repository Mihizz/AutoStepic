-- :name create-musterija! :! :n
-- :doc kreira novu musteriju
INSERT INTO musterije
(ime, prezime, email)
VALUES (:ime, :prezime, :email)

-- :name get-musterije :? :*
-- :doc prikazuje listu svih musterija
SELECT * from musterije

-- :name update-musterija! :! :n
-- :doc izmenjuje musteriju koja ima uneti id
UPDATE musterije
SET ime = :ime, prezime = :prezime, email = :email
WHERE id = :id

-- :name delete-musterija! :! :n
-- :doc brise musteriju koja ima uneti id
DELETE FROM musterije
WHERE id = :id

-- :name get-musterija-by-id :? :1
-- :doc prikazuje musteriju koja ima uneti id
SELECT * FROM musterije
WHERE id = :id

--------------------------------------
--------------------------------------

-- :name create-mesto! :! :n
-- :doc kreira novo mesto
    INSERT INTO mesto
(red, kolona, sprat, zauzeto)
VALUES (:red, :kolona, :sprat, 0)

-- :name get-mesta :? :*
-- :doc prikazuje listu svih mesta
SELECT * from mesto

-- :name get-slobodna-mesta :? :*
-- :doc prikazuje listu svih slobodnih mesta
SELECT * from mesto WHERE zauzeto = 0

-- :name update-mesto! :! :n
-- :doc izmenjuje mesto koje ima uneti id
UPDATE mesto
SET red = :red, kolona = :kolona, sprat = :sprat
WHERE id = :id

-- :name delete-mesto! :! :n
-- :doc brise mesto koje ima uneti id
DELETE FROM mesto
WHERE id = :id

-- :name get-mesto-by-id :? :1
-- :doc prikazuje mesto koje ima uneti id
SELECT * FROM mesto
WHERE id = :id

-- :name get-slobodna-mesta-by-id :? :1
-- :doc prikazuje listu svih slobodnih mesta
SELECT * from mesto WHERE zauzeto = 0 AND id = :id

--------------------------------------
--------------------------------------

-- :name create-gume! :! :n
-- :doc kreira nove gume u skladistu
    INSERT INTO skladiste
(sirina, profil, precnik, dot, sezona, kolicina, idMusterije, idMesto)
VALUES (:sirina, :profil, :precnik, :dot, :sezona, :kolicina, :idMusterije, :idMesto)

-- :name get-gume :? :*
-- :doc prikazuje listu svih guma
SELECT skladiste.*, musterije.ime, musterije.prezime, mesto.red, mesto.kolona, mesto.sprat
FROM skladiste
         INNER JOIN musterije ON skladiste.idMusterije = musterije.id
         INNER JOIN mesto ON skladiste.idMesto = mesto.id;


-- :name update-gume! :! :n
-- :doc izmenjuje gume koje imaju uneti id
UPDATE skladiste
SET sirina = :sirina, profil = :profil, precnik = :precnik, dot = :dot, sezona = :sezona, kolicina = :kolicina, idMusterije = :idMusterije, idMesto = :idMesto
WHERE id = :id

-- :name delete-gume! :! :n
-- :doc brise gume koje imaju uneti id
DELETE FROM skladiste
WHERE id = :id

-- :name get-gume-by-id :? :1
-- :doc prikazuje gume koje imaju uneti id
SELECT * FROM skladiste
WHERE id = :id

--------------------------------------
--------------------------------------

-- :name popuni-mesto! :! :2
-- :doc dodaje gume na odredjeno mesto
UPDATE mesto
SET zauzeto = :kolicina
WHERE id = :id;

-- :name oslobodi-mesto! :! :1
-- :doc oslobadja odredjeno mesto
UPDATE mesto
SET zauzeto = 0
WHERE id = :id;

-- :name proveri-mesto :! :1
-- :doc proveri da li je mesto slobodno pre brisanja mesta
SELECT * FROM skladiste
WHERE idMesto = :idMesto

-- :name proveri-musteriju :? :1
-- :doc proveri da li je musterija ima rezervacije pre brisanja musterija
SELECT * FROM skladiste
WHERE idMusterije = :idMusterije



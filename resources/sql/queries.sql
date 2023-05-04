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

--------------------------------------
--------------------------------------

-- :name proveri-mesto :! :1
-- :doc proveri da li je mesto slobodno pre brisanja mesta
SELECT * FROM skladiste
WHERE idMesto = :idMesto

-- :name proveri-musteriju :? :1
-- :doc proveri da li je musterija ima rezervacije pre brisanja musterija
SELECT * FROM skladiste
WHERE idMusterije = :idMusterije

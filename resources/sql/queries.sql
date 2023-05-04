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

-- :name get-musterija-by-id :? :1
-- :doc prikazuje musteriju koja ima uneti id
SELECT * FROM musterije
WHERE id = :id

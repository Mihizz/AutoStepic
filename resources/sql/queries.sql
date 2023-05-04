-- :name create-musterija! :! :n
-- :doc kreira novu musteriju
INSERT INTO musterije
(ime, prezime, email)
VALUES (:ime, :prezime, :email)


-- :name get-musterije :? :*
-- :doc prikazuje listu svih musterija
SELECT * from musterije

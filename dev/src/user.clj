(ns user
  (:require [ragtime.jdbc :as jdbc]))

; TODO ragtime を使って migrationを行うための定義。もっとうまくできるはず
(def dev-migration-config
  {:datastore  (jdbc/sql-database {:connection-uri "jdbc:postgresql://localhost:5432/duct-sample-development?user=postgres&password="})
   :migrations (jdbc/load-resources "migrations")})

(def test-migration-config
  {:datastore  (jdbc/sql-database {:connection-uri "jdbc:postgresql://localhost:5432/duct-sample-test?user=postgres&password="})
   :migrations (jdbc/load-resources "migrations")})

(defn dev
  "Load and switch to the 'dev' namespace."
  []
  (require 'dev)
  (in-ns 'dev)
  :loaded)

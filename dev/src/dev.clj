(ns dev
  (:refer-clojure :exclude [test])
  (:require [clojure.repl :refer :all]
            [fipp.edn :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.io :as io]
            [duct.core :as duct]
            [duct.core.repl :as duct-repl]
            [ragtime.repl :as ragtime-repl]
            [ragtime.jdbc :as jdbc]
            [eftest.runner :as eftest]
            [integrant.core :as ig]
            [integrant.repl :refer [clear halt go init prep reset]]
            [integrant.repl.state :refer [config system]]))

(duct/load-hierarchy)

(defn read-config []
  (duct/read-config (io/resource "dev.edn")))

(defn test []
  (eftest/run-tests (eftest/find-tests "test")))

; NOTE migration用のメソッド。env は "test" か "dev" を渡す
(defn read-config-env [env]
  (duct/read-config (io/resource (str env ".edn"))))

(defn migration-config [env]
  (let [url (-> (read-config-env env) :duct.module/sql :database-url)]
    {:datastore  (jdbc/sql-database {:connection-uri url})
     :migrations (jdbc/load-resources "migrations")}))

(defn migrate [env]
  (ragtime-repl/migrate (migration-config env)))

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test")

(when (io/resource "local.clj")
  (load "local"))

(integrant.repl/set-prep! (comp duct/prep read-config))

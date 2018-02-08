(ns duct-sample.boundary.users-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [integrant.core :as ig]
            [duct-sample.boundary.users :as users]
            [fipp.edn :refer (pprint) :rename {pprint fipp}])
  (:use [duct.database.sql]))

(def db-spec "jdbc:postgresql://localhost:5432/duct-sample-test?user=postgres")

; これでSQLを実際のデータベースで実行できる。
; use ではなく require だと -> がうまく出来なかった
(def db (->Boundary db-spec))

(defn create-data []
  (do
    (println "create-data")
    (users/create-user db {:name "kbaba" :email "kbaba@example.com"})))

(defn delete-data []
  (do
    (println "delete-test")
    (jdbc/delete! db-spec :users [])))

(defn my-test-fixture [f]
        (create-data)
        (f)
        (delete-data))

(use-fixtures :once my-test-fixture)

(deftest test-get-users
  (testing "get users"
    (let [user-data (first (users/get-users db))]
      (is (= "kbaba@example.com" (:email user-data))
      (is (= "kbaba" (:name user-data)))
    ))))

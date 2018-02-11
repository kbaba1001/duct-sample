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

; -----------------------------

(deftest test-get-users
  (testing "get users"
    (let [user-data (first (users/get-users db))]
      (are [expected actual] (= expected actual)
        "kbaba@example.com" (:email user-data)
        "kbaba" (:name user-data)))))

(deftest test-create-and-get-user
  (testing "create and get user"
    (let [user-id (:id (first (users/create-user db {:name "user1" :email "user1@example.com"})))
          user-data (first (users/get-user db user-id))]
      (are [expected actual] (= expected actual)
        "user1" (:name user-data)
        "user1@example.com" (:email user-data)))))

(deftest test-update-user
  (testing "update user"
    (let [user-id (:id (first (users/create-user db {:name "user1" :email "user1@example.com"})))
          updated-user-id (:id (first (users/update-user db user-id {:name "user2" :email "user2@example.com"})))
          user-data (first (users/get-user db user-id))]
      (are [expected actual] (= expected actual)
        "user2" (:name user-data)
        "user2@example.com" (:email user-data)))))

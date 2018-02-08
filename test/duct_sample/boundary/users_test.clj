(ns duct-sample.boundary.users-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [integrant.core :as ig]
            [duct-sample.boundary.users :as users]
            [fipp.edn :refer (pprint) :rename {pprint fipp}])
  (:use [duct.database.sql]))

(def db-spec "jdbc:postgresql://localhost:5432/duct-sample-test?user=postgres")

; (defn db []
;   (->Boundary db-spec))

; (defn create-data []
;   (do
;     (println "create-data")
;     (users/create-user db {:name "kbaba" :email "kbaba@example.com"})))

; (defn delete-data []
;   (do
;     (println "delete-test")
;     (jdbc/delete! db-spec :users [])))

; (defn my-test-fixture [f]
;         (create-data)
;         (f)
;         (delete-data))

; (use-fixtures :once my-test-fixture)

; これでSQLを実際のデータベースで実行できる。
; use ではなく require だと -> がうまく出来なかった
(deftest test-get-users
  (testing "get users"
    (let [db (->Boundary db-spec)]
      (do
        (users/create-user db {:name "kbaba" :email "kbaba@example.com"}) ; データの作成
        (is (= 5 (users/get-users db)))     ; テストを実行
        (jdbc/delete! db-spec :users []))) ; データの消去
    ))

; (let [db (->Boundary db-spec)]
;   (do
;     (users/create-user db {:name "kbaba" :email "kbaba@example.com"}) ; データの作成
;     (println (users/get-users db))     ; テストを実行
;     (jdbc/delete! db-spec :users []))) ; データの消去

; (deftest test-get-users
;   (testing "get users"
;     (do
;       (println "test-get-users")
;       (println (users/get-users db)))))



; TODO eftestはパラレル実行なので、ひとまずシングル実行にしておきたい
; TODO before, after 的な処理の書き方を知りたい。

; (deftest get-users-test
;   (testing "get users"
;     (let [db (ig/ref :duct.database/sql)]
;       (is (= "aaa" (users/get-users db))))))

; (users/get-users db)

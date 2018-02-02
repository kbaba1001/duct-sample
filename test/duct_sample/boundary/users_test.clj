(ns duct-sample.boundary.users-test
  (:require [clojure.test :refer :all]
            [integrant.core :as ig]
            [duct-sample.boundary.users :as users]
            [fipp.edn :refer (pprint) :rename {pprint fipp}])
  (:use [duct.database.sql]))

(def db-spec "jdbc:postgresql://localhost:5432/duct-sample-test?user=postgres&password=postgres")

; これでSQLを実際のデータベースで実行できる。
; use ではなく require だと -> がうまく出来なかった
(let [db (->Boundary db-spec)]
  (println (users/get-users db)))


; (deftest get-users-test
;   (testing "get users"
;     (let [db (ig/ref :duct.database/sql)]
;       (is (= "aaa" (users/get-users db))))))

; (users/get-users db)

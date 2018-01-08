(ns duct-sample.boundary.users
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.core :refer [format]]
            [duct.database.sql]))

(defprotocol Users
  (get-users [db])
  (get-user [db id])
  (create-user [db params])
  (update-user [db id params])
  (delete-user [db id]))

(extend-protocol Users
  duct.database.sql.Boundary
  (get-users [{:keys [spec]}]
    (jdbc/query spec ["SELECT * FROM users;"]))
  (get-user [{:keys [spec]} id]
    (jdbc/query spec [(format "SELECT * FROM users WHERE id = '%s';" id)]))
  (create-user [{:keys [spec]} params]
    (jdbc/query spec [(format "INSERT INTO users (name, email) VALUES ('%s', '%s') RETURNING id;"
                              (:name params)
                              (:email params))]))
  (update-user [{:keys [spec]} id params]
    (jdbc/query spec [(format "UPDATE users SET name = '%s', email = '%s' WHERE id = '%s' RETURNING id;"
                              (:name params)
                              (:email params)
                              id)]))
  (delete-user [{:keys [spec]} id]
    (jdbc/query spec [(format "DELETE FROM users WHERE id = '%s' RETURNING *;" id)])))

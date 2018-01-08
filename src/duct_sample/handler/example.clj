(ns duct-sample.handler.example
  (:use [hiccup.core])
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [clojure.string :as str]
            [duct-sample.boundary.users :as db.users]
            [duct-sample.spec.user :as s.user]
            [integrant.core :as ig]))

(defmethod ig/init-key :duct-sample.handler/example [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok (html [:span "This is an example handler"])]))

(defn error-message-line [error]
  [:div {:style "background: #fcc; margin-bottom: 5px;"} error])

(defn user-form [action method user & {:keys [error-messages]}]
  [:form {:action action :method "post"}
   [:input {:name "_method" :value method :type "hidden"}]
   [:div
    [:label {:for "name"} "name"]
    [:input {:name "name" :value (:name user)}]
    (if-let [error (:name error-messages)]
      (error-message-line error))]
   [:div
    [:label {:for "email"} "email"]
    [:input {:name "email" :value (:email user)}]
    (if-let [error (:email error-messages)]
      (error-message-line error))]
   [:button {:type "submit"} "Submit"]])

(defn show-users-view [users]
  (html [:div
         [:div "Users"]
         [:a {:href "/users/new"} "Add user"]
         [:table
          [:thead
           [:tr
            [:th "id"]
            [:th "name"]]]
          [:tbody
           (for [user users]
             [:tr
              [:td [:a {:href (str "/users/" (:id user))} (:id user)]]
              [:td (:name user)]])]]]))

(defn show-user-view [user]
  (html [:div "User"
         (pr-str user)
         [:div
          [:a {:href (str "/users/" (:id user) "/edit")} "edit"]
          [:form {:action (str "/users/" (:id user) "/delete") :method "post"}
           [:button {:type "submit"} "delete"]]]]))

(defn new-user-view [user error-messages]
  (html [:div "New User"
         (user-form "/users/" "post" user
                    :error-messages error-messages)]))

(defn edit-user-view [user-id user error-messages]
  (html [:div "Edit User"
         (user-form (str "/users/" user-id "/update") "put" user
                    :error-messages error-messages)]))

(defmethod ig/init-key :duct-sample.handler/user-new [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok (new-user-view nil nil)]))

(defmethod ig/init-key :duct-sample.handler/user-index [_ {:keys [db]}]
  (fn [{[_] :ataraxy/result}]
    (let [users (db.users/get-users db)]
      [::response/ok (show-users-view users)])))

(defmethod ig/init-key :duct-sample.handler/user-create [_ {:keys [db]}]
  (fn [{[_ params] :ataraxy/result}]
    (if (s.user/valid? params)
      (let [user (first (db.users/create-user db params))]
        [::response/found (str "/users/" (:id user))])
      [::response/ok (new-user-view params (s.user/error-messages params))])))

(defmethod ig/init-key :duct-sample.handler/user-show [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result}]
    (let [user (first (db.users/get-user db id))]
      [::response/ok (show-user-view user)])))

(defmethod ig/init-key :duct-sample.handler/user-edit [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result}]
    (let [user (first (db.users/get-user db id))]
      [::response/ok (edit-user-view id user nil)])))

(defmethod ig/init-key :duct-sample.handler/user-update [_ {:keys [db]}]
  (fn [{[_ id params] :ataraxy/result}]
    (if (s.user/valid? params)
      (let [user (first (db.users/update-user db id params))]
        [::response/found (str "/users/" (:id user))])
      [::response/ok (edit-user-view id params (s.user/error-messages params))])))

(defmethod ig/init-key :duct-sample.handler/user-destroy [_ {:keys [db]}]
  (fn [{[_ id] :ataraxy/result}]
    (let [user (first (db.users/delete-user db id))]
      [::response/found "/users/"])))

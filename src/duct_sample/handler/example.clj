(ns duct-sample.handler.example
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response] 
            [integrant.core :as ig]))

(defmethod ig/init-key :duct-sample.handler/example [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "This is an example handler"]))

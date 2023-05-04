(ns autostepic.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [autostepic.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[autostepic started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[autostepic has shut down successfully]=-"))
   :middleware wrap-dev})

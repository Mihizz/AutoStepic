(ns autostepic.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[autostepic started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[autostepic has shut down successfully]=-"))
   :middleware identity})

(ns samestep.boot-refresh.impl
  (:require [boot.util :as util]
            [clojure.tools.namespace.dir :as dir]
            [clojure.tools.namespace.track :as track]
            [clojure.tools.namespace.reload :as reload]))

(defn refresh-env
  [tracker dirs]
  (let [filter-ns (fn [ns] (filter find-ns ns))]
    (swap! tracker
           (fn [tracker]
             (util/dbug "Scan directories: %s\n" (pr-str dirs))
             (-> (or tracker (track/tracker))
                 (dir/scan-dirs dirs)
                 ;; Only reload namespaces which are already loaded
                 (update ::track/load filter-ns)
                 (update ::track/unload filter-ns))))
    (util/info "Unload: %s\n" (pr-str (::track/unload @tracker)))
    (util/info "Load: %s\n" (pr-str (::track/load @tracker)))
    (swap! tracker reload/track-reload)
    (try
      (when (::reload/error @tracker)
        (util/fail "Error reloading: %s\n" (name (::reload/error-ns @tracker)))
        (throw (::reload/error @tracker)))
      (catch java.io.FileNotFoundException e
        (util/info "Resetting tracker due to file not found exception, all namespaces will be reloaded next time.\n")
        (reset! tracker (track/tracker))
        (throw e)))))

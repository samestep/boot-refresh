(ns samestep.boot-refresh
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.util :as util]
            [clojure.tools.namespace.dir :as dir]
            [clojure.tools.namespace.track :as track]
            [clojure.tools.namespace.reload :as reload]))

(boot/deftask refresh
  "Reload all changed namespaces on the classpath.

  Throws an exception in the case of failure."
  []
  (let [tracker (atom nil)
        filter-ns (fn [ns] (filter find-ns ns))]
    (boot/with-pass-thru _
      (swap! tracker
             (fn [tracker]
               (util/dbug "Scan directories: %s\n" (pr-str (boot/get-env :directories)))
               (-> (or tracker (track/tracker))
                   (dir/scan-dirs (boot/get-env :directories))
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
          (throw e))))))

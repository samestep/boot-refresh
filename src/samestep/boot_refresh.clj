(ns samestep.boot-refresh
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [boot.pod :as pod]
            [boot.util :as util]
            [samestep.boot-refresh.impl :as impl]))

(boot/deftask refresh
  "Reload all changed namespaces on the classpath.

  Throws an exception in the case of failure."
  []
  (let [tracker (atom nil)
        deps '[[org.clojure/tools.namespace "0.3.0-alpha3"]]]
    (boot/with-pass-thru _
      (util/info "Refreshing parent environment\n")
      (impl/refresh-env tracker (boot/get-env :directories))
      (doseq [pod (remove #(= "worker" (pod/pod-name %)) (keys pod/pods))]
        (pod/with-eval-in pod
          (require 'boot.pod
                   'boot.util)
          (boot.pod/add-dependencies-in (.get boot.pod/this-pod)
                                        (update-in boot.pod/env [:dependencies] into '~deps))
          (require 'samestep.boot-refresh.impl
                   'clojure.tools.namespace.track)
          (boot.util/info "Refreshing pod environment\n")
          (defonce tracker (atom (clojure.tools.namespace.track/tracker)))
          (samestep.boot-refresh.impl/refresh-env tracker (:directories boot.pod/env)))))))

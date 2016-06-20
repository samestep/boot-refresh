(ns samestep.boot-refresh
  {:boot/export-tasks true}
  (:require [boot.core :as boot]
            [clojure.tools.namespace.repl :as tns]))

(boot/deftask refresh
  "Reload all changed namespaces on the classpath.

  Throws an exception in the case of failure."
  []
  (boot/with-pass-thru _
    (apply tns/set-refresh-dirs (boot/get-env :directories))
    (with-bindings {#'*ns* *ns*}
      (let [result (tns/refresh)]
        (when (instance? Throwable result)
          (throw result))))))

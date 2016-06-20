(set-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]
                 [boot/core "2.6.0" :scope "provided"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [adzerk/bootlaces "0.1.13" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
 pom {:project 'samestep/boot-refresh
      :version +version+
      :description "Boot task to reload code using clojure.tools.namespace."
      :url "https://github.com/samestep/boot-refresh"
      :scm {:url "https://github.com/samestep/boot-refresh"}
      :license {"MIT License" "https://opensource.org/licenses/MIT"}})

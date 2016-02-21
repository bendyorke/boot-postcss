(set-env!
  :source-paths #{"src" "test"}
  :resource-paths #{"js" "html"}
  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [adzerk/bootlaces "0.1.9" :scope "test"]
                  [adzerk/boot-test "1.1.0" :scope "test"]
                  [org.clojure/data.json "0.2.6"]])

(require '[bendyorke.boot-postcss :refer [postcss]]
         '[adzerk.boot-test :refer [test]]
         '[adzerk.bootlaces :refer :all])

(def +version+ "0.0.1")

(bootlaces! +version+)

(task-options!
  pom {:project     'bendyorke/boot-postcss
       :version     +version+
       :description "PostCSS & CSS Modules in ClojureScript"
       :url         "https://github.com/bendyorke/boot-postcss"
       :scm {:url   "https://github.com/bendyorke/boot-postcss"
             :name  "git"}})

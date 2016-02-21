(ns bendyorke.boot-postcss
  {:boot/export-tasks true}
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.edn :as edn]
            [boot.core :refer :all]
            [postcss.core :as postcss]
            [css-modules.loader :as loader]))

(def EDN_FILENAME "css_modules.edn")

(defn processed-file
  "Return the java.File for the module file (icss format)"
  [file]
  (io/file (str (tmp-file file) ".module")))

(defn find-css-files
  "Return all .css files in the fileset.  Assoc the processed-file for convenience"
  [fileset]
  (->> fileset
       input-files
       (by-ext [".css"])
       (map #(assoc % :processed-file (processed-file %)))))

(defn process-file
  "Run a given file through postcss and save it off to the module-file.
  CSS Modules will need access to all the CSS files in icss format."
  [{processed-file :processed-file :as file}]
  (let [content (-> file tmp-file slurp postcss/process)]
    (spit processed-file content)
    content))

(defn write-css-to
  "Save files to their final location in the fileset."
  [output-dir output-filename fileset get-content]
    (doseq [file fileset]
      (let [contents (get-content file)]
        (if output-filename
          (spit (io/file output-dir output-filename) contents :append true)
          (spit (tmp-file file) contents)))))

(defn write-edn-to
  "Save edn file containing module mappings"
  [dir exports]
  (spit (io/file dir EDN_FILENAME)
        (pr-str exports)))

(defn process-files
  "Run files through postcss.  Save files if modules is false"
  [files]
    (println "Running" (count files) "files through postcss")
    (doall (map process-file files)))

(defn parse-file
  "Get the css tree of a file"
  [file]
  (postcss/parse (-> file :processed-file slurp)))

(defn get-translations
  [files]
  (let [filemap  (into {} (map (juxt :path :tree) files))
        trees    (map :tree files)
        resolver #(filemap %)]
    (into {} (map #(loader/resolve-imports % resolver) trees))))

(defn slice-n-dice-modules
  "Extract modules from files"
  [files]
  (println "Slicing css into modules")
  (let [parsed-files (map #(assoc % :tree (parse-file %)) files)
        translations (get-translations parsed-files)]
  (loop [[file & remain] parsed-files
         css-trees       {}
         all-exports     {}]
    (if-not file
      [css-trees all-exports]
      (let [[css-tree exports] (loader/extract-css (get file :tree) translations)
            path               (get file :path)]
        (recur remain
               (assoc css-trees path css-tree)
               (assoc all-exports path exports)))))))

(deftask postcss
  [o output-filename NAME str  "File to concat all styles into.  If none is provided,
                                each css file will be written seperately"
   m modules              bool "Enable CSS Modules"]
  (let [dir (tmp-dir!)]
    (with-pre-wrap [fileset]
      (postcss/create)
      (let [css-files (find-css-files fileset)]
        (process-files css-files)
        (if-not modules
          (write-css-to dir output-filename css-files #(slurp % :processed-file))
          (let [[modules exports] (slice-n-dice-modules css-files)]
            (write-css-to dir output-filename css-files #(-> % :path modules))
            (write-edn-to dir exports))))
      (-> fileset (add-resource dir) commit!))))

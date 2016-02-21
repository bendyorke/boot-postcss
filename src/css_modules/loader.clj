(ns css-modules.loader
  (:require [postcss.core :refer [parse stringify]]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]))

(def import-re #"^:import\([\W]?(.+?)[\W]?\)$")

(defn import-file-node?
  "Checks to see if a node is an import node that imports a file"
  [node]
  (and (= (node :type) "rule")
       (re-find #"^:import\((.+)\)$" (node :selector))))

(defn export-node?
  "Checks to see if a node is an export node"
  [node]
  (and (= (node :type) "rule")
       (= (node :selector) ":export")))

(defn import-filename
  "Fetch and replace the values in a node"
  [node]
  (if node
    (last (re-matches import-re (node :selector)))))

(defn export-decls
  [tree]
  (->> tree
       (filter export-node?)
       (reduce #(concat %1 (%2 :nodes)))
       (reduce #())))

(defn tree->map
  [tree]
  (->> tree
       :nodes
       (map (juxt :prop :value))
       (into {})))

(defn resolve-imports
  "For any node that imports a value from file, resolve that value"
  [tree resolver]
  (let [import-nodes (filter import-file-node? (tree :nodes))]
    (loop [[node & remain] import-nodes
           translations    {}]
      (if-not node
        translations
        (let [file    (->> node
                           import-filename
                           resolver)
              exports (if file (->> file
                                    :nodes
                                    (filter export-node?)
                                    (map tree->map)
                                    (into {})))
              transl  (map (juxt :prop #(exports (% :value)))
                           (node :nodes))]
          (recur remain
                 (into translations transl)))))))

(defn update-all
  [m f & args]
  (reduce
    (fn [r [k v]]
      (assoc r k (apply f v args))) {} m))

(defmulti translate :type)
(defmethod translate "decl"
  [decl translations]
  (update-in decl [:value] translate translations))
(defmethod translate "atrule"
  [rule translations]
  (if (= "media" (:name rule))
    (update-in rule [:params] translate translations)
    rule))
(defmethod translate "rule"
  [rule translations]
  (update-in rule [:nodes] (partial map #(translate % translations))))
(defmethod translate "root"
  [rule translations]
  (update-in rule [:nodes] (partial map #(translate % translations))))
(defmethod translate :default
  [value translations]
  (if (string? value)
    (reduce (fn [m [src tar]] (str/replace m src tar)) value translations)
    value))

(defn extract-css
  "Extract the exports and css from a css/icss string.  Resolves any imports
  from the fileset - a map of filenames to css/icss strings.
  Returns [css, exports]"
  [tree translations]
  (let [{nodes :nodes :as tree}  (translate tree translations)
        [export-nodes css-nodes] ((juxt filter remove) export-node? nodes)
        [import-nodes css-nodes] ((juxt filter remove) import-file-node? css-nodes)]
    [(stringify (assoc tree :nodes css-nodes))
     (->> export-nodes (map tree->map) (into {}))]))

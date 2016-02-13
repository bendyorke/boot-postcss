(import '[javax.script ScriptEngineManager])

(def nashorn (.getEngineByName (ScriptEngineManager.) "nashorn"))

(deftask postcss
  "Task to create postcss"
  []
  (println (.eval nashorn "5 + 5")))

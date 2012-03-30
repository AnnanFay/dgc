(defproject dgc "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [seesaw "1.4.0"]
                 [cheshire "2.0.4"]
                 [clojure-protobuf "0.4.7-SNAPSHOT"]
                 [incanter "1.2.3-SNAPSHOT"]
                 ;[me.hspy/defrecord2 "1.0.0-SNAPSHOT"]
                 ;[org.jfree/jfreechart "1.0.14"]
                 ]
	:dev-dependencies [[protobuf "0.6.0-beta8"]]
  :main dgc.core
  :java-source-path "src/java")

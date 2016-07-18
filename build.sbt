name := "test-scala2"

version := "1.0"

scalaVersion := "2.11.8"

mainClass in(Compile, packageBin) := Some("come.larscode.testscala2.Main")

//resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.7",
  "org.reactivemongo" %% "reactivemongo" % "0.11.14",
  "com.wix" %% "accord-core" % "0.5"
)
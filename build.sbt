name := "test-mockito"

version := "0.1"

scalaVersion := "2.12.8"

allDependencies ++= Seq(
  "io.circe" % "circe-core_2.12" % "0.11.1",
  "io.circe" % "circe-generic_2.12" % "0.11.1",
  "io.circe" % "circe-parser_2.12" % "0.11.1",
  "io.circe" % "circe-java8_2.12" % "0.11.1",
  "org.mockito" %% "mockito-scala" % "1.3.1",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)
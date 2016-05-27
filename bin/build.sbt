name := "Pdf-Service"

version := "1.0-SNAPSHOT"

sbtPlugin := true

lazy val web = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  cache,
  javaWs,
  filters
)


name := "Pdf-Service"

version := "1.0-SNAPSHOT"

sbtPlugin := true

lazy val javaDemo = project.in( file(".") ).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  cache,
  javaWs,
  filters,
  "org.apache.commons" % "commons-io" % "1.3.2"
)


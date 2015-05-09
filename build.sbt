name := """scalendar"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
//  jdbc,
//  anorm,
//  cache,
//  ws,
  "com.h2database" % "h2" % "1.3.168",
  "org.sorm-framework" % "sorm" % "0.3.18",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value

fork in run := true
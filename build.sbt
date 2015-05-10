name := """scalendar"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value

fork in run := true
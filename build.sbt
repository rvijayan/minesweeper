name := "minesweeper"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6.5" % Test,
  "org.scalacheck" %% "scalacheck" % "1.12.0" % Test,
  "org.specs2"  %% "specs2-scalacheck"  % "3.6" % Test,
  "org.specs2"  %% "specs2-matcher-extra" % "3.6" % Test,
  "org.specs2"  %% "specs2-mock" % "3.6" % Test,
  "org.specs2"%% "specs2-junit"  % "3.6" % Test)
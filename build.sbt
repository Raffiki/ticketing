
scalaVersion := "2.12.7"

import Dependencies._

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"

scalacOptions in ThisBuild ++= Seq(
    "-Xlog-implicits"
)
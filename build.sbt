import scalariform.formatter.preferences._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.ishikavvavvavva"

lazy val root = (project in file("."))
  .settings(
    name := "GI2D",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.8"
    )
  )

scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DanglingCloseParenthesis, Preserve)

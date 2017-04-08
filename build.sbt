name := "my-scala-demos"

version := "1.0"

val commonSettings = Seq(
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-language:experimental.macros"),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
    //, "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
    //, "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
    , "joda-time" % "joda-time" % "2.9.4"
  ),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

/**
  * root
  */
lazy val root = (project in file(".")).aggregate(
  helloWorld
  //metaprogramming_macros, metaprogramming_demo
)

/**
  * Scala Hello World
  */
lazy val helloWorld = (project in file("practice/helloworld")).settings(commonSettings: _*)

/**
  * Scala Macros － 元编程 Metaprogramming with Def Macros
  * http://www.cnblogs.com/tiger-xc/p/6112143.html
  */
lazy val metaprogramming_macros = (project in file("practice/metaprogramming/macros")).settings(commonSettings: _*)

lazy val metaprogramming_demo = (project in file("practice/metaprogramming/demo")).settings(commonSettings: _*).dependsOn(metaprogramming_macros)





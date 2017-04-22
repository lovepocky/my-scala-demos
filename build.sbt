name := "my-scala-demos"

version := "1.0"

val commonSettings = Seq(
  scalaVersion := "2.11.11",
  scalacOptions ++= Seq("-language:experimental.macros"),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
    //, "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
    , "org.scalatest" % "scalatest_2.11" % "3.0.1" % "test"
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
  * Practice of scalameta
  * http://scalameta.org/
  */
scalaVersion in ThisBuild := "2.11.11"

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  // New-style macro annotations are under active development.  As a result, in
  // this build we'll be referring to snapshot versions of both scala.meta and
  // macro paradise.
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.bintrayRepo("scalameta", "maven"),
  // A dependency on macro paradise 3.x is required to both write and expand
  // new-style macros.  This is similar to how it works for old-style macro
  // annotations and a dependency on macro paradise 2.x.
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M8" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  // temporary workaround for https://github.com/scalameta/paradise/issues/10
  scalacOptions in (Compile, console) := Seq(), // macroparadise plugin doesn't work in repl yet.
  // temporary workaround for https://github.com/scalameta/paradise/issues/55
  sources in (Compile, doc) := Nil // macroparadise doesn't work with scaladoc yet.
)


// Define macros in this project.
lazy val scalameta_macros = (project in file("practice/scalameta/macros")).settings(
  metaMacroSettings,
  // A dependency on scala.meta is required to write new-style macros, but not
  // to expand such macros.  This is similar to how it works for old-style
  // macros and a dependency on scala.reflect.
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.7.0"
)

// Use macros in this project.
lazy val scalameta_app = (project in file("practice/scalameta/app")).settings(metaMacroSettings).dependsOn(scalameta_macros)


/**
  * Scala Macros － 元编程 Metaprogramming with Def Macros
  * http://www.cnblogs.com/tiger-xc/p/6112143.html
  */
lazy val metaprogramming_macros = (project in file("practice/metaprogramming/macros")).settings(commonSettings: _*)

lazy val metaprogramming_demo = (project in file("practice/metaprogramming/demo")).settings(commonSettings: _*).dependsOn(metaprogramming_macros)





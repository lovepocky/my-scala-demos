package examples

import practice.Writer

import scala.meta._

@main
object MyApp {
  println("Hello Scala.meta macros!")

  "val x = 2".tokenize.get
}


@Writer case class hello(name: String, age: Int)
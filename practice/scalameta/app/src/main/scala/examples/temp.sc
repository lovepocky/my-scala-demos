import scala.meta._


/**
  * Tokens
  */
val tokens = "val x = 1".tokenize.get

tokens.syntax

tokens
  .map(x => f"${x.structure}%10s -> ${x.productPrefix}").
  mkString("\n")


"kas foobar".tokenize.get(3).structure
"foobar kas".tokenize.get(1).structure
"kas foobar".tokenize.get(3).structure == "foobar kas".tokenize.get(1).structure


/**
  * Trees
  */

q"case class User(name: String, age: Int)"

val method = q"def `is a baby` = age < 1"

q"""
case class User(name: String, age: Int) {
  $method
}
"""

method match {
  case q"def $name = $body" =>
    s"You ${name.syntax} if your ${body.syntax}"
}


"object Main extends App { println(1) }".parse[Source].get


"foo(bar)".parse[Stat].get.structure

q"val     x = 1".structure

"val     x = 1".tokenize.get
  .map(x => f"${x.structure}%10s -> ${x.productPrefix}").
  mkString("\n")

q"val     x = 1" match {
  case q"val $name = 1"=> "matched"
  case _ => "not matched"
}
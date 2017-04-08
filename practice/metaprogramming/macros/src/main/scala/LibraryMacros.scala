//import scala.reflect.macros.whitebox.Context
import org.joda.time.DateTime

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

/**
  * Created by lovepocky on 17/4/6.
  */
object LibraryMacros {
  def greeting(person: String): Unit = macro greetingMacro

  def greetingMacro(c: Context)(person: c.Expr[String]): c.Expr[Unit] = {
    import c.universe._
    println("compiling greeting ...")
    val now = reify(DateTime.now().toString())
    reify {
      println("Hello " + person.splice + ", the time is: " + DateTime.now().toString)
    }
  }

  def tell(person: String): Unit = macro MacrosImpls.tellMacro

  class MacrosImpls(val c: Context) {

    import c.universe._

    def tellMacro(person: c.Tree): c.Tree = {
      val now = DateTime.now().toString
      println(s"compiling tell ... | now: $now")
      q"""
                println("Hello "+$person+", it is: "+$now)
             """
    }
  }

}

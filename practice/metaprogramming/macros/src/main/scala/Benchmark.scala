/**
  * Created by lovepocky on 17/4/8.
  */
import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

class Benchmark extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro Benchmark.impl
}
object Benchmark {
  def impl(c: Context)(annottees: c.Tree*): c.Tree = {
    import c.universe._

    annottees.head match {
      case q"$mods def $mname[..$tpes](...$args): $rettpe = { ..$stats }" => {
        q"""
            $mods def $mname[..$tpes](...$args): $rettpe = {
               val start = System.nanoTime()
               val result = {..$stats}
               val end = System.nanoTime()
               println(${mname.toString} + " elapsed time in nano second = " + (end-start).toString())
               result
            }
          """
      }
      case _ => c.abort(c.enclosingPosition, "Incorrect method signature!")
    }

  }
}

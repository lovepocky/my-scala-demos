package practice

import scala.meta._

/**
  * Created by lovepocky on 17/4/22.
  */
class Writer extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"case class $name(..$params)" =>
        println(name.syntax)
        params.map(_.syntax).foreach(println)
        q"""
           case class $name(..$params)
           object ${Term.Name(name.syntax)} {}
          """
      case _ => abort("Macro Writer abort")
    }
  }
}

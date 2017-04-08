/**
  * Created by lovepocky on 17/4/7.
  */

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

trait CaseClassMapConverter[C] {
  def toMap(c: C): Map[String, Any]

  def fromMap(m: Map[String, Any]): C
}

object CaseClassMapConverter {
  implicit def Materializer[C]: CaseClassMapConverter[C] = macro converterMacro[C]

  def converterMacro[C: c.WeakTypeTag](c: Context): c.Tree = {
    import c.universe._

    val tpe = weakTypeOf[C]
    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

    val companion = tpe.typeSymbol.companion
    val (toParams, fromParams) = fields.map { field =>
      val name = field.name.toTermName
      val decoded = name.decodedName.toString
      val rtype = tpe.decl(name).typeSignature

      (q"$decoded -> t.$name", q"map($decoded).asInstanceOf[$rtype]")

    }.unzip

    //println(s"tpe: $tpe")
    //println(s"fields: $fields")
    //println(s"toParams: $toParams")
    //println(s"fromParams: $fromParams")

    q"""
     new CaseClassMapConverter[$tpe] {
      def toMap(t: $tpe): Map[String,Any] = Map(..$toParams)
      def fromMap(map: Map[String,Any]): $tpe = $companion(..$fromParams)
     }
    """
  }
}
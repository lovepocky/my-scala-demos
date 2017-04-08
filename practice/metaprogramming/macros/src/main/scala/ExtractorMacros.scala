import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

/**
  * Created by lovepocky on 17/4/7.
  */
trait User {
  val fname: String
  val lname: String
}

class FreeUser(val fname: String, val lname: String) extends User {
  val i = 10

  def f = 1 + 2
}

class PremiumUser(val name: String, val gender: Char, val vipnum: String)

//extends User

object ExtractorMicros {

  implicit class UserInterpolate(sc: StringContext) {

    object usr {
      def apply(args: String*): Any = macro UserMacros.appl

      def unapply(u: User): Any = macro UserMacros.uapl
    }

  }

}

object UserMacros {

  def appl(c: Context)(args: c.Tree*) = {
    println("UserMacros appl start")
    println(s"UserMacros appl args: ${args}")
    import c.universe._

    val arglist = args.toList
    q"new FreeUser(..$arglist)"
  }

  def uapl(c: Context)(u: c.Tree) = {
    println("UserMacros uapl start")
    import c.universe._
    println(s"UserMacros uapl u.tpe: ${u.tpe}")
    println(s"UserMacros uapl u.isType: ${u.isType}")
    println(s"UserMacros uapl u.isTerm: ${u.isTerm}")
    println(s"UserMacros uapl u.isDef: ${u.isDef}")
    println(s"UserMacros uapl u.isEmpty: ${u.isEmpty}")
    println(s"UserMacros uapl u.tpe.baseClasses: ${u.tpe.baseClasses}")
    println(s"UserMacros uapl u.tpe.decls: ${u.tpe.decls}")
    //println(s"UserMacros uapl u.tpe.members: ${u.tpe.members}")
    println(s"UserMacros uapl u: ${u.tpe.members.collect { case m: MethodSymbol if m.isConstructor => m.asMethod }} ")

    val params = u.tpe.members.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m.asMethod
    }.get.paramLists.head.map { p => p.asTerm.name.toString }

    val (qget, qdef) = params.length match {
      case len if len == 0 =>
        (List(q""), List(q""))
      case len if len == 1 =>
        val pn = TermName(params.head)
        (List(q"def get = u.$pn"), List(q""))
      case _ =>
        val defs = List(q"def _1 = x", q"def _2 = x", q"def _3 = x", q"def _4 = x")
        val qdefs = (params zip defs).collect {
          case (p, d) =>
            val q"def $mname = $mbody" = d
            val pn = TermName(p)
            q"def $mname = u.$pn"
        }
        (List(q"def get = this"), qdefs)
    }

    q"""
        new {
          class Matcher(u: User) {
            def isEmpty = false
            ..$qget
            ..$qdef
          }
          def unapply(u: User) = new Matcher(u)
        }.unapply($u)
      """
  }
}

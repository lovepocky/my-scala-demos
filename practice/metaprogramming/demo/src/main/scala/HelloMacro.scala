/**
  * Created by lovepocky on 17/4/6.
  */
object HelloMacro extends App {

  import LibraryMacros._

  /**
    * example 1
    */
  greeting("john")
  tell("mary")
  Thread.sleep(500)
  tell("mary1")
  tell("mary2")

  /**
    * example 2: convert case class <--> Map
    */
  def ccToMap[C: CaseClassMapConverter](c: C): Map[String, Any] = implicitly[CaseClassMapConverter[C]].toMap(c)

  case class Person(name: String, age: Int)

  case class Car(make: String, year: Int, manu: String)

  val civic = Car("Civic", 2016, "Honda")
  println(ccToMap[Person](Person("john", 18)))
  println(ccToMap[Car](civic))

  def mapTocc[C: CaseClassMapConverter](m: Map[String, Any]) = implicitly[CaseClassMapConverter[C]].fromMap(m)

  val mapJohn = ccToMap[Person](Person("john", 18))
  val mapCivic = ccToMap[Car](civic)
  println(mapTocc[Person](mapJohn))
  println(mapTocc[Car](mapCivic))


  /**
    * example 3: Extractor Macros
    */

  import ExtractorMicros._

  val fname = "William"
  val lname = "Wang"
  val someuser = usr"$fname,$lname" //new FreeUser("William","Wang")

  println(someuser)

  //todo bug
  /*someuser match {
    case usr"$first,$last" => println(s"hello $first $last")
  }*/

  /**
    * example 4: Macro Annotation
    */

  @Benchmark
  def testMethod[T]: Double = {
    //val start = System.nanoTime()

    val x = 2.0 + 2.0
    Math.pow(x, x)

    //val end = System.nanoTime()
    //println(s"elapsed time is: ${end - start}")
  }
  @Benchmark
  def testMethodWithArgs(x: Double, y: Double) = {
    val z = x + y
    Math.pow(z,z)
  }

  println(testMethod[String])
  println(testMethodWithArgs(2.0, 3.0))

}

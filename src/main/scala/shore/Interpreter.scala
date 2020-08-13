package shore

object Interpreter {

  class DynamicClassLoader extends ClassLoader {
    def defineClass(name: String, bytes: Array[Byte]): Class[_] = {
      defineClass(name, bytes, 0, bytes.length)
    }
  }

  def run(): Unit = {
    var fa = 3
    try {
      fa = 5
    } catch {
      case e: Exception => {}
    }
  }

//  def main(args: Array[String]): Unit = {
//    run()
//
//    val bytes = Codegen.generateClass()
//    val loader = new DynamicClassLoader
//    val clazz = loader.defineClass("Main", bytes)
//
//    try {
//      val method = clazz.getMethod("main", classOf[Array[String]])
//      method.invoke(null, Array[String]())
//    } catch {
//      case _: NoSuchMethodException =>
//        System.err.println("Could not find the main function of the program.")
//        sys.exit(1)
//    }
//  }

  def main(args: Array[String]): Unit = {
    val input = "(plus (plus 1 2) (times 3 4))"
    val tokens = Lexer.tokenize(input)
    val expr = Parser.parse(tokens.get)
    println(expr)
  }

}

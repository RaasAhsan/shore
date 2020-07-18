package shore

import shore.codegen.ClassGenerator

object Runner {

  class DynamicClassLoader extends ClassLoader {
    def defineClass(name: String, bytes: Array[Byte]): Class[_] = {
      defineClass(name, bytes, 0, bytes.length)
    }
  }

  def main(args: Array[String]): Unit = {
    val bytes = ClassGenerator.generateClass()
    val loader = new DynamicClassLoader
    val clazz = loader.defineClass("Main", bytes)

    try {
      val method = clazz.getMethod("main", classOf[Array[String]])
      method.invoke(null, Array[String]())
    } catch {
      case _: NoSuchMethodException =>
        System.err.println("Could not find the main function of the program.")
        sys.exit(1)
    }
  }

}

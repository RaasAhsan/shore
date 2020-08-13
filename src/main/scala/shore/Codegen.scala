package shore

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.{ACC_PUBLIC, ACC_STATIC, GETSTATIC, INVOKEVIRTUAL, RETURN, V1_8}

object Codegen {

  def generateClass(): Array[Byte] = {
    val cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)
    cw.visit(V1_8, ACC_PUBLIC, "Main", null, "java/lang/Object", null)

    val mv = cw.visitMethod(ACC_STATIC | ACC_PUBLIC, "main", "([Ljava/lang/String;)V", null, null)
    mv.visitCode()
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
    mv.visitLdcInsn("HELLO WORLD!")
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
    mv.visitInsn(RETURN)
    mv.visitMaxs(0, 0)
    mv.visitEnd()

    cw.visitEnd()

    cw.toByteArray
  }

}

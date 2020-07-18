package shore.ast

final case class CompilationUnit(methods: List[MethodDeclaration])

// A method definition is a sequence of statements followed by an expression
// Expression block?

final case class MethodDeclaration(name: String, argTypes: List[Type], returnType: Type, body: Expression)

sealed trait Statement

object Statement {
  final case class ExpressionStatement(term: Expression) extends Statement
  final case class AssignmentStatement(name: String, ty: Type, expression: Expression) extends Statement
}

sealed trait Expression

object Expression {
  // The left-hand side should really be any expression, the type checker can
  // assert that it really is a function
  final case class InvokeFunctionExpression(name: String, args: Expression*) extends Expression
  final case class BlockExpression(statements: List[Statement], expression: Expression) extends Expression
  final case class IntegerExpression(value: Int) extends Expression
  final case class BooleanExpression(value: Boolean) extends Expression
  final case class IfExpression(cond: Expression, b1: Expression, b2: Expression)
}

sealed trait Type

object Type {
  case object Integer extends Type
  case object Boolean extends Type
}

package shore

sealed trait Expr

object Expr {
  final case class Number(value: Int) extends Expr

  final case class Name(value: String) extends Expr

  final case class Combination(operator: Expr, operands: List[Expr])
}

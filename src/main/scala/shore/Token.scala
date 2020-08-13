package shore

sealed abstract class Token

object Token {
  case object LeftParentheses extends Token

  case object RightParentheses extends Token

  final case class Number(value: Int) extends Token

  final case class Name(value: String) extends Token
}

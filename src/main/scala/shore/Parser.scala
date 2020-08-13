package shore

object Parser {

  def parse(tokens: List[Token]): Option[Expr] =
    parseExpr(tokens) match {
      case Success(tokens, expr) if tokens.isEmpty => Some(expr)
      case _ => None
    }

  private def parseExpr(tokens: List[Token]): ParseResult[Expr] =
    tokens.headOption match {
      case Some(head) =>
        head match {
          case Token.Number(_) => parseNumber(tokens)
          case Token.Name(_) => parseIdentifier(tokens)
          case Token.LeftParentheses => parseCombination(tokens)
          case _ => Failure
        }
      case None => Failure
    }

  private def parseExprs(tokens: List[Token]): ParseResult[List[Expr]] = {
    def go(tokens: List[Token], acc: List[Expr]): ParseResult[List[Expr]] =
      parseExpr(tokens) match {
        case Success(next, expr) => go(next, acc :+ expr)
        case Failure => Success(tokens, acc)
      }

    go(tokens, Nil)
  }

  def parseNumber(tokens: List[Token]): ParseResult[Expr] =
    parseToken(tokens) {
      case Token.Number(value) => Expr.Number(value)
    }

  def parseIdentifier(tokens: List[Token]): ParseResult[Expr] =
    parseToken(tokens) {
      case Token.Name(value) => Expr.Name(value)
    }

  def parseLeftParen(tokens: List[Token]): ParseResult[Unit] =
    parseToken(tokens) {
      case Token.LeftParentheses => ()
    }

  def parseRightParen(tokens: List[Token]): ParseResult[Unit] =
    parseToken(tokens) {
      case Token.RightParentheses => ()
    }

  def parseCombination(tokens: List[Token]): ParseResult[Expr] =
    parseLeftParen(tokens) match {
      case Success(ts2, _) =>
        parseExpr(ts2) match {
          case Success(ts2, operator) =>
            parseExprs(ts2) match {
              case Success(ts3, operands) =>
                parseRightParen(ts3) match {
                  case Success(ts3, _) => Success(ts3, Expr.Combination(operator, operands))
                  case Failure => Failure
                }
              case Failure => Failure
            }
          case Failure => Failure
        }
      case Failure => Failure
    }

  private def parseToken[A](tokens: List[Token])(f: PartialFunction[Token, A]): ParseResult[A] =
    tokens match {
      case t :: ts =>
        f.unapply(t) match {
          case Some(value) => Success(ts, value)
          case None => Failure
        }
      case _ =>  Failure
    }

}

// we can define a Parser monad or applicative
// it would also reduce a lot of nesting and boilerplate in the parse functions

sealed abstract class ParseResult[+A]

final case class Success[+A](tokens: List[Token], value: A) extends ParseResult[A]

case object Failure extends ParseResult[Nothing]

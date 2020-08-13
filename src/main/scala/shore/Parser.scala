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
    tokens match {
      case t :: ts =>
        t match {
          case Token.Number(value) => Success(ts, Expr.Number(value))
          case _ => Failure
        }
      case _ =>  Failure
    }

  def parseIdentifier(tokens: List[Token]): ParseResult[Expr] =
    tokens match {
      case t :: ts =>
        t match {
          case Token.Name(value) => Success(ts, Expr.Name(value))
          case _ => Failure
        }
      case _ =>  Failure
    }

  def parseCombination(tokens: List[Token]): ParseResult[Expr] =
    tokens match {
      case t :: ts if t == Token.LeftParentheses =>
        parseExpr(ts) match {
          case Success(ts2, operator) =>
            parseExprs(ts2) match {
              case Success(ts3, operands) =>
                ts3 match {
                  case y :: ys if y == Token.RightParentheses => Success(ys, Expr.Combination(operator, operands))
                  case _ => Failure
                }
              case Failure => Failure
            }
          case Failure => Failure
        }
      case _ => Failure
    }

}

// we can define a Parser monad or applicative
// it would also reduce a lot of nesting and boilerplate in the parse functions

sealed abstract class ParseResult[+A]

final case class Success[+A](tokens: List[Token], value: A) extends ParseResult[A]

case object Failure extends ParseResult[Nothing]

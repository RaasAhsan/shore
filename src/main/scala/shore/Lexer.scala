package shore

import scala.collection.mutable

object Lexer {

  val numberRegex = "^\\d+".r.unanchored
  val identifierRegex = "^[A-Za-z]+".r.unanchored
  val whitespaceRegex = "^\\s+".r.unanchored

  // Handwritten lexer
  def tokenize(input: String): Option[List[Token]] = {
    val tokens = mutable.Buffer[Token]()

    var current = input
    while (!current.isEmpty) {
//      println(current)
      if (current.startsWith("(")) {
        tokens.append(Token.LeftParentheses)
        current = current.substring(1)
      } else if (current.startsWith(")")) {
        tokens.append(Token.RightParentheses)
        current = current.substring(1)
      } else if (numberRegex.matches(current))  {
        val matched = numberRegex.findFirstIn(current).get
        tokens.append(Token.Number(matched.toInt))
        current = current.substring(matched.length)
      } else if (identifierRegex.matches(current))  {
        val matched = identifierRegex.findFirstIn(current).get
        tokens.append(Token.Name(matched))
        current = current.substring(matched.length)
      } else if (whitespaceRegex.matches(current))  {
        current = whitespaceRegex.replaceFirstIn(current, "")
      } else {
        return None
      }
    }

    Some(tokens.toList)
  }

}

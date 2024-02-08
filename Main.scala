import scala.io.Source
import java.io.File

object Tokenization {

  sealed trait Token

  case class Identifier(name: String) extends Token
  case class Number(value: Int) extends Token
  case class ProteusCodeBlock(code: String) extends Token
  case object HashHash extends Token

  def tokenize(input: String): List[Token] = {
    val proteusBlockStartEnd = "##"

    @scala.annotation.tailrec
    def tokenizeRec(input: List[Char], acc: List[Token], currentToken: StringBuilder, proteusBlockContent: StringBuilder, isProteusBlock: Boolean): List[Token] = {
      input match {
        case Nil =>
          if (currentToken.nonEmpty) {
            acc :+ (if (currentToken.forall(_.isDigit)) 
            Number(currentToken.toString.toInt) 
            else
              Identifier(currentToken.toString))
          } else {
            acc
          }
          // Check if Input is empty list
          // if so, return accum
          // else, pop the first charecter
          // match against either the #, \n something else
          // If matched against hash tag, add to # accum, keep going
          // If matched against \n, reset Hashtag accum
          // If something else, then check the context and apend to appropriate accum
        case '#' :: '#' :: tail if !isProteusBlock =>
          tokenizeRec(tail, acc :+ ProteusCodeBlock(proteusBlockContent.toString), new StringBuilder, new StringBuilder, isProteusBlock = true)
        case '#' :: '#' :: tail if isProteusBlock =>
          tokenizeRec(tail, acc :+ ProteusCodeBlock(proteusBlockContent.toString), new StringBuilder, new StringBuilder, isProteusBlock = false)
        case '#' :: '#' :: tail =>
          tokenizeRec(tail, acc, currentToken.append(proteusBlockStartEnd), proteusBlockContent, isProteusBlock = true)
        case c :: tail =>
          tokenizeRec(tail, acc, currentToken.append(c), proteusBlockContent.append(c), isProteusBlock)
      }
    }

    tokenizeRec(input.toList, List.empty, new StringBuilder, new StringBuilder, isProteusBlock = false)
  }
}

object TokenizationExample {

  def main(args: Array[String]): Unit = {
    val filePath = "testFPPP.txt"
    val fileContent = Source.fromFile(new File(filePath)).mkString
    val tokens = Tokenization.tokenize(fileContent)
    println(s"Tokens: $tokens")
  }
}

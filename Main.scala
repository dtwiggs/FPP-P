import java.io.File
import scala.io.Source

object Tokenization {

  // Define a sealed trait, Token, which allows for exhaustive pattern matching
  sealed trait Token

  // Define case classes for each subtype of Token, allows for pattern matching on this immutable data
  case class ProteusCodeBlock(code: String) extends Token

  // Function to tokenize the input string, including a helper function that does the recursive tokenizing
  def tokenize(input: String): List[Token] = {

    // Defined a tail-recursive helper function to tokenize the input string, will be called automatically
    @scala.annotation.tailrec
    def tokenizeRec(input: List[Char], acc: List[Token], proteusBlockContent: StringBuilder, isProteusBlock: Boolean): List[Token] = {
      input match {
        // If there is nothing left in the input file, return the acc list of Tokens
        case Nil =>
          acc

        // If '##' is encountered and not inside a Proteus block, set the isProteusBlock to True
        case '#' :: '#' :: tail if !isProteusBlock =>
          tokenizeRec(tail, acc, new StringBuilder, isProteusBlock = true)

        // If '##' is encountered and inside a Proteus block, add a ProteusCodeBlock Token to the accumulator
        case '#' :: '#' :: tail if isProteusBlock =>
          tokenizeRec(tail, acc :+ ProteusCodeBlock(proteusBlockContent.toString), new StringBuilder, isProteusBlock = false)

        // If '#' is encountered, ignore the # and tokenize the rest
        case '#' :: tail =>
          tokenizeRec(tail, acc, proteusBlockContent, isProteusBlock)

        // If inside a Proteus block, append the character to the Proteus block content
        case c :: tail =>
          if (isProteusBlock) {
            proteusBlockContent.append(c)
          }
          tokenizeRec(tail, acc, proteusBlockContent, isProteusBlock)
      }
    }

    // Start the tokenization process with an empty accumulator and StringBuilder for current token and Proteus block content
    tokenizeRec(input.toList, List.empty, new StringBuilder, isProteusBlock = false)
  }
}

object TokenizationExample {

  def main(args: Array[String]): Unit = {

    // Define the path to the file
    val filePath = "testFPPP.txt"

    // Read the content of the file
    val fileContent = Source.fromFile(new File(filePath)).mkString

    // Tokenize the file content
    val tokens = Tokenization.tokenize(fileContent)
    
    // Print each token on a new line (println(s"$tokens") works but looks worse)
    tokens.foreach(token => println(token))
  }
}

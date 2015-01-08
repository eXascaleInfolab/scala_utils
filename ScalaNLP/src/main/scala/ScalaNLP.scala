import java.util.regex.Pattern

import edu.stanford.nlp.ie.AbstractSequenceClassifier
import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreLabel

import scala.collection.JavaConverters._

/**
 * Created by alberto on 30/07/14.
 */
object ScalaNLP {
	//TODO: test
	private val stopword_path_all_languages = "../resources/stopwords"
	lazy val stopwords: Set[String] = (for (word <- io.Source.fromURL(getClass.getResource("/stopwords/english")).getLines()) yield word).toSet
	private lazy val classifier = CRFClassifier.getClassifier("edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz")

	type StringOffsetNotation = List[(Int, Int, String)]


	def classifyToCharacterOffsets(sentence: String) = {
		classifier.classifyToCharacterOffsets(sentence).asScala.map(t => (t.first(), t.second(), t.third()))
	} //classifyToCharacterOffsets


	def removeStopWords(sentence: String, wordSeparator: String = " "): String = {
		return (for (word <- sentence.replaceAll(wordSeparator + wordSeparator + "+", wordSeparator).split(wordSeparator)
		             if !(stopwords contains word)) yield word) mkString (wordSeparator)
	} //removeStopWords


	def getStringOutOf(offsets: StringOffsetNotation, wordSeparator: String = " ") = offsets.filter(_._3 != "").map(_._3).mkString(wordSeparator)


	def porterStemSentence(sentence: String, wordSeparator: String = " "): String =
		(for (word <- sentence.replaceAll(wordSeparator + wordSeparator + "+", wordSeparator).split(wordSeparator))
		yield PorterStemmer.stemWord(word)) mkString (wordSeparator)


	def split_offsets(sentence: String, wordSeparator: String = " "): StringOffsetNotation = {
		val processed_sentence = sentence.replaceAll(wordSeparator + wordSeparator + "+", wordSeparator)
		val lstSentence_offsets_tmp = Pattern.quote(wordSeparator).r.findAllMatchIn(sentence).foldLeft((List[(Int, Int, String)](), 0))(
			(acc, m) => acc match {
				case (lst, startOffset) => ((startOffset, m.start, sentence.substring(startOffset, m.start)) :: lst, m.end)
			}
		)
		((lstSentence_offsets_tmp._2, sentence.length, sentence.substring(lstSentence_offsets_tmp._2, sentence.length)) :: lstSentence_offsets_tmp._1).reverse
	}

	def removeStopWords_offsets(sentence: StringOffsetNotation): StringOffsetNotation =
		for ((b, e, str) <- sentence) yield (b, e, if (stopwords contains str) "" else str)

	def porterStemSentence_offsets(offsets: StringOffsetNotation): StringOffsetNotation =
		for {(b, e, str) <- offsets} yield (b, e, PorterStemmer.stemWord(str))

	def ngrams_offsets(offsets:  StringOffsetNotation, n: Int) = {
		offsets.filter( _._3 != "").sliding(n).toList
	} //ngrams

	def ngrams(s: String, n: Int, wordSeparator: String = " ") = {
		val words = s.replaceAll(s"$wordSeparator$wordSeparator+", wordSeparator).split(wordSeparator)
		words.sliding(n).toList
	} //ngrams

}//ScalaNLP

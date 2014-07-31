import java.io.StringReader

import edu.stanford.nlp.ie.AbstractSequenceClassifier
import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreLabel
import org.apache.lucene.analysis.Tokenizer
import org.apache.lucene.analysis.core.LowerCaseTokenizer
import org.apache.lucene.analysis.en.PorterStemFilter
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.tokenattributes.{CharTermAttribute, OffsetAttribute}
import scala.collection.JavaConverters._

/**
 * Created by alberto on 30/07/14.
 */
object ScalaNLP {
	//TODO: substitute this with stopwords from lucene
	private val stopword_path_all_languages = "/Users/alberto/nltk_data/corpora/stopwords"
	private var stopwords: Set[String] = null
	private var classifier: AbstractSequenceClassifier[CoreLabel] = null


	def classifyToCharacterOffsets(sentence: String) = {
		if(classifier  == null) classifier = CRFClassifier.getClassifier("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger")
		classifier.classifyToCharacterOffsets(sentence).asScala.map(t => (t.first(), t.second(), t.third()))
	}//classifyToCharacterOffsets


	def removeStopWords(sentence: String, wordSeparator: String = " "): String = {
		if(stopwords == null)
			stopwords = (for(word <- io.Source.fromFile(stopword_path_all_languages + "/english").getLines()) yield word).toSet

		return (for( word <- sentence.replaceAll(wordSeparator + wordSeparator + "+", wordSeparator).split(wordSeparator)
		if !(stopwords contains(word)) ) yield word) mkString(wordSeparator)
	}//removeStopWords


	def porterStemSentence(sentence: String, wordSeparator: String = " "): String = {
		val source: Tokenizer = new LowerCaseTokenizer(Version.LUCENE_4_9, new StringReader(sentence))

		val stemmer = new PorterStemFilter(source)
		val offsetAttribute = stemmer.addAttribute(classOf[OffsetAttribute])
		val charTermAttribute = stemmer.addAttribute(classOf[CharTermAttribute])

		stemmer.reset()
		val s: StringBuilder = new StringBuilder(sentence.length)
		while (stemmer.incrementToken()) {
//			val startOffset = offsetAttribute.startOffset() these can be used as additional info to output
//			val endOffset = offsetAttribute.endOffset()
			val term = charTermAttribute.toString()
			s.append(term + wordSeparator)
		}
		stemmer.close()
		return s toString
	}//porterStemSentence


	def ngrams(s: String, n: Int, wordSeparator: String = " ") = {
		val words = s.replaceAll(s"$wordSeparator$wordSeparator+", wordSeparator).split(wordSeparator)
		words.sliding(n)
	}//ngrams


}//ScalaNLP

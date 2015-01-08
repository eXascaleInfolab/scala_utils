package scalaRdf3x

import java.sql.{ResultSet, SQLException}
import java.util.Properties

import de.mpii.rdf3x.{Connection, Driver, Statement}

/**
 * Created by alberto on 7/01/15.
 * Wraps the original RDF3x Java apis.
 */
class RDFSearch(val rdf3xHome: String, val dbName: String) {
	val d: Driver = new Driver
	val prop: Properties = new Properties
	prop.put("rdf3xembedded", rdf3xHome + "/bin/rdf3xembedded")
	val c: Connection = d.connect("rdf3x://" + dbName, prop).asInstanceOf[Connection]
	val s = new Statement(c)


	def this(dbName: String) {
		this("./rdf3x3.7", dbName)
	}


	def close() {
		s.close()
		c.close()
	}

	def SPARQLQuery(query: String) = {
		val rs: ResultSet = s.executeQuery(query)
		val n_variables = rs.getMetaData.getColumnCount
		new Iterator[Map[String, String]] {
			override def next(): Map[String, String] = {
				(for{i <- 1 to n_variables} yield rs.getMetaData.getColumnLabel(i) -> rs.getString(i)).toMap
			}

			override def hasNext() = {
				val hasnext = rs.next()
				if(!hasnext) close()
				hasnext
			}

			private def close() {
				rs.close() }
		}//iterator
	}//SPARQLQuery
}//RDFSearch


object RDFSearch {
	def main(args: Array[String]) {
		val rdf3xhome = args(0)
		val dbPath = args(1)

		val rdfs = new RDFSearch(rdf3xhome, dbPath)
		for (row <- rdfs.SPARQLQuery("select ?name where { <http://dbpedia.org/resource/Tom_Cruise> <http://www.w3.org/2000/01/rdf-schema#label> ?name } LIMIT 20"))
			println(s"${row}")
		rdfs.close()
		println("the end.")
	}//main
}//RDFSearch
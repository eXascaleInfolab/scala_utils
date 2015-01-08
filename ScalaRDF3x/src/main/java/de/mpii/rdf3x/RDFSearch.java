//package de.mpii.rdf3x;
//
//import java.io.IOException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Properties;
//
//public class RDFSearch {
//
//	public final String rdf3xHome;
//	public final String dbName;
//
//	private Statement s;
//	private Connection c;
//
//	public RDFSearch(String dbName) throws SQLException {
//		this.dbName = dbName;
//        this.rdf3xHome = "rdf3x-0.3.7";
//		Driver d = new Driver();
//		Properties prop = new Properties();
//		prop.put("rdf3xembedded", rdf3xHome + "/bin/rdf3xembedded");
//		this.c = (Connection) d.connect(
//				"rdf3x://" + dbName, prop);
//
//		s = new Statement(c);
//	}
//
//
//    public RDFSearch(String rdf3xHome, String dbName) throws SQLException {
//        this.dbName = dbName;
//        this.rdf3xHome = rdf3xHome;
//        Driver d = new Driver();
//        Properties prop = new Properties();
//        prop.put("rdf3xembedded", rdf3xHome + "/bin/rdf3xembedded");
//        this.c = (Connection) d.connect(
//                "rdf3x://" + dbName, prop);
//
//        s = new Statement(c);
//    }
//
//	public void close() throws SQLException {
//		s.close();
//		c.close();
//	}
//
//
//	/**
//	 * @param args
//	 * @throws java.io.IOException
//	 * @throws InterruptedException
//	 * @throws java.sql.SQLException
//	 */
//	public static void main(String[] args) throws IOException,
//			InterruptedException, SQLException {
//		RDFSearch rdfs = new RDFSearch("/Users/alberto/rdf3x-0.3.7", "/Users/alberto/Documents/Projects/DBpedia_vs_Freebase/DBpedia3.9/rdf3x/dbpedia_properties.rdf3x");
//
//		ResultSet rs = rdfs
//				.SPARQLQuery("select ?name where { ?p <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?name } LIMIT 20");
//
//		System.out.println("\nquery executed. results:");
//		while (rs.next()) {
//			System.out.println(rs.getString(1));
//		}
//
//		rs = rdfs
//				.SPARQLQuery("SELECT ?y ?x WHERE { ?y <http://dbpedia.org/ontology/birthDate> ?x . ?z <http://dbpedia.org/ontology/starring> ?y . ?z <http://www.w3.org/2000/01/rdf-schema#label> 'Forrest Gump'}");
//
//		System.out.println("query executed. results:");
//		while (rs.next()) {
//			System.out.println(rs.getString(1));
//		}
//
//		rs.close();
//		rdfs.close();
//	}
//
//	public ResultSet SPARQLQuery(String query) throws SQLException {
//		ResultSet rs = s.executeQuery(query);
//
//
//	}
//
//}

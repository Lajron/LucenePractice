import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher extends Utility implements AutoCloseable {
	private Directory indexDir;
	private IndexReader indexReader;
	private IndexSearcher indexSearcher;
	private QueryParser queryParser;
	
	public Searcher(String indexDirStr) throws IOException {
		indexDirectory = indexDirStr;
		setup();
	}
	
	@Override
	public void close() throws Exception {
		indexReader.close();
		indexDir.close();
	}
	
	public void setup() throws IOException {
		indexDir = FSDirectory.open(Paths.get(indexDirectory));
		indexReader = DirectoryReader.open(indexDir);
		indexSearcher = new IndexSearcher(indexReader);
		queryParser = new QueryParser(fieldContent, this.analyzer);
	}
		
	public void parseQuerySearch(String queryString) {
		System.out.println(" ______________________________________________________________");
		System.out.println("| Using OLD parser for query: " + queryString);
		try {
			Query query = queryParser.parse(queryString);
			
			System.out.println("| Parsed query object: " + query);
			System.out.println("| Parsed query class name: " + query.getClass().getName());
			
			getDocumentsAndPrint(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void querySearch(Query query) {
		System.out.println(" ______________________________________________________________");
		System.out.println("| Using NO parser for query: / ");
		System.out.println("| Query object: " + query);
		System.out.println("| Query class name: " + query.getClass().getName());

		getDocumentsAndPrint(query);
	}
		
	public void getDocumentsAndPrint(Query query) {
		System.out.println("|--------------------------------------------------------------");
		List<Document> result = new ArrayList<Document>();			
		try {
			TopDocs hits = indexSearcher.search(query, 10);			
			StoredFields storedFields = indexSearcher.storedFields();	
			
			System.out.println("| QUERY HITS: " + hits.totalHits.value);

			for (ScoreDoc hit : hits.scoreDocs)
				result.add(storedFields.document(hit.doc));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		printResult(result);	
	}
	
	public void printResult(List<Document> documents) {
		if (documents.isEmpty()) {
			return;
		}
		
		for (Document document : documents) {
			System.out.println("|-------------");
			System.out.println("| File name: " + document.get(fieldTitle));
			System.out.println("| File path: " + document.get(fieldPath));
		}
		System.out.println("|______________________________________________________________");
	}
	
	//QUERIES
	public void booleanQuery() {
		parseQuerySearch("LeViAtHan OR WORK AND NOT KaRamazov");
		System.out.println(".....................................................................");
		TermQuery termLeviathan = new TermQuery(new Term(fieldContent, "leviathan"));
		TermQuery termWork = new TermQuery(new Term(fieldContent, "work"));
		TermQuery termKaramazov = new TermQuery(new Term(fieldContent, "karamazov"));
		
		BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
		booleanQueryBuilder.add(termLeviathan, Occur.SHOULD);
		booleanQueryBuilder.add(termWork, Occur.MUST);
		booleanQueryBuilder.add(termKaramazov, Occur.MUST_NOT);		
		querySearch(booleanQueryBuilder.build());		
	}
	
	public void termRangeQuery() {
		parseQuerySearch(fieldTitle + ":{KaRamAzov TO scarLet]");
		System.out.println(".....................................................................");
		TermRangeQuery trQuery = TermRangeQuery.newStringRange(fieldTitle, "karamazov", "scarlet", false, true);
		querySearch(trQuery);
		
		
		
	}
	
	

	
}

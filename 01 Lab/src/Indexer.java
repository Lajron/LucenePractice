import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer extends Utility {
	public Indexer() {}
	public void createIndex(String dataDirStr, String indexDirStr) {	
		IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
		config.setOpenMode(OpenMode.CREATE);
		
		dataDirectory = dataDirStr;
		indexDirectory = indexDirStr;
		
		long time = 0;
		int numberOfDocuments = 0;
		
		try (Directory indexDir = FSDirectory.open(Paths.get(indexDirectory)); IndexWriter indexWriter = new IndexWriter(indexDir, config);) {
			File dataDir = new File(dataDirectory);
			File[] files = dataDir.listFiles();
			
			long start = System.currentTimeMillis();
			for (File file : files) {
				Document document = new Document();
				String title = file.getName().replace(".txt", "").trim(); //For working with .txt files
				
				document.add(new TextField(fieldContent, new FileReader(file)));
				document.add(new TextField(fieldTitle, title, Field.Store.YES));
				document.add(new StringField(fieldPath, file.getCanonicalPath(), Field.Store.YES));
				document.add(new StringField(fieldSizeString, Long.valueOf(file.length()).toString(), Field.Store.YES));
				
				indexWriter.addDocument(document);
			}
			long end = System.currentTimeMillis();
			
			time = end - start;
			numberOfDocuments = indexWriter.getDocStats().numDocs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.printStats(time, numberOfDocuments);			
	}
}

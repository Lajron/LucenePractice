import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import java.io.File;

public class Utility {
	
	//Directories default
	public static String dataDirectory = "DataOne";
	public static String indexDirectory = "IndexOne";
	
	//Index fields
	public static String  fieldContent = "content";
	public static String fieldTitle = "title";
	public static String fieldPath = "path";
	public static String fieldSizeString = "size_string";
	public static String fieldSizeLong = "size_long";
	
	//Analyzer default
	public Analyzer analyzer = new StandardAnalyzer();
	
	public Utility() {}
	
	public void printStats(long time, int numberOfDocuments) {
		System.out.println("--------------------");
		System.out.println("| Directories: " + dataDirectory + " -> " + indexDirectory);
		System.out.println("| Indexed: " + numberOfDocuments);
		System.out.println("| Time: " + time + "ms");	
		System.out.println("| Size: " + getFolderSizeKB() + "KB");
		System.out.println("--------------------");
	}
	
	public long getFolderSizeKB() {
		File dataDir = new File(indexDirectory);
		File[] files = dataDir.listFiles();
			
		long result = 0;
		for (File file : files) {
			result += file.length();
		}
			
		return (result / 1024);
	}

	
	
}

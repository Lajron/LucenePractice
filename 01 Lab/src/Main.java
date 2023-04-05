
public class Main {

	public static void main(String[] args) {
		FileCreator fileCreator = new FileCreator();
		fileCreator.createNewFiles(100);
		
		System.out.println("==============================================================================================================================");
		System.out.println("CREATING INDEX...");
		Indexer indexer = new Indexer();
		indexer.createIndex("DataOne", "IndexOne");
		indexer.createIndex("DataTwo", "IndexTwo");
		
		System.out.println("==============================================================================================================================");
		try (Searcher searcherOne = new Searcher("IndexOne"); Searcher searcherTwo = new Searcher("IndexTwo");) {
			System.out.println("- SEARCHER ONE - ");
			searcherOne.booleanQuery();
			System.out.println("- SEARCHER TWO - ");
			searcherTwo.booleanQuery();
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			System.out.println("- SEARCHER ONE - ");
			searcherOne.termRangeQuery();
			System.out.println("- SEARCHER TWO - ");
			searcherTwo.termRangeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}

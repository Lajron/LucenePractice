import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileCreator extends Utility {
	public FileCreator() {}
	
	//Pokupljen code sa stackoverflow: https://stackoverflow.com/questions/70320848/split-a-string-into-n-roughly-equal-pieces-java
	public static List<String> divide(String s, int n) {
		List<String> result = new ArrayList<>();
	    int len = s.length() / n;
	    int rem = s.length() % n;
	    for (int i = 0, pos = 0; i < n; i++) {
	    	int end = pos + len + (rem-- > 0 ? 1: 0);
	    	result.add(s.substring(pos, end));
	    	pos = end;
	    }
	    return result;
	}
	
	public void createNewFiles(int number) {
		try {
			System.out.println("CREATING NEW FILES...");
			String path = Paths.get("").toAbsolutePath().toString();
			String data = path + "\\DataOne";
			String exported = path + "\\DataTwo";
			
			String[] _fileNames = (new File(data)).list();
			
			for(String fileName: _fileNames) {
				String folderName = fileName.replace(".txt", "").trim();
				String fileText = new String(Files.readAllBytes(Paths.get(data + "\\" + fileName)));
				
				List<String> fileChunks = divide(fileText, number);
				
				int count = 1;
				for(String chunk: fileChunks) {
					Path saveTo = Paths.get(exported + "\\" + folderName + " " + count + ".txt");
					Files.writeString(saveTo, chunk, StandardCharsets.UTF_8);
					count++;
				}
				
			}			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

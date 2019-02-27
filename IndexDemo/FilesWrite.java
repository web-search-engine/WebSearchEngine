import java.io.*;
import java.util.*;
public class FilesWrite{
	public void writeFile(Map<String, Integer> pageWords, int pageId, int fileId, String dirPath){
		String path = dirPath + "\\" + Integer.toString(fileId);
		for (String key: pageWords.keySet()){
			try {
				BufferedWriter write = new BufferedWriter(new FileWriter(path + "\\" + key + ".txt",true));
		        write.write("key " + Integer.toString(pageId) + " " + Integer.toString(pageWords.get(key)));
		        write.newLine();
		        write.close();
		        System.out.println("File " + key + " create Successfully in directory " + Integer.toString(fileId));
			}
			catch (IOException e){}
		}
	}
}
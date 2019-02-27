import java.io.*;
import java.util.*;
public class FilesFetch{
	public ArrayList<String> getFiles(String path){
		ArrayList<String> files = new ArrayList<>();
		File file = new File(path);
		File[] tmpFiles = file.listFiles();
		for (int i = 0; i < tmpFiles.length; i++){
			files.add(tmpFiles[i].toString());
		} 
		return files;
	}
}
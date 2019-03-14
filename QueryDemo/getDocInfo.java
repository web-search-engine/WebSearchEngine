package QueryDemo;

import java.io.*;
import java.util.*;
import IndexDemo.*;

public class getDocInfo{

	public Map<String, String> docLengthFetch(String path){
		Map<String, String> docLengthTable = new HashMap<>();
		FilesFetch fetch = new FilesFetch();
		ArrayList<String> filesLists = fetch.getFiles(path);

		for (int i = 0; i < filesLists.size(); i++){
			File file = new File(filesLists.get(i));
			BufferedReader reader = null; 
			int line = 1;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					String[] lines = tempString.split(" ");
					docLengthTable.put(lines[0], lines[1]);
					System.out.println("Line " + Integer.toString(line));
					line++;
			    }
			} 
			catch (IOException e){}
			finally {
				try {
		            reader.close();
		        } 
		        catch (IOException e1) {}
			} 	
		}
		return docLengthTable;
	}
}
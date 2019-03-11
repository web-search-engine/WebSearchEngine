package IndexDemo;

import java.io.*;
import java.util.*;

public class LexiconFetch{
	public ArrayList<String> getLexicon(String path){
		ArrayList<String> words = new ArrayList<>();
		File file = new File(path);
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// int line = 1;
			System.out.println("------> Starting reading");
			while ((tempString = reader.readLine()) != null){
				String word = tempString.split(" ")[1];
				words.add(word);
				// System.out.println("-----> " + word);
			}
		}
		catch (IOException e){}
		finally{
			if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
		}
		return words;
	}
}
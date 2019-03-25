package MergeDemo;

import java.io.*;
import java.util.*;
import IndexDemo.*;

public class MergeLexicon{
	static String FILE_DIRECTORY = "D:\\wse_data_new";
	static String FILE_MERGE_DIRECTORY = "D:\\wse_data_write";

	public static void main(String[] args){

		String path = "D:\\lexicon.txt";
		LexiconFetch words = new LexiconFetch();
		Set<String> lexicon = words.getLexicon(path);

		// Directory dir = new Directory();
		String dirPath = "D:\\wse_data_merge";
		// dir.makeDirectory(dirPath);

		FilesFetch fetch = new FilesFetch();
		ArrayList<String> filesLists = fetch.getFiles(FILE_MERGE_DIRECTORY);

		int k = 0;

		for (Iterator it= lexicon.iterator(); it.hasNext();){
			String word = it.next().toString();
			Map<Integer,Integer> freqTable = new HashMap<>();
			for (int i = 0; i < filesLists.size(); i++){
				System.out.println("---> Starting looking for files");
				File file = null;
				String filePath = FILE_MERGE_DIRECTORY + "\\" + Integer.toString(i) + "\\" + Integer.toString(i) + "\\" + word+".txt";
				System.out.println("file path is " + filePath);
				try {
					file = new File(filePath);
					if (file.exists()){
						BufferedReader reader = null;
						try {
							reader = new BufferedReader(new FileReader(file));
							String tempString = null;
							while ((tempString = reader.readLine()) != null){
								String[] lines = tempString.split(" ");
								int pageId =  Integer.parseInt(lines[0]);
								int freq =  Integer.parseInt(lines[1]);
								freqTable.put(pageId, freqTable.getOrDefault(pageId,0) + freq);
							}
						}
						catch (Exception e){}
						finally{
							try {
				                reader.close();
				            } 
				            catch (IOException e1) {}
						}
					}
				}
				catch (Exception e2){}
				finally{
					file = null;
				}
			}

			FilesWrite write = new FilesWrite();
			System.out.println("Map size is " + Integer.toString(freqTable.size()));
			System.out.println("word is " + word);
			System.out.println("word " + Integer.toString(k) + " Staring wrting");
			write.writeMergeFile(freqTable, dirPath, word);
			System.out.println("Finishing wrting");
			k++;
		}

	}
}
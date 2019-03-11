package MergeDemo;

import java.io.*;
import java.util.*;
import IndexDemo.*;

public class MergeLexicon{
	static String FILE_DIRECTORY = "D:\\wse_data_new";
	static String FILE_MERGE_DIRECTORY = "D:\\wse_data_write";

	public static Map<Integer, Integer> sortByValue(Map<Integer, Integer> freqTable){
		List<Map.Entry<Integer, Integer> > list = 
               new LinkedList<Map.Entry<Integer, Integer> >(freqTable.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() { 
            @Override 
            public int compare(Map.Entry<Integer, Integer> o1,  
                               Map.Entry<Integer, Integer> o2) 
            	{ 
                return o2.getValue().compareTo(o1.getValue());
            } 
        }); 

        Map<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>(); 
        for (Map.Entry<Integer, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp;
	}

	public static void main(String[] args){
		String path = FILE_DIRECTORY + "\\lexicon.txt";
		LexiconFetch words = new LexiconFetch();
		ArrayList<String> lexicon = words.getLexicon(path);

		Directory dir = new Directory();
		String dirPath = "D:\\wse_data_merge";
		dir.makeDirectory(dirPath);

		FilesFetch fetch = new FilesFetch();
		ArrayList<String> filesLists = fetch.getFiles(FILE_MERGE_DIRECTORY);
		for (int k = 0; k < 1; k++){
			System.out.println("word is " + lexicon.get(k));
			Map<Integer,Integer> freqTable = new HashMap<>();
			for (int i = 0; i < filesLists.size(); i++){
				System.out.println("---> Starting looking for files");
				File file = null;
				String filePath = FILE_MERGE_DIRECTORY + "\\" + Integer.toString(i) + "\\" + Integer.toString(i) + "\\" + lexicon.get(k)+".txt";
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
								freqTable.put(pageId, freq);
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
			Map<Integer, Integer> sortFreqTable = sortByValue(freqTable);

			FilesWrite write = new FilesWrite();
			System.out.println("Map size is " + Integer.toString(sortFreqTable.size()));
			System.out.println("Staring wrting");
			write.writeMergeFile(sortFreqTable, dirPath, lexicon.get(k));
			System.out.println("Finishing wrting");
		}

		// for (int i = 0; i < lexicon.size(); i++){
		// 	System.out.println("---> word is " + lexicon.get(i));
		// }
	}
}
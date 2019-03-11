package IndexDemo;

import java.io.*;
import java.util.*;
public class FilesWrite{

	public void writeMergeFile(Map<Integer, Integer> freqTable, String path, String word){
		for (Integer key: freqTable.keySet()){
			try{
				BufferedWriter write = new BufferedWriter(new FileWriter(path + "\\" + word + ".txt",true));
				if (freqTable.get(key) != null) {
					write.write(Integer.toString(key) + " " + Integer.toString(freqTable.get(key)));
				}
		        write.newLine();
		        write.close();
			}
			catch (IOException e){}
		}
	}

	public void writeDocTable(Map<Integer, String> pageToURL, String path, int dirId){
		// int[] keys = new int[pageToURL.size()];
		ArrayList<Integer> keys = new ArrayList<>(pageToURL.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++){
			int key = keys.get(i);
			try{
				BufferedWriter write = new BufferedWriter(new FileWriter(path + "\\" + dirId + ".txt",true));
				write.write(Integer.toString(key) + " " + pageToURL.get(key));
		        write.newLine();
		        write.close();
			}
			catch (IOException e){}
		}
	}

	public void writeFile(Map<String, Map<Integer,Integer>> pageWords, String path){
		for (String key: pageWords.keySet()){
			try {
				BufferedWriter write = new BufferedWriter(new FileWriter(path + "\\" + key + ".txt",true));
		        Map<Integer,Integer> docDic = pageWords.get(key);
		        for (Integer id:	docDic.keySet()){
		        	write.write(Integer.toString(id) + " " + Integer.toString(docDic.get(id)));
		        	write.newLine();
		        }
		        write.close();
		        // System.out.println("File " + key + " create Successfully in directory " + Integer.toString(fileId));
			}
			catch (IOException e){}
		}
	}
}
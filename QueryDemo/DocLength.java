package QueryDemo;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import IndexDemo.*;

public class DocLength{

	static String FILE_DIRECTORY = "D:\\wse_data_new";
	static String FILE_WRITE = "D:\\wse_data_docLength";
	static int pageId = 0;

	public static boolean newPageCheck(String line){
		String pageStart = "WARC/1.0";
		return line.startsWith(pageStart);
	}
	// Check the contents is needed or not
	public static String contentsStart(String line){
		String start = "Content-Length: ";
		if (line.startsWith(start)){
			String[] lines = line.split("[,.:?'/!*&^%#@;><| ]+");
			String regex = "^[0-9]+$";
			Pattern pattern = Pattern.compile(regex);
			for (int i = 0; i < lines.length; i++){
				Matcher match = pattern.matcher(lines[i]);
				boolean b = match.matches();
				if (b){
					return lines[i];
				}
			}
		}
		return "";
	} 

	public static void writePageLength(String path, Map<Integer,String> freqTable, int fileId){
		try{
			BufferedWriter write = new BufferedWriter(new FileWriter(path + "\\" + Integer.toString(fileId) + ".txt",true));
			for (Integer key: freqTable.keySet()){
				if (freqTable.get(key) != null) {
					write.write(Integer.toString(key) + " " + freqTable.get(key));
				}
		    	write.newLine();
			}
			write.close();
		}
		catch (IOException e){}
	}

	public static boolean readFile(String fileName, int fileId, Map<Integer,String> pageToURL){
		File file = new File(fileName);
		BufferedReader reader = null; 
		boolean t = false;
		int line = 1;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				if (!t) {
					if (contentsStart(tempString) != "")	{
						t = true;
						String length = contentsStart(tempString);
						pageToURL.put(pageId,tempString);
					}
				} else {
						if (newPageCheck(tempString)){
							pageId++;
							t = false;
							System.out.println("pageId is " + Integer.toString(pageId));
						}  
				}
				
				System.out.println("Line " + Integer.toString(line));
				line++;
	        }
	        return true;
		} 
		catch (IOException e){}
		finally {
			try {
                reader.close();
            } 
            catch (IOException e1) {}
		}
		return true; 	
	}

	public static void main(String[] args){
		FilesFetch fetch = new FilesFetch();
		ArrayList<String> filesLists = fetch.getFiles(FILE_DIRECTORY);

		boolean status = false;
		int dirId = 0;

		while (dirId < filesLists.size()){
			Map<Integer, String> docLengthTable = new HashMap<>();
			status = readFile(filesLists.get(dirId), dirId, docLengthTable);
			if (status){
				System.out.println("Starting write Length");
				writePageLength(FILE_WRITE, docLengthTable, dirId);
				System.out.println("Finishing write Length");
			}
			dirId++;
		}
		
	}
}
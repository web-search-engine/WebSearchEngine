package QueryDemo;

import java.io.*;
import java.util.*;
import IndexDemo.*;

public class AveLength{
	public static int getTotalLength(String fileName){
		int tmp = 0;
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String len = tempString.split(" ")[1];
				tmp += Integer.parseInt(len);
			}
			return tmp;
		}
		catch (IOException e){}
		finally {
			try {
                reader.close();
            } 
            catch (IOException e1) {}
		}
		return tmp;
	}

	public static void main(String[] args){
		String path = "D:\\wse_data_docLength";

		FilesFetch fetch = new FilesFetch();
		ArrayList<String> filesLists = fetch.getFiles(path);
		int length = 0;
		for (int i = 0; i < filesLists.size(); i++){
			length += getTotalLength(filesLists.get(i));
		}
		System.out.println("Total Length is " + Integer.toString(length));
	}
}
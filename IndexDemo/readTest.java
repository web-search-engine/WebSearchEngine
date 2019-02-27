import java.io.*;
import java.util.*;
public class readTest{

	static String FILE_WRITE_DIRECTORY = "D:\\wse_data_write";
	static String FILE_DIRECTORY = "D:\\wse_data_new";
	static Map<Integer, String> pageToURL = new HashMap<>();
	static Set<String> lexicon = new HashSet<>();

	public static ArrayList<String> getFiles(String path){
		ArrayList<String> files = new ArrayList<>();
		File file = new File(path);
		File[] tmpFiles = file.listFiles();
		for (int i = 0; i < tmpFiles.length; i++){
			files.add(tmpFiles[i].toString());
		} 
		return files;
	}

	public static boolean newPageCheck(String line){
		String pageStart = "WARC/1.0";
		return line.startsWith(pageStart);
	}
	public static boolean contentsStart(String line){
		String start = "Content-Length: ";
		return line.startsWith(start);
	} 
	public static String findURL(String line){
		String url = "WARC-Target-URI: ";
		if (line.startsWith(url)){
			int l = url.length();
			int r = line.length();
			return line.substring(l,r);
		} else 	return "";
	} 

	public static void contentsRecord(BufferedReader reader, Map<String,Integer> pageWords){
		String tempString = null;
		try{
			while ((tempString = reader.readLine()) != null){
				if (newPageCheck(tempString)){
					break;
				}
				String[] lines = tempString.split("[^a-zA-Z]");
				for (int i = 0; i < lines.length; i++){
					// System.out.println(lines[i]);
					if (lines[i] != ""){
						// System.out.println(pageWords.getOrDefault(lines[i],0));
						if (pageWords.containsKey(lines[i])){
							int cnt = pageWords.get(lines[i]);
							cnt++;
							pageWords.put(lines[i], cnt);
						} else{
							pageWords.put(lines[i], 1);
						}
						// pageWords.put(lines[i], pageWords.getOrDefault(lines[i],0) + 1);
					}
				}
			}

		}
		catch (IOException e){}
	}

	public static void writeFile(Map<String, Integer> pageWords, int pageId){
		for (String key: pageWords.keySet()){
			try {
				BufferedWriter write = new BufferedWriter(new FileWriter(FILE_WRITE_DIRECTORY + "\\" + key + ".txt",true));
		        write.write("key " + Integer.toString(pageId) + " " + Integer.toString(pageWords.get(key)));
		        write.newLine();
		        write.close();
		        System.out.println("File " + key + " create Successfully");
			}
			catch (IOException e){}
		}
	}

	public static void readFile(String fileName){
		File file = new File(fileName);
		BufferedReader reader = null; 
		boolean t = false;
		int pageId = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				// System.out.println(tempString);
				String url = findURL(tempString);
				if (url != ""){
					System.out.println(url);
					pageToURL.put(pageId,url);
					t = true;
				}
				if (contentsStart(tempString) && t){
					Map<String,Integer> pageWords = new HashMap<>();
					contentsRecord(reader, pageWords);
					writeFile(pageWords, pageId);
					System.out.println("page end");
					t = false;
					pageId++;
				}
				// System.out.println("....."+reader.readLine());
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

	public static void main(String[] args){
		ArrayList<String> filesLists = getFiles(FILE_DIRECTORY);
		for (int i = 0; i < filesLists.size(); i++){
			// readFile(filesLists.get(i));
			System.out.println(filesLists.get(i));
		}
	}
}


import java.io.*;
import java.util.*;
public class readFiles{

	static String FILE_WRITE_DIRECTORY = "D:\\wse_data_write";
	static String FILE_DIRECTORY = "D:\\wse_data_new";
	static Map<Integer, String> pageToURL = new HashMap<>();
	static Set<String> lexicon = new HashSet<>();

	// Check whether it starts a new page or not
	public static boolean newPageCheck(String line){
		String pageStart = "WARC/1.0";
		return line.startsWith(pageStart);
	}
	// Check the contents is needed or not
	public static boolean contentsStart(String line){
		String start = "Content-Length: ";
		return line.startsWith(start);
	} 

	// Find the website url
	public static String findURL(String line){
		String url = "WARC-Target-URI: ";
		if (line.startsWith(url)){
			int l = url.length();
			int r = line.length();
			return line.substring(l,r);
		} else 	return "";
	} 

	// Using for recording needed data
	public static void contentsRecord(BufferedReader reader, Map<String,Integer> pageWords){
		String tempString = null;
		try{
			while ((tempString = reader.readLine()) != null){
				if (newPageCheck(tempString)){
					break;
				}
				String[] lines = tempString.split("[^a-zA-Z]");
				for (int i = 0; i < lines.length; i++){
					if (lines[i] != ""){
						if (pageWords.containsKey(lines[i])){
							int cnt = pageWords.get(lines[i]);
							cnt++;
							pageWords.put(lines[i], cnt);
						} else{
							pageWords.put(lines[i], 1);
						}
					}
				}
			}

		}
		catch (IOException e){}
	}

	// Read file and process the contents
	public static boolean readFile(String fileName, int fileId){
		File file = new File(fileName);
		BufferedReader reader = null; 
		boolean t = false;
		int pageId = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String url = findURL(tempString);
				if (url != ""){
					System.out.println(url);
					pageToURL.put(pageId,url);
					t = true;
				}
				if (contentsStart(tempString) && t){
					Map<String,Integer> pageWords = new HashMap<>();
					contentsRecord(reader, pageWords);
					FilesWrite write = new FilesWrite();

					write.writeFile(pageWords, pageId, fileId, FILE_WRITE_DIRECTORY);
					System.out.println("page end");
					t = false;
					pageId++;
				}
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
		Directory dir = new Directory();
		FilesFetch fetch = new FilesFetch();
		ArrayList<String> filesLists = fetch.getFiles(FILE_DIRECTORY);
		boolean status = true;
		int fileId = 0;
		for (int i = 0; i < filesLists.size(); i++){
			String path = FILE_WRITE_DIRECTORY + "\\" + Integer.toString(fileId);
			dir.makeDirectory(path);
			status = readFile(filesLists.get(i), fileId);
			if (status) {
				fileId++;
				status = false;
			}
			// System.out.println(filesLists.get(i));
		}
	}
}


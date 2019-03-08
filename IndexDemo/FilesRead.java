import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesRead{

	static String FILE_WRITE_DIRECTORY = "D:\\wse_data_write";
	static String FILE_DIRECTORY = "D:\\wse_data_new";
	// static Set<String> lexicon = new HashSet<>();
	static Map<String,Map<Integer,Integer>> wordsDic = new HashMap<>();

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
	public static void contentsRecord(Map<String,Map<Integer,Integer>> pageWords, String line, int pageId){
		String[] lines = line.split("[,.:?'/!*&^%#@;><| ]+");
		String regex = "^[a-z]+$";
		Pattern pattern = Pattern.compile(regex);
		for (int i = 0; i < lines.length; i++){
			lines[i] = lines[i].toLowerCase();
			Matcher match = pattern.matcher(lines[i]);
			boolean b = match.matches();
			if (b){
				if (pageWords.containsKey(lines[i])){
					Map<Integer,Integer> docDic = pageWords.get(lines[i]);
					docDic.put(pageId, docDic.getOrDefault(pageId, 0) + 1);
					pageWords.put(lines[i], docDic);
				}
			}
		}
	}

	// Read file and process the contents
	public static boolean readFile(String fileName, int fileId, Map<Integer,String> pageToURL, int pageId){
		File file = new File(fileName);
		BufferedReader reader = null; 
		boolean t1 = false, t2 = false;
		int line = 1;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				if (!t1)	{
					String url = findURL(tempString);
					if (url != ""){
						System.out.println(url);
						pageToURL.put(pageId,url);
						t1 = true;
					}
				} else if (!t2) {
					if (contentsStart(tempString))	t2 = true;
				} else {
						if (newPageCheck(tempString)){
							pageId++;
							t1 = false;
							t2 = false;
							System.out.println("pageId is " + Integer.toString(pageId));
						} else contentsRecord(wordsDic,tempString, pageId);
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
		String path = FILE_DIRECTORY + "\\lexicon.txt";
		LexiconFetch words = new LexiconFetch();
		ArrayList<String> lexicon = words.getLexicon(path);


		Directory dir = new Directory();
		FilesFetch fetch = new FilesFetch();
		ArrayList<String> filesLists = fetch.getFiles(FILE_DIRECTORY);
		for (int i = 0; i < filesLists.size(); i++){
			System.out.println("-------> " + filesLists.get(i));
		}
		int pageId = 0;
		boolean status = false;
		int dirId = 0;

		while (dirId < filesLists.size()){
			for (int i = 0; i < lexicon.size(); i++){
				Map<Integer,Integer> table = new HashMap<>();
				wordsDic.put(lexicon.get(i), table);
			}

			Map<Integer, String> pageToURL = new HashMap<>();

			path = FILE_WRITE_DIRECTORY + "\\" + Integer.toString(dirId);
			dir.makeDirectory(path);

			status = readFile(filesLists.get(dirId), dirId, pageToURL, pageId);

			System.out.println("----------->");
			System.out.println(status);
			if (status){
				System.out.println("----------> Starting write files locally!");
				FilesWrite write = new FilesWrite();

				pageId += pageToURL.size();
				System.out.println("-----> pageId Starting " + Integer.toString(pageId));
				
				System.out.println("-----> Starting write page to url table !");
				write.writeDocTable(pageToURL, path, dirId);


				System.out.println("-----> Finish page to url table writing !");

				String curPath = path + "\\" + Integer.toString(dirId);
				dir.makeDirectory(curPath);
				System.out.println("-----> Starting writing docId and freq !");
				System.out.println("-----> keywords in total " + Integer.toString(wordsDic.size()));
				write.writeFile(wordsDic,curPath);
				System.out.println("---------------> files " + Integer.toString(dirId) + " write successfully!");
				dirId++;
			}
		}
	}
}


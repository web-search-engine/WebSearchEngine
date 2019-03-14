package QueryDemo;

import java.io.*;
import java.util.*;
import IndexDemo.*;

public class DocScores{

	static int totalDocs = 1233519;
	static double aveLength = 988.68;
	static String PAGE_DOC_TABLE = "D:\\wse_data_docURLtable";
	static String FILE_DIRECTORY = "D:\\wse_data_merge";
	static String DOC_LENGTH_TABLE = "D:\\wse_data_docLength";
	static String SCORE_WRITE = "D:\\wse_data_BM25_scores";

	public static int wordInfoRecord(String path, Map<String,String> docFreqTable){
		File file = null;
		int line = 0;
		try {
			file = new File(path);
			if (file.exists()){
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;
					while ((tempString = reader.readLine()) != null){
						String[] lines = tempString.split(" ");
						docFreqTable.put(lines[0],lines[1]);
						line ++;
					}
					return line;
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
		return line;
	}

	public static double IDF(String path, String word, Map<String,String> docFreqTable){
		String docPath = path + "\\" + word + ".txt";
		int docNums = wordInfoRecord(docPath, docFreqTable);

		System.out.println("Doc number contains this word is " + Integer.toString(docNums));

		double midUp = totalDocs - docNums + 0.5;
		double midDown = docNums + 0.5;

		if (midUp <= midDown){
			midUp = totalDocs + 0.5;
		}
		double res = Math.log(midUp/midDown);

		return res;
	}

	public static Map<String, Double> sortByValue(Map<String, Double> freqTable){
		List<Map.Entry<String, Double> > list = 
               new LinkedList<Map.Entry<String, Double> >(freqTable.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() { 
            @Override 
            public int compare(Map.Entry<String, Double> o1,  
                               Map.Entry<String, Double> o2) 
            	{ 
                return o2.getValue().compareTo(o1.getValue());
            } 
        }); 

        Map<String, Double> temp = new LinkedHashMap<String, Double>(); 
        for (Map.Entry<String, Double> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp;
	}

	public static void main(String[] args){
		String path = "D:\\lexicon.txt";
		LexiconFetch words = new LexiconFetch();
		Set<String> lexicon = words.getLexicon(path);

		getDocInfo docLength = new getDocInfo();
		Map<String, String> docLengthTable = docLength.docLengthFetch(DOC_LENGTH_TABLE);

		FilesWrite write = new FilesWrite();

		for (Iterator it= lexicon.iterator(); it.hasNext();){
			String word = it.next().toString();
			Map<String, String> docFreqTable = new HashMap<>();

			double alpha = IDF(FILE_DIRECTORY, word, docFreqTable);
			System.out.println(word + " alpha is " + Double.toString(alpha));

			Map<String, Double> wordDocScores = new HashMap<>();

			for (String key: docFreqTable.keySet()){
				int l = Integer.parseInt(docLengthTable.get(key));
				double K = l / aveLength;
				int freq = Integer.parseInt(docFreqTable.get(key));
				double R = 3 * freq / (freq + 0.5 + 1.5 * K) * alpha;
				wordDocScores.put(key, R);
				
			}

			System.out.println("Starting write ");
			Map<String, Double> sortScoreTable = sortByValue(wordDocScores);
			write.scoreFilesWrite(sortScoreTable, SCORE_WRITE, word);
			System.out.println("Finish wirte");
			for (String key: sortScoreTable.keySet()){
				System.out.println(key + " BM25 scores is " + Double.toString(wordDocScores.get(key)));
			}

		}
	}
}
package QueryTest;

import java.io.*;
import java.util.*;
import IndexDemo.*;

public class QueryTest{
	static String INPUT = "kid education";


	public static void docURLMap(Map<String,String> docURLTable, String path){
		File file = null;
		BufferedReader reader = null; 
		try {
			file = new File(path);
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] lines = tempString.split(" ");
				docURLTable.put(lines[0],lines[1]);
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

	public static void calculateScores(String path, Map<String,Double> queryScores){
		File file = null;
		BufferedReader reader = null; 
		try {
			file = new File(path);
			if (file.exists()){
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					String[] lines = tempString.split(" ");
					queryScores.put(lines[0], queryScores.getOrDefault(lines[0], 0.0) + Double.parseDouble(lines[1]));
	        	}
	        	try {
                	reader.close();
            	} 
            	catch (IOException e1) {}
			}
		} 
		catch (IOException e){}	
	}



	public static void main(String[] args){
		String path = "D:\\wse_data_BM25_scores";
		String docURLPath = "D:\\wse_data_docURL\\docUrlTable.txt";

		String[] input = INPUT.split(" ");

		Map<String,Double> queryScores = new HashMap<>();
		Map<String,String> docURLTable = new HashMap<>();

		docURLMap(docURLTable,docURLPath);


		for (int i = 0; i < input.length; i++){
			String queryPath = path + "\\" + input[i] + ".txt";
			System.out.println("The file is " + queryPath);
			calculateScores(queryPath, queryScores);
		}

		Map<String, Double> sortQueryScores = sortByValue(queryScores);
		// for (String key: sortQueryScores.keySet()){
		// 	System.out.println(key + " is " + Double.toString(sortQueryScores.get(key)));
		// }
		System.out.println("Map size is " + Integer.toString(queryScores.size()));
		int k = 1;
		String[] idTable = new String[10];
		for (String key: sortQueryScores.keySet()){
			if (k > 10)	break;
			else{
				idTable[k-1] = key;
				System.out.println("The id is " + key + " the scores is " + Double.toString(sortQueryScores.get(key)));
			}
			k++;
		}

		for (int i = 0; i < idTable.length; i++){
			System.out.println("The number " + Integer.toString(i + 1) + " related url is " + docURLTable.get(idTable[i]));
		}


	}
}
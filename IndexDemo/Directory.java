package IndexDemo;

import java.io.*;
import java.util.*;
public class Directory{
	public void makeDirectory(String path){
		File file = null;
		try {
			file = new File(path);
			if (!file.exists())	file.mkdirs();
		}
		catch (Exception e){}
		finally{
			file = null;
		}
	}
}
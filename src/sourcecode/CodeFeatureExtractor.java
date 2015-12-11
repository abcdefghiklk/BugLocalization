package sourcecode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import utils.WVToolWrapper;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class CodeFeatureExtractor {
	/**
	 * Extract the dictionaries for the source code corpus
	 * @param codeCorpusDirPath
	 * @return
	 * @throws Exception
	 */
	public static WVTWordList extractCodeDictionary(String codeCorpusDirPath) throws Exception{
		if(!new File(codeCorpusDirPath).isDirectory()){
			System.out.println("The corpus directory is invalid!");
			return new WVTWordList(1);
		}
		WVTFileInputList list=WVToolWrapper.extractCorpusFileList(codeCorpusDirPath);
		WVTWordList dictionary=WVToolWrapper.extractCorpusDic(list);
		return dictionary;
	}
	
	/**
	 * Save the code dictionary to the target file
	 * @param dictionary
	 * @param dicFilePath
	 * @throws IOException
	 */
	public static void exportCodeDictionary(WVTWordList dictionary, String dicFilePath) throws IOException{
		WVToolWrapper.saveCorpusDic(dictionary, dicFilePath);
	}
	
	/**
	 * Import the code dictionary from the file
	 * @param dicFilePath
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> importCodeDictionary(String dicFilePath) throws Exception{
		ArrayList<String> dictionary=new ArrayList<String>();
		if(!new File(dicFilePath).isFile()){
			System.out.println("The dictionary file path is invalid!");
			return dictionary;
		}
		BufferedReader reader=new BufferedReader(new FileReader(dicFilePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			dictionary.add(line.trim());
		}
		reader.close();
		return dictionary;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

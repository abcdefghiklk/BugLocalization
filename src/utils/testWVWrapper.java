package utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import Jama.Matrix;
import edu.udo.cs.wvtool.config.WVTConfigException;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.config.WVTConfigurationRule;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.LovinsStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.BinaryOccurrences;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.generic.vectorcreation.TermFrequency;
import edu.udo.cs.wvtool.generic.vectorcreation.TermOccurrences;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class testWVWrapper {
	public static void compare(String dirPath, String filePath) throws Exception{
		HashMap<String, Integer> myMap = new HashMap<String, Integer>();
		for(File oneFile:  new File(dirPath).listFiles()){
			BufferedReader reader=new BufferedReader(new FileReader(oneFile));
			String []str=reader.readLine().split(" ");
			myMap.put(oneFile.getName(), str.length);
			reader.close();
		}
		
		BufferedReader reader=new BufferedReader(new FileReader(filePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			String []str=line.split("\t");
			if(myMap.containsKey(str[0])){				
				if(myMap.get(str[0])!=str[1].split(" ").length-1){
					System.out.println("The length for "+str[0]+ " is not the same!");
					System.out.println("BugLocator Result: "+ String.valueOf(str[1].split(" ").length-1));
					System.out.println("my result: "+ String.valueOf(myMap.get(str[0])));
				}
				else{
					System.out.println("The length for "+str[0]+ " is the same!");
					System.out.println("BugLocator Result: "+ String.valueOf(str[1].split(" ").length-1));
					System.out.println("my result: "+ String.valueOf(myMap.get(str[0])));
				}
			}
			else{
				System.out.println("The file "+ str[0] + "does not exist in my result!");
			}
		}
		
		reader.close();
		
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String corpusDirPath1="C:/Users/ql29/Documents/EClipse/Corpus/bug/information";
		String corpusDirPath2="C:/Users/ql29/Documents/EClipse/Corpus/code/codeContentCorpus";
//		String corpusDirPath1="C:/Users/ql29/Documents/EClipse/testFolder";
//		String []corpusDirPaths= {corpusDirPath1,corpusDirPath2};
		String corpusFilePath="C:/Users/ql29/Documents/EClipse/corpusVec";
		String dstFilePath="C:/Users/ql29/Documents/EClipse/vectors";
		String dstFilePath_tf="C:/Users/ql29/Documents/EClipse/vectors_tf";
		String dstFilePath_logtf="C:/Users/ql29/Documents/EClipse/vectors_logtf";
		String dstFilePath_df="C:/Users/ql29/Documents/EClipse/vectors_df";
		String dstFilePath_idf="C:/Users/ql29/Documents/EClipse/vectors_idf";
		String dstFilePath_tfidf="C:/Users/ql29/Documents/EClipse/vectors_tfidf";
		String dstFilePath_logtfidf="C:/Users/ql29/Documents/EClipse/vectors_logtfidf";
		String vocabularyFilePath="C:/Users/ql29/Documents/EClipse/vocabulary";
//		WVTFileInputList list = WVToolWrapper.extractCorpusFileList(corpusDirPath2);
//		WVTool wvt = new WVTool(false);
//		
//		WVTWordList dic = WVToolWrapper.extractCorpusDic(list);
//		
//		WVToolWrapper.generateCorpusVectors(corpusFilePath, list, dic);
//		WVToolWrapper.generateVectors(dstFilePath, WVToolWrapper.extractCorpusFileList(corpusDirPath1), dic, corpusFilePath, "tfidf");
//		String dirPath="C:/Users/ql29/Documents/EClipse/Corpus/code/codeContentCorpus";
//		String filePath= "C:/Users/ql29/Documents/EClipse/BugLocatorTool/tmp/codeCorpus.txt";
//		compare(dirPath, filePath);
	}

}

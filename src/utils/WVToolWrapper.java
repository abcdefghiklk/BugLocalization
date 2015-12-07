package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import bug.BugDataProcessor;
import bug.BugRecord;
import property.Property;
import edu.udo.cs.wvtool.config.WVTConfigException;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.config.WVTConfigurationRule;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.LovinsStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * The Use of WVTools in this program
 * @author Qiuchi Li
 *
 */
public class WVToolWrapper {
	
	/**
	 * Extract the file list from the corpus
	 * @param corpusDirPath
	 * @return
	 */
	public static WVTFileInputList extractCorpusFileList(String corpusDirPath){
		WVTFileInputList list = new WVTFileInputList(1);
		list.addEntry(new WVTDocumentInfo(corpusDirPath,
				"txt", "", "english", 0));
		return list;
	}
	
	
	/**
	 * Extract the corpus dictionary from the file list
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static WVTWordList extractCorpusDic(WVTFileInputList list) throws Exception{
		WVTool wvt = new WVTool(false);
		WVTConfiguration config = new WVTConfiguration();
		WVTStemmer stemmer = new PorterStemmerWrapper();
		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(stemmer));
		WVTWordList dictionary = wvt.createWordList(list, config);
		return dictionary;
	}
	
	/**
	 * Generate a vector for each file given the dictionary
	 * @param dstFilePath
	 * @param list
	 * @param dictionary
	 * @throws Exception
	 */
	public static void generateVectors(String dstFilePath, WVTFileInputList list, WVTWordList dictionary) throws Exception{
		WVTool wvt= new WVTool(false);
		WordVectorWriter wvw= new WordVectorWriter(new FileWriter(dstFilePath),true);
		WVTConfiguration config = new WVTConfiguration();
		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));
		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));
		wvt.createVectors(list, config, dictionary);
	}
	
	
	private static void showHelp() {
		String usage = "Usage:java -jar VectorCreater [-options] \r\n\r\nwhere options must include:\r\n"
				+ "-d	indicates the absolute path of the file corpus\r\n"
				+ "-o	indicates the absolute path of the output file storing the vector representation for each file.";

		System.out.println(usage);
	}
	
	
	public static void parseArgs(String []args) throws Exception{
		int i = 0;
		String bugCorpusPath=new String();
		String bugVectorFilePath=new String();
//		String bugCorpusDirPath="C:/Users/ql29/Documents/EClipse/BugCorpus/summary";
//		String bugVectorFilePath="C:/Users/ql29/Documents/EClipse/BugCorpus/bugVector";
		while (i < args.length-1) {
			if (args[i].equals("-d")) {
				i++;
				bugCorpusPath = args[i];
			} else if (args[i].equals("-o")) {
				i++;
				bugVectorFilePath = args[i];
			}
			i++;
		}
		boolean isLegal=true;
		if (!new File(bugCorpusPath).isDirectory()){
			System.out.println("The input directory is invalid!");
			isLegal=false;
		}
		if (bugVectorFilePath.equals(new String())){
			System.out.println("Please assign an output file path!");
			isLegal=false;
		}
		if(!isLegal){
			showHelp();
		}
		else{
			WVTFileInputList list=extractCorpusFileList(bugCorpusPath);
			WVTWordList dictionary=extractCorpusDic(list);
			generateVectors(bugVectorFilePath,list,dictionary);
		}
	}

	public static void main(String []args) throws Exception{
		if(args.length==0){
			showHelp();
		}
		else{
			parseArgs(args);
		}
	}
}


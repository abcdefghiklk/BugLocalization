package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * The Use of WVTools in this program
 * @author Qiuchi Li
 *
 */
public class WVToolWrapper {
	
	/**
	 * Extract the file list from an array of corpus
	 * @param corpusDirPathArray
	 * @return
	 */
	public static WVTFileInputList extractCorpusFileList(String []corpusDirPathArray){
		WVTFileInputList list = new WVTFileInputList(1);
		for(String corpusDirPath:corpusDirPathArray){ 
			list.addEntry(new WVTDocumentInfo(corpusDirPath,
					"txt", "", "english", 0));
		}
		return list;
	}
	
	/**
	 * Extract the file list from a single directory
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
		final WVTStemmer porterStemmer = new PorterStemmerWrapper();
		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER,
				new WVTConfigurationRule() {
					public Object getMatchingComponent(WVTDocumentInfo d)
							throws WVTConfigException {
						return porterStemmer;
					}
				});
		WVTStemmer stemmer = new LovinsStemmerWrapper();
		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER,
				new WVTConfigurationFact(stemmer));
//		WVTStemmer stemmer = new PorterStemmerWrapper();
//		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(stemmer));
		WVTWordList dictionary = wvt.createWordList(list, config);
		return dictionary;
	}
	
	/**
	 * Save the corpus dictionary to the given file
	 * @param dictionary
	 * @param dstFilePath
	 * @throws IOException
	 */
	public static void saveCorpusDic(WVTWordList dictionary, String dstFilePath) throws IOException{
		dictionary.storePlain(new FileWriter(dstFilePath));
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
		if(!new File(dstFilePath).isFile()){
			new File(dstFilePath).createNewFile();
		}
		WordVectorWriter wvw= new WordVectorWriter(new FileWriter(dstFilePath),true);
		WVTConfiguration config = new WVTConfiguration();
		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));
		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));
		wvt.createVectors(list, config, dictionary);
		wvw.close();
	}
	
	private static void showHelp() {
		String usage = "Usage:java -jar vectorCreator [-options] \r\n\r\nwhere options must include:\r\n"
				+ "-c	indicates the absolute path of the file corpus\r\n"
				+ "and at least one of the following options:\r\n"
				+ "-d   indicates the absolute path of the output file storing the corpus dictionary\r\n"
				+ "-v   indicates the absolute path of the output file storing the vector representation for each file.";

		System.out.println(usage);
	}
	
	
	public static void parseArgs(String []args) throws Exception{
		int i = 0;
		String bugCorpusPath=new String();
		String bugVectorFilePath=new String();
		String corpusDicFilePath=new String();
//		String bugCorpusDirPath="C:/Users/ql29/Documents/EClipse/BugCorpus/summary";
//		String bugVectorFilePath="C:/Users/ql29/Documents/EClipse/BugCorpus/bugVector";
//		String bugVectorFilePath="C:/Users/ql29/Documents/EClipse/BugCorpus/corpusDic";
		while (i < args.length-1) {
			if (args[i].equals("-c")) {
				i++;
				bugCorpusPath = args[i];
			} else if (args[i].equals("-v")) {
				i++;
				bugVectorFilePath = args[i];
			} else if (args[i].equals("-d")) {
				i++;
				corpusDicFilePath =args[i];
			}
			i++;
		}
		boolean isLegal=true;
		if (!new File(bugCorpusPath).isDirectory()){
			System.out.println("The input directory is invalid!");
			isLegal=false;
		}
		if (bugVectorFilePath.equals(new String()) && corpusDicFilePath.equals(new String())){
			System.out.println("Please indicate the path of the file for the vectors or the one for the dictionary!");
			isLegal=false;
		}
		if(!isLegal){
			showHelp();
		}
		else{
			String []bugCorpusPathArray={bugCorpusPath};
			WVTFileInputList list=extractCorpusFileList(bugCorpusPathArray);
			WVTWordList dictionary=extractCorpusDic(list);
//			System.out.println(dictionary.getNumDocuments());
			saveCorpusDic(dictionary,corpusDicFilePath);
			generateVectors(bugVectorFilePath,list,dictionary);
		}
	}

	public static void main(String []args) throws Exception{
//		args=new String[6];
//		args[0]="-c";
//		args[1]="C:/Users/ql29/Documents/EClipse/BugCorpus/summary";
//		args[2]="-v";
//		args[3]="C:/Users/ql29/Documents/EClipse/BugCorpus/bugVector";
//		args[4]="-d";
//		args[5]="C:/Users/ql29/Documents/EClipse/BugCorpus/corpusDic";
		if(args.length==0){
			showHelp();
		}
		else{
			parseArgs(args);
		}
	}
}


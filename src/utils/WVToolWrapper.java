package utils;

import java.io.FileWriter;
import java.io.IOException;

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
	
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub#
		String bugCorpusDirPath="C:/Users/ql29/Documents/EClipse/BugCorpus/summary";
		String bugVectorFilePath="C:/Users/ql29/Documents/EClipse/BugCorpus/bugVector";
		WVTool wvt = new WVTool(false);
		WVTConfiguration config = new WVTConfiguration();
		WVTStemmer stemmer = new PorterStemmerWrapper();
		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(stemmer));
		WVTFileInputList list = new WVTFileInputList(1);

		list.addEntry(new WVTDocumentInfo(bugCorpusDirPath,
				"txt", "", "english", 0));

		WVTWordList wordList = wvt.createWordList(list, config);
		for(int i=0;i<wordList.getNumWords();i++){
			System.out.println(wordList.getWord(i));
		}
		WordVectorWriter wvw= new WordVectorWriter(new FileWriter(bugVectorFilePath),true);
		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));
		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));
		wvt.createVectors(list, config, wordList);
		wvw.close();
	}

}

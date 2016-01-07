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
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class testWVWrapper {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String corpusDirPath="C:/Users/dell/Documents/EClipse/testFolder";
		String vocabularyPath="C:/Users/dell/Documents/EClipse/vocabulary";
		String dstFilePath="C:/Users/dell/Documents/EClipse/vectors";
		WVTFileInputList list = WVToolWrapper.extractCorpusFileList(corpusDirPath);
		
		
		WVTWordList dic=WVToolWrapper.extractCorpusDic(list);		
//		WVTool wvt = new WVTool(false);
//		WVTConfiguration config = new WVTConfiguration();
//		final WVTStemmer porterStemmer = new PorterStemmerWrapper();
//		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER,
//				new WVTConfigurationRule() {
//					public Object getMatchingComponent(WVTDocumentInfo d)
//							throws WVTConfigException {
//						return porterStemmer;
//					}
//				});
//		WVTStemmer stemmer = new LovinsStemmerWrapper();
//		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER,
//				new WVTConfigurationFact(stemmer));
//		
//		WVTWordList dictionary = wvt.createWordList(list, config);
		
		
		dic.storePlain(new FileWriter(vocabularyPath));
//		WVTool wvt= new WVTool(false);
//		WordVectorWriter wvw= new WordVectorWriter(new FileWriter(dstFilePath),true);
//		WVTConfiguration config = new WVTConfiguration();
//		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));
//		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));
//		wvt.createVectors(list, config, dic);
//		wvw.close();
		WVToolWrapper.generateVectors(dstFilePath, list, dic);
	}

}

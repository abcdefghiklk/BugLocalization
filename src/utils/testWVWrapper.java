package utils;
import java.io.File;
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

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String corpusDirPath="C:/Users/ql29/Documents/EClipse/testFolder";
		String dstFilePath="C:/Users/ql29/Documents/EClipse/vectors";
		String dstFilePath_tf="C:/Users/ql29/Documents/EClipse/vectors_tf";
		String dstFilePath_logtf="C:/Users/ql29/Documents/EClipse/vectors_logtf";
		String dstFilePath_df="C:/Users/ql29/Documents/EClipse/vectors_df";
		String dstFilePath_idf="C:/Users/ql29/Documents/EClipse/vectors_idf";
		String dstFilePath_tfidf="C:/Users/ql29/Documents/EClipse/vectors_tfidf";
		String dstFilePath_logtfidf="C:/Users/ql29/Documents/EClipse/vectors_logtfidf";
		String vocabularyFilePath="C:/Users/ql29/Documents/EClipse/vocabulary";
		WVTFileInputList list = WVToolWrapper.extractCorpusFileList(corpusDirPath);
		WVTool wvt = new WVTool(false);
		
		WVTWordList dic = WVToolWrapper.extractCorpusDic(list);
//		WVTWordVector q = wvt.createVector("desperate beast company desperate beast company", dic); 

//		for(int docF:dic.getDocumentFrequencies()){
//			System.out.println(docF);
//		}
//		for(int i=0;i<dic.getNumWords();i++){
//			System.out.println(dic.getWord(i));
//		}
		
//		System.out.println(dic.getTermCountForCurrentDocument());
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
		
		
		dic.storePlain(new FileWriter(vocabularyFilePath));
//		WVTool wvt= new WVTool(false);
		WordVectorWriter wvw= new WordVectorWriter(new FileWriter(dstFilePath),true);
		WVTConfiguration config = new WVTConfiguration();
		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));
		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TermOccurrences()));
		wvt.createVectors(list, config, dic);
		wvw.close();
		HashMap<String, Matrix> occurencesMap=MatrixUtil.loadVectors(dstFilePath, dic.getNumWords());
		HashMap<String, Matrix> termFrequencyMap= MatrixUtil.convert(occurencesMap,"logtf");
		MatrixUtil.saveVectors(termFrequencyMap, dstFilePath_logtf);
//		termFrequencyMap= MatrixUtil.convert(occurencesMap,"logtf");
//		MatrixUtil.saveVectors(termFrequencyMap, dstFilePath_logtf);
//		termFrequencyMap= MatrixUtil.convert(occurencesMap,"df");
//		MatrixUtil.saveVectors(termFrequencyMap, dstFilePath_df);
//		termFrequencyMap= MatrixUtil.convert(occurencesMap,"idf");
//		MatrixUtil.saveVectors(termFrequencyMap, dstFilePath_idf);
//		termFrequencyMap= MatrixUtil.convert(occurencesMap,"tfidf");
//		MatrixUtil.saveVectors(termFrequencyMap, dstFilePath_tfidf);
//		termFrequencyMap= MatrixUtil.convert(occurencesMap,"logtfidf");
//		MatrixUtil.saveVectors(termFrequencyMap, dstFilePath_logtfidf);
//		WVToolWrapper.generateVectors(dstFilePath, list, dic);
//		WVToolWrapper.generateVectors(dstFilePath2, list2, dic);
	}

}

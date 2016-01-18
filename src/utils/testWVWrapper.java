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
		WVTFileInputList list = WVToolWrapper.extractCorpusFileList(corpusDirPath2);
		WVTool wvt = new WVTool(false);
		
		WVTWordList dic = WVToolWrapper.extractCorpusDic(list);
		
		WVToolWrapper.generateCorpusVectors(corpusFilePath, list, dic);
		WVToolWrapper.generateVectors(dstFilePath, WVToolWrapper.extractCorpusFileList(corpusDirPath1), dic, corpusFilePath, "tfidf");
		
//		dic.storePlain(new FileWriter(vocabularyFilePath));
//		WordVectorWriter wvw_corpus= new WordVectorWriter(new FileWriter(corpusFilePath),true);
//		WVTConfiguration config = new WVTConfiguration();
//		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw_corpus));
//		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TermOccurrences()));
//		wvt.createVectors(WVToolWrapper.extractCorpusFileList(corpusDirPath2), config, dic);
//		wvw_corpus.close();
//		
//		WordVectorWriter wvw_output= new WordVectorWriter(new FileWriter(dstFilePath),true);
//		config = new WVTConfiguration();
//		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw_output));
//		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TermOccurrences()));
//		wvt.createVectors(WVToolWrapper.extractCorpusFileList(corpusDirPath1), config, dic);
//		wvw_output.close();
//		
//		HashMap<String, Matrix> corpusOccurrencesMap=MatrixUtil.loadVectors(corpusFilePath, dic.getNumWords());
//		HashMap<String, Matrix> occurrencesMap=MatrixUtil.loadVectors(dstFilePath, dic.getNumWords());
//		HashMap<String, Matrix> termFrequencyMap= MatrixUtil.convert(occurrencesMap,corpusOccurrencesMap, "tfidf");
//		MatrixUtil.saveVectors(termFrequencyMap, dstFilePath_tfidf);
	}

}

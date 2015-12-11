package feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import sourcecode.CodeFeatureExtractor;
import utils.MatrixUtil;
import utils.WVToolWrapper;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.wordlist.WVTWordList;
import Jama.Matrix;
import bug.BugFeatureExtractor;

public class VSMScore {
	/**
	 * Merge the bug dictionary and the code dictionary
	 * @param bugDicFilePath
	 * @param codeDicFilePath
	 * @param mergedDicFilePath
	 * @throws Exception
	 */
	public static void generate(String bugCorpusDirPath, String codeCorpusDirPath, String bugVecFilePath, String codeVecFilePath, String simMatFilePath) throws Exception{
		WVTFileInputList list=new WVTFileInputList(1);
		list.addEntry(new WVTDocumentInfo(bugCorpusDirPath,"txt", "", "english", 0));
		list.addEntry(new WVTDocumentInfo(codeCorpusDirPath,"txt", "", "english", 0));
		WVTWordList dic=WVToolWrapper.extractCorpusDic(list);
		WVTFileInputList bugList=new WVTFileInputList(1);
		bugList.addEntry(new WVTDocumentInfo(bugCorpusDirPath,"txt", "","english",0));
		WVTFileInputList codeList=new WVTFileInputList(1);
		codeList.addEntry(new WVTDocumentInfo(codeCorpusDirPath,"txt", "","english",0));
		WVToolWrapper.generateVectors(bugVecFilePath, bugList, dic);
		WVToolWrapper.generateVectors(codeVecFilePath, codeList, dic);
		HashMap<String, Matrix> bugVecList=MatrixUtil.loadVectors(bugVecFilePath, dic.getNumWords());
		HashMap<String, Matrix> codeVecList=MatrixUtil.loadVectors(codeVecFilePath, dic.getNumWords());
		MatrixUtil.exportSimilarityMatrix(bugVecList, codeVecList, simMatFilePath, dic.getNumWords());
		
//		Matrix simMat=MatrixUtil.computeSimilarityMatrix(bugVecList, codeVecList, dic.getNumWords());
//		System.out.println(simMat.getRowDimension()+" "+simMat.getColumnDimension());
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String bugCorpusDirPath="C:/Users/ql29/Documents/EClipse/BugCorpus/information";
		String codeCorpusDirPath="C:/Users/ql29/Documents/EClipse/sourceCodeCorpus/codeContentCorpus";
		String bugVecFilePath="C:/Users/ql29/Documents/EClipse/bugVec";
		String codeVecFilePath="C:/Users/ql29/Documents/EClipse/codeVec";
		String simMatFilePath="C:/Users/ql29/Documents/EClipse/simMat";
//		BugFeatureExtractor.exportBugDictionary(BugFeatureExtractor.extractBugDictionary(bugCorpusDirPath), bugDicPath);
//		CodeFeatureExtractor.exportCodeDictionary(CodeFeatureExtractor.extractCodeDictionary(codeCorpusDirPath), codeDicPath);
		generate(bugCorpusDirPath,codeCorpusDirPath,bugVecFilePath,codeVecFilePath,simMatFilePath);
//		System.out.println(dic.getNumWords());
	}

}

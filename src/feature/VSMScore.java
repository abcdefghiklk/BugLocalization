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
	 * Generate the similarity matrix and save it to file
	 * @param bugCorpusDirPath (Input)
	 * @param codeCorpusDirPath (Input)
	 * @param bugVecFilePath (Intermediate file)
	 * @param codeVecFilePath (Intermediate file)
	 * @param simMatFilePath (Output)
	 * @throws Exception
	 */
	public static void generate(String bugCorpusDirPath, String codeCorpusDirPath, String bugVecFilePath, String codeVecFilePath, String simMatFilePath) throws Exception{
		
		//Extract dictionaries for bug and code corpus
		String []bugAndCodeDirPaths={bugCorpusDirPath,codeCorpusDirPath};
		WVTFileInputList bugAndCodeList=WVToolWrapper.extractCorpusFileList(bugAndCodeDirPaths);
		WVTWordList dic=WVToolWrapper.extractCorpusDic(bugAndCodeList);
		
		//Extract the bug list and code list
		WVTFileInputList bugList=WVToolWrapper.extractCorpusFileList(bugCorpusDirPath);
		WVTFileInputList codeList=WVToolWrapper.extractCorpusFileList(codeCorpusDirPath);
		
		//Generate Vectors and save them to files
		WVToolWrapper.generateVectors(bugVecFilePath, bugList, dic);
		WVToolWrapper.generateVectors(codeVecFilePath, codeList, dic);
		
		//Load the vectors for bug and code vectors, under the same dictionary
		HashMap<String, Matrix> bugVecList=MatrixUtil.loadVectors(bugVecFilePath, dic.getNumWords());
		HashMap<String, Matrix> codeVecList=MatrixUtil.loadVectors(codeVecFilePath, dic.getNumWords());
		
		//Calculate and save the similarity between each code and each bug in the file
		MatrixUtil.exportSimilarityMatrix(bugVecList, codeVecList, simMatFilePath, dic.getNumWords());
		
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

package feature;

import java.util.HashMap;



import config.Config;
import utils.MatrixUtil;
import utils.WVToolWrapper;
import Jama.Matrix;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class VectorCreator {
	/**
	 * Create the code and bug vectors and save them in files
	 * @param bugCorpusDirPath
	 * @param codeCorpusDirPath
	 * @param bugVecFilePath
	 * @param codeVecFilePath
	 * @param featureType
	 * @throws Exception
	 */
	public static void create(String bugCorpusDirPath, String codeCorpusDirPath, String bugVecFilePath, String codeVecFilePath, String featureType) throws Exception{
		
		//Extract the bug list and code list
		WVTFileInputList bugList=WVToolWrapper.extractCorpusFileList(bugCorpusDirPath);
		WVTFileInputList codeList=WVToolWrapper.extractCorpusFileList(codeCorpusDirPath);
		WVTWordList dic=WVToolWrapper.extractCorpusDic(codeList);
		
		//Generate Vectors and save them to files
		WVToolWrapper.generateCorpusVectors(codeVecFilePath, codeList, dic);
		WVToolWrapper.generateVectors(bugVecFilePath, bugList, dic, codeVecFilePath, featureType);
		WVToolWrapper.generateVectors(codeVecFilePath, codeList, dic, codeVecFilePath, featureType);
		
		//set the dictionary size
		Config.getInstance().setDicSize(dic.getNumWords());
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

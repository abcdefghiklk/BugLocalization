package feature;

import java.util.HashMap;

import config.Config;
import utils.MatrixUtil;
import Jama.Matrix;

public class VSMScore {
	/**
	 * Generate the similarity matrix and save it to file
	 * @param bugVecFilePath (Input)
	 * @param codeVecFilePath (Input)
	 * @param simMatFilePath (Output)
	 * @throws Exception
	 */
	public static void generate(String bugVecFilePath, String codeVecFilePath, String simMatFilePath) throws Exception{
		int dicSize=Config.getInstance().getDicSize();
		
		//Load the vectors for bug and code vectors, under the same dictionary
		HashMap<String, Matrix> bugVecList = MatrixUtil.loadVectors(bugVecFilePath, dicSize);
		HashMap<String, Matrix> codeVecList = MatrixUtil.loadVectors(codeVecFilePath, dicSize);
		
		//Calculate and save the similarity between each code and each bug in the file
		MatrixUtil.exportSimilarityMatrix(bugVecList, codeVecList, simMatFilePath, dicSize);
	}
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String bugVecFilePath="C:/Users/ql29/Documents/EClipse/bugVec";
		String codeVecFilePath="C:/Users/ql29/Documents/EClipse/codeVec";
		String simMatFilePath="C:/Users/ql29/Documents/EClipse/simMat";
		generate(bugVecFilePath,codeVecFilePath,simMatFilePath);
//		System.out.println(dic.getNumWords());
	}

}

package feature;

import java.util.HashMap;

import sourcecode.CodeFeatureExtractor;
import utils.MatrixUtil;
import Jama.Matrix;
import config.Config;

public class RevisedVSMScore {
	/**
	 * Generate the revised similarity matrix and save it to file
	 * @param bugVecFilePath (Input)
	 * @param codeVecFilePath (Input)
	 * @param codeLengthFilePath (Input)
	 * @param simMatFilePath (Output)
	 * @throws Exception
	 */
	public static void generate(String bugVecFilePath, String codeVecFilePath, String codeLengthFilePath, String simMatFilePath) throws Exception{
		int dicSize=Config.getInstance().getDicSize();
		
		//Load the vectors for bug and code vectors, under the same dictionary
		HashMap<String, Matrix> bugVecList = MatrixUtil.loadVectors(bugVecFilePath, dicSize);
		HashMap<String, Matrix> codeVecList = MatrixUtil.loadVectors(codeVecFilePath, dicSize);
		
		//Load the code length for each code file
		HashMap<String, Double> codeLengthMap = CodeFeatureExtractor.loadCodeLength(codeLengthFilePath);
		
		//Calculate and save the similarity between each code and each bug in the file
//		Matrix simMat=MatrixUtil.computeSimilarityMatrix(bugVecList,codeVecList,dicSize);
		int rowCount=bugVecList.size();
		int colCount=bugVecList.size();
		Matrix simMat=new Matrix(rowCount, colCount);
		Matrix []bugVecArray=bugVecList.values().toArray(new Matrix[0]);
		Matrix []codeVecArray=codeVecList.values().toArray(new Matrix[0]);
		String []codeIDList=codeVecList.keySet().toArray(new String[0]);
		for (int j=0; j<colCount; j++){
			double revisedLength= codeLengthMap.get(codeIDList[j]);
			for (int i=0; i<rowCount; i++){
				simMat.set(i, j, MatrixUtil.computeCosSimilarity(bugVecArray[i], codeVecArray[j])*revisedLength);
			}
		}
		MatrixUtil.exportMatrix(bugVecList,codeVecList,simMat,simMatFilePath);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

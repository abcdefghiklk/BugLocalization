package feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import bug.BugFeatureExtractor;
import bug.BugRecord;
import config.Config;
import Jama.Matrix;
import utils.MatrixUtil;

public class SimiScore {
	/**
	 * Generate the simiScore and save it to file
	 * @param codeVecFilePath
	 * @param bugVecFilePath
	 * @param bugList
	 * @param scoreMatFilePath
	 * @throws Exception
	 */
	public static void generate(String codeVecFilePath, String bugVecFilePath, ArrayList<BugRecord> bugList, String scoreMatFilePath) throws Exception{
		HashMap<String, Matrix> bugVecList = MatrixUtil.loadVectors(bugVecFilePath, Config.getInstance().getDicSize());
		HashMap<String, Matrix> codeVecList = MatrixUtil.loadVectors(codeVecFilePath, Config.getInstance().getDicSize());
		String []bugIDArray=bugVecList.keySet().toArray(new String[0]);
		String []codeClassArray=codeVecList.keySet().toArray(new String[0]);
		Matrix simMat = MatrixUtil.computeSimilarityMatrix(bugVecList, Config.getInstance().getDicSize());
		Matrix scoreMat=new Matrix(bugVecList.size(),codeVecList.size());
		for(int i=0;i<bugVecList.size();i++){
			for(int j=0;j<codeVecList.size();j++){
				HashMap<String,Integer> idNumPairs=BugFeatureExtractor.getPastBugsContainingTargetFile(codeClassArray[j], bugIDArray[i], bugList);			
				double simiScore=0.0d;
				for(Entry<String, Integer> onePair:idNumPairs.entrySet()){
					int index=MatrixUtil.getIndex(onePair.getKey(),bugIDArray);
					simiScore+=(simMat.get(index, i)+0.0d)/(onePair.getValue()+0.0d);
				}
				scoreMat.set(i, j, simiScore);
			}
		}
		MatrixUtil.exportMatrix(bugVecList, codeVecList, scoreMat, scoreMatFilePath);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}

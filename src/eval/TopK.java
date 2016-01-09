package eval;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import config.Config;
import feature.VSMScore;
import bug.BugDataProcessor;
import bug.BugRecord;
import Jama.Matrix;
import sourcecode.CodeDataProcessor;
import sourcecode.SourceCodeCorpus;
import utils.FileUtils;
import utils.MatrixUtil;

public class TopK extends Evaluation{
	private int _k;
	
	/**
	 * Construction function
	 */
	public TopK(){
		super();
		this._k=-1;
	}
	
	/**
	 * Construction function with a parameter k
	 * @param k
	 */
	public TopK(int k){
		super();
		this._k=k;
	}
	
	/**
	 * Set the parameter k
	 * @param k
	 */
	public void setK(int k){
		this._k=k;
	}
	
	/**
	 * Get the parameter k
	 * @return
	 */
	public int getK(){
		return this._k;
	}
	
	/**
	 * Set the oracle by path
	 * @param srcFilePath
	 * @throws Exception
	 */
	public void setOracle(String srcFilePath) throws Exception{
		super.set(srcFilePath);
	}
	
	/**
	 * Set the oracle
	 * @param oracles
	 */
	public void setOracle(HashMap<String, TreeSet<String>> oracles){
		super.set(oracles);
	}
	
	/**
	 * Obtain the performance under the topK metric
	 * The number of bugs whose associated files are ranked 
	 * in the topK of the returned results
	 * as compared to the oracles
	 * @throws Exception 
	 */
	public double evaluate(String srcFilePath) throws Exception {
		String evalDirPath=Config.getInstance().getEvaluationDir();
		String evalFilePath=Paths.get(evalDirPath, "topk").toString();
		if(new File(evalFilePath).isFile()){
			new File(evalFilePath).delete();
		}
		HashMap<String, TreeSet<String>> oracles=super.get();
		ArrayList<String> bugIdList=new ArrayList<String>();
		ArrayList<String> codeClassList=new ArrayList<String>();
		Matrix scoreMat= MatrixUtil.importSimilarityMatrix(bugIdList, codeClassList, srcFilePath);
		String [] bugIDArray= bugIdList.toArray(new String[0]);
		String [] codeClassArray = codeClassList.toArray(new String[0]);
		int numInTopK=0;
		for(int i=0; i<scoreMat.getRowDimension(); i++){
			String oneBugID=bugIDArray[i];
			
			TreeSet<String> fixedFileSet= oracles.get(oneBugID);
			ArrayList<Integer> indexSet = super.getIndexSet(fixedFileSet, codeClassArray);
			if(MatrixUtil.isInTopK(indexSet, i, scoreMat, getK())){
				numInTopK++;
				FileUtils.write_append2file(oneBugID+"\n", evalFilePath);
			}
		}
		// TODO Auto-generated method stub
		return numInTopK;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//bug
//		String XMLFilePath="C:/Users/ql29/Documents/EClipse/Dataset/AspectJBugRepository.xml";
//		String bugCorpusDirPath="C:/Users/ql29/Documents/EClipse/bugCorpus_aspectj";
//		ArrayList<BugRecord> bugList= BugDataProcessor.importFromXML();
//		BugDataProcessor.createBugCorpus(bugList, bugCorpusDirPath);
		
//		//code
//		String codeDirPath="C:/Users/ql29/Documents/EClipse/Dataset/aspectj";
//		String codeCorpusDirPath="C:/Users/ql29/Documents/EClipse/codeCorpus_aspectj";
//		SourceCodeCorpus corpus=CodeDataProcessor.extractCodeData(codeDirPath, "java", 800);
//		CodeDataProcessor.exportCodeData(codeCorpusDirPath, corpus);
		
		
//		vector and VSM similarities
//		String bugCorpusPath= Paths.get(bugCorpusDirPath,"information").toString();
//		String codeCorpusPath=Paths.get(codeCorpusDirPath,"codeContentCorpus").toString();
//		String bugVecFilePath= Paths.get(bugCorpusDirPath,"vectors").toString(); 
//		String codeVecFilePath= Paths.get(codeCorpusDirPath,"vectors").toString(); 
//		String simMatFilePath="C:/Users/ql29/Documents/EClipse/simScore_aspectj";
//		String oracleFilePath=Paths.get(bugCorpusDirPath,"fixedFiles").toString();
//		VSMScore.generate(bugCorpusPath, codeCorpusPath, bugVecFilePath, codeVecFilePath, simMatFilePath);
//		TopK topK=new TopK(10);
//		topK.setOracle(oracleFilePath);
//		double result=topK.evaluate(simMatFilePath);
//		System.out.println(result);
	}



}

package eval;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import config.Config;
import utils.FileUtils;
import utils.MatrixUtil;
import Jama.Matrix;

public class MRR extends Evaluation{
	public MRR(){
		super();
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
	 * Obtain the performance under the MRR metric
	 * @throws Exception 
	 */
	public double evaluate(String srcFilePath) throws Exception {
		String evalDirPath=Config.getInstance().getEvaluationDir();
		String evalFilePath=Paths.get(evalDirPath, "mrr").toString();
		if(new File(evalFilePath).isFile()){
			new File(evalFilePath).delete();
		}
		HashMap<String, TreeSet<String>> oracles=super.get();
		ArrayList<String> bugIdList=new ArrayList<String>();
		ArrayList<String> codeClassList=new ArrayList<String>();
		Matrix scoreMat= MatrixUtil.importSimilarityMatrix(bugIdList, codeClassList, srcFilePath);
		String [] bugIDArray= bugIdList.toArray(new String[0]);
		String [] codeClassArray = codeClassList.toArray(new String[0]);
		double MRRValue=0.0d;
		
		//traverse every bug
		//calculate the reciprocal rank
		//and obtain the average value
		for(int i=0; i<scoreMat.getRowDimension(); i++){
			
			//get the indexSet of the oracles
			String oneBugID=bugIDArray[i];
			TreeSet<String> fixedFileSet= oracles.get(oneBugID);
			ArrayList<Integer> indexSet = super.getIndexSet(fixedFileSet, codeClassArray);
			
			//obtain the RR value, which is 1/rank_1
			SortedSet<Integer> rankSet = new TreeSet<Integer>();
			for(int index:indexSet){
				int rank=MatrixUtil.getRank(i,index,scoreMat);
				rankSet.add(rank);
			}
			MRRValue+=1/(rankSet.first()+0.0);
			FileUtils.write_append2file(oneBugID+"\t"+1/(rankSet.first()+0.0)+"\n",evalFilePath);
		}
		
		// TODO Auto-generated method stub
		return MRRValue/(bugIdList.size()+0.0d);
	}
}

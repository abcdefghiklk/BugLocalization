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

public class MAP extends Evaluation{
	public MAP(){
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
	 * Obtain the performance under the MAP metric
	 * @throws Exception 
	 */
	public double evaluate(String srcFilePath) throws Exception {
		String evalDirPath=Config.getInstance().getEvaluationDir();
		String evalFilePath=Paths.get(evalDirPath, "map").toString();
		if(new File(evalFilePath).isFile()){
			new File(evalFilePath).delete();
		}
		HashMap<String, TreeSet<String>> oracles=super.get();
		ArrayList<String> bugIdList=new ArrayList<String>();
		ArrayList<String> codeClassList=new ArrayList<String>();
		Matrix scoreMat= MatrixUtil.importSimilarityMatrix(bugIdList, codeClassList, srcFilePath);
		String [] bugIDArray= bugIdList.toArray(new String[0]);
		String [] codeClassArray = codeClassList.toArray(new String[0]);
		double sumPrecision=0.0d;
		
		//traverse every bug
		//calculate the average precision
		//and obtain the average value of all the average precisions
		for(int i=0; i<scoreMat.getRowDimension(); i++){
			
			//get the indexSet of the oracles
			String oneBugID=bugIDArray[i];
			TreeSet<String> fixedFileSet= oracles.get(oneBugID);
			ArrayList<Integer> indexSet = super.getIndexSet(fixedFileSet, codeClassArray);
			
			
			//get the rank set of the oracles
			//i.e. the ranks for the actual fixed docs 
			//The ranks are in ascending order
			SortedSet<Integer> rankSet = new TreeSet<Integer>();
			for(int index:indexSet){
				int rank=MatrixUtil.getRank(i,index,scoreMat);
				rankSet.add(rank);
			}
			
			//The average precision is computed as:
			//(1/rank_1+2/rank_2+...+n/rank_n)/n
			int oracleCount=0;
			double precision=0.0d;
			for(int rank:rankSet){
				oracleCount+=1;
				precision+=(oracleCount+0.0d)/(rank+0.0d);
			}
			sumPrecision+=precision/(oracleCount+0.0d);
			FileUtils.write_append2file(oneBugID+"\t"+precision+"\n", evalFilePath);
		}
		// TODO Auto-generated method stub
		
		return sumPrecision/(bugIdList.size()+0.0d);
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}

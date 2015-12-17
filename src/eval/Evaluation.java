package eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import bug.BugFeatureExtractor;

/**
 * The Class for Evaluation
 * Provides interfaces for every metrics
 * All other metrics inherits the oracles that are compared to
 * And implements the evaluate(String srcFilePath) that generates the evaluation result
 * @author Qiuchi
 *
 */
abstract public class Evaluation {
	private HashMap<String, TreeSet<String>> _oracles;
	
	public Evaluation(){
		_oracles=new HashMap<String, TreeSet<String>>();
	}
	
	public Evaluation(HashMap<String, TreeSet<String>> oracles){
		this._oracles=oracles;
	}
	
	public Evaluation(String srcFilePath) throws Exception{
		this._oracles=BugFeatureExtractor.extractFixedFiles(srcFilePath);
	}
	
	public HashMap<String, TreeSet<String>> get(){
		return this._oracles;
	};
	
	public void set(HashMap<String, TreeSet<String>> oracles){
		this._oracles=oracles;
	}
	
	public void set(String srcFilePath) throws Exception{
		this._oracles=BugFeatureExtractor.extractFixedFiles(srcFilePath);
	}
	
	/**
	 * Obtain the index set of a given string set in the string array
	 * @param targetSet
	 * @param array
	 * @return
	 */
	public static ArrayList<Integer> getIndexSet(TreeSet<String> targetSet, String []array){
		ArrayList<Integer> indexSet=new ArrayList<Integer>();
		for(int i=0;i< array.length;i++){
//			if(targetSet.contains(array[i])){
//				indexSet.add(i);
//			}
			//for aspectJ, the fixed file name contains a longer path than the class path for source code file
			//e.g. org.aspectj.modules.weaver.src.org.aspectj.weaver.patterns.KindedPointcut.java(fixed file)
			//	   								  org.aspectj.weaver.patterns.KindedPointcut.java(full class path)
			//for other project, the fixed file name is the same as the class path for source code file
			for(String oneItem:targetSet){
				if(oneItem.contains(array[i])){
					indexSet.add(i);
					break;
				}
			}
		}
		return indexSet;
	}
	abstract public double evaluate(String srcFilePath) throws Exception; 

}

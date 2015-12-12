package eval;

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
	abstract double evaluate(String srcFilePath) throws Exception; 

}

package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	//global configuration	
	private String _datasetDir; //The directory containing the dataset
	
	private String _bugLogFile; //The path of the XML file containing bug log information

	private String _intermediateDir; //The directory containing intermediate files
	
	private String _outputFile; // The file recording the experimental results
	
	//configuration for bugs
	private int _bugReportCount; //The number of bug reports/bugs
	
	private int _bugTermCount; //The number of terms for bugs
	
	//configuration for codes
	private String _fileType; //The type of source code files 
	
	private int _segmentationLength; //The segmentation length
	
	private int _fileCount; //The number of files after segmentation by length of 800
	
	private int _originalfilecount; //The number of original files(before segmentation)
	
	private int _codeTermCount; //The number of terms for codes

	//features
	
	//VSM (has to be used)
	private int _dicSize; //The merged dictionary size
	
	private String _vectorType; //The type of vectors, namely TFIDF, TF or IDF
	
	private int _minCutoffFrequency; //The minimum cutoff frequency for dictionary pruning
	
	private int _maxCutoffFrequency; //The maximum cutoff frequency for dictionary pruning
	
	//Source Code File Length(Optional)
	private boolean _revisedUsed; //Whether the revised SVM is used, i.e. whether the length of source code file is considered
	
	private String _transformationFunctionType; //The type of transformation function
	
	//SimiScore(Optional)
	private boolean _simiScoreUsed; //Whether the simiScore is adapted
	
	private double _alpha; //Linear Interpolation Parameter in [0,1]
	
	//
	
	//evaluation
	private boolean _MAPUsed; //Whether the MAP metric is used

	private boolean _topKUsed; //Whether the TopK metric is used
	
	private int _k;	//The parameter of K for TopK metric
	
	private boolean _MRRUsed; //Whether the MRR metric is used
	
	/**
	 * Construction function
	 */
	public Config(){
		this._datasetDir=new String();
		this._bugLogFile=new String();
		this._intermediateDir=new String();
		this._outputFile=new String();
		this._bugReportCount=0;
		this._bugTermCount=0;
		this._fileCount=0;
		this._originalfilecount=0;
		this._codeTermCount=0;
		this._dicSize=0;
		this._minCutoffFrequency=0;
		this._maxCutoffFrequency=0;
		
		
		
		//default parameters
		this._fileType="java";
		this._segmentationLength=800;
		this._vectorType= "TFIDF";
		
		//by default, only the primitive VSM is considered
		this._revisedUsed=false;
		this._transformationFunctionType="logistic";
		this._simiScoreUsed=false;
		this._alpha=0.5d;
		
		//by default, only the Top5 metrics is used for evaluation
		this._MAPUsed=false;
		this._topKUsed=true;
		this._k=5;
		this._MRRUsed=false;
	}
	
	/**
	 * Set the input and output paths
	 * @param datasetDir
	 * @param bugLogPath
	 * @param intermediateDirPath
	 * @param outputFile
	 */
	public void setPaths(String datasetDir, String bugLogPath, String intermediateDirPath, String outputFile){
		this._datasetDir=datasetDir;
		this._bugLogFile=bugLogPath;
		this._intermediateDir=intermediateDirPath;
		this._outputFile=outputFile;
	}
	
	/**
	 * Set the features used 
	 * @param revisedUsed
	 * @param simiScoreUsed
	 */
	public void setFeatures(boolean revisedUsed, boolean simiScoreUsed, double alpha){
		this._revisedUsed=revisedUsed;
		this._simiScoreUsed=simiScoreUsed;
		this._alpha=alpha;
	}
	
	/**
	 * Set the evaluations used
	 * @param MAPUsed
	 * @param topKUsed
	 * @param k
	 * @param MRRUsed
	 */
	public void setEvaluations(boolean MAPUsed, boolean topKUsed, int k, boolean MRRUsed){
		this._MAPUsed=MAPUsed;
		this._topKUsed=topKUsed;
		this._k=k;
		this._MRRUsed=MRRUsed;
	}
	
	/**
	 * Export the configurations to a file
	 * @param configFilePath
	 * @throws IOException
	 */
	public void exportConfig(String configFilePath) throws IOException{
		Properties pro=new Properties();
		pro.setProperty("datasetDir", this._datasetDir);
		pro.setProperty("bugLogFile", this._bugLogFile);
		pro.setProperty("intermediateDir", this._intermediateDir);
		pro.setProperty("outputFile", this._outputFile);
		pro.setProperty("bugReportCount", String.valueOf(this._bugReportCount));
		pro.setProperty("bugTermCount", String.valueOf(this._bugTermCount));
		pro.setProperty("fileType", this._fileType);
		pro.setProperty("segmentationLength", String.valueOf(this._segmentationLength));
		pro.setProperty("fileCount", String.valueOf(this._fileCount));
		pro.setProperty("originalFileCount", String.valueOf(this._originalfilecount));
		pro.setProperty("codeTermCount", String.valueOf(this._codeTermCount));
		pro.setProperty("dicSize", String.valueOf(this._dicSize));
		pro.setProperty("vectorType", this._vectorType);
		pro.setProperty("minCutoffFrequence", String.valueOf(this._minCutoffFrequency));
		pro.setProperty("maxCutoffFrequency", String.valueOf(this._maxCutoffFrequency));
		pro.setProperty("revisedUsed", String.valueOf(this._revisedUsed));
		pro.setProperty("transformationFunctionType", this._transformationFunctionType);
		pro.setProperty("simiScoreUsed", String.valueOf(this._simiScoreUsed));
		pro.setProperty("alpha", String.valueOf(this._alpha));
		pro.setProperty("MAPUsed", String.valueOf(this._MAPUsed));
		pro.setProperty("topKUsed", String.valueOf(this._topKUsed));
		pro.setProperty("k", String.valueOf(this._k));
		pro.setProperty("MRRUsed", String.valueOf(this._MRRUsed));
		pro.store(new FileWriter(configFilePath), null);
	}
	
	public void importConfig(String configFilePath) throws Exception{
		Properties pro=new Properties();
		pro.load(new FileReader(configFilePath));
		if(pro.containsKey("datasetDir")){
			this._datasetDir=pro.getProperty("datasetDir");
		}
		if(pro.containsKey("bugLogFile")){
			this._bugLogFile=pro.getProperty("bugLogFile");
		}
		if(pro.containsKey("intermediateDir")){
			this._intermediateDir=pro.getProperty("intermediateDir");
		}
		if(pro.containsKey("outputFile")){
			this._outputFile=pro.getProperty("outputFile");
		}
		if(pro.containsKey("bugReportCount")){
			this._bugReportCount=Integer.parseInt(pro.getProperty("bugReportCount"));
		}
		if(pro.containsKey("bugTermCount")){
			this._bugTermCount=Integer.parseInt(pro.getProperty("bugTermCount"));
		}
		if(pro.containsKey("fileType")){
			this._fileType=pro.getProperty("fileType");
		}
		if(pro.containsKey("segmentationLength")){
			this._segmentationLength=Integer.parseInt(pro.getProperty("segmentationLength"));
		}
		if(pro.containsKey("fileCount")){
			this._fileCount=Integer.parseInt(pro.getProperty("fileCount"));
		}
		if(pro.containsKey("originalFileCount")){
			this._originalfilecount=Integer.parseInt(pro.getProperty("originalFileCount"));
		}
		if(pro.containsKey("codeTermCount")){
			this._codeTermCount=Integer.parseInt(pro.getProperty("codeTermCount"));
		}
		if(pro.containsKey("dicSize")){
			this._dicSize=Integer.parseInt(pro.getProperty("dicSize"));
		}
		if(pro.containsKey("vectorType")){
			this._vectorType=pro.getProperty("vectorType");
		}
		if(pro.containsKey("minCutoffFrequency")){
			this._minCutoffFrequency=Integer.parseInt(pro.getProperty("minCutoffFrequency"));
		}
		if(pro.containsKey("maxCutoffFrequency")){
			this._maxCutoffFrequency=Integer.parseInt(pro.getProperty("maxCutoffFrequency"));
		}
		if(pro.containsKey("revisedUsed")){
			this._revisedUsed=Boolean.parseBoolean(pro.getProperty("revisedUsed"));
		}
		if(pro.containsKey("transformationFunctionType")){
			this._transformationFunctionType=pro.getProperty("transformationFunctionType");
		}
		if(pro.containsKey("simiScoreUsed")){
			this._simiScoreUsed=Boolean.parseBoolean(pro.getProperty("simiScoreUsed"));
		}
		if(pro.containsKey("alpha")){
//			this._alpha=Double.parseDouble()
		}
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String propertyFilePath="C:/Users/ql29/Documents/EClipse/property";
		Config config=new Config();
		
		config.exportConfig(propertyFilePath);
	}

}

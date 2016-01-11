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
	private String _bugCorpusDir; //The bug corpus directory path
	
	private int _bugReportCount; //The number of bug reports/bugs
	
	//configuration for codes
	private String _codeCorpusDir; //The code corpus directory path
	
	private String _fileType; //The type of source code files 
	
	private int _segmentationLength; //The segmentation length
	
	private int _segmentCount; //The number of segments after segmentation
	
	private int _fileCount; //The number of original files(before segmentation)

	//features
	private String _featuresDir; //The directory path for features
	
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
	
	
	//evaluation
	private String _evaluationDir; //The directory path for evaluation results
	
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
		
		this._bugCorpusDir=new String();
		this._bugReportCount=0;
		
		this._codeCorpusDir=new String();
		this._segmentCount=0;
		this._fileCount=0;

		this._featuresDir=new String();
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
		this._evaluationDir=new String();
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
	 * Set the bug corpus directory
	 * @param bugCorpusDir
	 */
	public void setBugCorpusDir(String bugCorpusDir){
		this._bugCorpusDir=bugCorpusDir;
	}
	
	/**
	 * Set the bug report count
	 * @param bugReportCount
	 */
	public void setBugReportCount(int bugReportCount){
		this._bugReportCount=bugReportCount;
	}
	
	/**
	 * Set the code corpus directory
	 * @param codeCorpusDir
	 */
	public void setCodeCorpusDir(String codeCorpusDir){
		this._codeCorpusDir=codeCorpusDir;
	}
	
	/**
	 * Set source code file type
	 * @param fileType
	 */
	public void setFileType(String fileType){
		this._fileType=fileType;
	}
	
	/**
	 * Set the segmentation length
	 * @param segmentationLength
	 */
	public void setSegmentationLength(int segmentationLength){
		this._segmentationLength=segmentationLength;
	}
	
	/**
	 * Set source code file count
	 * @param count
	 */
	public void setFileCount(int count){
		this._fileCount=count;
	}
	
	/**
	 * Set the segment count
	 * @param count
	 */
	public void setSegmentCount(int count){
		this._segmentCount=count;
	}
	
	/**
	 * Set the dictionary size
	 * @param dicSize
	 */
	public void setDicSize(int dicSize){
		this._dicSize=dicSize;
	}
	
	/**
	 * Set the features used 
	 * @param featuresDir
	 * @param revisedUsed
	 * @param simiScoreUsed
	 */
	public void setFeatures(String featuresDir, boolean revisedUsed, boolean simiScoreUsed, double alpha){
		this._featuresDir=featuresDir;
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
	public void setEvaluations(String evaluationDir, boolean MAPUsed, boolean topKUsed, int k, boolean MRRUsed){
		this._evaluationDir=evaluationDir;
		this._MAPUsed=MAPUsed;
		this._topKUsed=topKUsed;
		this._k=k;
		this._MRRUsed=MRRUsed;
	}
	
	/**
	 * Get dataset directory path
	 * @return
	 */
	public String getDatasetDir(){
		return(this._datasetDir);
	}
	
	/**
	 * Get bug log file path
	 * @return
	 */
	public String getBugLogFile(){
		return(this._bugLogFile);
	}
	
	/**
	 * Get the directory path for intermediate files
	 * @return
	 */
	public String getIntermediateDir(){
		return(this._intermediateDir);
	}
	
	/**
	 * Get the output file path
	 * @return
	 */
	public String getOutputFile(){
		return(this._outputFile);
	}
	
	/**
	 * Get the bug corpus directory path
	 * @return
	 */
	public String getBugCorpusDir(){
		return(this._bugCorpusDir);
	}
	
	/**
	 * Get the code directory path
	 * @return
	 */
	public String getCodeCorpusDir(){
		return(this._codeCorpusDir);
	}
	
	/**
	 * Get the source code file type
	 */
	public String getFileType(){
		return(this._fileType);
	}
	
	/**
	 * Get the segmentation length
	 * @return
	 */
	public int getSegmentationLength(){
		return(this._segmentationLength);
	}
	/**
	 * Get the features directory path
	 * @return
	 */
	public String getFeaturesDir(){
		return(this._featuresDir);
	}
	
	/**
	 * Get the evaluation directory path
	 * @return
	 */
	public String getEvaluationDir(){
		return(this._evaluationDir);
	}
	
	
	/**
	 * Get the dictionary size
	 * @return
	 */
	public int getDicSize(){
		return(this._dicSize);
	}
	
	/**
	 * Get whether MAP is used
	 * @return
	 */
	public boolean getMAPUsed(){
		return(this._MAPUsed);
	}
	
	/**
	 * Get whether topK is used
	 * @return
	 */
	public boolean getTopKUsed(){
		return(this._topKUsed);
	}
	
	/**
	 * Get k
	 * @return
	 */
	public int getK(){
		return(this._k);
	}
	
	/**
	 * Get whether MRR is used
	 * @return
	 */
	public boolean getMRRUsed(){
		return(this._MRRUsed);
	}
	
	/**
	 * Get the parameter alpha
	 * @return
	 */
	public double getAlpha(){
		return(this._alpha);
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
		
		pro.setProperty("bugCorpusDir", this._bugCorpusDir);
		pro.setProperty("bugReportCount", String.valueOf(this._bugReportCount));
		
		pro.setProperty("codeCorpusDir", this._codeCorpusDir);
		pro.setProperty("fileType", this._fileType);
		pro.setProperty("segmentationLength", String.valueOf(this._segmentationLength));
		pro.setProperty("fileCount", String.valueOf(this._fileCount));
		pro.setProperty("segmentCount", String.valueOf(this._segmentCount));
		
		pro.setProperty("featuresDir", this._featuresDir);
		pro.setProperty("dicSize", String.valueOf(this._dicSize));
		pro.setProperty("vectorType", this._vectorType);
		pro.setProperty("minCutoffFrequence", String.valueOf(this._minCutoffFrequency));
		pro.setProperty("maxCutoffFrequency", String.valueOf(this._maxCutoffFrequency));
		pro.setProperty("revisedUsed", String.valueOf(this._revisedUsed));
		pro.setProperty("transformationFunctionType", this._transformationFunctionType);
		pro.setProperty("simiScoreUsed", String.valueOf(this._simiScoreUsed));
		pro.setProperty("alpha", String.valueOf(this._alpha));
		
		pro.setProperty("evaluationDir", this._evaluationDir);
		pro.setProperty("MAPUsed", String.valueOf(this._MAPUsed));
		pro.setProperty("topKUsed", String.valueOf(this._topKUsed));
		pro.setProperty("k", String.valueOf(this._k));
		pro.setProperty("MRRUsed", String.valueOf(this._MRRUsed));
		pro.store(new FileWriter(configFilePath), null);
	}
	
	/**
	 * Import the configuration from a file
	 * @param configFilePath
	 * @throws Exception
	 */
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
		if(pro.containsKey("fileType")){
			this._fileType=pro.getProperty("fileType");
		}
		if(pro.containsKey("segmentationLength")){
			this._segmentationLength=Integer.parseInt(pro.getProperty("segmentationLength"));
		}
		if(pro.containsKey("fileCount")){
			this._fileCount=Integer.parseInt(pro.getProperty("fileCount"));
		}
		if(pro.containsKey("segmentCount")){
			this._segmentCount=Integer.parseInt(pro.getProperty("segmentCount"));
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
			this._alpha=Double.parseDouble(pro.getProperty("alpha"));
		}
		if(pro.containsKey("MAPUsed")){
			this._MAPUsed=Boolean.parseBoolean(pro.getProperty("MAPUsed"));
		}
		if(pro.containsKey("topKUsed")){
			this._topKUsed=Boolean.parseBoolean(pro.getProperty("topKUsed"));
		}
		if(pro.containsKey("k")){
			this._k=Integer.parseInt(pro.getProperty("k"));
		}
		if(pro.containsKey("MRRUsed")){
			this._MRRUsed=Boolean.parseBoolean("MRRUsed");
		}
	}
	
	//a static object
	private static Config cfg=new Config();
	
	/**
	 * Create an instance using the input paths
	 * @param datasetDir
	 * @param bugLogPath
	 * @param intermediateDirPath
	 * @param outputFile
	 */
	private static void createInstance(String datasetDir, String bugLogPath, String intermediateDirPath, String outputFile){
		cfg.setPaths(datasetDir, bugLogPath, intermediateDirPath, outputFile);
	}
	/**
	 * Get the static object
	 * @return
	 */
	public static Config getInstance(){
		return cfg;
	}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String propertyFilePath="C:/Users/ql29/Documents/EClipse/property";
		Config.createInstance(propertyFilePath, propertyFilePath, propertyFilePath, propertyFilePath);
		Config.getInstance().setFileType("C++");
		Config.getInstance().exportConfig(propertyFilePath);

	}
}

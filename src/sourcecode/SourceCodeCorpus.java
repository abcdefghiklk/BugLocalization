package sourcecode;

import java.util.ArrayList;

import config.Config;

/**
 * Source Code Corpus is the class 
 * for the whole set of source code files 
 * in a project
 * @author Qiuchi
 *
 */
public class SourceCodeCorpus {
	
	//The list of source code files
	private ArrayList<SourceCode> _sourceCodeList;
	
	//The type of the files
	private String _fileType;
	
	//The segmentation length
	private int _segmentationLength;
	
	/**
	 * Default construction function
	 */
	public SourceCodeCorpus(){
		this._sourceCodeList=new ArrayList<SourceCode> ();
		this._fileType=new String();
		this._segmentationLength=0;
	}
	
	/**
	 * Construction function with input parameters
	 * @param codeList
	 * @param fileType
	 * @param segmentationLength
	 */
	public SourceCodeCorpus(ArrayList<SourceCode> codeList, String fileType, int segmentationLength){
		this._sourceCodeList=codeList;
		this._fileType=fileType;
		this._segmentationLength=segmentationLength;
	}
	
	/**
	 * Construction function with the fileType parameter only
	 * @param fileType
	 */
	public SourceCodeCorpus(String fileType){
		this._sourceCodeList=new ArrayList<SourceCode> ();
		this._fileType=fileType;
		this._segmentationLength=0;
	}
	
	/**
	 * Construction function with the segmentationLength parameter only
	 * @param segmentationLength
	 */
	public SourceCodeCorpus(int segmentationLength){
		_sourceCodeList=new ArrayList<SourceCode> ();
		_fileType=new String();
		this._segmentationLength=segmentationLength;
	}
	
	/**
	 * Construction function with the segmentationLength and fileType
	 * @param segmentationLength
	 * @param fileType
	 */
	public SourceCodeCorpus(int segmentationLength, String fileType){
		_sourceCodeList=new ArrayList<SourceCode> ();
		_fileType=fileType;
		this._segmentationLength=segmentationLength;
	}
	
	/**
	 * Set the file type 
	 * @param fileType
	 */
	public void setFileType(String fileType){
		this._fileType=fileType;
	}
	
	/**
	 * Get the file type
	 * @return
	 */
	public String getFileType(){
		return this._fileType;
	}
	
	/**
	 * Set the list of source code files
	 * @param codeList
	 */
	public void setSourceCodeList(ArrayList<SourceCode> codeList){
		this._sourceCodeList=codeList;
	}
	
	/**
	 * Get the list of source code files
	 * @return
	 */
	public ArrayList<SourceCode> getSourceCodeList(){
		return this._sourceCodeList;
	}
	
	/**
	 * Add an source code file to the file list
	 * @param sourceCodeFile
	 */
	public void addSourceCode(SourceCode sourceCodeFile){
		_sourceCodeList.add(sourceCodeFile);
	}
	
	/**
	 * Set the segmentation length
	 * @param segmentationLength
	 */
	public void setSegmentationLength(int segmentationLength){
		this._segmentationLength=segmentationLength;
	}
	
	/**
	 * Get the segmentation length
	 * @return
	 */
	public int getSegmentationLength(){
		return this._segmentationLength;
	}
	
	/**
	 * Segment each file in the file list using the given segmentation length
	 */
	public void segment(){
		if(this._segmentationLength==0){
			return;
		}
		int totalSegmentCount=0;
		for(SourceCode oneCodeFile: this._sourceCodeList){
			String fileContent= oneCodeFile.getContent();
			String []fileTerms= fileContent.split(" ");
			if(fileTerms.length==0){
				return;
			}
			for(int segmentCount=0; segmentCount* this._segmentationLength<fileTerms.length; segmentCount++){
				int startIndex=segmentCount* this._segmentationLength;
				int endIndex=Math.min((segmentCount+1)* _segmentationLength, fileTerms.length);
				String segmentString=new String();
				for(int i=startIndex; i<endIndex;i++){
					segmentString+=fileTerms[i]+" ";
				}
				totalSegmentCount++;
				oneCodeFile.addCodeSegment(segmentString.trim());
			}
		}
		Config.getInstance().setSegmentCount(totalSegmentCount);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

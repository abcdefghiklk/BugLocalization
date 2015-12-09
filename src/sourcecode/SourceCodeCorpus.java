package sourcecode;

import java.util.ArrayList;

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
	
	/**
	 * Default construction function
	 */
	public SourceCodeCorpus(){
		_sourceCodeList=new ArrayList<SourceCode> ();
		_fileType=new String();
	}
	
	/**
	 * Construction function with input parameters
	 * @param codeList
	 * @param fileType
	 */
	public SourceCodeCorpus(ArrayList<SourceCode> codeList, String fileType){
		this._sourceCodeList=codeList;
		this._fileType=fileType;
	}
	
	/**
	 * Construction function with the fileType parameter only
	 * @param fileType
	 */
	public SourceCodeCorpus(String fileType){
		this._sourceCodeList=new ArrayList<SourceCode> ();
		this._fileType=fileType;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

package sourcecode;

import java.util.ArrayList;

/**
 * Class Source Code is the class for a source code file
 * @author Qiuchi Li
 *
 */
public class SourceCode {
	//full class name for file
	private String _fullClassName; 
	
	//file content
	private String _content;
	
	//all class names in the file
	private ArrayList<String> _classNameList;
	
	//all method names in the file
	private ArrayList<String> _methodNameList;
	
	//code segment list by file segmentation
	private ArrayList<String> _codeSegmentList;
	
	
	/**
	 * Default construction function
	 */
	SourceCode(){
		this._fullClassName = new String();
		this._content = new String();
		this._classNameList = new ArrayList<String>();
		this._methodNameList = new ArrayList<String>();
		this._codeSegmentList = new ArrayList<String>();
	}
	
	/**
	 * Construction function with input parameter
	 * @param fullClassName
	 * @param filePath
	 * @param content
	 */
	SourceCode(String fullClassName, String filePath, String content){
		this._fullClassName=fullClassName;
		this._content=content;
		this._classNameList = new ArrayList<String>();
		this._methodNameList = new ArrayList<String>();
		this._codeSegmentList = new ArrayList<String>();
	}
	
	/**
	 * Get the full class name
	 * @return
	 */
	public String getFullClassName() {
		return this._fullClassName;
	}
	
	/**
	 * Set the full class name
	 * @param fullClassName
	 */
	public void setFullClassName(String fullClassName) {
		this._fullClassName = fullClassName;
	}
	
	/**
	 * Get the file content
	 * @return
	 */
	public String getContent() {
		return this._content;
	}
	
	/**
	 * Set the file content
	 * @param content
	 */
	public void setContent(String content) {
		this._content = content;
	}
	
	/**
	 * Add a class name to the class name list
	 * @param className
	 */
	public void addClassName(String className){
		this._classNameList.add(className);
	}
	
	/**
	 * Get the class name list
	 * @return
	 */
	public ArrayList<String> getClassNameList(){
		return this._classNameList;
	}
	
	/**
	 * Add a method name to the method name list
	 * @param methodName
	 */
	public void addMethodName(String methodName){
		this._methodNameList.add(methodName);
	}
	
	/**
	 * Get the method name list
	 * @return
	 */
	public ArrayList<String> getMethodNameList(){
		return this._methodNameList;
	}
	
	/**
	 * Add a code segment to the segment list
	 * @param codeSegment
	 */
	public void addCodeSegment(String codeSegment){
		this._codeSegmentList.add(codeSegment);
	}
	
	/**
	 * Get the code segment list
	 * @return
	 */
	public ArrayList<String> getCodeSegmentList(){
		return this._codeSegmentList;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

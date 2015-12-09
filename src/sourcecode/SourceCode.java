package sourcecode;

/**
 * Class Source Code is the class for a source code file
 * @author Qiuchi Li
 *
 */
public class SourceCode {
	//full class name for file
	private String _fullClassName; 
	
	//file path
	private String _filePath;
	
	//file content
	private String _content;
	
	/**
	 * Default construction function
	 */
	SourceCode(){
		this._fullClassName=new String();
		this._filePath=new String();
		this._content=new String();
	}
	
	/**
	 * Construction function with input parameter
	 * @param fullClassName
	 * @param filePath
	 * @param content
	 */
	SourceCode(String fullClassName, String filePath, String content){
		this._fullClassName=fullClassName;
		this._filePath=filePath;
		this._content=content;
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
	 * Get the file path
	 * @return
	 */
	public String getFilePath() {
		return this._filePath;
	}
	
	/**
	 * Set the file path
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this._filePath = filePath;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

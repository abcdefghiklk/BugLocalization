package config;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
	public String versionLogPath; //The path of the XML file containing version log information
	
	public String sourceCodeDir; //The directory of the project
	
	public String intermediateDir; //The directory containing intermediate files
	
	public int fileCount; //The number of files after segmentation by length of 800
	
	public int wordCount; //The number of words in bugs
	
	public int bugReportCount;// The number of bug reports/bugs
	
	public int bugTermCount; // The number of words in the bug reports
	
	public float alpha; // linear parameter alpha
	
	public String outputFile; // The file recording the experimental results

	public int originfilecount; // The number of original files(before segmentation)
	
    public String project; // The project of the experiment
    
    public int aspectj_filename_offset; // The offset for aspectj filename
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Properties pro=new Properties();
		pro.load(new FileInputStream("C:/Users/dell/Documents/EClipse/BRTracer/src/config.properties"));
		String value=pro.getProperty("dbPort");
		System.out.println(value);
	}

}

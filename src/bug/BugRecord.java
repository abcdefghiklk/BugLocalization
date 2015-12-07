package bug;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;
/**
 * 
 * @author ql29
 * BugRecord class
 * contains all the information of a bug
 * extracted from the bug reports
 */
public class BugRecord {
	String bugId;// bug ID
	
	Date openDate = new Date();// open date for the bug
	
	Date fixDate = new Date();//fix date for the bug
	
	String bugSummary;//bug summary
	
	String bugDescription;//bug description
	
	TreeSet<String> fixedFileSet = new TreeSet<String>(); //fixed files

	public String getBugId() {
		return bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	
	public void setOpenDate(String openDate) throws Exception{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.openDate=format.parse(openDate);
	}
	
	public Date getFixDate() {
		return fixDate;
	}
	
	public void setFixDate(String fixDate) throws Exception{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.fixDate=format.parse(fixDate);
	}
	
	public void setFixDate(Date fixDate) {
		this.fixDate = fixDate;
	}

	public String getBugSummary() {
		return bugSummary;
	}

	public void setBugSummary(String bugSummary) {
		this.bugSummary = bugSummary;
	}

	public String getBugDescription() {
		return bugDescription;
	}

	public void setBugDescription(String bugDescription) {
		this.bugDescription = bugDescription;
	}

	public TreeSet<String> getFixedFileSet() {
		return fixedFileSet;
	}

	public void addFixedFile(String fileName) {
		if(!this.fixedFileSet.contains(fileName)){
			this.fixedFileSet.add(fileName);		
		}
	}
}

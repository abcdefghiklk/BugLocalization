package bug;

import java.util.TreeSet;

public class Bug {

	String bugId;// bug ID
	
	String openDate;// open date for the bug
	
	String fixDate;//fix date for the bug
	
	String bugSummary;//bug summary
	
	String bugDescription;//bug description
	
	TreeSet<String> fixedFileSet = new TreeSet<String>(); //fixed files

	public String getBugId() {
		return bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getFixDate() {
		return fixDate;
	}

	public void setFixDate(String fixDate) {
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

	public TreeSet<String> getSet() {
		return fixedFileSet;
	}

	public void addFixedFile(String fileName) {
		this.fixedFileSet.add(fileName);
	}

}

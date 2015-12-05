package bug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import utils.Splitter;
import utils.Stem;
import utils.Stopword;

/**
 * 
 * @author Qiuchi Li
 * Extract features from the bug report
 * All bug features are stored as a hashmap object <bugID, Feature>
 * Provide features <---> BugRecord class interfaces
 * and features <---> bugCorpus interfaces
 */
public class BugFeatureExtractor {

	
	
	/**
	 * Extract <bug, information> pairs from the BugRecord List
	 * @param BugList
	 * @return 
	 */
	public static HashMap <String, String> extractBugInformation(ArrayList<BugRecord> bugList){
		HashMap <String, String> idInformationPairs=new HashMap<String, String>();
		for (BugRecord _bug:bugList){
			idInformationPairs.put(_bug.getBugId(), splitAndStem(_bug.getBugSummary()+" "+_bug.getBugDescription()));
		}
		return idInformationPairs;
	}
	
	/**
	 * Extract <bug, information> pairs from directory
	 * @param targetDirPath
	 * @return
	 * @throws Exception 
	 */
	public static HashMap <String, String> extractBugInformation(String srcDirPath) throws Exception{
		HashMap <String, String> idInformationPairs=new HashMap<String, String>();
		File srcDir=new File(srcDirPath);
		if(!srcDir.isDirectory()){
			System.out.println("the Input directory path is wrong!");
			return idInformationPairs;
		}
		
		for (File file:srcDir.listFiles()){
			String bugID=file.toString();
			FileReader reader = new FileReader(file);
			String information="";
			int c=reader.read();
			while(c!=-1){
				information+=(char)c;
				c=reader.read();
			}
			reader.close();
			if (!idInformationPairs.containsKey(bugID)){
				idInformationPairs.put(bugID, information);
			}
		}

		return idInformationPairs;
	}
	
	/**
	 * Export <bug, information> pairs to target directory
	 * @param idInformationPairs
	 * @param dstDirPath
	 * @throws IOException 
	 */
	public static void exportBugInformation(HashMap <String, String> idInformationPairs, String dstDirPath) throws IOException{
		File dstDir = new File(dstDirPath);
		if(!dstDir.isDirectory()){
			dstDir.mkdir();
		}
		for(Entry<String, String> idInfoPair : idInformationPairs.entrySet()){
			FileWriter writer = new FileWriter(Paths.get(dstDirPath, idInfoPair.getKey()).toFile());
			writer.write(idInfoPair.getValue());
			writer.close();
		}
	}
	

	
	
	

	/**
	 * Extract <bug, summary> pairs from BugRecord List
	 * @param BugList
	 * @return
	 */
	public static HashMap <String, String> extractBugSummary(ArrayList<BugRecord> bugList){
		HashMap <String, String> idSummaryPairs=new HashMap<String, String>();
		for (BugRecord _bug:bugList){
			idSummaryPairs.put(_bug.getBugId(), splitAndStem(_bug.getBugSummary()));
		}
		return idSummaryPairs;
	}
	
	/**
	 * Extract <bug, summary> pairs from directory
	 * @param targetDirPath
	 * @return
	 * @throws Exception 
	 */
	public static HashMap <String, String> extractBugSummary(String srcDirPath) throws Exception{
		HashMap <String, String> idSummaryPairs=new HashMap<String, String>();
		File srcDir=new File(srcDirPath);
		if(!srcDir.isDirectory()){
			System.out.println("the Input directory path is wrong!");
			return idSummaryPairs;
		}
		
		for (File file:srcDir.listFiles()){
			String bugID=file.toString();
			FileReader reader = new FileReader(file);
			String information="";
			int c=reader.read();
			while(c!=-1){
				information+=(char)c;
				c=reader.read();
			}
			reader.close();
			if (!idSummaryPairs.containsKey(bugID)){
				idSummaryPairs.put(bugID, information);
			}
		}
		return idSummaryPairs;
	}
	
	/**
	 * Export <bug, summary> pairs to BugRecord list
	 * @param idSummaryPairs
	 * @param bugList
	 */
	public static void exportBugSummary(HashMap <String, String> idSummaryPairs, ArrayList<BugRecord> bugList){
		
	}
	
	/**
	 * Export <bug, summary> pairs to target directory
	 * @param idSummaryPairs
	 * @param dstDirPath
	 */
	public static void exportBugSummary(HashMap <String, String> idSummaryPairs, String dstDirPath){
		
	}
	
	
	
	
	
	/**
	 * Extract <bug, description> pairs from BugRecord List
	 * @param BugList
	 * @return
	 */
	public static HashMap <String, String> extractBugDescription(ArrayList<BugRecord> bugList){
		HashMap <String, String> idDescriptionPairs=new HashMap<String, String>();
		for (BugRecord _bug:bugList){
			idDescriptionPairs.put(_bug.getBugId(), splitAndStem(_bug.getBugDescription()));
		}
		return idDescriptionPairs;
	}
	
	/**
	 * Extract <bug, description> pairs from directory
	 * @param targetDirPath
	 * @return
	 */
	public static HashMap <String, String> extractBugDescription(String srcDirPath){
		HashMap <String, String> idDescriptionPairs=new HashMap<String, String>();
		return idDescriptionPairs;
	}
	
	/**
	 * Export <bug, description> pairs to BugRecord list
	 * @param idDescriptionPairs
	 * @param bugList
	 */
	public static void exportBugDescription(HashMap <String, String> idDescriptionPairs, ArrayList<BugRecord> bugList){
		
	}
	
	/**
	 * Export <bug, description> pairs to target directory
	 * @param idDescriptionPairs
	 * @param dstDirPath
	 */
	public static void exportBugDescription(HashMap <String, String> idDescriptionPairs, String dstDirPath){
		
	}
	 
	
	/**
	 * split and stem a passage
	 * @param content
	 * @return
	 */
	public static String splitAndStem(String content){
		String [] terms=Splitter.splitNatureLanguage(content);
		StringBuffer buf=new StringBuffer();
		for (String oneTerm : terms) {
			oneTerm = Stem.stem(oneTerm.toLowerCase());
			if (!Stopword.isEnglishStopword(oneTerm)) {
				buf.append(oneTerm + " ");
			}
		}
		return buf.toString();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

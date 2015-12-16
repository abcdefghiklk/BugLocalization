package bug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.wordlist.WVTWordList;
import sourcecode.CodeDataProcessor;
import sourcecode.SourceCodeCorpus;
import utils.DateFormat;
import utils.Splitter;
import utils.Stopword;
import utils.WVToolWrapper;

/**
 * 
 * @author Qiuchi Li
 * Extract features from the bug report
 * All bug features are stored as a HashMap object <bugID, Feature>
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
			idInformationPairs.put(_bug.getBugId(), splitAndFilter(_bug.getBugSummary()+" "+_bug.getBugDescription()));
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
			System.out.println("the Input directory path is invalid!");
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
			idSummaryPairs.put(_bug.getBugId(), splitAndFilter(_bug.getBugSummary()));
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
			System.out.println("the Input directory path is invalid!");
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
		for (Entry <String, String> pair:idSummaryPairs.entrySet()){
			boolean isExist=false;
			for(BugRecord _bug:bugList){
				if(_bug.getBugId().equals(pair.getKey())){
					_bug.setBugSummary(pair.getValue());
					isExist=true;
					break;
				}
			}
			if(!isExist){
				BugRecord newBug=new BugRecord();
				newBug.setBugSummary(pair.getValue());
				bugList.add(newBug);
			}
		}
	}
	
	/**
	 * Export <bug, summary> pairs to target directory
	 * @param idSummaryPairs
	 * @param dstDirPath
	 * @throws IOException 
	 */
	public static void exportBugSummary(HashMap <String, String> idSummaryPairs, String dstDirPath) throws IOException{
		File dstDir = new File(dstDirPath);
		if(!dstDir.isDirectory()){
			dstDir.mkdir();
		}
		for(Entry<String, String> idSumPair : idSummaryPairs.entrySet()){
			FileWriter writer = new FileWriter(Paths.get(dstDirPath, idSumPair.getKey()).toFile());
			writer.write(idSumPair.getValue());
			writer.close();
		}
	}
	
	
	/**
	 * Extract <bug, description> pairs from BugRecord List
	 * @param BugList
	 * @return
	 */
	public static HashMap <String, String> extractBugDescription(ArrayList<BugRecord> bugList){
		HashMap <String, String> idDescriptionPairs=new HashMap<String, String>();
		for (BugRecord _bug:bugList){
			idDescriptionPairs.put(_bug.getBugId(), splitAndFilter(_bug.getBugDescription()));
		}
		return idDescriptionPairs;
	}
	
	/**
	 * Extract <bug, description> pairs from directory
	 * @param targetDirPath
	 * @return
	 * @throws Exception 
	 */
	public static HashMap <String, String> extractBugDescription(String srcDirPath) throws Exception{
		HashMap <String, String> idDescriptionPairs=new HashMap<String, String>();
		File srcDir=new File(srcDirPath);
		if(!srcDir.isDirectory()){
			System.out.println("the Input directory path is invalid!");
			return idDescriptionPairs;
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
			if (!idDescriptionPairs.containsKey(bugID)){
				idDescriptionPairs.put(bugID, information);
			}
		}
		return idDescriptionPairs;
	}
	
	/**
	 * Export <bug, description> pairs to BugRecord list
	 * @param idDescriptionPairs
	 * @param bugList
	 */
	public static void exportBugDescription(HashMap <String, String> idDescriptionPairs, ArrayList<BugRecord> bugList){
		for (Entry <String, String> pair:idDescriptionPairs.entrySet()){
			boolean isExist=false;
			for(BugRecord _bug:bugList){
				if(_bug.getBugId().equals(pair.getKey())){
					_bug.setBugDescription(pair.getValue());
					isExist=true;
					break;
				}
			}
			if(!isExist){
				BugRecord newBug=new BugRecord();
				newBug.setBugDescription(pair.getValue());
				bugList.add(newBug);
			}
		}
	}
	
	/**
	 * Export <bug, description> pairs to target directory
	 * @param idDescriptionPairs
	 * @param dstDirPath
	 * @throws Exception 
	 */
	public static void exportBugDescription(HashMap <String, String> idDescriptionPairs, String dstDirPath) throws Exception{
		File dstDir = new File(dstDirPath);
		if(!dstDir.isDirectory()){
			dstDir.mkdir();
		}
		for(Entry<String, String> idDesPair : idDescriptionPairs.entrySet()){
			FileWriter writer = new FileWriter(Paths.get(dstDirPath, idDesPair.getKey()).toFile());
			writer.write(idDesPair.getValue());
			writer.close();
		}
	}
	
	
	
	
	
	
	
	/**
	 * Extract <bug, OpenDate> pairs from the BugRecord List
	 * @param bugList
	 * @return
	 */
	public static HashMap<String, Date> extractOpenDate(ArrayList<BugRecord> bugList){
		HashMap<String, Date> idOpenDatePairs=new HashMap<String, Date>();
		for(BugRecord _bug:bugList){
			idOpenDatePairs.put(_bug.getBugId(), _bug.getOpenDate());
		}
		return idOpenDatePairs;
	}
	
	/**
	 * Extract <bug, OpenDate> pairs from file
	 * @param srcFilePath
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static HashMap<String, Date> extractOpenDate(String srcFilePath) throws IOException, Exception{
		HashMap<String, Date> idOpenDatePairs=new HashMap<String, Date>();
		if(!new File(srcFilePath).isFile()){
			System.out.println("The input file path is invalid!");
			return idOpenDatePairs;
		}
		BufferedReader reader=new BufferedReader(new FileReader(srcFilePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			String []strs=line.split("\t");
			if(strs.length==2){
				String bugID=strs[0];
				Date openDate=DateFormat.getFormat().parse(strs[1]);
				idOpenDatePairs.put(bugID, openDate);
			}
		}
		reader.close();
		return idOpenDatePairs;
	}
	
	/**
	 * Export <bug, OpenDate> pairs to Bug Record
	 * @param idOpenDatePairs
	 * @param bugList
	 */
	public static void exportOpenDate(HashMap<String, Date> idOpenDatePairs, ArrayList<BugRecord> bugList){
		for(Entry<String, Date> pair: idOpenDatePairs.entrySet()){
			boolean isExist= false;
			for(BugRecord _bug : bugList){
				if(_bug.getBugId().equals(pair.getKey())){
					_bug.setOpenDate(pair.getValue());
					isExist=true;
					break;
				}
			}
			if(!isExist){
				BugRecord newBug=new BugRecord();
				newBug.setOpenDate(pair.getValue());
				bugList.add(newBug);
			}
		}
	}
	
	/**
	 * Export <bug, OpenDate> pairs to file
	 * @param idOpenDatePairs
	 * @param dstFilePath
	 * @throws IOException
	 */
	public static void exportOpenDate(HashMap<String, Date> idOpenDatePairs, String dstFilePath) throws IOException{
		FileWriter writer=new FileWriter(dstFilePath);
		StringBuffer buf=new StringBuffer();
		for(Entry<String, Date> pair:idOpenDatePairs.entrySet()){
			buf.append(pair.getKey()+"\t"+DateFormat.getFormat().format(pair.getValue())+"\n");
		}
		writer.write(buf.toString());
		writer.close();
	}
	
	
	
	
	
	
	
	/**
	 * Extract <bug, FixDate> pairs from the BugRecord List
	 * @param bugList
	 * @return
	 */
	public static HashMap<String, Date> extractFixDate(ArrayList<BugRecord> bugList){
		HashMap<String, Date> idFixDatePairs=new HashMap<String, Date>();
		for(BugRecord _bug:bugList){
			idFixDatePairs.put(_bug.getBugId(), _bug.getOpenDate());
		}
		return idFixDatePairs;
	}
	
	/**
	 * Extract <bug, FixDate> pairs from file
	 * @param srcFilePath
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static HashMap<String, Date> extractFixDate(String srcFilePath) throws IOException, Exception{
		HashMap<String, Date> idFixDatePairs=new HashMap<String, Date>();
		if(!new File(srcFilePath).isFile()){
			System.out.println("The input file path is invalid!");
			return idFixDatePairs;
		}
		BufferedReader reader=new BufferedReader(new FileReader(srcFilePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			String []strs=line.split("\t");
			if(strs.length==2){
				String bugID=strs[0];
				Date fixDate=DateFormat.getFormat().parse(strs[1]);
				idFixDatePairs.put(bugID, fixDate);
			}
		}
		reader.close();
		return idFixDatePairs;
	}
	
	/**
	 * Export <bug, FixDate> pairs to Bug Record
	 * @param idFixDatePairs
	 * @param bugList
	 */
	public static void exportFixDate(HashMap<String, Date> idFixDatePairs, ArrayList<BugRecord> bugList){
		for(Entry<String, Date> pair: idFixDatePairs.entrySet()){
			boolean isExist= false;
			for(BugRecord _bug : bugList){
				if(_bug.getBugId().equals(pair.getKey())){
					_bug.setFixDate(pair.getValue());
					isExist=true;
					break;
				}
			}
			if(!isExist){
				BugRecord newBug=new BugRecord();
				newBug.setFixDate(pair.getValue());
				bugList.add(newBug);
			}
		}
	}
	
	/**
	 * Export <bug, FixDate> pairs to file
	 * @param idFixDatePairs
	 * @param dstFilePath
	 * @throws IOException
	 */
	public static void exportFixDate(HashMap<String, Date> idFixDatePairs, String dstFilePath) throws IOException{
		FileWriter writer=new FileWriter(dstFilePath);
		StringBuffer buf=new StringBuffer();
		for(Entry<String, Date> pair:idFixDatePairs.entrySet()){
			buf.append(pair.getKey()+"\t"+DateFormat.getFormat().format(pair.getValue())+"\n");
		}
		writer.write(buf.toString());
		writer.close();
	}
	
	
	/**
	 * Extract <bug, FixedFiles> pairs from BugRecord List
	 * @param bugList
	 * @return
	 */
	public static HashMap<String, TreeSet<String>> extractFixedFiles(ArrayList<BugRecord> bugList){
		HashMap<String, TreeSet<String>> idFixedFilesPairs=new HashMap<String, TreeSet<String>>();
		for(BugRecord _bug:bugList){
			idFixedFilesPairs.put(_bug.getBugId(), _bug.getFixedFileSet());
		}
		return idFixedFilesPairs;
	}
	
	/**
	 * Extract <bug, FixedFiles> pairs from file
	 * @param srcFilePath
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, TreeSet<String>> extractFixedFiles(String srcFilePath) throws Exception{
		HashMap<String, TreeSet<String>> idFixedFilesPairs=new HashMap<String, TreeSet<String>>();
		if(!new File(srcFilePath).isFile()){
			System.out.println("The input file path is invalid!");
			return idFixedFilesPairs;
		}
		BufferedReader reader = new BufferedReader(new FileReader(srcFilePath));
		String line=new String();
		while((line= reader.readLine())!=null){
			String []strs=line.split("\t");
			if(strs.length>1){
				String bugID=strs[0];
				TreeSet<String> fixedSet=new TreeSet<String>();
				for(int i=1; i<strs.length;i++){
					fixedSet.add(strs[i]);
				}
				idFixedFilesPairs.put(bugID, fixedSet);
			}
		}
		reader.close();
		return idFixedFilesPairs;
	}
	
	/**
	 * Export <bug, FixedFiles> pairs to BugRecord List
	 * @param idFixedFilesPairs
	 * @param bugList
	 */
	public static void exportFixedFiles(HashMap<String, TreeSet<String>> idFixedFilesPairs, ArrayList<BugRecord> bugList){
		for(Entry <String, TreeSet<String>> pair: idFixedFilesPairs.entrySet()){
			boolean isExist=false;
			for(BugRecord _bug: bugList){
				if(_bug.getBugId().equals(pair.getKey())){
					for(String oneFixedFile:pair.getValue()){
						_bug.addFixedFile(oneFixedFile);
					}
				}
				isExist=true;
				break;
			}
			if(!isExist){
				BugRecord newBug=new BugRecord();
				for(String oneFixedFile:pair.getValue()){
					newBug.addFixedFile(oneFixedFile);
				}
				bugList.add(newBug);
			}
		}
	}
	
	
	/**
	 * Export <bug, FixedFiles> pairs to file
	 * @param idFixedFilesPairs
	 * @param dstFilePath
	 * @throws IOException
	 */
	public static void exportFixedFiles(HashMap<String, TreeSet<String>> idFixedFilesPairs, String dstFilePath) throws IOException{
		FileWriter writer = new FileWriter(dstFilePath);
		StringBuffer buf = new StringBuffer();
		for(Entry <String, TreeSet<String>> pair: idFixedFilesPairs.entrySet()){
			String onePairStr=pair.getKey();
			for(String oneFixedFile:pair.getValue()){
				onePairStr+="\t"+oneFixedFile;
			}
			buf.append(onePairStr+"\n");
		}
		writer.write(buf.toString());
		writer.close();
	}
	
	
	
	
	
	/**
	 * Extract <bug, FilesInDescription> pairs from file
	 * @param srcFilePath
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, TreeSet<String>> extractFilesInDescription(String srcFilePath) throws Exception{
		HashMap<String, TreeSet<String>> idFilesInDescriptionPairs=new HashMap<String, TreeSet<String>>();
		if(!new File(srcFilePath).isFile()){
			System.out.println("The input file path is invalid!");
			return idFilesInDescriptionPairs;
		}
		BufferedReader reader=new BufferedReader(new FileReader(srcFilePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			String []strs=line.split("\t");
			if(strs.length>1){
				String bugID=strs[0];
				TreeSet<String> fixedSet=new TreeSet<String>();
				for(int i=1; i<strs.length;i++){
					fixedSet.add(strs[i]);
				}
				idFilesInDescriptionPairs.put(bugID, fixedSet);
			}
		}
		reader.close();
		return idFilesInDescriptionPairs;
	}
	
	/**
	 * Export <bug, FilesInDescription> pairs to file
	 * @param idFilesInDescriptionPairs
	 * @param dstFilePath
	 * @throws IOException
	 */
	public static void exportFilesInDescription(HashMap<String, TreeSet<String>> idFilesInDescriptionPairs, String dstFilePath) throws IOException{
		FileWriter writer = new FileWriter(dstFilePath);
		StringBuffer buf = new StringBuffer();
		for(Entry <String, TreeSet<String>> pair: idFilesInDescriptionPairs.entrySet()){
			String onePairStr=pair.getKey();
			for(String oneFixedFile:pair.getValue()){
				onePairStr+="\t"+oneFixedFile;
			}
			buf.append(onePairStr+"\n");
		}
		writer.write(buf.toString());
		writer.close();
	}
	
	/**
	 * Extract the dictionaries for the bug reports
	 * @param bugCorpusDirPath
	 * @return
	 * @throws Exception
	 */
	public static WVTWordList extractBugDictionary(String bugCorpusDirPath) throws Exception{
		if(!new File(bugCorpusDirPath).isDirectory()){
			System.out.println("The corpus directory is invalid!");
			return new WVTWordList(1);
		}
		WVTFileInputList list=WVToolWrapper.extractCorpusFileList(bugCorpusDirPath);
		WVTWordList dictionary=WVToolWrapper.extractCorpusDic(list);
		return dictionary;
	}
	
	/**
	 * Save the bug dictionary to the target file
	 * @param dictionary
	 * @param dicFilePath
	 * @throws IOException
	 */
	public static void exportBugDictionary(WVTWordList dictionary, String dicFilePath) throws IOException{
		WVToolWrapper.saveCorpusDic(dictionary, dicFilePath);
	}
	
	/**
	 * Import the code dictionary from the target file
	 * @param dicFilePath
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> importBugDictionary(String dicFilePath) throws Exception{
		ArrayList<String> dictionary=new ArrayList<String>();
		if(!new File(dicFilePath).isFile()){
			System.out.println("The dictionary file path is invalid!");
			return dictionary;
		}
		BufferedReader reader=new BufferedReader(new FileReader(dicFilePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			dictionary.add(line.trim());
		}
		reader.close();
		return dictionary;
	}
	
	/**
	 * Extract the past bugs that fixed the target source code file
	 * @param fileClassName
	 * @param bugID
	 * @param bugList
	 * @return
	 */
	public static HashMap<String, Integer> getPastBugsContainingTargetFile(String fileClassName, String bugID, ArrayList<BugRecord> bugList){
		HashMap<String, Integer> pairs=new HashMap<String, Integer>();
		Date date=new Date();
		for(BugRecord _bug:bugList){
			if(_bug.getBugId()==bugID){
				date=_bug.getFixDate();
				break;
			}
		}
		if(date==new Date()){
			System.out.println("The input bugID does not exist in the bug log");
			return pairs;
		}
		
		//find the past bug reports
		for(BugRecord _bug:bugList){
			if(_bug.getFixDate().compareTo(date)<0){
				//find the bug reports containing the source code file
				for(String oneFixedFile:_bug.getFixedFileSet()){
					if(oneFixedFile.contains(fileClassName)){
						pairs.put(_bug.getBugId(),_bug.getFixedFileSet().size());
					}
				}
			}
		}
		return pairs;
	}
	
	
	
	/**
	 * split and then filter the ones that are stopwords
	 * @param content
	 * @return
	 */
	public static String splitAndFilter(String content){
		String [] terms=Splitter.splitNatureLanguage(content);
		StringBuffer buf=new StringBuffer();
		for (String oneTerm : terms) {
			oneTerm = oneTerm.toLowerCase();
			if (!Stopword.isEnglishStopword(oneTerm)) {
				buf.append(oneTerm + " ");
			}
		}
		return buf.toString();
	}
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		

		
		
	}

}

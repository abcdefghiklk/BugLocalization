package bug;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import config.Config;
import utils.DateFormat;
import utils.Splitter;
import utils.Stem;
import utils.Stopword;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Qiuchi Li
 * BugDataProcessor is the class for Importing/Processing/Exporting Bug Data
 */
public class BugDataProcessor {
	/**
	 * Import the bug report data to a list of Bug Class objects
	 * @return
	 * @throws Exception 
	 */
	static public ArrayList<BugRecord> importFromXML() throws Exception{
		String XMLFilePath=Config.getInstance().getBugLogFile();
		ArrayList<BugRecord> bugList=new ArrayList<BugRecord>();

		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
		InputStream is = new FileInputStream(XMLFilePath);
		Document doc = domBuilder.parse(is);
		Element root = doc.getDocumentElement();
		NodeList bugRepository = root.getChildNodes();
		if (bugRepository != null) {
			for (int i = 0; i < bugRepository.getLength(); i++) {
				Node bugNode = bugRepository.item(i);
				if (bugNode.getNodeType() == Node.ELEMENT_NODE) {
					String bugId = bugNode.getAttributes().getNamedItem("id").getNodeValue();
					String openDate = bugNode.getAttributes().getNamedItem("opendate").getNodeValue();
					String fixDate = bugNode.getAttributes().getNamedItem("fixdate").getNodeValue();
					BugRecord bug = new BugRecord();
					bug.setBugId(bugId);
					bug.setOpenDate(openDate);
					bug.setFixDate(fixDate);
					for (Node node = bugNode.getFirstChild(); node != null; node = node.getNextSibling()) {
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							if (node.getNodeName().equals("buginformation")) {
								NodeList _l = node.getChildNodes();
								for (int j = 0; j < _l.getLength(); j++) {
									Node _n = _l.item(j);
									if (_n.getNodeName().equals("summary")) {
										String summary = _n.getTextContent();
										bug.setBugSummary(summary);
									}
									if (_n.getNodeName().equals("description")) {
										String description = _n.getTextContent();
										bug.setBugDescription(description);
									}
								}
							}
							if (node.getNodeName().equals("fixedFiles")) {
								NodeList _l = node.getChildNodes();
								for (int j = 0; j < _l.getLength(); j++) {
									Node _n = _l.item(j);
									if (_n.getNodeName().equals("file")) {
										String fileName = _n.getTextContent();
										bug.addFixedFile(fileName.replace("/", "."));
									}
								}
							}
						}
					}
					bugList.add(bug);
				}
			}
		}
		Config.getInstance().setBugReportCount(bugList.size());
		return bugList;
	}
	/**
	 * Export the list of bugs to XML file
	 * @param bugList
	 * @param XMLFilePath
	 * @throws Exception 
	 */
	static public void exportToXML(ArrayList<BugRecord> bugList, String XMLFilePath) throws Exception{
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder domBuilder= domFactory.newDocumentBuilder();
		Document doc=domBuilder.newDocument();
		Element rootNode=doc.createElement("bugrepository");
		doc.appendChild(rootNode);
		for(BugRecord _bug:bugList){
			Element _bugNode=doc.createElement("bug");
			_bugNode.setAttribute("id", _bug.getBugId());
			_bugNode.setAttribute("opendate", DateFormat.getFormat().format(_bug.getOpenDate()));
			_bugNode.setAttribute("fixdate", DateFormat.getFormat().format(_bug.getFixDate()));
				
			Element _bugInformationNode=doc.createElement("buginformation");
				
			Element _summaryNode=doc.createElement("summary");
			_summaryNode.appendChild(doc.createTextNode(_bug.getBugSummary()));
			_bugInformationNode.appendChild(_summaryNode);
				
			Element _descriptionNode=doc.createElement("description");
			_descriptionNode.appendChild(doc.createTextNode(_bug.getBugDescription()));
			_bugInformationNode.appendChild(_descriptionNode);
				
				
			_bugNode.appendChild(_bugInformationNode);
				
			Element _fixedFilesNode=doc.createElement("fixedFiles");
				
			for(String oneFixedFileName: _bug.getFixedFileSet()){
				Element _oneFixedFileNode=doc.createElement("file");
				_oneFixedFileNode.appendChild(doc.createTextNode(oneFixedFileName));
				_fixedFilesNode.appendChild(_oneFixedFileNode);
			}
			_bugNode.appendChild(_fixedFilesNode);
				
			rootNode.appendChild(_bugNode);
				
		}		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(XMLFilePath));
		transformer.transform(source, result);
	} 
	
	/**
	 * Create Bug Corpus
	 * @param bugList
	 * @param corpusDir
	 * @throws IOException 
	 */
	static public void createBugCorpus(ArrayList<BugRecord> bugList) throws IOException{
		String corpusDirPath= Config.getInstance().getBugCorpusDir();
		File corpusDir=new File(corpusDirPath);
		if(!corpusDir.exists()){
			corpusDir.mkdir();
		}
		
		//bug description corpus
		File bugDescriptionCorpusDir = Paths.get(corpusDirPath,"description").toFile();
		if (!bugDescriptionCorpusDir.exists()){
			bugDescriptionCorpusDir.mkdir();
		}
		
		//bug summary corpus
		File bugSummaryCorpusDir = Paths.get(corpusDirPath,"summary").toFile();
		if (!bugSummaryCorpusDir.exists()){
			bugSummaryCorpusDir.mkdir();
		}
		
		//bug description & summary corpus
		File bugInformationCorpusDir = Paths.get(corpusDirPath,"information").toFile();
		if (!bugInformationCorpusDir.exists()){
			bugInformationCorpusDir.mkdir();
		}
		
		//bug open dates file
		String openDateFilePath = Paths.get(corpusDirPath, "openDate").toString();
		FileWriter openDateWriter = new FileWriter(openDateFilePath,true);
		
		//bug fix dates file
		String fixDateFilePath = Paths.get(corpusDirPath, "fixDate").toString();
		FileWriter fixDateWriter=new FileWriter(fixDateFilePath,true);
		
		//the file containing fixed classes(files) for a bug
		String fixedClassesFilePath = Paths.get(corpusDirPath, "fixedFiles").toString();
		FileWriter fixedClassesWriter=new FileWriter(fixedClassesFilePath,true);
		
		//the file containing mentioned classes(files) in description
		String classesInDescriptionFilePath = Paths.get(corpusDirPath, "filenamesInDescription").toString();
		FileWriter classesInDescriptionWriter = new FileWriter(classesInDescriptionFilePath,true);
		
		//for every bug, create its corpus for the summary part, description part, and summary & description part
		//and record the open dates, fix dates
		for (BugRecord _bug: bugList){
			FileWriter summaryWriter = new FileWriter(Paths.get(corpusDirPath,"summary", _bug.getBugId()).toString());
			FileWriter descriptionWriter = new FileWriter(Paths.get(corpusDirPath,"description", _bug.getBugId()).toString());
			FileWriter informationWriter = new FileWriter(Paths.get(corpusDirPath,"information", _bug.getBugId()).toString());
			
			String [] bugSummaryWords = Splitter.splitNatureLanguage(_bug.getBugSummary());
			String [] bugDescriptionWords = Splitter.splitNatureLanguage(_bug.getBugDescription());
			StringBuffer summaryBuffer = new StringBuffer();
			StringBuffer descriptionBuffer = new StringBuffer();
			StringBuffer informationBuffer = new StringBuffer(); 
			
			//create bug summary corpus and part of information corpus
			for (String word : bugSummaryWords) {
				word = Stem.stem(word.toLowerCase());
//				word = word.toLowerCase();
				if (!Stopword.isEnglishStopword(word)) {
					summaryBuffer.append(word + " ");
					informationBuffer.append(word + " ");
				}
			}
			
			//create bug description corpus and part of information corpus
			for (String word : bugDescriptionWords) {
				word = Stem.stem(word.toLowerCase());
//				word = word.toLowerCase();
				if (!Stopword.isEnglishStopword(word)) {
					descriptionBuffer.append(word + " ");
					informationBuffer.append(word + " ");
				}
			}
			
			summaryWriter.write(summaryBuffer.toString().trim());
			summaryWriter.flush();
			summaryWriter.close();
			
			descriptionWriter.write(descriptionBuffer.toString().trim());
			descriptionWriter.flush();
			descriptionWriter.close();
			
			informationWriter.write(informationBuffer.toString().trim());
			informationWriter.flush();
			informationWriter.close();
			
			//export bug openDate and fixDate to file
			openDateWriter.write(_bug.getBugId()+"\t"+DateFormat.getFormat().format(_bug.getOpenDate())+"\n");
			openDateWriter.flush();
			
			
			fixDateWriter.write(_bug.getBugId()+"\t"+DateFormat.getFormat().format(_bug.getFixDate())+"\n");
			fixDateWriter.flush();
			
			
			//export fixed files
			String fixedFilesString=_bug.getBugId();
			for (String fileName: _bug.getFixedFileSet()){
				fixedFilesString+="\t"+fileName;
			}
			fixedClassesWriter.write(fixedFilesString.trim()+"\n");
			fixedClassesWriter.flush();
			
			
			//export files appeared in description
			String filesInDescriptionString=extractClassName(_bug.getBugDescription());
			classesInDescriptionWriter.write(_bug.getBugId()+"\t"+filesInDescriptionString.trim()+"\n");
			classesInDescriptionWriter.flush();
			
		}
		openDateWriter.close();
		fixDateWriter.close();
		fixedClassesWriter.close();
		classesInDescriptionWriter.close();	
	}
	

	
	/**
	 * Extract all the class names in a given bug report
	 * @param content
	 * @return
	 */
	public static String extractClassName(String content){

		String pattern = "[a-zA-Z_][a-zA-Z0-9_\\-]*\\.java";
		StringBuffer res = new StringBuffer();

		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Create matcher object.
		Matcher m = r.matcher(content);
		while (m.find()) {
			res.append(m.group(0) + "\t");
		}
		return res.toString();
	}
	
	private static void showHelp() {
		String usage = "Usage:java -jar BugCorpusCreater [-options] \r\n\r\nwhere options must include:\r\n"
				+ "-f	indicates the absolute path of the .xml log file\r\n"
				+ "-d	indicates the absolute path of the directory storing the bug corpus.";

		System.out.println(usage);
	}
	
	
	public static void parseArgs(String []args) throws Exception{
		int i = 0;
		String inputXMLFilePath=new String();
		String bugCorpusPath=new String();
		while (i < args.length - 1) {
			if (args[i].equals("-f")) {
				i++;
				inputXMLFilePath = args[i];
			} else if (args[i].equals("-d")) {
				i++;
				bugCorpusPath = args[i];
			}
			i++;
		}
		boolean isLegal=true;
		if (!new File(inputXMLFilePath).isFile() ){
			isLegal=false;
			System.out.println("Error--the input log file is illegal!\n");
		}
		if (bugCorpusPath.equals(new String())){
			System.out.println("please assign a directory for the bug corpus!");
			isLegal=false;
		}
		if(!isLegal){
			showHelp();
		}
		else{
			Config.getInstance().setPaths(new String(), inputXMLFilePath, new String(), bugCorpusPath);
			ArrayList<BugRecord> bugList=new ArrayList<BugRecord>();
			bugList=importFromXML();
			BugDataProcessor.createBugCorpus(bugList);
		}
	}
	
	public static void main(String []args) throws Exception{
		if(args.length==0){
			showHelp();
		}
		else{
			parseArgs(args);
		}
//		ArrayList<BugRecord> bugList=new ArrayList<BugRecord>();
//		inputXMLFilePath="C:/Users/ql29/Dropbox/Open University/OU Research/codes_Datas/BRTracer/Dataset/AspectJBugRepository.xml";
//		bugList=importFromXML(inputXMLFilePath);
//		String outputXMLFilePath="C:/Users/ql29/Dropbox/Open University/OU Research/codes_Datas/BRTracer/Dataset/AspectJBugRepository_output.xml";
//		exportToXML(bugList,outputXMLFilePath);
//		bugCorpusPath="C:/Users/ql29/Dropbox/Open University/OU Research/codes_Datas/BRTracer/bugCorpus";
//		BugDataProcessor.createBugCorpus(bugList, bugCorpusPath);

	}
}

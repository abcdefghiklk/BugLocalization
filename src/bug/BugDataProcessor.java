package bug;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import property.Property;
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Qiuchi Li
 * BugDataProcessor is the class for Input/Output Bug Data
 */
public class BugDataProcessor {
	/**
	 * Import the bug report data to a list of Bug Class objects
	 * @param XMLFilePath
	 * @return
	 */
	static public ArrayList<Bug> importFromXML(String XMLFilePath){
		ArrayList<Bug> bugList=new ArrayList<Bug>();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		try {
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			InputStream is = new FileInputStream(XMLFilePath);
			Document doc = domBuilder.parse(is);
			Element root = doc.getDocumentElement();
			NodeList bugRepository = root.getChildNodes();
			if (bugRepository != null) {
				for (int i = 0; i < bugRepository.getLength(); i++) {
					Node bugNode = bugRepository.item(i);
					if (bugNode.getNodeType() == Node.ELEMENT_NODE) {
						String bugId = bugNode.getAttributes()
								.getNamedItem("id").getNodeValue();
						String openDate = bugNode.getAttributes()
								.getNamedItem("opendate").getNodeValue();
						String fixDate = bugNode.getAttributes()
								.getNamedItem("fixdate").getNodeValue();
						Bug bug = new Bug();
						bug.setBugId(bugId);
						bug.setOpenDate(openDate);
						bug.setFixDate(fixDate);
						for (Node node = bugNode.getFirstChild(); node != null; node = node
								.getNextSibling()) {
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								if (node.getNodeName().equals("buginformation")) {
									NodeList _l = node.getChildNodes();
									for (int j = 0; j < _l.getLength(); j++) {
										Node _n = _l.item(j);
										if (_n.getNodeName().equals("summary")) {
											String summary = _n
													.getTextContent();
											bug.setBugSummary(summary);
										}

										if (_n.getNodeName().equals(
												"description")) {
											String description = _n
													.getTextContent();
											bug.setBugDescription(description);
										}

									}

								}
								if (node.getNodeName().equals("fixedFiles")) {
									NodeList _l = node.getChildNodes();
									for (int j = 0; j < _l.getLength(); j++) {
										Node _n = _l.item(j);
										if (_n.getNodeName().equals("file")) {
											String fileName = _n
													.getTextContent();
											bug.addFixedFile(fileName);
										}
									}
								}
							}
						}
						bugList.add(bug);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bugList;
	}
	/**
	 * Export the list of bugs to XML file
	 * @param bugList
	 * @param XMLFilePath
	 */
	static public void exportToXML(ArrayList<Bug> bugList, String XMLFilePath){
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		try {
			DocumentBuilder domBuilder= domFactory.newDocumentBuilder();
			Document doc=domBuilder.newDocument();
			Element rootNode=doc.createElement("bugrepository");
			doc.appendChild(rootNode);
			for(Bug _bug:bugList){
				Element _bugNode=doc.createElement("bug");
				_bugNode.setAttribute("id", _bug.getBugId());
				_bugNode.setAttribute("opendate", _bug.getOpenDate());
				_bugNode.setAttribute("fixdate", _bug.getFixDate());
				

				
				Element _bugInformationNode=doc.createElement("buginformation");
				
				Element _summaryNode=doc.createElement("summary");
				_summaryNode.appendChild(doc.createTextNode(_bug.getBugSummary()));
				_bugInformationNode.appendChild(_summaryNode);
				
				Element _descriptionNode=doc.createElement("description");
				_descriptionNode.appendChild(doc.createTextNode(_bug.getBugDescription()));
				_bugInformationNode.appendChild(_descriptionNode);
				
				
				_bugNode.appendChild(_bugInformationNode);
				
				Element _fixedFilesNode=doc.createElement("fixedFiles");
				
				for(String oneFixedFileName: _bug.getSet()){
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
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String []args){
		ArrayList<Bug> bugList=new ArrayList<Bug>();
		String inputXMLFilePath="C:/Users/dell/Dropbox/Open University/OU Research/codes&Datas/BRTracer/Dataset/AspectJBugRepository.xml";
		bugList=importFromXML(inputXMLFilePath);
		String outputXMLFilePath="C:/Users/dell/Dropbox/Open University/OU Research/codes&Datas/BRTracer/Dataset/AspectJBugRepository_output.xml";;
		exportToXML(bugList,outputXMLFilePath);
		
	}
}

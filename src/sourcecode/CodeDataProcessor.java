package sourcecode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import config.Config;
import sourcecode.ast.FileParser;
import utils.Stem;
import utils.Stopword;

/**
 * I/O for source code data
 * @author Qiuchi Li
 *
 */
public class CodeDataProcessor {
	/**
	 * Extract the code data from the project
	 * @param srcDirPath
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public static SourceCodeCorpus extractCodeData() throws Exception{
		String srcDirPath=Config.getInstance().getDatasetDir();
		String fileType=Config.getInstance().getFileType();
		int segmentationLength=Config.getInstance().getSegmentationLength();
		SourceCodeCorpus corpus=new SourceCodeCorpus(segmentationLength,fileType);
		File srcDir =new File(srcDirPath);
		if(!srcDir.isDirectory()){
			System.out.println("The input directory path is invalid");
			return corpus;
		}
		ArrayList<String> fileList=new ArrayList<String>();
		detectAllFiles(srcDirPath, fileType,fileList);
		for(String oneFilePath: fileList){
			SourceCode oneCodeFile = new SourceCode();
			FileParser parser=new FileParser(oneFilePath);
			
			
			//set the full class name(package+fileName)
			String packageName= parser.getPackageName();
			String fullClassName=new String();
			if (packageName.trim().equals("")){
				// no package, the name only
				fullClassName = new File(oneFilePath).getName();
			}
			else{
				//full class name = package name + file name
				fullClassName = packageName+ "." + new File(oneFilePath).getName();
			}
			oneCodeFile.setFullClassName(fullClassName);
			
			//set the file content
			String []terms= parser.getContent();
			String fileContent=new String();
			for(String term:terms){
				String stemmedTerm = Stem.stem(term.toLowerCase());
				if(!(Stopword.isKeyword(term) || Stopword.isEnglishStopword(term))){
					fileContent+=stemmedTerm+" ";
				}
			}
			oneCodeFile.setContent(fileContent);
			
			//set the class names in the file
			String classNamesString= parser.getAllClassName();
			String []classNameArray=classNamesString.split(" ");
			for(String oneClassName:classNameArray){
				oneCodeFile.addClassName(oneClassName);
			}
			
			//set the method names in the file
			String methodNamesString= parser.getAllMethodName();
			String []methodNameArray=methodNamesString.split(" ");
			for(String oneMethodName:methodNameArray){
				oneCodeFile.addMethodName(oneMethodName);
			}
			
			//add the source code file information to the corpus
			corpus.addSourceCode(oneCodeFile);
			
		}
		
		//set the original code file count
		Config.getInstance().setFileCount(corpus.getSourceCodeList().size());
		
		//segment each source code file
		corpus.segment();
		
		return corpus;
	}
	
	/**
	 * Export the code data to the given directory
	 * @param dstDirPath
	 * @param corpus
	 * @throws IOException 
	 */
	public static void exportCodeData(SourceCodeCorpus corpus) throws IOException{
		
		String dstDirPath= Config.getInstance().getCodeCorpusDir();
		//create a directory
		File dstDir=new File(dstDirPath);
		if(!dstDir.isDirectory()){
			dstDir.mkdir();
		}
		
		//record the basic information in the "/basicInfo"
		String basicInfoFilePath= Paths.get(dstDirPath, "basicInfo").toString();
		FileWriter basicInfoWriter=new FileWriter(basicInfoFilePath);
		
		//fileType + segmentationLength
		String basicInfoStr="fileType="+"\t"+corpus.getFileType()+"\r\n"+"segmentationLength="+"\t"+corpus.getSegmentationLength();
		basicInfoWriter.write(basicInfoStr);
		basicInfoWriter.close();
		
		//corpus built by original code contents
		String codeContentDirPath= Paths.get(dstDirPath, "codeContentCorpus").toString();
		File codeContentDir=new File(codeContentDirPath);
		if(!codeContentDir.isDirectory()){
			codeContentDir.mkdir();
		}
		
		//corpus built by code segments
		String codeSegmentDirPath= Paths.get(dstDirPath, "codeSegmentCorpus").toString();
		File codeSegmentDir=new File(codeSegmentDirPath);
		if(!codeSegmentDir.isDirectory()){
			codeSegmentDir.mkdir();
		}
		
		//corpus built by class name list
		String classNameDirPath= Paths.get(dstDirPath,"classNameCorpus").toString();
		File classNameDir=new File(classNameDirPath);
		if(!classNameDir.isDirectory()){
			classNameDir.mkdir();
		}
		
		//corpus built by method name list
		String methodNameDirPath= Paths.get(dstDirPath,"methodNameCorpus").toString();
		File methodNameDir=new File(methodNameDirPath);
		if(!methodNameDir.isDirectory()){
			methodNameDir.mkdir();
		}
				
		//Traverse every file in the source code file list
		for(SourceCode oneCodeFile: corpus.getSourceCodeList()){
			String fileName;
			FileWriter writer;
			
			//For each source code file, the name in the original content corpus is the full class name 
			fileName= Paths.get(codeContentDirPath, oneCodeFile.getFullClassName()).toString();
			writer=new FileWriter(fileName);
			writer.write(oneCodeFile.getContent());
			writer.close();
			
			//For each source code file, the name in the code segments corpus is the full class name+@+"the segment index".java 
			String []codeSegmentArray= oneCodeFile.getCodeSegmentList().toArray(new String[0]);
			for(int i=0;i<codeSegmentArray.length;i++){
				fileName= Paths.get(codeSegmentDirPath, oneCodeFile.getFullClassName()+"@"+i+".java").toString();
				writer=new FileWriter(fileName);
				writer.write(codeSegmentArray[i]);
				writer.close();
			}
			
			//For each source code file, the name in the class Names corpus is the full class name 
			fileName=Paths.get(classNameDirPath, oneCodeFile.getFullClassName()).toString();
			writer=new FileWriter(fileName);
			String classNamesString= new String();
			for(String oneClassName: oneCodeFile.getClassNameList()){
				classNamesString+=oneClassName+" ";
			}
			writer.write(classNamesString.trim());
			writer.close();
			
			//For each source code file, the name in the method Names corpus is the full class name 
			fileName=Paths.get(methodNameDirPath, oneCodeFile.getFullClassName()).toString();
			writer=new FileWriter(fileName);
			String methodNamesString= new String();
			for(String oneMethodName: oneCodeFile.getMethodNameList()){
				methodNamesString+=oneMethodName+" ";
			}
			writer.write(methodNamesString.trim());
			writer.close();
		}
		


	}
	
	/**
	 * Import the source code corpus from the given directory
	 * @param srcDirPath
	 * @return
	 * @throws Exception 
	 */
	public static SourceCodeCorpus importCodeData() throws Exception{
		String srcDirPath= Config.getInstance().getCodeCorpusDir();
		SourceCodeCorpus corpus=new SourceCodeCorpus();
		File srcDir=new File(srcDirPath);
		if(!srcDir.isDirectory()){
			System.out.println("The input directory path is invalid!");
			return corpus;
		}
		
		//read the basic information from the "/basicInfo"
		String basicInfoFilePath= Paths.get(srcDirPath, "basicInfo").toString();
		if(!new File(basicInfoFilePath).isFile()){
			System.out.println("The file recording the basic information is missing!");
		}
		else{
			BufferedReader basicInfoReader=new BufferedReader(new FileReader(basicInfoFilePath));
			
			//read the first line: fileType=\tfileType
			String []strs=basicInfoReader.readLine().split("\t");
			String fileType=strs[1].trim();
			
			//read the second line: segmentationLength=\tsegmentationLength
			strs=basicInfoReader.readLine().split("\t");
			int segmentationLength=Integer.parseInt(strs[1].trim());
			
			corpus.setFileType(fileType);
			corpus.setSegmentationLength(segmentationLength);
			basicInfoReader.close();
		}
		
		//read the code content information from the "/codeContentCorpus"
		String codeContentDirPath= Paths.get(srcDirPath, "codeContentCorpus").toString();
		File codeContentDir=new File(codeContentDirPath);
		if(!codeContentDir.isDirectory()){
			System.out.println("The directory for original code contents is missing!");
		}
		else{
			for(File oneFile: codeContentDir.listFiles()){
				if(oneFile.isFile()){
					String fullClassName=oneFile.getName();
					BufferedReader reader=new BufferedReader(new FileReader(oneFile));
					String codeContent=reader.readLine();
					
					//If exists the full class name, set the code content;
					//else create a new SourceCode object and set the code consent
					boolean isExist=false;
					for(SourceCode sourceCode: corpus.getSourceCodeList()){
						if(sourceCode.getFullClassName().equals(fullClassName.trim())){
							sourceCode.setContent(codeContent);
							isExist=true;
							break;
						}
					}
					if(!isExist){
						SourceCode newSourceCode=new SourceCode();
						newSourceCode.setFullClassName(fullClassName);
						newSourceCode.setContent(codeContent);
						corpus.addSourceCode(newSourceCode);
					}
					reader.close();
				}
			}
		}
		
		//read the code segment information from code segment corpus
		String codeSegmentDirPath= Paths.get(srcDirPath, "codeSegmentCorpus").toString();
		File codeSegmentDir=new File(codeSegmentDirPath);
		if(!codeSegmentDir.isDirectory()){
			System.out.println("The directory for code segments is missing!");
		}
		else{
			for(File oneFile: codeContentDir.listFiles()){
				if(oneFile.isFile()){
					
					//fullClassName+@+"segment index".java
					String oneSegmentName=oneFile.getName();
					BufferedReader reader=new BufferedReader(new FileReader(oneFile));
					String codeSegment=reader.readLine();
					
					String []strs=oneSegmentName.split("@");
					String fullClassName=strs[0].trim();
					
					//If exists the full class name, add the segment to the list;
					//else create a new SourceCode object and add the segment to the list
					boolean isExist=false;
					for(SourceCode sourceCode: corpus.getSourceCodeList()){
						if(sourceCode.getFullClassName().equals(fullClassName.trim())){
							if(!sourceCode.getCodeSegmentList().contains(codeSegment)){
								sourceCode.addCodeSegment(codeSegment);
							}
							isExist=true;
							break;
						}
					}
					if(!isExist){
						SourceCode newSourceCode=new SourceCode();
						newSourceCode.setFullClassName(fullClassName);
						newSourceCode.addCodeSegment(codeSegment);
						corpus.addSourceCode(newSourceCode);
					}
					reader.close();
				}
			}
		}
		
		//read the class names information from the "/classNameCorpus"
		String classNameDirPath= Paths.get(srcDirPath, "classNameCorpus").toString();
		File classNameDir=new File(classNameDirPath);
		if(!classNameDir.isDirectory()){
			System.out.println("The directory for class names is missing!");
		}
		else{
			for(File oneFile: classNameDir.listFiles()){
				if(oneFile.isFile()){
					String fullClassName=oneFile.getName();
					BufferedReader reader=new BufferedReader(new FileReader(oneFile));
					String classNamesString=reader.readLine();
							
					//If exists the full class name, add the class names to the list;
					//else create a new SourceCode object and add all the class names to the list
					boolean isExist=false;
					for(SourceCode sourceCode: corpus.getSourceCodeList()){
						if(sourceCode.getFullClassName().equals(fullClassName.trim())){
							if(classNamesString==null){
								isExist=true;
								break;
							}
							for(String oneClassName: classNamesString.split(" ")){
								if(!sourceCode.getClassNameList().contains(oneClassName.trim())){
									sourceCode.addClassName(oneClassName.trim());
								}
							}
							isExist=true;
							break;
						}
					}
					if(!isExist){
						SourceCode newSourceCode=new SourceCode();
						newSourceCode.setFullClassName(fullClassName);
						if(classNamesString!=null){
							for(String oneClassName: classNamesString.split(" ")){
								newSourceCode.addClassName(oneClassName.trim());
							}
						}
						corpus.addSourceCode(newSourceCode);
					}
					reader.close();
				}
			}
		}		
		
		//read the method names information from the "/methodNameCorpus"
		String methodNameDirPath= Paths.get(srcDirPath, "methodNameCorpus").toString();
		File methodNameDir=new File(methodNameDirPath);
		if(!methodNameDir.isDirectory()){
			System.out.println("The directory for method names is missing!");
		}
		else{
			for(File oneFile: methodNameDir.listFiles()){
				if(oneFile.isFile()){
					String fullClassName=oneFile.getName();
					BufferedReader reader=new BufferedReader(new FileReader(oneFile));
					String methodNamesString=reader.readLine();	
					//If exists the full class name, add the class names to the list;
					//else create a new SourceCode object and add all the method names to the list
					boolean isExist=false;
					for(SourceCode sourceCode: corpus.getSourceCodeList()){
						if(sourceCode.getFullClassName().equals(fullClassName.trim())){
							if(methodNamesString==null){
								isExist=true;
								break;
							}
							for(String oneMethodName: methodNamesString.split(" ")){
								if(!sourceCode.getMethodNameList().contains(oneMethodName.trim()))
								sourceCode.addMethodName(oneMethodName.trim());
							}
							isExist=true;
							break;
						}
					}
					if(!isExist){
						SourceCode newSourceCode=new SourceCode();
						newSourceCode.setFullClassName(fullClassName);
						if(methodNamesString!=null){
							for(String oneMethodName: methodNamesString.split(" ")){
								newSourceCode.addMethodName(oneMethodName.trim());
							}
						}
						corpus.addSourceCode(newSourceCode);
					}
					reader.close();
				}
			}
		}		
		return corpus;
	}
	
	/**
	 * Detect all files with the given type in the given directory 
	 * @param srcDirPath
	 * @param fileType
	 * @param fileList
	 */
	public static void detectAllFiles(String srcDirPath, String fileType,ArrayList<String> fileList){
		for(File oneFile: new File(srcDirPath).listFiles()){
			if(oneFile.isDirectory()){
				detectAllFiles(oneFile.getAbsolutePath(),fileType,fileList);
			}
			else{
				String fileName=oneFile.getAbsolutePath();
				if(fileType==null || fileName.endsWith(fileType)){
					fileList.add(fileName);
				}
			}
		}
	}
	
	
	
	
	private static void showHelp() {
		String usage = "Usage:java -jar BugCorpusCreater [-options] \r\n\r\nwhere options must include:\r\n"
				+ "-i	indicates the absolute path of the source code directory\r\n"
				+ "-o	indicates the absolute path of the directory containing the source code corpus\r\n"
				+ "user can choose to set the following parameters:\r\n"
				+ "-t 	indicates the type of source code file you are concerned about, default= java\r\n"
				+ "-l 	indicates the segmentation length for each source code file, default=800\r\n";

		System.out.println(usage);
	}
	
	
	public static void parseArgs(String []args) throws Exception{
		int i = 0;
		String srcDirPath=new String();
//		String srcDirPath="C:/Users/ql29/Documents/EClipse/Dataset/swt-3.1";
		String dstDirPath=new String();
//		String dstDirPath="C:/Users/ql29/Documents/EClipse/sourceCodeCorpus_new";
		while (i < args.length - 1) {
			if (args[i].equals("-i")) {
				i++;
				srcDirPath = args[i];
			} else if (args[i].equals("-o")) {
				i++;
				dstDirPath = args[i];
			} else if (args[i].equals("-t")) {
				i++;
				if(args[i]!=null){
					Config.getInstance().setFileType(args[i+1]);	
				}
			} else if (args[i].equals("-l")){
				i++;
				if(args[i]!=null){
					Config.getInstance().setSegmentationLength(Integer.parseInt(args[i]));
				}
			}
			i++;
		}
		boolean isLegal=true;
		if (!new File(srcDirPath).isDirectory() ){
			isLegal=false;
			System.out.println("Error--the input directory is illegal!\n");
		}
		if (dstDirPath.equals(new String())){
			System.out.println("please assign a directory for the source code corpus!");
			isLegal=false;
		}
		if(!isLegal){
			showHelp();
		}
		else{
			SourceCodeCorpus corpus=extractCodeData();
			Config.getInstance().setPaths(srcDirPath, null, dstDirPath, null);
			System.out.println("corpus extraction successful!");
			exportCodeData(corpus);
			System.out.println("corpus successfully exported!");
		}
	}
	
	public static void main(String []args) throws Exception{
		if(args.length==0){
			showHelp();
		}
		else{
			parseArgs(args);
		}
	}

}

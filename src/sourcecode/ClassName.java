package sourcecode;

import property.Property;
import sourcecode.ast.Corpus;
import sourcecode.ast.FileDetector;
import sourcecode.ast.FileParser;
import utils.Stem;
import utils.Stopword;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.TreeSet;

public class ClassName {

	public ClassName() throws IOException, ParseException {

	}

    public static void main(String[] agrs) throws Exception {
        new ClassName().create();
    }
	public void create() throws Exception {
		//instantiate a detector class object, with the file type java assigned
		FileDetector detector = new FileDetector("java");
		
		//collect all java files in the target directory
		File[] files = detector.detect(Property.getInstance()
				.getSourceCodeDir());
		ClassName corpusCreator = new ClassName();

		//ClassName.txt saves all class names
		FileWriter writer = new FileWriter(Property.getInstance().getWorkDir()
				+ Property.getInstance().getSeparator() + "ClassName.txt");
		
		//ClassAndMethodCorpus.txt saves all class and method names
        FileWriter NameWriter = new FileWriter(Property.getInstance().getWorkDir()+Property.getInstance().getSeparator()+"ClassAndMethodCorpus.txt");

		int count = 0;

		TreeSet<String> nameSet = new TreeSet<String>();
		for (File file : files) {
			Corpus corpus = corpusCreator.create(file);

			if (corpus == null)
				continue;
			if (!nameSet.contains(corpus.getJavaFileFullClassName())) {
				//if the full class name has .java
				if (corpus.getJavaFileFullClassName().endsWith(".java")) {
					writer.write(count + "\t"
							+ corpus.getJavaFileFullClassName()
							+ Property.getInstance().getLineSeparator());
                    NameWriter.write(corpus.getJavaFileFullClassName()+"\t"
                            + corpus.getContent() + Property.getInstance().getLineSeparator());
				} 
				//if the full class name doesn't have .java ending, just add it!
				else {
					writer.write(count + "\t"
							+ corpus.getJavaFileFullClassName() + ".java"
							+ Property.getInstance().getLineSeparator());
                    NameWriter.write(corpus.getJavaFileFullClassName()+".java"+"\t"
                            + corpus.getContent() + Property.getInstance().getLineSeparator());

				}
				writer.flush();
                NameWriter.flush();
				nameSet.add(corpus.getJavaFileFullClassName());
				count++;
			}

		}
		//assign the original file counts to the Property Class
		Property.getInstance().setOriginFileCount(count);
		writer.close();
        NameWriter.close();
	}

	
	public Corpus create(File file) {
		FileParser parser = new FileParser(file);
		
		//fileName is the name of the package
		String fileName = parser.getPackageName();
		if (fileName.trim().equals("")) {
			fileName = file.getName();
		} else {
			fileName += "." + file.getName();
		}
		
		/* modification for AspectJ */
        if(Property.getInstance().getProject().compareTo("aspectj")==0){
            fileName = file.getPath();
		    fileName = fileName.substring(Property.getInstance().getOffset());
        }
        /* ************************** */
        
		fileName = fileName.substring(0, fileName.lastIndexOf("."));

/*********************************************************************************************/
//This part seems to be useless
		//contentBuf stores all contents within a file,separated by " "
        String[] content = parser.getContent();
        StringBuffer contentBuf = new StringBuffer();
        for (String word : content) {
            String stemWord = Stem.stem(word.toLowerCase());
            if (!(Stopword.isKeyword(word) || Stopword.isEnglishStopword(word))) {

                contentBuf.append(stemWord);
                contentBuf.append(" ");
            }
        }
/********************************************************************************************/
        
		String[] classNameAndMethodName = parser.getClassNameAndMethodName();
		
		//nameBuf stores all class names and method names within a file
		StringBuffer nameBuf = new StringBuffer();

		for (String word : classNameAndMethodName) {
			String stemWord = Stem.stem(word.toLowerCase());
			nameBuf.append(stemWord);
			nameBuf.append(" ");
		}

		String names = nameBuf.toString();
		Corpus corpus = new Corpus();
		corpus.setJavaFilePath(file.getAbsolutePath());
		corpus.setJavaFileFullClassName(fileName);
		corpus.setContent(names);
		return corpus;
	}
}

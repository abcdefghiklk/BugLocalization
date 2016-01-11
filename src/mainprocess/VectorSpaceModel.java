package mainprocess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import sourcecode.CodeDataProcessor;
import utils.FileUtils;
import bug.BugDataProcessor;
import bug.BugFeatureExtractor;
import config.Config;
import eval.MAP;
import eval.MRR;
import eval.TopK;
import feature.VSMScore;
import feature.VectorCreator;

public class VectorSpaceModel {
	public static void run() throws Exception, Exception{
		
		String bugCorpusDirPath=Paths.get(Config.getInstance().getIntermediateDir(), "bug").toString();
		Config.getInstance().setBugCorpusDir(bugCorpusDirPath);
		BugDataProcessor.createBugCorpus(BugDataProcessor.importFromXML());
				
		String codeCorpusDirPath=Paths.get(Config.getInstance().getIntermediateDir(), "code").toString();
		Config.getInstance().setCodeCorpusDir(codeCorpusDirPath);
		CodeDataProcessor.exportCodeData(CodeDataProcessor.extractCodeData());
		
		
		String bugVecFilePath=Paths.get(Config.getInstance().getIntermediateDir(), "bugVec").toString();
		String codeVecFilePath=Paths.get(Config.getInstance().getIntermediateDir(), "codeVec").toString();
		VectorCreator.create(Paths.get(bugCorpusDirPath,"information").toString(), Paths.get(codeCorpusDirPath,"codeContentCorpus").toString(), bugVecFilePath, codeVecFilePath);
				
		String simMatFilePath=Paths.get(Config.getInstance().getIntermediateDir(), "VSMScore").toString();
		VSMScore.generate(bugVecFilePath, codeVecFilePath, simMatFilePath);
				
		String fixedFilePath=Paths.get(bugCorpusDirPath, "fixedFiles").toString();
		
		
		//evaluations
		if(Config.getInstance().getMRRUsed()){
			MRR mrr=new MRR();
			mrr.set(BugFeatureExtractor.extractFixedFiles(fixedFilePath));
			FileUtils.write_append2file("MRR"+"\t"+mrr.evaluate(simMatFilePath)+"\n", Config.getInstance().getOutputFile());
		}
		
		if(Config.getInstance().getMAPUsed()){
			MAP map=new MAP();
			map.set(BugFeatureExtractor.extractFixedFiles(fixedFilePath));
			FileUtils.write_append2file("MAP"+"\t"+map.evaluate(simMatFilePath)+"\n", Config.getInstance().getOutputFile());
//			System.out.println(map.evaluate(simMatFilePath));
		}
		
		if(Config.getInstance().getTopKUsed()){
			TopK topK=new TopK(Config.getInstance().getK());
			topK.set(BugFeatureExtractor.extractFixedFiles(fixedFilePath));
			FileUtils.write_append2file("TopK"+"\t"+topK.evaluate(simMatFilePath)+"\n", Config.getInstance().getOutputFile());
//			System.out.println(topK.evaluate(simMatFilePath));
		}	
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String rootDirPath="C:/Users/dell/Documents/EClipse";
		String configFilePath=Paths.get(rootDirPath, "property").toString();
		String datasetsDirPath=Paths.get(rootDirPath,"Dataset").toString();
		String intermediateDirPath=Paths.get(rootDirPath, "Corpus").toString();
		if(!new File(intermediateDirPath).isDirectory()){
			new File(intermediateDirPath).mkdir();
		}
		String outputFilePath=Paths.get(rootDirPath, "VSM", "output").toString();
		if(new File(outputFilePath).isFile()){
			new File(outputFilePath).delete();
		}
		String evaluationDirPath=Paths.get(rootDirPath, "VSM", "eval").toString();
		String projectName="swt";
		String datasetDirPath;
		String bugLogFilePath;
		if(projectName=="swt"){
			datasetDirPath=Paths.get(datasetsDirPath,"swt-3.1").toString();
			bugLogFilePath=Paths.get(datasetsDirPath, "SWTBugRepository.xml").toString();
		}
		else if(projectName=="aspectj"){
			datasetDirPath=Paths.get(datasetsDirPath, "aspect").toString();
			bugLogFilePath=Paths.get(datasetsDirPath, "AspectJBugRepository.xml").toString();
		}
		else if(projectName=="eclipse"){
			datasetDirPath=Paths.get(datasetsDirPath, "Eclipse-3.1").toString();
			bugLogFilePath=Paths.get(datasetsDirPath, "EclipseBugRepository.xml").toString();
		}
		else{
			System.out.println("The project name is invalid");
			return;
		}
		
		if(!new File(evaluationDirPath).isDirectory()){
			new File(evaluationDirPath).mkdir();
		}
		Config.getInstance().setPaths(datasetDirPath, bugLogFilePath, intermediateDirPath, outputFilePath);
		Config.getInstance().setEvaluations(evaluationDirPath, true, true, 5, true);
		Config.getInstance().exportConfig(configFilePath);
		run();
		
	}

}

package mainprocess;

import java.io.File;
import java.nio.file.Paths;

import sourcecode.CodeDataProcessor;
import sourcecode.SourceCodeCorpus;
import utils.FileUtils;
import bug.BugDataProcessor;
import bug.BugFeatureExtractor;
import config.Config;
import eval.MAP;
import eval.MRR;
import eval.TopK;
import feature.CodeLength;
import feature.RevisedVSMScore;
import feature.VectorCreator;

public class RevisedVectorSpaceModel {

	public static void run() throws Exception{
		String bugCorpusDirPath=Paths.get(Config.getInstance().getIntermediateDir(), "bug").toString();
		Config.getInstance().setBugCorpusDir(bugCorpusDirPath);
		BugDataProcessor.createBugCorpus(BugDataProcessor.importFromXML());
		
		String codeCorpusDirPath=Paths.get(Config.getInstance().getIntermediateDir(), "code").toString();
		Config.getInstance().setCodeCorpusDir(codeCorpusDirPath);
		SourceCodeCorpus corpus=CodeDataProcessor.extractCodeData();
		String codeLengthFilePath=Paths.get(Config.getInstance().getIntermediateDir(), "codelength").toString();
		CodeLength.generate(corpus, codeLengthFilePath);
		CodeDataProcessor.exportCodeData(CodeDataProcessor.extractCodeData());
		
		String bugVecFilePath=Paths.get(Config.getInstance().getIntermediateDir(), "bugVec").toString();
		String codeVecFilePath=Paths.get(Config.getInstance().getIntermediateDir(), "codeVec").toString();
		VectorCreator.create(Paths.get(bugCorpusDirPath,"description").toString(), Paths.get(codeCorpusDirPath,"codeContentCorpus").toString(), bugVecFilePath, codeVecFilePath, "logtfidf");
		
		String simMatFilePath=Paths.get(Config.getInstance().getIntermediateDir(), "revisedVSMScore").toString();
		RevisedVSMScore.generate(bugVecFilePath, codeVecFilePath, codeLengthFilePath,simMatFilePath);
		
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
		}
				
		if(Config.getInstance().getTopKUsed()){
			TopK topK=new TopK(Config.getInstance().getK());
			topK.set(BugFeatureExtractor.extractFixedFiles(fixedFilePath));
			FileUtils.write_append2file("TopK@"+topK.getK()+"\t"+topK.evaluate(simMatFilePath)+"\n", Config.getInstance().getOutputFile());
		}			
		
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String rootDirPath="C:/Users/ql29/Documents/EClipse";
		String configFilePath=Paths.get(rootDirPath, "property").toString();
		String datasetsDirPath=Paths.get(rootDirPath,"Dataset").toString();
		String intermediateDirPath=Paths.get(rootDirPath, "Corpus").toString();
		if(!new File(intermediateDirPath).isDirectory()){
			new File(intermediateDirPath).mkdir();
		}
		String outputFilePath=Paths.get(rootDirPath, "RevisedVSM", "output").toString();
		if(new File(outputFilePath).isFile()){
			new File(outputFilePath).delete();
		}
		String evaluationDirPath=Paths.get(rootDirPath, "RevisedVSM", "eval").toString();
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

package mainprocess;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import sourcecode.CodeDataProcessor;
import bug.BugDataProcessor;
import bug.BugFeatureExtractor;
import bug.BugRecord;
import config.Config;
import eval.MAP;
import eval.MRR;
import eval.TopK;
import feature.VSMScore;
import feature.VectorCreator;

public class BugLocator {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String rootDirPath="C:/Users/dell/Documents/EClipse";
		String configFilePath=Paths.get(rootDirPath, "property").toString();
		String datasetsDirPath=Paths.get(rootDirPath,"Dataset").toString();
		String intermediateDirPath=Paths.get(rootDirPath, "Corpus").toString();
		String outputFilePath=Paths.get(rootDirPath, "output").toString();
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
		Config.getInstance().setPaths(datasetDirPath, bugLogFilePath, intermediateDirPath, outputFilePath);
		
		String bugCorpusDirPath=Paths.get(intermediateDirPath, "bug").toString();
		Config.getInstance().setBugCorpusDir(bugCorpusDirPath);
//		BugDataProcessor.createBugCorpus(BugDataProcessor.importFromXML());
		
		String codeCorpusDirPath=Paths.get(intermediateDirPath, "code").toString();
		Config.getInstance().setCodeCorpusDir(codeCorpusDirPath);
//		CodeDataProcessor.exportCodeData(CodeDataProcessor.extractCodeData());
		
		String bugVecFilePath=Paths.get(intermediateDirPath, "bugVec").toString();
		String codeVecFilePath=Paths.get(intermediateDirPath, "codeVec").toString();
//		VectorCreator.create(Paths.get(bugCorpusDirPath,"information").toString(), Paths.get(codeCorpusDirPath,"codeContentCorpus").toString(), bugVecFilePath, codeVecFilePath);
		
		String simMatFilePath=Paths.get(intermediateDirPath, "VSMScore").toString();
//		VSMScore.generate(bugVecFilePath, codeVecFilePath, simMatFilePath);
		
		String fixedFilePath=Paths.get(bugCorpusDirPath, "fixedFiles").toString();
		TopK topK=new TopK(5);
		topK.set(BugFeatureExtractor.extractFixedFiles(fixedFilePath));
		
		MRR mrr=new MRR();
		mrr.set(BugFeatureExtractor.extractFixedFiles(fixedFilePath));
		
		MAP map=new MAP();
		map.set(BugFeatureExtractor.extractFixedFiles(fixedFilePath));
		
		System.out.println(map.evaluate(simMatFilePath));
		System.out.println(topK.evaluate(simMatFilePath));
		System.out.println(mrr.evaluate(simMatFilePath));
	}

}

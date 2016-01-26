package feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import property.Property;
import sourcecode.CodeFeatureExtractor;
import sourcecode.SourceCodeCorpus;

public class CodeLength {
	public static void generate(SourceCodeCorpus corpus, String dstFilePath) throws Exception{
		HashMap<String, Integer> fileClassLengthPairs= CodeFeatureExtractor.extractCodeLength(corpus);
		HashMap<String, Double> normalizedLengths= normalize(fileClassLengthPairs);
		HashMap<String, Double> nonLinearTransformedLengths= nonLinearTransform(normalizedLengths, "logistic");
		
		savePairs(dstFilePath, nonLinearTransformedLengths);
//		CodeFeatureExtractor.saveCodeLength(dstFilePath, fileClassLengthPairs);
	}
	
	/**
	 * Normalize to [0,1] using a linear transformation
	 * @param originalPairs
	 * @return
	 */
	public static HashMap<String, Double> normalize(HashMap<String, Integer> originalPairs){
		HashMap<String, Integer> rawData=new HashMap<String, Integer>();
		rawData.putAll(originalPairs);
		HashMap<String, Double> normalizedPairs=new HashMap<String, Double>();
		//exclude outliers using 3-sigma rule
		int sum;
		double avg=0;
		double squaredError;
		double deviation=0;	
		boolean hasOutlier=true;
		int iter=0;
		while(hasOutlier && iter<1){
			hasOutlier=false;
			sum=0;
			for(int value:rawData.values()){
				sum+=value;
			}
			avg=(sum+0.0d)/(double)(rawData.size());
			squaredError= 0.0d;
			for(int value:rawData.values()){
				squaredError+=(value-avg)*(value-avg);
			}
			deviation= Math.sqrt(squaredError/rawData.size());
			System.out.println("deviation="+deviation+" avg="+avg);
			ArrayList<String> removeItems=new ArrayList<String>();
			for(Entry<String, Integer> pair:rawData.entrySet()){
				int value=pair.getValue();
				if(value>(avg+3*deviation) || value<(avg-3*deviation)){
					removeItems.add(pair.getKey());
					hasOutlier=true;
				}
			}
			if(hasOutlier){
				for(String oneItem:removeItems){
					rawData.remove(oneItem);
				}
				iter++;
			}
		}	

		int min=0;
		if(avg-3*deviation>0){
			min=(int)(avg-3*deviation);
		}
		double max= avg+ 3*deviation;
		for(Entry<String, Integer> pair: originalPairs.entrySet()){
			if(pair.getValue()>max){
				normalizedPairs.put(pair.getKey(), 1.0);
			}
			else if(pair.getValue()<min){
				normalizedPairs.put(pair.getKey(), 0.0);
			}
			else{
				double normalizedValue=6 * (pair.getValue()-min+0.0d)/(max-min+0.0d);
//				if(normalizedValue>6){
//					normalizedValue=6;
//				}
				normalizedPairs.put(pair.getKey(), normalizedValue);
			}
		}
		return normalizedPairs;
	}
	
	
//	/**
//	 * Normalize to [0,1] using a linear transformation
//	 * @param originalPairs
//	 * @return
//	 */
//	public static HashMap<String, Double> normalize2(HashMap<String, Integer> originalPairs){
//		HashMap<String, Double> normalizedPairs=new HashMap<String, Double>();
//		int sum = 0;
//		int count = 0;
//		for(int value: originalPairs.values()){
//			sum+=value;
//			if(value!=0){
//				count++;
//			}
//		}
//		double average = sum / (double) count;
//		double squareDevi = 0;
//		
//		for (int value: originalPairs.values()) {
//			if (value != 0) {
//				squareDevi += (value - average) * (value - average);
//			}
//		}
//		double standardDevi = Math.sqrt(squareDevi / count);
//		double low = average - 3 * standardDevi;
//		double high = average + 3 * standardDevi;
//
//		int min = 0;
//		if (low > 0)
//			min = (int) low;
//		for (String key : originalPairs.keySet()) {
//			int len = originalPairs.get(key);
//			double score = 0.0;
//			double nor = getNormValue(len, high, min);
//			if (len != 0) {
//				if (len > low && len < high) {
//
//					score = getLenScore(nor);
//				} else if (len < low) {
//					score = 0.5;
//				} else {
//					score = 1.0;
//				}
//			} else {
//				score = 0.0;
//			}
//			if (nor > 6)
//				nor = 6;
//			if (score < 0.5)
//				score = 0.5f;
//			normalizedPairs.put(key, score);
//		}
//		return normalizedPairs;
//		
//		
//	}
//	public static float getNormValue(int x, double max, double min) {
//		return 6 * (float) (x - min) / (float) (max - min);
//	}
//
//	public static double getLenScore(double len) {
//		return (Math.exp(len) / (1 + Math.exp(len)));
//	}
	
	/**
	 * Nonlinear transform on the normalized pairs
	 * @param originalPairs
	 * @param functionType
	 * @return
	 */
	public static HashMap<String, Double> nonLinearTransform(HashMap<String, Double> originalPairs, String functionType){
		HashMap<String, Double> nonLinearTransformedPairs=new HashMap<String, Double>();
		for(Entry<String, Double> pair:originalPairs.entrySet()){
			double transformedValue=0.0d;
			if(functionType.toLowerCase().equals("logistic")){
				transformedValue=logistic(pair.getValue(),1);
				if(transformedValue<0.5){
					transformedValue=0.5;
				}
			}
			else if(functionType.toLowerCase().equals("exponential")){
				transformedValue=exponential(pair.getValue());
			}
			else if(functionType.toLowerCase().equals("squareroot")){
				transformedValue=squareRoot(pair.getValue());
			}
			else if(functionType.toLowerCase().equals("linear")){
				transformedValue=linear(pair.getValue());
			}
			else{
				System.out.println("The input function type is illegal, so the default function type, logistic function is used");
				transformedValue=logistic(pair.getValue(),1);
			}
			nonLinearTransformedPairs.put(pair.getKey(), transformedValue);
		}
		return nonLinearTransformedPairs;
	}
	
	/**
	 * Logistic function used for nonlinear transformation
	 * @param x
	 * @param beta
	 * @return
	 */
	public static double logistic(double x, double beta){
		return 1.0d/(1.0d+Math.exp(-beta*x));
	}
	
	/**
	 * Exponential function used for nonlinear transformation
	 * @param x
	 * @return
	 */
	public static double exponential(double x){
		return (Math.exp(x)-0.5d);
	}
	
	/**
	 * Square root function used for nonlinear transformation
	 * @param x
	 * @return
	 */
	public static double squareRoot(double x){
		return (1-1.0d/(Math.sqrt(x)));
	}
	
	/**
	 * linear function used for nonlinear transformation
	 * @param x
	 * @return
	 */
	public static double linear(double x){
		return (x/(2.0d)+0.5);
	}
	
	/**
	 * Save the <className, transformedLength> pairs to file
	 * @param dstFilePath
	 * @param pairs
	 * @throws Exception
	 */
	public static void savePairs(String dstFilePath, HashMap<String, Double> pairs) throws Exception{
		FileWriter writer=new FileWriter(dstFilePath);
		StringBuffer buf=new StringBuffer();
		for(Entry<String, Double> onepair:pairs.entrySet()){
			buf.append(onepair.getKey()+"\t"+onepair.getValue()+"\r\n");
		}
		writer.write(buf.toString());
		writer.close();
	}
	
	/**
	 * Load the <className, transformedLength> pairs from file
	 * @param srcFilePath
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Double> loadPairs(String srcFilePath) throws Exception{
		HashMap<String, Double> fileLengthPairs=new HashMap<String, Double>();
		if(!new File(srcFilePath).isFile()){
			System.out.println("The input file path is invalid!");
			return fileLengthPairs;
		}
		BufferedReader reader=new BufferedReader(new FileReader(srcFilePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			String strs[]=line.split("\t");
			String key=strs[0].trim();
			double value=Double.parseDouble(strs[1].trim());
			fileLengthPairs.put(key, value);
		}
		reader.close();
		return fileLengthPairs;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> testList=new HashMap<String, Integer>();
		testList.put("a", 1);
		testList.put("b", 2);
		testList.put("c", 2);
		testList.put("d", 2);
		testList.put("e", 2);
		testList.put("f", 2);
		testList.put("g", 30);
		HashMap<String, Double> normalizedList = normalize(testList);
		for(Entry<String, Double> pair: normalizedList.entrySet()){
			System.out.println(pair.getKey()+"\t"+pair.getValue());
		}
	}

}

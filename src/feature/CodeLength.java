package feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import sourcecode.CodeFeatureExtractor;
import sourcecode.SourceCodeCorpus;

public class CodeLength {
	public static void generate(SourceCodeCorpus corpus, String dstFilePath) throws Exception{
		HashMap<String, Integer> fileClassLengthPairs= CodeFeatureExtractor.extractCodeLength(corpus);
		HashMap<String, Double> normalizedLengths= normalize(fileClassLengthPairs);
		HashMap<String, Double> nonLinearTransformedLengths= nonLinearTransform(normalizedLengths, "logistic");
		savePairs(dstFilePath, nonLinearTransformedLengths);
	}
	
	/**
	 * Normalize to [0,1] using a linear transformation
	 * @param originalPairs
	 * @return
	 */
	public static HashMap<String, Double> normalize(HashMap<String, Integer> originalPairs){
		HashMap<String, Double> normalizedPairs=new HashMap<String, Double>();
		
		//Get the maximum and the minimum of the values 
		int maxVal=0;
		int minVal=-1;
		for(int value:originalPairs.values()){
			if(minVal==-1 || value<minVal){
				minVal=value;
			}
			if(maxVal<value){
				maxVal=value;
			}
		}
		
		//scale each value using the maximum and minimum value
		for(Entry<String, Integer> pair: originalPairs.entrySet()){
			double normalizedValue=(pair.getValue()-minVal+0.0d)/(maxVal-minVal+0.0d);
			normalizedPairs.put(pair.getKey(), normalizedValue);
		}
		return normalizedPairs;
	}
	
	
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

	}

}

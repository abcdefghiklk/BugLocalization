package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import Jama.Matrix;


public class MatrixUtil {
	
	/**
	 * Normalize the matrix to unit length
	 * @param inputMat
	 * @return
	 */
	public static Matrix NormalizeToUnitLength(Matrix inputMat){
		return(inputMat.times(1/inputMat.normF()));
	}
	
	
	/**
	 * Load the vector list from file
	 * @param srcFilePath
	 * @param dicSize
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Matrix> loadVectors(String srcFilePath, int dicSize) throws Exception{
		HashMap<String, Matrix> idVecPairs=new HashMap<String, Matrix>();
		if(!new File(srcFilePath).isFile()){
			System.out.println("the vector file is invalid!");
			return idVecPairs;
		}
		BufferedReader reader=new BufferedReader(new FileReader(srcFilePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			String []strs = line.split(" ");
			String bugID=strs[0].substring(0, strs[0].lastIndexOf(";"));
			Matrix fileVec=new Matrix(1,dicSize);
			if(strs.length>1){
				for(int j=1;j<strs.length;j++){
					String []indexValuePair = strs[j].split(":");
					if(indexValuePair.length==2){
						int index=Integer.parseInt(indexValuePair[0]);
						double value=Double.parseDouble(indexValuePair[1]);
						fileVec.set(0, index, value);
					}
				}
			}
			idVecPairs.put(bugID, fileVec);
		}
		reader.close();
		return idVecPairs;
	}
	
	/**
	 * Convert the occurrences map to other features map
	 * the feature types can be "tf", "logtf", "df", "idf", "tfidf", "logtfidf"
	 * Controlled by featureType
	 * @param occurrencesMap
	 * @param featureType
	 * @return
	 */
	public static HashMap<String, Matrix> convert(HashMap<String, Matrix> occurrencesMap, HashMap<String, Matrix> corpusOccurrencesMap, String featureType){
		HashMap<String, Matrix> outputMap = new HashMap<String, Matrix>();
		
		//constructing term document frequency vectors
		int rowCount=0;
		int colCount=0;
		for(Entry<String, Matrix> pair: corpusOccurrencesMap.entrySet()){
			rowCount=pair.getValue().getRowDimension();
			colCount=pair.getValue().getColumnDimension();
			break;
		}
		Matrix corpusDocOccurrences = new Matrix(rowCount,colCount);
		for(Entry<String, Matrix> pair: corpusOccurrencesMap.entrySet()){
			for(int i=0;i<corpusDocOccurrences.getRowDimension();i++){
				for(int j=0;j<corpusDocOccurrences.getColumnDimension();j++){
					if(pair.getValue().get(i, j)>0){
						corpusDocOccurrences.set(i, j, corpusDocOccurrences.get(i, j)+1);
					}
				}
			}
		}
		
		//constructing vectors for each input feature type
		for(Entry<String, Matrix> pair: occurrencesMap.entrySet()){
			Matrix mat=pair.getValue();
			double docTermCount=0.0d;
			for(int i=0;i<mat.getRowDimension();i++){
				for(int j=0;j<mat.getColumnDimension();j++){
					docTermCount+=mat.get(i, j);
				}
			}
			for(int i=0;i<mat.getRowDimension();i++){
				for(int j=0;j<mat.getColumnDimension();j++){
					
					//tf: Occurrence/documentSize 
					if(featureType.toLowerCase().equals("tf")){
						mat.set(i, j, mat.get(i, j));
					}
					
					//logtf: log(Occurrence)+1
					else if(featureType.toLowerCase().equals("logtf")){
						mat.set(i, j,  Math.log(mat.get(i,j))+1);
					}
					
					//df: documentNumOccurrence/corpusSize
					else if(featureType.toLowerCase().equals("df")){
//						System.out.println("program goes to df");
						mat.set(i, j, corpusDocOccurrences.get(i, j));
					}
					
					//idf: corpusSize/documentNumOccurrence
					else if(featureType.toLowerCase().equals("idf")){
//						System.out.println("program goes to idf");
						if(corpusDocOccurrences.get(i, j)>0){
							mat.set(i, j, Math.log((corpusOccurrencesMap.size()+0.0d)/corpusDocOccurrences.get(i, j)));
						}
					}
					
					//tfidf: tf*idf
					else if(featureType.toLowerCase().equals("tfidf")){
//						System.out.println("program goes to tfidf");
						if(corpusDocOccurrences.get(i, j)>0){
							mat.set(i, j, (Math.log(corpusOccurrencesMap.size()/corpusDocOccurrences.get(i, j)))*(mat.get(i, j)/docTermCount));
						}
					}
					
					//logtfidf:logtf*idf
					else if(featureType.toLowerCase().equals("logtfidf")){
//						System.out.println("program goes to logtfidf");
						if(corpusDocOccurrences.get(i, j)>0 && mat.get(i, j)>0){
							mat.set(i, j, (Math.log(corpusOccurrencesMap.size()/corpusDocOccurrences.get(i, j)))*(Math.log(mat.get(i, j))+1));
						}
					}
					else{
						System.out.println("The input feature type is invalid! The default type tfidf will be used");
						if(corpusDocOccurrences.get(i, j)>0){
							mat.set(i, j, (Math.log(corpusOccurrencesMap.size()/corpusDocOccurrences.get(i, j)))*(mat.get(i, j)/docTermCount));
						}
					}
				}
			}
			outputMap.put(pair.getKey(), mat);
		}
		return outputMap;
	}
	
	
	/**
	 * Compute similarity matrix for the two given list of vectors
	 * @param idMatPairs1
	 * @param idMatPairs2
	 * @param dicSize
	 * @return
	 */
	public static Matrix computeSimilarityMatrix(HashMap<String, Matrix> idMatPairs1, HashMap<String, Matrix> idMatPairs2, int dicSize){
		int rowCount=idMatPairs1.size();
		int colCount=idMatPairs2.size();
		Matrix simMat=new Matrix(rowCount, colCount);
		Matrix []matList1=idMatPairs1.values().toArray(new Matrix[0]);
		Matrix []matList2=idMatPairs2.values().toArray(new Matrix[0]);
		for (int i=0; i<rowCount; i++){
			for (int j=0; j<colCount; j++){
				simMat.set(i, j, computeCosSimilarity(matList1[i], matList2[j]));
			}
		}
		return simMat;
	}
	
	/**
	 * save the vector list to file
	 * @param srcFilePath
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static void saveVectors(HashMap<String, Matrix> map, String dstFilePath) throws Exception{
		FileUtils.deleteExistingFile(dstFilePath);
		for(Entry<String, Matrix> pair: map.entrySet()){
			StringBuffer buf=new StringBuffer();
			buf.append(pair.getKey()+";");			
			Matrix mat=pair.getValue();
			for(int i=0;i< mat.getRowDimension();i++){
				for(int j=0;j<mat.getColumnDimension();j++){
					if(mat.get(i, j)>0){
						buf.append(" "+j+":"+mat.get(i, j));
					}
				}
			}
			FileUtils.write_append2file(buf.toString()+"\n", dstFilePath);
		}
	}
	/**
	 * Compute the similarity matrix given a list of vectors
	 * @param idFilePairs
	 * @param dicSize
	 * @return
	 */
	public static Matrix computeSimilarityMatrix(HashMap<String, Matrix> idMatPairs, int dicSize){
		Matrix simMat=new Matrix(dicSize, dicSize);
		Matrix []matList=idMatPairs.values().toArray(new Matrix[0]);
		for (int i=0; i<matList.length; i++){
			for (int j=0; j<matList.length; j++){
				simMat.set(i, j, computeCosSimilarity(matList[i], matList[j]));
			}
		}
		return simMat;
	}
	
	/**
	 * for two vectors(matrices), compute their cosine similarity
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static double computeCosSimilarity(Matrix vec1, Matrix vec2){
		int rowCount= vec1.getRowDimension();
		int columnCount = vec1.getColumnDimension();
		if(rowCount!=vec2.getRowDimension() || columnCount!=vec2.getColumnDimension()){
			System.out.println("The dimensions of the input matrices does not agree!");
			return -1;
		}
		double dotProduct=0.0f;
		double lenVec1=0.0f;
		double lenVec2=0.0f;
		for(int r=0; r<rowCount; r++){
			for(int c=0; c<columnCount; c++){
				double val1=vec1.get(r, c);
				double val2=vec2.get(r, c);
				dotProduct+=val1*val2;
				lenVec1+=val1*val1;
				lenVec2+=val2*val2;
			}
		}
		if(dotProduct==0.0){
			return 0.0;
		}
		return dotProduct/Math.sqrt(lenVec1*lenVec2);
	}
	
	/**
	 * Export the similarity Matrix to file
	 * @param idFilePairs
	 * @param dstFilePath
	 * @param dicSize(can be any larger than the vector length)
	 * @throws IOException
	 */
	public static void exportSimilarityMatrix(HashMap<String, Matrix> idMatPairs, String dstFilePath, int dicSize) throws IOException{
		FileUtils.deleteExistingFile(dstFilePath);
		StringBuffer buf=new StringBuffer();
		for(String id: idMatPairs.keySet()){
			buf.append(id+"\t");
		}
		buf.append("\n");
		Matrix simMat=computeSimilarityMatrix(idMatPairs,dicSize);
		for(int i=0;i<simMat.getRowDimension();i++){
			for(int j=0;j<simMat.getColumnDimension();j++){
				buf.append(String.valueOf(simMat.get(i, j))+"\t");
			}
			buf.append("\n");
		}
		FileUtils.write_append2file(buf.toString(), dstFilePath);
	}
	
	/**
	 * Export the rowMap, colMap and matrix to the file
	 * @param rowMap
	 * @param colMap
	 * @param mat
	 * @param dstFilePath
	 * @throws IOException
	 */
	public static void exportMatrix(HashMap<String, Matrix> rowMap, HashMap<String, Matrix> colMap, Matrix mat, String dstFilePath) throws IOException{
		FileUtils.deleteExistingFile(dstFilePath);
		StringBuffer buf=new StringBuffer();
		//First line saves the row ids
		for(String id: rowMap.keySet()){
			buf.append(id+"\t");
		}
		buf.append("\n");
		
		//Second line saves the column ids
		for(String id: colMap.keySet()){
			buf.append(id+"\t");
		}
		buf.append("\n");
		for(int i=0;i<mat.getRowDimension();i++){
			for(int j=0;j<mat.getColumnDimension();j++){
				buf.append(String.valueOf(mat.get(i, j))+"\t");
			}
			buf.append("\n");
		}
		FileUtils.write_append2file(buf.toString(), dstFilePath);
	}
	
	/**
	 * Export the rowList, colList and Matrix to the file
	 * @param rowList
	 * @param colList
	 * @param mat
	 * @param dstFilePath
	 * @throws IOException
	 */
	public static void exportMatrix(ArrayList<String> rowList, ArrayList<String> colList, Matrix mat, String dstFilePath) throws IOException{
		FileUtils.deleteExistingFile(dstFilePath);
		StringBuffer buf=new StringBuffer();
		//First line saves the row ids
		for(String id: rowList){
			buf.append(id+"\t");
		}
		buf.append("\n");
		
		//Second line saves the column ids
		for(String id: colList){
			buf.append(id+"\t");
		}
		buf.append("\n");
		for(int i=0;i<mat.getRowDimension();i++){
			for(int j=0;j<mat.getColumnDimension();j++){
				buf.append(String.valueOf(mat.get(i, j))+"\t");
			}
			buf.append("\n");
		}
		FileUtils.write_append2file(buf.toString(), dstFilePath);
	}
	
	/**
	 * Export the similarity Matrix to file
	 * @param idMatPairs1
	 * @param idMatPairs2
	 * @param dstFilePath
	 * @param dicSize(can be any larger than the vector length)
	 * @throws IOException
	 */
	public static void exportSimilarityMatrix(HashMap<String, Matrix> idMatPairs1, HashMap<String, Matrix> idMatPairs2, String dstFilePath, int dicSize) throws IOException{
		Matrix simMat=computeSimilarityMatrix(idMatPairs1,idMatPairs2,dicSize);
		exportMatrix(idMatPairs1,idMatPairs2,simMat,dstFilePath);
	}
	
	
	/**
	 * Import the similarity matrix from file,
	 * The information is saved in simMat and idList
	 * @param simMat
	 * @param idList
	 * @param srcFilePath
	 * @throws Exception
	 */
	public static Matrix importSimilarityMatrix(ArrayList<String> idList, String srcFilePath) throws Exception{
		if(!new File(srcFilePath).isFile()){
			System.out.println("The similarity file path is invalid");
			return Matrix.random(0, 0);
		}
		
		//The file format:
		//id1\tid2\t...\tidk
		//entry(1,1)\tentry(1,2)\t...entry(1,k)
		//...
		//entry(k,1)\tentry(k,2)\t...entry(k,k)
		BufferedReader reader=new BufferedReader(new FileReader(srcFilePath));
		String line=reader.readLine();
		String []ids=line.split("\t");
		idList.clear();
		for(String id:ids){
			idList.add(id);
		}
		
		Matrix simMat=Matrix.random(idList.size(),idList.size());
		int i=0;
		while((line=reader.readLine())!=null){
			String []strs=line.split("\t");
			for(int j=0; j<strs.length;j++){
				simMat.set(i, j, Double.parseDouble(strs[j]));
			}
		}
		reader.close();
		return simMat;
	}
	
	/**
	 * Import the similarity matrix from file,
	 * The information is saved in simMat and idList1, idList2
	 * @param idList1
	 * @param idList2
	 * @param srcFilePath
	 * @throws Exception
	 */
	public static Matrix importSimilarityMatrix(ArrayList<String> idList1, ArrayList<String> idList2, String srcFilePath) throws Exception{
		if(!new File(srcFilePath).isFile()){
			System.out.println("The similarity file path is invalid");
			return Matrix.random(0, 0);
		}
		
		//The file format:
		//id1\tid2\t...\tidk
		//entry(1,1)\tentry(1,2)\t...entry(1,k)
		//...
		//entry(k,1)\tentry(k,2)\t...entry(k,k)
		BufferedReader reader=new BufferedReader(new FileReader(srcFilePath));
		String line=reader.readLine();
		String []ids=line.split("\t");
		idList1.clear();
		for(String id:ids){
			idList1.add(id);
		}
		line=reader.readLine();
		ids=line.split("\t");
		idList2.clear();
		for(String id:ids){
			idList2.add(id);
		}
		Matrix simMat=Matrix.random(idList1.size(), idList2.size());
		int i=0;
		while((line=reader.readLine())!=null){
			String []strs=line.split("\t");
//			System.out.println(strs.length);
			for(int j=0; j<strs.length;j++){
				simMat.set(i, j, Double.parseDouble(strs[j]));
			}
			i++;
		}
		reader.close();
		return simMat;
	}
	
	/**
	 * Determine whether a given indexSet contains the topK entry in the whole row
	 * For the topK metric
	 * @param colIndexSet
	 * @param rowNum
	 * @param mat
	 * @param K
	 * @return
	 */
	public static boolean isInTopK(ArrayList<Integer> colIndexSet, int rowNum, Matrix mat, int K){
		//obtain the largest value in the indexSet
		double maxValue=0;
		for(int index: colIndexSet){
			double value=mat.get(rowNum, index);
			if(value>maxValue){
				maxValue=value;
			}
		}
		
		//find out how many entries are larger than than the maximum
		int countOfLargerItems=0;
		for(int i=0;i<mat.getColumnDimension();i++){
			if(mat.get(rowNum, i)>maxValue){
				countOfLargerItems+=1;
			}
		}
		
		//compare this with K to determine whether it contains the topK entry
		return (countOfLargerItems<K);
	}
	
	/**
	 * Obtain the rank of the target column index in the whole row determined by the rowIndex
	 * @param rowIndex
	 * @param colIndex
	 * @param mat
	 * @return
	 */
	public static int getRank(int rowIndex, int colIndex, Matrix mat){
		int rank=1;
		for(int i=0;i< mat.getColumnDimension();i++){
			if(mat.get(rowIndex, i)>mat.get(rowIndex, colIndex)){
				rank++;
			}
		}
		return rank;
	}
	
	/**
	 * Get the index of the target string in the string array
	 * @param targetString
	 * @param strArray
	 * @return
	 */
	public static int getIndex(String targetString,String []strArray){
		for(int i=0;i<strArray.length;i++){
			if(strArray[i].equals(targetString)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Linear combination of two matrix file
	 * @param matFilePath1
	 * @param matFilePath2
	 * @param dstFilePath
	 * @param linearInterpolator
	 * @throws Exception 
	 */
	public static void linearCombination(String matFilePath1, String matFilePath2, String dstFilePath, double linearInterpolator) throws Exception{
		ArrayList<String> rowList=new ArrayList<String> ();
		ArrayList<String> colList=new ArrayList<String> ();
		Matrix mat1=importSimilarityMatrix(rowList, colList, matFilePath1);
		Matrix mat2=importSimilarityMatrix(rowList, colList, matFilePath2);
		if(mat1.getRowDimension()!=mat2.getRowDimension() || mat1.getColumnDimension()!=mat2.getColumnDimension()){
			System.out.println("error--the two matrices are different in size!");
			return;
		}
		Matrix resultMat=Matrix.random(mat1.getRowDimension(), mat1.getColumnDimension());
		for(int i=0; i<mat1.getRowDimension();i++){
			for(int j=0;j<mat2.getColumnDimension();j++){
				resultMat.set(i, j, linearInterpolator*mat1.get(i, j)+(1-linearInterpolator)*mat2.get(i, j));
			}
		}
		exportMatrix(rowList,colList,resultMat,dstFilePath);
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		HashMap<String, Matrix> map1=new HashMap<String, Matrix>();
//		Matrix mat=new Matrix(1,3);
//		mat=Matrix.random(1, 3);
//		map1.put("A2", mat);
//		mat=Matrix.random(1, 3);
//		map1.put("A1", mat);
//		mat=Matrix.random(1, 3);
//		map1.put("A3", mat);
//		
//		for(String oneKey:map1.keySet().toArray(new String[0])){
//			System.out.println(oneKey);
//		}
//		HashMap<String, Matrix> map2=new HashMap<String, Matrix>();
//		mat=Matrix.random(1, 3);
//		mat.print(5, 2);
//		map2.put("B2", mat);
//		mat=Matrix.random(1, 3);
//		mat.print(5, 2);
//		map2.put("B1", mat);
//		mat=Matrix.random(1, 3);
//		mat.print(5, 2);
//		map2.put("B3", mat);
		
//		Matrix simMat=computeSimilarityMatrix(map1, map2, 3);
//		simMat.print(5, 2);
//		Matrix mat=Matrix.random(1, 3); 
//		mat.print(5, 2);
//		Matrix mat2=Matrix.random(1, 3);
//		mat2.print(5, 2);
//		double simVal=computeCosSimilarity(mat,mat2);
//		System.out.println(simVal);
		
		
	}

}

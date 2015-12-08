package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import Jama.Matrix;


public class MatrixUtil {

	
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
			System.out.println("Input File invalid!");
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
						fileVec.set(0, index-1, value);
					}
				}
			}
			idVecPairs.put(bugID, fileVec);
		}
		reader.close();
		return idVecPairs;
	}
	
	/**
	 * Compute the similarity matrix given a list of vectors
	 * @param idFilePairs
	 * @param dicSize
	 * @return
	 */
	public static Matrix computeSimilarityMatrix(HashMap<String, Matrix> idFilePairs, int dicSize){
		Matrix simMat=new Matrix(dicSize, dicSize);
		Matrix []matList=idFilePairs.values().toArray(new Matrix[0]);
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
	 * @param dicSize
	 * @throws IOException
	 */
	public static void exportSimilarityMatrix(HashMap<String, Matrix> idFilePairs, String dstFilePath, int dicSize) throws IOException{
		FileWriter writer=new FileWriter(dstFilePath);
		StringBuffer buf=new StringBuffer();
		for(String id: idFilePairs.keySet()){
			buf.append(id+"\t");
		}
		buf.append("\n");
		Matrix simMat=computeSimilarityMatrix(idFilePairs,dicSize);
		for(int i=0;i<simMat.getRowDimension();i++){
			for(int j=0;j<simMat.getColumnDimension();j++){
				buf.append(String.valueOf(simMat.get(i, j))+"\t");
			}
			buf.append("\n");
		}
		writer.write(buf.toString());
		writer.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		double [][]vec1={{0.1,0.2,0.3}};
		double [][]vec2={{0.3,0.5,0.6}};
		double [][]vec3={{0.4,0.8,0.8}};
		Matrix mat1=new Matrix(vec1);
		Matrix mat2=new Matrix(vec2);
		Matrix mat3=new Matrix(vec3);
		String id1="a";
		String id2="b";
		String id3="c";
		HashMap<String, Matrix> idMatPairs=new HashMap<String,Matrix>();
		idMatPairs.put(id1, mat1);
		idMatPairs.put(id2, mat2);
		idMatPairs.put(id3, mat3);
		String pathFile="C:/Users/ql29/Documents/EClipse/BugCorpus/similarityMat";
		exportSimilarityMatrix(idMatPairs,pathFile,3);
	}

}
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileUtils {
	/**
	 *  Appends a given textual contents to an existing text file.
	 *  Creates the file if it does not exist before adding textual contents
	 * @param str Given textual contents
	 * @param outputFilepath Full path of text file to add textual contents
	 */
	public static void write_append2file(String str, String outputFilepath) {
		try {
			File f = new File(outputFilepath);
			
			if (!f.exists()) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.write("");
				bw.close();
			}
			FileWriter writer = new FileWriter(f,true);
//			System.out.println(writer.getEncoding());;
			BufferedWriter bw = new BufferedWriter(writer);
			bw.append(str);
			bw.close();

		} catch (Exception e) {
			System.out.println("Writing to file error- " + e);
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

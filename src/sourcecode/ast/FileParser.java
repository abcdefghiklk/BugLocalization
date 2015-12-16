package sourcecode.ast;

import java.io.*;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;


import utils.Splitter;

public class FileParser {

	private CompilationUnit cu = null;

	/**
	 * Initialize a file parser given the target file
	 * @param file
	 */
	public FileParser(File file) {
		ASTCreator creator = new ASTCreator();
		creator.getFileContent(file);
		cu = creator.getCompilationUnit();
	}
	
	/**
	 * Initialize a file parser given the target file path
	 * @param filePath
	 * @throws Exception
	 */
	public FileParser(String filePath) throws Exception{
		StringBuffer buf=new StringBuffer();
		BufferedReader reader=new BufferedReader(new FileReader(filePath));
		String line=new String();
		while((line=reader.readLine())!=null){
			buf.append(line+"\r\n");
		}
		reader.close();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(buf.toString().toCharArray());
		cu = (CompilationUnit) parser.createAST(null);
	}

	/**
	 * Get the lines of code of a source code file
	 * @return
	 */
	public int getLinesOfCode() {
		this.deleteNoNeededNode();
		String[] lines = cu.toString().split("\n");
		int len = 0;
		for (String strLine : lines) {
			if (!strLine.trim().equals("")) {
				len++;

			}
		}
		return len;
	}

	/**
	 * Get the content of a source code file 
	 * @return
	 */
	public String[] getContent() {
		String[] tokensInSourceCode = Splitter.splitSourceCode(this
				.deleteNoNeededNode());
		StringBuffer sourceCodeContentBuffer = new StringBuffer();
		for (String token : tokensInSourceCode) {
			sourceCodeContentBuffer.append(token + " ");
		}
		String content = sourceCodeContentBuffer.toString().toLowerCase();
		return content.split(" ");
	}
	/**
	 * Get the class and method names of a source code file
	 * @return
	 */
	public String[] getClassNameAndMethodName() {
		String content = (this.getAllClassName() + " " + this
				.getAllMethodName()).toLowerCase();
		return content.split(" ");
	}

	/**
	 * Get the package name of a source code file
	 * @return
	 */
	public String getPackageName() {

		return cu.getPackage() == null ? "" : cu.getPackage().getName()
				.getFullyQualifiedName();
	}

	/**
	 * Get all method names of a source code file
	 * @return
	 */
	public String getAllMethodName() {
		ArrayList<String> methodNameList = new ArrayList<String>();
		for (int i = 0; i < cu.types().size(); i++) {
			TypeDeclaration type = (TypeDeclaration) cu.types().get(i);
			MethodDeclaration[] methodDecls = type.getMethods();
			for (MethodDeclaration methodDecl : methodDecls) {
				String methodName = methodDecl.getName()
						.getFullyQualifiedName();
				methodNameList.add(methodName);
			}
		}
		String allMethodName = "";
		for (String methodName : methodNameList) {
			allMethodName += methodName + " ";
		}
		return allMethodName.trim();

	}

	/**
	 * Get all class names of a source code file
	 * @return
	 */
	public String getAllClassName() {
		ArrayList<String> classNameList = new ArrayList<String>();
		for (int i = 0; i < cu.types().size(); i++) {
			TypeDeclaration type = (TypeDeclaration) cu.types().get(i);
			String name = type.getName().getFullyQualifiedName();
			classNameList.add(name);
		}
		String allClassName = "";
		for (String className : classNameList) {
			allClassName += className + " ";
		}
		return allClassName.trim();
	}

	/**
	 * Delete all useless nodes, including
	 * I) package member type declaration nodes
	 * II) package declaration nodes
	 * III) import declaration nodes
	 ** @return
	 */
	private String deleteNoNeededNode() {
		cu.accept(new ASTVisitor() {
			public boolean visit(AnnotationTypeDeclaration node) {
				if (node.isPackageMemberTypeDeclaration()) {

					node.delete();
				}
				return super.visit(node);
			}
		});
		cu.accept(new ASTVisitor() {
			public boolean visit(PackageDeclaration node) {
				node.delete();
				return super.visit(node);
			}
		});
		cu.accept(new ASTVisitor() {
			public boolean visit(ImportDeclaration node) {
				node.delete();
				return super.visit(node);
			}
		});
		return cu.toString();
	}
	/**
	 * Export all import information into a file for a given source code file
	 * @param writeImport
	 */
	public void getImport(final FileWriter writeImport){
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(ImportDeclaration node) {
				try {
					writeImport.write(node.getName() + " ");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return super.visit(node);
			}
		});
	}
}

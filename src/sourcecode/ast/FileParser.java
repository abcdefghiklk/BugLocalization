package sourcecode.ast;

import java.io.*;
import java.util.ArrayList;
<<<<<<< HEAD

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
=======
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec

import utils.Splitter;

public class FileParser {

	private CompilationUnit cu = null;

	/**
<<<<<<< HEAD
	 * Initialize a file parser given the target file
	 * @param file
=======
	 * ���ָ����java�ļ���ʼ��CompilationUnit
	 * 
	 * @param file:java �ļ�
	 *            
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
	 */
	public FileParser(File file) {
		ASTCreator creator = new ASTCreator();
		creator.getFileContent(file);
		cu = creator.getCompilationUnit();
	}
<<<<<<< HEAD
	
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
=======

	/**
	 * ��ȡjava�ļ��Ĵ�������
	 * 
	 * @return ��������
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
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
<<<<<<< HEAD
	 * Get the content of a source code file 
	 * @return
=======
	 * ��ȡ����ı��ĵ���
	 * 
	 * @return ����ı��ĵ�������
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
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
<<<<<<< HEAD
	
	/**
	 * Get the class and method names of a source code file
	 * @return
	 */
=======

>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
	public String[] getClassNameAndMethodName() {
		String content = (this.getAllClassName() + " " + this
				.getAllMethodName()).toLowerCase();
		return content.split(" ");
	}

	/**
<<<<<<< HEAD
	 * Get the package name of a source code file
	 * @return
=======
	 * ��ȡ�ļ����ڰ���
	 * 
	 * @return ����
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
	 */
	public String getPackageName() {

		return cu.getPackage() == null ? "" : cu.getPackage().getName()
				.getFullyQualifiedName();
	}

	/**
<<<<<<< HEAD
	 * Get all method names of a source code file
	 * @return
	 */
	public String getAllMethodName() {
=======
	 * ��ȡ�ļ��е����з�����
	 * 
	 * @return ��������ɵ��ַ�
	 */
	private String getAllMethodName() {
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
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
<<<<<<< HEAD
	 * Get all class names of a source code file
	 * @return
	 */
	public String getAllClassName() {
=======
	 * ��ȡ�ļ��е���������
	 * 
	 * @return ������ɵ��ַ�
	 */
	private String getAllClassName() {
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
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
<<<<<<< HEAD
	 * Delete all useless nodes, including
	 * I) package member type declaration nodes
	 * II) package declaration nodes
	 * III) import declaration nodes
	 ** @return
=======
	 * ɾ���ļ��в���Ҫ����Ϣ
	 * 
	 * @return �ļ����ַ��ʾ
>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
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
<<<<<<< HEAD
	
	/**
	 * Export all import information into a file for a given source code file
	 * @param writeImport
	 */
=======

>>>>>>> f7a82a6109bed7ab926545abb4e8c76590a8f4ec
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

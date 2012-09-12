/*
 * Created by Bryan Saunders
 * Created on Aug 24, 2004
 */
import java.io.*;

import javax.swing.*;
public class Plan9Code {
	private JFileChooser fileGrab;
	private JTextArea textArea;
	private String sourceCode;
	
	public Plan9Code(JFileChooser file, JTextArea tArea){
		fileGrab = file;
		textArea = tArea;
	}
	
	public String openCode(){
		String code = "";
		String fileName = "";
		int c = fileGrab.showOpenDialog(null);
		if(c == JFileChooser.APPROVE_OPTION){
			// File Selected
			fileName = fileGrab.getSelectedFile().getName();
			sourceCode = parseFile(fileGrab.getSelectedFile());
			textArea.setText(sourceCode);
			return fileName+" Loaded.";
		} else {
			// File Dialog Cancelled
			return "Please Open Code File...";
		}
	}
	
	public String saveCode(){
		String fileName;
		String fileDir;
		int c = fileGrab.showSaveDialog(null);
		if(c == JFileChooser.APPROVE_OPTION){
			// File Selected
			try{
				System.out.println(sourceCode);
				System.out.println("===============");
				System.out.println(textArea.getText());
				fileName = fileGrab.getSelectedFile().getName();
				fileDir = fileGrab.getCurrentDirectory().getCanonicalPath();
				PrintWriter fout = new PrintWriter(new FileWriter(fileGrab.getSelectedFile()));
				fout.print(sourceCode);
				fout.close();
				return fileName+" Saved.";
			}catch(IOException e){
				return "ERROR SAVING FILE";
			}
		} else {
			// File Dialog Cancelled
			return "Code not Saved.";
		}
	}
	
	public void updateSource(){
		sourceCode = textArea.getText();
	}
	
	private String parseFile(File f) throws Plan9Exception{
		String code = "";
		try{
			BufferedReader fin = new BufferedReader(new FileReader(f));
			code += fin.readLine()+"\n";
			while(fin.ready()){
				code += fin.readLine()+"\n";
			}
			return code;
		}catch(IOException e){
			e.printStackTrace();
			throw new Plan9Exception("ERROR READING FILE");
		}
	}
	
	public String getSource(){
		return sourceCode;
	}
}

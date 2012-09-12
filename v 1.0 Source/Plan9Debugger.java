/*
 * Created by Bryan Saunders
 * Created on Aug 23, 2004
 */
import java.io.*;
import javax.swing.*;

public class Plan9Debugger {
	private JFileChooser fileGrab;
	private String debugReport;
	
	public Plan9Debugger(JFileChooser fileGraber){
		debugReport = "Starting Debug Report::\n";
		fileGrab = fileGraber;
	}
	
	public void println(String line){
		debugReport += line + "\n";
	}
	
	public void printCmd(int row,int col,int cnt,char dir,String cmd){
		debugReport += "Running Command: "+cmd;
		debugReport += "-Row:"+row+"-Col:"+col+"-Dir:"+dir+"-Cnt:"+cnt;
		debugReport += "\n";
	}
	
	public void printStart(int row,int col, char dir){
		debugReport += "Starting Run @ R"+row+",C"+col+"Facing "+dir;
		debugReport += "\n";
	}
	
	public String printReport(){
		return debugReport;
	}
	
	public void resetReport(){
		debugReport = "";
	}
	
	public String saveReport(){
		String fileName;
		String fileDir;
		int c = fileGrab.showSaveDialog(null);
		if(c == JFileChooser.APPROVE_OPTION){
			// File Selected
			try{
				fileName = fileGrab.getSelectedFile().getName();
				fileDir = fileGrab.getCurrentDirectory().getCanonicalPath();
				PrintWriter fout = new PrintWriter(new FileWriter(fileGrab.getSelectedFile()));
				fout.print(debugReport);
				fout.close();
				return "Report Saved.("+fileName+")";
			}catch(IOException e){
				return "ERROR SAVING FILE";
			}
		} else {
			// File Dialog Cancelled
			return "Code not Saved.";
		}
	}
}

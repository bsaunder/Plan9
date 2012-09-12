/*
 * Created by Bryan Saunders
 * Created on Aug 24, 2004
 */
import java.util.*;

import javax.swing.*;
public class Plan9Executer {
	private Plan9Debugger debug;
	private int row,col;
	private int loopRuns = 0;
	private int userCount = 0;
	private char dir;
	private char fillChar = '*';
	private char grid[][] = new char[10][10];
	private boolean isHidden = false;
	private boolean isInitialized = false;
	private boolean ranView = false;
	private String sourceCode;
	
	public Plan9Executer(Plan9Debugger d, Plan9Code src){
		sourceCode = src.getSource();
		debug = d;
		eraseGrid();
	}
	
	public String executeCode(){
		if(getStart()){
			return runCode();
		}else{
			debug.println("INVALID START INFO("+row+","+col+")"+dir);
			return "INVALID START INFO"; 
		}
	}
		
	private String runCode(){
		// Run Code
		debug.println("Running Code...");
		String line;
		String loopCode = "";
		int lineNum = 1;
		StringTokenizer tokenizer = new StringTokenizer(sourceCode);
		// Start Processing Code
		while(tokenizer.hasMoreTokens()){
			line = tokenizer.nextToken();
			switch(Character.toUpperCase(line.charAt(0))){
				case 'M':
					try{
						move();
					}catch(Plan9Exception e){
						return e.getMessage()+lineNum;
					}
					break;
				case 'D':
					dir(getChar(line));
					break;
				case 'I':
					init(getInt(line));
					break;
				case 'S':
					if(line.equals("SHOW;")){
						show();
					}else{
						set(getInt(line));
					}
					break;
				case 'C':
					change(getInt(line));
					break;
				case 'H':
					hide();
					break;
				case 'L':
					boolean inLoop = true;
					loopRuns = getInt(line);
					int loopLine = lineNum;
					while(inLoop){
						line = tokenizer.nextToken();
						lineNum++;
						if(line.toUpperCase().equals("ENDLOOP;")){
							inLoop = false;
						} else {
							loopCode += line;
						}
					}
					//System.out.println(loopCode);
					for(int i=0;i<loopRuns;i++){
						runLoop(loopCode,loopLine);
					}
					loopCode = "";
					break;
				case 'V':
					view();
					break;
				case 'E':
					break;
			}
		}
		debug.println("Code Run Complete.");
		return "Code Run Complete";
	}
	
	public String printEnd(){
		String finalRun = "";
		//grid[row][col] = fillChar;
		for(int r=9;r>=0;r--){
			finalRun += "["+r+"]";
			for(int c=0;c<10;c++){
				finalRun += " "+grid[r][c]+" ";
			}
			finalRun += "\n";
		}
		finalRun += "[ ][0][1][2][3][4][5][6][7][8][9]\n";
		finalRun += "\n";
		finalRun += "Row: "+row+" - Col: "+col+" - Dir: "+dir;
		if(ranView){
			finalRun += "\nCount:"+userCount;
		}
		return finalRun;
	}
	
	private void runLoop(String loopCode,int startLine) throws Plan9Exception{
		String line;
		int lineNum = startLine;
		StringTokenizer tokenizer = new StringTokenizer(loopCode,";");
		while(tokenizer.hasMoreTokens()){
			line = tokenizer.nextToken();
			switch(Character.toUpperCase(line.charAt(0))){
				case 'M':
					try{
						move();
					}catch(Plan9Exception e){
						throw new Plan9Exception(e.getMessage()+lineNum);
					}
					break;
				case 'D':
					dir(getChar(line));
					break;
				case 'I':
					init(getInt(line));
					break;
				case 'S':
					if(line.equals("SHOW;")){
						show();
					}else{
						set(getInt(line));
					}
					break;
				case 'C':
					change(getInt(line));
					break;
				case 'H':
					hide();
					break;
				case 'L': 
					debug.println("LOOP Embedding Not Supported!!");
					break;
				case 'V':
					view();
					break;
			}
		}
	}
	
	private void view(){
		ranView = true;
		debug.printCmd(row,col,userCount,dir,"VIEW");
	}
	
	private void dir(char d){
		dir = d;
		debug.printCmd(row,col,userCount,dir,"DIR:"+d);
	}
	
	private void set(int x){
		userCount = x;
		debug.printCmd(row,col,userCount,dir,"SET:"+x);
	}
	
	private void change(int x){
		userCount += x;
		debug.printCmd(row,col,userCount,dir,"CHANGE:"+x);
	}
	
	private void init(int x){
		userCount = x;
		isInitialized = true;
		debug.printCmd(row,col,userCount,dir,"SET:"+x);
	}
	
	private void move() throws Plan9Exception{
		try{
			switch(dir){
			case 'N':
				row++;
				break;
			case 'S':
				row--;
				break;
			case 'E':
				col++;
				break;
			case 'W':
				col--;
				break;
			default:
				break;
			}
			grid[row][col] = fillChar;
			debug.printCmd(row,col,userCount,dir,"MOVE1");
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.print(debug.printReport());
			throw new Plan9Exception("MOVES OFF COURSE::");
		}
	}
	
	private void hide(){
		isHidden = true;
		fillChar = ' ';
		debug.printCmd(row,col,userCount,dir,"HIDE");
	}
	
	private void show(){
		isHidden = false;
		fillChar = '*';
		debug.printCmd(row,col,userCount,dir,"SHOW");
	}
	
	private boolean getStart(){
		// Set Starting Values
		row = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Starting Row"));
		col = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Starting Col"));
		dir = Character.toUpperCase(JOptionPane.showInputDialog(null,"Enter Starting Dir").charAt(0));
		
		boolean rowCheck = checkInt(row);
		boolean colCheck = checkInt(col);
		boolean dirCheck = checkDir(dir);
		
		if(rowCheck && colCheck && dirCheck){
			grid[row][col] = fillChar;
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkInt(int x){
		if((-127<x)&&(x<128)){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean checkDir(char d){
		switch(d){
			case 'N':
			case 'S':
			case 'E':
			case 'W':
				return true;
			default:
				return false;
		}
	}
	
	private int getInt(String cmd){
		StringTokenizer lineTokenizer = new StringTokenizer(cmd,"(");
		lineTokenizer.nextToken();
		String temp = lineTokenizer.nextToken();
		lineTokenizer = new StringTokenizer(temp,")");
		return Integer.parseInt(lineTokenizer.nextToken().toString());
	}
	
	private char getChar(String cmd){
		StringTokenizer lineTokenizer = new StringTokenizer(cmd,"(");
		lineTokenizer.nextToken();
		String temp = lineTokenizer.nextToken();
		lineTokenizer = new StringTokenizer(temp,")");
		return lineTokenizer.nextToken().charAt(0);
	}
	
	private void eraseGrid(){
		for(int r = 0;r<10;r++){
			for(int c = 0;c<10;c++){
				grid[r][c] = ' ';
			}
		}
	}
}

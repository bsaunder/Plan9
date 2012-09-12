/*
 * Created by Bryan Saunders
 * Created on Aug 23, 2004
 */
import java.util.*;

public class Plan9Checker {
	private String code;
	private boolean validCode_flag = false;
	private LinkedList insSet = new LinkedList();
	private Plan9Debugger debug;
	
	public Plan9Checker(Plan9Debugger debugger,Plan9Code src){
		code = src.getSource();
		debug = debugger;
		// Set Valid Instructions
		insSet.add("MOVE1");
		insSet.add("DIR");
		insSet.add("INIT");
		insSet.add("SET");
		insSet.add("CHANGE");
		insSet.add("LOOP");
		insSet.add("ENDLOOP");
		insSet.add("HIDE");
		insSet.add("SHOW");
		insSet.add("VIEW");
		debug.println("Setting Instruction Set.");
	}
	
	public boolean codeIsValid(){
		return validCode_flag;
	}
	
	public void checkCode() throws Plan9Exception{
		try{
			checkCmds();
			checkSyntax();
			validCode_flag = true;
			debug.println("All Code Is Valid.");
		}catch(Plan9Exception e){
			throw new Plan9Exception(e.getMessage());
		}
	}
	
	private boolean checkInt(String cmd){
		StringTokenizer lineTokenizer = new StringTokenizer(cmd,"(");
		lineTokenizer.nextToken();
		String temp = lineTokenizer.nextToken();
		lineTokenizer = new StringTokenizer(temp,")");
		try{
			int x = Integer.parseInt(lineTokenizer.nextToken().toString());
			if((-127<x)&&(x<128)){
				return true;
			} else {
				return false;
			}
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	private boolean checkDir(String cmd){
		StringTokenizer lineTokenizer = new StringTokenizer(cmd,"(");
		lineTokenizer.nextToken();
		String temp = lineTokenizer.nextToken();
		lineTokenizer = new StringTokenizer(temp,")");
		char dir = lineTokenizer.nextToken().charAt(0);
		switch(dir){
		case 'N':
		case 'S':
		case 'E':
		case 'W':
			return true;
		default:
			return false;
		}
	}
	
	private void checkCmds() throws Plan9Exception{
		debug.println("Checking for Valid Instructions...");
		String line;
		boolean inLoop = false;
		boolean isHidden = false;
		boolean isInit = false;
		int lineNum = 1;
		StringTokenizer lineTokenizer;
		StringTokenizer tokenizer = new StringTokenizer(code);
		while(tokenizer.hasMoreTokens()){
			line = tokenizer.nextToken();
			lineTokenizer = new StringTokenizer(line,"(;");
			if(!insSet.contains(lineTokenizer.nextToken())){
				debug.println("INVALID INSTRUCTION::"+lineNum);
				throw new Plan9Exception("INVALID INSTRUCTION::"+lineNum);
			} else {
				switch(Character.toUpperCase(line.charAt(0))){
					case 'L':
						if(inLoop){
							debug.println("ALREADY IN LOOP::"+lineNum);
							throw new Plan9Exception("ALREADY IN LOOP::"+lineNum);
						}else{
							inLoop = true;
						}
						if(!checkInt(line)){
							debug.println("INVALID INT::"+lineNum);
							throw new Plan9Exception("INVALID INT::"+lineNum);
						}
						break;
					case 'I':
						if(isInit){
							debug.println("VAR ALREADY INITIALIZED::"+lineNum);
							throw new Plan9Exception("VAR ALREADY INITIALIZED::"+lineNum);
						} else {
							if(!checkInt(line)){
								debug.println("INVALID INT::"+lineNum);
								throw new Plan9Exception("INVALID INT::"+lineNum);
							}
							isInit = true;
						}
						break;
					case 'C':
						if(isInit){
							if(!checkInt(line)){
								debug.println("INVALID INT::"+lineNum);
								throw new Plan9Exception("INVALID INT::"+lineNum);
							}	
						} else {
							debug.println("VAR NOT INITIALIZED::"+lineNum);
							throw new Plan9Exception("VAR NOT INITIALIZED::"+lineNum);
						}
						break;
					case 'S':
						if(line.equals("SHOW;")){
							if(!isHidden){
								debug.println("NOT HIDDEN::"+lineNum);
								throw new Plan9Exception("NOT HIDDEN::"+lineNum);
							} else {
								isHidden = false;
							}
						} else {
							// Checking Set Instruction
							if(isInit){
								if(!checkInt(line)){
									debug.println("INVALID INT::"+lineNum);
									throw new Plan9Exception("INVALID INT::"+lineNum);
								}	
							} else {
								debug.println("VAR NOT INITIALIZED::"+lineNum);
								throw new Plan9Exception("VAR NOT INITIALIZED::"+lineNum);
							}
						}
						break;
					case 'D':
						if(!checkDir(line)){
							debug.println("INVALID DIR::"+lineNum);
							throw new Plan9Exception("INVALID DIR::"+lineNum);
						}
						break;
					case 'E':
						if(inLoop){
							inLoop = false;
						}else{
							debug.println("LOOP NOT STARTED::"+lineNum);
							throw new Plan9Exception("LOOP NOT STARTED::"+lineNum);
						}
						break;
					case 'H':
						if(isHidden){
							debug.println("ALREADY HIDDEN::"+lineNum);
							throw new Plan9Exception("ALREADY HIDDEN::"+lineNum);
						} else {
							isHidden = true;
						}
						break;
					default:
						break;
				}
			}
			lineNum++;
		}
		debug.println("All Instructions Valid.");
	}
	
	private void checkSyntax() throws Plan9Exception{
		debug.println("Checking for Valid Syntax...");
		StringTokenizer tokenizer = new StringTokenizer(code);
		int lineCount = 0;
		int tokens = tokenizer.countTokens();
		String line;
		while(tokenizer.hasMoreTokens()){
			line = tokenizer.nextToken();
			lineCount++;
		}
		if(lineCount != tokens){
			debug.println("INVALID SYNTAX ';'");
			throw new Plan9Exception("INVALID SYNTAX ';'");
		}
		debug.println("Syntax is Valid.");
	}
}

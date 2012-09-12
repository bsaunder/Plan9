/*
 * Created by Bryan Saunders
 * Created on Aug 20, 2004
 * 
 * System Version 1.0
 */

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Client extends JApplet implements ActionListener{
	private static JTextArea statusBox = new JTextArea();
	private static JButton getSource = new JButton("Open File");
	private static JButton saveSource = new JButton("Save File");
	private static JButton runSource = new JButton("Run Code");
	private static JButton saveReport = new JButton("Save Report");
	//private JButton saveCpp = new JButton("Save C++");
	private static JLabel debugText = new JLabel(":Debug Report:");
	//private JLabel cppText = new JLabel(":C++ Code:");
	private static JTextArea outputBox = new JTextArea();
	private static JLabel outputText = new JLabel(":Output:");
	private static JTextArea codeBox = new JTextArea();
	private static JLabel codeText = new JLabel(":Source Code:");
	private static JFileChooser fileGrab = new JFileChooser();
	private static JTextArea textArea = new JTextArea();
	private static JTextArea debugArea = new JTextArea();
	//private JTextArea cppArea = new JTextArea();
	private static JScrollPane editor = new JScrollPane(textArea);
	private static JScrollPane debugger = new JScrollPane(debugArea);
	//private JScrollPane cppgen = new JScrollPane(cppArea);
	private static String fileName, sourceCode;
	private static Plan9Code p9 = new Plan9Code(fileGrab,textArea);
	private static Plan9Debugger debug = new Plan9Debugger(fileGrab);
	private static Plan9Checker check = new Plan9Checker(debug,p9);
	private static Plan9Executer exec = new Plan9Executer(debug,p9);
	private static JTabbedPane tabbedPane = new JTabbedPane();
	
	//public void init(){
	public static void main(String[] args) {
				
		// Set Panels
	    JPanel codePanel = new JPanel();
	    JPanel debugPanel = new JPanel();
	    //JPanel cppPanel = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		
		// Begin Applet Code
		/*Container container = getContentPane();
		container.add( topPanel );
		getSource.addActionListener(this);
		saveSource.addActionListener(this);
		runSource.addActionListener(this);
		saveReport.addActionListener(this);*/
		// End Applet Code
		
		// Begin Application Code
		JFrame container = new JFrame();
		container.getContentPane();
		container.getContentPane().add( topPanel );
		container.setBounds(50,50,500,450);
		container.show();
		// End Application Code
		
		Font courier = new Font("Courier",0,12);
		
		tabbedPane.addTab( "Code", codePanel );
		tabbedPane.addTab( "Debug", debugPanel );
		//tabbedPane.addTab( "C++ Code", cppPanel);
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		
		codePanel.setLayout(null);
		debugPanel.setLayout(null);
		//cppPanel.setLayout(null);
		
		
		// Configure GUI Components
		topPanel.setBackground(Color.DARK_GRAY);
		tabbedPane.setBorder(new LineBorder(Color.BLACK));
		codePanel.setBackground(Color.DARK_GRAY);
		debugPanel.setBackground(Color.DARK_GRAY);
		//cppPanel.setBackground(Color.DARK_GRAY);
		
		getSource.setBounds(10,10,125,20);
		saveSource.setBounds(10,35,125,20);
		
		codeText.setBounds(35,65,100,20);
		codeText.setForeground(Color.WHITE);
		
		editor.setBounds(10,85,125,275);
		textArea.setFont(courier);
		textArea.setMargin(new Insets(2,2,2,2));
		textArea.setText("Enter Code\n   Or\nOpen A File");
		editor.setBorder(new LineBorder(Color.BLACK));
		
		
		statusBox.setBounds(254,10,200,20);
		statusBox.setBorder(new LineBorder(Color.WHITE));
		statusBox.setBackground(Color.LIGHT_GRAY);
		statusBox.setEditable(false);
		statusBox.setText("Please Open Code File...");
		
		runSource.setBounds(254,35,200,30);
		outputText.setBounds(345,70,50,20);
		outputText.setForeground(Color.WHITE);
		
		outputBox.setBounds(236,90,236,270);
		outputBox.setBackground(Color.WHITE);
		outputBox.setBorder(new LineBorder(Color.BLACK));
		outputBox.setFont(courier);
		outputBox.setText("");
		
		saveReport.setBounds(330,10,125,20);
		
		debugText.setBounds(10,10,100,20);
		debugText.setForeground(Color.WHITE);
		
		debugger.setBounds(10,35,460,320);
		debugArea.setBackground(Color.WHITE);
		debugArea.setMargin(new Insets(2,2,2,2));
		debugArea.setFont(courier);
		debugger.setBorder(new LineBorder(Color.BLACK));
		
		//saveCpp.setBounds(330,10,125,20);
		
		//cppText.setBounds(10,10,100,20);
		//cppText.setForeground(Color.WHITE);
		
		//cppgen.setBounds(10,35,460,320);
		//cppArea.setBackground(Color.WHITE);
		//cppArea.setMargin(new Insets(2,2,2,2));
		//cppArea.setFont(courier);
		//cppgen.setBorder(new LineBorder(Color.BLACK));
		
		// Add Components to GUI
		codePanel.add(statusBox);
		codePanel.add(getSource);
		codePanel.add(runSource);
		codePanel.add(outputText);
		codePanel.add(outputBox);
		codePanel.add(codeBox);
		codePanel.add(codeText);
		codePanel.add(saveSource);
		codePanel.add(editor);
		debugPanel.add(debugger);
		debugPanel.add(saveReport);
		debugPanel.add(debugText);
		//cppPanel.add(cppgen);
		//cppPanel.add(saveCpp);
		//cppPanel.add(cppText);
		outputText.setVisible(false);
		outputBox.setVisible(false);
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getSource){
			statusBox.setText("Browsing For File...");
			statusBox.setText(p9.openCode());
		} else if(e.getSource() == saveSource){
			statusBox.setText("Saving File...");
			p9.updateSource();
			statusBox.setText(p9.saveCode());
		} else if(e.getSource() == runSource){
			statusBox.setText("Processing Code...");
			p9.updateSource();
			debug.resetReport();
			check = new Plan9Checker(debug,p9);
			exec = new Plan9Executer(debug,p9);
			try{
				debugArea.setText("");
				check.checkCode();
				if(check.codeIsValid()){
					statusBox.setText(exec.executeCode());
					outputBox.setText(exec.printEnd());
					outputBox.setVisible(true);
					outputText.setVisible(true);
					debugArea.setText(debug.printReport());
				}
			}catch(Plan9Exception ex){
				statusBox.setText(ex.getMessage());
			}
		} else if(e.getSource() == saveReport){
			statusBox.setText("Saving Report...");
			statusBox.setText(debug.saveReport());
		}
	}
}

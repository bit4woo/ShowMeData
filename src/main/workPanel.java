package main;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class workPanel extends JPanel{
	
	public static JTextArea inputTextArea;
	public static JTextArea outputTextArea;
	public static JPanel LeftOfCenter;
	public static ButtonPanel RightOfCenter;
	
	workPanel(){
		
		//第一次分割，中间的大模块一分为二
		JSplitPane CenterSplitPane = new JSplitPane();//中间的大模块，一分为二
		CenterSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		CenterSplitPane.setResizeWeight(0.7);
		this.add(CenterSplitPane, BorderLayout.CENTER);

		LeftOfCenter = new JPanel();
		CenterSplitPane.setLeftComponent(LeftOfCenter);
		LeftOfCenter.add(createTextArea());

		RightOfCenter = new ButtonPanel();
		CenterSplitPane.setRightComponent(RightOfCenter);
	}
	
	public JTextArea createTextArea() {
		JTextArea newOne = new JTextArea();
		newOne.setRows(20);
		newOne.setLineWrap(true);
		return newOne;
	}

	public static Set<String> getSetFromTextArea(JTextArea textarea){
		//user input maybe use "\n" in windows, so the System.lineSeparator() not always works fine!
		Set<String> domainList = new HashSet<>(Arrays.asList(textarea.getText().replaceAll(" ","").replaceAll("\r\n", "\n").split("\n")));
		domainList.remove("");
		return domainList;
	}
}

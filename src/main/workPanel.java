package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

public class workPanel extends JPanel{

	public static ArrayList<JTextArea> textAreaList = new ArrayList<JTextArea>();
	public static JTextArea inputTextArea;
	public static JTextArea outputTextArea;
	public static JPanel panelToStoreTextAreas;
	public static ButtonPanel RightOfCenter;

	workPanel(){
		setLayout(new BorderLayout(0, 0));

		//第一次分割，中间的大模块一分为二
		JSplitPane CenterSplitPane = new JSplitPane();//中间的大模块，一分为二
		CenterSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		CenterSplitPane.setResizeWeight(0.8);
		this.add(CenterSplitPane);

		//不能直接在JScrollPane中添加组件，不会显示出来，必须先设置ViewportView，在ViewportView中添加
		panelToStoreTextAreas = new JPanel();//存放文本框
		JScrollPane LeftOfCenterScrollPanel = new JScrollPane();
		LeftOfCenterScrollPanel.setViewportView(panelToStoreTextAreas);
		CenterSplitPane.setLeftComponent(LeftOfCenterScrollPanel);

		LeftOfCenterScrollPanel.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		LeftOfCenterScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		LeftOfCenterScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		panelToStoreTextAreas.setLayout(new BoxLayout(panelToStoreTextAreas, BoxLayout.PAGE_AXIS));
		createTextArea();

		RightOfCenter = new ButtonPanel();//存放按钮
		RightOfCenter.setBorder(new LineBorder(new Color(0, 0, 0)));
		CenterSplitPane.setRightComponent(RightOfCenter);
		RightOfCenter.setLayout(new GridLayout(20, 1, 5, 5));

	}

	public static JTextArea createTextArea() {

		JTextArea newOne = new JTextArea();
		newOne.setBorder(new LineBorder(new Color(0, 0, 0)));
		newOne.setRows(15);
		newOne.setLineWrap(true);
		newOne.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				inputTextArea = (JTextArea) e.getComponent();
				System.out.print(inputTextArea);
			}
		});
		textAreaList.add(newOne);

		JScrollPane scroller = new JScrollPane(newOne);
		panelToStoreTextAreas.add(scroller);
		panelToStoreTextAreas.revalidate();//每次添加后必须刷新.
		return newOne;
	}

	public static Set<String> getSetFromTextArea(JTextArea textarea){
		//user input maybe use "\n" in windows, so the System.lineSeparator() not always works fine!
		Set<String> domainList = new HashSet<>(Arrays.asList(textarea.getText().replaceAll(" ","").replaceAll("\r\n", "\n").split("\n")));
		domainList.remove("");
		return domainList;
	}

	public static JTextArea getInputTextArea() {

		if (textAreaList.size() == 0) {
			createTextArea();
		}

		if (inputTextArea == null) {
			inputTextArea = textAreaList.get(textAreaList.size()-1);
		}
		return inputTextArea;
	}

	public static JTextArea getOutputTextArea() {
		int currentIndex = textAreaList.indexOf(inputTextArea);
		if (currentIndex == textAreaList.size()-1) {
			outputTextArea = createTextArea();
		}else {
			outputTextArea = textAreaList.get(currentIndex+1);
		}
		return outputTextArea;
	}
}

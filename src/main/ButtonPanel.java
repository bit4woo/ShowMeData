package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.text.StringEscapeUtils;

public class ButtonPanel extends JPanel{
	
	JTextArea inputTextArea;
	JTextArea outputTextArea;
	Config config;
	PrintWriter stderr = new PrintWriter(System.out, true);
	PrintWriter stdout = new PrintWriter(System.out, true);

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame("ButtonPanel");
					frame.setVisible(true);
					frame.setContentPane(new ButtonPanel());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	ButtonPanel(){
		this.inputTextArea = MainPanel.inputTextArea;
		this.outputTextArea = MainPanel.outputTextArea;
		this.config = MainPanel.config;

		//四分之三部分放一个panel，里面放操作按钮
		JPanel threeFourthPanel = this;
		threeFourthPanel.setLayout(new FlowLayout());
		//https://stackoverflow.com/questions/5709690/how-do-i-make-this-flowlayout-wrap-within-its-jsplitpane
		threeFourthPanel.setMinimumSize(new Dimension(0, 0));//为了让button自动换行

		JButton btnOpenurls = new JButton("OpenURLs");
		threeFourthPanel.add(btnOpenurls);
		btnOpenurls.addActionListener(new ActionListener() {
			List<String> urls = new ArrayList<>();
			Iterator<String> it = urls.iterator();

			@Override
			public void actionPerformed(ActionEvent e) {
				urls = Arrays.asList(MainPanel.inputTextArea.getText().replaceAll(" ","").replaceAll("\r\n", "\n").split("\n"));
				it = urls.iterator();
				try {
					int i =10;
					while(i>0 && it.hasNext()) {
						String url = it.next();
						//stdout.println(url);
						Commons.browserOpen(url, config.getBrowserPath());
						i--;
					}
				} catch (Exception e1) {
					e1.printStackTrace(stderr);
				}
			}

		});

		JButton rows2List = new JButton("Rows To List");
		threeFourthPanel.add(rows2List);
		rows2List.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<String> content = Commons.getLinesFromTextArea(inputTextArea);					
					outputTextArea.setText(content.toString());
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
				}
			}

		});

		JButton rows2Array = new JButton("Rows To Array");
		threeFourthPanel.add(rows2Array);
		rows2Array.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<String> content = Commons.getLinesFromTextArea(inputTextArea);
					for (int i=0;i<content.size();i++) {
						content.set(i, "\""+content.get(i)+"\"");
					}

					outputTextArea.setText(String.join(",", content));
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
				}
			}

		});


		JButton btnGrep = new JButton("Grep Json");
		threeFourthPanel.add(btnGrep);
		btnGrep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String content = inputTextArea.getText();
					String toFind = JOptionPane.showInputDialog("to find which value", null);
					if (toFind == null) {
						return;
					} else {
						//stdout.println(content);
						ArrayList<String> result = JSONHandler.grepValueFromJson(content, toFind);
						//								stdout.println("##################Result of Grep JSON##################");
						//								stdout.println(result.toString());
						//								stdout.println("##################Result of Grep JSON##################");
						//								stdout.println();

						outputTextArea.setText(String.join(System.lineSeparator(), result));
					}

				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
					//e1.printStackTrace(stderr);
				}
			}

		});

		JButton btnLine = new JButton("Grep Line");
		threeFourthPanel.add(btnLine);
		btnLine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String toFind = JOptionPane.showInputDialog("to find which value", null);
					ArrayList<String> result = new ArrayList<String>();
					if (toFind == null) {
						return;
					} else {
						List<String> content = Commons.getLinesFromTextArea(inputTextArea);
						for (String item:content) {
							if (item.toLowerCase().contains(toFind.toLowerCase().trim())) {
								result.add(item); 
							}
						}
						//outputTextArea.setText(result.toString());
						outputTextArea.setText(String.join(System.lineSeparator(), result));
					}

				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
					//e1.printStackTrace(stderr);
				}
			}
		});


		JButton btnAddPrefix = new JButton("Add Prefix/Suffix");
		threeFourthPanel.add(btnAddPrefix);
		btnAddPrefix.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String toAddPrefix = JOptionPane.showInputDialog("prefix to add", null);
					String toAddSuffix = JOptionPane.showInputDialog("suffix to add", null);
					ArrayList<String> result = new ArrayList<String>();
					if (toAddPrefix == null && toAddSuffix == null) {
						return;
					} else {
						if (toAddPrefix == null) {
							toAddPrefix = "";
						}

						if (toAddSuffix == null) {
							toAddSuffix = "";
						}

						List<String> content = Commons.getLinesFromTextArea(inputTextArea);
						for (String item:content) {
							item = toAddPrefix.trim()+item+toAddSuffix.trim();
							result.add(item); 
						}
						outputTextArea.setText(String.join(System.lineSeparator(), result));
					}
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
				}
			}
		});


		JButton btnRegexGrep = new JButton("Regex Grep");
		btnRegexGrep.setEnabled(false);
		threeFourthPanel.add(btnRegexGrep);
		btnRegexGrep.addActionListener(new ActionListener() {
			

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//					String toFind = JOptionPane.showInputDialog("to find which value", null);
					//					if (toFind == null) {
					//						return;
					//					} else {
					ArrayList<String> result = new ArrayList<String>();
					//主要目的是找url        path: '/admin/menu',
					String webpack_PATTERN = "\'/([0-9a-z])*\'"; //TODO 正则表达不正确
					Pattern pRegex = Pattern.compile(webpack_PATTERN);
					String content = inputTextArea.getText();
					Matcher matcher = pRegex.matcher(content);
					while (matcher.find()) {//多次查找
						result.add(matcher.group());
					}
					outputTextArea.setText(result.toString());
					//}
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}

		});

		JButton btnIPsToCIDR = new JButton("IPs To CIDR");
		threeFourthPanel.add(btnIPsToCIDR);
		btnIPsToCIDR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<String> IPs = Commons.getLinesFromTextArea(inputTextArea);
					Set<String> subnets = Commons.toSmallerSubNets(new HashSet<String>(IPs));
					outputTextArea.setText(String.join(System.lineSeparator(), subnets));
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});

		JButton btnCIDRToIPs = new JButton("CIDR To IPs");
		threeFourthPanel.add(btnCIDRToIPs);
		btnCIDRToIPs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<String> subnets = Commons.getLinesFromTextArea(inputTextArea);
					List<String> IPs = Commons.toIPList(subnets);// 当前所有title结果计算出的IP集合
					outputTextArea.setText(String.join(System.lineSeparator(), IPs));
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});

		JButton unescapeJava = new JButton("unescapeJava");
		threeFourthPanel.add(unescapeJava);
		unescapeJava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					outputTextArea.setText(StringEscapeUtils.unescapeJava(inputTextArea.getText()));
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});

		JButton unescapeHTML = new JButton("unescapeHTML");
		threeFourthPanel.add(unescapeHTML);
		unescapeHTML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					outputTextArea.setText(StringEscapeUtils.unescapeHtml4(inputTextArea.getText()));
				} catch (Exception e1) {
					outputTextArea.setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});
	}
}

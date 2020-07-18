package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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

	public void freshCurrentInputOutput(){
		this.inputTextArea = workPanel.getInputTextArea();
		this.outputTextArea = workPanel.getOutputTextArea();
	}

	ButtonPanel(){
		this.config = MainPanel.config;

		//四分之三部分放一个panel，里面放操作按钮
		JPanel buttonPanel = this;
		buttonPanel.setLayout(new FlowLayout());
		//https://stackoverflow.com/questions/5709690/how-do-i-make-this-flowlayout-wrap-within-its-jsplitpane
		buttonPanel.setMinimumSize(new Dimension(0, 0));//为了让button自动换行

		/*
		JButton btnOpenurls = new JButton("OpenURLs");
		threeFourthPanel.add(btnOpenurls);
		btnOpenurls.addActionListener(new ActionListener() {
			List<String> urls = new ArrayList<>();
			Iterator<String> it = urls.iterator();

			@Override
			public void actionPerformed(ActionEvent e) {
				urls = Arrays.asList(inputTextArea.getText().replaceAll(" ","").replaceAll("\r\n", "\n").split("\n"));
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
		 */

		JButton rows2List = new JButton("Rows To List");
		rows2List.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<String> content = Commons.getLinesFromTextArea(workPanel.getInputTextArea());					
					workPanel.getOutputTextArea().setText(content.toString());
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
				}
			}

		});

		JButton rows2Array = new JButton("Rows To Array");
		rows2Array.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<String> content = Commons.getLinesFromTextArea(workPanel.getInputTextArea());
					for (int i=0;i<content.size();i++) {
						content.set(i, "\""+content.get(i)+"\"");
					}

					workPanel.getOutputTextArea().setText(String.join(",", content));
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
				}
			}

		});


		JButton btnGrepJson = new JButton("Grep by JSON Key");
		btnGrepJson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String content = workPanel.getInputTextArea().getText();
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

						workPanel.getOutputTextArea().setText(String.join(System.lineSeparator(), result));
					}

				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					//e1.printStackTrace(stderr);
				}
			}
		});

		JButton btnGrepLine = new JButton("Grep Line by content");
		btnGrepLine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String toFind = JOptionPane.showInputDialog("to find which value", null);
					ArrayList<String> result = new ArrayList<String>();
					if (toFind == null) {
						return;
					} else {
						List<String> content = Commons.getLinesFromTextArea(workPanel.getInputTextArea());
						for (String item:content) {
							if (item.toLowerCase().contains(toFind.toLowerCase().trim())) {
								result.add(item); 
							}
						}
						//outputTextArea.setText(result.toString());
						workPanel.getOutputTextArea().setText(String.join(System.lineSeparator(), result));
					}

				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					//e1.printStackTrace(stderr);
				}
			}
		});


		JButton btnAddPrefix = new JButton("Add Prefix/Suffix");
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

						List<String> content = Commons.getLinesFromTextArea(workPanel.getInputTextArea());
						for (String item:content) {
							item = toAddPrefix.trim()+item+toAddSuffix.trim();
							result.add(item); 
						}
						workPanel.getOutputTextArea().setText(String.join(System.lineSeparator(), result));
					}
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
				}
			}
		});

		JButton btnRemovePrefix = new JButton("Remove Prefix/Suffix");
		btnAddPrefix.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String Prefix = JOptionPane.showInputDialog("prefix to remove", null);
					String Suffix = JOptionPane.showInputDialog("suffix to remove", null);
					ArrayList<String> result = new ArrayList<String>();
					if (Prefix == null && Suffix == null) {
						return;
					} else {
						if (Prefix == null) {
							Prefix = "";
						}

						if (Suffix == null) {
							Suffix = "";
						}

						List<String> content = Commons.getLinesFromTextArea(workPanel.getInputTextArea());
						for (String item:content) {
							if (item.startsWith(Prefix)) {
								item = item.replaceFirst(Prefix, "");
							}
							if (item.endsWith(Suffix)) {
								item = reverse(item).replaceFirst(reverse(Suffix), "");
								item = reverse(item);
							}
							result.add(item); 
						}
						workPanel.getOutputTextArea().setText(String.join(System.lineSeparator(), result));
					}
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
				}
			}
			
			public String reverse(String str) {
				if (str == null) {
					return null;
				}
				return new StringBuffer(str).reverse().toString();
			}
		});

		JButton btnRegexGrep = new JButton("Grep by regex");
		btnRegexGrep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String toFind = JOptionPane.showInputDialog("regex to find", null);
					if (toFind == null) {
						return;
					} else {
					ArrayList<String> result = new ArrayList<String>();
					//String webpack_PATTERN = "\'/([0-9a-z])*\'"; //TODO 正则表达不正确
					Pattern pRegex = Pattern.compile(toFind);
					String content = workPanel.getInputTextArea().getText();
					Matcher matcher = pRegex.matcher(content);
					while (matcher.find()) {//多次查找
						result.add(matcher.group());
					}
					workPanel.getOutputTextArea().setText(result.toString());
					}
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}

		});

		JButton btnIPsToCIDR = new JButton("IPs To CIDR");
		btnIPsToCIDR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freshCurrentInputOutput();
				try {
					List<String> IPs = Commons.getLinesFromTextArea(workPanel.getInputTextArea());
					Set<String> subnets = Commons.toSmallerSubNets(new HashSet<String>(IPs));
					workPanel.getOutputTextArea().setText(String.join(System.lineSeparator(), subnets));
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});

		JButton btnCIDRToIPs = new JButton("CIDR To IPs");
		btnCIDRToIPs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freshCurrentInputOutput();
				try {
					List<String> subnets = Commons.getLinesFromTextArea(workPanel.getInputTextArea());
					List<String> IPs = Commons.toIPList(subnets);// 当前所有title结果计算出的IP集合
					workPanel.getOutputTextArea().setText(String.join(System.lineSeparator(), IPs));
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});

		JButton unicodeDecode = new JButton("Decode Unicode");
		unicodeDecode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freshCurrentInputOutput();
				try {
					String out = StringEscapeUtils.unescapeJava(inputTextArea.getText());
					if (!out.equals(inputTextArea.getText())) {
						workPanel.getOutputTextArea().setText(out);
					}
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});

		JButton unescapeHTML = new JButton("Unescape HTML");
		unescapeHTML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freshCurrentInputOutput();
				try {
					String out = StringEscapeUtils.unescapeHtml4(inputTextArea.getText());
					if (!out.equals(inputTextArea.getText())) {
						workPanel.getOutputTextArea().setText(out);
					}
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});
		
		JButton removeLineChar = new JButton("Remove \\n\\r");
		removeLineChar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freshCurrentInputOutput();
				try {
					String out = inputTextArea.getText().replaceAll("\n", "").replaceAll("\r", "");
					if (!out.equals(inputTextArea.getText())) {
						workPanel.getOutputTextArea().setText(out);
					}
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
		});
		
		JButton formateJson = new JButton("Formate JSON");
		formateJson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freshCurrentInputOutput();
				try {
					String out = beauty(inputTextArea.getText());
					
					if (!out.equals(inputTextArea.getText())) {
						workPanel.getOutputTextArea().setText(out);
					}
				} catch (Exception e1) {
					workPanel.getOutputTextArea().setText(e1.getMessage());
					e1.printStackTrace(stderr);
				}
			}
			
			public String beauty(String inputJson) {
				//Take the input, determine request/response, parse as json, then print prettily.
				Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
				JsonParser jp = new JsonParser();
				JsonElement je = jp.parse(inputJson);
				return gson.toJson(je);
			}
		});
		
		//数据格式转换类
		buttonPanel.add(rows2List);
		buttonPanel.add(rows2Array);
		buttonPanel.add(btnIPsToCIDR);
		buttonPanel.add(btnCIDRToIPs);
		
		buttonPanel.add(new JButton(System.lineSeparator()));
		
		//数据提取过滤类
		buttonPanel.add(btnGrepJson);
		buttonPanel.add(btnGrepLine);
		buttonPanel.add(btnRegexGrep);
		
		buttonPanel.add(new JButton(System.lineSeparator()));
		
		//每行处理
		buttonPanel.add(btnAddPrefix);
		buttonPanel.add(btnRemovePrefix);
		buttonPanel.add(removeLineChar);

		buttonPanel.add(new JButton(System.lineSeparator()));
		//整体处理、格式化
		buttonPanel.add(unicodeDecode);
		buttonPanel.add(unescapeHTML);
		buttonPanel.add(formateJson);
	}
}

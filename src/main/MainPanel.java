package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import bsh.This;

/*
 * 所有配置的修改，界面的操作，都立即写入LineConfig对象，如有必要保存到磁盘，再调用一次SaveConfig函数，思路要清晰
 * 加载： 磁盘文件-->LineConfig对象--->具体控件的值
 * 保存： 具体各个控件的值---->LineConfig对象---->磁盘文件
 */

public class MainPanel extends JPanel {

	private JLabel lblGithub;

	PrintWriter stdout;
	PrintWriter stderr;

	private ConfigPanel configPanel;
	private workPanel workPanel;

	public static JTextField proxyTextField;
	public static boolean inputTextAreaChanged = true;
	public static JTextField BrowserPath;
	public static Config config;

	private static String ToolName = "ShowMeData";
	private static String Version =  This.class.getPackage().getImplementationVersion();
	private static String Author = "by bit4woo";
	private static String Github = "https://github.com/bit4woo/ShowMeData";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame(getFullToolName());
					frame.setSize(500 , 1000);
					frame.setVisible(true);
					frame.setContentPane(new MainPanel());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static String getFullToolName(){
		return ToolName+" "+Version+" "+Author;
	}

	public MainPanel() {
		setForeground(Color.DARK_GRAY);//构造函数
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout(0, 0));

		stdout = new PrintWriter(System.out, true);
		stderr = new PrintWriter(System.out, true);

		///////////////////////Header Panel//////////////


		JPanel HeaderPanel = new JPanel();
		HeaderPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		FlowLayout fl_HeaderPanel = (FlowLayout) HeaderPanel.getLayout();
		fl_HeaderPanel.setAlignment(FlowLayout.LEFT);
		this.add(HeaderPanel, BorderLayout.NORTH);

		JLabel lblNewLabelNull = new JLabel("proxy");
		HeaderPanel.add(lblNewLabelNull);

		proxyTextField = new JTextField("127.0.0.1:8080");
		HeaderPanel.add(proxyTextField);
		proxyTextField.setColumns(25);

		///////////////////////body Panel//////////////

		/*
		JTabbedPane CentertabbedWrapper = new JTabbedPane();
		workPanel = new workPanel();
		configPanel = new ConfigPanel(config);
		CentertabbedWrapper.addTab("work", null, workPanel, null);
		CentertabbedWrapper.addTab("config", null,configPanel,null);

		this.add(CentertabbedWrapper,BorderLayout.CENTER);
		*/
		
		this.add(new workPanel(),BorderLayout.CENTER);
		///////////////////////////Footer Panel//////////////////


		JPanel footerPanel = new JPanel();
		footerPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		FlowLayout fl_FooterPanel = (FlowLayout) footerPanel.getLayout();
		fl_FooterPanel.setAlignment(FlowLayout.LEFT);
		this.add(footerPanel, BorderLayout.SOUTH);

		lblGithub = new JLabel(getFullToolName()+"    "+Github);
		lblGithub.setFont(new Font("宋体", Font.BOLD, 12));
		lblGithub.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					URI uri = new URI(Github);
					Desktop desktop = Desktop.getDesktop();
					if(Desktop.isDesktopSupported()&&desktop.isSupported(Desktop.Action.BROWSE)){
						desktop.browse(uri);
					}
				} catch (Exception e2) {
					e2.printStackTrace(stderr);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblGithub.setForeground(Color.BLUE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblGithub.setForeground(Color.BLACK);
			}
		});
		footerPanel.add(lblGithub);
	}

}

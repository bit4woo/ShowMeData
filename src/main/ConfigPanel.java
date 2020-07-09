package main;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.ConfigPanel.textFieldListener;

public class ConfigPanel extends JPanel{
	Config config;
	private JTextField BrowserPath;
	
	ConfigPanel(Config config){
		if (config == null) {
			config = new Config();
		}
		this.config = config;
		
		JLabel lblNewLabel = new JLabel("Browser Path:");
		add(lblNewLabel);

		BrowserPath = new JTextField();
		add(BrowserPath);
		BrowserPath.setColumns(50);
		BrowserPath.getDocument().addDocumentListener(new textFieldListener());
		
	}
	
	class textFieldListener implements DocumentListener {

		@Override
		public void removeUpdate(DocumentEvent e) {
			File browser = new File(config.getBrowserPath().trim());
			if (browser.exists()) {
				config.setBrowserPath(browser.getAbsolutePath());
				saveConfig();
			}
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			File browser = new File(BrowserPath.getText().trim());
			if (browser.exists()) {
				config.setBrowserPath(browser.getAbsolutePath());
				saveConfig();
			}
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			File browser = new File(BrowserPath.getText().trim());
			if (browser.exists()) {
				config.setBrowserPath(browser.getAbsolutePath());
				saveConfig();
			}
		}
	}
	
	public void saveConfig() {
		config.setBrowserPath(BrowserPath.getText());
		String strConfig = config.ToJson();
	}
}

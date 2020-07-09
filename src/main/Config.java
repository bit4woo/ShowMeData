package main;

import com.google.gson.Gson;

public class Config {

	private String python3Path = "C:\\Python37\\python.exe";
	private String browserPath = "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe";
	private String toolPanelText = "";
	
	public String getPython3Path() {
		return python3Path;
	}

	public void setPython3Path(String python3Path) {
		this.python3Path = python3Path;
	}

	public String getBrowserPath() {
		return browserPath;
	}

	public void setBrowserPath(String browserPath) {
		this.browserPath = browserPath;
	}

	public String getToolPanelText() {
		return toolPanelText;
	}

	public void setToolPanelText(String toolPanelText) {
		this.toolPanelText = toolPanelText;
	}

	public String ToJson() {
		return new Gson().toJson(this);
	}

	public  static Config FromJson(String instanceString) {// throws Exception {
		return new Gson().fromJson(instanceString, Config.class);
	}
}

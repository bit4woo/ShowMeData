package main;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

public class LineConfig {


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
		return JSONObject.toString();
		//https://blog.csdn.net/qq_27093465/article/details/73277291
		//return new Gson().toJson(this);
	}

	public  static LineConfig FromJson(String instanceString) {// throws Exception {
		return JSON.parseObject(instanceString,LineConfig.class);
		//return new Gson().fromJson(instanceString, DomainObject.class);
	}
}

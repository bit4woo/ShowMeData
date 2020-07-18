package httpbase;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/** 
 * @author bit4woo
 * @github https://github.com/bit4woo 
 * @version CreateTime：Jun 13, 2020 7:05:18 AM 
 */
public class Request {

	//解析后的常用变量
	private String host;
	private int port;
	private String halfurlFromRawData;
	private String halfurlFromUrl;
	private ArrayList<String> headerList;

	//发送请求所需参数
	private String protocol = "http";
	private String method = "GET"; //GET POST,default is get.
	private String url;
	private HashMap<String,String> headerMap = new HashMap<String,String>();
	private byte[] body;
	//发送请求所需参数


	public static void main(String args[]) {
		try {
			System.out.print(new Request("https://www.cnblogs.com/mywood/p/8041807.html").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//和burp一致，通过URL(或者httpService)和burp请求包构造请求。
	public Request(String httpservice,byte[] rawRequest) throws Exception{
		parser(httpservice,rawRequest);
	}

	//只通过burp请求数据包构造请求，需要指定是否使用https。
	public Request(boolean useHttps,byte[] rawRequest) throws Exception{
		parserRawData(rawRequest);
		if (useHttps) {
			url ="https://"+headerMap.get("Host")+url;
		}else {
			url ="http://"+headerMap.get("host")+url;
		}
		parser(url,rawRequest);
	}

	//通过一个单独的URL构造一个Get请求。
	public Request(String url) throws Exception {
		String rawRequest = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:78.0) Gecko/20100101 Firefox/78.0\r\n" + 
				"Accept: */*\r\n" + 
				"Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\r\n" + 
				"Accept-Encoding: gzip, deflate\r\n" + 
				"Connection: close\r\n" + 
				"\r\n";

		rawRequest = "Host: "+host+":"+port+"\r\n"+rawRequest;
		parser(url,rawRequest.getBytes());
	}




	private static boolean isFirstLine(String line) {
		try {
			String[] keyAndValue = line.split(" ",2);
			String key = keyAndValue[0];
			String value = keyAndValue[1].trim();

			return isFirstLine(key,value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private static boolean isFirstLine(String key,String Value) {
		//GET /sys/remark/pages.pvt?t=1593481248723&limit=3&start=0&sysId=2111 HTTP/1.1
		//HTTP/1.1 200 OK
		if (key.startsWith("HTTP/")) {//response first line
			return true;
		}
		try {
			if (Value.split(" ",2)[1].startsWith("HTTP/")) {
				return true;
			}
		}catch(Exception e){}

		return false;
	}

	private void parser(String inputurl,byte[] rawRequest){
		try {
			parserUrl(inputurl);
			parserRawData(rawRequest);
			String httpservice = protocol+"://"+host+":"+port;
			if (halfurlFromRawData != null) {
				this.url = httpservice+halfurlFromRawData;
			}else {
				this.url = httpservice+halfurlFromUrl;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void parserUrl(String inputurl) throws Exception {
		URL serviceUrl = new URL(inputurl);
		host = serviceUrl.getHost();
		port = serviceUrl.getPort();
		protocol = serviceUrl.getProtocol();

		if (port == -1) {
			port = serviceUrl.getDefaultPort();
			halfurlFromUrl = inputurl.split(host,2)[1];
		}else {
			halfurlFromUrl = inputurl.split(host+":"+port,2)[1];
		}
		if (method == null || method =="") {
			method = "GET";
		}
	}

	private void parserRawData(byte[] rawRequest) throws Exception {

		int bodyOffset = findBodyOffset(rawRequest);
		byte[] head = Arrays.copyOfRange(rawRequest, 0, bodyOffset-4);
		body = Arrays.copyOfRange(rawRequest, bodyOffset, rawRequest.length);//not length-1

		String headString = new String(head);
		//List<String> headerList = Arrays.asList(headString.split("\r\n"));
		headerList = new ArrayList<String>(Arrays.asList(headString.split("\r\n")));

		ArrayList<String> headerListTmp = new ArrayList<String>();
		headerListTmp.addAll(headerList);

		String firstLine = headerListTmp.get(0);

		if (isFirstLine(firstLine)) {
			halfurlFromRawData = firstLine.split(" ")[1];//half url
			method = firstLine.split(" ")[0];
			headerListTmp.remove(0);
		}

		for (String line:headerListTmp) {
			String[] keyAndValue = line.split(":",2);
			String key = keyAndValue[0];
			String value = keyAndValue[1].trim();
			headerMap.put(key, value);
		}
	}

	private static int findBodyOffset(byte[] requestOrResponse) {
		for(int i =0;i<=requestOrResponse.length-4;i++) {
			byte[] item = {requestOrResponse[i],requestOrResponse[i+1],requestOrResponse[i+2],requestOrResponse[i+3]};
			//System.out.println("---"+new String(item)+"---");
			if (Arrays.equals(item,"\r\n\r\n".getBytes())) {
				return i+4;//指向body中的第一个字符
			}
		}
		return -1;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getProtocol() {
		return protocol;
	}

	public byte[] getBody() {
		return body;
	}

	public ArrayList<String> getHeaderList() {
		return headerList;
	}

	public String getUrl() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	/*
	 * 不包含请求数据包的第一行
	 */
	public HashMap<String, String> getHeaderMap() {
		return headerMap;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("----------url----------\r\n");
		sb.append(this.url);

		sb.append("\r\n----------headers----------\r\n");
		for (String key: headerMap.keySet()) {
			sb.append(key+": "+headerMap.get(key)+"\r\n");
		}

		sb.append("\r\n----------body----------\r\n");
		sb.append(new String(body));

		sb.append("\r\n----------body----------\r\n");
		sb.append(getMethod());

		return sb.toString();
	}
}

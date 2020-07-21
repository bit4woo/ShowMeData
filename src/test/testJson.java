package test;

import org.json.JSONObject;

import com.google.gson.Gson;


public class testJson {
	public static void main(String args[]) {
		orgJson();
		gson();
		//fastjson();
		
		//String json = FileUtils.readFileToString(new File("D:\\user\\01374214\\desktop\\fengdong-system-id.txt"));
	}

	public static void orgJson(){
		User userObject = new User("张三",26,"男"	);
		String userJson = JSONObject.valueToString(userObject);  
		System.out.println(userJson);

		String userJson1 = "{'age':26,'sex':'男','name':'张三'}";
//		User userObject1 = (User) JSONObject.stringToValue(userJson1);
//		System.out.println(userObject1);
	}

	public static void gson(){
		User userObject = new User("张三",26,"男"	);
		Gson gson = new Gson();  
		String userJson = gson.toJson(userObject); 
		System.out.println(userJson);


		String userJson1 = "{'age':26,'sex':'男','name':'张三'}";  
		Gson gson1 = new Gson();  
		User userObject1 = gson1.fromJson(userJson1, User.class);  
		System.out.println(userObject1);
	}

	/*
	public static void fastjson() {
		User userObject = new User("张三",26,"男"	);
		String userJson = JSON.toJSONString(userObject);
		System.out.println(userJson);


		String userJson1 = "{'age':26,'sex':'男','name':'张三'}";  
		User userObject1 = JSON.parseObject(userJson1, User.class);
		System.out.println(userObject1);
	}
	*/

}

class User {
	String name;
	int age;
	String sex;
	User(){
		
	}
	User(String name,int age,String sex){
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
}
package controller;
// 执行发布任务的servlet
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyledEditorKit.BoldAction;


import net.sf.json.JSONObject;

import tool.HYTaskInfoTool;
import tool.HYUserInfoTool;

import model.HYTaskInfo;

public class TaskServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// 获取任务的json
		String json = null;
		json = TaskServlet.getJsonByReq(request);
//		System.out.println(json);
		
		// 将json的内容转化为HYTaskInfo模型
		HYTaskInfo taskInfo = TaskServlet.getTaskInfoByJson(json, request);
		
		HYTaskInfoTool taskInfoTool = HYTaskInfoTool.shareTaskInfoTool();

		boolean insertTaskInfo = false;
	
		// 把其余任务信息和name属性存入到taskInfo表里, 向已发布的任务表插入新的纪录
		try {
			insertTaskInfo = taskInfoTool.insertTaskWithTaskInfo(taskInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		if (insertTaskInfo == true) {
//			writer.write("发送任务成功");
			servletOutputStream.write("1".getBytes());
		} else {
//			writer.write("发送任务失败");
			servletOutputStream.write("0".getBytes());
		}
		
		servletOutputStream.close();
	}
	
	//将json的内容转化为HYTaskInfo模型, 同时把任务的图片写入本地，获取图片url，存入到HYTaskInfo模型中
	public static HYTaskInfo getTaskInfoByJson(String json, HttpServletRequest request) throws IOException {
		JSONObject jsonObject = JSONObject.fromObject(json);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)jsonObject;
		HYTaskInfo taskInfo = new HYTaskInfo();
		taskInfo.content = (String)map.get("content");
		taskInfo.photoNumber = (String)map.get("photoNumber");
		taskInfo.money = (String)map.get("money");
		String imageString = (String)map.get("image");
		byte[] image = TaskServlet.hex2byte(imageString);
		
		// 时用 手机号码 + 200位随机字符串作为name属性，下次通过name参数获取对应的图片
		String randomStr = TaskServlet.getRandomString(52);
		// 得到工程的真正路径
		String realPathString = request.getRealPath(""); 
		// 得到服务端存储位置
		String preString = "\\WebRoot\\taskImage\\"; // 前缀
		String imageSave = String.format("%s%s.png", taskInfo.photoNumber, randomStr); // 文件名字
		
		// 创建图片以后访问的URL
		// 如： http://localhost:8080/HYLazyCatServer/WebRoot/taskImage
		taskInfo.imageUrl = "http://localhost:8080/HYLazyCatServer/WebRoot/taskImage/" + imageSave;
		
		imageSave = realPathString + preString + imageSave;
		
		// 把图片存入写入本地(子线程中写入)
		String defaultSaveImageString = realPathString + "\\WebRoot\\taskImage";
		File fileDirFile = new File(defaultSaveImageString);
		if (!fileDirFile.exists()) {
			fileDirFile.mkdirs();
		}
		System.out.println("文件夹是否存在" + fileDirFile.exists());
		File file = new File(imageSave);
		if (!file.exists()) {
			file.createNewFile();
		}
		System.out.println("文件是否存在" + file.exists());
		
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		System.out.println(image);
		fileOutputStream.write(image);
		fileOutputStream.close();
		System.out.println("已写入文件");
		
		return taskInfo;
	}
	
	
	public static String getJsonByReq(HttpServletRequest request) throws UnsupportedEncodingException, IOException {
		String json = "";
		// 获取任务的json
		BufferedReader bufferedReader = null;
		// 读取的时候使用UTF-8,将字节转化成字符
		bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
		String tem;
		while((tem = bufferedReader.readLine()) != null){
			json = json + tem;
		}
		bufferedReader.close();
		return json;
	}
	
	// 产生随机字符串
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789QWERTYUIOPASDFGHJKLZXCVBNM";  
	    Random random = new Random();
	    StringBuffer sb  = new StringBuffer();
	    for (int i = 0; i < length; i++) {
			sb.append(base.charAt(random.nextInt(length)));
		}
	    
	    return sb.toString();   
	 } 
	
	// 字符串转二进制  
	public static byte[] hex2byte(String str) { 
	    if (str == null)  
	     return null;  
	    str = str.trim();  
	    int len = str.length();  
	    if (len == 0 || len % 2 == 1)  
	     return null;  
	    byte[] b = new byte[len / 2];  
	    try {  
	     for (int i = 0; i < str.length(); i += 2) {  
	      b[i / 2] = (byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue();  
	     }  
	     return b;  
	    } catch (Exception e) {  
	     return null;  
	   } 
	}
}

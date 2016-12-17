package controller;
// ִ�з��������servlet
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
		// ��ȡ�����json
		String json = null;
		json = TaskServlet.getJsonByReq(request);
//		System.out.println(json);
		
		// ��json������ת��ΪHYTaskInfoģ��
		HYTaskInfo taskInfo = TaskServlet.getTaskInfoByJson(json, request);
		
		HYTaskInfoTool taskInfoTool = HYTaskInfoTool.shareTaskInfoTool();

		boolean insertTaskInfo = false;
	
		// ������������Ϣ��name���Դ��뵽taskInfo����, ���ѷ��������������µļ�¼
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
//			writer.write("��������ɹ�");
			servletOutputStream.write("1".getBytes());
		} else {
//			writer.write("��������ʧ��");
			servletOutputStream.write("0".getBytes());
		}
		
		servletOutputStream.close();
	}
	
	//��json������ת��ΪHYTaskInfoģ��, ͬʱ�������ͼƬд�뱾�أ���ȡͼƬurl�����뵽HYTaskInfoģ����
	public static HYTaskInfo getTaskInfoByJson(String json, HttpServletRequest request) throws IOException {
		JSONObject jsonObject = JSONObject.fromObject(json);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>)jsonObject;
		HYTaskInfo taskInfo = new HYTaskInfo();
		taskInfo.content = (String)map.get("content");
		taskInfo.photoNumber = (String)map.get("photoNumber");
		taskInfo.money = (String)map.get("money");
		taskInfo.setSendTime((String)map.get("sendTime"));
		String imageString = (String)map.get("image");
		
		if (imageString != null) { // ��ͼƬ
			byte[] image = TaskServlet.hex2byte(imageString);
			
			// ʱ�� �ֻ����� + 52λ����ַ�����Ϊname���ԣ��´�ͨ��name������ȡ��Ӧ��ͼƬ
			String randomStr = TaskServlet.getRandomString(52);
			// �õ����̵�����·��
			String realPathString = request.getRealPath(""); 
			// �õ�����˴洢λ��
			String preString = "\\WebRoot\\taskImage\\"; // ǰ׺
			String imageSave = String.format("%s%s.png", taskInfo.photoNumber, randomStr); // �ļ�����
			
			// ����ͼƬ�Ժ���ʵ�URL
			// �磺 http://localhost:8080/HYLazyCatServer/WebRoot/taskImage
			String iP = "http://139.199.204.216:8080"; 
//			taskInfo.imageUrl = "http://localhost:8080/HYLazyCatServer/WebRoot/taskImage/" + imageSave;
			// ������
			taskInfo.imageUrl = iP + "/HYLazyCatServer/WebRoot/taskImage/" + imageSave;
			
			imageSave = realPathString + preString + imageSave;
			
			// ��ͼƬ����д�뱾��(���߳���д��)
			String defaultSaveImageString = realPathString + "\\WebRoot\\taskImage";
			File fileDirFile = new File(defaultSaveImageString);
			if (!fileDirFile.exists()) {
				fileDirFile.mkdirs();
			}
			System.out.println("�ļ����Ƿ����" + fileDirFile.exists());
			File file = new File(imageSave);
			if (!file.exists()) {
				file.createNewFile();
			}
			System.out.println("�ļ��Ƿ����" + file.exists());
			
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			System.out.println(image);
			fileOutputStream.write(image);
			fileOutputStream.close();
			System.out.println("��д���ļ�");
		}
		
		return taskInfo;
	}
	
	
	public static String getJsonByReq(HttpServletRequest request) throws UnsupportedEncodingException, IOException {
		String json = "";
		// ��ȡ�����json
		BufferedReader bufferedReader = null;
		// ��ȡ��ʱ��ʹ��UTF-8,���ֽ�ת�����ַ�
		bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
		String tem;
		while((tem = bufferedReader.readLine()) != null){
			json = json + tem;
		}
		bufferedReader.close();
		return json;
	}
	
	// ��������ַ���
	public static String getRandomString(int length) { //length��ʾ�����ַ����ĳ���
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789QWERTYUIOPASDFGHJKLZXCVBNM";  
	    Random random = new Random();
	    StringBuffer sb  = new StringBuffer();
	    for (int i = 0; i < length; i++) {
			sb.append(base.charAt(random.nextInt(length)));
		}
	    
	    return sb.toString();   
	 } 
	
	// �ַ���ת������  
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

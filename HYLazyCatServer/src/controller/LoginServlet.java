package controller;
// 用于处理登陆请求
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.HYLoginInfo;

import tool.HYUserInfoTool;

import java.sql.SQLException;
import java.util.*;

public class LoginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter writer = resp.getWriter();
		writer.write("djadaja");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("doPost");
		String photoNumber = req.getParameter("photoNumber"); 
		String pwd = req.getParameter("pwd");
		// 登陆示例ng
		HYUserInfoTool userInfoTool = HYUserInfoTool.shareUserInfoTool();
		HYLoginInfo loginInfo = new HYLoginInfo();
		loginInfo.photoNumber = photoNumber;
		loginInfo.pwd = pwd;
		boolean result = false;
		try {
			result = userInfoTool.loginInfoToDataBase(loginInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			PrintWriter pWriter = resp.getWriter();
			if (result == true) {
				pWriter.write("true");
			} else {
				pWriter.write("false");
			}
			
		}
		
	}

	// 发送数组数据示例
	public void test() {
//		System.out.println("doGet");
//		ServletOutputStream sOutputStream = resp.getOutputStream();
//		Vector vector = new Vector();
//		Map<String, String> mapOne = new HashMap<String, String>();
//		mapOne.put("name", "荒原");
//		vector.add(mapOne);
//		
//		Map<String, String> mapTwo = new HashMap<String, String>();
//		mapTwo.put("photoNumber", "13527206719");
//		vector.add(mapTwo);
//	
//		Map<String, String> mapThree = new HashMap<String, String>();
//		mapThree.put("地址", "吉林大学珠海学院");
//		vector.add(mapThree);
//		
//		String string =  vector.toString();
//		byte[] bytes = string.getBytes();
//		sOutputStream.write(bytes);
	}
}

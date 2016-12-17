package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.HYUserInfoTool;

import model.userInfo.HYAlertUserInfo;

public class AlertUserInfoServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HYAlertUserInfo alertUserInfo = new HYAlertUserInfo();
		alertUserInfo.photoNumber = (String)request.getParameter("photoNumber");
		alertUserInfo.useName = (String)request.getParameter("useName");
		alertUserInfo.pwd = (String)request.getParameter("pwd");
		alertUserInfo.sex = (String)request.getParameter("sex");
		alertUserInfo.perId = (String)request.getParameter("perId");
		alertUserInfo.schoolName = (String)request.getParameter("schoolName");
		
		boolean result = true;
		
		try {
			result = HYUserInfoTool.shareUserInfoTool().alertUserInfo(alertUserInfo);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			PrintWriter writer = response.getWriter();
			if (result) {
				writer.write("true");
			} else {
				writer.write("false");
			}
		}
	}

}

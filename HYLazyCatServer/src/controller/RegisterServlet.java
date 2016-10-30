package controller;
// ����ע������
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.HYRegiserInfo;
import model.HYValidateInfo;

import tool.HYUserInfoTool;

public class RegisterServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		HYUserInfoTool userInfoTool = HYUserInfoTool.shareUserInfoTool();
		HYRegiserInfo regiserInfo = new HYRegiserInfo();
		regiserInfo.useName = req.getParameter("userName");
		regiserInfo.pwd = req.getParameter("pwd");
		regiserInfo.perId = req.getParameter("perId");
		regiserInfo.schoolName = req.getParameter("schoolName");
		regiserInfo.photoNumber = req.getParameter("photoNumber");
		regiserInfo.sex = req.getParameter("sex");
		
		
		boolean result = false;
		try {
			result = userInfoTool.savaRegisterInfo(regiserInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			PrintWriter pWriter = resp.getWriter();
			if (result == true) {
				pWriter.write("true");
			} else {
				pWriter.write("false");
			}
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
	}

}

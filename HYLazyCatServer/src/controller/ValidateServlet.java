package controller;
// 用于处理验证请求
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.HYValidateInfo;

import tool.HYUserInfoTool;

public class ValidateServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String perId = request.getParameter("perId");
		String userName = request.getParameter("userName");
		System.out.println(perId + " " + userName);
		HYUserInfoTool userInfoTool = HYUserInfoTool.shareUserInfoTool();
		HYValidateInfo validateInfo = new HYValidateInfo();
		validateInfo.perId = perId;
		validateInfo.userName = userName;
		boolean result = false;
		try {
			result = userInfoTool.validateToDataBase(validateInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			PrintWriter pWriter = response.getWriter();
			if (result == true) {
				pWriter.write("true");
			} else {
				pWriter.write("false");
			}
		}
	}

}

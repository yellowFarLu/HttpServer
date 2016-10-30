package controller;
// 通过传来的手机号码，返回对应的个人信息
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.HYUserInfoTool;

public class GetPersonInfo extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String photoNumber = request.getParameter("photoNumber");
		HYUserInfoTool userInfoTool = HYUserInfoTool.shareUserInfoTool();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userInfoTool.personalInfoWithPhotoNumber(photoNumber);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ServletOutputStream sOutputStream = response.getOutputStream();
			String mapString = map.toString();
			byte[] mapArr = mapString.getBytes();
			sOutputStream.write(mapArr);
			sOutputStream.close();
		}
	}

}

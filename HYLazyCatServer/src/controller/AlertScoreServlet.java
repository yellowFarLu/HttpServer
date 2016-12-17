package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.HYUserInfoTool;

public class AlertScoreServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String photoNumber = request.getParameter("photoNumber"); 
		String honestyScoreStr = request.getParameter("honestyScore");
		int honestyScore = 100;
		if (honestyScoreStr != null) {
			honestyScore = Integer.parseInt(honestyScoreStr);
		}
		
		try {
			HYUserInfoTool.shareUserInfoTool().alertPeopleScore(photoNumber, honestyScore);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			PrintWriter writer = response.getWriter();
			writer.close();
		}
		
		
		
	}

}

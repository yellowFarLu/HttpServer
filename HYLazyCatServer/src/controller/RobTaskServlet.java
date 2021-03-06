package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.HYRobInfo;

import tool.HYTaskInfoTool;

public class RobTaskServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HYRobInfo robInfo = new HYRobInfo();
		robInfo.setTaskId(request.getParameter("taskId"));
		robInfo.setPhotoNumber(request.getParameter("photoNumber"));
		
		HYTaskInfoTool taskInfoTool = HYTaskInfoTool.shareTaskInfoTool();
		Boolean result = false;
		try {
			result = taskInfoTool.inserthasRobTask(robInfo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			PrintWriter writer = response.getWriter();
			writer.write(String.valueOf(result));
			writer.close();
		}
	
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}

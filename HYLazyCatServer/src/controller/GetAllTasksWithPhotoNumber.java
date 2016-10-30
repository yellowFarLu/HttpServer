package controller;
// 处理某个人关联的所有任务

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.HYTaskInfoTool;

public class GetAllTasksWithPhotoNumber extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String photoNumber = request.getParameter("photoNumber");
		HYTaskInfoTool taskInfoTool = HYTaskInfoTool.shareTaskInfoTool();
		Map<String, Object> resultMap = null;
		try {
			resultMap = taskInfoTool.getAllTaskByPhotoNumber(photoNumber);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(resultMap.toString().getBytes());
			servletOutputStream.close();
		}
	}

}

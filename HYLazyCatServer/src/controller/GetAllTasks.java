package controller;
// 返回给客户端所有的任务数据
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import tool.HYTaskInfoTool;

public class GetAllTasks extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HYTaskInfoTool taskInfoTool = HYTaskInfoTool.shareTaskInfoTool();
		Map<String, Object> resultMap = null;
		try {
			resultMap = taskInfoTool.getAllTask();
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

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	
	}

}

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.HYValidateInfo;
import tool.HYUserInfoTool;

/**
 * 插入验证信息
 * */
public class InsertValidateServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String perId = request.getParameter("perId");
		String userName = request.getParameter("userName");
//		System.out.println(perId + " " + userName);
		HYUserInfoTool userInfoTool = HYUserInfoTool.shareUserInfoTool();
		HYValidateInfo validateInfo = new HYValidateInfo();
		validateInfo.perId = perId;
		validateInfo.userName = userName;
		validateInfo.photoNumber = request.getParameter("photoNumber");
		
		boolean result = false;
		try {
			result = userInfoTool.insertValidate(validateInfo);
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

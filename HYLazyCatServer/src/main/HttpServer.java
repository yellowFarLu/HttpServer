package main;
import java.sql.SQLException;

import model.HYLoginInfo;
import model.HYRegiserInfo;
import tool.HYUserInfoTool;

public class HttpServer {
	
	
	
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		HYUserInfoTool userInfoTool = HYUserInfoTool.shareUserInfoTool();
		// ��½ʾ��
//		HYLoginInfo loginInfo = new HYLoginInfo();
//		loginInfo.photoNumber = "13527206719";
//		loginInfo.pwd = "123456";
//		boolean result = userInfoTool.loginInfoToDataBase(loginInfo);
//		System.out.println(result);
		// ע��ʾ��
//		HYRegiserInfo regiserInfo = new HYRegiserInfo();
//		regiserInfo.useName = "��Զ";
//		regiserInfo.pwd = "123456";
//		regiserInfo.perId = "440981199505010838";
//		regiserInfo.schoolName = "���ִ�ѧ�麣ѧԺ";
//		regiserInfo.photoNumber = "13527206719";
//		regiserInfo.sex = "man";
//		
//		userInfoTool.savaRegisterInfo(regiserInfo);
	}

}

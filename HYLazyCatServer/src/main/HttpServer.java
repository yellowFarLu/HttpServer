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
		// 登陆示例
//		HYLoginInfo loginInfo = new HYLoginInfo();
//		loginInfo.photoNumber = "13527206719";
//		loginInfo.pwd = "123456";
//		boolean result = userInfoTool.loginInfoToDataBase(loginInfo);
//		System.out.println(result);
		// 注册示例
//		HYRegiserInfo regiserInfo = new HYRegiserInfo();
//		regiserInfo.useName = "黄远";
//		regiserInfo.pwd = "123456";
//		regiserInfo.perId = "440981199505010838";
//		regiserInfo.schoolName = "吉林大学珠海学院";
//		regiserInfo.photoNumber = "13527206719";
//		regiserInfo.sex = "man";
//		
//		userInfoTool.savaRegisterInfo(regiserInfo);
	}

}

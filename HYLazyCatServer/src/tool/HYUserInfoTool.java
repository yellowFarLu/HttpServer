package tool;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import model.HYLoginInfo;
import model.HYRegiserInfo;
import model.HYValidateInfo;

/* 用户信息工具类(单例)：  用于从数据库中加载用户有关信息 */
public class HYUserInfoTool {
	Connection connection;
    Statement statement;
    static HYUserInfoTool _userInfoTool = null;
    
    // 创建单利对象
    public static HYUserInfoTool shareUserInfoTool() {
		if (_userInfoTool == null) {
			_userInfoTool = new HYUserInfoTool();
		}
    	return _userInfoTool;
	}
    
    // 懒加载
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		if(connection == null)
		{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/HYLazyCatDataBase";
			String user = "root";
			String pwdString = "";
			// 链接到数据库
			connection = DriverManager.getConnection(url, user, pwdString);
		}	
		return connection;
	}
	

	public Statement getStatement() throws SQLException, ClassNotFoundException {
		if (statement == null) {
			statement = _userInfoTool.getConnection().createStatement();
		}
		return statement;
	}

	// 将注册的信息放入到数据库
	public boolean savaRegisterInfo(HYRegiserInfo regiserInfo) throws SQLException, ClassNotFoundException{		
		String creatSql = "create table if not exists userInfo(photoNumber char(11) default NULL, " +
				"pwd varchar(30), userName varchar(30) DEFAULT NULL, " +
				"sex char(3), perId char(32) default NULL,school varchar(100) default NULL, " +
				"honestyScore int, isSendPerson tinyint(1), isRobPerson tinyint(1), primary key(photoNumber));";
		
		int result = _userInfoTool.getStatement().executeUpdate(creatSql);
	
			
		statement = _userInfoTool.getStatement();
		boolean resultBool = false;
		if (result == 0) { 
			int isSendTaskPersonInt = regiserInfo.isSendTaskPerson == true ? 1 : 0;
			int isRobTaskPersonInt = regiserInfo.isRobTaskPerson == true ? 1 : 0;
			String insertSql = String.format("insert into userInfo(photoNumber, userName, pwd, sex, perId, school, isSendPerson, isRobPerson)" +
					" values('%s', '%s', '%s', '%s', '%s', '%s', %d, %d);", regiserInfo.photoNumber, regiserInfo.useName, regiserInfo.pwd, 
					regiserInfo.sex, regiserInfo.perId, regiserInfo.schoolName, isSendTaskPersonInt, isRobTaskPersonInt);
			result = statement.executeUpdate(insertSql);
			
			if (result == 1) {
				System.out.println("插入成功");
				resultBool = true;
			}
		} else {
			System.out.println("建表失败");
		}
		
		// 刷新输出流
		if (connection != null) {
			connection.close();
		}
		
		if (statement != null) {
			statement.close();
		}
		
		return resultBool;
	}
	
	// 查看数据库里面是否有手机号码对应的记录，查看记录中的密码和传入的密码是否一致,一致返回true，否则返回false
	public boolean loginInfoToDataBase(HYLoginInfo loginInfo) throws SQLException, ClassNotFoundException{
		boolean result = false;
		statement = _userInfoTool.getStatement();
		String queryString = String.format("select * from userInfo where photoNumber like '%s' and pwd like '%s'",
				loginInfo.photoNumber, loginInfo.pwd);
		ResultSet resultSet = statement.executeQuery(queryString);
		if (resultSet.next()) {
			result = true;
		}
		
		// 刷新输出流
		if (connection != null) {
			connection.close();
			connection = null;
		}
		
		if (statement != null) {
			statement.close();
			statement = null;
		}
		
		return result;
	}
	
	// 验证系统：查看数据库有没有对应身份证号码的名字
	public boolean validateToDataBase(HYValidateInfo validateInfo) throws SQLException, ClassNotFoundException{
		boolean result = false;
		statement = _userInfoTool.getStatement();
		String queryString = String.format("select * from userInfo where perId like '%s' and userName like '%s'",
				validateInfo.perId, validateInfo.userName);
		ResultSet resultSet = statement.executeQuery(queryString);
		if (resultSet.next()) {
			result = true;
		}
		

		// 刷新输出流
		if (connection != null) {
			connection.close();
			connection = null;
		}
		
		if (statement != null) {
			statement.close();
			statement = null;
		}
		
		return result;
	}

	// 返回手机号码对应的个人信息
	public Map<String, String> personalInfoWithPhotoNumber(String photoNumber) throws SQLException, ClassNotFoundException {
		statement = _userInfoTool.getStatement();
		String queryPersonalInfoString = String.format("select * from userInfo where photoNumber like '%s'", photoNumber);
		ResultSet resultSet = statement.executeQuery(queryPersonalInfoString);
		ResultSetMetaData rsMetaData = resultSet.getMetaData();
		int count = rsMetaData.getColumnCount();
		
		Map<String, String> map = new HashMap<String, String>();
		while (resultSet.next()) {
			for (int i = 1; i <= count; i++) {
				String key = rsMetaData.getColumnName(i); 
				String value = resultSet.getString(i);
				key = String.format("\"%s\"", key);
				value = String.format("\"%s\"", value);
				map.put(key, value);
				System.out.println(key + " " + value);
			}
			
		}

		// 刷新输出流
		if (connection != null) {
			connection.close();
			connection = null;
		}
		
		if (statement != null) {
			statement.close();
			statement = null;
		}
		
		return map;
	}
}

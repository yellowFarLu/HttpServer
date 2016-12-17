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
import model.HYValidateInfo;
import model.userInfo.HYAlertUserInfo;
import model.userInfo.HYRegiserInfo;

/* �û���Ϣ������(����)��  ���ڴ����ݿ��м����û��й���Ϣ */
public class HYUserInfoTool {
	Connection connection;
    Statement statement;
    static HYUserInfoTool _userInfoTool = null;
    
    // ������������
    public static HYUserInfoTool shareUserInfoTool() {
		if (_userInfoTool == null) {
			_userInfoTool = new HYUserInfoTool();
		}
    	return _userInfoTool;
	}
	
	/**
	 * �޸��û���Ϣ
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	public boolean alertUserInfo(HYAlertUserInfo alertUserInfo) throws ClassNotFoundException, SQLException {
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		
		boolean resultPhotoNumber = true, resultPwd = true, resultSex = true, resultPerId = true, 
				resultUserName = true, resultSchool = true;
	
		// *****NULLZ*****...***123456����ǰ̨Լ�����ַ��������������ʱ�򣬲��޸ģ�
		if (!alertUserInfo.photoNumber.equals("*****NULLZ*****...***123456")) {
			String update = String.format("update userInfo set photoNumber = '%s' where photoNumber like '%s'", 
					alertUserInfo.photoNumber, alertUserInfo.photoNumber);
			resultPhotoNumber = (statement.executeUpdate(update) == 1);
		}
		
		if (!alertUserInfo.pwd.equals("*****NULLZ*****...***123456")) {
			String update = String.format("update userInfo set pwd = '%s' where photoNumber like '%s'", 
					alertUserInfo.pwd, alertUserInfo.photoNumber);
			resultPwd = (statement.executeUpdate(update) == 1);
		}
		
		if (!alertUserInfo.sex.equals("*****NULLZ*****...***123456")) {
			String update = String.format("update userInfo set sex = '%s' where photoNumber like '%s'", 
					alertUserInfo.sex, alertUserInfo.photoNumber);
			resultSex = (statement.executeUpdate(update) == 1);
		}
		
		if (!alertUserInfo.perId.equals("*****NULLZ*****...***123456")) {
			String update = String.format("update userInfo set perId = '%s' where photoNumber like '%s'",
					alertUserInfo.perId, alertUserInfo.photoNumber);
			resultPerId = (statement.executeUpdate(update) == 1);
		}
		
		if (!alertUserInfo.useName.equals("*****NULLZ*****...***123456")) {
			String update = String.format("update userInfo set userName = '%s' where photoNumber like '%s'", 
					alertUserInfo.useName, alertUserInfo.photoNumber);
			resultUserName = (statement.executeUpdate(update) == 1);
		}
		
		if (!alertUserInfo.schoolName.equals("*****NULLZ*****...***123456")) {
			String update = String.format("update userInfo set school = '%s' where photoNumber like '%s'", 
					alertUserInfo.schoolName, alertUserInfo.photoNumber);
			resultSchool = (statement.executeUpdate(update) == 1);
		}
		
		DBUtil.closeCon(conn);
		
		return (resultPhotoNumber && resultPwd && resultSex && resultPerId && resultUserName 
				&& resultSchool);
	}

	
	/**
	 * �����û���
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	int createUser_Table(){
		String creatSql = "create table if not exists userInfo(photoNumber char(11), " +
				"userName varchar(30) DEFAULT NULL, sex char(3), perId char(32) default NULL," +
				"school varchar(100) default NULL, honestyScore int, isDelete tinyInt(1) default 0," +
				"primary key(photoNumber));";
		
		int result = 0;
		try {
			Connection conn = DBUtil.getCon();
			result = conn.createStatement().executeUpdate(creatSql);
			DBUtil.closeCon(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ��ע�����Ϣ���뵽���ݿ�
	 * */ 
	public boolean savaRegisterInfo(HYRegiserInfo regiserInfo) throws SQLException, ClassNotFoundException{		
		// ������Ϣ���뵽�û���		
		int result = createUser_Table();
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		boolean resultBool = false;
		if (result == 0) { 
			String insertSql = String.format("insert into userInfo(photoNumber, userName, sex, perId, school, honestyScore)" +
					" values('%s', '%s', '%s', '%s', '%s', %d);", regiserInfo.photoNumber, regiserInfo.useName, 
					regiserInfo.sex, regiserInfo.perId, regiserInfo.schoolName, regiserInfo.honestyScore);
			result = statement.executeUpdate(insertSql);
			
			if (result == 1) {
				System.out.println("����ɹ�");
				resultBool = true;
			}
		} else {
			System.out.println("����ʧ��");
		}
		
		// ���������ֻ��������
		String createPwdTable = "create table if not exists pwd_table(photoNumber char(11), " +
				"pwd varchar(16),primary key(photoNumber, pwd));";
		result = statement.executeUpdate(createPwdTable);
		if (result == 0) { 
			String insertPwd = String.format("insert into pwd_table values('%s', '%s')", 
					regiserInfo.photoNumber, regiserInfo.pwd);
			result = statement.executeUpdate(insertPwd);
			
			if (result == 1) {
				System.out.println("���������ɹ�");
				resultBool = true;
			}
		} else {
			System.out.println("����ʧ��");
		}
		
		// ˢ�������
		DBUtil.closeCon(conn);
		
		return resultBool;
	}
	
	// �鿴���ݿ������Ƿ����ֻ������Ӧ�ļ�¼���鿴��¼�е�����ʹ���������Ƿ�һ��,һ�·���true�����򷵻�false
	public boolean loginInfoToDataBase(HYLoginInfo loginInfo) throws SQLException, ClassNotFoundException{
		boolean result = false;
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		String queryString = String.format("select * from pwd_table where photoNumber like '%s' and pwd like '%s'",
				loginInfo.photoNumber, loginInfo.pwd);
		ResultSet resultSet = statement.executeQuery(queryString);
		if (resultSet.next()) {
			result = true;
		}
		
		// ˢ�������
		DBUtil.closeCon(conn);
		
		return result;
	}
	
	// �޸ĸ��˵����÷���
	public void alertPeopleScore(String photoNumber, int score) throws SQLException, ClassNotFoundException {
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		int oldScore = 0, newScore=0;
		String queryString = String.format("select * from userInfo where photoNumber like '%s'", photoNumber);
		ResultSet resultSet = statement.executeQuery(queryString);
		while (resultSet.next()) {
			oldScore = resultSet.getInt("honestyScore");
			break;
		}
		newScore = (score + oldScore) / 2;
		String undateScore = String.format("update userInfo set honestyScore = %d where photoNumber = '%s';", 
				newScore, photoNumber);
		statement.executeUpdate(undateScore);
		DBUtil.closeCon(conn);
	}

	
	// ��֤ϵͳ
	public boolean validateToDataBase(HYValidateInfo validateInfo) throws SQLException, ClassNotFoundException{
		boolean result = true;
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		String queryString = String.format("select * from userInfo where photoNumber like '%s'",
				validateInfo.photoNumber);
		ResultSet resultSet = statement.executeQuery(queryString);
		while (resultSet.next()) {
			// �����ڡ�δ��֤��ݡ�
			String value = resultSet.getString("perId");
			if (value.equals("δ��֤���")) {
				result = false;
				break;
			}
		}

		// ˢ�������
		DBUtil.closeCon(conn);
		
		return result;
	}
	
	/**
	 * 	������֤��Ϣ�����֤���������Ա�
	 * 
	 * 	�鿴������������û�У��вŰ���Ϣ���뵽�û���
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * */
	public boolean insertValidate(HYValidateInfo validateInfo) throws SQLException, ClassNotFoundException {
		boolean result = false;
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		
		// ���ɹ�����
		String createString = "create table if not exists police(perId char(32), " +
				"userName varchar(30) DEFAULT NULL, " +
				"sex char(3), PRIMARY KEY(perId));";
		statement.executeUpdate(createString);
		
		// ���û������֤�ڲ��ڹ����������ݿ�����
		String query = String.format("select * from police where perId like '%s' and userName like '%s' and " +
				"sex like '%s'", validateInfo.perId, validateInfo.userName, validateInfo.sex);
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			result = true;
		}
		
		// ������˵���֤��Ϣ���뵽�û���
		String updatePerIdString = String.format("update userInfo set perId = '%s' where photoNumber like '%s'", 
						validateInfo.perId, validateInfo.photoNumber);
		statement.executeUpdate(updatePerIdString);
			
		String updateUsernameString = String.format("update userInfo set userName = '%s' where photoNumber like '%s'", 
						validateInfo.userName, validateInfo.photoNumber);
		statement.executeUpdate(updateUsernameString);
	
		String updateSexString = String.format("update userInfo set sex = '%s' where photoNumber like '%s'", 
				validateInfo.sex, validateInfo.photoNumber);
		statement.executeUpdate(updateSexString);
		
		// ˢ�������
		DBUtil.closeCon(conn);
		
		return result;
	}
	

	// �����ֻ������Ӧ�ĸ�����Ϣ
	public Map<String, String> personalInfoWithPhotoNumber(String photoNumber) throws SQLException, ClassNotFoundException {
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		String queryPersonalInfoString = String.format("select * from userInfo, pwd_table where " +
				"userInfo.photoNumber " +
				"like '%s' and userInfo.photoNumber = pwd_table.photoNumber;", photoNumber);
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
//				System.out.println(key + " " + value);
			}
			
		}

		// ˢ�������
		DBUtil.closeCon(conn);
		
		return map;
	}
}




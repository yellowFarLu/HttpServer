package tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import model.HYRobInfo;
import model.HYTaskInfo;
// ������Ϣ�Ĺ�����

public class HYTaskInfoTool {
	Connection connection;
    Statement statement;
    
    static class SingleTaskInfoTool{
    	// ����
    	static final HYTaskInfoTool _taskInfoTool = new HYTaskInfoTool();
    }
	
	
	// ���캯��˽�л�����ʹ����ֻ��ͨ��share������õ���
	private HYTaskInfoTool() {
		// TODO Auto-generated constructor stub
	}
	
	// ��1��˽�л����캯��  ��2��ʹ�þ�̬�ڲ���(�����ڲ���̬��ֻ�ᱻ����һ�Σ��ʸ�ʵ�ַ�ʽʱ�̰߳�ȫ��)
	public static HYTaskInfoTool shareTaskInfoTool() {
		return SingleTaskInfoTool._taskInfoTool;
	}
	
	
	
	/**
	 * ��������������ֶ�����Ϊ1
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	public void compeletTask(String taskId) throws ClassNotFoundException, SQLException {
		statement = DBUtil.getCon().createStatement();
		int taskIdInt = Integer.parseInt(taskId);
		String updateString = String.format("update taskInfo set isCompelete = 1 where taskId = %d", taskIdInt);
		statement.executeUpdate(updateString);
	}
	
	/**
	 * ���������
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	int createTask_table() throws ClassNotFoundException, SQLException{
		this.statement = DBUtil.getCon().createStatement();
		String createSql = "Create table if not exists taskInfo(taskId int primary key auto_increment," +
				"content text, imageUrl text,  isRob tinyint(1), isCompelete tinyint(1));";
		return statement.executeUpdate(createSql);
	}
	
	
	/**
	 * �����ѷ��ͱ�
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	int createHasSend_Table() throws SQLException, ClassNotFoundException {
		this.statement = DBUtil.getCon().createStatement();
		HYUserInfoTool.shareUserInfoTool().createUser_Table();
		String createHasSendTaskSql = "Create table if not exists hasSendTask(" +
				"photoNumber char(11)," +
				"taskId int, money text,sendTime text," +
				"primary key(photoNumber, taskId)," +
				"foreign key(photoNumber) references userInfo(photoNumber), " +
				"foreign key(taskId) references taskInfo(taskId));";
		return statement.executeUpdate(createHasSendTaskSql);
	}
	
	/**
	 * ����������
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	int createHasRob_Table() throws SQLException, ClassNotFoundException {
		this.statement = DBUtil.getCon().createStatement();
		HYUserInfoTool.shareUserInfoTool().createUser_Table();
		String createRobTableString = "Create table if not exists hasRobTask(" +
				"RobPhotoNumber char(11), RobTaskId int(11), " +
				"primary key(RobPhotoNumber, RobTaskId)," +
				"foreign key(RobPhotoNumber) references userInfo(photoNumber)," +
				"foreign key(RobTaskId) references taskInfo(taskId));";
		return statement.executeUpdate(createRobTableString);
	}
	
	
	// ���������񣨷���������
	public boolean insertTaskWithTaskInfo(HYTaskInfo taskInfo) throws SQLException, ClassNotFoundException {
		Connection conn = DBUtil.getCon(); 
		statement = conn.createStatement();
		// ���������
		int resultInt = this.createTask_table();
		
		// ���²������������Id
		int taskIdForHasSendTaskTable = 0; 
		boolean resultOfInsertTaskInfo = false;
		if (resultInt == 0) {
			System.out.println("���������ɹ�");
			int isRobInt = taskInfo.isRob == true ? 1 : 0;
			int isCompeleteInt = taskInfo.isCompelete == true ? 1 : 0;

			String insertSql = String.format("insert into taskInfo(" +
					"content, imageUrl, isRob, isCompelete) values('%s', '%s', %d, %d);",
					taskInfo.content, taskInfo.imageUrl, isRobInt, isCompeleteInt);
			resultInt = statement.executeUpdate(insertSql);
			if (resultInt == 1) {
				System.out.println("���������ɹ�");
				resultOfInsertTaskInfo = true;
			}
			
			//�����������������һ������ID����Ϊ���²�������Id
			String quryTaskId = String.format("select taskId from taskInfo where imageUrl like '%s'", taskInfo.imageUrl);
			ResultSet set = statement.executeQuery(quryTaskId);
			while(set.next()){
				taskIdForHasSendTaskTable = set.getInt("taskId");
			}
			
		} else {
			System.out.println("���������ʧ��");
		}
		
		
		// ���� �ѷ����������
		boolean resultOfHasSendTask = false;
		resultInt = this.createHasSend_Table();
		if (resultInt == 0) {
			System.out.println("�����ѷ����ı�ɹ�");
			
			String insertHasSendTaskString = String.format("insert into hasSendTask(photoNumber, taskId, money, sendTime) values('%s', %s, '%s', '%s');", 
					taskInfo.photoNumber, taskIdForHasSendTaskTable, taskInfo.money, taskInfo.getSendTime());
			resultInt = statement.executeUpdate(insertHasSendTaskString);
			if (resultInt == 1) {
				System.out.println("�����ѷ��������ɹ�");
				resultOfHasSendTask = true;
			}
//			double aaa =  Double.parseDouble(taskInfo.money);
//			System.out.println("������ĸ�����" + aaa);
		} else {
			System.out.println("�����ѷ�����ʧ��");
		}
		
		// �ر�������
		DBUtil.closeCon(conn);
		
		return (resultOfInsertTaskInfo == true && resultOfHasSendTask == true);
	}
	
	// ��������ж�Ӧ������Ƿ�����Ϊ1��UI���洦���1���Ͱ�������ťչʾΪ���ɵ����
	public boolean inserthasRobTask(HYRobInfo robInfo) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		Connection conn = DBUtil.getCon();
		this.statement = conn.createStatement();
		
		// ���������
		String updataRobSql = String.format("UPDATE taskInfo set isRob = 1 where taskId like '%s'", robInfo.getTaskId());
		int reslutInt = this.statement.executeUpdate(updataRobSql);
		reslut = reslutInt == 1 ? true : false;
		
		// �����µļ�¼���������������
		int resultInt = this.createHasRob_Table();
		boolean resultRob = false;
		if (resultInt == 0) {
			System.out.println("���������ı�ɹ�");	
			String insertHasRobTaskString = String.format("insert into hasRobTask(RobPhotoNumber, RobTaskId) values(%s, %s);", 
					robInfo.getPhotoNumber(), robInfo.getTaskId());
			resultInt = statement.executeUpdate(insertHasRobTaskString);
			if (resultInt == 1) {
				resultRob = true;
				System.out.println("�����ѷ��������ɹ�");
			}

		} else {
			System.out.println("�����ѷ�����ʧ��");
		}
		
		// �ر�������
		DBUtil.closeCon(conn);
		return (reslut&&resultRob);
	}
	
	// ɾ�����񣨲���������Id��
	public boolean deleteTask(String taskId) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		Connection conn = DBUtil.getCon();
		this.statement = conn.createStatement();
		
		// ɾ���ѷ����������ȥ�����Լ����
		String deletehasSendtaskSql = String.format("delete from hassendtask where taskId like '%s'", taskId);
		this.statement.executeUpdate(deletehasSendtaskSql);
		
		// ɾ�������
		String deleteTaskSql = String.format("delete from taskInfo where taskId like '%s'", taskId);
		int reslutInt = this.statement.executeUpdate(deleteTaskSql);
		reslut = reslutInt == 1 ? true : false;
		
		// �ر�������
		DBUtil.closeCon(conn);
		return reslut;
	}
	
	// ��ȡȫ������
	public Map<String, Object> getAllTask() throws SQLException, ClassNotFoundException{
		Vector<Map<String, Object>> mapArr = new Vector<Map<String,Object>>();
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		
		// һ��ʼ��ȡ�����⼸�������ڣ�����Ҫ����
		this.createTask_table();
		this.createHasSend_Table();
		this.createHasRob_Table();
		
		String queryAllTask = "select * from ((taskInfo natural join hasSendTask) natural join userInfo ua) " +
				"left join hasrobtask on taskinfo.taskId = hasrobtask.RobTaskId;";
		ResultSet resultSet = statement.executeQuery(queryAllTask);
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int countCol = resultSetMetaData.getColumnCount();
		
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = null;
			String value = null;
			for (int i = 1; i <= countCol; i++) {
				key = resultSetMetaData.getColumnName(i);
				value = resultSet.getString(i);
				key = String.format("\"%s\"", key);
				value = String.format("\"%s\"", value);
				map.put(key, value);
//				System.out.println(key + " " + value);
			}
			
		    mapArr.add(map);
		}
		
		
		Map<String, Object> mapForTasks = new HashMap<String, Object>();
		mapForTasks.put("tasks", mapArr);
		
		// �ر�������
		DBUtil.closeCon(conn);
		
		return mapForTasks;
	}
	
	// ��ȡĳ���˹�����ȫ������
	public Map<String, Object> getAllTaskByPhotoNumber(String photoNumber) throws SQLException, ClassNotFoundException{
		Vector<Map<String, Object>> mapArr = new Vector<Map<String,Object>>();
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		
		// ��ȡ����˷���������
		String queryHasSendTask = String.format("select * from ((taskInfo join hasSendTask on " +
				"taskInfo.taskId = hasSendTask.taskId) natural join userInfo) " +
				"join hasrobtask on hasrobtask.RobTaskId = taskInfo.taskId " +
				"where userInfo.photoNumber like '%s';", photoNumber);
		ResultSet resultSet = statement.executeQuery(queryHasSendTask);
		setUpMapWithResultSet(resultSet, mapArr);
		
		// ��ȡ�������������
		String queryHasRobTask = String.format("select * from (((taskInfo join hasrobtask on " +
				" taskInfo.taskId = hasrobtask.RobTaskId) NATURAL join hasSendTask) join userInfo on " +
				"userInfo.photoNumber = hasrobtask.RobPhotoNumber)  where " +
				"userInfo.photoNumber like '%s';", photoNumber);
		ResultSet resultSetTwo = statement.executeQuery(queryHasRobTask);
		setUpMapWithResultSet(resultSetTwo, mapArr);
		
		Map<String, Object> mapForTasks = new HashMap<String, Object>();
		mapForTasks.put("tasks", mapArr);
		
		// �ر�������
		DBUtil.closeCon(conn);
		return mapForTasks;
	}
	
	/***
	 * �������ݿ�����ת����Map
	 * */
	private void setUpMapWithResultSet(ResultSet resultSet, Vector<Map<String, Object>> mapArr) throws SQLException {
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int countCol = resultSetMetaData.getColumnCount();
		
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = null;
			String value = null;
			for (int i = 1; i <= countCol; i++) {
				key = resultSetMetaData.getColumnName(i);
				value = resultSet.getString(i);
				key = String.format("\"%s\"", key);
				value = String.format("\"%s\"", value);
				map.put(key, value);
//				System.out.println(key + " " + value);
			}
			
		    mapArr.add(map);
		}
	}
}

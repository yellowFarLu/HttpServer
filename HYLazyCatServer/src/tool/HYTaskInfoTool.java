package tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
	
	
	
	// ������
	public Connection getConnection() throws SQLException, ClassNotFoundException {
			if(connection == null)
			{
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/HYLazyCatDataBase";
				String user = "root";
				String pwdString = "";
				// ���ӵ����ݿ�
				connection = DriverManager.getConnection(url, user, pwdString);
			}	
			return connection;
		}
		

	public Statement getStatement() throws SQLException, ClassNotFoundException {
			if (statement == null) {
				statement = SingleTaskInfoTool._taskInfoTool.getConnection().createStatement();
			}
			return statement;
		}

	// ���������񣨷���������
	public boolean insertTaskWithTaskInfo(HYTaskInfo taskInfo) throws SQLException, ClassNotFoundException {
		// ���������
		boolean resultOfInsertTaskInfo = false;
		String createSql = "Create table if not exists taskInfo(taskId int primary key auto_increment," +
				"content text, imageUrl text,  isRob tinyint(1), isCompelete tinyint(1));";
		statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		
		// ���²������������Id
		int taskIdForHasSendTaskTable = 0; 
		int resultInt = statement.executeUpdate(createSql);
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
		String createHasSendTaskSql = "Create table if not exists hasSendTask(photoNumber char(11) default NULL," +
				"taskId int, money text,primary key(photoNumber, taskId)," +
				"foreign key(photoNumber) references userInfo(photoNumber), " +
				"foreign key(taskId) references taskInfo(taskId));";
		resultInt = statement.executeUpdate(createHasSendTaskSql);
		if (resultInt == 0) {
			System.out.println("�����ѷ����ı�ɹ�");
			
			String insertHasSendTaskString = String.format("insert into hasSendTask(photoNumber, taskId, money) values(%s, %s, %s);", 
					taskInfo.photoNumber, taskIdForHasSendTaskTable, taskInfo.money);
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
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		
		return (resultOfInsertTaskInfo == true && resultOfHasSendTask == true);
	}
	
	// ��������ж�Ӧ������Ƿ�����Ϊ1��UI���洦���1���Ͱ�������ťչʾΪ���ɵ����
	public boolean inserthasRobTask(String taskId) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		this.statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		String updataRobSql = String.format("UPDATE taskInfo set isRob = 1 where taskId like '%s'", taskId);
		int reslutInt = this.statement.executeUpdate(updataRobSql);
		reslut = reslutInt == 1 ? true : false;
		
		// �ر�������
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		return reslut;
	}
	
	// ɾ�����񣨲���������Id��
	public boolean deleteTask(String taskId) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		this.statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		
		// ɾ���ѷ����������ȥ�����Լ����
		String deletehasSendtaskSql = String.format("delete from hassendtask where taskId like '%s'", taskId);
		this.statement.executeUpdate(deletehasSendtaskSql);
		
		// ɾ�������
		String deleteTaskSql = String.format("delete from taskInfo where taskId like '%s'", taskId);
		int reslutInt = this.statement.executeUpdate(deleteTaskSql);
		reslut = reslutInt == 1 ? true : false;
		
		// �ر�������
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		return reslut;
	}
	
	// ��ȡȫ������
	public Map<String, Object> getAllTask() throws SQLException, ClassNotFoundException{
		Vector<Map<String, Object>> mapArr = new Vector<Map<String,Object>>();
		statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		String queryAllTask = "select * from (taskInfo natural join hasSendTask) natural join userInfo;";
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
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		
		return mapForTasks;
	}
	
	// ��ȡĳ���˹�����ȫ������
	public Map<String, Object> getAllTaskByPhotoNumber(String photoNumber) throws SQLException, ClassNotFoundException{
		Vector<Map<String, Object>> mapArr = new Vector<Map<String,Object>>();
		statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		String queryAllTask = String.format("select * from (taskInfo natural join hasSendTask) natural join userInfo where userInfo.photoNumber like '%s';", photoNumber);
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
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		
		return mapForTasks;
	}
	
	private void closeAll() throws SQLException {
		if (this.connection != null) {
			this.connection.close();
			this.connection = null;
		}
		
		if (this.statement != null) {
			this.statement.close();
			this.statement = null;
		}
	}
}

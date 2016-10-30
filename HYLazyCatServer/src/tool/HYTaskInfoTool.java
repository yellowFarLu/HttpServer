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
// 任务信息的工具类

public class HYTaskInfoTool {
	Connection connection;
    Statement statement;
    
    static class SingleTaskInfoTool{
    	// 单例
    	static final HYTaskInfoTool _taskInfoTool = new HYTaskInfoTool();
    }
	
	
	// 构造函数私有化，让使用者只能通过share函数获得单例
	private HYTaskInfoTool() {
		// TODO Auto-generated constructor stub
	}
	
	// （1）私有化构造函数  （2）使用静态内部类(由于内部静态类只会被加载一次，故该实现方式时线程安全的)
	public static HYTaskInfoTool shareTaskInfoTool() {
		return SingleTaskInfoTool._taskInfoTool;
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
				statement = SingleTaskInfoTool._taskInfoTool.getConnection().createStatement();
			}
			return statement;
		}

	// 插入新任务（发布新任务）
	public boolean insertTaskWithTaskInfo(HYTaskInfo taskInfo) throws SQLException, ClassNotFoundException {
		// 插入任务表
		boolean resultOfInsertTaskInfo = false;
		String createSql = "Create table if not exists taskInfo(taskId int primary key auto_increment," +
				"content text, imageUrl text,  isRob tinyint(1), isCompelete tinyint(1));";
		statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		
		// 最新插入任务的任务Id
		int taskIdForHasSendTaskTable = 0; 
		int resultInt = statement.executeUpdate(createSql);
		if (resultInt == 0) {
			System.out.println("创建任务表成功");
			int isRobInt = taskInfo.isRob == true ? 1 : 0;
			int isCompeleteInt = taskInfo.isCompelete == true ? 1 : 0;

			String insertSql = String.format("insert into taskInfo(" +
					"content, imageUrl, isRob, isCompelete) values('%s', '%s', %d, %d);",
					taskInfo.content, taskInfo.imageUrl, isRobInt, isCompeleteInt);
			resultInt = statement.executeUpdate(insertSql);
			if (resultInt == 1) {
				System.out.println("插入任务表成功");
				resultOfInsertTaskInfo = true;
			}
			
			//从任务表中搜索最大的一条任务ID，即为最新插入任务Id
			String quryTaskId = String.format("select taskId from taskInfo where imageUrl like '%s'", taskInfo.imageUrl);
			ResultSet set = statement.executeQuery(quryTaskId);
			while(set.next()){
				taskIdForHasSendTaskTable = set.getInt("taskId");
			}
			
		} else {
			System.out.println("创建任务表失败");
		}
		
		
		// 插入 已发布的任务表
		boolean resultOfHasSendTask = false;
		String createHasSendTaskSql = "Create table if not exists hasSendTask(photoNumber char(11) default NULL," +
				"taskId int, money text,primary key(photoNumber, taskId)," +
				"foreign key(photoNumber) references userInfo(photoNumber), " +
				"foreign key(taskId) references taskInfo(taskId));";
		resultInt = statement.executeUpdate(createHasSendTaskSql);
		if (resultInt == 0) {
			System.out.println("创建已发布的表成功");
			
			String insertHasSendTaskString = String.format("insert into hasSendTask(photoNumber, taskId, money) values(%s, %s, %s);", 
					taskInfo.photoNumber, taskIdForHasSendTaskTable, taskInfo.money);
			resultInt = statement.executeUpdate(insertHasSendTaskString);
			if (resultInt == 1) {
				System.out.println("插入已发布任务表成功");
				resultOfHasSendTask = true;
			}
//			double aaa =  Double.parseDouble(taskInfo.money);
//			System.out.println("解析后的浮点型" + aaa);
		} else {
			System.out.println("创建已发布表失败");
		}
		
		// 关闭所有流
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		
		return (resultOfInsertTaskInfo == true && resultOfHasSendTask == true);
	}
	
	// 把任务表中对应任务的是否被抢改为1（UI界面处获得1，就把抢单按钮展示为不可点击）
	public boolean inserthasRobTask(String taskId) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		this.statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		String updataRobSql = String.format("UPDATE taskInfo set isRob = 1 where taskId like '%s'", taskId);
		int reslutInt = this.statement.executeUpdate(updataRobSql);
		reslut = reslutInt == 1 ? true : false;
		
		// 关闭所有流
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		return reslut;
	}
	
	// 删除任务（参数：任务Id）
	public boolean deleteTask(String taskId) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		this.statement = SingleTaskInfoTool._taskInfoTool.getStatement();
		
		// 删除已发布的任务表（去掉外键约束）
		String deletehasSendtaskSql = String.format("delete from hassendtask where taskId like '%s'", taskId);
		this.statement.executeUpdate(deletehasSendtaskSql);
		
		// 删除任务表
		String deleteTaskSql = String.format("delete from taskInfo where taskId like '%s'", taskId);
		int reslutInt = this.statement.executeUpdate(deleteTaskSql);
		reslut = reslutInt == 1 ? true : false;
		
		// 关闭所有流
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		return reslut;
	}
	
	// 获取全部任务
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
		
		// 关闭所有流
		HYTaskInfoTool.SingleTaskInfoTool._taskInfoTool.closeAll();
		
		return mapForTasks;
	}
	
	// 获取某个人关联的全部任务
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
		
		// 关闭所有流
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

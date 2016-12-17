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
	
	
	
	/**
	 * 把任务表的已完成字段设置为1
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
	 * 生成任务表
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
	 * 生成已发送表
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
	 * 生成已抢表
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
	
	
	// 插入新任务（发布新任务）
	public boolean insertTaskWithTaskInfo(HYTaskInfo taskInfo) throws SQLException, ClassNotFoundException {
		Connection conn = DBUtil.getCon(); 
		statement = conn.createStatement();
		// 插入任务表
		int resultInt = this.createTask_table();
		
		// 最新插入任务的任务Id
		int taskIdForHasSendTaskTable = 0; 
		boolean resultOfInsertTaskInfo = false;
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
		resultInt = this.createHasSend_Table();
		if (resultInt == 0) {
			System.out.println("创建已发布的表成功");
			
			String insertHasSendTaskString = String.format("insert into hasSendTask(photoNumber, taskId, money, sendTime) values('%s', %s, '%s', '%s');", 
					taskInfo.photoNumber, taskIdForHasSendTaskTable, taskInfo.money, taskInfo.getSendTime());
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
		DBUtil.closeCon(conn);
		
		return (resultOfInsertTaskInfo == true && resultOfHasSendTask == true);
	}
	
	// 把任务表中对应任务的是否被抢改为1（UI界面处获得1，就把抢单按钮展示为不可点击）
	public boolean inserthasRobTask(HYRobInfo robInfo) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		Connection conn = DBUtil.getCon();
		this.statement = conn.createStatement();
		
		// 更新任务表
		String updataRobSql = String.format("UPDATE taskInfo set isRob = 1 where taskId like '%s'", robInfo.getTaskId());
		int reslutInt = this.statement.executeUpdate(updataRobSql);
		reslut = reslutInt == 1 ? true : false;
		
		// 插入新的纪录到已抢的任务表中
		int resultInt = this.createHasRob_Table();
		boolean resultRob = false;
		if (resultInt == 0) {
			System.out.println("创建已抢的表成功");	
			String insertHasRobTaskString = String.format("insert into hasRobTask(RobPhotoNumber, RobTaskId) values(%s, %s);", 
					robInfo.getPhotoNumber(), robInfo.getTaskId());
			resultInt = statement.executeUpdate(insertHasRobTaskString);
			if (resultInt == 1) {
				resultRob = true;
				System.out.println("插入已发布任务表成功");
			}

		} else {
			System.out.println("创建已发布表失败");
		}
		
		// 关闭所有流
		DBUtil.closeCon(conn);
		return (reslut&&resultRob);
	}
	
	// 删除任务（参数：任务Id）
	public boolean deleteTask(String taskId) throws SQLException, ClassNotFoundException {
		boolean reslut = false;
		Connection conn = DBUtil.getCon();
		this.statement = conn.createStatement();
		
		// 删除已发布的任务表（去掉外键约束）
		String deletehasSendtaskSql = String.format("delete from hassendtask where taskId like '%s'", taskId);
		this.statement.executeUpdate(deletehasSendtaskSql);
		
		// 删除任务表
		String deleteTaskSql = String.format("delete from taskInfo where taskId like '%s'", taskId);
		int reslutInt = this.statement.executeUpdate(deleteTaskSql);
		reslut = reslutInt == 1 ? true : false;
		
		// 关闭所有流
		DBUtil.closeCon(conn);
		return reslut;
	}
	
	// 获取全部任务
	public Map<String, Object> getAllTask() throws SQLException, ClassNotFoundException{
		Vector<Map<String, Object>> mapArr = new Vector<Map<String,Object>>();
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		
		// 一开始获取任务，这几个表都不在，所以要创建
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
		
		// 关闭所有流
		DBUtil.closeCon(conn);
		
		return mapForTasks;
	}
	
	// 获取某个人关联的全部任务
	public Map<String, Object> getAllTaskByPhotoNumber(String photoNumber) throws SQLException, ClassNotFoundException{
		Vector<Map<String, Object>> mapArr = new Vector<Map<String,Object>>();
		Connection conn = DBUtil.getCon();
		statement = conn.createStatement();
		
		// 获取这个人发布的任务
		String queryHasSendTask = String.format("select * from ((taskInfo join hasSendTask on " +
				"taskInfo.taskId = hasSendTask.taskId) natural join userInfo) " +
				"join hasrobtask on hasrobtask.RobTaskId = taskInfo.taskId " +
				"where userInfo.photoNumber like '%s';", photoNumber);
		ResultSet resultSet = statement.executeQuery(queryHasSendTask);
		setUpMapWithResultSet(resultSet, mapArr);
		
		// 获取这个人抢的任务
		String queryHasRobTask = String.format("select * from (((taskInfo join hasrobtask on " +
				" taskInfo.taskId = hasrobtask.RobTaskId) NATURAL join hasSendTask) join userInfo on " +
				"userInfo.photoNumber = hasrobtask.RobPhotoNumber)  where " +
				"userInfo.photoNumber like '%s';", photoNumber);
		ResultSet resultSetTwo = statement.executeQuery(queryHasRobTask);
		setUpMapWithResultSet(resultSetTwo, mapArr);
		
		Map<String, Object> mapForTasks = new HashMap<String, Object>();
		mapForTasks.put("tasks", mapArr);
		
		// 关闭所有流
		DBUtil.closeCon(conn);
		return mapForTasks;
	}
	
	/***
	 * 根据数据库数据转化成Map
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

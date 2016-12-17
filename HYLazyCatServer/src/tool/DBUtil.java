package tool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.*;
/**
 * 
 * ���ݿ������࣬ʵ�����ݿ�����Ӷ���
 *
 */
public class DBUtil {
	
	private static final String DBURL = "jdbc:mysql://localhost:3306/mysql";
	
	private static final String DBUSER = "root";
	
	private static final String DBPASSWORD = "123456";
	
	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
	
	/**
	 * ��ͳ�����ݿ�����
	 * */
	public static Connection getCon() {
		Connection conn = null;
		
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
			
			Statement statement = conn.createStatement();
			statement.executeUpdate("create database if not exists HYLazyCatDataBase;");
			statement.execute("use HYLazyCatDataBase;");
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("���ݿ����Ӵ���");
		}
		
		return conn;
	}
	
	/**
	 * �������ݿ����ӳص�����
	 * */
	public Connection getDBCon()  {
		 DataSource ds = null;
		 Connection conn=null;
		  try{ 
		  InitialContext ctx=new InitialContext(); 
		  ds=(DataSource)ctx.lookup("java:comp/env/jdbc/hello"); 
		  conn = ds.getConnection(); 
		  }
		  catch (Exception e){
			  System.out.print(e);
		  }
		  return conn;
	}
	
	public static void closeCon(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	public static void main(String []args) {
//		DBUtil dbUtil = new DBUtil();
//		try {
//			dbUtil.getCon();
//			//dbUtil.getDBCon();
//			System.out.println("���ݿ����ӳɹ�");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
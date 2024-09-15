package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUntil {
	private String url="jdbc:sqlserver://localhost:1433;" //若你的port不是預設1433,請修改
			+ "databasename=VocabularyDB;encrypt=false";
	
	//請按提示更換MSSQL 資料庫帳密
	private String user="請輸入你的MSSQL帳號";
	private String pwd="請輸入你的MSSQL密碼";
	
	public Connection getConnection() throws SQLException {		
		Connection conn = DriverManager.getConnection(url, user, pwd);
		return conn;
	}

}

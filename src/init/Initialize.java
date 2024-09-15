package init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import conn.ConnectionUntil;

public class Initialize {
	
	private static String[] tableList = { "GEPT初級", "GEPT中級", "GEPT中高級" };

	public static void initializeDatabase() {
		// 創建資料庫
		String sql = "IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'VocabularyDB') CREATE DATABASE VocabularyDB";
		ConnectionUntil cu = new ConnectionUntil();

		// 建立連接
		try (Connection conn = cu.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.executeUpdate();

			// 切換到新創建的資料庫
			conn.setCatalog("VocabularyDB");

			for (String tableName : tableList) {
				// 創建表格
				sql = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='" + tableName + "'" + "AND xtype='U') "
						+ "CREATE TABLE " + tableName + " (" + "Id INT IDENTITY(1,1) PRIMARY KEY, "
						+ "字母開頭 NVARCHAR(1), " + "字彙 NVARCHAR(100), " + "詞類 NVARCHAR(50), " + "中文 NVARCHAR(MAX), "
						+ "級數 NVARCHAR(50)" + ")";
				pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();

				System.out.println("資料庫和表格" + tableName + "創建成功");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public static void initializeTable() {
		
		//取得當前路徑
		String currentPath = System.getProperty("user.dir");
		System.out.println(currentPath);

		// 建立連接
	    ConnectionUntil cu = new ConnectionUntil();
	    try (Connection conn = cu.getConnection()) {
	        // 切換到新創建的資料庫
	        conn.setCatalog("VocabularyDB");

	        for (String tableName : tableList) {
	            // 處理文件路徑
	            String filePath = currentPath + "\\importCard\\" + tableName + ".csv";
	            filePath = filePath.replace("\\", "\\\\");

	            // SQL 語句
	            String sql = "CREATE TABLE #TempGEPT (" +
	                "字母開頭 NVARCHAR(50), " +
	                "字彙 NVARCHAR(50), " +
	                "詞類 NVARCHAR(50), " +
	                "中文 NVARCHAR(MAX), " +
	                "級數 NVARCHAR(50)" +
	                ");" +
	                "BULK INSERT #TempGEPT " +
	                "FROM '" + filePath + "' " +
	                "WITH (" +
	                "FIELDTERMINATOR = ',', " +
	                "ROWTERMINATOR = '\\n', " +
	                "FIRSTROW = 2, " +
	                "CODEPAGE = '65001'" +
	                ");" +
	                "INSERT INTO " + tableName + " (字母開頭, 字彙, 詞類, 中文, 級數) " +
	                "SELECT 字母開頭, 字彙, 詞類, 中文, 級數 " +
	                "FROM #TempGEPT;" +
	                "DROP TABLE #TempGEPT;";

	            System.out.println("Executing SQL for " + tableName);
	            System.out.println(sql);  // 打印 SQL 語句以進行調試

	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.executeUpdate();
	            System.out.println(tableName + " 匯入資料庫成功");
	        }
	    } catch (SQLException se) {
	        se.printStackTrace();
	    }
	}
	}
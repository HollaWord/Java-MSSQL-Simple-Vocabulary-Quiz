package function;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import conn.ConnectionUntil;

public class quizDAOimpl implements quizDAO {

	public Elementary selectById(String inputlevel, int inputId) {
		String sql = "SELECT * " + "  FROM [VocabularyDB].[dbo]." + inputlevel + "  WHERE [Id]=?";
		ConnectionUntil cu = new ConnectionUntil();
		try (Connection conn = cu.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, inputId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Elementary ele = new Elementary();
				ele.setCapital(rs.getString("字母開頭"));
				ele.setWord(rs.getString("字彙"));
				ele.setCategories(rs.getNString("詞類"));
				ele.setExplain(rs.getString("中文"));
				return ele;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public int getIdNum(String inputlevel) {

		String sql = "SELECT count([Id]) FROM [VocabularyDB].[dbo]." + "[" + inputlevel + "]";
		ConnectionUntil cu = new ConnectionUntil();
		int i = 0;
		try (Connection conn = cu.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				i = rs.getInt(1);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return i;
	}

	@Override
	public List<Integer> decideQNum(int levelNum, int totalQNum) {

		int min = 1; // 最小值
		int max = levelNum; // 該table最大值

		Set<Integer> generatedNumbers = new HashSet<>();
		while (generatedNumbers.size() < totalQNum) {
			int randomNum = ((int) (Math.random() * 1000) % (max + 1 - min)) + min;
			generatedNumbers.add(randomNum);
		}


		List<Integer> list = new ArrayList<>(generatedNumbers);
		//確認隨機抽樣數字
		System.out.println(list);
		return list;
	}


}

package function;

import java.util.List;

public interface quizDAO {

	//偵測table name並計算共多少題
	int getIdNum(String inputlevel);
	
	//選擇測驗題數 - 生成亂數set (單字總量,測驗題數)
	List<Integer> decideQNum(int levelNum, int totalQNum);
	
	//生成中文內容
	Elementary selectById(String inputlevel, int inputId);
	

}

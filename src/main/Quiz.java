package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTextPane;

import function.Elementary;
import function.quizDAO;
import function.quizDAOimpl;
import init.Initialize;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import conn.ConnectionUntil;

public class Quiz {

	private JFrame frame;
	JTextField textField = new JTextField();
	int maxId;
	Integer qNum;
	JLabel quizExplan1 = new JLabel();
	JLabel quizExplan2 = new JLabel();
	JLabel quizExplan2_1 = new JLabel();
	JLabel questionL = new JLabel();
	JLabel qNo = new JLabel();
	JButton btnNewButton_1;
	Elementary ele = new Elementary();
	JLabel quizExplan2_1_1 = new JLabel();
	int correctNum;
	boolean end;
	List<Integer> totalQNumArr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//先建立資料庫內容
		Initialize.initializeDatabase();
		Initialize.initializeTable();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Quiz window = new Quiz();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public Quiz() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("英文單字測試");
		frame.setBounds(100, 100, 446, 462);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("請選擇測試等級");
		lblNewLabel.setFont(new Font("新細明體", Font.PLAIN, 16));
		lblNewLabel.setBounds(32, 28, 125, 38);
		frame.getContentPane().add(lblNewLabel);

		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("新細明體", Font.PLAIN, 16));
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		// get default table
		ConnectionUntil cu = new ConnectionUntil();
		List<String> list = new ArrayList<String>();
		try (Connection conn = cu.getConnection()) {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getTables(null, "dbo", "%", new String[] { "TABLE" });

			int i = 0;
			while (rs.next()) {
				// Print table name
				String tableName = rs.getString("TABLE_NAME");
				if (!tableName.startsWith("trace_xe_")) { // Exclude system tables
					list.add(tableName);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		comboBox.setModel(new DefaultComboBoxModel(list.toArray()));
		comboBox.setBounds(150, 32, 140, 30);
		frame.getContentPane().add(comboBox);

		JButton btnNewButton = new JButton("開始");
		btnNewButton.setFont(new Font("新細明體", Font.PLAIN, 16));
		btnNewButton.setBounds(295, 32, 100, 30);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 內容清空
				btnNewButton.setText("重新開始");
				quizExplan2_1.setText("");
				correctNum = 0;
				qNum = 1;
				end = false;
				textField.setText("");
				totalQNumArr = null;

				int totalQNum = 20; // 測試多少單字

				// 取得SQLtableName並計算該級別共有多少單字
				String inputlevel = (String) comboBox.getSelectedItem();
				quizDAO vocaNum = new quizDAOimpl();
				Integer levelNum = vocaNum.getIdNum(inputlevel);

				quizExplan1.setFont(new Font("新細明體", Font.PLAIN, 16));
				quizExplan1.setBounds(32, 59, 345, 30);
				frame.getContentPane().add(quizExplan1);
				levelNum.toString();
				quizExplan1.setText(inputlevel + " 共" + levelNum + "個單字");
				quizDAO quiz01 = new quizDAOimpl();
				totalQNumArr = quiz01.decideQNum(levelNum, totalQNum);

				// Text - 測驗方式說明
				quizExplan2.setFont(new Font("新細明體", Font.PLAIN, 16));
				quizExplan2.setBounds(32, 86, 345, 30);
				frame.getContentPane().add(quizExplan2);
				quizExplan2.setText("本次測驗共 " + totalQNum + " 題，請在下方輸入對應的英文單字");

				// Text - 開始計算題數
				qNo.setFont(new Font("新細明體", Font.PLAIN, 16));
				qNo.setBounds(32, 111, 125, 30);
				frame.getContentPane().add(qNo);
				qNum = 1;
				qNo.setText("第 " + qNum.toString() + " 題");

				// 取得測驗題目
				quizDAO sbei = new quizDAOimpl();
				ele = sbei.selectById(inputlevel, totalQNumArr.get(qNum - 1));
				String question = "<html>中文意思：<br>" + ele.getCategories() + " " + ele.getExplain()
						+ "<br><br>請問對應的英文單字是?<br>提示為 " + ele.getCapital() + " 開頭</html>";
				questionL.setText(question);
				frame.getContentPane().add(questionL);
				questionL.setVerticalAlignment(SwingConstants.TOP);
				questionL.setFont(new Font("新細明體", Font.PLAIN, 16));
				questionL.setBounds(32, 139, 382, 150);
				questionL.setVerticalAlignment(SwingConstants.TOP);

				// Get Input Text
				textField.setBounds(32, 285, 153, 30);
				frame.getContentPane().add(textField);
				textField.setColumns(10);
				Font currentFont = textField.getFont();
				Font newFont = currentFont.deriveFont(16f); // 16f 是新的字體大小
				textField.setFont(newFont);
				textField.setText("");

				// 建立按鈕
				JButton btnNewButton_1 = new JButton("確定");
				btnNewButton_1.setBounds(195, 285, 91, 30);
				frame.getContentPane().add(btnNewButton_1);
				end = false; // 確認是否已做完題目

				// 執行回答判斷
				ActionListener actionListener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						qNo.setText("第 " + qNum.toString() + " 題");

						if (end == false) {
							if (btnNewButton_1.getText().equals("下一題")) { // 判斷是否有重新按過"開始"
								textField.setText("");
								btnNewButton_1.setText("確定");
								quizExplan2_1.setText("");
								quizExplan2_1.setVerticalAlignment(SwingConstants.TOP);
								ele = sbei.selectById(inputlevel, totalQNumArr.get(qNum - 1));
								String question = "<html>中文意思：<br>" + ele.getCategories() + " " + ele.getExplain()
										+ "<br><br>請問對應的英文單字是?<br>提示為 " + ele.getCapital() + " 開頭</html>";
								questionL.setText(question);

							} else {
								btnNewButton_1.setText("確定");
								quizExplan2_1.setText("");
								quizExplan2_1.setFont(new Font("新細明體", Font.PLAIN, 16));
								quizExplan2_1.setBounds(32, 318, 345, 102);
								quizExplan2_1.setVerticalAlignment(SwingConstants.TOP);
								frame.getContentPane().add(quizExplan2_1);

								if (textField.getText().equals(ele.getWord())) {
									quizExplan2_1.setText("<html>正確！<br>" + ele.statement() + "</html>");
									correctNum++;
								} else {
									quizExplan2_1.setText("<html>錯誤，正確答案為：<br>" + ele.statement() + "</html>");
								}

								if (qNum < totalQNum) {
									btnNewButton_1.setText("下一題");
									qNum++;
								} else {
									end = true;
									JOptionPane.showMessageDialog(null, "測驗結束，共答對" + correctNum + "題，正確率"
											+ ((double) correctNum / totalQNum) * 100 + "%，請按重新開始");
								}
							}
						} else {
							JOptionPane.showMessageDialog(null, "測驗結束，共答對" + correctNum + "題，正確率"
									+ ((double) correctNum / totalQNum) * 100 + "%，請按重新開始");
						}
						frame.revalidate();
						frame.repaint();
					}
				};

				textField.addActionListener(actionListener);
				btnNewButton_1.addActionListener(actionListener);

				frame.revalidate();
				frame.repaint();
			}

		}

		);

		// 示意位置，最後須全部刪除
//		quizExplan1.setFont(new Font("新細明體", Font.PLAIN, 14));
//		quizExplan1.setBounds(32, 59, 345, 30);
//		frame.getContentPane().add(quizExplan1);
//
//		quizExplan1.setText("共~~~~~~~~");
//
//
//		
//		
//		quizExplan2.setFont(new Font("新細明體", Font.PLAIN, 14));
//		quizExplan2.setBounds(32, 86, 345, 30);
//		frame.getContentPane().add(quizExplan2);
//		quizExplan2.setText("本次測驗共30題，請在下方輸入對應的英文單字");
//
//		qNo.setFont(new Font("新細明體", Font.PLAIN, 14));
//		qNo.setBounds(32, 111, 125, 30);
//		frame.getContentPane().add(qNo);
//		qNum = 1;
//		qNo.setText("第 " + qNum.toString() + " 題");

//		JLabel statement = new JLabel();
//		statement.setVerticalAlignment(SwingConstants.TOP);
//		statement.setText("中文意思");
//		statement.setFont(new Font("新細明體", Font.PLAIN, 14));
//		statement.setBounds(32, 139, 382, 110);
//		frame.getContentPane().add(statement);

//		textField = new JTextField();
//		textField.setBounds(32, 256, 153, 30);
//		frame.getContentPane().add(textField);
//		textField.setColumns(10);
//
//		JButton btnNewButton_1 = new JButton("確定");
//		btnNewButton_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
//		btnNewButton_1.setBounds(195, 255, 91, 30);
//		frame.getContentPane().add(btnNewButton_1);

//		JLabel quizExplan2_1 = new JLabel();
//		quizExplan2_1.setVerticalAlignment(SwingConstants.TOP);
//		quizExplan2_1.setText("答案");
//		quizExplan2_1.setFont(new Font("新細明體", Font.PLAIN, 14));
//		quizExplan2_1.setBounds(32, 291, 345, 102);
//		frame.getContentPane().add(quizExplan2_1);
//
//		JLabel quizExplan2_1_1 = new JLabel();
//		quizExplan2_1_1.setVerticalAlignment(SwingConstants.TOP);
//		quizExplan2_1_1.setText("結束");
//		quizExplan2_1_1.setFont(new Font("新細明體", Font.PLAIN, 14));
//		quizExplan2_1_1.setBounds(32, 365, 345, 50);
//		frame.getContentPane().add(quizExplan2_1_1);
//
	}
}

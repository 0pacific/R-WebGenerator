package view.tableList.fieldList;

import gui.*;
import gui.arrow.*;

import table.*;
import table.field.Field;
import tpp.*;
import view.tableList.fieldList.fieldEdit.FieldEditFrame;
import view.tableList.fieldList.fieldEdit.FieldEditPanel;
import view.transProcEdit.serviceArgsWindow.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import property.GeneratorProperty;

import debug.Debug;

import mainFrame.MainFrame;
import utility.*;

/*
 * SUMMARY :
 * フィールド一覧画面のパネル
 * 
 * NOTICE :
 * Singleton
 */
public class Panel_FieldList extends JPanel implements ActionListener,Serializable {
	private SuperTable table;

	public static String[] fieldTableColumnNames;
	private JTable fieldTable;

	private JButton fieldAddButton;

	public JButton fieldEditButton;

	public JButton btnFieldRemove;
	
	// 画面遷移ボタン
	public JButton btnTransTableList;
	public JButton btnTransRoleEdit;
	public JButton btnTransAuthEdit;
	public JButton btnTransTrans;
	
	// 背景色
	public static Color bgColor = Color.WHITE;

	// SpringLayout
	public SerializableSpringLayout springLayout; 
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static Panel_FieldList obj = new Panel_FieldList();
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private Panel_FieldList() {
		boolean japanese = GeneratorProperty.japanese();
		
		fieldTableColumnNames = japanese ?
									new String[]{"フィールド名", "データ型", "最小値", "最大値"} :
									new String[]{"Field Name", "Data Type", "Minimum", "Maximum"};
		
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Panel_FieldList.bgColor);

		// フィールド編集ボタン
		fieldEditButton = japanese ? new JButton("選択中のフィールドを編集") : new JButton("Edit Field");
		fieldEditButton.addActionListener(this);
		fieldEditButton.setActionCommand("フィールド編集");
		
		// 画面遷移ボタンの初期化・リスナー追加・コマンドセット
		btnTransTableList = japanese ? new JButton("テーブル一覧へ戻る") : new JButton("Go To Table List");
		btnTransRoleEdit = japanese ? new JButton("ロール定義画面へ") : new JButton("Go To Role Definition");
		btnTransAuthEdit = japanese ? new JButton("アクセス権限定義画面へ") : new JButton("Go To Accessibility Definition");
		btnTransTrans = japanese ? new JButton("Webページ定義画面へ") : new JButton("Go To Web Page Definition");
		btnTransTableList.addActionListener(this);
		btnTransRoleEdit.addActionListener(this);
		btnTransAuthEdit.addActionListener(this);
		btnTransTrans.addActionListener(this);
		btnTransTableList.setActionCommand("遷移 : テーブル一覧画面");
		btnTransRoleEdit.setActionCommand("遷移 : ロール編集画面");
		btnTransAuthEdit.setActionCommand("遷移 : 権限編集画面");
		btnTransTrans.setActionCommand("遷移 : ページ遷移定義画面");
	}

	
	
	
	/*
	 * PURPOSE :
	 * 指定テーブルを編集できるように、コンポーネントを配置する
	 */
	public void locateComps(SuperTable table) {
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		this.table = table;

		// テキストペイン　編集テーブル名
		JTextPane tpTblName = new JTextPane();
		if(table instanceof DataTable) {
			String text = japanese ? "データテーブル「" + this.table.getTableName() + "」を編集中です" : "Editing Data Table \""+this.table.getTableName()+"\".";
			tpTblName.setText(text);
		}
		else if(table instanceof AccountTable) {
			String text = japanese ? "アカウントテーブル「" + this.table.getTableName() + "」を編集中です" : "Editing Account Table \""+this.table.getTableName()+"\".";
			tpTblName.setText(text);
		}
		springLayout.putConstraint(SpringLayout.NORTH, tpTblName, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tpTblName, 20, SpringLayout.WEST, this);
		add(tpTblName);
		
		// 左上のフィールド一覧テーブルと、それを格納する JScrollPane
		fieldTable = new JTable();
		JScrollPane sp_fieldTable = new JScrollPane(fieldTable);
		int WIDTH = 600;
		int HEIGHT = 200;
		sp_fieldTable.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, sp_fieldTable, 20, SpringLayout.SOUTH, tpTblName);
		springLayout.putConstraint(SpringLayout.WEST, sp_fieldTable, 0, SpringLayout.WEST, tpTblName);
		add(sp_fieldTable);

	    // 「新たなフィールドを追加」ボタン
	    fieldAddButton = japanese ? new JButton("新たなフィールドを追加") : new JButton("Add Field");
		fieldAddButton.addActionListener(this);
		fieldAddButton.setActionCommand("フィールド追加");
		springLayout.putConstraint(SpringLayout.NORTH, fieldAddButton, 0, SpringLayout.NORTH, sp_fieldTable);
		springLayout.putConstraint(SpringLayout.WEST, fieldAddButton, 40, SpringLayout.EAST, sp_fieldTable);
	    add(fieldAddButton);
		
	    // 「選択中のフィールドを編集」ボタン
		springLayout.putConstraint(SpringLayout.NORTH, fieldEditButton, 20, SpringLayout.SOUTH, fieldAddButton);
		springLayout.putConstraint(SpringLayout.WEST, fieldEditButton, 0, SpringLayout.WEST, fieldAddButton);
	    add(fieldEditButton);

	    // 「選択中のフィールドを削除」ボタン
	    btnFieldRemove = japanese ? new JButton("選択中のフィールドを削除") : new JButton("Delete Field");
	    btnFieldRemove.addActionListener(this);
	    btnFieldRemove.setActionCommand("フィールド削除");
	    Slpc.put(springLayout, "N", btnFieldRemove, "S", fieldEditButton, 20);
	    Slpc.put(springLayout, "W", btnFieldRemove, "W", fieldEditButton, 0);
	    add(btnFieldRemove);

	    // フィールド一覧更新
		refreshFieldTable();

		
		// 画面遷移ボタン
		springLayout.putConstraint(SpringLayout.SOUTH, btnTransTableList, -20, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnTransTableList, 20, SpringLayout.WEST, this);
		add(btnTransTableList);

		springLayout.putConstraint(SpringLayout.NORTH, btnTransRoleEdit, 0, SpringLayout.NORTH, btnTransTableList);
		springLayout.putConstraint(SpringLayout.WEST, btnTransRoleEdit, 20, SpringLayout.EAST, btnTransTableList);
		add(btnTransRoleEdit);

		springLayout.putConstraint(SpringLayout.NORTH, btnTransAuthEdit, 0, SpringLayout.NORTH, btnTransRoleEdit);
		springLayout.putConstraint(SpringLayout.WEST, btnTransAuthEdit, 20, SpringLayout.EAST, btnTransRoleEdit);
		add(btnTransAuthEdit);

		springLayout.putConstraint(SpringLayout.NORTH, btnTransTrans, 0, SpringLayout.NORTH, btnTransAuthEdit);
		springLayout.putConstraint(SpringLayout.WEST, btnTransTrans, 20, SpringLayout.EAST, btnTransAuthEdit);
		add(btnTransTrans);
	}
	
	
	
	/*
	 * PURPOSE :
	 * フィールドテーブルの内容を更新する（フィールドの追加・削除時などに使う）
	 */
	public void refreshFieldTable() {
		boolean japanese = GeneratorProperty.japanese();
		
		// 値部分を新たに計算
		int fieldNum = table.getFieldNum();
		String[][] fieldTableValues = new String[fieldNum][Panel_FieldList.fieldTableColumnNames.length];
		for(int i=0; i<fieldNum; i++) {
			Field field = table.getField(i);
			
			// 項目「フィールド名」
			String fieldName = field.getFieldName();
			fieldTableValues[i][0] = fieldName;

			String CHAR = japanese?"字":" length";
			
			// 項目「データ型」
			// INT
			if(field.dataType.equals(Field.DATATYPE_INT)) {
				fieldTableValues[i][1] = japanese ? "整数" : "INTEGER";
				fieldTableValues[i][2] = Integer.toString(field.getMin());
				fieldTableValues[i][3] = Integer.toString(field.getMax());
			}
			// VARCHAR
			else if(field.dataType.equals(Field.DATATYPE_VARCHAR)) {
				fieldTableValues[i][1] = japanese ? "文字列" : "STRING";
				fieldTableValues[i][2] = Integer.toString(field.getMin()) + CHAR;
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + CHAR;
			}
			// DATETIME
			else if(field.dataType.equals(Field.DATATYPE_DATETIME)) {
				fieldTableValues[i][1] = japanese ? "年月日・時分秒" : "DATE&TIME";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// DATE
			else if(field.dataType.equals(Field.DATATYPE_DATE)) {
				fieldTableValues[i][1] = japanese ? "年月日" : "DATE";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// TIME
			else if(field.dataType.equals(Field.DATATYPE_TIME)) {
				fieldTableValues[i][1] = japanese ? "時分秒" : "TIME";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// FILE
			else if(field.dataType.equals(Field.DATATYPE_FILE)) {
				fieldTableValues[i][1] = japanese ? "ファイル" : "FILE";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + "KB";
			}
			// MAIL
			else if(field.dataType.equals(Field.DATATYPE_MAIL)) {
				fieldTableValues[i][1] = japanese ? "メールアドレス" : "MAIL";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// MAIL_AUTH
			else if(field.dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
				fieldTableValues[i][1] = japanese ? "メールアドレス（認証用）" : "MAIL(for identification)";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// USERID
			else if(field.dataType.equals(Field.DATATYPE_USERID)) {
				fieldTableValues[i][1] = japanese ? "ユーザID" : "USER ID";
				fieldTableValues[i][2] = Integer.toString(field.getMin()) + CHAR;
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + CHAR;
			}
			// PASSWORD
			else if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
				fieldTableValues[i][1] = japanese ? "パスワード" : "PASSWORD";
				fieldTableValues[i][2] = Integer.toString(field.getMin()) + CHAR;
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + CHAR;
			}
			// ROLE_NAME
			else if(field.dataType.equals(Field.DATATYPE_ROLE_NAME)) {
				fieldTableValues[i][1] = japanese ? "ロール名" : "ROLE NAME";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			else {
				Debug.error("想定外のデータ型です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
		
		// 新たなモデルをセット
		DefaultTableModel fieldTableModel
	    	= new DefaultTableModel(fieldTableValues, Panel_FieldList.fieldTableColumnNames);
		fieldTable.setModel(fieldTableModel);

		MainFrame.repaintAndValidate();
	}

	/*
	 * PURPOSE :
	 * ActionEvent ハンドラ
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		// 「新たなフィールドを追加」ボタンクリック
		if(cmd.equals("フィールド追加")) {
			FieldEditFrame frame = FieldEditFrame.getInstance();
			frame.setVisible(true);
			frame.setEnabled(true);
			FieldEditPanel.getInstance().openAddMode();

			MainFrame.getInstance().setEnabled(false);
		}
		else if(cmd.equals("フィールド編集")) {
			int selFieldIdx = fieldTable.getSelectedRow();
			Field selField = table.getField(selFieldIdx);

			FieldEditFrame frame = FieldEditFrame.getInstance();
			frame.setVisible(true);
			frame.setEnabled(true);
			FieldEditPanel.getInstance().openEditMode(selField);

			MainFrame.getInstance().setEnabled(false);
		}
		else if(cmd.equals("フィールド削除")) {
			int selectedFieldIdx = fieldTable.getSelectedRow();
			Field selectedField = table.getField(selectedFieldIdx);

			// ユーザID、認証メールアドレス、パスワードのフィールドは削除できない
			if(selectedField.dataType.equals(Field.DATATYPE_USERID)) {
				String msg = japanese ?
								"ユーザIDのフィールドは削除できません\n（メールアドレスに切り替えることはできます）" :
								"Can't delete USER ID field.\n(You can change its data type to MAIL for identification)";
				JOptionPane.showMessageDialog(this, msg);
				return;
			}
			else if(selectedField.dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
				String msg = japanese ?
						"認証用メールアドレスのフィールドは削除できません\n（ユーザIDに切り替えることはできます）" :
						"Can't delete MAIL field for identification.\n(You can change its data type to USER ID)";
				JOptionPane.showMessageDialog(this, msg);
				return;
			}
			else if(selectedField.dataType.equals(Field.DATATYPE_PASSWORD)) {
				String msg = japanese ?
						"パスワードのフィールドは削除できません" :
						"Can't delete PASSWORD field";
				JOptionPane.showMessageDialog(this, msg);
				return;
			}

			String msg1 = japanese ? "削除してもよろしいですか？" : "Delete selecting field?";
			int confirm = JOptionPane.showConfirmDialog(this, msg1, "フィールドの削除", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// キャンセル
				return;
			}
			
			this.table.removeField(selectedField);
			this.refreshFieldTable();
		}
		else if(cmd.equals("遷移 : テーブル一覧画面")) {
			MainFrame.getInstance().shiftToTableList();
		}
		else if(cmd.equals("遷移 : ロール編集画面")) {
			MainFrame.getInstance().shiftToRoleEdit();
		}
		else if(cmd.equals("遷移 : 権限編集画面")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		else if(cmd.equals("遷移 : ページ遷移定義画面")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
	}
	
	public SuperTable getTable() {
		return table;
	}

	public void setTable(SuperTable table) {
		this.table = table;
	}
	
	public static Panel_FieldList getInstance() {
		return Panel_FieldList.obj;
	}
	
	
	public static void updateInstance(Panel_FieldList newObject) {
		Panel_FieldList.obj = newObject;
	}

}
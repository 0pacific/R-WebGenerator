package view.tableList.fieldList.fieldEdit;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import table.*;
import table.field.Field;
import utility.Slpc;
import view.tableList.fieldList.Panel_FieldList;
import utility.*;

/*
 * Singleton
 */
public class FieldEditPanel extends JPanel implements ItemListener,ActionListener,Serializable {
	// 追加モード・編集モード
	public int modeAddEdit;
	public static final int MODE_ADD = 0;
	public static final int MODE_EDIT = 1;
	
	// 編集中のフィールド（編集モードでのみ使用される）
	public Field editingField;
	
	// テキストペイン「フィールド名：」
	JTextPane textPaneFieldName;
	// テキストフィールド（フィールド名入力）
	JTextField textFieldFieldName;
	
	// テキストペイン「データのタイプ：」
	public JTextPane textPaneDataTypeSelect;
	// コンボボックス（データタイプ選択）
	public JComboBox comboBoxDataTypeSelect;
	public String[] valuesComboBoxDataTypeSelect;
	public static String[] DATA_TYPE_LIST_1;		// データテーブルのフィールド
	public static String[] DATA_TYPE_LIST_2;		// アカウントテーブルの認証フィールド
	public static String[] DATA_TYPE_LIST_3;		// アカウントテーブルのパスワードフィールド
	public static String[] DATA_TYPE_LIST_4;		// アカウントテーブルのその他のフィールド
	
	// 最小値
	public JTextPane tpMin = new JTextPane();
	public JTextField tfMin = new JTextField();

	// 最大値
	public JTextPane tpMax = new JTextPane();
	public JTextField tfMax = new JTextField();

	// ファイル最大KB
	public JTextPane textPaneFileSizeMaxKb = new JTextPane();
	public JTextField textFieldFileSizeMaxKb = new JTextField();
	
	// 追加実行ボタン
	public JButton btnAdd;

	// 編集実行ボタン
	public JButton btnEdit;

	// キャンセルボタン
	public JButton btnCancel;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	// 背景色
	public static final Color BG_COLOR = Color.WHITE;

	// 今、どのデータタイプの追加（または編集）フォームを表示しているか（Fieldクラスのstatic定数を代入）
	public String currentDataType;
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static FieldEditPanel obj = new FieldEditPanel();

	
	
	
	
	
	private FieldEditPanel() {
		boolean japanese = GeneratorProperty.japanese();
		
		FieldEditPanel.DATA_TYPE_LIST_1 = new String[]{
			Field.getDataTypeExpression(Field.DATATYPE_INT),
			Field.getDataTypeExpression(Field.DATATYPE_VARCHAR),
			Field.getDataTypeExpression(Field.DATATYPE_TEXT),
			Field.getDataTypeExpression(Field.DATATYPE_DATETIME),
			Field.getDataTypeExpression(Field.DATATYPE_DATE),
			Field.getDataTypeExpression(Field.DATATYPE_TIME),
			Field.getDataTypeExpression(Field.DATATYPE_FILE),
			Field.getDataTypeExpression(Field.DATATYPE_MAIL)
		};
		FieldEditPanel.DATA_TYPE_LIST_2 = new String[]{
			Field.getDataTypeExpression(Field.DATATYPE_USERID),
			Field.getDataTypeExpression(Field.DATATYPE_MAIL_AUTH)
		};
		FieldEditPanel.DATA_TYPE_LIST_3 = new String[]{
			Field.getDataTypeExpression(Field.DATATYPE_PASSWORD)
		};
		FieldEditPanel.DATA_TYPE_LIST_1 = new String[]{
				Field.getDataTypeExpression(Field.DATATYPE_INT),
				Field.getDataTypeExpression(Field.DATATYPE_VARCHAR),
				Field.getDataTypeExpression(Field.DATATYPE_TEXT),
				Field.getDataTypeExpression(Field.DATATYPE_DATETIME),
				Field.getDataTypeExpression(Field.DATATYPE_DATE),
				Field.getDataTypeExpression(Field.DATATYPE_TIME),
				Field.getDataTypeExpression(Field.DATATYPE_FILE),
				Field.getDataTypeExpression(Field.DATATYPE_MAIL),
				Field.getDataTypeExpression(Field.DATATYPE_ROLE_NAME)
		};
		
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		int PADD_LEFT = 20;

		// テキストペイン　「フィールド名：」
		textPaneFieldName = new JTextPane();
		textPaneFieldName.setText(japanese ? "フィールド名：" : "Field Name:");
		textPaneFieldName.setEditable(false);

		// テキストフィールド　フィールド名
		textFieldFieldName = new JTextField();

		// データタイプ選択
		textPaneDataTypeSelect = new JTextPane();
		textPaneDataTypeSelect.setText(japanese ? "データのタイプ：" : "Data Type :");
		textPaneDataTypeSelect.setEditable(false);
		comboBoxDataTypeSelect = new JComboBox();
		comboBoxDataTypeSelect.addItemListener(this);
		
		btnAdd = new JButton(japanese ? "フィールドを追加" : "Add Field");
		btnAdd.addActionListener(this);
		btnAdd.setActionCommand("フィールド追加");

		btnEdit = new JButton(japanese ? "以上の内容で編集" : "   OK   ");
		btnEdit.addActionListener(this);
		btnEdit.setActionCommand("フィールド編集");

		btnCancel = new JButton(japanese ? "キャンセル" : "Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("キャンセル");
	}

	
	
	public void locateFieldNameComps(int PADD_LEFT) {
		int PADD_TOP = 20;
		springLayout.putConstraint(SpringLayout.NORTH, textPaneFieldName, PADD_TOP, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, textPaneFieldName, PADD_LEFT, SpringLayout.WEST, this);
		add(textPaneFieldName);

		int WIDTH = 100;
		textFieldFieldName.setPreferredSize(new Dimension(WIDTH, 20));
		springLayout.putConstraint(SpringLayout.NORTH, textFieldFieldName, 0, SpringLayout.SOUTH, textPaneFieldName);
		springLayout.putConstraint(SpringLayout.WEST, textFieldFieldName, PADD_LEFT, SpringLayout.WEST, this);
		add(textFieldFieldName);
	}
	
	
	public void removeAllComps() {
		removeAll();
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * フィールド一覧画面で、フィールド追加ボタンによりフィールド編集フレームを起動したときに実行される
	 */
	public void openAddMode() {
		removeAllComps();
		
		modeAddEdit = FieldEditPanel.MODE_ADD;
		
		// フィールド名のテキストペイン・テキストフィールド
		locateFieldNameComps(20);

		textFieldFieldName.setText("");
		
		// 編集中のテーブルがデータテーブルかアカウントテーブルかに応じ、データタイプのメニューを決定
		SuperTable table = Panel_FieldList.getInstance().getTable();
		String tableClassName = table.getClass().getSimpleName();
		if(table instanceof DataTable) {
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_1;
		}
		else if(table instanceof AccountTable) {
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_4;
		}
		else {
			Debug.error("エラー", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		// データ型選択コンボボックス配置、INT型を選択した状態にする
		locateDataTypeComps(true);
		locateCompsInt(null);

		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	public void openEditMode(Field field) {
		// 編集モードに移行
		modeAddEdit = FieldEditPanel.MODE_EDIT;

		this.editingField = field;
		
		// 全コンポーネント除去
		removeAllComps();
		
		
		// フィールド名のテキストペイン・テキストフィールド
		locateFieldNameComps(20);
		textFieldFieldName.setText(field.getFieldName());
		

		// 編集中のテーブルがデータテーブルかアカウントテーブルかによって、コンボボックスのメニューを決定
		SuperTable table = Panel_FieldList.getInstance().getTable();
		String tableClassName = table.getClass().getSimpleName();
		String dataType = field.dataType;
		boolean dataTypeG1 = 	tableClassName.equals("DataTable") && (
								dataType.equals(Field.DATATYPE_INT)
								|| dataType.equals(Field.DATATYPE_VARCHAR)
								|| dataType.equals(Field.DATATYPE_DATETIME)
								|| dataType.equals(Field.DATATYPE_DATE)
								|| dataType.equals(Field.DATATYPE_TIME)
								|| dataType.equals(Field.DATATYPE_MAIL)
								|| dataType.equals(Field.DATATYPE_FILE)
								);
		boolean dataTypeG2 = 	tableClassName.equals("AccountTable") && (
								dataType.equals(Field.DATATYPE_MAIL_AUTH)
								|| dataType.equals(Field.DATATYPE_USERID)
								);
		boolean dataTypeG3 = 	tableClassName.equals("AccountTable") && (
								dataType.equals(Field.DATATYPE_PASSWORD)
								);
		boolean dataTypeG4 = 	tableClassName.equals("AccountTable") && (
								dataType.equals(Field.DATATYPE_INT)
								|| dataType.equals(Field.DATATYPE_VARCHAR)
								|| dataType.equals(Field.DATATYPE_DATETIME)
								|| dataType.equals(Field.DATATYPE_DATE)
								|| dataType.equals(Field.DATATYPE_FILE)
								|| dataType.equals(Field.DATATYPE_TIME)
								|| dataType.equals(Field.DATATYPE_MAIL)
								);

		if(dataTypeG1) {	// データテーブルのフィールドを編集
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_1;
		}
		else if(dataTypeG2) {	// ユーザID or 認証メールアドレス　のフィールドを編集
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_2;
		}
		else if(dataTypeG3) {	// パスワードフィールドを編集
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_3;
		}
		else if(dataTypeG4) {	// アカウントテーブルのその他のフィールドを編集
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_4;
		}
		locateDataTypeComboOnEditStart(field);

		
		// 編集しようとしているフィールドの、現在のデータ型に対応したフォームを表示
		if(dataType.equals(Field.DATATYPE_INT)) {
			locateCompsInt(field);
		}
		else if(dataType.equals(Field.DATATYPE_VARCHAR)) {
			locateCompsVarchar(field);
		}
		else if(dataType.equals(Field.DATATYPE_TEXT)) {
			locateCompsText(field);
		}
		else if(dataType.equals(Field.DATATYPE_DATETIME)) {
			locateCompsDatetime(field);
		}
		else if(dataType.equals(Field.DATATYPE_DATE)) {
			locateCompsDate(field);
		}
		else if(dataType.equals(Field.DATATYPE_TIME)) {
			locateCompsTime(field);
		}
		else if(dataType.equals(Field.DATATYPE_FILE)) {
			locateCompsFile(field);
		}
		else if(dataType.equals(Field.DATATYPE_MAIL)) {
			locateCompsMail(field);
		}
		else if(dataType.equals(Field.DATATYPE_USERID)) {
			locateCompsUserid(field);
		}
		else if(dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
			locateCompsMailAuth(field);
		}
		else if(dataType.equals(Field.DATATYPE_PASSWORD)) {
			locateCompsPassword(field);
		}
		else if(dataType.equals(Field.DATATYPE_ROLE_NAME)) {
			locateCompsRoleName(field);
		}
		else {
			Debug.error("編集しようとしているフィールドのデータ型が想定外です。");
		}
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	public void locateCompsInt(Field intField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_INT;
		
		if(intField!=null && intField.dataType!=Field.DATATYPE_INT) {
			Debug.error("FieldEditPanel locateCompsInt() : INT型のフィールドではありません");
		}
		
		springLayout.putConstraint(SpringLayout.NORTH, tpMin, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tpMin, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tpMin);
		tpMin.setText(japanese ? "最小値：" : "Minimum:");
		
		if(intField==null) {
			int MIN = 0;
			tfMin.setText(Integer.toString(MIN));
		}
		else {
			int MIN = intField.getMin();
			tfMin.setText(Integer.toString(MIN));
		}
		int WIDTH = 100;
		tfMin.setPreferredSize(new Dimension(WIDTH, 20));
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		add(tfMin);
		
		JTextPane tpMax = new JTextPane();
		tpMax.setText(japanese ? "最大値：" : "Maximum:");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		tfMax = new JTextField();
		if(intField==null) {
			int MAX = 65535;
			tfMax.setText(Integer.toString(MAX));
		}
		else {
			int MAX = intField.getMax();
			tfMax.setText(Integer.toString(MAX));
		}
		WIDTH = 100;
		tfMax.setPreferredSize(new Dimension(WIDTH, 20));
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		add(tfMax);

		locateButtons(tfMax);
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	
	public void locateCompsVarchar(Field varcharField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_VARCHAR;
		
		
		// テキストペイン
		tpMin.setText(japanese ? "最小文字数：" : "Minimum Length:");
		springLayout.putConstraint(SpringLayout.NORTH, tpMin, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tpMin, 20, SpringLayout.WEST, this);
		add(tpMin);
		
		
		// テキストフィールド（最小値入力）
		if(varcharField==null)
			tfMin.setText(Integer.toString(0));
		else
			tfMin.setText(Integer.toString(varcharField.getMin()));
		int WIDTH = 100;
		int HEIGHT = 20;
		tfMin.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		add(tfMin);
		
		
		// テキストペイン
		tpMax.setText(japanese ? "最大文字数：" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		
		// テキストフィールド（最大値入力）
		if(varcharField==null)
			tfMax.setText(Integer.toString(255));
		else
			tfMax.setText(Integer.toString(varcharField.getMax()));
		tfMax.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		add(tfMax);


		// 追加（or編集）ボタンとキャンセルボタン配置
		locateButtons(tfMax);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void locateCompsText(Field textField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_VARCHAR;
		
		
		// テキストペイン
		tpMin.setText(japanese ? "最小文字数：" : "Minimum Length:");
		springLayout.putConstraint(SpringLayout.NORTH, tpMin, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tpMin, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tpMin);
		
		
		// テキストフィールド（最小値入力）
		if(textField==null)
			tfMin.setText(Integer.toString(0));
		else
			tfMin.setText(Integer.toString(textField.getMin()));
		int WIDTH = 100;
		int HEIGHT = 20;
		tfMin.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		add(tfMin);
		
		
		// テキストペイン
		tpMax.setText(japanese ? "最大文字数：" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		
		// テキストフィールド（最大値入力）
		if(textField==null)
			tfMax.setText(Integer.toString(65535));
		else
			tfMax.setText(Integer.toString(textField.getMax()));
		tfMax.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		add(tfMax);


		// 追加（or編集）ボタンとキャンセルボタン配置
		locateButtons(tfMax);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	public void locateCompsDatetime(Field datetimeField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_DATETIME;

		
		// テキストペイン
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "このフィールドには日付と時刻のセットを格納します。" : "Date and time will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		
		// 追加（or編集）ボタンとキャンセルボタン配置
		locateButtons(tp);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	public void locateCompsDate(Field dateField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_DATE;

		
		// テキストペイン
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "このフィールドには日付を格納します。" : "Date will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		
		// 追加（or編集）ボタンとキャンセルボタン配置
		locateButtons(tp);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	public void locateCompsTime(Field timeField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_TIME;

		
		// テキストペイン
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "このフィールドには時刻を格納します。" : "Time will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		
		// 追加（or編集）ボタンとキャンセルボタン配置
		locateButtons(tp);
		
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	public void locateCompsMail(Field mailField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_MAIL;
		
		if(mailField!=null && mailField.dataType!=Field.DATATYPE_MAIL) {
			Debug.error("FieldEditPanel locateCompsMail() : MAIL型のフィールドではありません");
		}
		
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "このフィールドにはメールアドレスを格納します。" : "Mail address will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		locateButtons(tp);
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	public void locateCompsFile(Field fileField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_FILE;

		if(fileField!=null && !fileField.dataType.equals(Field.DATATYPE_FILE)) {
			Debug.error("FILE型フィールドを編集しようとしているはずですが、対象のフィールドはFILE型ではありません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}

		// テキストペイン初期化
		textPaneFileSizeMaxKb = new JTextPane();
		textPaneFileSizeMaxKb.setText(japanese ? "ファイルサイズ上限（KB）：" : "Maximum File Size(KB):");
		textPaneFileSizeMaxKb.setEditable(false);
		// 各コンポーネント配置
		springLayout.putConstraint(SpringLayout.NORTH, textPaneFileSizeMaxKb, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, textPaneFileSizeMaxKb, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(textPaneFileSizeMaxKb);

		// テキストフィールド（ファイルサイズ上限入力）初期化
		textFieldFileSizeMaxKb = new JTextField();
		if(fileField==null) {	// 追加時
			textFieldFileSizeMaxKb.setText("1000");
		}
		else if(fileField.dataType==Field.DATATYPE_FILE) {	// 既存のFILE型フィールド編集時
			Debug.out("既存のFILE型フィールドを編集しようとしているので、サイズのテキストフィールドの値を"+fileField.max+"に設定します", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			textFieldFileSizeMaxKb.setText(Integer.toString(fileField.max));
		}
		textFieldFileSizeMaxKb.setPreferredSize(new Dimension(100, 20));
		springLayout.putConstraint(SpringLayout.NORTH, textFieldFileSizeMaxKb, 20, SpringLayout.SOUTH, textPaneFileSizeMaxKb);
		springLayout.putConstraint(SpringLayout.WEST, textFieldFileSizeMaxKb, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(textFieldFileSizeMaxKb);

		locateButtons(textFieldFileSizeMaxKb);

		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void locateCompsUserid(Field useridField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_USERID;
		
		
		// テキストペイン（最小文字数）除去・再配置
		tpMin.setText(japanese ? "最小文字数：" : "Minimum Length:");
		Slpc.put(springLayout, "N", tpMin, "S", comboBoxDataTypeSelect, 20);
		Slpc.put(springLayout, "W", tpMin, "W", comboBoxDataTypeSelect, 0);
		add(tpMin);
		
		// テキストフィールド（最小文字数）除去・再配置・値設定
		int MIN = -1;
		if(useridField==null) {	// 追加時の場合、および編集モードでデータタイプ選択コンボボックスの値を"ユーザID"にした場合
			MIN = 4;
		}
		else {	// 既存のUSERID型フィールドを編集を開始したところである場合
			MIN = useridField.getMin();
		}
		tfMin.setText(Integer.toString(MIN));
		int WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		springLayout.putConstraint(SpringLayout.EAST, tfMin, WIDTH, SpringLayout.WEST, tfMin);
		add(tfMin);
		
		// テキストペイン（最大文字数）除去・再配置
		tpMax.setText(japanese ? "最大文字数：" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		// テキストフィールド（最大文字数）除去・再配置・値設定（USERID型の場合は編集モードしかありえないはずなので、userifFieldにmaxが設定されているという前提
		int MAX = -1;
		if(useridField==null) {	// 追加時の場合、および編集モードでデータタイプ選択コンボボックスの値を"ユーザID"にした場合
			MAX = 16;
		}
		else {	// 既存のUSERID型フィールドを編集を開始したところである場合
			MAX = useridField.getMax();
		}
		tfMax.setText(Integer.toString(MAX));
		WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		springLayout.putConstraint(SpringLayout.EAST, tfMax, WIDTH, SpringLayout.WEST, tfMax);
		add(tfMax);


		locateButtons(tfMax);
		
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	public void locateCompsMailAuth(Field mailAuthField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_MAIL_AUTH;
		
		if(mailAuthField!=null && !mailAuthField.dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
			Debug.error("MAIL_AUTH型フィールドを編集しようとしているはずですが、対象のフィールドはMAIL_AUTH型ではありません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "認証用メールアドレスを格納するフィールドに変更します。" : "Data type will be changed to MAIL for identification");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.EAST, tp, -20, SpringLayout.EAST, this);
		add(tp);

		
		locateButtons(tp);

		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	public void locateCompsPassword(Field passwordField) {
		boolean japanese = GeneratorProperty.japanese();
		currentDataType = Field.DATATYPE_PASSWORD;
		
		if(passwordField!=null && !passwordField.dataType.equals(Field.DATATYPE_PASSWORD)) {
			Debug.error("PASSWORD型のフィールドではありません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		
		// テキストペイン（最小文字数）除去・再配置
		tpMin.setText(japanese ? "最小文字数：" : "Minimum Length");
		Slpc.put(springLayout, "N", tpMin, "S", comboBoxDataTypeSelect, 20);
		Slpc.put(springLayout, "W", tpMin, "W", comboBoxDataTypeSelect, 0);
		add(tpMin);
		
		// テキストフィールド（最小文字数）除去・再配置・値設定
		int MIN = -1;
		if(passwordField==null) {	// 追加時の場合、および編集モードでデータタイプ選択コンボボックスの値を"ユーザID"にした場合
			MIN = 4;
		}
		else {	// 既存のPASSWORD型フィールドを編集を開始したところである場合
			MIN = passwordField.getMin();
		}
		tfMin.setText(Integer.toString(MIN));
		int WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		springLayout.putConstraint(SpringLayout.EAST, tfMin, WIDTH, SpringLayout.WEST, tfMin);
		add(tfMin);
		
		// テキストペイン（最大文字数）除去・再配置
		tpMax.setText(japanese ? "最大文字数：" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		// テキストフィールド（最大文字数）除去・再配置・値設定（PASSWORD型の場合は編集モードしかありえないはずなので、userifFieldにmaxが設定されているという前提
		int MAX = -1;
		if(passwordField==null) {	// 追加時の場合、および編集モードでデータタイプ選択コンボボックスの値を"ユーザID"にした場合
			MAX = 16;
		}
		else {	// 既存のPASSWORD型フィールドを編集を開始したところである場合
			MAX = passwordField.getMax();
		}
		tfMax.setText(Integer.toString(MAX));
		WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		springLayout.putConstraint(SpringLayout.EAST, tfMax, WIDTH, SpringLayout.WEST, tfMax);
		add(tfMax);

		
		locateButtons(tfMax);

		
		FieldEditFrame.repaintAndValidate();
	}

	
	public void locateCompsRoleName(Field roleNameField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_ROLE_NAME;
		

		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "このフィールドにはアカウント所持者のロール名を格納します。" : "Name of account owner's role will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		locateButtons(tp);
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * データ型を選択するコンボボックスを配置
	 */
	public void locateDataTypeComps(boolean frameFirstOpen) {
		// エラーチェック
		boolean check = (valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_1
							|| valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_2
							|| valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_3
							|| valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_4);
		if(!check) {
			Debug.error("FieldEditPanel locateDataTypeComps() : コンボボックスのモードが不当です");
		}

		
		// テキストペイン
		springLayout.putConstraint(SpringLayout.NORTH, textPaneDataTypeSelect, 20, SpringLayout.SOUTH, textFieldFieldName);
		springLayout.putConstraint(SpringLayout.WEST, textPaneDataTypeSelect, 0, SpringLayout.WEST, textFieldFieldName);
		add(textPaneDataTypeSelect);

		
		// コンボボックス　データ型
		if(frameFirstOpen) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(valuesComboBoxDataTypeSelect);
			comboBoxDataTypeSelect.setModel(model);
		}
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxDataTypeSelect, 0, SpringLayout.SOUTH, textPaneDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxDataTypeSelect, 0, SpringLayout.WEST, textPaneDataTypeSelect);
		add(comboBoxDataTypeSelect);

		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * フィールドの編集を開始する際に、データタイプ選択コンボボックスを配置する
	 * 開始時のフィールドのデータタイプに応じて、適切なものが既に選択された状態で配置される
	 */
	public void locateDataTypeComboOnEditStart(Field field) {
		boolean japanese = GeneratorProperty.japanese();
		locateDataTypeComps(true);
		
		// 編集しようとしているフィールドの現在のデータタイプに応じて、適切なものを選択状態にする
		String dataType = field.dataType;
		comboBoxDataTypeSelect.setSelectedItem(Field.getDataTypeExpression(dataType));
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * 追加実行ボタン（or編集実行ボタン）とキャンセルボタンを配置する
	 */
	public void locateButtons(Component balometerComp) {
		// 追加モード
		if(modeAddEdit==FieldEditPanel.MODE_ADD) {
			springLayout.putConstraint(SpringLayout.NORTH, btnAdd, 20, SpringLayout.SOUTH, balometerComp);
			springLayout.putConstraint(SpringLayout.WEST, btnAdd, 20, SpringLayout.WEST, this);
			add(btnAdd);

			// キャンセルボタン
			springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, btnAdd);
			springLayout.putConstraint(SpringLayout.WEST, btnCancel, 40, SpringLayout.EAST, btnAdd);
			add(btnCancel);
		}
		// 編集モード
		else {
			springLayout.putConstraint(SpringLayout.NORTH, btnEdit, 20, SpringLayout.SOUTH, balometerComp);
			springLayout.putConstraint(SpringLayout.WEST, btnEdit, 20, SpringLayout.WEST, this);
			add(btnEdit);

			// キャンセルボタン
			springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, btnEdit);
			springLayout.putConstraint(SpringLayout.WEST, btnCancel, 40, SpringLayout.EAST, btnEdit);
			add(btnCancel);
		}

		FieldEditFrame.repaintAndValidate();
	}


	


	public void locate_nameComps_dataTypeComps(boolean frameFirstOpen) {
		removeAllComps();
		
		locateFieldNameComps(20);
		
		locateDataTypeComps(frameFirstOpen);
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ItemEventリスナー
	 */
	public void itemStateChanged(ItemEvent e) {
		Debug.notice("コンボボックスの値が変更されました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		if(e.getSource()!=comboBoxDataTypeSelect) {
			Debug.error("データ型を選択するコンボボックスに変更が生じたと思ったら、別のコンポーネントがイベント発生源みたいです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		// フレームオープン時ではないのでfalseを渡す
		locate_nameComps_dataTypeComps(false);
		
		
		String selType = (String)comboBoxDataTypeSelect.getSelectedItem();
		if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_INT))) {
			locateCompsInt(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_VARCHAR))) {
			locateCompsVarchar(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_TEXT))) {
			locateCompsText(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_DATETIME))) {
			locateCompsDatetime(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_DATE))) {
			locateCompsDate(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_TIME))) {
			locateCompsTime(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_FILE))) {
			locateCompsFile(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_MAIL))) {
			locateCompsMail(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_USERID))) {
			locateCompsUserid(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_MAIL_AUTH))) {
			locateCompsMailAuth(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_ROLE_NAME))) {
			locateCompsRoleName(null);
		}
		else {
			Debug.error("コンボボックスで想定外のデータタイプが選択されました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();
		
		if(cmd.equals("フィールド追加")) {
			Field newField = null;

			// 入力されたフィールド名取得
			String name = textFieldFieldName.getText();
			if(name.equals("")) {
				JOptionPane.showMessageDialog(this, japanese?"フィールド名が入力されていません":"Field name is blank.", japanese?"警告":"Warning", JOptionPane.OK_OPTION);
				return;
			}

			
			if(currentDataType.equals(Field.DATATYPE_INT)) {
				int min = Integer.parseInt(tfMin.getText());
				int max = Integer.parseInt(tfMax.getText());
				newField = new Field(name, Field.DATATYPE_INT, min, max);
			}
			else if(currentDataType.equals(Field.DATATYPE_VARCHAR)) {
				int min = Integer.parseInt(tfMin.getText());
				int max = Integer.parseInt(tfMax.getText());
				newField = new Field(name, Field.DATATYPE_VARCHAR, min, max);
			}
			else if(currentDataType.equals(Field.DATATYPE_TEXT)) {
				int min = Integer.parseInt(tfMin.getText());
				int max = Integer.parseInt(tfMax.getText());
				newField = new Field(name, Field.DATATYPE_TEXT, min, max);
			}
			else if(currentDataType.equals(Field.DATATYPE_DATETIME)) {
				newField = new Field(name, Field.DATATYPE_DATETIME);
			}
			else if(currentDataType.equals(Field.DATATYPE_DATE)) {
				newField = new Field(name, Field.DATATYPE_DATE);
			}
			else if(currentDataType.equals(Field.DATATYPE_TIME)) {
				newField = new Field(name, Field.DATATYPE_TIME);
			}
			else if(currentDataType.equals(Field.DATATYPE_MAIL)) {
				newField = new Field(name, Field.DATATYPE_MAIL, 0, 0);
			}
			else if(currentDataType.equals(Field.DATATYPE_FILE)) {
				int fileSizeMaxKb = Integer.parseInt(this.textFieldFileSizeMaxKb.getText());
				newField = new Field(name, Field.DATATYPE_FILE, 0, fileSizeMaxKb);
			}
			else if(currentDataType.equals(Field.DATATYPE_ROLE_NAME)) {
				newField = new Field(name, Field.DATATYPE_ROLE_NAME);
			}
			else {
				Debug.error("フィールドの追加を実行しようとしましたが、想定外のデータ型です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			SuperTable table = Panel_FieldList.getInstance().getTable();
			table.addField(newField);
		}
		else if(cmd.equals("フィールド編集")) {
			// 入力されたフィールド名を新たなフィールド名に設定
			String newName = textFieldFieldName.getText();
			if(newName.equals("")) {
				JOptionPane.showMessageDialog(this, japanese?"フィールド名が入力されていません":"Field name is blank.", japanese?"警告":"Warning", JOptionPane.OK_OPTION);
				return;
			}
			editingField.name = newName;
			

			// INT
			if(currentDataType.equals(Field.DATATYPE_INT)) {
				editingField.dataType = Field.DATATYPE_INT;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// VARCHAR
			else if(currentDataType.equals(Field.DATATYPE_VARCHAR)) {
				editingField.dataType = Field.DATATYPE_VARCHAR;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// TEXT
			else if(currentDataType.equals(Field.DATATYPE_TEXT)) {
				editingField.dataType = Field.DATATYPE_TEXT;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// DATETIME
			else if(currentDataType.equals(Field.DATATYPE_DATETIME)) {
				editingField.dataType = Field.DATATYPE_DATETIME;
			}
			// DATE
			else if(currentDataType.equals(Field.DATATYPE_DATE)) {
				editingField.dataType = Field.DATATYPE_DATE;
			}
			// TIME
			else if(currentDataType.equals(Field.DATATYPE_TIME)) {
				editingField.dataType = Field.DATATYPE_TIME;
			}
			// FILE
			else if(currentDataType.equals(Field.DATATYPE_FILE)) {
				editingField.dataType = Field.DATATYPE_FILE;
				editingField.max = Integer.parseInt(this.textFieldFileSizeMaxKb.getText());
			}
			// MAIL
			else if(currentDataType.equals(Field.DATATYPE_MAIL)) {
				editingField.dataType = Field.DATATYPE_MAIL;
			}
			// USERID
			else if(currentDataType.equals(Field.DATATYPE_USERID)) {
				editingField.dataType = Field.DATATYPE_USERID;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// MAIL_AUTH
			else if(currentDataType.equals(Field.DATATYPE_MAIL_AUTH)) {
				editingField.dataType = Field.DATATYPE_MAIL_AUTH;
			}
			// PASSWORD
			else if(currentDataType.equals(Field.DATATYPE_PASSWORD)) {
				editingField.dataType = Field.DATATYPE_PASSWORD;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// ROLE_NAME
			else if(currentDataType.equals(Field.DATATYPE_ROLE_NAME)) {
				editingField.dataType = Field.DATATYPE_ROLE_NAME;
			}
			else {
				Debug.error("フィールドの編集を実行しようとしましたが、想定外のデータ型です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
		else if(cmd.equals("キャンセル")) {
			// 何もしない
		}

		frameClosed();
	}

	
	
	
	// フレームが閉じるときの処理
	public void frameClosed() {
		FieldEditFrame.repaintAndValidate();

		// フィールド一覧画面のフィールド一覧を更新
		Panel_FieldList.getInstance().refreshFieldTable();

		// フィールド編集フレームを消し、ジェネレータフレームを使用可能に
		FieldEditFrame.getInstance().setVisible(false);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().requestFocus();
	}
	
	
	
	
	
	public void paintComponent(Graphics g) {
		g.setColor(FieldEditPanel.BG_COLOR);
		g.fillRect(0, 0, FieldEditFrame.frameWidth, FieldEditFrame.frameHeight);
	}
	
	
	
	public static FieldEditPanel getInstance() {
		return FieldEditPanel.obj;
	}


	
	public static void updateInstance(FieldEditPanel newObject) {
		FieldEditPanel.obj = newObject;
	}

}

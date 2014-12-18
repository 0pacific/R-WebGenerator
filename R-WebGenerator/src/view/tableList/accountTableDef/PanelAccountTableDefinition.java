package view.tableList.accountTableDef;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import property.GeneratorProperty;
import role.*;
import table.*;
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import utility.*;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class PanelAccountTableDefinition extends JPanel implements ActionListener, ItemListener,Serializable {
	public static final int PANEL_HEIGHT = 1500;
	
	public JComboBox comboBoxIdFieldDataTypeSelect;

	public JTextPane textPaneAtName = new JTextPane();
	public JTextPane textPaneAccOwnRoles = new JTextPane();
	public JTextPane textPaneIdFieldType = new JTextPane();
	public JTextPane textPaneUserid = new JTextPane();
	public JTextPane textPaneUseridName = new JTextPane();
	public JTextPane textPaneUseridFieldMin = new JTextPane();
	public JTextPane textPaneUseridFieldMax = new JTextPane();
	public JTextPane textPaneMailAuth = new JTextPane();
	public JTextPane textPanePassword = new JTextPane();
	public JTextPane textPanePasswordName = new JTextPane();
	public JTextPane textPanePasswordMin = new JTextPane();
	public JTextPane textPanePasswordMax = new JTextPane();
	
	public JTextField textFieldAtName = new JTextField();
	public JTextField textFieldUseridName = new JTextField();
	public JTextField textFieldUseridMin = new JTextField();
	public JTextField textFieldUseridMax = new JTextField();
	public JTextField textFieldMailAuthName = new JTextField();
	public JTextField textFieldPasswordName = new JTextField();
	public JTextField textFieldPasswordMin = new JTextField();
	public JTextField textFieldPasswordMax = new JTextField();
	
	public ArrayList<JCheckBox> checkBoxArrayAccOwnRoles = new ArrayList<JCheckBox>();
	
	public JButton btnAtAdd = new JButton();
	public JButton btnCancel = new JButton();
	
	public SerializableSpringLayout springLayout;

	public static final Color BACKGROUND_COLOR = Color.white;
	
	// 認証フィールドのデータタイプとしてID・メールアドレスのどちらが選択されているか記録する変数
	public int SELECTED_ID_FIELD_DATATYPE;
	public static final int SELECTED_ID_FIELD_DATATYPE_USERID = 0;
	public static final int SELECTED_ID_FIELD_DATATYPE_MAILAUTH = 1;
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static PanelAccountTableDefinition obj = new PanelAccountTableDefinition();

	
	
	
	
	private PanelAccountTableDefinition() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(PanelAccountTableDefinition.BACKGROUND_COLOR);
		
		initComps();
	}

	
	
	
	
	public void initComps() {
		boolean japanese = GeneratorProperty.japanese();
		
		String[] idFieldDataTypeList = {japanese?"IDで認証する":"Identify by ID", japanese?"メールアドレスで認証する":"Identify by Mail Address"};
		comboBoxIdFieldDataTypeSelect = new JComboBox(idFieldDataTypeList);
		comboBoxIdFieldDataTypeSelect.addItemListener(this);
		
		textFieldUseridMin = new JTextField();
		textFieldUseridMax = new JTextField();
	}
	
	
	
	
	
	public void relocateCompsForAccountTableDefinition(boolean frameOpened) {	// フレームが開かれこれから編集を開始するところなのかどうかを引数で指定
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		int PADD_LEFT = 20;

		// テキストペイン
		remove(textPaneAtName);
		textPaneAtName = new JTextPane();
		textPaneAtName.setEditable(false);
		textPaneAtName.setText(japanese?"1. アカウントテーブルの名称を入力して下さい。":"1. Name of Account Table");
		textPaneAtName.setPreferredSize(new Dimension(this.getWidth(), 20));
		Slpc.put(springLayout, "N", textPaneAtName, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneAtName, "W", this, PADD_LEFT);
		add(textPaneAtName);

		// テキストフィールド（アカウントテーブル名）
		remove(textFieldAtName);
		if(frameOpened) {	// フレームオープン時のみ初期化
			textFieldAtName = new JTextField();
		}
		textFieldAtName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldAtName, "S", textPaneAtName, 20);
		Slpc.put(springLayout, "W", textFieldAtName, "W", this, PADD_LEFT);
		add(textFieldAtName);
		
		// テキストペイン
		remove(textPaneAccOwnRoles);
		textPaneAccOwnRoles = new JTextPane();
		textPaneAccOwnRoles.setEditable(false);
		textPaneAccOwnRoles.setText(japanese?"2. どのロールのアカウントを管理しますか。１つ以上チェックして下さい。":"2. Which Roles' Accounts will be stored?");
		textPaneAccOwnRoles.setPreferredSize(new Dimension(this.getWidth(), 20));
		Slpc.put(springLayout, "N", textPaneAccOwnRoles, "S", textFieldAtName, 20);
		Slpc.put(springLayout, "W", textPaneAccOwnRoles, "W", this, PADD_LEFT);
		add(textPaneAccOwnRoles);

		// チェックボックス（アカウントオーナーロール選択）
		for(int i=0; i<checkBoxArrayAccOwnRoles.size(); i++) {	// 一旦全て除去
			remove(checkBoxArrayAccOwnRoles.get(i));
		}
		if(frameOpened) {	// フレームを開いた瞬間のみ、チェックボックスの初期化が必要
			checkBoxArrayAccOwnRoles = new ArrayList<JCheckBox>();
			ArrayList<String> noAtNormalRolesNames = RoleManager.getInstance().getNoAtNormalRolesNames();
			for(int i=0; i<noAtNormalRolesNames.size(); i++) {
				// チェックボックス初期化
				String noAtNormalRoleName = noAtNormalRolesNames.get(i);
				JCheckBox cb = new JCheckBox(noAtNormalRoleName);

				// チェックボックス配列に追加
				checkBoxArrayAccOwnRoles.add(cb);
			}
		}
		for(int i=0; i<checkBoxArrayAccOwnRoles.size(); i++) {
			JCheckBox cb = checkBoxArrayAccOwnRoles.get(i);

			// チェックボックス位置調整、配置
			Slpc.put(springLayout, "N", cb, "S", textPaneAccOwnRoles, 20+(i*30));
			Slpc.put(springLayout, "W", cb, "W", this, PADD_LEFT);
			add(cb);
		}
		
		
		// テキストペイン
		remove(textPaneIdFieldType);
		textPaneIdFieldType = new JTextPane();
		textPaneIdFieldType.setEditable(false);
		textPaneIdFieldType.setText(japanese?"3-1. 認証にID、メールアドレスのどちらを用いますか。選択して下さい。":"Identify by ID or Mail Address?");
		textPaneIdFieldType.setPreferredSize(new Dimension(this.getWidth(), 20));
		Slpc.put(springLayout, "N", textPaneIdFieldType, "S", checkBoxArrayAccOwnRoles.get(checkBoxArrayAccOwnRoles.size()-1), 40);
		Slpc.put(springLayout, "W", textPaneIdFieldType, "W", this, PADD_LEFT);
		add(textPaneIdFieldType);
		
		
		
		// コンボボックス（ユーザID or メールアドレス　の選択）
		remove(comboBoxIdFieldDataTypeSelect);
		Slpc.put(springLayout, "N", comboBoxIdFieldDataTypeSelect, "S", textPaneIdFieldType, 10);
		Slpc.put(springLayout, "W", comboBoxIdFieldDataTypeSelect, "W", this, PADD_LEFT);
		add(comboBoxIdFieldDataTypeSelect);
		// ItemListenerはinitComps()で追加してある

		
		// フレームを開いた瞬間である -> ユーザIDを選択しておく
		if(frameOpened) {
			comboBoxIdFieldDataTypeSelect.setSelectedItem(comboBoxIdFieldDataTypeSelect.getItemAt(0));
		}

		// ユーザIDと認証メールアドレスのどちらが選択されているかに応じて、以降の描画をわける
		String selectedIdType = (String)this.comboBoxIdFieldDataTypeSelect.getSelectedItem();
		if(selectedIdType.equals(japanese?"IDで認証する":"Identify by ID")) {
			this.locateSinceUseridEditForm(frameOpened);
		}
		else if(selectedIdType.equals(japanese?"メールアドレスで認証する":"Identify by Mail Address")) {
			this.locateSinceMailauthEditForm(frameOpened);
		}
		else {
			Debug.error("コンボボックスの値が想定外です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	
	public void locateSinceUseridEditForm(boolean frameOpened) {
		boolean japanese = GeneratorProperty.japanese();
		
		int PADD_LEFT = 20;
		int TEXT_FIELD_WIDTH = 60;

		// テキストペイン
		remove(textPaneUseridName);
		textPaneUseridName = new JTextPane();
		textPaneUseridName.setText(japanese?"3-2. IDを格納するフィールドの名前を入力して下さい。（例：「ユーザID」":"3-2. Name of ID Field (Ex. 'User ID')");
		Slpc.put(springLayout, "N", textPaneUseridName, "S", comboBoxIdFieldDataTypeSelect, 20);
		Slpc.put(springLayout, "W", textPaneUseridName, "W", this, PADD_LEFT);
		add(textPaneUseridName);

		// テキストフィールド（フィールド名）
		remove(textFieldUseridName);
		if(frameOpened) {	// フレームオープン時のみ初期化
			textFieldUseridName = new JTextField();
		}
		textFieldUseridName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldUseridName, "S", textPaneUseridName, 0);
		Slpc.put(springLayout, "W", textFieldUseridName, "W", this, PADD_LEFT);
		add(textFieldUseridName);

		
		// テキストペイン
		remove(textPaneUserid);
		textPaneUserid = new JTextPane();
		textPaneUserid.setText(japanese?"3-3. IDの最小文字数、最大文字数を設定して下さい。":"3-3. Min/Max Length of ID");
		textPaneUserid.setPreferredSize(new Dimension(this.getHeight(), 20));
		Slpc.put(springLayout, "N", textPaneUserid, "S", textFieldUseridName, 20);
		Slpc.put(springLayout, "W", textPaneUserid, "W", this, PADD_LEFT);
		add(textPaneUserid);


		// テキストペイン（最小文字数）
		remove(textPaneUseridFieldMin);
		textPaneUseridFieldMin = new JTextPane();
		textPaneUseridFieldMin.setText(japanese?"最小文字数：":"Min Length : ");
		Slpc.put(springLayout, "N", textPaneUseridFieldMin, "S", textPaneUserid, 5);
		Slpc.put(springLayout, "W", textPaneUseridFieldMin, "W", this, PADD_LEFT);
		add(textPaneUseridFieldMin);

		// テキストフィールド（最小文字数入力）
		remove(textFieldUseridMin);
		textFieldUseridMin.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldUseridMin, "S", textPaneUseridFieldMin, 0);
		Slpc.put(springLayout, "W", textFieldUseridMin, "W", this, PADD_LEFT);
		add(textFieldUseridMin);
		if(frameOpened) {	// フレームオープン時 -> 値を入れておく
			textFieldUseridMin.setText("4");
		}
		
	
		// テキストペイン（最大文字数）
		remove(textPaneUseridFieldMax);
		textPaneUseridFieldMax = new JTextPane();
		textPaneUseridFieldMax.setText(japanese?"最大文字数：":"Max Length : ");
		Slpc.put(springLayout, "N", textPaneUseridFieldMax, "S", textFieldUseridMin, 10);
		Slpc.put(springLayout, "W", textPaneUseridFieldMax, "W", this, PADD_LEFT);
		add(textPaneUseridFieldMax);

		// テキストフィールド（最大文字数入力）
		remove(textFieldUseridMax);
		textFieldUseridMax.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldUseridMax, "S", textPaneUseridFieldMax, 0);
		Slpc.put(springLayout, "W", textFieldUseridMax, "W", this, PADD_LEFT);
		add(textFieldUseridMax);
		if(frameOpened) {	// フレームオープン時 -> 値を入れておく
			textFieldUseridMax.setText("16");
		}

		
		locateSincePasswordEditForm(frameOpened, textFieldUseridMax);

		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	
	public void locateSinceMailauthEditForm(boolean frameOpened) {
		boolean japanese = GeneratorProperty.japanese();
		
		int PADD_LEFT = 20;

		// テキストペイン
		remove(textPaneMailAuth);
		textPaneMailAuth = new JTextPane();
		textPaneMailAuth.setText(
				japanese?
				"3-2. 認証にメールアドレスを用います。\n      メールアドレスを格納するフィールドの名前を入力して下さい。\n     （例：「登録メールアドレス」）":
				"3-2. Name of Mail Address Field (Ex. 'E-Mail')"
		);
		Slpc.put(springLayout, "N", textPaneMailAuth, "S", comboBoxIdFieldDataTypeSelect, 20);
		Slpc.put(springLayout, "W", textPaneMailAuth, "W", this, PADD_LEFT);
		add(textPaneMailAuth);

		// テキストフィールド（フィールド名）
		remove(textFieldMailAuthName);
		if(frameOpened) {	// フレームオープン時のみ初期化
			textFieldMailAuthName = new JTextField();
		}
		textFieldMailAuthName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldMailAuthName, "S", textPaneMailAuth, 0);
		Slpc.put(springLayout, "W", textFieldMailAuthName, "W", this, PADD_LEFT);
		add(textFieldMailAuthName);

		locateSincePasswordEditForm(frameOpened, textFieldMailAuthName);

		FrameAccountTableDefinition.repaintAndValidate();
	}


	
	
	
	public void locateSincePasswordEditForm(boolean frameOpened, Component barometerComp) {
		boolean japanese = GeneratorProperty.japanese();

		int PADD_LEFT = 20;

		// テキストペイン
		remove(textPanePasswordName);
		textPanePasswordName = new JTextPane();
		textPanePasswordName.setText(japanese?"4-1. パスワードを格納するフィールドの名前を入力して下さい。（例：「登録パスワード」":"4-1. Name of Password Field (Ex. 'Password')");
		Slpc.put(springLayout, "N", textPanePasswordName, "S", barometerComp, 20);
		Slpc.put(springLayout, "W", textPanePasswordName, "W", this, PADD_LEFT);
		add(textPanePasswordName);

		// テキストフィールド（パスワードフィールド名）
		remove(textFieldPasswordName);
		if(frameOpened) {	// フレームオープン時のみ初期化
			textFieldPasswordName = new JTextField();
		}
		textFieldPasswordName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldPasswordName, "S", textPanePasswordName, 0);
		Slpc.put(springLayout, "W", textFieldPasswordName, "W", this, PADD_LEFT);
		add(textFieldPasswordName);

		// テキストペイン
		remove(textPanePassword);
		textPanePassword = new JTextPane();
		textPanePassword.setText(japanese?"4-2. パスワードの最小文字数、最大文字数を設定して下さい。":"4-2. Min/Max Length of Password");
		textPanePassword.setPreferredSize(new Dimension(this.getHeight(), 20));
		Slpc.put(springLayout, "N", textPanePassword, "S", textFieldPasswordName, 20);
		Slpc.put(springLayout, "W", textPanePassword, "W", this, PADD_LEFT);
		add(textPanePassword);

		int TEXT_FIELD_WIDTH = 60;
		
		// テキストペイン
		remove(textPanePasswordMin);
		textPanePasswordMin = new JTextPane();
		textPanePasswordMin.setText(japanese?"最小文字数:":"Min Length : ");
		Slpc.put(springLayout, "N", textPanePasswordMin, "S", textPanePassword, 5);
		Slpc.put(springLayout, "W", textPanePasswordMin, "W", this, PADD_LEFT);
		add(textPanePasswordMin);
		
		// テキストフィールド（最小文字数入力）
		remove(textFieldPasswordMin);
		textFieldPasswordMin.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldPasswordMin, "S", textPanePasswordMin, 0);
		Slpc.put(springLayout, "W", textFieldPasswordMin, "W", this, PADD_LEFT);
		add(textFieldPasswordMin);
		if(frameOpened) {	// フレームオープン時 -> 値を入れておく
			textFieldPasswordMin.setText("4");
		}

		// テキストペイン
		remove(textPanePasswordMax);
		textPanePasswordMax = new JTextPane();
		textPanePasswordMax.setText(japanese?"最大文字数:":"Max Length : ");
		Slpc.put(springLayout, "N", textPanePasswordMax, "S", textFieldPasswordMin, 5);
		Slpc.put(springLayout, "W", textPanePasswordMax, "W", this, PADD_LEFT);
		add(textPanePasswordMax);
		
		// テキストフィールド（最大文字数入力）
		remove(textFieldPasswordMax);
		textFieldPasswordMax.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldPasswordMax, "S", textPanePasswordMax, 0);
		Slpc.put(springLayout, "W", textFieldPasswordMax, "W", this, PADD_LEFT);
		add(textFieldPasswordMax);
		if(frameOpened) {	// フレームオープン時 -> 値を入れておく
			textFieldPasswordMax.setText("16");
		}

		
		
		// アカウントテーブル追加ボタン
		remove(btnAtAdd);
		btnAtAdd = new JButton(japanese?"アカウントテーブルを追加":"Add Account Table");
		Slpc.put(springLayout, "N", btnAtAdd, "S", textFieldPasswordMax, 30);
		Slpc.put(springLayout, "W", btnAtAdd, "W", this, PADD_LEFT);
		add(btnAtAdd);
		btnAtAdd.addActionListener(this);
		btnAtAdd.setActionCommand("アカウントテーブル追加");

	
		// キャンセルボタン
		remove(btnCancel);
		btnCancel = new JButton(japanese?"キャンセル":"Cancel");
		Slpc.put(springLayout, "N", btnCancel, "N", btnAtAdd, 0);
		Slpc.put(springLayout, "W", btnCancel, "E", btnAtAdd, 10);
		add(btnCancel);
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("キャンセル");

		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		if(cmd.equals("アカウントテーブル追加")) {
			String newAtName = textFieldAtName.getText();
			
			ArrayList<NormalRole> accOwnRoleArray = new ArrayList<NormalRole>();
			for(int i=0; i<checkBoxArrayAccOwnRoles.size(); i++) {
				JCheckBox cb = checkBoxArrayAccOwnRoles.get(i);

				// チェックされていないロールはスルー
				if(!cb.isSelected()) {
					Debug.out("ノーマルロール「" + cb.getText() + "」はチェックされていないので、アカウントオーナーロールとなりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					continue;
				}

				String roleName = cb.getText();
				NormalRole normalRole = (NormalRole)RoleManager.getInstance().getRoleByName(roleName);
				accOwnRoleArray.add(normalRole);
			}

			if(newAtName.equals("")) {
				JOptionPane.showMessageDialog(this, japanese?"アカウントテーブル名を正しく入力して下さい。":"Input Account Table Name");
				Debug.out("アカウントテーブル名が入力されていません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			
			if(accOwnRoleArray.size()==0) {
				JOptionPane.showMessageDialog(this, japanese?"どのロールのアカウントを管理するのか、１つ以上選択して下さい。":"Choose at least 1 Roles");
				Debug.notice("アカウントオーナーロールが１つも選択されていません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}

			// アカウントテーブル作成
			AccountTable newTable = new AccountTable(newAtName, accOwnRoleArray);

			// 認証フィールド追加
			String selectedIdType = (String)this.comboBoxIdFieldDataTypeSelect.getSelectedItem();
			if(selectedIdType.equals(japanese?"IDで認証する":"Identify by ID")) {
				String fieldName = this.textFieldUseridName.getText();
				int min = Integer.parseInt(this.textFieldUseridMin.getText());
				int max = Integer.parseInt(this.textFieldUseridMax.getText());
				Field useridField = new Field(fieldName, Field.DATATYPE_USERID, min, max);
				newTable.addField(useridField);
			}
			else if(selectedIdType.equals(japanese?"メールアドレスで認証する":"Identify by Mail Address")) {
				String fieldName = this.textFieldMailAuthName.getText();
				Field mailAuthField = new Field(fieldName, Field.DATATYPE_MAIL_AUTH);
				newTable.addField(mailAuthField);
			}
			else {
				Debug.error("コンボボックスの値が想定外です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}

			// パスワードフィールド追加
			String passwordFieldName = this.textFieldPasswordName.getText();
			int min = Integer.parseInt(this.textFieldPasswordMin.getText());
			int max = Integer.parseInt(this.textFieldPasswordMax.getText());
			Field passwordField = new Field(passwordFieldName, Field.DATATYPE_PASSWORD, min, max);
			newTable.addField(passwordField);
			
			// TableManagerに追加
			TableManager.getInstance().addAccountTable(newTable);

			// このフレームを消し、メインフレームを使用可能にする
			FrameAccountTableDefinition.getInstance().setEnabled(false);
			FrameAccountTableDefinition.getInstance().setVisible(false);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setVisible(true);

			// メインフレームのテーブル一覧画面上部パネルのアカウントテーブル一覧を更新
			PanelTableListAbove.getInstance().refreshAccountTableList();
		}
		else if(cmd.equals("キャンセル")) {
			// このフレームを消し、メインフレームを使用可能にする
			FrameAccountTableDefinition.getInstance().setVisible(false);
			FrameAccountTableDefinition.getInstance().setEnabled(false);
			FrameAccountTableDefinition.getInstance().setFocusable(false);
			MainFrame.getInstance().setVisible(true);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setFocusable(true);
			MainFrame.getInstance().requestFocus();
		}

		MainFrame.repaintAndValidate();
		FrameAccountTableDefinition.repaintAndValidate();	
	}
	
	
	
	
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()!=this.comboBoxIdFieldDataTypeSelect) {
			Debug.error("想定外のイベント発生源です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		
		this.relocateCompsForAccountTableDefinition(false);		// フレームオープン時ではないのでfalse
		//　コンボボックスでユーザID・認証メールアドレスどちらが選択されたのかは、relocateCompsForAccountTableDefinition()側でチェックし描画も場合わけするので大丈夫

		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	public static PanelAccountTableDefinition getInstance() {
		return PanelAccountTableDefinition.obj;
	}



	public static void updateInstance(PanelAccountTableDefinition newObject) {
		PanelAccountTableDefinition.obj = newObject;
	}
}

package view.tableList;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import role.NormalRole;
import role.RoleManager;
import table.AccountTable;
import table.DataTable;
import table.TableManager;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.tableList.accountTableDef.FrameAccountTableDefinition;
import view.tableList.accountTableDef.PanelAccountTableDefinition;
import utility.*;

public class PanelTableListBottom extends JPanel implements ActionListener,Serializable {
	// 「選択中のデータテーブルを編集」ボタン
	private JButton dtEditButton;

	public SerializableSpringLayout springLayout;

	// データテーブル追加ボタン
	public JButton btnDtAdd;

	// 「選択中のアカウントテーブルを編集」ボタン
	private JButton btnAtEdit;

	// アカウントテーブル名入力テキストフィールド
	public JTextField tfAtName;
	
	// ロール選択チェックボックス
	public ArrayList<JCheckBox> cbArrayRoleSelect;
	
	// アカウントテーブル追加ボタン
	public JButton btnAtAdd;

	public static final Color backGroundColor = Color.white;
	
	// シリアライズボタン
	ButtonSaveWork btnSaveWork;
	// デシリアライズボタン
	ButtonLoadWork btnLoadWork;

	/*
	 * インスタンス参照変数は最後！
	 */
	private static PanelTableListBottom obj = new PanelTableListBottom();

	
	
	
	
	
	
	private PanelTableListBottom() {
		// パネルのレイアウト
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	
	public void locateCompsPanelTableListBottom() {
		boolean japanese = GeneratorProperty.japanese();		
		
		removeAll();
		
		// 「選択中のデータテーブルを編集」ボタン
		dtEditButton = japanese ? new JButton("選択中のデータテーブルを編集") : new JButton("Edit Data Table");
		dtEditButton.addActionListener(this);
		dtEditButton.setActionCommand("データテーブル編集");
		springLayout.putConstraint(SpringLayout.NORTH, dtEditButton, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, dtEditButton, 20, SpringLayout.WEST, this);
		add(dtEditButton);

		// データテーブル追加ボタン
		btnDtAdd = japanese ? new JButton("新たなデータテーブルを作成") : new JButton("Create Data Table");
		springLayout.putConstraint(SpringLayout.NORTH, btnDtAdd, 30, SpringLayout.SOUTH, dtEditButton);
		springLayout.putConstraint(SpringLayout.WEST, btnDtAdd, 0, SpringLayout.WEST, dtEditButton);
		btnDtAdd.addActionListener(this);
		btnDtAdd.setActionCommand("データテーブル追加");
		add(btnDtAdd);

		RoleManager roleManager = RoleManager.getInstance();

		// 「選択中のアカウントテーブルを編集」ボタン
		btnAtEdit = japanese ? new JButton("選択中のアカウントテーブルを編集") : new JButton("Edit Account Table");
		btnAtEdit.addActionListener(this);
		btnAtEdit.setActionCommand("アカウントテーブル編集");
		springLayout.putConstraint(SpringLayout.NORTH, btnAtEdit, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnAtEdit, 100, SpringLayout.EAST, dtEditButton);
		add(btnAtEdit);
		if(TableManager.getInstance().getAccountTableNum()==0) {
			// まだアカウントテーブルが１つもない場合はクリックできない
			btnAtEdit.setEnabled(false);
		}
		
		// アカウントテーブルを持たないノーマルロールが残っている場合 -> アカウントテーブル追加ボタン表示
		btnAtAdd = japanese ? new JButton("新たなアカウントテーブルを作成") : new JButton("Create Account Table");
		Slpc.put(springLayout, "N", btnAtAdd, "S", btnAtEdit, 30);
		Slpc.put(springLayout, "W", btnAtAdd, "W", btnAtEdit, 0);
		btnAtAdd.addActionListener(this);
		btnAtAdd.setActionCommand("アカウントテーブル追加");
		add(btnAtAdd);

		// 全てのノーマルロールがアカウントテーブルを持っている -> アカウントテーブルを新しく追加できないことを表示し、アカウントテーブル追加ボタンを無効化
		if(!roleManager.someNormalRolesHaveNoAt()) {
			Debug.out("アカウントテーブルを持たないノーマルロールは、現在存在しません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			
			// テキストペイン
			JTextPane textPaneAllNormalRoleAtDefined = new JTextPane();
			textPaneAllNormalRoleAtDefined.setText(japanese ? "全てのロール（ゲストロール除く）に対し\nアカウントテーブルが定義されています。" : "All roles (except Guest) have account tables.");
			Slpc.put(springLayout, "N", textPaneAllNormalRoleAtDefined, "S", btnAtAdd, 20);
			Slpc.put(springLayout, "W", textPaneAllNormalRoleAtDefined, "W", btnAtAdd, 0);
			add(textPaneAllNormalRoleAtDefined);

			// アカウントテーブル追加ボタン無効化
			btnAtAdd.setEnabled(false);
		}

		// 移動ボタン：ページ遷移定義画面
		JButton btnGoTrans = japanese ? new JButton("Webページ・遷移権限定義画面へ") : new JButton("Go To Web Page Definition");
		btnGoTrans.addActionListener(this);
		btnGoTrans.setActionCommand("遷移 : ページ遷移定義画面");
		springLayout.putConstraint(SpringLayout.NORTH, btnGoTrans, 300, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnGoTrans, 20, SpringLayout.WEST, this);
		add(btnGoTrans);

		// 移動ボタン：ロール定義画面
		JButton btnGoRoleEdit = japanese ? new JButton("ロール定義画面へ") : new JButton("Go To Role Definition");
		springLayout.putConstraint(SpringLayout.SOUTH, btnGoRoleEdit, 0, SpringLayout.SOUTH, btnGoTrans);
		springLayout.putConstraint(SpringLayout.WEST, btnGoRoleEdit, 20, SpringLayout.EAST, btnGoTrans);
		btnGoRoleEdit.addActionListener(this);
		btnGoRoleEdit.setActionCommand("遷移 : ロール定義画面");
		add(btnGoRoleEdit);

		// 移動ボタン：権限定義画面
		JButton btnGoAuthEdit = japanese ? new JButton("アクセス権限定義画面へ") : new JButton("Go To Accessibility Definition");
		springLayout.putConstraint(SpringLayout.SOUTH, btnGoAuthEdit, 0, SpringLayout.SOUTH, btnGoRoleEdit);
		springLayout.putConstraint(SpringLayout.WEST, btnGoAuthEdit, 20, SpringLayout.EAST, btnGoRoleEdit);
		btnGoAuthEdit.addActionListener(this);
		btnGoAuthEdit.setActionCommand("遷移 : 権限定義画面");
		add(btnGoAuthEdit);
		
		// シリアライズボタン
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", this, 300);
		Slpc.put(springLayout, "E", btnSaveWork, "E", this, -20);
		add(btnSaveWork);

		// デシリアライズボタン
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", btnSaveWork, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		// 「選択中のデータテーブルを編集」ボタンクリック -> 移動
		if(cmd.equals("データテーブル編集")) {
			// 選択中のデータテーブル取得
			int selectedDtIndex = PanelTableListAbove.getInstance().dataTableList.getSelectedRow();
			DataTable selectedDt = TableManager.getInstance().getDataTable(selectedDtIndex);

			// 移動
			MainFrame.getInstance().shiftToFieldList(selectedDt);
		}
		// データテーブル追加ボタンクリック
		else if(cmd.equals("データテーブル追加")) {
			while(true) {
				String newDataTableName = JOptionPane.showInputDialog(this, japanese?"データテーブルの名称を入力して下さい。":"Input data table's name.");
				if(newDataTableName==null) {	// キャンセル
					return;
				}
				else if(newDataTableName.equals("")) {	// 空列
					JOptionPane.showMessageDialog(this, "正しく入力して下さい。");
				}
				else {
					DataTable newTable = new DataTable(newDataTableName);
					TableManager.getInstance().addDataTable(newTable);
					break;
				}
			}
		}
		// アカウントテーブル追加ボタンクリック
		else if(cmd.equals("アカウントテーブル追加")) {
			FrameAccountTableDefinition frameAtd = FrameAccountTableDefinition.getInstance();
			frameAtd.setVisible(true);
			frameAtd.setEnabled(true);
			PanelAccountTableDefinition.getInstance().relocateCompsForAccountTableDefinition(true);
			
			MainFrame mainFrame = MainFrame.getInstance();
			mainFrame.setEnabled(false);
		}
		// 「選択中のアカウントテーブルを編集」ボタンクリック -> 移動
		else if(cmd.equals("アカウントテーブル編集")) {
			// 選択中のアカウントテーブル取得
			int selectedAtIndex = PanelTableListAbove.getInstance().accountTableList.getSelectedRow();
			AccountTable selectedAt = TableManager.getInstance().getAccountTable(selectedAtIndex);

			// 移動
			MainFrame.getInstance().shiftToFieldList(selectedAt);
		}
		else if(cmd.equals("遷移 : ページ遷移定義画面")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
		else if(cmd.equals("遷移 : ロール定義画面")) {
			MainFrame.getInstance().shiftToRoleEdit();
		}
		else if(cmd.equals("遷移 : 権限定義画面")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		else {
			Debug.error("不正なコマンドです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		
		PanelTableListAbove.getInstance().refreshDataTableList();
		PanelTableListAbove.getInstance().refreshAccountTableList();
		PanelTableListAbove.getInstance().locateComps();

		PanelTableListBottom.getInstance().locateCompsPanelTableListBottom();

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}


	
	
	public static PanelTableListBottom getInstance() {
		return PanelTableListBottom.obj;
	}



	public static void updateInstance(PanelTableListBottom newObject) {
		PanelTableListBottom.obj = newObject;
	}
}

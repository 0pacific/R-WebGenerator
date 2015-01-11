package view.tableList;

import gui.*;
import gui.arrow.*;

import role.Role;
import table.*;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.Serializable;

import utility.*;

/*
 * Singleton
 */
public class PanelTableListAbove extends JPanel implements Serializable {
	// パネルサイズ
	public final int panelHeight = 150;
	
	// 背景色
	public static Color backGroundColor = Color.WHITE;

	public JTable dataTableList;
	public static String[] dataTableListColumnNames;

	public JTable accountTableList;
	public static String[] accountTableListColumnNames;
	public static final int colWidthAt0 = 130;
	public static final int colWidthAt1 = 90;
	public static final int colWidthAt2 = 300;

	// アカウントテーブル追加ボタン
	public JButton btnAtAdd;
	
	public SerializableSpringLayout springLayout;
	
	/*
	 * インスタンス変数は最後！
	 */
	private static PanelTableListAbove obj = new PanelTableListAbove();

	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private PanelTableListAbove() {
		boolean japanese = GeneratorProperty.japanese();
		dataTableListColumnNames = japanese ?
									new String[]{"データテーブル名", "フィールド数"} :
									new String[]{"Data Table Name", "Fields Quantity"};
		accountTableListColumnNames = japanese ?
										new String[]{"アカウントテーブル名", "フィールド数", "ロール"} :
										new String[]{"Account Table Name", "Fields Quantity", "Role"};
		// パネルのレイアウト
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * データテーブル一覧テーブルの内容を更新する（データテーブルの追加・削除時などに使う）
	 */
	public void refreshDataTableList() {
		TableManager tableManager = TableManager.getInstance();

		// 値部分を新たに計算
		int dataTableNum = tableManager.getDataTableNum();
		String[][] dataTableListValues = new String[dataTableNum][PanelTableListAbove.dataTableListColumnNames.length];
		for(int i=0; i<dataTableNum; i++) {
			DataTable dataTable = tableManager.getDataTable(i);

			// 項目「データテーブル名」
			String dataTableName = dataTable.getTableName();
			dataTableListValues[i][0] = dataTableName;

			// 項目「フィールド数」
			int fieldNum = dataTable.getFieldNum();
			int abFieldNum = dataTable.getAllFieldNum() - fieldNum;
			dataTableListValues[i][1] = Integer.toString(fieldNum) + "(" +Integer.toString(abFieldNum) + ")";
		}
		
		// 新たなモデルをセット
		DefaultTableModel dataTableListModel
	    	= new DefaultTableModel(dataTableListValues, PanelTableListAbove.dataTableListColumnNames);
		dataTableList.setModel(dataTableListModel);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * アカウントテーブル一覧テーブルの内容を更新する（アカウントテーブルの追加・削除時などに使う）
	 */
	public void refreshAccountTableList() {
		TableManager tableManager = TableManager.getInstance();

		// 値部分を新たに計算
		int accountTableNum = tableManager.getAccountTableNum();
		String[][] accountTableListValues = new String[accountTableNum][PanelTableListAbove.accountTableListColumnNames.length];
		for(int i=0; i<accountTableNum; i++) {
			Debug.out(i + "番目のアカウントテーブルの行を出力します");

			AccountTable accountTable = tableManager.getAccountTable(i);

			// 項目「アカウントテーブル名」
			String accountTableName = accountTable.getTableName();
			accountTableListValues[i][0] = accountTableName;
			Debug.out("アカウントテーブル名 : " + accountTableName);

			
			// 項目「フィールド数」
			int fieldNum = accountTable.getFieldNum();
			int abFieldNum = accountTable.getAllFieldNum() - fieldNum;
			accountTableListValues[i][1] = Integer.toString(fieldNum) + "(" +Integer.toString(abFieldNum) + ")";

			// アカウントテーブルに対応する各ロールの名前
			int roleNum = accountTable.getNormalRoleNum();
			String roleNames = "";
			for(int j=0; j<roleNum; j++) {
				Role role = accountTable.getNormalRole(j);
				roleNames = roleNames + role.getRoleName() + "、";
			}
			roleNames = roleNames.substring(0, roleNames.length()-1);
			accountTableListValues[i][2] = roleNames;

			Debug.out(i + "番目のアカウントテーブルの行を出力しました");
		}
		
		// 新たなモデルをセット
		DefaultTableModel accountTableListModel
	    	= new DefaultTableModel(accountTableListValues, PanelTableListAbove.accountTableListColumnNames);
		accountTableList.setModel(accountTableListModel);

		// カラムの幅調整
		DefaultTableColumnModel atColumnModel = (DefaultTableColumnModel)accountTableList.getColumnModel();
		atColumnModel.getColumn(0).setPreferredWidth(colWidthAt0);
		atColumnModel.getColumn(1).setPreferredWidth(colWidthAt1);
		atColumnModel.getColumn(2).setPreferredWidth(colWidthAt2);
	}

	
	
	
	
	
	
	public void locateComps() {
		removeAll();
		
		// データテーブル一覧テーブルの初期化（ここでの refreshDataTableList() は事実上初期化）
		dataTableList = new JTable();
		refreshDataTableList();

		// データテーブル一覧テーブルを格納する JScrollPane
		JScrollPane sp_dataTableList = new JScrollPane(dataTableList);
		int widthDt = 250;
		sp_dataTableList.setPreferredSize(new Dimension(widthDt, this.panelHeight-40));
		springLayout.putConstraint(SpringLayout.NORTH, sp_dataTableList, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, sp_dataTableList, 20, SpringLayout.WEST, this);
		add(sp_dataTableList);

		// アカウントテーブル一覧（JTable）初期化（ここでの refreshAccountTableList() は事実上初期化）
		accountTableList = new JTable();
		refreshAccountTableList();
		
		// アカウントテーブル一覧テーブルを格納する JScrollPane
		JScrollPane sp_accountTableList = new JScrollPane(accountTableList);
		int widthAt = colWidthAt0 + colWidthAt1 + colWidthAt2;
		sp_accountTableList.setPreferredSize(new Dimension(widthAt, this.panelHeight-40));
		springLayout.putConstraint(SpringLayout.NORTH, sp_accountTableList, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, sp_accountTableList, 40, SpringLayout.EAST, sp_dataTableList);
		add(sp_accountTableList);

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void paintComponent(Graphics g) {
		g.setColor(PanelTableListAbove.backGroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	
	
	
	
	public static PanelTableListAbove getInstance() {
		return PanelTableListAbove.obj;
	}
	
	
	public static void updateInstance(PanelTableListAbove newObject) {
		PanelTableListAbove.obj = newObject;
	}

}

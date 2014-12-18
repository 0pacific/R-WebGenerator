package view.roleEdit;

import gui.*;
import gui.arrow.*;

import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.*;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import java.awt.Dimension;
import java.io.Serializable;

import utility.*;

/*
 * Singleton
 */
public class Panel_RoleEdit_Above extends JPanel implements Serializable {
	public RoleManager roleManager;
	public static final int panelHeight = 300;
	public JTable roleTable;
	public static String[] roleTableColumnNames;

	// 背景色
	public static Color backGroundColor = Color.WHITE;

	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// テーブル一覧表を格納したスクロールペインの横幅
	public static final int SCROLL_PANE_WIDTH = 300;
	
	/*
	 * インスタンスは最後！
	 */
	private static Panel_RoleEdit_Above obj = new Panel_RoleEdit_Above();
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private Panel_RoleEdit_Above() {
		roleTableColumnNames = GeneratorProperty.japanese() ? new String[]{"ロール名", "アカウントテーブル"} : new String[]{"Role Name", "Account Table"};
		
		// レイアウト
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		
		// ロールテーブルの初期化（ここでの refreshRoleTable() は初期化にあたる）
		roleTable = new JTable();
		refreshRoleTable();

		// ロールテーブルを格納するスクロールペイン
		JScrollPane sp = new JScrollPane(roleTable);
		sp.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, Panel_RoleEdit_Above.panelHeight-20));
		springLayout.putConstraint(SpringLayout.NORTH, sp, 20, SpringLayout.NORTH, this);
	    springLayout.putConstraint(SpringLayout.WEST, sp, 20, SpringLayout.WEST, this);
	    add(sp);
	}

	public static Panel_RoleEdit_Above getInstance() {
		return Panel_RoleEdit_Above.obj;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}


	
	
	/*
	 * PURPOSE :
	 * ロールテーブルの内容を更新する（ロールの追加・削除時などに使う）
	 */
	public void refreshRoleTable() {
		RoleManager roleManager = RoleManager.getInstance();
		int roleNum = roleManager.getRoleNum();

		String[][] roleTableValues = new String[roleNum][Panel_RoleEdit_Above.roleTableColumnNames.length];
		for(int i=0; i<roleNum; i++) {
			Role role = roleManager.getRole(i);

			String roleName = role.getRoleName();
			roleTableValues[i][0] = roleName;
			
			// アカウントテーブル名の欄の値を決める
			String accountTableName = null;
			if(role instanceof NormalRole) {	// ノーマルロール
				NormalRole normalRole = (NormalRole)role;

				// アカウントテーブルが指定されていればその名前、いなければnullのまま
				if(normalRole.accountTableDefined()) {
					AccountTable at = normalRole.getAccountTable();
					accountTableName = at.getTableName();
				}
			}
			else {	// ゲストロールの場合はnull（なのでそのまま）
			}
			
			roleTableValues[i][1] = accountTableName;
		}
	    DefaultTableModel roleTableModel
	    	= new DefaultTableModel(roleTableValues, Panel_RoleEdit_Above.roleTableColumnNames);
		roleTable.setModel(roleTableModel);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(backGroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}



	public static void updateInstance(Panel_RoleEdit_Above newObject) {
		Panel_RoleEdit_Above.obj = newObject;
	}
}

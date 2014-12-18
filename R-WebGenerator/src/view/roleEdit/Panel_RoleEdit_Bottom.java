package view.roleEdit;

import gui.*;
import gui.arrow.*;

import role.GuestRole;
import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.*;
import tpp.*;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.tableList.PanelTableListAbove;
import view.transProcEdit.serviceArgsWindow.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import property.GeneratorProperty;

import debug.Debug;

import mainFrame.MainFrame;

import java.awt.event.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.Serializable;

import utility.*;

/*
 * Singleton
 */
public class Panel_RoleEdit_Bottom extends JPanel implements ActionListener,Serializable {
	private JButton roleAddButton;

	public JButton btnRemoveRole;
	
	// 新規ロール名を入力するテキストフィールドの横幅
	public static int ROLE_NAME_TF_WIDTH = 100;
	
	// 他画面への遷移ボタン
	private JButton transitionButton_diagram;
	private JButton transitionButton_tableList;
	private JButton transBtn_authEdit;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// 背景色
	public static Color BG_COLOR = Color.WHITE;

	// シリアライズボタン
	ButtonSaveWork btnSaveWork;
	// デシリアライズボタン
	ButtonLoadWork btnLoadWork;

	/*
	 * インスタンスは最後！
	 */
	private static Panel_RoleEdit_Bottom obj = new Panel_RoleEdit_Bottom();
	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private Panel_RoleEdit_Bottom() {
		// パネルのレイアウト
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	

	/*
	 * PURPOSE :
	 * コンポーネントの再配置
	 */
	public void locateComps() {
		boolean japanese = GeneratorProperty.japanese();
		
		// 全コンポーネントを除去
		removeAll();
		
		
		// ロール作成ボタン
		String msg1 = japanese ? "ロールを作成" : "Add Role";
		roleAddButton = new JButton(msg1);
		springLayout.putConstraint(SpringLayout.NORTH, roleAddButton, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, roleAddButton, 20, SpringLayout.WEST, this);
		add(roleAddButton);

		// ロール削除ボタン
		String msg2 = japanese ? "選択中のロールを削除" : "Delete Selected Role";
		btnRemoveRole = new JButton(msg2);
		Slpc.put(springLayout, "N", btnRemoveRole, "N", roleAddButton, 0);
		Slpc.put(springLayout, "W", btnRemoveRole, "E", roleAddButton, 30);
		add(btnRemoveRole);
		
		// 遷移図定義画面への遷移ボタン
		String msg3 = japanese ? "Webページ定義画面へ" : "Go To Web Page Definition";
		transitionButton_diagram = new JButton(msg3);
		springLayout.putConstraint(SpringLayout.NORTH, transitionButton_diagram, 50, SpringLayout.SOUTH, roleAddButton);
		springLayout.putConstraint(SpringLayout.WEST, transitionButton_diagram, 20, SpringLayout.WEST, this);
		add(transitionButton_diagram);

		// テーブルリスト画面への遷移ボタン
		String msg4 = japanese ? "テーブル定義画面へ" : "Go To Table Definition";
		transitionButton_tableList = new JButton(msg4);
		springLayout.putConstraint(SpringLayout.NORTH, transitionButton_tableList, 0, SpringLayout.NORTH, transitionButton_diagram);
		springLayout.putConstraint(SpringLayout.WEST, transitionButton_tableList, 20, SpringLayout.EAST, transitionButton_diagram);
		add(transitionButton_tableList);

		// 権限定義画面への遷移ボタン
		String msg5 = japanese ? "アクセス権限定義画面へ" : "Go To Accessibility Definition";
		transBtn_authEdit = new JButton(msg5);
		springLayout.putConstraint(SpringLayout.NORTH, transBtn_authEdit, 0, SpringLayout.NORTH, transitionButton_diagram);
		springLayout.putConstraint(SpringLayout.WEST, transBtn_authEdit, 20, SpringLayout.EAST, transitionButton_tableList);
		add(transBtn_authEdit);

		// シリアライズボタン
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", this, 20);
		Slpc.put(springLayout, "E", btnSaveWork, "E", this, -20);
		add(btnSaveWork);

		// デシリアライズボタン
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", btnSaveWork, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);

		// 各ボタンはこの関数が実行される度に新しく生成されているため、毎回イベントリスナの追加・コマンド設定を行う
		addListeners();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * ロール追加ボタンと、各画面への遷移ボタンに、ActionListenerを追加し、ActionCommandを設定する
	 */
	public void addListeners() {
		roleAddButton.addActionListener(this);
		roleAddButton.setActionCommand("ロール追加");

		btnRemoveRole.addActionListener(this);
		btnRemoveRole.setActionCommand("ロール削除");
		
		transitionButton_diagram.addActionListener(this);
		transitionButton_diagram.setActionCommand("遷移：ページ遷移定義画面");

		transitionButton_tableList.addActionListener(this);
		transitionButton_tableList.setActionCommand("遷移：テーブルリスト画面");
		
		transBtn_authEdit.addActionListener(this);
		transBtn_authEdit.setActionCommand("遷移：権限定義画面");
	}
	
	
	
	
	
	public void paintComponent(Graphics g) {
		// 背景塗り潰し
		g.setColor(Panel_RoleEdit_Bottom.BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * 各ボタンクリック時のアクション
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		// 「ロールの追加」ボタンクリック -> ロール追加、ロールテーブル更新
		if(cmd.equals("ロール追加")) {
			while(true) {
				String msg1 = japanese ? "作成するロールの名称を入力して下さい。" : "Enter Role Name";
				String newRoleName = JOptionPane.showInputDialog(this, msg1);
				if(newRoleName==null) {	// キャンセル
					return;
				}
				else if(newRoleName.equals("")) {	// 空列
					String msg2 = japanese ? "1字以上の名称を入力して下さい。" : "Role's name mustn't be blank.";
					JOptionPane.showMessageDialog(this, msg2);
				}
				else {
					NormalRole newNormalRole = new NormalRole(newRoleName);
					RoleManager.getInstance().addRole(newNormalRole);
					break;
				}
			}
		}
		else if(cmd.equals("ロール削除")) {
			// 選択中のロール取得
			int selectedRoleIndex = Panel_RoleEdit_Above.getInstance().roleTable.getSelectedRow();
			Role selectedRole = RoleManager.getInstance().getRole(selectedRoleIndex);

			// ゲストロールを削除しようとしている -> ダメ
			if(selectedRole instanceof GuestRole) {
				String msg3 = japanese ? "ゲストロールは削除できません" : "Guest Role can't be deleted";
				JOptionPane.showMessageDialog(this, msg3);
				return;
			}

			/*
			 * 選択されたのは非ゲストロールである
			 */

			NormalRole selectedNormalRole = (NormalRole)selectedRole;
			
			// このロールを削除すると、アカウントテーブルも削除することになるか否か（そのアカウントテーブルでこのロールのアカウントしか管理していない場合そうなる）
			boolean shouldDeleteAt = RoleManager.getInstance().shouldRemoveAtIfRoleRemoved(selectedNormalRole);
			
			// アカウントテーブルが削除されるか否かで、確認メッセージを変える
			String confirmMessage = null;
			if(shouldDeleteAt) {
				confirmMessage =	japanese ?
									"削除してもよろしいですか？このロールのアカウントテーブルやログインフォームも削除します。" :
									"Would you really like to delete the role? This role's account table and login form would also be deleted";
			}
			else {
				confirmMessage =	japanese ?
									"このロールを削除してもよろしいですか？" :
									"Would you really like to delete the role?";
			}

			// 確認メッセージ
			int confirm = JOptionPane.showConfirmDialog(this, confirmMessage, "ロールの削除", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {
				return;
			}

			/*
			 * ロール削除が決定
			 */
			
			RoleManager.getInstance().removeNormalRole(selectedNormalRole);
		}
		// 「ページ遷移定義画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：ページ遷移定義画面")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
		// 「テーブル編集画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：テーブルリスト画面")) {
			MainFrame.getInstance().shiftToTableList();
		}
		// 「権限定義画面へ」ボタンクリック -> 移動
		else if(cmd.equals("遷移：権限定義画面")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		// エラー
		else {
			JOptionPane.showMessageDialog(this, "エラーが発生しました。");
			Debug.error("不正なコマンドです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		Panel_RoleEdit_Above.getInstance().refreshRoleTable();
		MainFrame.getInstance().repaintAndValidate();
	}

	
	
	
	
	public static Panel_RoleEdit_Bottom getInstance() {
		return Panel_RoleEdit_Bottom.obj;
	}
	
	
	
	public static void updateInstance(Panel_RoleEdit_Bottom newObject) {
		Panel_RoleEdit_Bottom.obj = newObject;
	}

}

package view.adminAuthEdit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import mainFrame.MainFrame;
import debug.Debug;
import property.GeneratorProperty;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.roleEdit.Panel_RoleEdit_Above;
import view.roleEdit.Panel_RoleEdit_Bottom;
//【追加】
public class PanelAdminAuthEditBottom extends JPanel implements ActionListener,Serializable{
	
	// 他画面への遷移ボタン
		private JButton transitionButton_diagram;
		private JButton transitionButton_tableList;
		private JButton transBtn_authEdit;
		private JButton transBtn_roleEdit;
		
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
		private static PanelAdminAuthEditBottom obj = new PanelAdminAuthEditBottom();

		private PanelAdminAuthEditBottom(){
			springLayout = new SerializableSpringLayout();
			setLayout(springLayout);
		}
		
		//コンポーネントの再配置
		public void locateComps(){
			boolean japanese = GeneratorProperty.japanese();
			
			
			// 遷移図定義画面への遷移ボタン
			String msg3 = japanese ? "Webページ定義画面へ" : "Go To Web Page Definition";
			transitionButton_diagram = new JButton(msg3);
			springLayout.putConstraint(SpringLayout.NORTH, transitionButton_diagram, 50, SpringLayout.SOUTH, this);
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

			// ロール定義画面への遷移ボタン
			String msg6 = japanese ? "ロール定義画面へ" : "Go To Role Definition";
			transBtn_roleEdit = new JButton(msg6);
			springLayout.putConstraint(SpringLayout.NORTH, transBtn_roleEdit, 0, SpringLayout.NORTH, transitionButton_diagram);
			springLayout.putConstraint(SpringLayout.WEST, transBtn_roleEdit, 20, SpringLayout.EAST, transitionButton_tableList);
			add(transBtn_roleEdit);
			
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
		
		
	private void addListeners() {
		
		transitionButton_diagram.addActionListener(this);
		transitionButton_diagram.setActionCommand("遷移：ページ遷移定義画面");

		transitionButton_tableList.addActionListener(this);
		transitionButton_tableList.setActionCommand("遷移：テーブルリスト画面");
		
		transBtn_authEdit.addActionListener(this);
		transBtn_authEdit.setActionCommand("遷移：権限定義画面");
		
		transBtn_roleEdit.addActionListener(this);
		transBtn_roleEdit.setActionCommand("遷移：ロール定義画面");
			
		}

	public void paintComponent(Graphics g) {
		// 背景塗り潰し
		g.setColor(Panel_RoleEdit_Bottom.BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	
	public void actionPerformed(ActionEvent e) {

		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();

		// 「ページ遷移定義画面へ」ボタンクリック -> 移動
		if(cmd.equals("遷移：ページ遷移定義画面")) {
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
		else if(cmd.equals("遷移：ロール定義画面")) {
			MainFrame.getInstance().shiftToRoleEdit();
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

}

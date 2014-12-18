package view.webPageDefinition.transAuthEdit;

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





public class PanelTransAuthEdit extends JPanel implements ActionListener{
	public static final int PANEL_HEIGHT = 800;
	
	public ArrayList<JCheckBox> checkBoxArrayRoles = new ArrayList<JCheckBox>();

	public JTextPane textPaneTransDesc;
	public JTextPane textPaneTransAuth;
	
	public JButton btnFinish = new JButton();
	public JButton btnCancel = new JButton();
	
	public SerializableSpringLayout springLayout;

	public static final Color BACKGROUND_COLOR = Color.white;

	public Transition editingTransition;
	
	/*
	 * インスタンス参照変数は最後！
	 */
	private static PanelTransAuthEdit obj = new PanelTransAuthEdit();

	
	
	
	
	private PanelTransAuthEdit() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(PanelTransAuthEdit.BACKGROUND_COLOR);
	}

	
	
	
	
	
	
	
	
	
	public void relocateCompsForTransAuthEdit(Transition transition) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.editingTransition = transition;
		
		removeAll();

		int PADD_LEFT = 20;

		// テキストペイン（遷移の説明）
		textPaneTransDesc = new JTextPane();
		textPaneTransDesc.setEditable(false);
		String textTransDesc =	transition.startPage.pageFileName + " -> " + transition.endPage.pageFileName + "\n";
		PageElement triggerPe = transition.triggerPe;
		if(triggerPe instanceof PageElement_HyperLink) {
			PageElement_HyperLink triggerHyperLink = (PageElement_HyperLink)triggerPe;
			textTransDesc += japanese?
								"ハイパーリンク「" + triggerHyperLink.getText() + "」による遷移":
								"Transition by Hyper Link '" + triggerHyperLink.getText() + "'";
		}
		else if(triggerPe instanceof PageElementCreateForm) {
			PageElementCreateForm triggerCreateForm = (PageElementCreateForm)triggerPe;
			textTransDesc += japanese?
								"テーブル「" + triggerCreateForm.table.getTableName() + "」のレコード作成フォームによる遷移":
								"Transition by Creation Form of Table '" + triggerCreateForm.table.getTableName() + "'";
		}
		else if(triggerPe instanceof PageElementUpdateForm) {
			PageElementUpdateForm triggerUpdateForm = (PageElementUpdateForm)triggerPe;
			textTransDesc += japanese?
								"テーブル「" + triggerUpdateForm.getTable().getTableName() + "」の更新フォームによる遷移":
								"Transition by Update Form of Table '" + triggerUpdateForm.getTable().getTableName() + "'";
		}
		else if(triggerPe instanceof PageElementLoginForm) {
			PageElementLoginForm triggerLoginForm = (PageElementLoginForm)triggerPe;
			textTransDesc += japanese?
							"アカウントテーブル「" + triggerLoginForm.accountTable.getTableName() + "」のログインフォームによる遷移":
							"Transition by Login Form of Account Table '" + triggerLoginForm.accountTable.getTableName() + "'";
		}
		else {
			Debug.error("想定外の遷移トリガーページエレメントです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		textPaneTransDesc.setText(textTransDesc);
		Slpc.put(springLayout, "N", textPaneTransDesc, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneTransDesc, "W", this, PADD_LEFT);
		add(textPaneTransDesc);

		
		textPaneTransAuth = new JTextPane();
		textPaneTransAuth.setEditable(false);
		textPaneTransAuth.setText(
			japanese?
			"上記遷移をどのロールに許可しますか。\n１つ以上チェックして下さい。\n※遷移を許可すると、遷移プロセス中の全ての処理は\n操作権限に関係なく実行されます。":
			"Check Roles which can transit."
		);
		textPaneTransAuth.setPreferredSize(new Dimension(FrameTransAuthEdit.frameWidth-(2*PADD_LEFT), 20));
		Slpc.put(springLayout, "N", textPaneTransAuth, "S", textPaneTransDesc, 20);
		Slpc.put(springLayout, "W", textPaneTransAuth, "W", this, PADD_LEFT);
		add(textPaneTransAuth);
		
		
		// チェックボックス（各ロールの名前）
		checkBoxArrayRoles = new ArrayList<JCheckBox>();
		RoleManager roleManager = RoleManager.getInstance();
		for(int i=0; i<roleManager.getRoleNum(); i++) {
			Role role = roleManager.getRole(i);
			TransitionAuth transAuth = AuthorityManager.getInstance().getTransAuth(role, transition);

			JCheckBox checkBoxRoleName = new JCheckBox(role.getRoleName());
			if(transAuth.transAuth) {
				checkBoxRoleName.setSelected(true);
			}

			// チェックボックス配列に追加
			checkBoxArrayRoles.add(checkBoxRoleName);

			// チェックボックス位置調整、配置
			Slpc.put(springLayout, "N", checkBoxRoleName, "S", textPaneTransAuth, 20+(i*30));
			Slpc.put(springLayout, "W", checkBoxRoleName, "W", this, PADD_LEFT);
			add(checkBoxRoleName);
		}

		
		// 編集完了ボタン
		btnFinish = new JButton(japanese?"編集完了":"Edit");
		btnFinish.addActionListener(this);
		btnFinish.setActionCommand("編集完了");
		Slpc.put(springLayout, "N", btnFinish, "S", textPaneTransAuth, 20+(checkBoxArrayRoles.size()*30));
		Slpc.put(springLayout, "W", btnFinish, "W", this, PADD_LEFT);
		add(btnFinish);
		
		// キャンセルボタン
		btnCancel = new JButton(japanese?"キャンセル":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("キャンセル");
		Slpc.put(springLayout, "N", btnCancel, "N", btnFinish, 0);
		Slpc.put(springLayout, "W", btnCancel, "E", btnFinish, 20);
		add(btnCancel);

		
		FrameTransAuthEdit.repaintAndValidate();
	}
	
	
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("編集完了")) {
			// まず、１つ以上のロールをチェックしてあるか調べる。１つもしてないなら警告して終了
			int checkedRoleNum = 0;
			for(int i=0; i<this.checkBoxArrayRoles.size(); i++) {
				JCheckBox cb = checkBoxArrayRoles.get(i);
				if(cb.isSelected()) {
					checkedRoleNum++;
				}
			}
			if(checkedRoleNum==0) {
				JOptionPane.showMessageDialog(this, "最低１つのロールに遷移を許可して下さい。");
				return;
			}

		
			for(int i=0; i<this.checkBoxArrayRoles.size(); i++) {
				JCheckBox cb = checkBoxArrayRoles.get(i);
				Role role = RoleManager.getInstance().getRoleByName(cb.getText());
				TransitionAuth transAuth = AuthorityManager.getInstance().getTransAuth(role, this.editingTransition);

				transAuth.changeTransAuth(cb.isSelected());
			}
		}
		else if(cmd.equals("キャンセル")) {
			// 何もしない
		}

		// このフレームを消し、メインフレームを使用可能にする
		FrameTransAuthEdit.getInstance().setVisible(false);
		FrameTransAuthEdit.getInstance().setEnabled(false);
		FrameTransAuthEdit.getInstance().setFocusable(false);
		MainFrame.getInstance().setVisible(true);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().setFocusable(true);
		MainFrame.getInstance().requestFocus();

		// 上部パネルと下部パネルのコンポーネント再配置
		PanelWebPageDefAbove.getInstance().locateWebPagePanels();
		PanelWebPageDefBottom.getInstance().locateCompsWebPageDefBottom();
		
		MainFrame.repaintAndValidate();
		FrameTransAuthEdit.repaintAndValidate();	
	}
	
	
	
	
	
	
	public static PanelTransAuthEdit getInstance() {
		return PanelTransAuthEdit.obj;
	}





	public static void updateInstance(PanelTransAuthEdit newObject) {
		PanelTransAuthEdit.obj = newObject;
	}
}

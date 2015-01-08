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

import debug.Debug;
import property.GeneratorProperty;
import utility.SerializableSpringLayout;
import view.adminAuthEdit.editPage.IDPassEditFrame;
import view.adminAuthEdit.editPage.IDPassEditPanel;
import view.authEdit.PanelAuthEditBottom;
import view.gotoButton.GoToButton_RoleDefinition;
import view.gotoButton.GoToButton_TableDefinition;
import view.gotoButton.GoToButton_WebPageDefinition;
import view.roleEdit.Panel_RoleEdit_Bottom;
import mainFrame.MainFrame;

public class PanelAdminAuthEditMiddle extends JPanel implements ActionListener,Serializable{
	public final int panelWidth = MainFrame.frameWidth - 20;
	public final int panelHeight = 80;
	
	private JButton btn_AdminInfoEdit = new JButton();
	private JButton btn_CreaterInfoEdit = new JButton();
	private JButton btn_TableAuthEdit = new JButton();
	private JButton btn_RoleAuthEdit = new JButton();
	private JButton btn_PageAuthEdit = new JButton();

	SerializableSpringLayout springLayout;
	
	private static PanelAdminAuthEditMiddle obj = new PanelAdminAuthEditMiddle();
	
	private PanelAdminAuthEditMiddle(){
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		setBackground(Color.WHITE);
		
		locateComps();
	}
	
	public void locateComps(){
		boolean japanese = GeneratorProperty.japanese();
		
		// 各コンポーネント配置
		String msg1= japanese? "管理者ID・パスワードの設定" : "Edit Admin Info";
		btn_AdminInfoEdit = new JButton(msg1);
		springLayout.putConstraint(SpringLayout.NORTH, btn_AdminInfoEdit, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btn_AdminInfoEdit, 20, SpringLayout.WEST, this);
		add(btn_AdminInfoEdit);
		
		String msg2= japanese? "作成者ID・パスワードの設定" : "Edit Creater Info";
		btn_CreaterInfoEdit = new JButton(msg2);
		springLayout.putConstraint(SpringLayout.NORTH, btn_CreaterInfoEdit, 0, SpringLayout.NORTH, btn_AdminInfoEdit);
		springLayout.putConstraint(SpringLayout.WEST, btn_CreaterInfoEdit, 20, SpringLayout.EAST, btn_AdminInfoEdit);
		add(btn_CreaterInfoEdit);
		
		String msg3= japanese? "テーブルに関する管理者権限の編集" : "Edit Admin Info <Table>";
		btn_TableAuthEdit = new JButton(msg3);
		springLayout.putConstraint(SpringLayout.NORTH,	btn_TableAuthEdit, 20, SpringLayout.SOUTH, btn_AdminInfoEdit);
		springLayout.putConstraint(SpringLayout.WEST,	btn_TableAuthEdit, 0, SpringLayout.WEST, btn_AdminInfoEdit);
		add(btn_TableAuthEdit);
		
		String msg4= japanese? "ロールに関する管理者権限の編集" : "Edit Admin Info <Role>";
		btn_RoleAuthEdit = new JButton(msg4);
		springLayout.putConstraint(SpringLayout.NORTH,	btn_RoleAuthEdit, 0, SpringLayout.NORTH, btn_TableAuthEdit);
		springLayout.putConstraint(SpringLayout.WEST,	btn_RoleAuthEdit, 20, SpringLayout.EAST, btn_TableAuthEdit);
		add(btn_RoleAuthEdit);
		
		String msg5= japanese? "ページに関する管理者権限の編集" : "Edit Admin Info <Page>";
		btn_PageAuthEdit = new JButton(msg5);
		springLayout.putConstraint(SpringLayout.NORTH,	btn_PageAuthEdit, 0, SpringLayout.NORTH, btn_RoleAuthEdit);
		springLayout.putConstraint(SpringLayout.WEST,	btn_PageAuthEdit, 20, SpringLayout.EAST, btn_RoleAuthEdit);
		add(btn_PageAuthEdit);
		
		addActionListener();
	}
	//ActionListener追加
	private void addActionListener() {
		
		btn_AdminInfoEdit.addActionListener(this);
		btn_AdminInfoEdit.setActionCommand("管理者編集");
		
		btn_CreaterInfoEdit.addActionListener(this);
		btn_CreaterInfoEdit.setActionCommand("作成者編集");
		
		btn_TableAuthEdit.addActionListener(this);
		btn_TableAuthEdit.setActionCommand("テーブル権限編集");
		
		btn_RoleAuthEdit.addActionListener(this);
		btn_RoleAuthEdit.setActionCommand("ロール権限編集");
		
		btn_PageAuthEdit.addActionListener(this);
		btn_PageAuthEdit.setActionCommand("ページ権限編集");
		
		
	}

	public void paintComponent(Graphics g) {
		// 背景塗り潰し
		g.setColor(Panel_RoleEdit_Bottom.BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		boolean japanese = GeneratorProperty.japanese();
		if(cmd.equals("管理者編集")){
			IDPassEditFrame frame = IDPassEditFrame.getInstance();
			frame.setVisible(true);
			frame.setEnabled(true);
			IDPassEditPanel.getInstance().openAdmin();
			
			MainFrame.getInstance().setEnabled(false);
		}
		else if(cmd.equals("作成者編集")){
			IDPassEditFrame frame = IDPassEditFrame.getInstance();
			frame.setVisible(true);
			frame.setEnabled(true);
			IDPassEditPanel.getInstance().openCreater();
			
			MainFrame.getInstance().setEnabled(false);
			
		}
		else if(cmd.equals("テーブル権限編集")){
			
			PanelAdminAuthEditAbove bottomPanel = PanelAdminAuthEditAbove.getInstance();
			bottomPanel.relocate("table");
		}
		else if(cmd.equals("ロール権限編集")){
			PanelAdminAuthEditAbove bottomPanel = PanelAdminAuthEditAbove.getInstance();
			bottomPanel.relocate("role");
		}
		else if(cmd.equals("ページ権限編集")){
			PanelAdminAuthEditAbove bottomPanel = PanelAdminAuthEditAbove.getInstance();
			bottomPanel.relocate("page");
		}
		// エラー
		else {
			JOptionPane.showMessageDialog(this, "エラーが発生しました。");
			Debug.error("不正なコマンドです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		//PanelAdminAuthEditAbove.getInstance().refleshAdminAuthTable();
		MainFrame.getInstance().repaintAndValidate();
	}
	
	
	
}

package view.authEdit;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
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
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.gotoButton.GoToButton_RoleDefinition;
import view.gotoButton.GoToButton_TableDefinition;
import view.gotoButton.GoToButton_WebPageDefinition;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





/*
 * NOTICE :
 * Singleton
 */
public class PanelAuthEditBottom extends JPanel implements ActionListener,Serializable {
	public final int panelWidth = MainFrame.frameWidth - 20;
	public final int panelHeight = 80;

	private JComboBox roleComboBox = new JComboBox();
	private JComboBox tableComboBox = new JComboBox();

	// �V���A���C�Y�{�^��
	ButtonSaveWork btnSaveWork;
	// �f�V���A���C�Y�{�^��
	ButtonLoadWork btnLoadWork;

	SerializableSpringLayout springLayout;


	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�
	 */
	private static PanelAuthEditBottom obj = new PanelAuthEditBottom();

	

	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private PanelAuthEditBottom() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		setBackground(Color.WHITE);

		relocateCompsPanelAuthEditBottom();
	}
	

	
	
	
	public void relocateCompsPanelAuthEditBottom() {
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		// ���[���I���R���{�{�b�N�X������
		roleComboBox = new JComboBox();
		roleComboBox.addItem(new String(japanese?"---���[����I�����ĉ�����---":"---Select Role---"));
		RoleManager roleManager = RoleManager.getInstance();
		int roleNum = roleManager.getRoleNum();
		for(int i=0; i<roleNum; i++) {
			Role role = roleManager.getRole(i);
			String roleName = role.getRoleName();
			roleComboBox.addItem(roleName);
		}
		roleComboBox.addActionListener(this);
		roleComboBox.setActionCommand("���[���I��");

		
		
		// �e�[�u���I���R���{�{�b�N�X������
		tableComboBox = new JComboBox();
		tableComboBox.addItem(japanese ? "---�e�[�u����I�����ĉ�����---" : "---Select Table---");
		TableManager tableManager = TableManager.getInstance();
		int dataTableNum = tableManager.getDataTableNum();
		for(int i=0; i<dataTableNum; i++) {
			DataTable dt = tableManager.getDataTable(i);
			String name = dt.getTableName();
			tableComboBox.addItem(name);
		}
		int accountTableNum = tableManager.getAccountTableNum();
		for(int i=0; i<accountTableNum; i++) {
			AccountTable at = tableManager.getAccountTable(i);
			String name = at.getTableName();
			tableComboBox.addItem(name);
		}
		tableComboBox.addActionListener(this);
		tableComboBox.setActionCommand("�e�[�u���I��");

		
		
		// �e�R���|�[�l���g�z�u
		springLayout.putConstraint(SpringLayout.NORTH, roleComboBox, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, roleComboBox, 20, SpringLayout.WEST, this);
		add(roleComboBox);
		springLayout.putConstraint(SpringLayout.NORTH, tableComboBox, 0, SpringLayout.NORTH, roleComboBox);
		springLayout.putConstraint(SpringLayout.WEST, tableComboBox, 20, SpringLayout.EAST, roleComboBox);
		add(tableComboBox);
		GoToButton_TableDefinition buttonT = new GoToButton_TableDefinition();
		springLayout.putConstraint(SpringLayout.NORTH,	buttonT, 20, SpringLayout.SOUTH, roleComboBox);
		springLayout.putConstraint(SpringLayout.WEST,	buttonT, 0, SpringLayout.WEST, roleComboBox);
		add(buttonT);
		GoToButton_RoleDefinition buttonR = new GoToButton_RoleDefinition();
		springLayout.putConstraint(SpringLayout.NORTH,	buttonR, 0, SpringLayout.NORTH, buttonT);
		springLayout.putConstraint(SpringLayout.WEST,	buttonR, 20, SpringLayout.EAST, buttonT);
		add(buttonR);
		GoToButton_WebPageDefinition buttonW = new GoToButton_WebPageDefinition();
		springLayout.putConstraint(SpringLayout.NORTH,	buttonW, 0, SpringLayout.NORTH, buttonR);
		springLayout.putConstraint(SpringLayout.WEST,	buttonW, 20, SpringLayout.EAST, buttonR);
		add(buttonW);

		
		// �V���A���C�Y�{�^��
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", this, 20);
		Slpc.put(springLayout, "E", btnSaveWork, "E", this, -20);
		add(btnSaveWork);

		// �f�V���A���C�Y�{�^��
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", btnSaveWork, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);
		
		MainFrame.repaintAndValidate();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * ActionEvent�n���h��
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		boolean japanese = GeneratorProperty.japanese();
		
		// ���[���I���R���{�{�b�N�X�Ȃ����e�[�u���I���R���{�{�b�N�X�̕ύX�@->�@�㕔�p�l���ɒm�点�A�R���|�[�l���g�Ĕz�u�𑣂�
		if(cmd.equals("���[���I��") || cmd.equals("�e�[�u���I��")) {
			String roleName = roleComboBox.getSelectedItem().toString();
			String tableName = tableComboBox.getSelectedItem().toString();

			PanelAuthEditAbove bottomPanel = PanelAuthEditAbove.getInstance(); 

			if(roleName.equals(japanese?"---���[����I�����ĉ�����---":"---Select Role---") || tableName.equals(japanese ? "---�e�[�u����I�����ĉ�����---" : "---Select Table---")) {
				bottomPanel.relocate(null, null);
			}
			else {
				Role role = RoleManager.getInstance().getRoleByName(roleName);
				SuperTable table = TableManager.getInstance().getTableByName(tableName);
				bottomPanel.relocate(role, table);
			}
		}
		else {
			Debug.informError();
			Debug.error("�z��O�̃A�N�V�����R�}���h", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		MainFrame.repaintAndValidate();
	}
	
	
	
	

	public int getPanelWidth() {
		return panelWidth;
	}

	
	public int getPanelHeight() {
		return panelHeight;
	}
	
	public static PanelAuthEditBottom getInstance() {
		return PanelAuthEditBottom.obj;
	}
	public static void updateInstance(PanelAuthEditBottom newObject) {
		PanelAuthEditBottom.obj = newObject;
	}

}
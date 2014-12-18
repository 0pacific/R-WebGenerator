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

	// �w�i�F
	public static Color backGroundColor = Color.WHITE;

	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	// �e�[�u���ꗗ�\���i�[�����X�N���[���y�C���̉���
	public static final int SCROLL_PANE_WIDTH = 300;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static Panel_RoleEdit_Above obj = new Panel_RoleEdit_Above();
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private Panel_RoleEdit_Above() {
		roleTableColumnNames = GeneratorProperty.japanese() ? new String[]{"���[����", "�A�J�E���g�e�[�u��"} : new String[]{"Role Name", "Account Table"};
		
		// ���C�A�E�g
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		
		// ���[���e�[�u���̏������i�����ł� refreshRoleTable() �͏������ɂ�����j
		roleTable = new JTable();
		refreshRoleTable();

		// ���[���e�[�u�����i�[����X�N���[���y�C��
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
	 * ���[���e�[�u���̓��e���X�V����i���[���̒ǉ��E�폜���ȂǂɎg���j
	 */
	public void refreshRoleTable() {
		RoleManager roleManager = RoleManager.getInstance();
		int roleNum = roleManager.getRoleNum();

		String[][] roleTableValues = new String[roleNum][Panel_RoleEdit_Above.roleTableColumnNames.length];
		for(int i=0; i<roleNum; i++) {
			Role role = roleManager.getRole(i);

			String roleName = role.getRoleName();
			roleTableValues[i][0] = roleName;
			
			// �A�J�E���g�e�[�u�����̗��̒l�����߂�
			String accountTableName = null;
			if(role instanceof NormalRole) {	// �m�[�}�����[��
				NormalRole normalRole = (NormalRole)role;

				// �A�J�E���g�e�[�u�����w�肳��Ă���΂��̖��O�A���Ȃ����null�̂܂�
				if(normalRole.accountTableDefined()) {
					AccountTable at = normalRole.getAccountTable();
					accountTableName = at.getTableName();
				}
			}
			else {	// �Q�X�g���[���̏ꍇ��null�i�Ȃ̂ł��̂܂܁j
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

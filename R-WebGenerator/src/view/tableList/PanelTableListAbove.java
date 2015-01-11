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
	// �p�l���T�C�Y
	public final int panelHeight = 150;
	
	// �w�i�F
	public static Color backGroundColor = Color.WHITE;

	public JTable dataTableList;
	public static String[] dataTableListColumnNames;

	public JTable accountTableList;
	public static String[] accountTableListColumnNames;
	public static final int colWidthAt0 = 130;
	public static final int colWidthAt1 = 90;
	public static final int colWidthAt2 = 300;

	// �A�J�E���g�e�[�u���ǉ��{�^��
	public JButton btnAtAdd;
	
	public SerializableSpringLayout springLayout;
	
	/*
	 * �C���X�^���X�ϐ��͍Ō�I
	 */
	private static PanelTableListAbove obj = new PanelTableListAbove();

	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private PanelTableListAbove() {
		boolean japanese = GeneratorProperty.japanese();
		dataTableListColumnNames = japanese ?
									new String[]{"�f�[�^�e�[�u����", "�t�B�[���h��"} :
									new String[]{"Data Table Name", "Fields Quantity"};
		accountTableListColumnNames = japanese ?
										new String[]{"�A�J�E���g�e�[�u����", "�t�B�[���h��", "���[��"} :
										new String[]{"Account Table Name", "Fields Quantity", "Role"};
		// �p�l���̃��C�A�E�g
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �f�[�^�e�[�u���ꗗ�e�[�u���̓��e���X�V����i�f�[�^�e�[�u���̒ǉ��E�폜���ȂǂɎg���j
	 */
	public void refreshDataTableList() {
		TableManager tableManager = TableManager.getInstance();

		// �l������V���Ɍv�Z
		int dataTableNum = tableManager.getDataTableNum();
		String[][] dataTableListValues = new String[dataTableNum][PanelTableListAbove.dataTableListColumnNames.length];
		for(int i=0; i<dataTableNum; i++) {
			DataTable dataTable = tableManager.getDataTable(i);

			// ���ځu�f�[�^�e�[�u�����v
			String dataTableName = dataTable.getTableName();
			dataTableListValues[i][0] = dataTableName;

			// ���ځu�t�B�[���h���v
			int fieldNum = dataTable.getFieldNum();
			int abFieldNum = dataTable.getAllFieldNum() - fieldNum;
			dataTableListValues[i][1] = Integer.toString(fieldNum) + "(" +Integer.toString(abFieldNum) + ")";
		}
		
		// �V���ȃ��f�����Z�b�g
		DefaultTableModel dataTableListModel
	    	= new DefaultTableModel(dataTableListValues, PanelTableListAbove.dataTableListColumnNames);
		dataTableList.setModel(dataTableListModel);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �A�J�E���g�e�[�u���ꗗ�e�[�u���̓��e���X�V����i�A�J�E���g�e�[�u���̒ǉ��E�폜���ȂǂɎg���j
	 */
	public void refreshAccountTableList() {
		TableManager tableManager = TableManager.getInstance();

		// �l������V���Ɍv�Z
		int accountTableNum = tableManager.getAccountTableNum();
		String[][] accountTableListValues = new String[accountTableNum][PanelTableListAbove.accountTableListColumnNames.length];
		for(int i=0; i<accountTableNum; i++) {
			Debug.out(i + "�Ԗڂ̃A�J�E���g�e�[�u���̍s���o�͂��܂�");

			AccountTable accountTable = tableManager.getAccountTable(i);

			// ���ځu�A�J�E���g�e�[�u�����v
			String accountTableName = accountTable.getTableName();
			accountTableListValues[i][0] = accountTableName;
			Debug.out("�A�J�E���g�e�[�u���� : " + accountTableName);

			
			// ���ځu�t�B�[���h���v
			int fieldNum = accountTable.getFieldNum();
			int abFieldNum = accountTable.getAllFieldNum() - fieldNum;
			accountTableListValues[i][1] = Integer.toString(fieldNum) + "(" +Integer.toString(abFieldNum) + ")";

			// �A�J�E���g�e�[�u���ɑΉ�����e���[���̖��O
			int roleNum = accountTable.getNormalRoleNum();
			String roleNames = "";
			for(int j=0; j<roleNum; j++) {
				Role role = accountTable.getNormalRole(j);
				roleNames = roleNames + role.getRoleName() + "�A";
			}
			roleNames = roleNames.substring(0, roleNames.length()-1);
			accountTableListValues[i][2] = roleNames;

			Debug.out(i + "�Ԗڂ̃A�J�E���g�e�[�u���̍s���o�͂��܂���");
		}
		
		// �V���ȃ��f�����Z�b�g
		DefaultTableModel accountTableListModel
	    	= new DefaultTableModel(accountTableListValues, PanelTableListAbove.accountTableListColumnNames);
		accountTableList.setModel(accountTableListModel);

		// �J�����̕�����
		DefaultTableColumnModel atColumnModel = (DefaultTableColumnModel)accountTableList.getColumnModel();
		atColumnModel.getColumn(0).setPreferredWidth(colWidthAt0);
		atColumnModel.getColumn(1).setPreferredWidth(colWidthAt1);
		atColumnModel.getColumn(2).setPreferredWidth(colWidthAt2);
	}

	
	
	
	
	
	
	public void locateComps() {
		removeAll();
		
		// �f�[�^�e�[�u���ꗗ�e�[�u���̏������i�����ł� refreshDataTableList() �͎����㏉�����j
		dataTableList = new JTable();
		refreshDataTableList();

		// �f�[�^�e�[�u���ꗗ�e�[�u�����i�[���� JScrollPane
		JScrollPane sp_dataTableList = new JScrollPane(dataTableList);
		int widthDt = 250;
		sp_dataTableList.setPreferredSize(new Dimension(widthDt, this.panelHeight-40));
		springLayout.putConstraint(SpringLayout.NORTH, sp_dataTableList, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, sp_dataTableList, 20, SpringLayout.WEST, this);
		add(sp_dataTableList);

		// �A�J�E���g�e�[�u���ꗗ�iJTable�j�������i�����ł� refreshAccountTableList() �͎����㏉�����j
		accountTableList = new JTable();
		refreshAccountTableList();
		
		// �A�J�E���g�e�[�u���ꗗ�e�[�u�����i�[���� JScrollPane
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

package view.tableList.fieldList;

import gui.*;
import gui.arrow.*;

import table.*;
import table.field.Field;
import tpp.*;
import view.tableList.fieldList.fieldEdit.FieldEditFrame;
import view.tableList.fieldList.fieldEdit.FieldEditPanel;
import view.transProcEdit.serviceArgsWindow.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import property.GeneratorProperty;

import debug.Debug;

import mainFrame.MainFrame;
import utility.*;

/*
 * SUMMARY :
 * �t�B�[���h�ꗗ��ʂ̃p�l��
 * 
 * NOTICE :
 * Singleton
 */
public class Panel_FieldList extends JPanel implements ActionListener,Serializable {
	private SuperTable table;

	public static String[] fieldTableColumnNames;
	private JTable fieldTable;

	private JButton fieldAddButton;

	public JButton fieldEditButton;

	public JButton btnFieldRemove;
	
	// ��ʑJ�ڃ{�^��
	public JButton btnTransTableList;
	public JButton btnTransRoleEdit;
	public JButton btnTransAuthEdit;
	public JButton btnTransTrans;
	
	// �w�i�F
	public static Color bgColor = Color.WHITE;

	// SpringLayout
	public SerializableSpringLayout springLayout; 
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static Panel_FieldList obj = new Panel_FieldList();
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private Panel_FieldList() {
		boolean japanese = GeneratorProperty.japanese();
		
		fieldTableColumnNames = japanese ?
									new String[]{"�t�B�[���h��", "�f�[�^�^", "�ŏ��l", "�ő�l"} :
									new String[]{"Field Name", "Data Type", "Minimum", "Maximum"};
		
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Panel_FieldList.bgColor);

		// �t�B�[���h�ҏW�{�^��
		fieldEditButton = japanese ? new JButton("�I�𒆂̃t�B�[���h��ҏW") : new JButton("Edit Field");
		fieldEditButton.addActionListener(this);
		fieldEditButton.setActionCommand("�t�B�[���h�ҏW");
		
		// ��ʑJ�ڃ{�^���̏������E���X�i�[�ǉ��E�R�}���h�Z�b�g
		btnTransTableList = japanese ? new JButton("�e�[�u���ꗗ�֖߂�") : new JButton("Go To Table List");
		btnTransRoleEdit = japanese ? new JButton("���[����`��ʂ�") : new JButton("Go To Role Definition");
		btnTransAuthEdit = japanese ? new JButton("�A�N�Z�X������`��ʂ�") : new JButton("Go To Accessibility Definition");
		btnTransTrans = japanese ? new JButton("Web�y�[�W��`��ʂ�") : new JButton("Go To Web Page Definition");
		btnTransTableList.addActionListener(this);
		btnTransRoleEdit.addActionListener(this);
		btnTransAuthEdit.addActionListener(this);
		btnTransTrans.addActionListener(this);
		btnTransTableList.setActionCommand("�J�� : �e�[�u���ꗗ���");
		btnTransRoleEdit.setActionCommand("�J�� : ���[���ҏW���");
		btnTransAuthEdit.setActionCommand("�J�� : �����ҏW���");
		btnTransTrans.setActionCommand("�J�� : �y�[�W�J�ڒ�`���");
	}

	
	
	
	/*
	 * PURPOSE :
	 * �w��e�[�u����ҏW�ł���悤�ɁA�R���|�[�l���g��z�u����
	 */
	public void locateComps(SuperTable table) {
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		this.table = table;

		// �e�L�X�g�y�C���@�ҏW�e�[�u����
		JTextPane tpTblName = new JTextPane();
		if(table instanceof DataTable) {
			String text = japanese ? "�f�[�^�e�[�u���u" + this.table.getTableName() + "�v��ҏW���ł�" : "Editing Data Table \""+this.table.getTableName()+"\".";
			tpTblName.setText(text);
		}
		else if(table instanceof AccountTable) {
			String text = japanese ? "�A�J�E���g�e�[�u���u" + this.table.getTableName() + "�v��ҏW���ł�" : "Editing Account Table \""+this.table.getTableName()+"\".";
			tpTblName.setText(text);
		}
		springLayout.putConstraint(SpringLayout.NORTH, tpTblName, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tpTblName, 20, SpringLayout.WEST, this);
		add(tpTblName);
		
		// ����̃t�B�[���h�ꗗ�e�[�u���ƁA������i�[���� JScrollPane
		fieldTable = new JTable();
		JScrollPane sp_fieldTable = new JScrollPane(fieldTable);
		int WIDTH = 600;
		int HEIGHT = 200;
		sp_fieldTable.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, sp_fieldTable, 20, SpringLayout.SOUTH, tpTblName);
		springLayout.putConstraint(SpringLayout.WEST, sp_fieldTable, 0, SpringLayout.WEST, tpTblName);
		add(sp_fieldTable);

	    // �u�V���ȃt�B�[���h��ǉ��v�{�^��
	    fieldAddButton = japanese ? new JButton("�V���ȃt�B�[���h��ǉ�") : new JButton("Add Field");
		fieldAddButton.addActionListener(this);
		fieldAddButton.setActionCommand("�t�B�[���h�ǉ�");
		springLayout.putConstraint(SpringLayout.NORTH, fieldAddButton, 0, SpringLayout.NORTH, sp_fieldTable);
		springLayout.putConstraint(SpringLayout.WEST, fieldAddButton, 40, SpringLayout.EAST, sp_fieldTable);
	    add(fieldAddButton);
		
	    // �u�I�𒆂̃t�B�[���h��ҏW�v�{�^��
		springLayout.putConstraint(SpringLayout.NORTH, fieldEditButton, 20, SpringLayout.SOUTH, fieldAddButton);
		springLayout.putConstraint(SpringLayout.WEST, fieldEditButton, 0, SpringLayout.WEST, fieldAddButton);
	    add(fieldEditButton);

	    // �u�I�𒆂̃t�B�[���h���폜�v�{�^��
	    btnFieldRemove = japanese ? new JButton("�I�𒆂̃t�B�[���h���폜") : new JButton("Delete Field");
	    btnFieldRemove.addActionListener(this);
	    btnFieldRemove.setActionCommand("�t�B�[���h�폜");
	    Slpc.put(springLayout, "N", btnFieldRemove, "S", fieldEditButton, 20);
	    Slpc.put(springLayout, "W", btnFieldRemove, "W", fieldEditButton, 0);
	    add(btnFieldRemove);

	    // �t�B�[���h�ꗗ�X�V
		refreshFieldTable();

		
		// ��ʑJ�ڃ{�^��
		springLayout.putConstraint(SpringLayout.SOUTH, btnTransTableList, -20, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnTransTableList, 20, SpringLayout.WEST, this);
		add(btnTransTableList);

		springLayout.putConstraint(SpringLayout.NORTH, btnTransRoleEdit, 0, SpringLayout.NORTH, btnTransTableList);
		springLayout.putConstraint(SpringLayout.WEST, btnTransRoleEdit, 20, SpringLayout.EAST, btnTransTableList);
		add(btnTransRoleEdit);

		springLayout.putConstraint(SpringLayout.NORTH, btnTransAuthEdit, 0, SpringLayout.NORTH, btnTransRoleEdit);
		springLayout.putConstraint(SpringLayout.WEST, btnTransAuthEdit, 20, SpringLayout.EAST, btnTransRoleEdit);
		add(btnTransAuthEdit);

		springLayout.putConstraint(SpringLayout.NORTH, btnTransTrans, 0, SpringLayout.NORTH, btnTransAuthEdit);
		springLayout.putConstraint(SpringLayout.WEST, btnTransTrans, 20, SpringLayout.EAST, btnTransAuthEdit);
		add(btnTransTrans);
	}
	
	
	
	/*
	 * PURPOSE :
	 * �t�B�[���h�e�[�u���̓��e���X�V����i�t�B�[���h�̒ǉ��E�폜���ȂǂɎg���j
	 */
	public void refreshFieldTable() {
		boolean japanese = GeneratorProperty.japanese();
		
		// �l������V���Ɍv�Z
		int fieldNum = table.getFieldNum();
		String[][] fieldTableValues = new String[fieldNum][Panel_FieldList.fieldTableColumnNames.length];
		for(int i=0; i<fieldNum; i++) {
			Field field = table.getField(i);
			
			// ���ځu�t�B�[���h���v
			String fieldName = field.getFieldName();
			fieldTableValues[i][0] = fieldName;

			String CHAR = japanese?"��":" length";
			
			// ���ځu�f�[�^�^�v
			// INT
			if(field.dataType.equals(Field.DATATYPE_INT)) {
				fieldTableValues[i][1] = japanese ? "����" : "INTEGER";
				fieldTableValues[i][2] = Integer.toString(field.getMin());
				fieldTableValues[i][3] = Integer.toString(field.getMax());
			}
			// VARCHAR
			else if(field.dataType.equals(Field.DATATYPE_VARCHAR)) {
				fieldTableValues[i][1] = japanese ? "������" : "STRING";
				fieldTableValues[i][2] = Integer.toString(field.getMin()) + CHAR;
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + CHAR;
			}
			// DATETIME
			else if(field.dataType.equals(Field.DATATYPE_DATETIME)) {
				fieldTableValues[i][1] = japanese ? "�N�����E�����b" : "DATE&TIME";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// DATE
			else if(field.dataType.equals(Field.DATATYPE_DATE)) {
				fieldTableValues[i][1] = japanese ? "�N����" : "DATE";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// TIME
			else if(field.dataType.equals(Field.DATATYPE_TIME)) {
				fieldTableValues[i][1] = japanese ? "�����b" : "TIME";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// FILE
			else if(field.dataType.equals(Field.DATATYPE_FILE)) {
				fieldTableValues[i][1] = japanese ? "�t�@�C��" : "FILE";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + "KB";
			}
			// MAIL
			else if(field.dataType.equals(Field.DATATYPE_MAIL)) {
				fieldTableValues[i][1] = japanese ? "���[���A�h���X" : "MAIL";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// MAIL_AUTH
			else if(field.dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
				fieldTableValues[i][1] = japanese ? "���[���A�h���X�i�F�ؗp�j" : "MAIL(for identification)";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			// USERID
			else if(field.dataType.equals(Field.DATATYPE_USERID)) {
				fieldTableValues[i][1] = japanese ? "���[�UID" : "USER ID";
				fieldTableValues[i][2] = Integer.toString(field.getMin()) + CHAR;
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + CHAR;
			}
			// PASSWORD
			else if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
				fieldTableValues[i][1] = japanese ? "�p�X���[�h" : "PASSWORD";
				fieldTableValues[i][2] = Integer.toString(field.getMin()) + CHAR;
				fieldTableValues[i][3] = Integer.toString(field.getMax()) + CHAR;
			}
			// ROLE_NAME
			else if(field.dataType.equals(Field.DATATYPE_ROLE_NAME)) {
				fieldTableValues[i][1] = japanese ? "���[����" : "ROLE NAME";
				fieldTableValues[i][2] = "-----";
				fieldTableValues[i][3] = "-----";
			}
			else {
				Debug.error("�z��O�̃f�[�^�^�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
		
		// �V���ȃ��f�����Z�b�g
		DefaultTableModel fieldTableModel
	    	= new DefaultTableModel(fieldTableValues, Panel_FieldList.fieldTableColumnNames);
		fieldTable.setModel(fieldTableModel);

		MainFrame.repaintAndValidate();
	}

	/*
	 * PURPOSE :
	 * ActionEvent �n���h��
	 */
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		// �u�V���ȃt�B�[���h��ǉ��v�{�^���N���b�N
		if(cmd.equals("�t�B�[���h�ǉ�")) {
			FieldEditFrame frame = FieldEditFrame.getInstance();
			frame.setVisible(true);
			frame.setEnabled(true);
			FieldEditPanel.getInstance().openAddMode();

			MainFrame.getInstance().setEnabled(false);
		}
		else if(cmd.equals("�t�B�[���h�ҏW")) {
			int selFieldIdx = fieldTable.getSelectedRow();
			Field selField = table.getField(selFieldIdx);

			FieldEditFrame frame = FieldEditFrame.getInstance();
			frame.setVisible(true);
			frame.setEnabled(true);
			FieldEditPanel.getInstance().openEditMode(selField);

			MainFrame.getInstance().setEnabled(false);
		}
		else if(cmd.equals("�t�B�[���h�폜")) {
			int selectedFieldIdx = fieldTable.getSelectedRow();
			Field selectedField = table.getField(selectedFieldIdx);

			// ���[�UID�A�F�؃��[���A�h���X�A�p�X���[�h�̃t�B�[���h�͍폜�ł��Ȃ�
			if(selectedField.dataType.equals(Field.DATATYPE_USERID)) {
				String msg = japanese ?
								"���[�UID�̃t�B�[���h�͍폜�ł��܂���\n�i���[���A�h���X�ɐ؂�ւ��邱�Ƃ͂ł��܂��j" :
								"Can't delete USER ID field.\n(You can change its data type to MAIL for identification)";
				JOptionPane.showMessageDialog(this, msg);
				return;
			}
			else if(selectedField.dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
				String msg = japanese ?
						"�F�ؗp���[���A�h���X�̃t�B�[���h�͍폜�ł��܂���\n�i���[�UID�ɐ؂�ւ��邱�Ƃ͂ł��܂��j" :
						"Can't delete MAIL field for identification.\n(You can change its data type to USER ID)";
				JOptionPane.showMessageDialog(this, msg);
				return;
			}
			else if(selectedField.dataType.equals(Field.DATATYPE_PASSWORD)) {
				String msg = japanese ?
						"�p�X���[�h�̃t�B�[���h�͍폜�ł��܂���" :
						"Can't delete PASSWORD field";
				JOptionPane.showMessageDialog(this, msg);
				return;
			}

			String msg1 = japanese ? "�폜���Ă���낵���ł����H" : "Delete selecting field?";
			int confirm = JOptionPane.showConfirmDialog(this, msg1, "�t�B�[���h�̍폜", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// �L�����Z��
				return;
			}
			
			this.table.removeField(selectedField);
			this.refreshFieldTable();
		}
		else if(cmd.equals("�J�� : �e�[�u���ꗗ���")) {
			MainFrame.getInstance().shiftToTableList();
		}
		else if(cmd.equals("�J�� : ���[���ҏW���")) {
			MainFrame.getInstance().shiftToRoleEdit();
		}
		else if(cmd.equals("�J�� : �����ҏW���")) {
			MainFrame.getInstance().shiftToAuthEdit();
		}
		else if(cmd.equals("�J�� : �y�[�W�J�ڒ�`���")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}
	}
	
	public SuperTable getTable() {
		return table;
	}

	public void setTable(SuperTable table) {
		this.table = table;
	}
	
	public static Panel_FieldList getInstance() {
		return Panel_FieldList.obj;
	}
	
	
	public static void updateInstance(Panel_FieldList newObject) {
		Panel_FieldList.obj = newObject;
	}

}
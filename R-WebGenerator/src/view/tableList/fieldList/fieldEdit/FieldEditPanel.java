package view.tableList.fieldList.fieldEdit;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

import table.*;
import table.field.Field;
import utility.Slpc;
import view.tableList.fieldList.Panel_FieldList;
import utility.*;

/*
 * Singleton
 */
public class FieldEditPanel extends JPanel implements ItemListener,ActionListener,Serializable {
	// �ǉ����[�h�E�ҏW���[�h
	public int modeAddEdit;
	public static final int MODE_ADD = 0;
	public static final int MODE_EDIT = 1;
	
	// �ҏW���̃t�B�[���h�i�ҏW���[�h�ł̂ݎg�p�����j
	public Field editingField;
	
	// �e�L�X�g�y�C���u�t�B�[���h���F�v
	JTextPane textPaneFieldName;
	// �e�L�X�g�t�B�[���h�i�t�B�[���h�����́j
	JTextField textFieldFieldName;
	
	// �e�L�X�g�y�C���u�f�[�^�̃^�C�v�F�v
	public JTextPane textPaneDataTypeSelect;
	// �R���{�{�b�N�X�i�f�[�^�^�C�v�I���j
	public JComboBox comboBoxDataTypeSelect;
	public String[] valuesComboBoxDataTypeSelect;
	public static String[] DATA_TYPE_LIST_1;		// �f�[�^�e�[�u���̃t�B�[���h
	public static String[] DATA_TYPE_LIST_2;		// �A�J�E���g�e�[�u���̔F�؃t�B�[���h
	public static String[] DATA_TYPE_LIST_3;		// �A�J�E���g�e�[�u���̃p�X���[�h�t�B�[���h
	public static String[] DATA_TYPE_LIST_4;		// �A�J�E���g�e�[�u���̂��̑��̃t�B�[���h
	
	// �ŏ��l
	public JTextPane tpMin = new JTextPane();
	public JTextField tfMin = new JTextField();

	// �ő�l
	public JTextPane tpMax = new JTextPane();
	public JTextField tfMax = new JTextField();

	// �t�@�C���ő�KB
	public JTextPane textPaneFileSizeMaxKb = new JTextPane();
	public JTextField textFieldFileSizeMaxKb = new JTextField();
	
	// �ǉ����s�{�^��
	public JButton btnAdd;

	// �ҏW���s�{�^��
	public JButton btnEdit;

	// �L�����Z���{�^��
	public JButton btnCancel;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;

	// �w�i�F
	public static final Color BG_COLOR = Color.WHITE;

	// ���A�ǂ̃f�[�^�^�C�v�̒ǉ��i�܂��͕ҏW�j�t�H�[����\�����Ă��邩�iField�N���X��static�萔�����j
	public String currentDataType;
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static FieldEditPanel obj = new FieldEditPanel();

	
	
	
	
	
	private FieldEditPanel() {
		boolean japanese = GeneratorProperty.japanese();
		
		FieldEditPanel.DATA_TYPE_LIST_1 = new String[]{
			Field.getDataTypeExpression(Field.DATATYPE_INT),
			Field.getDataTypeExpression(Field.DATATYPE_VARCHAR),
			Field.getDataTypeExpression(Field.DATATYPE_TEXT),
			Field.getDataTypeExpression(Field.DATATYPE_DATETIME),
			Field.getDataTypeExpression(Field.DATATYPE_DATE),
			Field.getDataTypeExpression(Field.DATATYPE_TIME),
			Field.getDataTypeExpression(Field.DATATYPE_FILE),
			Field.getDataTypeExpression(Field.DATATYPE_MAIL)
		};
		FieldEditPanel.DATA_TYPE_LIST_2 = new String[]{
			Field.getDataTypeExpression(Field.DATATYPE_USERID),
			Field.getDataTypeExpression(Field.DATATYPE_MAIL_AUTH)
		};
		FieldEditPanel.DATA_TYPE_LIST_3 = new String[]{
			Field.getDataTypeExpression(Field.DATATYPE_PASSWORD)
		};
		FieldEditPanel.DATA_TYPE_LIST_1 = new String[]{
				Field.getDataTypeExpression(Field.DATATYPE_INT),
				Field.getDataTypeExpression(Field.DATATYPE_VARCHAR),
				Field.getDataTypeExpression(Field.DATATYPE_TEXT),
				Field.getDataTypeExpression(Field.DATATYPE_DATETIME),
				Field.getDataTypeExpression(Field.DATATYPE_DATE),
				Field.getDataTypeExpression(Field.DATATYPE_TIME),
				Field.getDataTypeExpression(Field.DATATYPE_FILE),
				Field.getDataTypeExpression(Field.DATATYPE_MAIL),
				Field.getDataTypeExpression(Field.DATATYPE_ROLE_NAME)
		};
		
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		int PADD_LEFT = 20;

		// �e�L�X�g�y�C���@�u�t�B�[���h���F�v
		textPaneFieldName = new JTextPane();
		textPaneFieldName.setText(japanese ? "�t�B�[���h���F" : "Field Name:");
		textPaneFieldName.setEditable(false);

		// �e�L�X�g�t�B�[���h�@�t�B�[���h��
		textFieldFieldName = new JTextField();

		// �f�[�^�^�C�v�I��
		textPaneDataTypeSelect = new JTextPane();
		textPaneDataTypeSelect.setText(japanese ? "�f�[�^�̃^�C�v�F" : "Data Type :");
		textPaneDataTypeSelect.setEditable(false);
		comboBoxDataTypeSelect = new JComboBox();
		comboBoxDataTypeSelect.addItemListener(this);
		
		btnAdd = new JButton(japanese ? "�t�B�[���h��ǉ�" : "Add Field");
		btnAdd.addActionListener(this);
		btnAdd.setActionCommand("�t�B�[���h�ǉ�");

		btnEdit = new JButton(japanese ? "�ȏ�̓��e�ŕҏW" : "   OK   ");
		btnEdit.addActionListener(this);
		btnEdit.setActionCommand("�t�B�[���h�ҏW");

		btnCancel = new JButton(japanese ? "�L�����Z��" : "Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");
	}

	
	
	public void locateFieldNameComps(int PADD_LEFT) {
		int PADD_TOP = 20;
		springLayout.putConstraint(SpringLayout.NORTH, textPaneFieldName, PADD_TOP, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, textPaneFieldName, PADD_LEFT, SpringLayout.WEST, this);
		add(textPaneFieldName);

		int WIDTH = 100;
		textFieldFieldName.setPreferredSize(new Dimension(WIDTH, 20));
		springLayout.putConstraint(SpringLayout.NORTH, textFieldFieldName, 0, SpringLayout.SOUTH, textPaneFieldName);
		springLayout.putConstraint(SpringLayout.WEST, textFieldFieldName, PADD_LEFT, SpringLayout.WEST, this);
		add(textFieldFieldName);
	}
	
	
	public void removeAllComps() {
		removeAll();
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �t�B�[���h�ꗗ��ʂŁA�t�B�[���h�ǉ��{�^���ɂ��t�B�[���h�ҏW�t���[�����N�������Ƃ��Ɏ��s�����
	 */
	public void openAddMode() {
		removeAllComps();
		
		modeAddEdit = FieldEditPanel.MODE_ADD;
		
		// �t�B�[���h���̃e�L�X�g�y�C���E�e�L�X�g�t�B�[���h
		locateFieldNameComps(20);

		textFieldFieldName.setText("");
		
		// �ҏW���̃e�[�u�����f�[�^�e�[�u�����A�J�E���g�e�[�u�����ɉ����A�f�[�^�^�C�v�̃��j���[������
		SuperTable table = Panel_FieldList.getInstance().getTable();
		String tableClassName = table.getClass().getSimpleName();
		if(table instanceof DataTable) {
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_1;
		}
		else if(table instanceof AccountTable) {
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_4;
		}
		else {
			Debug.error("�G���[", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		// �f�[�^�^�I���R���{�{�b�N�X�z�u�AINT�^��I��������Ԃɂ���
		locateDataTypeComps(true);
		locateCompsInt(null);

		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	public void openEditMode(Field field) {
		// �ҏW���[�h�Ɉڍs
		modeAddEdit = FieldEditPanel.MODE_EDIT;

		this.editingField = field;
		
		// �S�R���|�[�l���g����
		removeAllComps();
		
		
		// �t�B�[���h���̃e�L�X�g�y�C���E�e�L�X�g�t�B�[���h
		locateFieldNameComps(20);
		textFieldFieldName.setText(field.getFieldName());
		

		// �ҏW���̃e�[�u�����f�[�^�e�[�u�����A�J�E���g�e�[�u�����ɂ���āA�R���{�{�b�N�X�̃��j���[������
		SuperTable table = Panel_FieldList.getInstance().getTable();
		String tableClassName = table.getClass().getSimpleName();
		String dataType = field.dataType;
		boolean dataTypeG1 = 	tableClassName.equals("DataTable") && (
								dataType.equals(Field.DATATYPE_INT)
								|| dataType.equals(Field.DATATYPE_VARCHAR)
								|| dataType.equals(Field.DATATYPE_DATETIME)
								|| dataType.equals(Field.DATATYPE_DATE)
								|| dataType.equals(Field.DATATYPE_TIME)
								|| dataType.equals(Field.DATATYPE_MAIL)
								|| dataType.equals(Field.DATATYPE_FILE)
								);
		boolean dataTypeG2 = 	tableClassName.equals("AccountTable") && (
								dataType.equals(Field.DATATYPE_MAIL_AUTH)
								|| dataType.equals(Field.DATATYPE_USERID)
								);
		boolean dataTypeG3 = 	tableClassName.equals("AccountTable") && (
								dataType.equals(Field.DATATYPE_PASSWORD)
								);
		boolean dataTypeG4 = 	tableClassName.equals("AccountTable") && (
								dataType.equals(Field.DATATYPE_INT)
								|| dataType.equals(Field.DATATYPE_VARCHAR)
								|| dataType.equals(Field.DATATYPE_DATETIME)
								|| dataType.equals(Field.DATATYPE_DATE)
								|| dataType.equals(Field.DATATYPE_FILE)
								|| dataType.equals(Field.DATATYPE_TIME)
								|| dataType.equals(Field.DATATYPE_MAIL)
								);

		if(dataTypeG1) {	// �f�[�^�e�[�u���̃t�B�[���h��ҏW
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_1;
		}
		else if(dataTypeG2) {	// ���[�UID or �F�؃��[���A�h���X�@�̃t�B�[���h��ҏW
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_2;
		}
		else if(dataTypeG3) {	// �p�X���[�h�t�B�[���h��ҏW
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_3;
		}
		else if(dataTypeG4) {	// �A�J�E���g�e�[�u���̂��̑��̃t�B�[���h��ҏW
			valuesComboBoxDataTypeSelect = FieldEditPanel.DATA_TYPE_LIST_4;
		}
		locateDataTypeComboOnEditStart(field);

		
		// �ҏW���悤�Ƃ��Ă���t�B�[���h�́A���݂̃f�[�^�^�ɑΉ������t�H�[����\��
		if(dataType.equals(Field.DATATYPE_INT)) {
			locateCompsInt(field);
		}
		else if(dataType.equals(Field.DATATYPE_VARCHAR)) {
			locateCompsVarchar(field);
		}
		else if(dataType.equals(Field.DATATYPE_TEXT)) {
			locateCompsText(field);
		}
		else if(dataType.equals(Field.DATATYPE_DATETIME)) {
			locateCompsDatetime(field);
		}
		else if(dataType.equals(Field.DATATYPE_DATE)) {
			locateCompsDate(field);
		}
		else if(dataType.equals(Field.DATATYPE_TIME)) {
			locateCompsTime(field);
		}
		else if(dataType.equals(Field.DATATYPE_FILE)) {
			locateCompsFile(field);
		}
		else if(dataType.equals(Field.DATATYPE_MAIL)) {
			locateCompsMail(field);
		}
		else if(dataType.equals(Field.DATATYPE_USERID)) {
			locateCompsUserid(field);
		}
		else if(dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
			locateCompsMailAuth(field);
		}
		else if(dataType.equals(Field.DATATYPE_PASSWORD)) {
			locateCompsPassword(field);
		}
		else if(dataType.equals(Field.DATATYPE_ROLE_NAME)) {
			locateCompsRoleName(field);
		}
		else {
			Debug.error("�ҏW���悤�Ƃ��Ă���t�B�[���h�̃f�[�^�^���z��O�ł��B");
		}
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	public void locateCompsInt(Field intField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_INT;
		
		if(intField!=null && intField.dataType!=Field.DATATYPE_INT) {
			Debug.error("FieldEditPanel locateCompsInt() : INT�^�̃t�B�[���h�ł͂���܂���");
		}
		
		springLayout.putConstraint(SpringLayout.NORTH, tpMin, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tpMin, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tpMin);
		tpMin.setText(japanese ? "�ŏ��l�F" : "Minimum:");
		
		if(intField==null) {
			int MIN = 0;
			tfMin.setText(Integer.toString(MIN));
		}
		else {
			int MIN = intField.getMin();
			tfMin.setText(Integer.toString(MIN));
		}
		int WIDTH = 100;
		tfMin.setPreferredSize(new Dimension(WIDTH, 20));
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		add(tfMin);
		
		JTextPane tpMax = new JTextPane();
		tpMax.setText(japanese ? "�ő�l�F" : "Maximum:");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		tfMax = new JTextField();
		if(intField==null) {
			int MAX = 65535;
			tfMax.setText(Integer.toString(MAX));
		}
		else {
			int MAX = intField.getMax();
			tfMax.setText(Integer.toString(MAX));
		}
		WIDTH = 100;
		tfMax.setPreferredSize(new Dimension(WIDTH, 20));
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		add(tfMax);

		locateButtons(tfMax);
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	
	public void locateCompsVarchar(Field varcharField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_VARCHAR;
		
		
		// �e�L�X�g�y�C��
		tpMin.setText(japanese ? "�ŏ��������F" : "Minimum Length:");
		springLayout.putConstraint(SpringLayout.NORTH, tpMin, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tpMin, 20, SpringLayout.WEST, this);
		add(tpMin);
		
		
		// �e�L�X�g�t�B�[���h�i�ŏ��l���́j
		if(varcharField==null)
			tfMin.setText(Integer.toString(0));
		else
			tfMin.setText(Integer.toString(varcharField.getMin()));
		int WIDTH = 100;
		int HEIGHT = 20;
		tfMin.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		add(tfMin);
		
		
		// �e�L�X�g�y�C��
		tpMax.setText(japanese ? "�ő啶�����F" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		
		// �e�L�X�g�t�B�[���h�i�ő�l���́j
		if(varcharField==null)
			tfMax.setText(Integer.toString(255));
		else
			tfMax.setText(Integer.toString(varcharField.getMax()));
		tfMax.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		add(tfMax);


		// �ǉ��ior�ҏW�j�{�^���ƃL�����Z���{�^���z�u
		locateButtons(tfMax);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void locateCompsText(Field textField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_VARCHAR;
		
		
		// �e�L�X�g�y�C��
		tpMin.setText(japanese ? "�ŏ��������F" : "Minimum Length:");
		springLayout.putConstraint(SpringLayout.NORTH, tpMin, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tpMin, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tpMin);
		
		
		// �e�L�X�g�t�B�[���h�i�ŏ��l���́j
		if(textField==null)
			tfMin.setText(Integer.toString(0));
		else
			tfMin.setText(Integer.toString(textField.getMin()));
		int WIDTH = 100;
		int HEIGHT = 20;
		tfMin.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		add(tfMin);
		
		
		// �e�L�X�g�y�C��
		tpMax.setText(japanese ? "�ő啶�����F" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		
		// �e�L�X�g�t�B�[���h�i�ő�l���́j
		if(textField==null)
			tfMax.setText(Integer.toString(65535));
		else
			tfMax.setText(Integer.toString(textField.getMax()));
		tfMax.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		add(tfMax);


		// �ǉ��ior�ҏW�j�{�^���ƃL�����Z���{�^���z�u
		locateButtons(tfMax);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	public void locateCompsDatetime(Field datetimeField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_DATETIME;

		
		// �e�L�X�g�y�C��
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "���̃t�B�[���h�ɂ͓��t�Ǝ����̃Z�b�g���i�[���܂��B" : "Date and time will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		
		// �ǉ��ior�ҏW�j�{�^���ƃL�����Z���{�^���z�u
		locateButtons(tp);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	public void locateCompsDate(Field dateField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_DATE;

		
		// �e�L�X�g�y�C��
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "���̃t�B�[���h�ɂ͓��t���i�[���܂��B" : "Date will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		
		// �ǉ��ior�ҏW�j�{�^���ƃL�����Z���{�^���z�u
		locateButtons(tp);
		
		
		FieldEditFrame.repaintAndValidate();
	}
	public void locateCompsTime(Field timeField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_TIME;

		
		// �e�L�X�g�y�C��
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "���̃t�B�[���h�ɂ͎������i�[���܂��B" : "Time will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		
		// �ǉ��ior�ҏW�j�{�^���ƃL�����Z���{�^���z�u
		locateButtons(tp);
		
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	public void locateCompsMail(Field mailField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_MAIL;
		
		if(mailField!=null && mailField.dataType!=Field.DATATYPE_MAIL) {
			Debug.error("FieldEditPanel locateCompsMail() : MAIL�^�̃t�B�[���h�ł͂���܂���");
		}
		
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "���̃t�B�[���h�ɂ̓��[���A�h���X���i�[���܂��B" : "Mail address will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		locateButtons(tp);
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	public void locateCompsFile(Field fileField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_FILE;

		if(fileField!=null && !fileField.dataType.equals(Field.DATATYPE_FILE)) {
			Debug.error("FILE�^�t�B�[���h��ҏW���悤�Ƃ��Ă���͂��ł����A�Ώۂ̃t�B�[���h��FILE�^�ł͂���܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}

		// �e�L�X�g�y�C��������
		textPaneFileSizeMaxKb = new JTextPane();
		textPaneFileSizeMaxKb.setText(japanese ? "�t�@�C���T�C�Y����iKB�j�F" : "Maximum File Size(KB):");
		textPaneFileSizeMaxKb.setEditable(false);
		// �e�R���|�[�l���g�z�u
		springLayout.putConstraint(SpringLayout.NORTH, textPaneFileSizeMaxKb, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, textPaneFileSizeMaxKb, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(textPaneFileSizeMaxKb);

		// �e�L�X�g�t�B�[���h�i�t�@�C���T�C�Y������́j������
		textFieldFileSizeMaxKb = new JTextField();
		if(fileField==null) {	// �ǉ���
			textFieldFileSizeMaxKb.setText("1000");
		}
		else if(fileField.dataType==Field.DATATYPE_FILE) {	// ������FILE�^�t�B�[���h�ҏW��
			Debug.out("������FILE�^�t�B�[���h��ҏW���悤�Ƃ��Ă���̂ŁA�T�C�Y�̃e�L�X�g�t�B�[���h�̒l��"+fileField.max+"�ɐݒ肵�܂�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			textFieldFileSizeMaxKb.setText(Integer.toString(fileField.max));
		}
		textFieldFileSizeMaxKb.setPreferredSize(new Dimension(100, 20));
		springLayout.putConstraint(SpringLayout.NORTH, textFieldFileSizeMaxKb, 20, SpringLayout.SOUTH, textPaneFileSizeMaxKb);
		springLayout.putConstraint(SpringLayout.WEST, textFieldFileSizeMaxKb, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(textFieldFileSizeMaxKb);

		locateButtons(textFieldFileSizeMaxKb);

		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void locateCompsUserid(Field useridField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_USERID;
		
		
		// �e�L�X�g�y�C���i�ŏ��������j�����E�Ĕz�u
		tpMin.setText(japanese ? "�ŏ��������F" : "Minimum Length:");
		Slpc.put(springLayout, "N", tpMin, "S", comboBoxDataTypeSelect, 20);
		Slpc.put(springLayout, "W", tpMin, "W", comboBoxDataTypeSelect, 0);
		add(tpMin);
		
		// �e�L�X�g�t�B�[���h�i�ŏ��������j�����E�Ĕz�u�E�l�ݒ�
		int MIN = -1;
		if(useridField==null) {	// �ǉ����̏ꍇ�A����ѕҏW���[�h�Ńf�[�^�^�C�v�I���R���{�{�b�N�X�̒l��"���[�UID"�ɂ����ꍇ
			MIN = 4;
		}
		else {	// ������USERID�^�t�B�[���h��ҏW���J�n�����Ƃ���ł���ꍇ
			MIN = useridField.getMin();
		}
		tfMin.setText(Integer.toString(MIN));
		int WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		springLayout.putConstraint(SpringLayout.EAST, tfMin, WIDTH, SpringLayout.WEST, tfMin);
		add(tfMin);
		
		// �e�L�X�g�y�C���i�ő啶�����j�����E�Ĕz�u
		tpMax.setText(japanese ? "�ő啶�����F" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		// �e�L�X�g�t�B�[���h�i�ő啶�����j�����E�Ĕz�u�E�l�ݒ�iUSERID�^�̏ꍇ�͕ҏW���[�h�������肦�Ȃ��͂��Ȃ̂ŁAuserifField��max���ݒ肳��Ă���Ƃ����O��
		int MAX = -1;
		if(useridField==null) {	// �ǉ����̏ꍇ�A����ѕҏW���[�h�Ńf�[�^�^�C�v�I���R���{�{�b�N�X�̒l��"���[�UID"�ɂ����ꍇ
			MAX = 16;
		}
		else {	// ������USERID�^�t�B�[���h��ҏW���J�n�����Ƃ���ł���ꍇ
			MAX = useridField.getMax();
		}
		tfMax.setText(Integer.toString(MAX));
		WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		springLayout.putConstraint(SpringLayout.EAST, tfMax, WIDTH, SpringLayout.WEST, tfMax);
		add(tfMax);


		locateButtons(tfMax);
		
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	public void locateCompsMailAuth(Field mailAuthField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_MAIL_AUTH;
		
		if(mailAuthField!=null && !mailAuthField.dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
			Debug.error("MAIL_AUTH�^�t�B�[���h��ҏW���悤�Ƃ��Ă���͂��ł����A�Ώۂ̃t�B�[���h��MAIL_AUTH�^�ł͂���܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "�F�ؗp���[���A�h���X���i�[����t�B�[���h�ɕύX���܂��B" : "Data type will be changed to MAIL for identification");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.EAST, tp, -20, SpringLayout.EAST, this);
		add(tp);

		
		locateButtons(tp);

		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	public void locateCompsPassword(Field passwordField) {
		boolean japanese = GeneratorProperty.japanese();
		currentDataType = Field.DATATYPE_PASSWORD;
		
		if(passwordField!=null && !passwordField.dataType.equals(Field.DATATYPE_PASSWORD)) {
			Debug.error("PASSWORD�^�̃t�B�[���h�ł͂���܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		
		// �e�L�X�g�y�C���i�ŏ��������j�����E�Ĕz�u
		tpMin.setText(japanese ? "�ŏ��������F" : "Minimum Length");
		Slpc.put(springLayout, "N", tpMin, "S", comboBoxDataTypeSelect, 20);
		Slpc.put(springLayout, "W", tpMin, "W", comboBoxDataTypeSelect, 0);
		add(tpMin);
		
		// �e�L�X�g�t�B�[���h�i�ŏ��������j�����E�Ĕz�u�E�l�ݒ�
		int MIN = -1;
		if(passwordField==null) {	// �ǉ����̏ꍇ�A����ѕҏW���[�h�Ńf�[�^�^�C�v�I���R���{�{�b�N�X�̒l��"���[�UID"�ɂ����ꍇ
			MIN = 4;
		}
		else {	// ������PASSWORD�^�t�B�[���h��ҏW���J�n�����Ƃ���ł���ꍇ
			MIN = passwordField.getMin();
		}
		tfMin.setText(Integer.toString(MIN));
		int WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMin, 0, SpringLayout.NORTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tfMin, 0, SpringLayout.EAST, tpMin);
		springLayout.putConstraint(SpringLayout.EAST, tfMin, WIDTH, SpringLayout.WEST, tfMin);
		add(tfMin);
		
		// �e�L�X�g�y�C���i�ő啶�����j�����E�Ĕz�u
		tpMax.setText(japanese ? "�ő啶�����F" : "Maximum Length");
		springLayout.putConstraint(SpringLayout.NORTH, tpMax, 20, SpringLayout.SOUTH, tpMin);
		springLayout.putConstraint(SpringLayout.WEST, tpMax, 0, SpringLayout.WEST, tpMin);
		add(tpMax);
		
		// �e�L�X�g�t�B�[���h�i�ő啶�����j�����E�Ĕz�u�E�l�ݒ�iPASSWORD�^�̏ꍇ�͕ҏW���[�h�������肦�Ȃ��͂��Ȃ̂ŁAuserifField��max���ݒ肳��Ă���Ƃ����O��
		int MAX = -1;
		if(passwordField==null) {	// �ǉ����̏ꍇ�A����ѕҏW���[�h�Ńf�[�^�^�C�v�I���R���{�{�b�N�X�̒l��"���[�UID"�ɂ����ꍇ
			MAX = 16;
		}
		else {	// ������PASSWORD�^�t�B�[���h��ҏW���J�n�����Ƃ���ł���ꍇ
			MAX = passwordField.getMax();
		}
		tfMax.setText(Integer.toString(MAX));
		WIDTH = 50;
		springLayout.putConstraint(SpringLayout.NORTH, tfMax, 0, SpringLayout.NORTH, tpMax);
		springLayout.putConstraint(SpringLayout.WEST, tfMax, 0, SpringLayout.EAST, tpMax);
		springLayout.putConstraint(SpringLayout.EAST, tfMax, WIDTH, SpringLayout.WEST, tfMax);
		add(tfMax);

		
		locateButtons(tfMax);

		
		FieldEditFrame.repaintAndValidate();
	}

	
	public void locateCompsRoleName(Field roleNameField) {
		boolean japanese = GeneratorProperty.japanese();
		
		currentDataType = Field.DATATYPE_ROLE_NAME;
		

		JTextPane tp = new JTextPane();
		tp.setText(japanese ? "���̃t�B�[���h�ɂ̓A�J�E���g�����҂̃��[�������i�[���܂��B" : "Name of account owner's role will be stored to this field.");
		springLayout.putConstraint(SpringLayout.NORTH, tp, 20, SpringLayout.SOUTH, comboBoxDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, tp, 0, SpringLayout.WEST, comboBoxDataTypeSelect);
		add(tp);

		locateButtons(tp);
		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * �f�[�^�^��I������R���{�{�b�N�X��z�u
	 */
	public void locateDataTypeComps(boolean frameFirstOpen) {
		// �G���[�`�F�b�N
		boolean check = (valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_1
							|| valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_2
							|| valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_3
							|| valuesComboBoxDataTypeSelect==FieldEditPanel.DATA_TYPE_LIST_4);
		if(!check) {
			Debug.error("FieldEditPanel locateDataTypeComps() : �R���{�{�b�N�X�̃��[�h���s���ł�");
		}

		
		// �e�L�X�g�y�C��
		springLayout.putConstraint(SpringLayout.NORTH, textPaneDataTypeSelect, 20, SpringLayout.SOUTH, textFieldFieldName);
		springLayout.putConstraint(SpringLayout.WEST, textPaneDataTypeSelect, 0, SpringLayout.WEST, textFieldFieldName);
		add(textPaneDataTypeSelect);

		
		// �R���{�{�b�N�X�@�f�[�^�^
		if(frameFirstOpen) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(valuesComboBoxDataTypeSelect);
			comboBoxDataTypeSelect.setModel(model);
		}
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxDataTypeSelect, 0, SpringLayout.SOUTH, textPaneDataTypeSelect);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxDataTypeSelect, 0, SpringLayout.WEST, textPaneDataTypeSelect);
		add(comboBoxDataTypeSelect);

		
		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * �t�B�[���h�̕ҏW���J�n����ۂɁA�f�[�^�^�C�v�I���R���{�{�b�N�X��z�u����
	 * �J�n���̃t�B�[���h�̃f�[�^�^�C�v�ɉ����āA�K�؂Ȃ��̂����ɑI�����ꂽ��ԂŔz�u�����
	 */
	public void locateDataTypeComboOnEditStart(Field field) {
		boolean japanese = GeneratorProperty.japanese();
		locateDataTypeComps(true);
		
		// �ҏW���悤�Ƃ��Ă���t�B�[���h�̌��݂̃f�[�^�^�C�v�ɉ����āA�K�؂Ȃ��̂�I����Ԃɂ���
		String dataType = field.dataType;
		comboBoxDataTypeSelect.setSelectedItem(Field.getDataTypeExpression(dataType));
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * �ǉ����s�{�^���ior�ҏW���s�{�^���j�ƃL�����Z���{�^����z�u����
	 */
	public void locateButtons(Component balometerComp) {
		// �ǉ����[�h
		if(modeAddEdit==FieldEditPanel.MODE_ADD) {
			springLayout.putConstraint(SpringLayout.NORTH, btnAdd, 20, SpringLayout.SOUTH, balometerComp);
			springLayout.putConstraint(SpringLayout.WEST, btnAdd, 20, SpringLayout.WEST, this);
			add(btnAdd);

			// �L�����Z���{�^��
			springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, btnAdd);
			springLayout.putConstraint(SpringLayout.WEST, btnCancel, 40, SpringLayout.EAST, btnAdd);
			add(btnCancel);
		}
		// �ҏW���[�h
		else {
			springLayout.putConstraint(SpringLayout.NORTH, btnEdit, 20, SpringLayout.SOUTH, balometerComp);
			springLayout.putConstraint(SpringLayout.WEST, btnEdit, 20, SpringLayout.WEST, this);
			add(btnEdit);

			// �L�����Z���{�^��
			springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, btnEdit);
			springLayout.putConstraint(SpringLayout.WEST, btnCancel, 40, SpringLayout.EAST, btnEdit);
			add(btnCancel);
		}

		FieldEditFrame.repaintAndValidate();
	}


	


	public void locate_nameComps_dataTypeComps(boolean frameFirstOpen) {
		removeAllComps();
		
		locateFieldNameComps(20);
		
		locateDataTypeComps(frameFirstOpen);
		
		FieldEditFrame.repaintAndValidate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ItemEvent���X�i�[
	 */
	public void itemStateChanged(ItemEvent e) {
		Debug.notice("�R���{�{�b�N�X�̒l���ύX����܂����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		if(e.getSource()!=comboBoxDataTypeSelect) {
			Debug.error("�f�[�^�^��I������R���{�{�b�N�X�ɕύX���������Ǝv������A�ʂ̃R���|�[�l���g���C�x���g�������݂����ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		// �t���[���I�[�v�����ł͂Ȃ��̂�false��n��
		locate_nameComps_dataTypeComps(false);
		
		
		String selType = (String)comboBoxDataTypeSelect.getSelectedItem();
		if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_INT))) {
			locateCompsInt(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_VARCHAR))) {
			locateCompsVarchar(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_TEXT))) {
			locateCompsText(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_DATETIME))) {
			locateCompsDatetime(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_DATE))) {
			locateCompsDate(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_TIME))) {
			locateCompsTime(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_FILE))) {
			locateCompsFile(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_MAIL))) {
			locateCompsMail(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_USERID))) {
			locateCompsUserid(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_MAIL_AUTH))) {
			locateCompsMailAuth(null);
		}
		else if(selType.equals(Field.getDataTypeExpression(Field.DATATYPE_ROLE_NAME))) {
			locateCompsRoleName(null);
		}
		else {
			Debug.error("�R���{�{�b�N�X�őz��O�̃f�[�^�^�C�v���I������܂����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		FieldEditFrame.repaintAndValidate();
	}

	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();
		
		if(cmd.equals("�t�B�[���h�ǉ�")) {
			Field newField = null;

			// ���͂��ꂽ�t�B�[���h���擾
			String name = textFieldFieldName.getText();
			if(name.equals("")) {
				JOptionPane.showMessageDialog(this, japanese?"�t�B�[���h�������͂���Ă��܂���":"Field name is blank.", japanese?"�x��":"Warning", JOptionPane.OK_OPTION);
				return;
			}

			
			if(currentDataType.equals(Field.DATATYPE_INT)) {
				int min = Integer.parseInt(tfMin.getText());
				int max = Integer.parseInt(tfMax.getText());
				newField = new Field(name, Field.DATATYPE_INT, min, max);
			}
			else if(currentDataType.equals(Field.DATATYPE_VARCHAR)) {
				int min = Integer.parseInt(tfMin.getText());
				int max = Integer.parseInt(tfMax.getText());
				newField = new Field(name, Field.DATATYPE_VARCHAR, min, max);
			}
			else if(currentDataType.equals(Field.DATATYPE_TEXT)) {
				int min = Integer.parseInt(tfMin.getText());
				int max = Integer.parseInt(tfMax.getText());
				newField = new Field(name, Field.DATATYPE_TEXT, min, max);
			}
			else if(currentDataType.equals(Field.DATATYPE_DATETIME)) {
				newField = new Field(name, Field.DATATYPE_DATETIME);
			}
			else if(currentDataType.equals(Field.DATATYPE_DATE)) {
				newField = new Field(name, Field.DATATYPE_DATE);
			}
			else if(currentDataType.equals(Field.DATATYPE_TIME)) {
				newField = new Field(name, Field.DATATYPE_TIME);
			}
			else if(currentDataType.equals(Field.DATATYPE_MAIL)) {
				newField = new Field(name, Field.DATATYPE_MAIL, 0, 0);
			}
			else if(currentDataType.equals(Field.DATATYPE_FILE)) {
				int fileSizeMaxKb = Integer.parseInt(this.textFieldFileSizeMaxKb.getText());
				newField = new Field(name, Field.DATATYPE_FILE, 0, fileSizeMaxKb);
			}
			else if(currentDataType.equals(Field.DATATYPE_ROLE_NAME)) {
				newField = new Field(name, Field.DATATYPE_ROLE_NAME);
			}
			else {
				Debug.error("�t�B�[���h�̒ǉ������s���悤�Ƃ��܂������A�z��O�̃f�[�^�^�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			SuperTable table = Panel_FieldList.getInstance().getTable();
			table.addField(newField);
		}
		else if(cmd.equals("�t�B�[���h�ҏW")) {
			// ���͂��ꂽ�t�B�[���h����V���ȃt�B�[���h���ɐݒ�
			String newName = textFieldFieldName.getText();
			if(newName.equals("")) {
				JOptionPane.showMessageDialog(this, japanese?"�t�B�[���h�������͂���Ă��܂���":"Field name is blank.", japanese?"�x��":"Warning", JOptionPane.OK_OPTION);
				return;
			}
			editingField.name = newName;
			

			// INT
			if(currentDataType.equals(Field.DATATYPE_INT)) {
				editingField.dataType = Field.DATATYPE_INT;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// VARCHAR
			else if(currentDataType.equals(Field.DATATYPE_VARCHAR)) {
				editingField.dataType = Field.DATATYPE_VARCHAR;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// TEXT
			else if(currentDataType.equals(Field.DATATYPE_TEXT)) {
				editingField.dataType = Field.DATATYPE_TEXT;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// DATETIME
			else if(currentDataType.equals(Field.DATATYPE_DATETIME)) {
				editingField.dataType = Field.DATATYPE_DATETIME;
			}
			// DATE
			else if(currentDataType.equals(Field.DATATYPE_DATE)) {
				editingField.dataType = Field.DATATYPE_DATE;
			}
			// TIME
			else if(currentDataType.equals(Field.DATATYPE_TIME)) {
				editingField.dataType = Field.DATATYPE_TIME;
			}
			// FILE
			else if(currentDataType.equals(Field.DATATYPE_FILE)) {
				editingField.dataType = Field.DATATYPE_FILE;
				editingField.max = Integer.parseInt(this.textFieldFileSizeMaxKb.getText());
			}
			// MAIL
			else if(currentDataType.equals(Field.DATATYPE_MAIL)) {
				editingField.dataType = Field.DATATYPE_MAIL;
			}
			// USERID
			else if(currentDataType.equals(Field.DATATYPE_USERID)) {
				editingField.dataType = Field.DATATYPE_USERID;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// MAIL_AUTH
			else if(currentDataType.equals(Field.DATATYPE_MAIL_AUTH)) {
				editingField.dataType = Field.DATATYPE_MAIL_AUTH;
			}
			// PASSWORD
			else if(currentDataType.equals(Field.DATATYPE_PASSWORD)) {
				editingField.dataType = Field.DATATYPE_PASSWORD;
				editingField.min = Integer.parseInt(tfMin.getText());
				editingField.max = Integer.parseInt(tfMax.getText());
			}
			// ROLE_NAME
			else if(currentDataType.equals(Field.DATATYPE_ROLE_NAME)) {
				editingField.dataType = Field.DATATYPE_ROLE_NAME;
			}
			else {
				Debug.error("�t�B�[���h�̕ҏW�����s���悤�Ƃ��܂������A�z��O�̃f�[�^�^�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
		else if(cmd.equals("�L�����Z��")) {
			// �������Ȃ�
		}

		frameClosed();
	}

	
	
	
	// �t���[��������Ƃ��̏���
	public void frameClosed() {
		FieldEditFrame.repaintAndValidate();

		// �t�B�[���h�ꗗ��ʂ̃t�B�[���h�ꗗ���X�V
		Panel_FieldList.getInstance().refreshFieldTable();

		// �t�B�[���h�ҏW�t���[���������A�W�F�l���[�^�t���[�����g�p�\��
		FieldEditFrame.getInstance().setVisible(false);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().requestFocus();
	}
	
	
	
	
	
	public void paintComponent(Graphics g) {
		g.setColor(FieldEditPanel.BG_COLOR);
		g.fillRect(0, 0, FieldEditFrame.frameWidth, FieldEditFrame.frameHeight);
	}
	
	
	
	public static FieldEditPanel getInstance() {
		return FieldEditPanel.obj;
	}


	
	public static void updateInstance(FieldEditPanel newObject) {
		FieldEditPanel.obj = newObject;
	}

}

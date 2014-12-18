package view.tableList.accountTableDef;

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





public class PanelAccountTableDefinition extends JPanel implements ActionListener, ItemListener,Serializable {
	public static final int PANEL_HEIGHT = 1500;
	
	public JComboBox comboBoxIdFieldDataTypeSelect;

	public JTextPane textPaneAtName = new JTextPane();
	public JTextPane textPaneAccOwnRoles = new JTextPane();
	public JTextPane textPaneIdFieldType = new JTextPane();
	public JTextPane textPaneUserid = new JTextPane();
	public JTextPane textPaneUseridName = new JTextPane();
	public JTextPane textPaneUseridFieldMin = new JTextPane();
	public JTextPane textPaneUseridFieldMax = new JTextPane();
	public JTextPane textPaneMailAuth = new JTextPane();
	public JTextPane textPanePassword = new JTextPane();
	public JTextPane textPanePasswordName = new JTextPane();
	public JTextPane textPanePasswordMin = new JTextPane();
	public JTextPane textPanePasswordMax = new JTextPane();
	
	public JTextField textFieldAtName = new JTextField();
	public JTextField textFieldUseridName = new JTextField();
	public JTextField textFieldUseridMin = new JTextField();
	public JTextField textFieldUseridMax = new JTextField();
	public JTextField textFieldMailAuthName = new JTextField();
	public JTextField textFieldPasswordName = new JTextField();
	public JTextField textFieldPasswordMin = new JTextField();
	public JTextField textFieldPasswordMax = new JTextField();
	
	public ArrayList<JCheckBox> checkBoxArrayAccOwnRoles = new ArrayList<JCheckBox>();
	
	public JButton btnAtAdd = new JButton();
	public JButton btnCancel = new JButton();
	
	public SerializableSpringLayout springLayout;

	public static final Color BACKGROUND_COLOR = Color.white;
	
	// �F�؃t�B�[���h�̃f�[�^�^�C�v�Ƃ���ID�E���[���A�h���X�̂ǂ��炪�I������Ă��邩�L�^����ϐ�
	public int SELECTED_ID_FIELD_DATATYPE;
	public static final int SELECTED_ID_FIELD_DATATYPE_USERID = 0;
	public static final int SELECTED_ID_FIELD_DATATYPE_MAILAUTH = 1;
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelAccountTableDefinition obj = new PanelAccountTableDefinition();

	
	
	
	
	private PanelAccountTableDefinition() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(PanelAccountTableDefinition.BACKGROUND_COLOR);
		
		initComps();
	}

	
	
	
	
	public void initComps() {
		boolean japanese = GeneratorProperty.japanese();
		
		String[] idFieldDataTypeList = {japanese?"ID�ŔF�؂���":"Identify by ID", japanese?"���[���A�h���X�ŔF�؂���":"Identify by Mail Address"};
		comboBoxIdFieldDataTypeSelect = new JComboBox(idFieldDataTypeList);
		comboBoxIdFieldDataTypeSelect.addItemListener(this);
		
		textFieldUseridMin = new JTextField();
		textFieldUseridMax = new JTextField();
	}
	
	
	
	
	
	public void relocateCompsForAccountTableDefinition(boolean frameOpened) {	// �t���[�����J���ꂱ�ꂩ��ҏW���J�n����Ƃ���Ȃ̂��ǂ����������Ŏw��
		removeAll();

		boolean japanese = GeneratorProperty.japanese();
		
		int PADD_LEFT = 20;

		// �e�L�X�g�y�C��
		remove(textPaneAtName);
		textPaneAtName = new JTextPane();
		textPaneAtName.setEditable(false);
		textPaneAtName.setText(japanese?"1. �A�J�E���g�e�[�u���̖��̂���͂��ĉ������B":"1. Name of Account Table");
		textPaneAtName.setPreferredSize(new Dimension(this.getWidth(), 20));
		Slpc.put(springLayout, "N", textPaneAtName, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneAtName, "W", this, PADD_LEFT);
		add(textPaneAtName);

		// �e�L�X�g�t�B�[���h�i�A�J�E���g�e�[�u�����j
		remove(textFieldAtName);
		if(frameOpened) {	// �t���[���I�[�v�����̂ݏ�����
			textFieldAtName = new JTextField();
		}
		textFieldAtName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldAtName, "S", textPaneAtName, 20);
		Slpc.put(springLayout, "W", textFieldAtName, "W", this, PADD_LEFT);
		add(textFieldAtName);
		
		// �e�L�X�g�y�C��
		remove(textPaneAccOwnRoles);
		textPaneAccOwnRoles = new JTextPane();
		textPaneAccOwnRoles.setEditable(false);
		textPaneAccOwnRoles.setText(japanese?"2. �ǂ̃��[���̃A�J�E���g���Ǘ����܂����B�P�ȏ�`�F�b�N���ĉ������B":"2. Which Roles' Accounts will be stored?");
		textPaneAccOwnRoles.setPreferredSize(new Dimension(this.getWidth(), 20));
		Slpc.put(springLayout, "N", textPaneAccOwnRoles, "S", textFieldAtName, 20);
		Slpc.put(springLayout, "W", textPaneAccOwnRoles, "W", this, PADD_LEFT);
		add(textPaneAccOwnRoles);

		// �`�F�b�N�{�b�N�X�i�A�J�E���g�I�[�i�[���[���I���j
		for(int i=0; i<checkBoxArrayAccOwnRoles.size(); i++) {	// ��U�S�ď���
			remove(checkBoxArrayAccOwnRoles.get(i));
		}
		if(frameOpened) {	// �t���[�����J�����u�Ԃ̂݁A�`�F�b�N�{�b�N�X�̏��������K�v
			checkBoxArrayAccOwnRoles = new ArrayList<JCheckBox>();
			ArrayList<String> noAtNormalRolesNames = RoleManager.getInstance().getNoAtNormalRolesNames();
			for(int i=0; i<noAtNormalRolesNames.size(); i++) {
				// �`�F�b�N�{�b�N�X������
				String noAtNormalRoleName = noAtNormalRolesNames.get(i);
				JCheckBox cb = new JCheckBox(noAtNormalRoleName);

				// �`�F�b�N�{�b�N�X�z��ɒǉ�
				checkBoxArrayAccOwnRoles.add(cb);
			}
		}
		for(int i=0; i<checkBoxArrayAccOwnRoles.size(); i++) {
			JCheckBox cb = checkBoxArrayAccOwnRoles.get(i);

			// �`�F�b�N�{�b�N�X�ʒu�����A�z�u
			Slpc.put(springLayout, "N", cb, "S", textPaneAccOwnRoles, 20+(i*30));
			Slpc.put(springLayout, "W", cb, "W", this, PADD_LEFT);
			add(cb);
		}
		
		
		// �e�L�X�g�y�C��
		remove(textPaneIdFieldType);
		textPaneIdFieldType = new JTextPane();
		textPaneIdFieldType.setEditable(false);
		textPaneIdFieldType.setText(japanese?"3-1. �F�؂�ID�A���[���A�h���X�̂ǂ����p���܂����B�I�����ĉ������B":"Identify by ID or Mail Address?");
		textPaneIdFieldType.setPreferredSize(new Dimension(this.getWidth(), 20));
		Slpc.put(springLayout, "N", textPaneIdFieldType, "S", checkBoxArrayAccOwnRoles.get(checkBoxArrayAccOwnRoles.size()-1), 40);
		Slpc.put(springLayout, "W", textPaneIdFieldType, "W", this, PADD_LEFT);
		add(textPaneIdFieldType);
		
		
		
		// �R���{�{�b�N�X�i���[�UID or ���[���A�h���X�@�̑I���j
		remove(comboBoxIdFieldDataTypeSelect);
		Slpc.put(springLayout, "N", comboBoxIdFieldDataTypeSelect, "S", textPaneIdFieldType, 10);
		Slpc.put(springLayout, "W", comboBoxIdFieldDataTypeSelect, "W", this, PADD_LEFT);
		add(comboBoxIdFieldDataTypeSelect);
		// ItemListener��initComps()�Œǉ����Ă���

		
		// �t���[�����J�����u�Ԃł��� -> ���[�UID��I�����Ă���
		if(frameOpened) {
			comboBoxIdFieldDataTypeSelect.setSelectedItem(comboBoxIdFieldDataTypeSelect.getItemAt(0));
		}

		// ���[�UID�ƔF�؃��[���A�h���X�̂ǂ��炪�I������Ă��邩�ɉ����āA�ȍ~�̕`����킯��
		String selectedIdType = (String)this.comboBoxIdFieldDataTypeSelect.getSelectedItem();
		if(selectedIdType.equals(japanese?"ID�ŔF�؂���":"Identify by ID")) {
			this.locateSinceUseridEditForm(frameOpened);
		}
		else if(selectedIdType.equals(japanese?"���[���A�h���X�ŔF�؂���":"Identify by Mail Address")) {
			this.locateSinceMailauthEditForm(frameOpened);
		}
		else {
			Debug.error("�R���{�{�b�N�X�̒l���z��O�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	
	public void locateSinceUseridEditForm(boolean frameOpened) {
		boolean japanese = GeneratorProperty.japanese();
		
		int PADD_LEFT = 20;
		int TEXT_FIELD_WIDTH = 60;

		// �e�L�X�g�y�C��
		remove(textPaneUseridName);
		textPaneUseridName = new JTextPane();
		textPaneUseridName.setText(japanese?"3-2. ID���i�[����t�B�[���h�̖��O����͂��ĉ������B�i��F�u���[�UID�v":"3-2. Name of ID Field (Ex. 'User ID')");
		Slpc.put(springLayout, "N", textPaneUseridName, "S", comboBoxIdFieldDataTypeSelect, 20);
		Slpc.put(springLayout, "W", textPaneUseridName, "W", this, PADD_LEFT);
		add(textPaneUseridName);

		// �e�L�X�g�t�B�[���h�i�t�B�[���h���j
		remove(textFieldUseridName);
		if(frameOpened) {	// �t���[���I�[�v�����̂ݏ�����
			textFieldUseridName = new JTextField();
		}
		textFieldUseridName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldUseridName, "S", textPaneUseridName, 0);
		Slpc.put(springLayout, "W", textFieldUseridName, "W", this, PADD_LEFT);
		add(textFieldUseridName);

		
		// �e�L�X�g�y�C��
		remove(textPaneUserid);
		textPaneUserid = new JTextPane();
		textPaneUserid.setText(japanese?"3-3. ID�̍ŏ��������A�ő啶������ݒ肵�ĉ������B":"3-3. Min/Max Length of ID");
		textPaneUserid.setPreferredSize(new Dimension(this.getHeight(), 20));
		Slpc.put(springLayout, "N", textPaneUserid, "S", textFieldUseridName, 20);
		Slpc.put(springLayout, "W", textPaneUserid, "W", this, PADD_LEFT);
		add(textPaneUserid);


		// �e�L�X�g�y�C���i�ŏ��������j
		remove(textPaneUseridFieldMin);
		textPaneUseridFieldMin = new JTextPane();
		textPaneUseridFieldMin.setText(japanese?"�ŏ��������F":"Min Length : ");
		Slpc.put(springLayout, "N", textPaneUseridFieldMin, "S", textPaneUserid, 5);
		Slpc.put(springLayout, "W", textPaneUseridFieldMin, "W", this, PADD_LEFT);
		add(textPaneUseridFieldMin);

		// �e�L�X�g�t�B�[���h�i�ŏ����������́j
		remove(textFieldUseridMin);
		textFieldUseridMin.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldUseridMin, "S", textPaneUseridFieldMin, 0);
		Slpc.put(springLayout, "W", textFieldUseridMin, "W", this, PADD_LEFT);
		add(textFieldUseridMin);
		if(frameOpened) {	// �t���[���I�[�v���� -> �l�����Ă���
			textFieldUseridMin.setText("4");
		}
		
	
		// �e�L�X�g�y�C���i�ő啶�����j
		remove(textPaneUseridFieldMax);
		textPaneUseridFieldMax = new JTextPane();
		textPaneUseridFieldMax.setText(japanese?"�ő啶�����F":"Max Length : ");
		Slpc.put(springLayout, "N", textPaneUseridFieldMax, "S", textFieldUseridMin, 10);
		Slpc.put(springLayout, "W", textPaneUseridFieldMax, "W", this, PADD_LEFT);
		add(textPaneUseridFieldMax);

		// �e�L�X�g�t�B�[���h�i�ő啶�������́j
		remove(textFieldUseridMax);
		textFieldUseridMax.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldUseridMax, "S", textPaneUseridFieldMax, 0);
		Slpc.put(springLayout, "W", textFieldUseridMax, "W", this, PADD_LEFT);
		add(textFieldUseridMax);
		if(frameOpened) {	// �t���[���I�[�v���� -> �l�����Ă���
			textFieldUseridMax.setText("16");
		}

		
		locateSincePasswordEditForm(frameOpened, textFieldUseridMax);

		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	
	public void locateSinceMailauthEditForm(boolean frameOpened) {
		boolean japanese = GeneratorProperty.japanese();
		
		int PADD_LEFT = 20;

		// �e�L�X�g�y�C��
		remove(textPaneMailAuth);
		textPaneMailAuth = new JTextPane();
		textPaneMailAuth.setText(
				japanese?
				"3-2. �F�؂Ƀ��[���A�h���X��p���܂��B\n      ���[���A�h���X���i�[����t�B�[���h�̖��O����͂��ĉ������B\n     �i��F�u�o�^���[���A�h���X�v�j":
				"3-2. Name of Mail Address Field (Ex. 'E-Mail')"
		);
		Slpc.put(springLayout, "N", textPaneMailAuth, "S", comboBoxIdFieldDataTypeSelect, 20);
		Slpc.put(springLayout, "W", textPaneMailAuth, "W", this, PADD_LEFT);
		add(textPaneMailAuth);

		// �e�L�X�g�t�B�[���h�i�t�B�[���h���j
		remove(textFieldMailAuthName);
		if(frameOpened) {	// �t���[���I�[�v�����̂ݏ�����
			textFieldMailAuthName = new JTextField();
		}
		textFieldMailAuthName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldMailAuthName, "S", textPaneMailAuth, 0);
		Slpc.put(springLayout, "W", textFieldMailAuthName, "W", this, PADD_LEFT);
		add(textFieldMailAuthName);

		locateSincePasswordEditForm(frameOpened, textFieldMailAuthName);

		FrameAccountTableDefinition.repaintAndValidate();
	}


	
	
	
	public void locateSincePasswordEditForm(boolean frameOpened, Component barometerComp) {
		boolean japanese = GeneratorProperty.japanese();

		int PADD_LEFT = 20;

		// �e�L�X�g�y�C��
		remove(textPanePasswordName);
		textPanePasswordName = new JTextPane();
		textPanePasswordName.setText(japanese?"4-1. �p�X���[�h���i�[����t�B�[���h�̖��O����͂��ĉ������B�i��F�u�o�^�p�X���[�h�v":"4-1. Name of Password Field (Ex. 'Password')");
		Slpc.put(springLayout, "N", textPanePasswordName, "S", barometerComp, 20);
		Slpc.put(springLayout, "W", textPanePasswordName, "W", this, PADD_LEFT);
		add(textPanePasswordName);

		// �e�L�X�g�t�B�[���h�i�p�X���[�h�t�B�[���h���j
		remove(textFieldPasswordName);
		if(frameOpened) {	// �t���[���I�[�v�����̂ݏ�����
			textFieldPasswordName = new JTextField();
		}
		textFieldPasswordName.setPreferredSize(new Dimension(150, 20));
		Slpc.put(springLayout, "N", textFieldPasswordName, "S", textPanePasswordName, 0);
		Slpc.put(springLayout, "W", textFieldPasswordName, "W", this, PADD_LEFT);
		add(textFieldPasswordName);

		// �e�L�X�g�y�C��
		remove(textPanePassword);
		textPanePassword = new JTextPane();
		textPanePassword.setText(japanese?"4-2. �p�X���[�h�̍ŏ��������A�ő啶������ݒ肵�ĉ������B":"4-2. Min/Max Length of Password");
		textPanePassword.setPreferredSize(new Dimension(this.getHeight(), 20));
		Slpc.put(springLayout, "N", textPanePassword, "S", textFieldPasswordName, 20);
		Slpc.put(springLayout, "W", textPanePassword, "W", this, PADD_LEFT);
		add(textPanePassword);

		int TEXT_FIELD_WIDTH = 60;
		
		// �e�L�X�g�y�C��
		remove(textPanePasswordMin);
		textPanePasswordMin = new JTextPane();
		textPanePasswordMin.setText(japanese?"�ŏ�������:":"Min Length : ");
		Slpc.put(springLayout, "N", textPanePasswordMin, "S", textPanePassword, 5);
		Slpc.put(springLayout, "W", textPanePasswordMin, "W", this, PADD_LEFT);
		add(textPanePasswordMin);
		
		// �e�L�X�g�t�B�[���h�i�ŏ����������́j
		remove(textFieldPasswordMin);
		textFieldPasswordMin.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldPasswordMin, "S", textPanePasswordMin, 0);
		Slpc.put(springLayout, "W", textFieldPasswordMin, "W", this, PADD_LEFT);
		add(textFieldPasswordMin);
		if(frameOpened) {	// �t���[���I�[�v���� -> �l�����Ă���
			textFieldPasswordMin.setText("4");
		}

		// �e�L�X�g�y�C��
		remove(textPanePasswordMax);
		textPanePasswordMax = new JTextPane();
		textPanePasswordMax.setText(japanese?"�ő啶����:":"Max Length : ");
		Slpc.put(springLayout, "N", textPanePasswordMax, "S", textFieldPasswordMin, 5);
		Slpc.put(springLayout, "W", textPanePasswordMax, "W", this, PADD_LEFT);
		add(textPanePasswordMax);
		
		// �e�L�X�g�t�B�[���h�i�ő啶�������́j
		remove(textFieldPasswordMax);
		textFieldPasswordMax.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, 20));
		Slpc.put(springLayout, "N", textFieldPasswordMax, "S", textPanePasswordMax, 0);
		Slpc.put(springLayout, "W", textFieldPasswordMax, "W", this, PADD_LEFT);
		add(textFieldPasswordMax);
		if(frameOpened) {	// �t���[���I�[�v���� -> �l�����Ă���
			textFieldPasswordMax.setText("16");
		}

		
		
		// �A�J�E���g�e�[�u���ǉ��{�^��
		remove(btnAtAdd);
		btnAtAdd = new JButton(japanese?"�A�J�E���g�e�[�u����ǉ�":"Add Account Table");
		Slpc.put(springLayout, "N", btnAtAdd, "S", textFieldPasswordMax, 30);
		Slpc.put(springLayout, "W", btnAtAdd, "W", this, PADD_LEFT);
		add(btnAtAdd);
		btnAtAdd.addActionListener(this);
		btnAtAdd.setActionCommand("�A�J�E���g�e�[�u���ǉ�");

	
		// �L�����Z���{�^��
		remove(btnCancel);
		btnCancel = new JButton(japanese?"�L�����Z��":"Cancel");
		Slpc.put(springLayout, "N", btnCancel, "N", btnAtAdd, 0);
		Slpc.put(springLayout, "W", btnCancel, "E", btnAtAdd, 10);
		add(btnCancel);
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");

		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();

		if(cmd.equals("�A�J�E���g�e�[�u���ǉ�")) {
			String newAtName = textFieldAtName.getText();
			
			ArrayList<NormalRole> accOwnRoleArray = new ArrayList<NormalRole>();
			for(int i=0; i<checkBoxArrayAccOwnRoles.size(); i++) {
				JCheckBox cb = checkBoxArrayAccOwnRoles.get(i);

				// �`�F�b�N����Ă��Ȃ����[���̓X���[
				if(!cb.isSelected()) {
					Debug.out("�m�[�}�����[���u" + cb.getText() + "�v�̓`�F�b�N����Ă��Ȃ��̂ŁA�A�J�E���g�I�[�i�[���[���ƂȂ�܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					continue;
				}

				String roleName = cb.getText();
				NormalRole normalRole = (NormalRole)RoleManager.getInstance().getRoleByName(roleName);
				accOwnRoleArray.add(normalRole);
			}

			if(newAtName.equals("")) {
				JOptionPane.showMessageDialog(this, japanese?"�A�J�E���g�e�[�u�����𐳂������͂��ĉ������B":"Input Account Table Name");
				Debug.out("�A�J�E���g�e�[�u���������͂���Ă��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			
			if(accOwnRoleArray.size()==0) {
				JOptionPane.showMessageDialog(this, japanese?"�ǂ̃��[���̃A�J�E���g���Ǘ�����̂��A�P�ȏ�I�����ĉ������B":"Choose at least 1 Roles");
				Debug.notice("�A�J�E���g�I�[�i�[���[�����P���I������Ă��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}

			// �A�J�E���g�e�[�u���쐬
			AccountTable newTable = new AccountTable(newAtName, accOwnRoleArray);

			// �F�؃t�B�[���h�ǉ�
			String selectedIdType = (String)this.comboBoxIdFieldDataTypeSelect.getSelectedItem();
			if(selectedIdType.equals(japanese?"ID�ŔF�؂���":"Identify by ID")) {
				String fieldName = this.textFieldUseridName.getText();
				int min = Integer.parseInt(this.textFieldUseridMin.getText());
				int max = Integer.parseInt(this.textFieldUseridMax.getText());
				Field useridField = new Field(fieldName, Field.DATATYPE_USERID, min, max);
				newTable.addField(useridField);
			}
			else if(selectedIdType.equals(japanese?"���[���A�h���X�ŔF�؂���":"Identify by Mail Address")) {
				String fieldName = this.textFieldMailAuthName.getText();
				Field mailAuthField = new Field(fieldName, Field.DATATYPE_MAIL_AUTH);
				newTable.addField(mailAuthField);
			}
			else {
				Debug.error("�R���{�{�b�N�X�̒l���z��O�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}

			// �p�X���[�h�t�B�[���h�ǉ�
			String passwordFieldName = this.textFieldPasswordName.getText();
			int min = Integer.parseInt(this.textFieldPasswordMin.getText());
			int max = Integer.parseInt(this.textFieldPasswordMax.getText());
			Field passwordField = new Field(passwordFieldName, Field.DATATYPE_PASSWORD, min, max);
			newTable.addField(passwordField);
			
			// TableManager�ɒǉ�
			TableManager.getInstance().addAccountTable(newTable);

			// ���̃t���[���������A���C���t���[�����g�p�\�ɂ���
			FrameAccountTableDefinition.getInstance().setEnabled(false);
			FrameAccountTableDefinition.getInstance().setVisible(false);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setVisible(true);

			// ���C���t���[���̃e�[�u���ꗗ��ʏ㕔�p�l���̃A�J�E���g�e�[�u���ꗗ���X�V
			PanelTableListAbove.getInstance().refreshAccountTableList();
		}
		else if(cmd.equals("�L�����Z��")) {
			// ���̃t���[���������A���C���t���[�����g�p�\�ɂ���
			FrameAccountTableDefinition.getInstance().setVisible(false);
			FrameAccountTableDefinition.getInstance().setEnabled(false);
			FrameAccountTableDefinition.getInstance().setFocusable(false);
			MainFrame.getInstance().setVisible(true);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setFocusable(true);
			MainFrame.getInstance().requestFocus();
		}

		MainFrame.repaintAndValidate();
		FrameAccountTableDefinition.repaintAndValidate();	
	}
	
	
	
	
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()!=this.comboBoxIdFieldDataTypeSelect) {
			Debug.error("�z��O�̃C�x���g�������ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		
		this.relocateCompsForAccountTableDefinition(false);		// �t���[���I�[�v�����ł͂Ȃ��̂�false
		//�@�R���{�{�b�N�X�Ń��[�UID�E�F�؃��[���A�h���X�ǂ��炪�I�����ꂽ�̂��́ArelocateCompsForAccountTableDefinition()���Ń`�F�b�N���`����ꍇ�킯����̂ő��v

		FrameAccountTableDefinition.repaintAndValidate();
	}
	
	
	
	public static PanelAccountTableDefinition getInstance() {
		return PanelAccountTableDefinition.obj;
	}



	public static void updateInstance(PanelAccountTableDefinition newObject) {
		PanelAccountTableDefinition.obj = newObject;
	}
}

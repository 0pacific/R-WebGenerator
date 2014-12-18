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


/*
 * NOTICE :
 * Singleton
 */
public class PanelAuthEditAbove extends JPanel implements ActionListener,Serializable {
	private Role role;
	private SuperTable table;

	public SerializableSpringLayout springLayout;
	
	private final int panelWidth = MainFrame.frameWidth - 20;
	private final int panelHeight = 2000;					// ���Ƃ肠�������̍����ɂ��Ă���
	public static final int SCROLL_PANE_HEIGHT = 400;		// ���̏㕔�p�l������荞�ރX�N���[���y�C���̏c��
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelAuthEditAbove obj = new PanelAuthEditAbove();

	
	
	
	
	
	private PanelAuthEditAbove() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		setBackground(Color.WHITE);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �`�F�b�N�{�b�N�X����z�u���Č�����`��ʂ��\������
	 */
	public void relocate(Role role, SuperTable table) {
		boolean japanese = GeneratorProperty.japanese();
		
		setRoleAndTable(role, table);
		
		// �S�R���|�[�l���g����
		removeAll();

		int PADDING_LEFT = 20;
		
		
		// ���[�����e�[�u�������I���̏ꍇ�A���b�Z�[�W�����\�����ďI��
		if(role==null || table==null) {
			JTextPane textPaneMessage = new JTextPane();
			textPaneMessage.setText(japanese ? "���[���ƃe�[�u����I�����ĉ������B" : "Select Role and Table.");
			Slpc.put(springLayout, "N", textPaneMessage, "N", this, 20);
			Slpc.put(springLayout, "W", textPaneMessage, "W", this, PADDING_LEFT);
			add(textPaneMessage);

			MainFrame.repaintAndValidate();
			return;
		}
		

		AuthorityToTable authToTable = AuthorityManager.getInstance().getTableAuth(role, table);
		if(authToTable instanceof AuthorityToDataTable) {
			relocateForEditingAuthToDataTable((AuthorityToDataTable)authToTable);
		}
		else if(authToTable instanceof AuthorityToAccountTable) {
			relocateForEditingAuthToAccountTable((AuthorityToAccountTable)authToTable);
		}
	}

	
	
	
	
	public void relocateForEditingAuthToAccountTable(AuthorityToAccountTable authToAt) {
		boolean japanese = GeneratorProperty.japanese();

		AccountTable accountTable = (AccountTable)authToAt.getTable();
		
		int PADDING_LEFT = 20;

		// for���̒��Ń��[�J���ɒ�`���ꂽ�R���|�[�l���g�ȂǁA��ŎQ�Ƃł��Ȃ��Ȃ�R���|�[�l���g��SpringLayout�̊�ɗp�������Ƃ��A����Ɋi�[���Ă����ĎQ�Ƃ���
		Component barometerComp = null;
		
		// �e�L�X�g�y�C��
		JTextPane textPaneMessage = new JTextPane();
		String msg1 = japanese ?
						"���[���u" + role.getRoleName() + "�v�̃e�[�u���u" + table.getTableName() + "�v�ɑ΂���A�N�Z�X������ҏW���܂��B\n��]�ɍ��킹�ă`�F�b�N�����ĉ������B" :
						"Editing accessibility of role \"" + role.getRoleName() + "\" to table \"" + table.getTableName() + "\".";
		textPaneMessage.setText(msg1);
		Slpc.put(springLayout, "N", textPaneMessage, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneMessage, "W", this, PADDING_LEFT);
		add(textPaneMessage);


		for(int accountOrder=0; accountOrder<accountTable.getNormalRoleNum(); accountOrder++) {
			// �A�J�E���g�I�[�i�[���[���Ƃ��̖��O
			NormalRole accountOwnerRole = accountTable.getNormalRole(accountOrder);
			String accountOwnerRoleName = accountOwnerRole.getRoleName();

			// �����ҏW���̃��[�������̃A�J�E���g�ɑ΂��Ď����Ă��錠���Z�b�g
			AuthoritySet authorityToAccount = authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName);
			
			
			// �e�L�X�g�y�C���i�A�J�E���g��ʁj...����̉��������̑��Έʒu�̊�́A�ŏ��̃A�J�E���g���Q�ڈȍ~�̃A�J�E���g���ňقȂ�
			JTextPane textPaneAccount = new JTextPane();
			String msg2 = japanese ?
							(accountOrder+1)+". ���[���u" + accountOwnerRoleName + "�v�̃A�J�E���g�ɑ΂��錠����ҏW���܂��B" :
							(accountOrder+1)+". Editing accessibility to account of role \"" + accountOwnerRoleName + "\".";
			textPaneAccount.setText(msg2);
			Slpc.put(springLayout, "W", textPaneAccount, "W", this, PADDING_LEFT);
			if(accountOrder==0)
				Slpc.put(springLayout, "N", textPaneAccount, "S", textPaneMessage, 20);
			else
				Slpc.put(springLayout, "N", textPaneAccount, "S", barometerComp, 20);
			add(textPaneAccount);

			
			// �e�L�X�g�y�C���iRA�j
			JTextPane textPaneRa = new JTextPane();
			String msg3 = japanese ?
							(accountOrder+1)+"-1. ���[�������̒�` ... �^�����������Ƀ`�F�b�N�����ĉ������B" :
							(accountOrder+1)+"-1. Role Accessibility Definition ... check data access you want to allow."; 
			textPaneRa.setText(msg3);
			Slpc.put(springLayout, "N", textPaneRa, "S", textPaneAccount, 20);
			Slpc.put(springLayout, "W", textPaneRa, "W", this, PADDING_LEFT);
			add(textPaneRa);

			
			// �e�L�X�g�y�C���iCreate�j
			JTextPane textPaneCreate = new JTextPane();
			String msg4 = japanese ?
					(accountOrder+1)+"-1-1.���[���u"+accountOwnerRoleName+"�v�̃A�J�E���g���쐬���錠���iCreate�j" :
						(accountOrder+1)+"-1-1. Create account of role \""+accountOwnerRoleName+"\"";
			textPaneCreate.setText(msg4);
			Slpc.put(springLayout, "N", textPaneCreate, "S", textPaneRa, 10);
			Slpc.put(springLayout, "W", textPaneCreate, "W", this, PADDING_LEFT);
			add(textPaneCreate);

			
			// �`�F�b�N�{�b�N�X Create
			JCheckBox createCb = new JCheckBox("Create");
			createCb.addActionListener(this);
			createCb.setActionCommand("AccountTable Create " + accountOwnerRoleName);
			if(authorityToAccount.getCreate())
				createCb.setSelected(true);
			else
				createCb.setSelected(false);
			Slpc.put(springLayout, "N", createCb, "S", textPaneCreate, 5);
			Slpc.put(springLayout, "W", createCb, "W", this, PADDING_LEFT);
			add(createCb);

			
			// �e�L�X�g�y�C���iDelete�j
			JTextPane textPaneDelete = new JTextPane();
			String msg5 = japanese ?
					(accountOrder+1)+"-1-2.���[���u"+accountOwnerRoleName+"�v�̔C�ӂ̃A�J�E���g���폜���錠���iDelete�j" :
						(accountOrder+1)+"-1-2. Delete other users' accounts";
			textPaneDelete.setText(msg5);
			Slpc.put(springLayout, "N", textPaneDelete, "S", createCb, 20);
			Slpc.put(springLayout, "W", textPaneDelete, "W", this, PADDING_LEFT);
			add(textPaneDelete);

			// �`�F�b�N�{�b�N�X Delete�i���[�������j
			JCheckBox deleteCb = new JCheckBox("Delete");
			deleteCb.addActionListener(this);
			deleteCb.setActionCommand("AccountTable RaDelete " + accountOwnerRoleName);
			if(authorityToAccount.getDelete())
				deleteCb.setSelected(true);
			else
				deleteCb.setSelected(false);
			Slpc.put(springLayout, "N", deleteCb, "S", textPaneDelete, 5);
			Slpc.put(springLayout, "W", deleteCb, "W", this, PADDING_LEFT);
			add(deleteCb);

			
			// �e�L�X�g�y�C���iRead, Write, ExWrite�j
			JTextPane textPaneFieldAuth = new JTextPane();
			String msg6 = japanese ?
					(accountOrder+1)+"-1-3.���[���u"+accountOwnerRoleName+"�v�̔C�ӂ̃A�J�E���g���́A�e�t�B�[���h�ɑ΂��錠���iRead, Write, ExclusiveWrite�j" :
						(accountOrder+1)+"-1-3. Read, Write, ExclusiveWrite each field of other users' accounts;";
			textPaneFieldAuth.setText(msg6);
			Slpc.put(springLayout, "N", textPaneFieldAuth, "S", deleteCb, 20);
			Slpc.put(springLayout, "W", textPaneFieldAuth, "W", this, PADDING_LEFT);
			add(textPaneFieldAuth);

			
			// �e�t�B�[���h��Read, Write, ExWrite�i���[�������j
			// �������̃R�[�h�͌l�����̏ꍇ�Ƃ��Ȃ�d��
			int fieldNum = table.getFieldNum();
			for(int i=0; i<fieldNum; i++) {
				Field field = table.getField(i);
				String fieldName = field.getFieldName();

				// ���̃t�B�[���h�̕����́A��̃R���|�[�l���g����̋���
				int HEIGHT = 30*i + 20;
				
				// �t�B�[���h�����x��
				JLabel fieldNameLabel = new JLabel(fieldName);
				int LABEL_WIDTH = 120;
				fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
				Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
				add(fieldNameLabel);

				JCheckBox checkBoxRaRead = new JCheckBox("Read");
				checkBoxRaRead.addActionListener(this);
				checkBoxRaRead.setActionCommand("AccountTable RaRead " + twoDigit(i) + " " + accountOwnerRoleName);
				// PASSWORD�^�t�B�[���h�̏ꍇ�ARead�͂ł��Ȃ�
				if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
					checkBoxRaRead.setSelected(false);
					checkBoxRaRead.setEnabled(false);
				}
				
				JCheckBox checkBoxRaWrite = new JCheckBox("Write");
				checkBoxRaWrite.addActionListener(this);
				checkBoxRaWrite.setActionCommand("AccountTable RaWrite " + twoDigit(i) + " " + accountOwnerRoleName);
				// ROLE_NAME�^�t�B�[���h�̏ꍇ�AWrite�͂ł��Ȃ�
				if(field.dataType.equals(Field.DATATYPE_ROLE_NAME)) {
					checkBoxRaWrite.setSelected(false);
					checkBoxRaWrite.setEnabled(false);
				}

				JCheckBox checkBoxRaExWrite = new JCheckBox("ExWrite");
				checkBoxRaExWrite.addActionListener(this);
				checkBoxRaExWrite.setActionCommand("AccountTable RaExWrite " + twoDigit(i) + " " + accountOwnerRoleName);
				// ROLE_NAME�^�t�B�[���h�̏ꍇ�AExWrite�͂ł��Ȃ�
				if(field.dataType.equals(Field.DATATYPE_ROLE_NAME)) {
					checkBoxRaExWrite.setSelected(false);
					checkBoxRaExWrite.setEnabled(false);
				}


				// ���݂� Read, Write, ExWrite�����ɉ����āA�`�F�b�N������O������
				if(authorityToAccount.getRaRead(i))
					checkBoxRaRead.setSelected(true);
				else
					checkBoxRaRead.setSelected(false);
				if(authorityToAccount.getRaWrite(i))
					checkBoxRaWrite.setSelected(true);
				else
					checkBoxRaWrite.setSelected(false);
				if(authorityToAccount.getRaExWrite(i))
					checkBoxRaExWrite.setSelected(true);
				else
					checkBoxRaExWrite.setSelected(false);

				
				// �P�t�B�[���h���̃`�F�b�N�{�b�N�X����
				Slpc.put(springLayout, "N", checkBoxRaRead, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxRaRead, "E", fieldNameLabel, 20);
				add(checkBoxRaRead);
				Slpc.put(springLayout, "N", checkBoxRaWrite, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxRaWrite, "E", checkBoxRaRead, 20);
				add(checkBoxRaWrite);
				Slpc.put(springLayout, "N", checkBoxRaExWrite, "S", textPaneFieldAuth, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxRaExWrite, "E", checkBoxRaWrite, 20);
				add(checkBoxRaExWrite);

				// �Ō�̏ꍇ�́A�t�B�[���h�����x�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
				if(i==fieldNum-1) {
					barometerComp = fieldNameLabel;
				}
			}
			// �t�B�[���h���P���Ȃ������ꍇ�́A�e�L�X�g�y�C�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
			if(fieldNum==0) {
				barometerComp = textPaneFieldAuth;
			}

			
			// Delete�ƁA�e�t�B�[���h��Read, Write, ExWrite�i�l�����j
			// �����ҏW���̃��[�������̃A�J�E���g�̃I�[�i�[���[���ł���ꍇ�̂ݕ\��
			// �������̃R�[�h�̓��[�������̏ꍇ�Ƃ��Ȃ�d��
			if(role==accountOwnerRole) {
				// �e�L�X�g�y�C���iIA-Delete�j
				JTextPane textPaneIaDelete = new JTextPane();
				String msg7 = japanese ?
						(accountOrder+1)+"-2-1.�����̃A�J�E���g���폜���錠���iDelete�j" :
							(accountOrder+1)+"-2-1. Delete own account";
				textPaneIaDelete.setText(msg7);
				Slpc.put(springLayout, "N", textPaneIaDelete, "S", barometerComp, 20);
				Slpc.put(springLayout, "W", textPaneIaDelete, "W", this, PADDING_LEFT);
				add(textPaneIaDelete);

				// �`�F�b�N�{�b�N�X�iIA-Delete�j
				JCheckBox iaDeleteCb = new JCheckBox("Delete");
				iaDeleteCb.addActionListener(this);
				iaDeleteCb.setActionCommand("AccountTable IaDelete " + accountOwnerRoleName);
				if(authorityToAccount.getIaDelete())
					iaDeleteCb.setSelected(true);
				else
					iaDeleteCb.setSelected(false);
				Slpc.put(springLayout, "N", iaDeleteCb, "S", textPaneIaDelete, 0);
				Slpc.put(springLayout, "W", iaDeleteCb, "W", this, PADDING_LEFT);
				add(iaDeleteCb);


				// �e�L�X�g�y�C���iIA-Read, IA-Write, IA-ExWrite�j
				JTextPane textPaneFieldAuthIa = new JTextPane();
				String msg8 = japanese ?
						(accountOrder+1)+"-2-2.�����̃A�J�E���g���́A�e�t�B�[���h�ɑ΂��錠���iRead, Write, ExclusiveWrite�j" :
							(accountOrder+1)+"-2-2. Read, Write, ExclusiveWrite each field of own account";
				textPaneFieldAuthIa.setText(msg8);
				Slpc.put(springLayout, "N", textPaneFieldAuthIa, "S", iaDeleteCb, 20);
				Slpc.put(springLayout, "W", textPaneFieldAuthIa, "W", this, PADDING_LEFT);
				add(textPaneFieldAuthIa);

				// �e�t�B�[���h��Read, Write, ExWrite�i�l�����j
				// �������̃R�[�h�̓��[�������̏ꍇ�Ƃ��Ȃ�d��
				for(int i=0; i<fieldNum; i++) {
					Field field = table.getField(i);
					String fieldName = field.getFieldName();

					// ���̃t�B�[���h�̕����́A��̃R���|�[�l���g����̋���
					int HEIGHT = 30*i + 20;
					
					// �t�B�[���h�����x��
					JLabel fieldNameLabel = new JLabel(fieldName);
					int LABEL_WIDTH = 120;
					fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
					Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
					add(fieldNameLabel);

					
					JCheckBox checkBoxIaRead = new JCheckBox("Read");
					checkBoxIaRead.addActionListener(this);
					checkBoxIaRead.setActionCommand("AccountTable IaRead " + twoDigit(i) + " " + accountOwnerRoleName);
					// PASSWORD�^�t�B�[���h�̏ꍇ�ARead�͂ł��Ȃ�
					if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
						checkBoxIaRead.setSelected(false);
						checkBoxIaRead.setEnabled(false);
					}

					JCheckBox checkBoxIaWrite = new JCheckBox("Write");
					checkBoxIaWrite.addActionListener(this);
					checkBoxIaWrite.setActionCommand("AccountTable IaWrite " + twoDigit(i) + " " + accountOwnerRoleName);

					JCheckBox checkBoxIaExWrite = new JCheckBox("ExWrite");
					checkBoxIaExWrite.addActionListener(this);
					checkBoxIaExWrite.setActionCommand("AccountTable IaExWrite " + twoDigit(i) + " " + accountOwnerRoleName);


					// ���݂� Read, Write, ExWrite�����ɉ����āA�`�F�b�N������O������
					if(authorityToAccount.getIaRead(i))
						checkBoxIaRead.setSelected(true);
					else
						checkBoxIaRead.setSelected(false);
					if(authorityToAccount.getIaWrite(i))
						checkBoxIaWrite.setSelected(true);
					else
						checkBoxIaWrite.setSelected(false);
					if(authorityToAccount.getIaExWrite(i))
						checkBoxIaExWrite.setSelected(true);
					else
						checkBoxIaExWrite.setSelected(false);

					
					// �P�t�B�[���h���̃`�F�b�N�{�b�N�X����
					Slpc.put(springLayout, "N", checkBoxIaRead, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", checkBoxIaRead, "E", fieldNameLabel, 20);
					add(checkBoxIaRead);
					Slpc.put(springLayout, "N", checkBoxIaWrite, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", checkBoxIaWrite, "E", checkBoxIaRead, 20);
					add(checkBoxIaWrite);
					Slpc.put(springLayout, "N", checkBoxIaExWrite, "S", textPaneFieldAuthIa, HEIGHT);
					Slpc.put(springLayout, "W", checkBoxIaExWrite, "E", checkBoxIaWrite, 20);
					add(checkBoxIaExWrite);

					// �Ō�̏ꍇ�́A�t�B�[���h�����x�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
					if(i==fieldNum-1) {
						barometerComp = fieldNameLabel;
					}
				}
				// �t�B�[���h���P���Ȃ������ꍇ�́A�e�L�X�g�y�C�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
				if(fieldNum==0) {
					barometerComp = textPaneFieldAuthIa;
				}
			}
		}

		MainFrame.repaintAndValidate();
	}
	
	

	
	public void relocateForEditingAuthToDataTable(AuthorityToDataTable authToDt) {
		boolean japanese = GeneratorProperty.japanese();
		DataTable dataTable = (DataTable)authToDt.getTable();
		
		int PADDING_LEFT = 20;

		// for���̒��Ń��[�J���ɒ�`���ꂽ�R���|�[�l���g�ȂǁA��ŎQ�Ƃł��Ȃ��Ȃ�R���|�[�l���g��SpringLayout�̊�ɗp�������Ƃ��A����Ɋi�[���Ă����ĎQ�Ƃ���
		Component barometerComp = null;
		
		// �e�L�X�g�y�C��
		JTextPane textPaneMessage = new JTextPane();
		String msg1 = japanese ?
				"���[���u" + role.getRoleName() + "�v�����A�e�[�u���u" + table.getTableName() + "�v�ɑ΂��鑀�쌠����ҏW���ł��B\n��]�ɍ��킹�ă`�F�b�N�����ĉ������B" :
					"Editing accessibility of role \""+role.getRoleName()+"\" to table \"" + table.getTableName() + "\".";
		textPaneMessage.setText(msg1);
		Slpc.put(springLayout, "N", textPaneMessage, "N", this, 20);
		Slpc.put(springLayout, "W", textPaneMessage, "W", this, PADDING_LEFT);
		add(textPaneMessage);


		// �����ҏW���̃��[�������̃f�[�^�e�[�u���ɑ΂��Ď����Ă��錠���Z�b�g
		AuthoritySet authSetToDt = authToDt.authSet;
		
		
		// �e�L�X�g�y�C���iRA�j
		JTextPane textPaneRa = new JTextPane();
		String msg2 = japanese ?
				"1. ���[�������̒�` ... �^�����������Ƀ`�F�b�N�����ĉ������B" :
				"1. Role Accessibility Definition ... check data access you want to allow.";
		textPaneRa.setText(msg2);
		Slpc.put(springLayout, "N", textPaneRa, "S", textPaneMessage, 20);
		Slpc.put(springLayout, "W", textPaneRa, "W", this, PADDING_LEFT);
		add(textPaneRa);

		
		// �e�L�X�g�y�C���iCreate�j
		JTextPane textPaneCreate = new JTextPane();
		String msg3 = japanese ? "1-1. ���R�[�h�쐬" : "1-1. Create Records";
		textPaneCreate.setText(msg3);
		Slpc.put(springLayout, "N", textPaneCreate, "S", textPaneRa, 10);
		Slpc.put(springLayout, "W", textPaneCreate, "W", this, PADDING_LEFT);
		add(textPaneCreate);

		
		// �`�F�b�N�{�b�N�X Create
		JCheckBox createCb = new JCheckBox("Create");
		createCb.addActionListener(this);
		createCb.setActionCommand("DataTable Create");
		if(authSetToDt.getCreate())
			createCb.setSelected(true);
		else
			createCb.setSelected(false);
		Slpc.put(springLayout, "N", createCb, "S", textPaneCreate, 5);
		Slpc.put(springLayout, "W", createCb, "W", this, PADDING_LEFT);
		add(createCb);

		
		// �e�L�X�g�y�C���iDelete�j
		JTextPane textPaneDelete = new JTextPane();
		textPaneDelete.setText(japanese ? "1-2.���R�[�h�̍폜" : "1-2.Delete records");
		Slpc.put(springLayout, "N", textPaneDelete, "S", createCb, 20);
		Slpc.put(springLayout, "W", textPaneDelete, "W", this, PADDING_LEFT);
		add(textPaneDelete);

		// �`�F�b�N�{�b�N�X Delete�i���[�������j
		JCheckBox deleteCb = new JCheckBox("Delete");
		deleteCb.addActionListener(this);
		deleteCb.setActionCommand("DataTable RaDelete");
		if(authSetToDt.getDelete())
			deleteCb.setSelected(true);
		else
			deleteCb.setSelected(false);
		Slpc.put(springLayout, "N", deleteCb, "S", textPaneDelete, 5);
		Slpc.put(springLayout, "W", deleteCb, "W", this, PADDING_LEFT);
		add(deleteCb);

		
		// �e�L�X�g�y�C���iRead, Write, ExWrite�j
		JTextPane textPaneFieldAuth = new JTextPane();
		textPaneFieldAuth.setText(japanese ? "1-3.�e�t�B�[���h�ɑ΂��錠��" : "1-3.Accessibility to each field");
		Slpc.put(springLayout, "N", textPaneFieldAuth, "S", deleteCb, 20);
		Slpc.put(springLayout, "W", textPaneFieldAuth, "W", this, PADDING_LEFT);
		add(textPaneFieldAuth);

		
		// �e�t�B�[���h��Read, Write, ExWrite�i���[�������j
		// �������̃R�[�h�͌l�����̏ꍇ�Ƃ��Ȃ�d��
		int fieldNum = table.getFieldNum();
		for(int i=0; i<fieldNum; i++) {
			Field field = table.getField(i);
			String fieldName = field.getFieldName();

			// ���̃t�B�[���h�̕����́A��̃R���|�[�l���g����̋���
			int HEIGHT = 30*i + 20;
			
			// �t�B�[���h�����x��
			JLabel fieldNameLabel = new JLabel(fieldName);
			int LABEL_WIDTH = 70;
			fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
			Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
			add(fieldNameLabel);

			JCheckBox checkBoxRaRead = new JCheckBox(japanese ? "Read�i�Ǐo�j" : "Read");
			checkBoxRaRead.addActionListener(this);
			checkBoxRaRead.setActionCommand("DataTable RaRead " + twoDigit(i));
			// PASSWORD�^�t�B�[���h�̏ꍇ�ARead�͂ł��Ȃ�
			if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
				checkBoxRaRead.setSelected(false);
				checkBoxRaRead.setEnabled(false);
			}
			
			JCheckBox checkBoxRaWrite = new JCheckBox(japanese ? "Write�i�X�V�j" : "Write");
			checkBoxRaWrite.addActionListener(this);
			checkBoxRaWrite.setActionCommand("DataTable RaWrite " + twoDigit(i));

			JCheckBox checkBoxRaExWrite = new JCheckBox(japanese ? "ExclusiveWrite�i�r���I�X�V�j" : "ExclusiveWrite");
			checkBoxRaExWrite.addActionListener(this);
			checkBoxRaExWrite.setActionCommand("DataTable RaExWrite " + twoDigit(i));


			// ���݂� Read, Write, ExWrite�����ɉ����āA�`�F�b�N������O������
			if(authSetToDt.getRaRead(i))
				checkBoxRaRead.setSelected(true);
			else
				checkBoxRaRead.setSelected(false);
			if(authSetToDt.getRaWrite(i))
				checkBoxRaWrite.setSelected(true);
			else
				checkBoxRaWrite.setSelected(false);
			if(authSetToDt.getRaExWrite(i))
				checkBoxRaExWrite.setSelected(true);
			else
				checkBoxRaExWrite.setSelected(false);

			
			// �P�t�B�[���h���̃`�F�b�N�{�b�N�X����
			Slpc.put(springLayout, "N", checkBoxRaRead, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", checkBoxRaRead, "E", fieldNameLabel, 20);
			add(checkBoxRaRead);
			Slpc.put(springLayout, "N", checkBoxRaWrite, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", checkBoxRaWrite, "E", checkBoxRaRead, 20);
			add(checkBoxRaWrite);
			Slpc.put(springLayout, "N", checkBoxRaExWrite, "S", textPaneFieldAuth, HEIGHT);
			Slpc.put(springLayout, "W", checkBoxRaExWrite, "E", checkBoxRaWrite, 20);
			add(checkBoxRaExWrite);

			// �Ō�̏ꍇ�́A�t�B�[���h�����x�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
			if(i==fieldNum-1) {
				barometerComp = fieldNameLabel;
			}
		}
		// �t�B�[���h���P���Ȃ������ꍇ�́A�e�L�X�g�y�C�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
		if(fieldNum==0) {
			barometerComp = textPaneFieldAuth;
		}


		
		// IA-Defined���ۂ���؂�ւ���`�F�b�N�{�b�N�X
		JCheckBox iaSwitchCb = new JCheckBox();
		if(role instanceof GuestRole) {
			// �Q�X�g���[���̏ꍇ -> IA-Defined�ɂ͂ł��Ȃ��̂ŁA�`�F�b�N�{�b�N�X�̓`�F�b�N�Ȃ��̏�ԂŎg�p�s�\�ɂ���
			iaSwitchCb.setText(japanese ? "2. �l�����̒�` ... �Q�X�g���[���ɂ͒�`�ł��܂���B" : "2. Individual Accessibility ... Can't be defined for Guest Role");
			iaSwitchCb.setSelected(false);
			iaSwitchCb.setEnabled(false);
		} else {
			iaSwitchCb.setText(japanese ? "2. �l�����̒�` ... ���[���Ɍl�����������������ꍇ�̓`�F�b�N���ĉ������B" : "2. Individual Accessibility ... Check if you want to define");
			iaSwitchCb.setEnabled(true);

			if(authSetToDt.haveIa())
				iaSwitchCb.setSelected(true);
			else
				iaSwitchCb.setSelected(false);
		}
		iaSwitchCb.addActionListener(this);
		iaSwitchCb.setActionCommand("DataTable iaSwitch");
		Slpc.put(springLayout, "N", iaSwitchCb, "S", barometerComp, 30);
		Slpc.put(springLayout, "W", iaSwitchCb, "W", this, PADDING_LEFT);
		add(iaSwitchCb);

		// ���Έʒu�̊�Ƃ��āA���̃`�F�b�N�{�b�N�X���L�����Ă����i�ȍ~�̃R���|�[�l���g�����̃`�F�b�N�{�b�N�X����̑��Έʒu��SpringLayout��p���Ďw��ł���悤�Ɂj
		barometerComp = iaSwitchCb;
		
		
		
		// Delete�ƁA�e�t�B�[���h��Read, Write, ExWrite�i�l�����j
		// �����ҏW���̃��[�������̃f�[�^�e�[�u���ɑ΂�IA-Defined�ł���ꍇ�̂ݕ\��
		// �������̃R�[�h�̓��[�������̏ꍇ�Ƃ��Ȃ�d��
		if(authSetToDt.haveIa()) {
			// �e�L�X�g�y�C���iIA-Delete�j
			JTextPane textPaneIaDelete = new JTextPane();
			textPaneIaDelete.setText(japanese ? "2-1.�����ɑΉ��������R�[�h���폜" : "2-1.Delete own records");
			Slpc.put(springLayout, "N", textPaneIaDelete, "S", iaSwitchCb, 20);
			Slpc.put(springLayout, "W", textPaneIaDelete, "W", this, PADDING_LEFT);
			add(textPaneIaDelete);

			// �`�F�b�N�{�b�N�X�iIA-Delete�j
			JCheckBox iaDeleteCb = new JCheckBox("Delete");
			iaDeleteCb.addActionListener(this);
			iaDeleteCb.setActionCommand("DataTable IaDelete");
			if(authSetToDt.getIaDelete())
				iaDeleteCb.setSelected(true);
			else
				iaDeleteCb.setSelected(false);
			Slpc.put(springLayout, "N", iaDeleteCb, "S", textPaneIaDelete, 0);
			Slpc.put(springLayout, "W", iaDeleteCb, "W", this, PADDING_LEFT);
			add(iaDeleteCb);


			// �e�L�X�g�y�C���iIA-Read, IA-Write, IA-ExWrite�j
			JTextPane textPaneFieldAuthIa = new JTextPane();
			textPaneFieldAuthIa.setText(japanese ? "2-2.�����ɑΉ��������R�[�h���́A�e�t�B�[���h�ɑ΂��錠��" : "2-2.Accessibility to each field of own records");
			Slpc.put(springLayout, "N", textPaneFieldAuthIa, "S", iaDeleteCb, 20);
			Slpc.put(springLayout, "W", textPaneFieldAuthIa, "W", this, PADDING_LEFT);
			add(textPaneFieldAuthIa);

			// �e�t�B�[���h��Read, Write, ExWrite�i�l�����j
			// �������̃R�[�h�̓��[�������̏ꍇ�Ƃ��Ȃ�d��
			for(int i=0; i<fieldNum; i++) {
				Field field = table.getField(i);
				String fieldName = field.getFieldName();

				// ���̃t�B�[���h�̕����́A��̃R���|�[�l���g����̋���
				int HEIGHT = 30*i + 20;
				
				// �t�B�[���h�����x��
				JLabel fieldNameLabel = new JLabel(fieldName);
				int LABEL_WIDTH = 70;
				fieldNameLabel.setPreferredSize(new Dimension(LABEL_WIDTH, 20));
				Slpc.put(springLayout, "N", fieldNameLabel, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", fieldNameLabel, "W", this, PADDING_LEFT+20);
				add(fieldNameLabel);

				
				JCheckBox checkBoxIaRead = new JCheckBox(japanese ? "Read�i�Ǐo�j" : "Read");
				checkBoxIaRead.addActionListener(this);
				checkBoxIaRead.setActionCommand("DataTable IaRead " + twoDigit(i));
				// PASSWORD�^�t�B�[���h�̏ꍇ�ARead�͂ł��Ȃ�
				if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
					checkBoxIaRead.setSelected(false);
					checkBoxIaRead.setEnabled(false);
				}

				JCheckBox checkBoxIaWrite = new JCheckBox(japanese ? "Write�i�X�V�j" : "Write");
				checkBoxIaWrite.addActionListener(this);
				checkBoxIaWrite.setActionCommand("DataTable IaWrite " + twoDigit(i));

				JCheckBox checkBoxIaExWrite = new JCheckBox(japanese ? "ExclusiveWrite�i�r���I�X�V�j" : "ExclusiveWrite");
				checkBoxIaExWrite.addActionListener(this);
				checkBoxIaExWrite.setActionCommand("DataTable IaExWrite " + twoDigit(i));


				// ���݂� Read, Write, ExWrite�����ɉ����āA�`�F�b�N������O������
				if(authSetToDt.getIaRead(i))
					checkBoxIaRead.setSelected(true);
				else
					checkBoxIaRead.setSelected(false);
				if(authSetToDt.getIaWrite(i))
					checkBoxIaWrite.setSelected(true);
				else
					checkBoxIaWrite.setSelected(false);
				if(authSetToDt.getIaExWrite(i))
					checkBoxIaExWrite.setSelected(true);
				else
					checkBoxIaExWrite.setSelected(false);

				
				// �P�t�B�[���h���̃`�F�b�N�{�b�N�X����
				Slpc.put(springLayout, "N", checkBoxIaRead, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxIaRead, "E", fieldNameLabel, 20);
				add(checkBoxIaRead);
				Slpc.put(springLayout, "N", checkBoxIaWrite, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxIaWrite, "E", checkBoxIaRead, 20);
				add(checkBoxIaWrite);
				Slpc.put(springLayout, "N", checkBoxIaExWrite, "S", textPaneFieldAuthIa, HEIGHT);
				Slpc.put(springLayout, "W", checkBoxIaExWrite, "E", checkBoxIaWrite, 20);
				add(checkBoxIaExWrite);

				// �Ō�̏ꍇ�́A�t�B�[���h�����x�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
				if(i==fieldNum-1) {
					barometerComp = fieldNameLabel;
				}
			}
			// �t�B�[���h���P���Ȃ������ꍇ�́A�e�L�X�g�y�C�����L�����Ă����i��ő��Έʒu�̊�ɂ���j
			if(fieldNum==0) {
				barometerComp = textPaneFieldAuthIa;
			}
		}

		
		// �f�[�^�e�[�u���ɑ΂�IA-Defined�ȃ��[���𒲂ׂ�
		ArrayList<Role> iaHavingRoles = AuthorityManager.getInstance().getIaDefinedRolesOfDataTable((DataTable)table);
		
		// IA-Defined�ȃ��[�������Ȃ� -> �����ŏI��
		if(iaHavingRoles.size()==0) {
			MainFrame.repaintAndValidate();
			return;
		}


		/*
		 * �ȉ��AIA-Defined�ȃ��[�����P�ł�����ꍇ
		 */

		
	
		// ���x���iIA�A�T�C�������j
		JLabel iaAssignAuthLabel = new JLabel(japanese ? "3. �l�����A�T�C������ ... �ǂ̃��[���Ɍl�������A�T�C���ł���悤�ɂ��܂����B" : "3. Indivisual Accessibility Assignment Authority ... Can assign Individual Accessibility to checked roles");
		Slpc.put(springLayout, "W", iaAssignAuthLabel, "W", this, PADDING_LEFT);
		Slpc.put(springLayout, "N", iaAssignAuthLabel, "S", barometerComp, 20);
		add(iaAssignAuthLabel);


		// IA-Defined�Ȋe���[���ɂ��ď���
		for(int i=0; i<iaHavingRoles.size(); i++) {
			Role iaHavingRole = iaHavingRoles.get(i);

			// ����IA-Defined���[���̖��O��t�����`�F�b�N�{�b�N�X
			JCheckBox checkBox = new JCheckBox(iaHavingRole.getRoleName());

			// ActionCommand..."iaAssignAuth ���[����"
			// "iaAssignAuth �w��", "iaAssignAuth �u�t"�Ƃ����������ɂȂ�
			checkBox.addActionListener(this);
			String cmd = "iaAssignAuth " + iaHavingRole.getRoleName();
			checkBox.setActionCommand(cmd);

			
			// ���̃`�F�b�N�{�b�N�X�ɕt���ꂽ���[���ɑ΂��A���ݕҏW���̃��[����IA�A�T�C�����������Ȃ�΁A�`�F�b�N���Ă����i�����Ȃ��Ȃ�`�F�b�N�O���j
			boolean assignable = authSetToDt.checkAssignable(iaHavingRole);
			if(assignable)
				checkBox.setSelected(true);
			else
				checkBox.setSelected(false);

			
			// �`�F�b�N�{�b�N�X�z�u
			Slpc.put(springLayout,	"N",	checkBox,	"S",	iaAssignAuthLabel,	30*i+20);
			Slpc.put(springLayout,	"W",	checkBox,	"W",	this, 				PADDING_LEFT);
			add(checkBox);
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	/*
	 * PURPOSE :
	 * ActionEvent�n���h��
	 */
	public void actionPerformed(ActionEvent e) {
		AuthorityToTable authToTable = AuthorityManager.getInstance().getTableAuth(role, table);

		AuthorityToDataTable authToDt = null;
		if(authToTable instanceof AuthorityToDataTable) {
			authToDt = (AuthorityToDataTable)authToTable;
		}
		AuthorityToAccountTable authToAt = null;
		if(authToTable instanceof AuthorityToAccountTable) {
			authToAt = (AuthorityToAccountTable)authToTable;
		}

		
		String cmd = e.getActionCommand();
		JCheckBox sourceCheckBox = (JCheckBox)e.getSource();
		boolean sourceChecked = sourceCheckBox.isSelected();
		
		if(cmd.startsWith("DataTable Create")) {
			authToDt.authSet.setCreate(sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaDelete")) {
			authToDt.authSet.setRaDelete(sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaRead ")) {	// "DataTable RaRead xx"	xx�͂Q���̃I�t�Z�b�g
			int fieldOffset = twoDigit(cmd.substring("DataTable RaRead ".length(), "DataTable RaRead ".length()+2));
			authToDt.authSet.setRaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaWrite ")) {	// "DataTable RaWrite xx"	xx�͂Q���̃I�t�Z�b�g
			int fieldOffset = twoDigit(cmd.substring("DataTable RaWrite ".length(), "DataTable RaWrite ".length()+2));
			authToDt.authSet.setRaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable RaExWrite ")) {	// "DataTable RaExWrite xx"	xx�͂Q���̃I�t�Z�b�g
			int fieldOffset = twoDigit(cmd.substring("DataTable RaExWrite ".length(), "DataTable RaExWrite ".length()+2));
			authToDt.authSet.setRaExWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.equals("DataTable iaSwitch")) {
			authToDt.authSet.setHavingIa(sourceChecked);

			// IA���ǉ��E�������ꂽ���Ƃ�AuthorityManager�ɒm�点��
			if(sourceChecked) {
				AuthorityManager.getInstance().informIaAddition(role, table);
			}
			else if(!sourceChecked) {
				AuthorityManager.getInstance().informIaDeletion(role, table);
			}

			// IA���`���镔���̑S�R���|�[�l���g��ǉ�/�폜�������̂ŁA�p�l���̃R���|�[�l���g���Ĕz�u
			relocate(role, table);
		}
		else if(cmd.startsWith("DataTable IaDelete")) {
			authToDt.authSet.setIaDelete(sourceChecked);
		}
		else if(cmd.startsWith("DataTable IaRead ")) {	// "DataTable IaRead xx"	xx�͂Q���̃I�t�Z�b�g
			int fieldOffset = twoDigit(cmd.substring("DataTable IaRead ".length(), "DataTable IaRead ".length()+2));
			authToDt.authSet.setIaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable IaWrite ")) {	// "DataTable IaWrite xx"	xx�͂Q���̃I�t�Z�b�g
			int fieldOffset = twoDigit(cmd.substring("DataTable IaWrite ".length(), "DataTable IaWrite ".length()+2));
			authToDt.authSet.setIaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("DataTable IaExWrite ")) {	// "DataTable IaExWrite xx"	xx�͂Q���̃I�t�Z�b�g
			int fieldOffset = twoDigit(cmd.substring("DataTable IaExWrite ".length(), "DataTable IaExWrite ".length()+2));
			authToDt.authSet.setIaExWrite(fieldOffset, sourceChecked);
		}
		/*
		 * ���郍�[���ɑ΂���IA�A�T�C��������ON,OFF�i�f�[�^�e�[�u���̏ꍇ�̂ݐݒ肪�K�v�j
		 * �R�}���h�́@"iaAssignAuth " + ���̃��[���̖��O
		 */
		else if(cmd.startsWith("iaAssignAuth ")) {
			// IA�A�T�C���Ώۂ̃��[�����擾
			String assignTargetRoleName = cmd.substring("iaAssignAuth ".length());
			Role assignTargetRole = RoleManager.getInstance().getRoleByName(assignTargetRoleName);

			Debug.out("���[���u" + role.getRoleName() + "�v�́A���[���u" + assignTargetRole.getRoleName() + "�v�ɑ΂���IA�A�T�C��������ύX���܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			
			// �`�F�b�N�{�b�N�X��ON,OFF�ɉ����A���̃��[���ւ�IA�A�T�C��������ON,OFF
			if(sourceChecked) {
				authToDt.authSet.enableIaAssignAuth(assignTargetRole);
				Debug.out("ON�ɂ��܂����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else {
				authToDt.authSet.disableIaAssignAuth(assignTargetRole);
				Debug.out("OFF�ɂ��܂����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
		else if(cmd.startsWith("AccountTable Create ")) {
			String accountOwnerRoleName = cmd.substring("AccountTable Create ".length());
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setCreate(sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaDelete ")) {
			String accountOwnerRoleName = cmd.substring("AccountTable RaDelete ".length());
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaDelete(sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaRead ")) {	// "AccountTable RaRead xx yyyyy"	xx�͂Q���̃I�t�Z�b�g�Ayyyyy�̓A�J�E���g�I�[�i�[���[����
			int fieldOffset = twoDigit(cmd.substring("AccountTable RaRead ".length(), "AccountTable RaRead ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable RaRead ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaWrite ")) {	// "AccountTable RaWrite xx yyyyy"	xx�͂Q���̃I�t�Z�b�g�Ayyyyy�̓A�J�E���g�I�[�i�[���[����
			int fieldOffset = twoDigit(cmd.substring("AccountTable RaWrite ".length(), "AccountTable RaWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable RaWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable RaExWrite ")) {	// "AccountTable RaExWrite xx yyyyy"	xx�͂Q���̃I�t�Z�b�g�Ayyyyy�̓A�J�E���g�I�[�i�[���[����
			int fieldOffset = twoDigit(cmd.substring("AccountTable RaExWrite ".length(), "AccountTable RaExWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable RaExWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setRaExWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaDelete ")) {
			String accountOwnerRoleName = cmd.substring("AccountTable IaDelete ".length());
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaDelete(sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaRead ")) {	// "AccountTable IaRead xx yyyyy"	xx�͂Q���̃I�t�Z�b�g�Ayyyyy�̓A�J�E���g�I�[�i�[���[����
			int fieldOffset = twoDigit(cmd.substring("AccountTable IaRead ".length(), "AccountTable IaRead ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable IaRead ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaRead(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaWrite ")) {	// "AccountTable IaWrite xx yyyyy"	xx�͂Q���̃I�t�Z�b�g�Ayyyyy�̓A�J�E���g�I�[�i�[���[����
			int fieldOffset = twoDigit(cmd.substring("AccountTable IaWrite ".length(), "AccountTable IaWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable IaWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaWrite(fieldOffset, sourceChecked);
		}
		else if(cmd.startsWith("AccountTable IaExWrite ")) {	// "AccountTable IaExWrite xx yyyyy"	xx�͂Q���̃I�t�Z�b�g�Ayyyyy�̓A�J�E���g�I�[�i�[���[����
			int fieldOffset = twoDigit(cmd.substring("AccountTable IaExWrite ".length(), "AccountTable IaExWrite ".length()+2));
			String accountOwnerRoleName = cmd.substring("AccountTable IaExWrite ".length()+3);
			authToAt.getAuthorityToAccountByAccountOwnerRoleName(accountOwnerRoleName).setIaExWrite(fieldOffset, sourceChecked);
		}
		else {
			// ��ERROR : �s���ȃR�}���h
			Debug.informError();
			Debug.error("Panel_AuthEdit_Bottom actionPerformed() : �s����ActionCommand����M���܂��� : " + cmd, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	
	public void setRoleAndTable(Role role, SuperTable table) {
		this.role = role;
		this.table = table;
	}

	
	
	
	
	public int getPanelWidth() {
		return panelWidth;
	}

	
	

	public String twoDigit(int number) {
		if(number<0 || number>=100) {
			Debug.informError();
			Debug.error("�s���Ȓl�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		else if(number>=0 && number<10) {
			return "0" + Integer.toString(number);
		}
		return Integer.toString(number);
	}

	
	
	
	public int twoDigit(String twoDigitStr) {
		if(twoDigitStr.length()!=2) {
			Debug.informError();
			Debug.error("�s���Ȓl�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		else if(twoDigitStr.startsWith("0")) {
			return Integer.parseInt(twoDigitStr.substring(1));
		}
		return Integer.parseInt(twoDigitStr);
	}
	
	
	
	public int getPanelHeight() {
		return panelHeight;
	}
	
	
	
	
	
	public static PanelAuthEditAbove getInstance() {
		return PanelAuthEditAbove.obj;
	}




	public static void updateInstance(PanelAuthEditAbove newObject) {
		PanelAuthEditAbove.obj = newObject;
	}

}

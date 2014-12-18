package view.peEdit;

import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import debug.Debug;

import mainFrame.MainFrame;
import pageElement.*;
import property.GeneratorProperty;
import table.SuperTable;
import table.TableManager;
import table.field.Field;
import webPage.WebPage;
import webPage.WebPageManager;
import utility.*;




public class PanelPeEditRightSupporterSaif implements ActionListener,Serializable {
	Panel_PeEdit_Right panelRight;
	SerializableSpringLayout springLayout;

	// �ҏW����Saif�i�ҏW���[�h�ł�PageElementSaif�C���X�^���X���A�ǉ����[�h�ł�null���i�[�����j
	public PageElementSaif SaifToEdit;

	// �R���|�[�l���g�iNullPointerException����̂��߁A���������Ă���j
	JTextPane textPaneSaifName = new JTextPane();
	JTextField textFieldSaifName = new JTextField();
	JTextPane textPaneSaifKind = new JTextPane();
	JComboBox comboBoxSaifKind = new JComboBox();
	JButton buttonEditExecute = new JButton();
	JButton buttonAddExecute = new JButton();
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�!
	 */
	private static PanelPeEditRightSupporterSaif obj = new PanelPeEditRightSupporterSaif();
	
	
	
	
	
	private PanelPeEditRightSupporterSaif() {
		this.panelRight = Panel_PeEdit_Right.getInstance();
		this.springLayout = panelRight.springLayout;
	}





	/*
	 * NOTICE :
	 * �ҏW�̏ꍇ�͕ҏW����SAIF��n���A�ǉ��̏ꍇ��null��n��
	 */
	public void locateForSaif(PageElementSaif SaifToEdit) {
		boolean japanese = GeneratorProperty.japanese();
		
		this.SaifToEdit = SaifToEdit;
		
		// �S�R���|�[�l���g����
		panelRight.removeAll();

		int PADD_LEFT = 20;

		
		
		// �e�L�X�g�y�C��
		textPaneSaifName = new JTextPane();
		textPaneSaifName.setEditable(false);
		textPaneSaifName.setText(japanese?"1. �p�����[�^�� :":"1. Parameter Name :");
		Slpc.put(springLayout, "N", textPaneSaifName, "N", panelRight, 20);
		Slpc.put(springLayout, "W", textPaneSaifName, "W", panelRight, PADD_LEFT);
		panelRight.add(textPaneSaifName);
		
		// �e�L�X�g�t�B�[���h
		textFieldSaifName = new JTextField();
		Slpc.put(springLayout, "N", textFieldSaifName, "S", textPaneSaifName, 10);
		Slpc.put(springLayout, "W", textFieldSaifName, "W", panelRight, PADD_LEFT);
		Slpc.put(springLayout, "E", textFieldSaifName, "W", textFieldSaifName, 100);
		panelRight.add(textFieldSaifName);

		

		// �e�L�X�g�y�C��
		textPaneSaifKind = new JTextPane();
		textPaneSaifKind.setEditable(false);
		textPaneSaifKind.setText(japanese?"�p�����[�^�̃f�[�^�^�C�v":"2. Parameter Data Type :");
		Slpc.put(springLayout, "N", textPaneSaifKind, "S", textFieldSaifName, 20);
		Slpc.put(springLayout, "W", textPaneSaifKind, "W", panelRight, PADD_LEFT);
		panelRight.add(textPaneSaifKind);

		
		// �R���{�{�b�N�X
		String[] safKinds = {
				japanese?"---�I�����ĉ�����---":"------Select------",
				Field.getDataTypeExpression(Field.DATATYPE_INT),
				Field.getDataTypeExpression(Field.DATATYPE_VARCHAR),
				Field.getDataTypeExpression(Field.DATATYPE_DATETIME),
				Field.getDataTypeExpression(Field.DATATYPE_DATE),
				Field.getDataTypeExpression(Field.DATATYPE_TIME)
		};
		comboBoxSaifKind = new JComboBox(safKinds);

		
		// �ҏW���[�h�̏ꍇ -> �R���{�{�b�N�X�i�e�[�u���I���j�̍��ڂ��A�ҏW�Ώۂ�Saif�Ɍ��ݐݒ肳��Ă�����̂ɂ��Ă���
		if(SaifToEdit instanceof PageElementSaif) {
			for(int i=0; i<comboBoxSaifKind.getItemCount(); i++) {
				String item = (String)comboBoxSaifKind.getItemAt(i);
				String editingSaifKind = SaifToEdit.getSaifKind();
				if(item.equals(editingSaifKind)) {
					comboBoxSaifKind.setSelectedIndex(i);
					break;
				}
			}
		}
		// �ǉ����[�h�̏ꍇ -> �R���{�{�b�N�X�̍��ڂ��Ajapanese?"---�I�����ĉ�����---":"------Select------"�ɂ��Ă���
		else {
			comboBoxSaifKind.setSelectedIndex(0);
		}

		Slpc.put(springLayout, "N", comboBoxSaifKind, "S", textPaneSaifKind, 20);
		Slpc.put(springLayout, "W", comboBoxSaifKind, "W", panelRight, PADD_LEFT);
		panelRight.add(comboBoxSaifKind);
		


		// �ҏW���[�h
		if(SaifToEdit instanceof PageElementSaif) {
			// �ҏW���s�{�^��
			buttonEditExecute = new JButton("�ȏ�̓��e�ɕύX����");
			buttonEditExecute.addActionListener(this);
			buttonEditExecute.setActionCommand("�ҏW���s");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonEditExecute,	50,	SpringLayout.SOUTH,	comboBoxSaifKind);
			springLayout.putConstraint(SpringLayout.WEST,	buttonEditExecute,	20,	SpringLayout.WEST,	panelRight);
			panelRight.add(buttonEditExecute);
		}
		// �ǉ����[�h
		else {
			// �ǉ����s�{�^��
			buttonAddExecute = new JButton(japanese?"�y�[�W�\���v�f��ǉ�":"Add Page Factor");
			buttonAddExecute.addActionListener(this);
			buttonAddExecute.setActionCommand("�ǉ����s");
			springLayout.putConstraint(SpringLayout.NORTH,	buttonAddExecute,	50,	SpringLayout.SOUTH, comboBoxSaifKind);
			springLayout.putConstraint(SpringLayout.WEST,	buttonAddExecute,	20,	SpringLayout.WEST, panelRight);
			panelRight.add(buttonAddExecute);
		}

		
		
		MainFrame.repaintAndValidate();
	}

	
	
	
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		
		String cmd = e.getActionCommand();
		WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();

		
		
		if(cmd.equals("�ҏW���s") || cmd.equals("�ǉ����s")) {
			// SAIF�̖��O�擾
			String saifName = this.textFieldSaifName.getText();
			if(saifName.equals("")) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), japanese?"�p�����[�^������͂��ĉ������B":"Parameter name is blank.");
				return;
			}
			
			
			// SAIF�̃f�[�^�^�C�v�擾
			String saifKindExp = (String)comboBoxSaifKind.getSelectedItem();
			if(saifKindExp.equals(japanese?"---�I�����ĉ�����---":"------Select------")) {
				JOptionPane.showMessageDialog(MainFrame.getInstance(), japanese?"�f�[�^�^�C�v�����I���ł�":"Data type is not selected.");
				return;
			}
			String saifKind = null;
			if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_INT))) {
				saifKind = PageElementSaif.KIND_INT;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_VARCHAR))) {
				saifKind = PageElementSaif.KIND_VARCHAR;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_DATETIME))) {
				saifKind = PageElementSaif.KIND_DATETIME;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_DATE))) {
				saifKind = PageElementSaif.KIND_DATE;
			}
			else if(saifKindExp.equals(Field.getDataTypeExpression(Field.DATATYPE_TIME))) {
				saifKind = PageElementSaif.KIND_TIME;
			}
			else {
				Debug.informError();
				Debug.error("�z��O�̒l�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return;
			}
			
			
	
			// �ǉ��E�ҏW�̎��s
			if(cmd.equals("�ǉ����s")) {
				// PageElementSaif���� -> �y�[�W�֒ǉ�
				PageElementSaif Saif = new PageElementSaif(editingPage, saifName, saifKind);
				editingPage.addPageElement(Saif);
			}
			else if(cmd.equals("�ҏW���s")) {
				// �ҏW�Ώۂ�Saif���A�w��ʂ�ɕύX
				PageElementSaif editingSaif = (PageElementSaif)Panel_PeEdit_Left.getInstance().getFocusedPageElement();
				editingSaif.saifName = saifName;
				editingSaif.saifKind = saifKind;

				// �y�[�W�G�������g�ҏW�ɕt�����鏈�����s��
				PageElementObserver.getInstance().informPeEdition(editingSaif);
			}

			// �E�p�l���̑S�R���|�[�l���g����
			panelRight.removeAll();
			// ���p�l���̃R���|�[�l���g�Ĕz�u
			Panel_PeEdit_Left.getInstance().locateTextPanes();
		}

		// ���C���t���[���ĕ`��
		MainFrame.repaintAndValidate();
	}





	public static PanelPeEditRightSupporterSaif getInstance() {
		return PanelPeEditRightSupporterSaif.obj;
	}
	
	
	public static void updateInstance(PanelPeEditRightSupporterSaif newObject) {
		PanelPeEditRightSupporterSaif.obj = newObject;
	}

}

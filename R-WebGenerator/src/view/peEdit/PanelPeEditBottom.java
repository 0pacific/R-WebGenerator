package view.peEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.ButtonLoadWork;
import view.ButtonSaveWork;
import view.transProcEdit.serviceArgsWindow.*;
import webPage.WebPage;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import debug.Debug;

import mainFrame.MainFrame;

import pageElement.PageElement;
import pageElement.PageElementCreateForm;
import pageElement.PageElementDeleteForm;
import pageElement.PageElementLoginForm;
import pageElement.PageElementSaif;
import pageElement.PageElementText;
import pageElement.PageElementUpdateForm;
import pageElement.PageElement_HyperLink;
import pageElement.PageElementDisplayArea;
import pageElement.PageElementTableDisplay;
import property.GeneratorProperty;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.Serializable;


/*
 * NOTICE : Singleton
 */
public class PanelPeEditBottom extends JPanel implements ActionListener,Serializable {
	public JButton additionButton;
	public JComboBox peTypeCombo;
	public JButton editButton;
	public JButton deleteButton;

	// ����ʂւ̑J�ڃ{�^��
	public JButton transButton_transEdit;
	
	// �p�l���̃T�C�Y
	public static final int panelWidth = MainFrame.frameWidth;
	public static final int panelHeight = 150;

	public SerializableSpringLayout springLayout;
	
	// �V���A���C�Y�{�^��
	ButtonSaveWork btnSaveWork;
	// �f�V���A���C�Y�{�^��
	ButtonLoadWork btnLoadWork;

	// �����̔z��ɗv�f��t�������Ƃ��́A�K���Ō���ɕt�������悤�ɁI
	// ���r���ɕt�������������ŃG���[�ɂȂ�f�o�b�O�Ɏ��Ԃ�H����
	public static String[] peTypeList;
	
	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static PanelPeEditBottom obj = new PanelPeEditBottom();

	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private PanelPeEditBottom() {
		if(GeneratorProperty.japanese()) {
			PanelPeEditBottom.peTypeList = new String[]{"�e�L�X�g", "�n�C�p�[�����N", "�e�[�u���\��", "���R�[�h�쐬�t�H�[��", "�X�V�t�H�[��", "���R�[�h�폜�t�H�[��", "���O�C���t�H�[��", "�T�[�r�X�o�̓e�[�u���\��", "�T�[�r�X�������̓t�H�[��"};
		}
		else {
			PanelPeEditBottom.peTypeList = new String[]{"Text", "Hyperlink", "Table Display", "Record Creation Form", "Record Deletion Form", "Update Form", "Login Form", "Service Output Table Display", "Service Argument Input Form"};
		}
		
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);

		setBackground(Color.DARK_GRAY);
	}
	
	
	
	public void locateComps() {
		boolean japanese = GeneratorProperty.japanese();
		
		removeAll();
		
		// �R���{�{�b�N�X : �ǉ�����Page Element�̃^�C�v��I��
		peTypeCombo = new JComboBox(PanelPeEditBottom.peTypeList);
		peTypeCombo.addActionListener(this);
		peTypeCombo.setActionCommand("Page Element�^�C�v�I��");
		Slpc.put(springLayout, "N", peTypeCombo, "N", this, 20);
		Slpc.put(springLayout, "W", peTypeCombo, "W", this, 20);
		add(peTypeCombo);

		
		
		// Page Element �ǉ��{�^��
		additionButton = japanese ? new JButton("���y�[�W�\���v�f�Ƃ��Ēǉ�") : new JButton("Add Page Factor");
		additionButton.addActionListener(this);
		additionButton.setActionCommand("Page Element �ǉ�");
		Slpc.put(springLayout, "N", additionButton, "N", peTypeCombo, 0);
		Slpc.put(springLayout, "W", additionButton, "E", peTypeCombo, 10);
		add(additionButton);

		
		
		// Page Element �ҏW�{�^��
		editButton = japanese ? new JButton("�I�𒆂̃y�[�W�\���v�f��ҏW") : new JButton("Edit Page Factor");
		editButton.addActionListener(this);
		editButton.setActionCommand("Page Element �ҏW");
		editButton.setEnabled(false);
		Slpc.put(springLayout, "N", editButton, "S", peTypeCombo, 10);
		Slpc.put(springLayout, "W", editButton, "W", peTypeCombo, 0);
		add(editButton);

		// Page Element �폜�{�^��
		deleteButton = japanese ? new JButton("�I�𒆂̃y�[�W�\���v�f���폜") : new JButton("Delete Page Factor");
		deleteButton.addActionListener(this);
		deleteButton.setActionCommand("Page Element �폜");
		deleteButton.setEnabled(false);
		Slpc.put(springLayout, "N", deleteButton, "N", editButton, 0);
		Slpc.put(springLayout, "W", deleteButton, "E", editButton, 10);
		add(deleteButton);
		
		// �J�ڕҏW��ʂւ̑J�ڃ{�^��
		transButton_transEdit = japanese ? new JButton("Web�y�[�W�E�J�ڌ�����`��ʂ�") : new JButton("Go To Web Page Definition");
		transButton_transEdit.addActionListener(this);
		transButton_transEdit.setActionCommand("�J�� - �y�[�W�J�ڒ�`���");
		Slpc.put(springLayout, "N", transButton_transEdit, "N", this, 20);
		Slpc.put(springLayout, "E", transButton_transEdit, "E", this, -20);
		add(transButton_transEdit);

		// �f�V���A���C�Y�{�^��
		btnLoadWork = new ButtonLoadWork();
		Slpc.put(springLayout, "N", btnLoadWork, "S", transButton_transEdit, 20);
		Slpc.put(springLayout, "E", btnLoadWork, "E", this, -20);
		add(btnLoadWork);

		// �V���A���C�Y�{�^��
		btnSaveWork = new ButtonSaveWork();
		Slpc.put(springLayout, "N", btnSaveWork, "N", btnLoadWork, 0);
		Slpc.put(springLayout, "E", btnSaveWork, "W", btnLoadWork, -20);
		add(btnSaveWork);

		MainFrame.getInstance().repaintAndValidate();
	}


	public void informPageElementFocus() {
		editButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}
	
	public void informPageElementUnfocus() {
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		// Page Element �ǉ�
		if(cmd.equals("Page Element �ǉ�")) {
			String peType = (String)peTypeCombo.getSelectedItem();

			// �e�[�u���\���̈�
			if(peType.equals("�e�[�u���\��") || peType.equals("Table Display")) {
				PanelPeEditRightSupporterTableDisplay.getInstance().locateForTableDisplay(null);
			}
			// ���ʃe�[�u���\��
			else if(peType.equals("�T�[�r�X�o�̓e�[�u���\��") || peType.equals("Service Output Table Display")) {
				Debug.out("�T�[�r�X�o�̓e�[�u���\���̒ǉ����w������܂���");
				Panel_PeEdit_Right.getInstance().locateForResultTableDisplay(false);
			}
			// �n�C�p�[�����N
			else if(peType.equals("�n�C�p�[�����N") || peType.equals("Hyperlink")) {
				Panel_PeEdit_Right.getInstance().locateForHyperLink(false);
			}
			// Create�t�H�[��
			else if(peType.equals("���R�[�h�쐬�t�H�[��") || peType.equals("Record Creation Form")) {
				Panel_PeEdit_Right.getInstance().locateForCreateForm(false);
			}
			// Delete�t�H�[��
			else if(peType.equals("���R�[�h�폜�t�H�[��") || peType.equals("Record Deletion Form")) {
				PanelPeEditRightSupporterDeleteForm.getInstance().locateForDeleteForm(null);
			}
			// Update�t�H�[��
			else if(peType.equals("�X�V�t�H�[��") || peType.equals("Update Form")) {
				PanelPeEditRightSupporterUpdateForm.getInstance().locateForUpdateForm(null);
			}
			// �e�L�X�g
			else if(peType.equals("�e�L�X�g") || peType.equals("Text")) {
				Panel_PeEdit_Right.getInstance().locateForText(false);
			}
			// ���O�C���t�H�[��
			else if(peType.equals("���O�C���t�H�[��") || peType.equals("Login Form")) {
				Panel_PeEdit_Right.getInstance().locateForLoginForm(false);
			}
			// �T�[�r�X�������̓t�H�[��
			else if(peType.equals("�T�[�r�X�������̓t�H�[��") || peType.equals("Service Argument Input Form")) {
				PanelPeEditRightSupporterSaif.getInstance().locateForSaif(null);
			}
		}
		// Page Element �ҏW
		else if(cmd.equals("Page Element �ҏW")) {
			PageElement targetPe = Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			
			if(targetPe instanceof PageElementTableDisplay) {
				PanelPeEditRightSupporterTableDisplay.getInstance().locateForTableDisplay((PageElementTableDisplay)targetPe);
			}
			else if(targetPe instanceof PageElement_HyperLink) {
				Panel_PeEdit_Right.getInstance().locateForHyperLink(true);
			}
			else if(targetPe instanceof PageElementCreateForm) {
				Panel_PeEdit_Right.getInstance().locateForCreateForm(true);
			}
			else if(targetPe instanceof PageElementDeleteForm) {
				PanelPeEditRightSupporterDeleteForm.getInstance().locateForDeleteForm((PageElementDeleteForm)targetPe);
			}
			else if(targetPe instanceof PageElementUpdateForm) {
				PanelPeEditRightSupporterUpdateForm.getInstance().locateForUpdateForm((PageElementUpdateForm)targetPe);
			}
			else if(targetPe instanceof PageElementText) {
				Panel_PeEdit_Right.getInstance().locateForText(true);
			}
			else if(targetPe instanceof PageElementLoginForm) {
				Panel_PeEdit_Right.getInstance().locateForLoginForm(true);
			}
			else if(targetPe instanceof PageElementDisplayArea) {
				Panel_PeEdit_Right.getInstance().locateForResultTableDisplay(true);
			}
			else if(targetPe instanceof PageElementSaif) {
				PanelPeEditRightSupporterSaif.getInstance().locateForSaif((PageElementSaif)targetPe);
			}
		}
		else if(cmd.equals("Page Element �폜")) {
			String msg1 = GeneratorProperty.japanese() ? "�폜���Ă���낵���ł����H�֘A����J�ڂ₻�̑J�ڃv���Z�X�Ȃǂ��폜����܂��B" : "Delete the page factor?";
			int confirm = JOptionPane.showConfirmDialog(this, msg1, "�y�[�W�\���v�f�̍폜", JOptionPane.OK_CANCEL_OPTION);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// �L�����Z��
				return;
			}
			
			WebPage editingPage = Panel_PeEdit_Left.getInstance().getEditingPage();
			PageElement focusedPe = Panel_PeEdit_Left.getInstance().getFocusedPageElement();
			editingPage.removePageElement(focusedPe);

			Panel_PeEdit_Left.getInstance().locateTextPanes();
			Panel_PeEdit_Right.getInstance().removeAll();
		}
		// �y�[�W�J�ڒ�`��ʂւ̑J��
		else if(cmd.equals("�J�� - �y�[�W�J�ڒ�`���")) {
			MainFrame.getInstance().shiftToWebPageDefinition();
		}

		MainFrame.repaintAndValidate();
	}
	
	
	
	
	public static PanelPeEditBottom getInstance() {
		return PanelPeEditBottom.obj;
	}





	/*
	 * NOTICE :
	 * ���e�X�g�p
	 */
	public void setPeType(int jComboBoxIndex) {
		peTypeCombo.setSelectedIndex(jComboBoxIndex);
	}

	/*
	 * NOTICE :
	 * ���e�X�g�p
	 */
	public void clickAdditionButton() {
		additionButton.doClick();
	}

	/*
	 * NOTICE :
	 * ���e�X�g�p
	 */
	public void clickTransEditShiftButton() {
		transButton_transEdit.doClick();
	}
	
	
	public static void updateInstance(PanelPeEditBottom newObject) {
		PanelPeEditBottom.obj = newObject;
	}

}

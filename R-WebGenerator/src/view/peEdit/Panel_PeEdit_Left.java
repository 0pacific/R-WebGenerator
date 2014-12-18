package view.peEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.serviceArgsWindow.*;
import webPage.WebPage;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.io.Serializable;

import javax.swing.border.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import mainFrame.MainFrame;

import pageElement.*;
import property.GeneratorProperty;

import debug.Debug;

/*
 * NOTICE : Singleton
 */
@SuppressWarnings("serial")
public class Panel_PeEdit_Left extends JPanel implements MouseListener,Serializable {
	private WebPage page;
	private ArrayList<JTextPane> peTextPanes = new ArrayList<JTextPane>();

	// ���݃t�H�[�J�X����Ă���JTextPane��ێ�����
	private JTextPane focusedTextPane = null;

	public SerializableSpringLayout springLayout;
	
	// �p�l���̃T�C�Y
	public static final int panelWidth = 300;
	public static final int panelHeight = 1200;

	// JTextPane �̏c��
	private static final int textPaneHeight = 100;

	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static Panel_PeEdit_Left obj = new Panel_PeEdit_Left();


	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private Panel_PeEdit_Left() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		
		setBackground(Color.WHITE);
		addMouseListener(this);

		setAutoscrolls(true);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * Page Element �ҏW��ʂɑJ�ڂ����u�Ԃɍs������
	 */
	public void initOnShift(WebPage page) {
		setPage(page);
		locateTextPanes();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �eJTextPane�iPage Element ��\���j�̔z�u���s��
	 */
	public void locateTextPanes() {
		boolean japanese = GeneratorProperty.japanese();
		
		// �S�R���|�[�l���g����
		removeAll();


		// ���]��
		int PADD_LEFT = 20;
		
		
		// �e�L�X�g�G���A�i�y�[�W���j
		JTextArea textArea_pageName = new JTextArea(japanese?this.page.pageFileName+"�̃y�[�W�\���v�f��ҏW��":"Editing page factors of \""+this.page.pageFileName+"\"", 1, 20);
		Slpc.put(springLayout, "N", textArea_pageName, "N", this, 10);
		Slpc.put(springLayout, "W", textArea_pageName, "W", this, PADD_LEFT);
		add(textArea_pageName);

		
		for(int i=0; i<page.getPageElementNum(); i++) {
			// JTextPane�I�u�W�F�N�g������Ȃ���Βǉ�
			if(peTextPanes.size() <= i) {
				peTextPanes.add(new JTextPane());
				JTextPane jtp = peTextPanes.get(i);
			    jtp.setEditable(false);
			    jtp.addMouseListener(this);
			}

			JTextPane textPane = peTextPanes.get(i);
			textPane.setBackground(Color.WHITE);
			PageElement pe = page.getPageElement(i);

			// EtchedBorder�C���X�^���X
			EtchedBorder eBorder = null;

			// PageElement�̎�ނ������{�[�_�[�^�C�g��
			String borderTitle = null;
			
			// �e�[�u���\���̈�̏ꍇ
			if(pe instanceof PageElementTableDisplay) {
				borderTitle = japanese ? "�e�[�u���\��" : "Table Display";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.ORANGE, Color.ORANGE);
				
				PageElementTableDisplay td = (PageElementTableDisplay)pe;
				String text = japanese ?
								"�e�[�u��:"+ td.getTable().getTableName() :
								"Table:" + td.getTable().getTableName();
				textPane.setText(text);
			}
			// ���ʃe�[�u���\���̈�̏ꍇ
			else if(pe instanceof PageElementDisplayArea) {
				borderTitle = japanese ? "�T�[�r�X�o�̓e�[�u���\��" : "Service Output Table Display";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.GREEN, Color.GREEN);
				textPane.setText("");
			}
			// Create�t�H�[���̏ꍇ
			else if(pe instanceof PageElementCreateForm) {
				borderTitle = japanese ? "���R�[�h�쐬�t�H�[��" : "Record Creation Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.PINK, Color.PINK);

				PageElementCreateForm cf = (PageElementCreateForm)pe;
				String tableName = cf.table.getTableName();
				String text = japanese ? 
								"�e�[�u���F" + tableName + "\n" + "�J�ڐ�F" + cf.destPage.pageFileName :
								"Table:" + tableName + "\n" + "Destination:" + cf.destPage.pageFileName;
				textPane.setText(text);
			}
			// Delete�t�H�[���̏ꍇ
			else if(pe instanceof PageElementDeleteForm) {
				borderTitle = japanese ? "���R�[�h�폜�t�H�[��" : "Record Delete Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.PINK, Color.PINK);

				PageElementDeleteForm cf = (PageElementDeleteForm)pe;
				String tableName = cf.table.getTableName();
				String text = japanese ? 
						"�e�[�u���F" + tableName + "\n" + "�J�ڐ�F" + cf.destPage.pageFileName :
						"Table:" + tableName + "\n" + "Destination:" + cf.destPage.pageFileName;
				textPane.setText(text);
			}
			// Update�t�H�[���̏ꍇ
			else if(pe instanceof PageElementUpdateForm) {
				borderTitle = japanese ? "�X�V�t�H�[��" : "Update Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.PINK, Color.PINK);

				PageElementUpdateForm updateForm = (PageElementUpdateForm)pe;
				String tableName = updateForm.getTable().getTableName();
				String text = japanese ? 
						"�e�[�u���F" + tableName + "\n" + "�J�ڐ�F" + updateForm.destPage.pageFileName :
						"Table:" + tableName + "\n" + "Destination:" + updateForm.destPage.pageFileName;
				textPane.setText(text);
			}
			// SAIF�̏ꍇ
			else if(pe instanceof PageElementSaif) {
				borderTitle = japanese ? "�T�[�r�X�������̓t�H�[��" : "Service Argument Input Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.YELLOW, Color.YELLOW);

				PageElementSaif saif = (PageElementSaif)pe;
				String text = japanese ?
								"�p�����[�^���F " + saif.getSaifName() + "\n" + "�f�[�^�^�C�v�F " + saif.getSaifKind() :
								"Parameter Name: " + saif.getSaifName() + "\n" + "Data Type: " + saif.getSaifKind();
				textPane.setText(text);
			}
			// �n�C�p�[�����N�̏ꍇ
			else if(pe instanceof PageElement_HyperLink) {
				borderTitle = japanese ? "�n�C�p�[�����N" : "Hyperlink";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.BLUE, Color.BLUE);

				PageElement_HyperLink hl = (PageElement_HyperLink)pe;
				String text = japanese ?
								"�J�ڐ�F" + hl.getDestPage().pageFileName + "\n" + "�e�L�X�g�F\"" + hl.getText() + "\"" :
								"Destination:" + hl.getDestPage().pageFileName + "\n" + "Text:\"" + hl.getText() + "\"";
				textPane.setText(text);
			}
			// �e�L�X�g�̏ꍇ
			else if(pe instanceof PageElementText) {
				borderTitle = japanese ? "�e�L�X�g" : "Text";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.CYAN, Color.CYAN);

				PageElementText textPe = (PageElementText)pe;
				textPane.setText(textPe.containingText);
			}
			// ���O�C���t�H�[���̏ꍇ
			if(pe instanceof PageElementLoginForm) {
				borderTitle = japanese ? "���O�C���t�H�[��" : "Login Form";
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.MAGENTA, Color.MAGENTA);
				
				PageElementLoginForm loginForm = (PageElementLoginForm)pe;
				String atName = loginForm.accountTable.getTableName();

				String destWebPageFileName = loginForm.destWebPage.pageFileName;

				String text = japanese ?
								"�A�J�E���g�e�[�u���F\n" + atName + "\n" + "�J�ڐ�F\n" + destWebPageFileName :
								"Account Table:\n" + atName + "\n" + "Destination:" + destWebPageFileName;
				textPane.setText(text);
			}

			// �t�H�[�J�X����Ă��遨�Ԙg�ɕύX�A�e�L�X�g�y�C���̔w�i�F�ύX
			if(textPane==focusedTextPane) {
				eBorder = new EtchedBorder(EtchedBorder.RAISED, Color.RED, Color.RED);
				textPane.setBackground(Color.LIGHT_GRAY);
			}

			// �{�[�_�[�Z�b�g
			TitledBorder tBorder = new TitledBorder(eBorder);
			tBorder.setTitle(borderTitle);
			tBorder.setTitleJustification(TitledBorder.LEFT);
			tBorder.setTitlePosition(TitledBorder.TOP);
			tBorder.setTitleFont(new Font("Serif", Font.PLAIN, Panel_TpEdit_Above.PAN_STR_SIZE));
			textPane.setBorder(tBorder);
			
			int WIDTH = Panel_PeEdit_Left.panelWidth-(PADD_LEFT*2);
			textPane.setPreferredSize(new Dimension(WIDTH, Panel_PeEdit_Left.textPaneHeight));
			Slpc.put(springLayout, "N", textPane, "S", textArea_pageName, 20+(i*(Panel_PeEdit_Left.textPaneHeight+20)));
			Slpc.put(springLayout, "W", textPane, "W", this, PADD_LEFT);
			add(textPane);
		}

		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}
	
	
	public WebPage getEditingPage() {
		return page;
	}
	
	public void setPage(WebPage page) {
		this.page = page;
	}

	public void mousePressed(MouseEvent e) {
	}
	public void mouseClicked(MouseEvent e) {
		// �e�L�X�g�y�C���̊O�������i�p�l���j�ł̃N���b�N -> �t�H�[�J�X���O���ďI��
		if(!(e.getSource() instanceof JTextPane)) {
			focusedTextPane = null;
			PanelPeEditBottom.getInstance().informPageElementUnfocus();
			locateTextPanes();
			return;
		}

		// �e�L�X�g�y�C���̓��������ł̃N���b�N -> �t�H�[�J�X�����킹��
		JTextPane jtp = (JTextPane)e.getSource();
		focusedTextPane = jtp;
		PanelPeEditBottom.getInstance().informPageElementFocus();

		// ���b��@������pe���g���Ǝv��
		PageElement pe = getCorrespondantPageElement(jtp);

		// �e�L�X�g�y�C���Ĕz�u
		locateTextPanes();
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}

	public PageElement getCorrespondantPageElement(JTextPane jtp) {
		for(int i=0; i<peTextPanes.size(); i++) {
			JTextPane peTextPane = peTextPanes.get(i);
			if(peTextPane==jtp) {
				PageElement requiredPageElement = page.getPageElement(i);
				return requiredPageElement;
			}
		}
		
		Debug.error("Panel_PeEdit_Left getCorrespondantPageElement() : PageElement�C���X�^���X��������܂���");
		return null;
	}

	public JTextPane getFocusedTextPane() {
		return focusedTextPane;
	}
	
	public PageElement getFocusedPageElement() {
		JTextPane focusedTp = getFocusedTextPane();
		if(focusedTp==null) {
			return null;
		}
		return getCorrespondantPageElement(focusedTp);
	}
	
	public static Panel_PeEdit_Left getInstance() {
		return Panel_PeEdit_Left.obj;
	}
	
	public static void updateInstance(Panel_PeEdit_Left newObject) {
		Panel_PeEdit_Left.obj = newObject;
	}

}

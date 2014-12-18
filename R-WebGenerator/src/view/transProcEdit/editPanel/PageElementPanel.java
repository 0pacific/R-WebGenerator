package view.transProcEdit.editPanel;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import pageElement.PageElement;
import pageElement.PageElementCreateForm;
import pageElement.PageElementDeleteForm;
import pageElement.PageElementSaif;
import pageElement.PageElementText;
import pageElement.PageElementUpdateForm;
import pageElement.PageElement_HyperLink;
import pageElement.PageElementDisplayArea;
import pageElement.PageElementTableDisplay;
import property.GeneratorProperty;

import debug.Debug;

import view.EditPanel;
import utility.*;

public class PageElementPanel extends EditPanel {
	// �p�l���c��
	public static int panelHeight = 20;

	public PageElement pageElement;

	public SerializableSpringLayout springLayout;

	public Color bgColor;
	
	
	
	
	
	public PageElementPanel(PageElement pe) {
		this.pageElement = pe;

		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
		setOpaque(true);
		
		JTextPane jtp = new JTextPane();
		jtp.setFont(new Font("Serif", Font.PLAIN, 10));
		jtp.setEditable(false);

		bgColor = Color.GRAY;
		setBackground(bgColor);
	}

	
	
	
	
	public PageElement getPageElement() {
		return pageElement;
	}

	
	
	
	
	public void paintComponent(Graphics g) {
		boolean japanese = GeneratorProperty.japanese();
		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(bgColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		String str = null;
		if(pageElement instanceof PageElementText) {
			str = japanese ? "�e�L�X�g" : "Text";
		}
		else if(pageElement instanceof PageElement_HyperLink) {
			str = japanese ? "�n�C�p�[�����N" : "Hyper Link";
		}
		else if(pageElement instanceof PageElementTableDisplay) {
			str = japanese ? "�e�[�u���\��" : "Table Display";
		}
		else if(pageElement instanceof PageElementCreateForm) {
			str = japanese ? "���R�[�h�쐬�t�H�[��" : "Form(Create)";
		}
		else if(pageElement instanceof PageElementDeleteForm) {
			str = japanese ? "���R�[�h�폜�t�H�[��" : "Form(Delete)";
		}
		else if(pageElement instanceof PageElementUpdateForm) {
			str = japanese ? "�X�V�t�H�[��" : "Form(Update)";
		}
		else if(pageElement instanceof PageElementDisplayArea) {
			str = japanese ? "�T�[�r�X�o�͕\��" : "Service Output Table";
		}
		else if(pageElement instanceof PageElementSaif) {
			str = "SAIF(" + ((PageElementSaif)pageElement).getSaifKind() + ")";
		}
		else {
			Debug.informError();
			Debug.error("�z��O�̃y�[�W�\���v�f�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Serif", Font.PLAIN, 10));
		g2.drawString(str, 5, PageElementPanel.panelHeight-5);
	}
}


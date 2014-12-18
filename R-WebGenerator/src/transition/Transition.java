package transition;


import gui.arrow.DirectionDecider;

import java.awt.Point;
import java.io.Serializable;

import debug.Debug;


import pageElement.PageElement;
import pageElement.PageElementCreateForm;
import pageElement.PageElementLoginForm;
import pageElement.PageElementUpdateForm;
import pageElement.PageElement_HyperLink;
import property.GeneratorProperty;
import view.webPageDefinition.WebPagePanel;
import view.webPageDefinition.WebPagePanelManager;
import webPage.WebPage;

public class Transition implements Serializable {
	public WebPage startPage;
	public PageElement triggerPe;
	public WebPage endPage;

	// ���̑J�ڂ̎�L�[�i�A�v���������ɏ��߂Ċ��蓖�Ă��A�����̂��߂Ɏg�p����j
	public int transKey;
	
	public TransitionProcess transProc;
	
	
	
	
	public Transition(WebPage startPage, PageElement triggerPe, WebPage endPage) {
		this.startPage = startPage;
		this.endPage = endPage;
		this.triggerPe = triggerPe;

		// �J�ڂ��ł����ۂɂ́A�Ή�����J�ڃv���Z�X����������
		this.transProc = new TransitionProcess(this, startPage, endPage);
	}

	
	
	public int getTransNumber() {
		return TransitionManager.getInstance().getTransitionIndex(this);
	}
	
	
	
	
	public Point[] getArrowPointPair() {
		DirectionDecider dd = new DirectionDecider();

		// �J�ڌ��E�J�ڐ悻�ꂼ��ɑΉ�����Web�y�[�W�p�l�����擾
		WebPagePanelManager wppm = WebPagePanelManager.getInstance();
		WebPagePanel startWpp = wppm.getWebPagePanelByPage(startPage);
		WebPagePanel endWpp = wppm.getWebPagePanelByPage(endPage);

		Point[] pointPair = dd.decide(startWpp, endWpp);

		return pointPair;
	}

	
	
	public TransitionProcess getTransitionProcess() {
		return transProc;
	}
	

	/*
	 * PURPOSE :
	 * �w�肵�����W���A���̑J�ڂ�\�������t�H�[�J�X�ł�����W���ۂ���Ԃ�
	 */
	public boolean within(int x, int y) {
		Debug.out("CALL within() : " + startPage.pageFileName + "����" + endPage.pageFileName + "�ւ̑J��");
		
		// ���̑J�ڂ�\�����́A�n�_�E�I�_�̍��W
		Point[] pointPair = getArrowPointPair();
		Point arrowStartPoint = pointPair[0];
		Point arrowEndPoint = pointPair[1];

		// �E���Ȃ����E��ւ̖��
		if(arrowStartPoint.getX() < arrowEndPoint.getX()) {
			// �E���ւ̖��
			if(arrowStartPoint.getY() < arrowEndPoint.getY()) {
				if(arrowStartPoint.getX() <= x && x <= arrowEndPoint.getX() && arrowStartPoint.getY() <= y && y <= arrowEndPoint.getY())
					return true;
				return false;
			}
			// �E��ւ̖��
			else {
				if(arrowStartPoint.getX() <= x && x <= arrowEndPoint.getX() && arrowEndPoint.getY() <= y && y <= arrowStartPoint.getY())
					return true;
				return false;
			}
		}
		// �����ւ̖��
		if(arrowStartPoint.getY() < arrowEndPoint.getY()) {
			if(arrowEndPoint.getX() <= x && x <= arrowStartPoint.getX() && arrowStartPoint.getY() <= y && y <= arrowEndPoint.getY())
				return true;
			return false;
		}
		// ����ւ̖��
		if(arrowEndPoint.getX() <= x && x <= arrowStartPoint.getX() && arrowEndPoint.getY() <= y && y <= arrowStartPoint.getY())
			return true;
		return false;
	}
	
	
	
	
	public WebPage getStartPage() {
		return startPage;
	}

	public WebPage getEndPage() {
		return endPage;
	}

	
	
	
	public String getDescription() {
		boolean japanese = GeneratorProperty.japanese();
		String text = startPage.pageFileName + " -> " + endPage.pageFileName + "�@�i";
		
		if(triggerPe instanceof PageElement_HyperLink) {
			PageElement_HyperLink triggerHyperLink = (PageElement_HyperLink)triggerPe;
			text += japanese ? "�n�C�p�[�����N�u"+triggerHyperLink.getText()+"�v�ɂ��J��" : "Transition by hyperlink \"" + triggerHyperLink.getText() + "\"";
		}
		else if(triggerPe instanceof PageElementCreateForm) {
			PageElementCreateForm triggerCreateForm = (PageElementCreateForm)triggerPe;
			text += japanese ? "�e�[�u���u" + triggerCreateForm.table.getTableName() + "�v�̃��R�[�h�쐬�t�H�[���ɂ��J��" : "Transition by record creation form of table \"" + triggerCreateForm.table.getTableName() + "\"";
		}
		else if(triggerPe instanceof PageElementUpdateForm) {
			PageElementUpdateForm triggerUpdateForm = (PageElementUpdateForm)triggerPe;
			text += japanese ?
					"�e�[�u���u" + triggerUpdateForm.getTable().getTableName() + "�v�̍X�V�t�H�[���ɂ��J��" :
					"Transition by update form of table \"" + triggerUpdateForm.getTable().getTableName() + "\"";
		}
		else if(triggerPe instanceof PageElementLoginForm) {
			PageElementLoginForm triggerLoginForm = (PageElementLoginForm)triggerPe;
			text += japanese ? 
					"�A�J�E���g�e�[�u���u" + triggerLoginForm.accountTable.getTableName() + "�v�̃��O�C���t�H�[���ɂ��J��" :
					"Transition by login form of account table \"" + triggerLoginForm.accountTable.getTableName() + "\"";
		}
		else {
			Debug.error("�z��O�̃y�[�W�G�������g�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		text += "�j";

		return text;
	}
}

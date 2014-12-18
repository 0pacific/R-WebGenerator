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

	// この遷移の主キー（アプリ生成時に初めて割り当てられ、生成のために使用する）
	public int transKey;
	
	public TransitionProcess transProc;
	
	
	
	
	public Transition(WebPage startPage, PageElement triggerPe, WebPage endPage) {
		this.startPage = startPage;
		this.endPage = endPage;
		this.triggerPe = triggerPe;

		// 遷移ができた際には、対応する遷移プロセスも生成する
		this.transProc = new TransitionProcess(this, startPage, endPage);
	}

	
	
	public int getTransNumber() {
		return TransitionManager.getInstance().getTransitionIndex(this);
	}
	
	
	
	
	public Point[] getArrowPointPair() {
		DirectionDecider dd = new DirectionDecider();

		// 遷移元・遷移先それぞれに対応したWebページパネルを取得
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
	 * 指定した座標が、この遷移を表す矢印をフォーカスできる座標か否かを返す
	 */
	public boolean within(int x, int y) {
		Debug.out("CALL within() : " + startPage.pageFileName + "から" + endPage.pageFileName + "への遷移");
		
		// この遷移を表す矢印の、始点・終点の座標
		Point[] pointPair = getArrowPointPair();
		Point arrowStartPoint = pointPair[0];
		Point arrowEndPoint = pointPair[1];

		// 右下ないし右上への矢印
		if(arrowStartPoint.getX() < arrowEndPoint.getX()) {
			// 右下への矢印
			if(arrowStartPoint.getY() < arrowEndPoint.getY()) {
				if(arrowStartPoint.getX() <= x && x <= arrowEndPoint.getX() && arrowStartPoint.getY() <= y && y <= arrowEndPoint.getY())
					return true;
				return false;
			}
			// 右上への矢印
			else {
				if(arrowStartPoint.getX() <= x && x <= arrowEndPoint.getX() && arrowEndPoint.getY() <= y && y <= arrowStartPoint.getY())
					return true;
				return false;
			}
		}
		// 左下への矢印
		if(arrowStartPoint.getY() < arrowEndPoint.getY()) {
			if(arrowEndPoint.getX() <= x && x <= arrowStartPoint.getX() && arrowStartPoint.getY() <= y && y <= arrowEndPoint.getY())
				return true;
			return false;
		}
		// 左上への矢印
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
		String text = startPage.pageFileName + " -> " + endPage.pageFileName + "　（";
		
		if(triggerPe instanceof PageElement_HyperLink) {
			PageElement_HyperLink triggerHyperLink = (PageElement_HyperLink)triggerPe;
			text += japanese ? "ハイパーリンク「"+triggerHyperLink.getText()+"」による遷移" : "Transition by hyperlink \"" + triggerHyperLink.getText() + "\"";
		}
		else if(triggerPe instanceof PageElementCreateForm) {
			PageElementCreateForm triggerCreateForm = (PageElementCreateForm)triggerPe;
			text += japanese ? "テーブル「" + triggerCreateForm.table.getTableName() + "」のレコード作成フォームによる遷移" : "Transition by record creation form of table \"" + triggerCreateForm.table.getTableName() + "\"";
		}
		else if(triggerPe instanceof PageElementUpdateForm) {
			PageElementUpdateForm triggerUpdateForm = (PageElementUpdateForm)triggerPe;
			text += japanese ?
					"テーブル「" + triggerUpdateForm.getTable().getTableName() + "」の更新フォームによる遷移" :
					"Transition by update form of table \"" + triggerUpdateForm.getTable().getTableName() + "\"";
		}
		else if(triggerPe instanceof PageElementLoginForm) {
			PageElementLoginForm triggerLoginForm = (PageElementLoginForm)triggerPe;
			text += japanese ? 
					"アカウントテーブル「" + triggerLoginForm.accountTable.getTableName() + "」のログインフォームによる遷移" :
					"Transition by login form of account table \"" + triggerLoginForm.accountTable.getTableName() + "\"";
		}
		else {
			Debug.error("想定外のページエレメントです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		text += "）";

		return text;
	}
}

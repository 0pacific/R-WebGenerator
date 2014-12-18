package webPage;

import gui.*;
import gui.arrow.*;
import pageElement.PageElement;
import pageElement.PageElementObserver;

import table.*;
import tpp.*;
import transition.Transition;
import view.transProcEdit.serviceArgsWindow.*;

import java.util.ArrayList;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import mainFrame.MainFrame;

import debug.Debug;

public class WebPage implements Serializable {
	public ArrayList<PageElement> pageElements = new ArrayList<PageElement>();

	public String pageFileName;


	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public WebPage(String pageFileName) {
		this.pageFileName = pageFileName;
		this.pageElements = new ArrayList<PageElement>();
	}
	
		
	
	
	
	public int getPageElementNum() {
		return pageElements.size();
	}

	public PageElement getPageElement(int index) {
		return pageElements.get(index);
	}

	public void addPageElement(PageElement pe) {
		pageElements.add(pe);
		PageElementObserver.getInstance().informPeAddition(pe);
	}

	public void removePageElement(PageElement pe) {
		boolean remove = pageElements.remove(pe);
		if(!remove) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
			Debug.error("存在しないページエレメントを削除しようとしたようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		PageElementObserver.getInstance().informPeRemovement(pe);
		return;
	}




	public int getWebPageNumber() {
		int webPageNumber = WebPageManager.getInstance().giveWebPageNumber(this);
		return webPageNumber;
	}
}
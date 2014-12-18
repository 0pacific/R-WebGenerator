package view.webPageDefinition;


import java.awt.Point;
import java.io.Serializable;
import java.util.*;

import debug.Debug;

import webPage.WebPage;
import webPage.WebPageManager;





public class WebPagePanelManager implements Serializable {
	public ArrayList<WebPagePanel> webPagePanelArray;

	/*
	 * �C���X�^���X�Q�ƕϐ��͍Ō�
	 */
	private static WebPagePanelManager obj = new WebPagePanelManager();

	
	
	
	
	public int getPanelNum() {
		return webPagePanelArray.size();
	}

	
	
	public WebPagePanel getWebPagePanelByIndex(int index) {
		return webPagePanelArray.get(index);
	}
	
	
	
	private WebPagePanelManager() {
		this.webPagePanelArray = new ArrayList<WebPagePanel>();

		/*
		 * ��PageManager�����Ȃ���APage�C���X�^���X������WebPagePanel�C���X�^���X�����Ƃ����������������낤���H
		 * ���̃R���X�g���N�^���s���_��Page�C���X�^���X���͂O�̂͂��Ȃ̂ŕK�v�Ȃ��Ǝv����
		 */
	}



	
	
	public static WebPagePanelManager getInstance() {
		return WebPagePanelManager.obj;
	}





	/*
	 * SUMMARY :
	 * �V����Web�y�[�W���ł����Ƃ��ɌĂяo�����\�b�h
	 * ����Web�y�[�W��\��Page�C���X�^���X���󂯎��A�Ή�����Web�y�[�W�p�l���iWebPagePanel�C���X�^���X�j�𐶐��AArrayList�ɒǉ����ĕێ�����
	 */
	public void informWebPageAddition(WebPage page) {
		// Web�y�[�W�p�l���̏������W
		Point initPoint = new Point(10, 10);

		// Web�y�[�W�p�l���𐶐��AArrayList�ɒǉ�
		WebPagePanel webPagePanel = new WebPagePanel(page, initPoint);
		webPagePanelArray.add(webPagePanel);
		Debug.out("WebPagePanelManager Add : " + page.pageFileName + "��\���p�l��", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	
	
	
	public void informWebPageRemovement(WebPage page) {
		WebPagePanel panel = getWebPagePanelByPage(page);
		boolean remove = webPagePanelArray.remove(panel);
		if(!remove) {
			Debug.error("�폜���悤�Ƃ���Web�y�[�W�p�l����������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}




	public WebPagePanel getWebPagePanelByPage(WebPage page) {
		for(int i=0; i<webPagePanelArray.size(); i++) {
			WebPagePanel webPagePanel = webPagePanelArray.get(i);
			if(webPagePanel.page==page) {
				return webPagePanel;
			}
		}

		Debug.error("Web�y�[�W�����Ƃ�Web�y�[�W�p�l����T���܂������A������܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}





	public void move(WebPage page, int posX, int posY) {
		WebPagePanel wpp = getWebPagePanelByPage(page);
		wpp.move(posX, posY);
	}





	public static void updateInstance(WebPagePanelManager newObject) {
		WebPagePanelManager.obj = newObject;
	}





}

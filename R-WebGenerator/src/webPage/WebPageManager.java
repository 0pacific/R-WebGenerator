package webPage;


import java.io.Serializable;
import java.util.ArrayList;

import authority.AuthorityManager;

import debug.Debug;

import transition.TransitionManager;
import view.webPageDefinition.WebPagePanelManager;





/*
 * Singleton
 */
public class WebPageManager implements Serializable {
	private ArrayList<WebPage> pages;
	public WebPage logoutDestPage;
	public WebPage ARButtonPage;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static WebPageManager pageManager = new WebPageManager();
	
	
	
	
	
	
	
	
	private WebPageManager() {
		pages = new ArrayList<WebPage>();
	}

	
	public static WebPageManager getInstance() {
		return WebPageManager.pageManager;
	}

	
	
	public WebPage getPage(int pageNumber) {
		return pages.get(pageNumber);
	}

	
	public int getPageNum() {
		return pages.size();
	}

	public WebPage[] getPages() {
		WebPage[] pageArray = new WebPage[pages.size()];
		for(int i=0; i<pages.size(); i++) {
			pageArray[i] = pages.get(i);
		}
		return pageArray;
	}
	
	public WebPage getPageByName(String name) {
		for(int i=0; i<getPageNum(); i++) {
			WebPage page = getPage(i);
			if(page.pageFileName.equals(name)) {
				return page;
			}
		}

		Debug.notice("Web�y�[�W�u"+name+"�v�͑��݂��Ȃ��悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	public String[] getPageNameArray() {
		String[] names = new String[getPageNum()];
		for(int i=0; i<getPageNum(); i++) {
			WebPage page = getPage(i);
			names[i] = page.pageFileName;
		}
		return names;
	}

	public ArrayList<String> getPageNameArrayList() {
		ArrayList<String> nameArray = new ArrayList<String>();
		for(int i=0; i<getPageNum(); i++) {
			WebPage page = getPage(i);
			nameArray.add(page.pageFileName);
		}
		return nameArray;
	}
	
	
	
	
	/*
	 * SUMMARY :
	 * Web�y�[�W��ǉ�
	 * 
	 * NOTICE :
	 * WebPagePanelManager�C���X�^���X�ɕ񍐂��s���A�Ή�����Web�y�[�W�p�l�����쐬�E�ǉ�������
	 */
	public void addWebPage(WebPage page) {
		pages.add(page);
		WebPagePanelManager.getInstance().informWebPageAddition(page);
	}

	
	public void addNewPage(String pageFileName) {
		WebPage page = new WebPage(pageFileName);
		addWebPage(page);
	}


	public boolean removeWebPage(WebPage webPage) {
		boolean remove = pages.remove(webPage);
		if(!remove) {
			Debug.error("�폜���悤�Ƃ���Web�y�[�W��������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		WebPagePanelManager.getInstance().informWebPageRemovement(webPage);
		TransitionManager.getInstance().informWebPageRemovement(webPage);
		return true;
	}
	
	


	public int giveWebPageNumber(WebPage webPage) {
		for(int i=0; i<this.getPageNum(); i++) {
			WebPage wp = this.getPage(i);
			if(wp==webPage) {
				return i;
			}
		}

		// �G���[
		Debug.error("�Ή�����WebPage�C���X�^���X��������܂���ł���...WebPageManager::giveWebPageNumber()");
		return -1;
	}





	public static void updateInstance(WebPageManager newObject) {
		WebPageManager.pageManager = newObject;
	}
	
	
	
	
	
	public WebPage getLogoutDestPage() {
		return logoutDestPage;
	}

	public void setLogoutDestPage(WebPage webPage) {
		this.logoutDestPage = webPage;
	}
	
	
	// �A�v���ĕҏW�{�^���z�u�ꏊ�֘A�y�ǉ��z
	public WebPage getARButtonPage() {
		return ARButtonPage;
	}

	public void setARButtonPage(WebPage webPage) {
		this.ARButtonPage = webPage;
	}


	
	public void debug() {
		Debug.debug_call(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		for(int i=0; i<this.getPageNum(); i++) {
			Debug.out(i+"�ԃy�[�W(0-b) : " + getPage(i).pageFileName);
		}

		Debug.debug_return(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}

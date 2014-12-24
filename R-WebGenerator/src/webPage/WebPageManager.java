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
	 * インスタンスは最後！
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

		Debug.notice("Webページ「"+name+"」は存在しないようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
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
	 * Webページを追加
	 * 
	 * NOTICE :
	 * WebPagePanelManagerインスタンスに報告を行い、対応するWebページパネルを作成・追加させる
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
			Debug.error("削除しようとしたWebページが見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
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

		// エラー
		Debug.error("対応するWebPageインスタンスが見つかりませんでした...WebPageManager::giveWebPageNumber()");
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
	
	
	// アプリ再編集ボタン配置場所関連【追加】
	public WebPage getARButtonPage() {
		return ARButtonPage;
	}

	public void setARButtonPage(WebPage webPage) {
		this.ARButtonPage = webPage;
	}


	
	public void debug() {
		Debug.debug_call(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		for(int i=0; i<this.getPageNum(); i++) {
			Debug.out(i+"番ページ(0-b) : " + getPage(i).pageFileName);
		}

		Debug.debug_return(getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}

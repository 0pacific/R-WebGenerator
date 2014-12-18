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
	 * インスタンス参照変数は最後
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
		 * ◆PageManagerを見ながら、Pageインスタンス数だけWebPagePanelインスタンスを作るというやり方がいいだろうか？
		 * このコンストラクタ実行時点でPageインスタンス数は０のはずなので必要ないと思うが
		 */
	}



	
	
	public static WebPagePanelManager getInstance() {
		return WebPagePanelManager.obj;
	}





	/*
	 * SUMMARY :
	 * 新しいWebページができたときに呼び出すメソッド
	 * そのWebページを表すPageインスタンスを受け取り、対応するWebページパネル（WebPagePanelインスタンス）を生成、ArrayListに追加して保持する
	 */
	public void informWebPageAddition(WebPage page) {
		// Webページパネルの初期座標
		Point initPoint = new Point(10, 10);

		// Webページパネルを生成、ArrayListに追加
		WebPagePanel webPagePanel = new WebPagePanel(page, initPoint);
		webPagePanelArray.add(webPagePanel);
		Debug.out("WebPagePanelManager Add : " + page.pageFileName + "を表すパネル", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	
	
	
	public void informWebPageRemovement(WebPage page) {
		WebPagePanel panel = getWebPagePanelByPage(page);
		boolean remove = webPagePanelArray.remove(panel);
		if(!remove) {
			Debug.error("削除しようとしたWebページパネルが見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}




	public WebPagePanel getWebPagePanelByPage(WebPage page) {
		for(int i=0; i<webPagePanelArray.size(); i++) {
			WebPagePanel webPagePanel = webPagePanelArray.get(i);
			if(webPagePanel.page==page) {
				return webPagePanel;
			}
		}

		Debug.error("WebページをもとにWebページパネルを探しましたが、見つかりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
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

package executer.generation.pePrimKey;

import pageElement.PageElement;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementPrimaryKeyAssigner {
	/*
	 * SUMMARY :
	 * 全ページの全ページエレメントに、主キーを与える
	 */
	public void assignKeys() {
		// 主キーは0から順に与えていく
		int key = 0;
	
		WebPageManager wpm = WebPageManager.getInstance();

		// 各Webページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// このWebページの各ページエレメントについて処理
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);
				pe.pePrimaryKey = key;
				key++;
			}
		}
	}
}

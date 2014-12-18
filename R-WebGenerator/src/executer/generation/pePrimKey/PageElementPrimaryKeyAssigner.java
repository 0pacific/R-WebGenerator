package executer.generation.pePrimKey;

import pageElement.PageElement;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementPrimaryKeyAssigner {
	/*
	 * SUMMARY :
	 * �S�y�[�W�̑S�y�[�W�G�������g�ɁA��L�[��^����
	 */
	public void assignKeys() {
		// ��L�[��0���珇�ɗ^���Ă���
		int key = 0;
	
		WebPageManager wpm = WebPageManager.getInstance();

		// �eWeb�y�[�W�ɂ��ď���
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// ����Web�y�[�W�̊e�y�[�W�G�������g�ɂ��ď���
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);
				pe.pePrimaryKey = key;
				key++;
			}
		}
	}
}

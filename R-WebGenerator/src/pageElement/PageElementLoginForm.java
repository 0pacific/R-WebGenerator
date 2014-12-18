package pageElement;

import table.AccountTable;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementLoginForm extends PageElement {
	public AccountTable accountTable;
	
	// ログイン成功時の遷移先Webページ
	public WebPage destWebPage;
	
	

	
	
	public PageElementLoginForm(WebPage parentPage, AccountTable accountTable, WebPage destWebPage) {
		super(parentPage);
		this.accountTable = accountTable;
		this.destWebPage = destWebPage;
	}







	public void initInputPePorts(TransitionProcess transProc) {
		// ページエレメントポートは存在しない
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		// ページエレメントポートは存在しない
	}
}

package pageElement;

import table.AccountTable;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementLoginForm extends PageElement {
	public AccountTable accountTable;
	
	// ���O�C���������̑J�ڐ�Web�y�[�W
	public WebPage destWebPage;
	
	

	
	
	public PageElementLoginForm(WebPage parentPage, AccountTable accountTable, WebPage destWebPage) {
		super(parentPage);
		this.accountTable = accountTable;
		this.destWebPage = destWebPage;
	}







	public void initInputPePorts(TransitionProcess transProc) {
		// �y�[�W�G�������g�|�[�g�͑��݂��Ȃ�
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		// �y�[�W�G�������g�|�[�g�͑��݂��Ȃ�
	}
}

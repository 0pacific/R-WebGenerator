package pageElement;

import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementText extends PageElement {
	public String containingText;
	
	
	
	
	
	public PageElementText(WebPage parentPage, String text) {
		super(parentPage);
		this.containingText = text;
	}

	
	
	
	
	public void initInputPePorts(TransitionProcess transProc) {
		// ページエレメントポートは存在しない
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		// ページエレメントポートは存在しない
	}
}

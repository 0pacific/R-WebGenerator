package pageElement;

import transition.TransitionProcess;
import webPage.WebPage;

public class PageElement_HyperLink extends PageElement {
	private WebPage destPage;
	private String text;
	
	public PageElement_HyperLink(WebPage parentPage, WebPage destPage, String text) {
		super(parentPage);
		this.destPage = destPage;
		this.text = text;
	}

	public WebPage getDestPage() {
		return destPage;
	}

	public void setDestPage(WebPage page) {
		destPage = page;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void initInputPePorts(TransitionProcess transProc) {
		// ページエレメントポートは存在しない
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		// ページエレメントポートは存在しない
	}
}

package tpp.port;

import pageElement.PageElement;

public class PageElementPort extends DataPort {
	private PageElement pageElement;

	public PageElementPort(PageElement pageElement) {
		this.pageElement = pageElement;
	}

	public PageElement getPageElement() {
		return pageElement;
	}
}

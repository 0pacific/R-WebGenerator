package adminAuth.page;

import webPage.WebPage;

public class AdminAuth_Page {

	
	private WebPage page;
	private int editLevel;
	private static final int EDITABLE = 1;
	private static final int NOT_EDITABLE = 0;
	
	public AdminAuth_Page(){
	}
	
	public void setPage(WebPage page){
		this.page = page;
		this.editLevel = NOT_EDITABLE;
	}
	
	
	public void setEditable(){
		this.editLevel = EDITABLE;
	}
	
	public void setNotEditable(){
		this.editLevel = NOT_EDITABLE;
	}
	
	public WebPage getPage(){
		return this.page;
	}
	
	public int getEditable(){
		return this.editLevel;
	}
}

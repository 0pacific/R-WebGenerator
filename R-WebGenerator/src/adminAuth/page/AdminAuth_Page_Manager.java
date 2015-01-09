package adminAuth.page;

import java.util.ArrayList;

import webPage.WebPage;

public class AdminAuth_Page_Manager {

	public ArrayList<AdminAuth_Page> pageAuthArray;
	
	//ページに対する管理者の権限（全て、一部、なし）
	public int adminAuth_Page_General;
	
	private static final int FULL_EDITABLE = 2;
	private static final int EDITABLE = 1;
	private static final int NOT_EDITABLE = 0;
	
	private static AdminAuth_Page_Manager obj = new AdminAuth_Page_Manager();
	
	private AdminAuth_Page_Manager(){
		 pageAuthArray = new ArrayList<AdminAuth_Page>();
		 adminAuth_Page_General = NOT_EDITABLE;
	}
	
	public void addPage(WebPage page){
		AdminAuth_Page aap = new AdminAuth_Page();
		aap.setPage(page);
		pageAuthArray.add(aap);
	}
	
	

	
	public void setFullEditable(){
		this.adminAuth_Page_General = FULL_EDITABLE;
	}
	
	public void setEditable(){
		this.adminAuth_Page_General = EDITABLE;
	}
	
	public void setNotEditable(){
		this.adminAuth_Page_General = NOT_EDITABLE;
	}
	
	
	public AdminAuth_Page_Manager getInstance(){
		return AdminAuth_Page_Manager.obj;
	}
	
	public void updateInstance(AdminAuth_Page_Manager newObj){
		AdminAuth_Page_Manager.obj = newObj;
	}
	
}

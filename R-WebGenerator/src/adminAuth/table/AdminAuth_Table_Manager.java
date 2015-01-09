package adminAuth.table;

import adminAuth.page.AdminAuth_Page_Manager;

public class AdminAuth_Table_Manager {

	//テーブルに対する管理者の権限（全て、一部、なし）
	public int adminAuth_Table_General;
	
	private static final int FULL_EDITABLE = 2;
	private static final int EDITABLE = 1;
	private static final int NOT_EDITABLE = 0;
	
	private static AdminAuth_Table_Manager obj = new AdminAuth_Table_Manager();
	
	private AdminAuth_Table_Manager(){
		adminAuth_Table_General = NOT_EDITABLE;
	}
	
	public void setFullEditable(){
		this.adminAuth_Table_General = FULL_EDITABLE;
	}
	
	public void setEditable(){
		this.adminAuth_Table_General = EDITABLE;
	}
	
	public void setNotEditable(){
		this.adminAuth_Table_General = NOT_EDITABLE;
	}
	
	
	public AdminAuth_Table_Manager getInstance(){
		return AdminAuth_Table_Manager.obj;
	}
	
	public void updateInstance(AdminAuth_Table_Manager newObj){
		AdminAuth_Table_Manager.obj = newObj;
	}
	
	
}

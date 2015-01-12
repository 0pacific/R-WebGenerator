package adminAuth;

import adminAuth.page.AdminAuth_Page_Manager;
import adminAuth.role.AdminAuth_Role_Manager;
import adminAuth.table.AdminAuth_Table_Manager;

public class AdminAuthManager {
	
	//y’Ç‰Áz
	//ŠÇ—ÒŒ ŒÀ‚ğŠÇ—‚·‚éƒNƒ‰ƒX
	
	private AdminAuth_Table_Manager adminAuth_Table_Manager;
	private AdminAuth_Role_Manager adminAuth_Role_Manager;
	private AdminAuth_Page_Manager adminAuth_Page_Manager;
	
	private static AdminAuthManager obj = new AdminAuthManager();
	
	private AdminAuthManager(){
		
	}
	
	public static AdminAuthManager getInstance(){
		return AdminAuthManager.obj;
	}
	
	public static void updateInstance(AdminAuthManager newObj){
		AdminAuthManager.obj = newObj;
	}

}

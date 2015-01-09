package adminAuth.role;

import java.util.ArrayList;

import role.Role;

public class AdminAuth_Role_Manager {

	public ArrayList<AdminAuth_Role> roleAuthArray; 
	
	//ロールに対する管理者の権限（全て、一部、なし）
	public int adminAuth_Role_General;

	private static final int FULL_EDITABLE = 2;
	private static final int EDITABLE = 1;
	private static final int NOT_EDITABLE = 0;
	
	
	private static AdminAuth_Role_Manager obj = new AdminAuth_Role_Manager();
	
	private AdminAuth_Role_Manager(){
		roleAuthArray = new ArrayList<AdminAuth_Role>();
		adminAuth_Role_General = NOT_EDITABLE;
	}
	
	public void addRole(Role role){
		AdminAuth_Role aar = new AdminAuth_Role(role);
		roleAuthArray.add(aar);
	}
	
	public AdminAuth_Role getAdminAuthRole(int i){
		return roleAuthArray.get(i);
	}
	
	public void setFullEditable(){
		this.adminAuth_Role_General = FULL_EDITABLE;
	}
	
	public void setEditable(){
		this.adminAuth_Role_General = EDITABLE;
	}
	
	public void setNotEditable(){
		this.adminAuth_Role_General = NOT_EDITABLE;
	}
	
	
	
	
	public AdminAuth_Role_Manager getInstance(){
		return AdminAuth_Role_Manager.obj;
	}
	
	public void updateInstance(AdminAuth_Role_Manager newObj){
		AdminAuth_Role_Manager.obj = newObj;
	}
}

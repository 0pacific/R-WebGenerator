package account;

/*
 * 【追加】
 * singleton
 * 管理者、作成者のアカウントを管理
 */

public class AccountManager {
	
	private Account adminAccount;
	private Account createrAccount;
	
	private static AccountManager obj = new AccountManager();
	
	private int MODE = 0;
	private static final int DEVELOPING_MODE=0;
	private static final int ADMINS_MODE=1;
	
	private AccountManager(){
		adminAccount = new Account();
		createrAccount = new Account();
		MODE = DEVELOPING_MODE;
	}
	
	public void setAdmin(Account adminAccount){
		this.adminAccount = adminAccount;
	}
	
	public void setCreater(Account createrAccount){
		this.createrAccount = createrAccount;
	}
	
	public Account getAdmin(){
		return this.adminAccount;
	}
	
	public Account getCreater(){
		return this.createrAccount;
	}
	
	
	public int getMODE(){
		return MODE;
	}
	
	public void setDeveloping(){
		this.MODE = DEVELOPING_MODE;
	}
	
	public void setAdmins(){
		this.MODE = ADMINS_MODE;
	}
	
	public boolean isExistAdmin(){
		if(adminAccount.getID()==""){
			return false;
		}
		else return true;
	}
	
	public boolean isExistCreater(){
		if(createrAccount.getID()==""){
			return false;
		}
		else return true;
	}
	public static AccountManager getInstance(){
		return obj;
	}
	
	public static void updateInstance(AccountManager newObj){
		AccountManager.obj = newObj;
	}
}

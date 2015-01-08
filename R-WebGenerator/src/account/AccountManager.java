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
	
	private AccountManager(){
		adminAccount = new Account();
		createrAccount = new Account();
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
	
	public static AccountManager getInstance(){
		return obj;
	}
	
	
}

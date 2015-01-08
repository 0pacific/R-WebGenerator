package account;

/*
 * �y�ǉ��z
 * singleton
 * �Ǘ��ҁA�쐬�҂̃A�J�E���g���Ǘ�
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

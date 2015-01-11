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
	
	public static AccountManager getInstance(){
		return obj;
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
}

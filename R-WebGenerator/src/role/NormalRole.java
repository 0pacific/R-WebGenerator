package role;

import table.AccountTable;





/*
 * 非ゲストロールを表すクラス
 */
public class NormalRole extends Role {
	// アカウントテーブルがまだ定義されていないならNULL、されていればAccountTableインスタンス
	private AccountTable accountTable = null;

	
	
	
	
	public NormalRole(String name) {
		super(name);
	}
	
	
	
	
	
	public AccountTable getAccountTable() {
		return accountTable;
	}

	
	
	
	
	public void setAccountTable(AccountTable accountTable) {
		this.accountTable = accountTable;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * 既にアカウントテーブルが指定されているか否かを返す
	 */
	public boolean accountTableDefined() {
		return (this.accountTable!=null);
	}
}

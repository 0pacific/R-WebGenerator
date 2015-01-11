package table;

import authority.AuthorityManager;
import gui.*;
import gui.arrow.*;
import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.*;
import table.field.AbstractField;
import table.field.Field;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

import java.io.Serializable;
import java.util.ArrayList;

import pageElement.PageElementObserver;
import debug.Debug;

/*
 * DUTY :
 * データテーブル・アカウントテーブルを一括管理
 * 
 * NOTICE :
 * Singleton
 */
public class TableManager implements Serializable {
	private ArrayList<DataTable> dataTables;
	private ArrayList<AccountTable> accountTables;
	/*
	 * インスタンス変数は最後！
	 */
	private static TableManager obj = new TableManager();
	
	
	
	private TableManager() {
		dataTables = new ArrayList<DataTable>();
		accountTables = new ArrayList<AccountTable>();
	}

	public void addDataTable(DataTable dataTable) {
		dataTables.add(dataTable);
		AuthorityManager.getInstance().informTableAddition(dataTable);
	}

	public void addAccountTable(AccountTable accountTable) {
		accountTables.add(accountTable);
		AuthorityManager.getInstance().informTableAddition(accountTable);
	}

	public void removeAccountTable(AccountTable accountTable) {
		accountTables.remove(accountTable);

		// アカウントテーブルでアカウントを管理されていた全ロールについて、アカウントテーブルをNULLとする
		for(int i=0; i<accountTable.getNormalRoleNum(); i++) {
			NormalRole accOwnerRole = accountTable.getNormalRole(i);
			accOwnerRole.setAccountTable(null);
		}

		PageElementObserver.getInstance().informTableRemovement(accountTable);
		AuthorityManager.getInstance().informTableRemovement(accountTable);
	}
	
	public DataTable getDataTable(int index) {
		return dataTables.get(index);
	}

	public AccountTable getAccountTable(int index) {
		return accountTables.get(index);
	}
	
	
	public SuperTable getTableByName(String name) {
		for(int i=0; i<getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt.getTableName().equals(name)) {
				return dt;
			}
		}
		for(int i=0; i<getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.getTableName().equals(name)) {
				return at;
			}
		}
		
		// ◆ERROR : テーブルが見つからなかった
		Debug.error("テーブル「"+name+"」が見つかりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	public int getDataTableNum() {
		return dataTables.size();
	}

	public int getAccountTableNum() {
		return accountTables.size();
	}

	
	
	public void informFieldAddition(Field newField) {
		AuthorityManager.getInstance().informFieldAddition(newField);
	}
	
	public void informAbFieldAddition(AbstractField newAbField){
		AuthorityManager.getInstance().informAbFieldAddition(newAbField);
	}
	
	

	/*
	 * SUMMARY :
	 * テーブルのインデックス（番号）を取得
	 * 
	 * NOTICE :
	 * ◆インデックスは、０番からデータテーブルが並び、【データテーブル数】番目からアカウントテーブルが並ぶ
	 */
	public int getTableIndex(SuperTable argTable) {
		for(int i=0; i<getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt==argTable) {
				return i;
			}
		}
		for(int j=0; j<getAccountTableNum(); j++) {
			AccountTable at = getAccountTable(j);
			if(at==argTable) {
				// データテーブル数を足すことに注意
				return getDataTableNum() + j;
			}
		}

		Debug.error("テーブルが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}


	
	
	
	public String[] getDataTableNames() {
		String[] names = new String[getDataTableNum()];
		for(int i=0; i<getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			names[i] = dt.getTableName();
		}
		return names;
	}

	public String[] getAccountTableNames() {
		String[] names = new String[getAccountTableNum()];
		for(int i=0; i<getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			names[i] = at.getTableName();
		}
		return names;
	}

	public ArrayList<String> getTableNames() {
		String[] dtNameArray = getDataTableNames();
		int dtNum = dtNameArray.length;
		String[] atNameArray = getAccountTableNames();
		int atNum = atNameArray.length;

		ArrayList<String> names = new ArrayList<String>();
		for(int i=0; i<dtNum; i++) {
			names.add(dtNameArray[i]);
		}
		for(int j=0; j<atNum; j++) {
			names.add(atNameArray[j]);
		}

		return names;
	}

	
	
	
	/*
	 * 指定したロールのアカウントテーブルを探し出して返却（非ゲストロールであることが前提）
	 */
	public AccountTable getAccountTableByRole(Role role) {
		if(!(role instanceof NormalRole)) {
			Debug.error("指定されたロールは非ゲストロールではありません（ゲストロールかと思われます）", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		
		for(int i=0; i<getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.isAccountOwnerRole(role)) {
				return at;
			}
		}

		Debug.error("指定したロールのアカウントテーブルが見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	public SuperTable getTableByField(Field field) {
		for(int i=0; i<this.getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt.containsThisField(field)) {
				return dt;
			}
		}
		for(int i=0; i<this.getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.containsThisField(field)) {
				return at;
			}
		}

		Debug.error("テーブルが見つかりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	//抽象フィールドも含む
	public SuperTable getTableByFieldAllArray(Field field) {
		for(int i=0; i<this.getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt.containsThisFieldAll(field)) {
				return dt;
			}
		}
		for(int i=0; i<this.getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.containsThisFieldAll(field)) {
				return at;
			}
		}

		Debug.error("テーブルが見つかりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	
	
	public static TableManager getInstance() {
		return TableManager.obj;
	}




	public static void updateInstance(TableManager newObject) {
		TableManager.obj = newObject;
	}
}
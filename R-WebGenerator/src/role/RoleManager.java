package role;

import debug.Debug;

import java.io.Serializable;
import java.util.ArrayList;

import property.GeneratorProperty;

import table.AccountTable;
import table.TableManager;

import mainFrame.MainFrame;

import authority.AuthorityManager;


/*
 * Singleton
 */
public class RoleManager implements Serializable {
	private ArrayList<Role> roles = new ArrayList<Role>();
	/*
	 * インスタンスは最後！
	 */
	private static RoleManager obj = new RoleManager();
	
	
	
	private RoleManager() {
		// デフォルトでゲストロールを入れておく
		Role guestRole = new GuestRole(GeneratorProperty.japanese()?"ゲスト":"Guest");
		addRole(guestRole);
	}
	
	
	
	
	
	public Role getRole(int index) {
		return roles.get(index);
	}

	
	
	
	
	public int getRoleIndex(Role argRole) {
		for(int i=0; i<getRoleNum(); i++) {
			Role role = getRole(i);
			if(role==argRole) {
				return i;
			}
		}

		// エラー
		Debug.error("ロールが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
	
	
	
	
	public Role getRoleByName(String name) {
		for(int i=0; i<getRoleNum(); i++) {
			Role role = getRole(i);
			if(role.getRoleName().equals(name)) {
				return role;
			}
		}

		// ◆ERROR : ロールが見つからない
		Debug.error("RoleManagerインスタンス : getRoleByName() : 指定した名前を持つロールが見つかりません");
		return null;
	}
	
	public boolean someNormalRolesHaveNoAt() {
		for(int i=0; i<getRoleNum(); i++) {
			Role role = getRole(i);
			if(role instanceof GuestRole) {
				continue;
			}
			
			NormalRole nr = (NormalRole)role;
			if(nr.accountTableDefined()) {
				Debug.today("ノーマルロール「"+nr.getRoleName()+"」はアカウントテーブルを定義してあるようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				continue;
			}
			
			Debug.today("ノーマルロール「"+nr.getRoleName()+"」はアカウントテーブルを持たないようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return true;
		}
		
		return false;
	}
	
	public ArrayList<String> getNoAtNormalRolesNames() {
		if(!someNormalRolesHaveNoAt()) {
			// エラー
			Debug.error("すべてのノーマルロールがアカウントテーブルを持つのにgetNoAtNormalRolesNames()が呼ばれました。不正です。");
			return null;
		}

		ArrayList<String> names = new ArrayList<String>();
		for(int i=0; i<getRoleNum(); i++) {
			Role role = getRole(i);
			if(role instanceof GuestRole) {
				continue;
			}
			
			NormalRole nr = (NormalRole)role;
			if(nr.accountTableDefined()) {
				continue;
			}

			names.add(role.getRoleName());
		}

		return names;
	}
	
	public void addRole(Role role) {
		roles.add(role);
		AuthorityManager.getInstance().informRoleAddition(role);
	}

	
	
	
	
	public void removeNormalRole(NormalRole role) {
		roles.remove(role);
		
		AccountTable at = role.getAccountTable();

		// アカウントテーブルが定義されていなかった -> 何もしない
		if(at==null) {
		}
		// アカウントテーブルが定義されていて、ロールもろとも削除するべきである場合
		else if(shouldRemoveAtIfRoleRemoved(role)) {
			TableManager.getInstance().removeAccountTable(at);
		}
		// アカウントテーブルは定義されていたが、単にこのロールをアカウントオーナーロール一覧から消せばいい場合
		else {
			at.removeNormalRole(role);
		}

		AuthorityManager.getInstance().informRoleRemovement(role);
	}

	
	public boolean shouldRemoveAtIfRoleRemoved(NormalRole role) {
		boolean shouldDeleteAt = false;

		AccountTable accountTableOfSelectedRole = role.getAccountTable();
		if(accountTableOfSelectedRole instanceof AccountTable) {
			int accOwnerRoleNum = accountTableOfSelectedRole.getNormalRoleNum();
			if(accOwnerRoleNum==1) {	// このロールのアカウントしか管理してない
				shouldDeleteAt = true;
			}
		}

		return shouldDeleteAt;
	}
	
	public int getRoleNum() {
		return roles.size();
	}

	public static RoleManager getInstance() {
		return RoleManager.obj;
	}



	public static void updateInstance(RoleManager newObject) {
		RoleManager.obj = newObject;
	}

}

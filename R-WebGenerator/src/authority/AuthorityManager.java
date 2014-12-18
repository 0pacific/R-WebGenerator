package authority;


import java.io.Serializable;
import java.util.ArrayList;

import debug.Debug;

import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.*;
import table.field.Field;
import transition.Transition;
import transition.TransitionManager;
import view.webPageDefinition.WebPagePanelManager;

/*
 * NOTICE :
 * Singleton
 */
public class AuthorityManager implements Serializable {
	public ArrayList<AuthorityToTable> tableAuthArray = new ArrayList<AuthorityToTable>();
	public ArrayList<TransitionAuth> transAuthArray = new ArrayList<TransitionAuth>();
	/*
	 * インスタンス参照変数は最後！
	 */
	private static AuthorityManager obj = new AuthorityManager();
	

	
	private AuthorityManager() {
	}
	

	/*
	 * PURPOSE :
	 * 新しいテーブルが定義されたことを知り、そのテーブルに対する全ロールの権限を初期化、以後管理する
	 */
	public void informTableAddition(SuperTable table) {
		RoleManager roleManager = RoleManager.getInstance();
		int roleNum = roleManager.getRoleNum();

		for(int i=0; i<roleNum; i++) {
			Role role = roleManager.getRole(i);
			AuthorityToTable authToTable = null;
			if(table instanceof DataTable) {
				authToTable = new AuthorityToDataTable(role, (DataTable)table);
			}
			else if(table instanceof AccountTable) {
				authToTable = new AuthorityToAccountTable(role, (AccountTable)table);
			}
			tableAuthArray.add(authToTable);
		}
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * テーブルが削除されたことを知り、そのテーブルに対する全ロールの権限を削除
	 */
	public void informTableRemovement(SuperTable table) {
		RoleManager roleManager = RoleManager.getInstance();
		for(int i=0; i<roleManager.getRoleNum(); i++) {
			Role role = roleManager.getRole(i);
			removeTableAuth(role, table);
		}
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * データテーブル（アカウントテーブル）に新しいフィールドが追加されたことを受け、
	 * 全ロールのそのデータテーブル（アカウントテーブル）への権限を取り出す
	 * そしてそこへ、新しいフィールドへのRead・Write・ExWrite権限（RA・IAとも）をfalseで追加する
	 */
	public void informFieldAddition(Field newField) {
		SuperTable fieldAddedTable = newField.getTable();
		
		for(int i=0; i<tableAuthArray.size(); i++) {
			AuthorityToTable authToTable = tableAuthArray.get(i);
			if(authToTable.getTable()!=fieldAddedTable) {
				continue;
			}

			authToTable.informFieldAddition(newField);
		}
	}
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 新しいロールが定義されたことを知り、そのロールの全テーブル、全遷移に対する権限を初期化、以後管理する
	 */
	public void informRoleAddition(Role role) {
		TableManager tableManager = TableManager.getInstance();

		// 全データテーブルに対する権限
		int dataTableNum = tableManager.getDataTableNum();
		for(int i=0; i<dataTableNum; i++) {
			DataTable dt = tableManager.getDataTable(i);
			AuthorityToDataTable tableAuth = new AuthorityToDataTable(role, dt);
			tableAuthArray.add(tableAuth);
		}

		// 全アカウントテーブルに対する権限
		int accountTableNum = tableManager.getAccountTableNum();
		for(int i=0; i<accountTableNum; i++) {
			AccountTable at = tableManager.getAccountTable(i);
			AuthorityToAccountTable tableAuth = new AuthorityToAccountTable(role, at);
			tableAuthArray.add(tableAuth);
		}

		// 全遷移に対する遷移権限
		TransitionManager transManager = TransitionManager.getInstance();
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionAuth newTransAuth = new TransitionAuth(role, trans);
			this.transAuthArray.add(newTransAuth);
		}
	}

	
	
	public void informRoleRemovement(NormalRole role) {
		TableManager tableManager = TableManager.getInstance();

		// 全データテーブルに対する権限を削除
		int dataTableNum = tableManager.getDataTableNum();
		for(int i=0; i<dataTableNum; i++) {
			DataTable dt = tableManager.getDataTable(i);
			removeTableAuth(role, dt);
		}

		// 全アカウントテーブルに対する権限を削除
		int accountTableNum = tableManager.getAccountTableNum();
		for(int i=0; i<accountTableNum; i++) {
			AccountTable at = tableManager.getAccountTable(i);
			removeTableAuth(role, at);
		}

		// 全遷移に対する遷移権限を削除
		TransitionManager transManager = TransitionManager.getInstance();
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			removeTransAuth(role, trans);
		}
	}
	
	
	
	
	/*
	 * SUMMARY :
	 * 新しい遷移が定義されたことを知り、全ロールのその遷移に対する権限を初期化する
	 */
	public void informTransAddition(Transition newTrans) {
		RoleManager roleManager = RoleManager.getInstance();
		for(int i=0; i<roleManager.getRoleNum(); i++) {
			Role role = roleManager.getRole(i);
			TransitionAuth newTransAuth = new TransitionAuth(role, newTrans);
			this.transAuthArray.add(newTransAuth);
		}
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * あるロールがあるテーブルへIA-Definedとなったことを受け、全ロールのそのテーブルに対する権限（AuthorityToData/AccountTableインスタンス）に報告
	 */
	public void informIaAddition(Role role, SuperTable table) {
		for(int i=0; i<tableAuthArray.size(); i++) {
			AuthorityToTable auth = tableAuthArray.get(i);
			if(auth.getTable()!=table) {
				continue;
			}

			auth.informIaAddition(role);
		}
	}

	
	
	/*
	 * PURPOSE :
	 * あるロールのあるテーブルに対するIAが消えたことを知り、反映させる
	 */
	public void informIaDeletion(Role role, SuperTable table) {
		for(int i=0; i<tableAuthArray.size(); i++) {
			AuthorityToTable auth = tableAuthArray.get(i);
			if(auth.getTable()!=table) {
				continue;
			}

			auth.informIaDeletion(role);
		}
	}
	
	

	/*
	 * PURPOSE :
	 * 指定したデータテーブルに対し個人権限を持つロール達をArrayListで返却
	 */
	public ArrayList<Role> getIaDefinedRolesOfDataTable(DataTable dataTable) {
		ArrayList<Role> roles = new ArrayList<Role>();

		// 管理している全ての対テーブル権限について処理
		for(int i=0; i<getAuthorityNum(); i++) {
			AuthorityToTable auth = tableAuthArray.get(i);

			// 目当てのデータテーブルに対する権限以外は飛ばす
			if(auth.getTable()!=dataTable) {
				continue;
			}

			AuthorityToDataTable authToDt = (AuthorityToDataTable)auth;
			Role role = authToDt.role;
			
			// このロールが目当てのデータテーブルについてIA-Defined -> 追加
			if(authToDt.authSet.haveIa()) {
				roles.add(role);
			}
		}
		
		return roles;
	}
	
	
	public AuthorityToTable getTableAuth(Role role, SuperTable table) {
		int tableAuthNum = tableAuthArray.size();
		for(int i=0; i<tableAuthNum; i++) {
			AuthorityToTable tableAuth = tableAuthArray.get(i);
			if(tableAuth.getRole()!=role) {
				continue;
			}
			if(tableAuth.getTable()!=table) {
				continue;
			}
			return tableAuth;
		}
		
		// ◆ERROR : 指定したロール＆テーブルに対応する権限が見つからなかった
		Debug.error("AuthorityManagerインスタンスが権限の発見に失敗しました");
		return null;
	}

	
	
	public void removeTableAuth(Role role, SuperTable table) {
		int tableAuthNum = tableAuthArray.size();
		for(int i=0; i<tableAuthNum; i++) {
			AuthorityToTable tableAuth = tableAuthArray.get(i);
			if(tableAuth.getRole()!=role) {
				continue;
			}
			if(tableAuth.getTable()!=table) {
				continue;
			}

			tableAuthArray.remove(tableAuth);
			return;
		}
		
		// ◆ERROR : 指定したロール＆テーブルに対応する権限が見つからなかった
		Debug.error("AuthorityManagerインスタンスが権限の発見に失敗しました");
	}
	
	
	
	public TransitionAuth getTransAuth(Role role, Transition trans) {
		int transAuthNum = transAuthArray.size();
		for(int i=0; i<transAuthNum; i++) {
			TransitionAuth transAuth = transAuthArray.get(i);
			if(transAuth.role!=role) {
				continue;
			}
			if(transAuth.transition!=trans) {
				continue;
			}
			return transAuth;
		}
		
		Debug.error("指定されたロール・遷移に基づきTransitionAuthインスタンスを探しましたが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	public void informTransRemovement(Transition trans) {
		ArrayList<TransitionAuth> transAuthArrayToRemove = new ArrayList<TransitionAuth>();
		
		int transAuthNum = transAuthArray.size();
		for(int i=0; i<transAuthNum; i++) {
			TransitionAuth transAuth = transAuthArray.get(i);
			if(transAuth.transition==trans) {
				transAuthArrayToRemove.add(transAuth);
			}
		}

		for(int i=0; i<transAuthArrayToRemove.size(); i++) {
			TransitionAuth transAuth = transAuthArrayToRemove.get(i);
			boolean remove = transAuthArray.remove(transAuth);
			if(!remove) {
				Debug.error("削除しようとした遷移権限が見つかりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
	}
	
	
	
	
	public void removeTransAuth(Role role, Transition trans) {
		int transAuthNum = transAuthArray.size();
		for(int i=0; i<transAuthNum; i++) {
			TransitionAuth transAuth = transAuthArray.get(i);
			if(transAuth.role!=role) {
				continue;
			}
			if(transAuth.transition!=trans) {
				continue;
			}
			transAuthArray.remove(transAuth);
			return;
		}
		
		Debug.error("指定されたロール・遷移に基づきTransitionAuthインスタンスを探しましたが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	
	
	
	public int getAuthorityNum() {
		return tableAuthArray.size();
	}
	
	
	public static AuthorityManager getInstance() {
		return AuthorityManager.obj;
	}




	public static void updateInstance(AuthorityManager newObject) {
		AuthorityManager.obj = newObject;
	}
}

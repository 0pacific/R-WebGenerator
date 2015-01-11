package authority;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.AbstractField;
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import utility.*;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class AuthorityToAccountTable extends AuthorityToTable implements Serializable {
	public HashMap<NormalRole, AuthoritySet> authSetHashMap;
	
	
	
	
	public AuthorityToAccountTable(Role role, AccountTable accountTable) {
		super(role, accountTable);
		initAuthSets();
	}





	public void initAuthSets() {
		authSetHashMap = new HashMap<NormalRole, AuthoritySet>();
		
		AccountTable accountTable = (AccountTable)table;
		for(int i=0; i<accountTable.getNormalRoleNum(); i++) {
			NormalRole accountOwnerRole = accountTable.getNormalRole(i);
			Debug.today("ロール「"+role.getRoleName()+"」の、アカウントオーナーオール「"+accountOwnerRole.getRoleName()+"」のアカウントに対する権限セットを初期化します。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			AuthoritySet authSetToAccount = new AuthoritySet(this.role, this.table, accountOwnerRole);
			authSetHashMap.put(accountOwnerRole, authSetToAccount);
		}
	}
	
	
	
	public AuthoritySet getAuthorityToAccountByAccountOwnerRoleName(String roleName) {
		// 全ての対アカウント権限の中から該当するものを探す
		Iterator iterator = authSetHashMap.keySet().iterator();
		while(iterator.hasNext()) {
			NormalRole accountOwnerRole = (NormalRole)iterator.next();
			AuthoritySet authSet = authSetHashMap.get(accountOwnerRole);
			if(authSet.accountOwnerRole.getRoleName().equals(roleName)) {
				return authSet;
			}
		}

		Debug.error("アカウントオーナーロール名を指定され、アカウントオーナーロールのアカウントに対する権限を取り出そうとしましたが、見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	




	public void informFieldAddition(Field newField) {
		// 全ての対アカウント権限にフィールドの追加を報告
		Iterator iterator = authSetHashMap.keySet().iterator();
		while(iterator.hasNext()) {
			NormalRole accountOwnerRole = (NormalRole)iterator.next();
			AuthoritySet authSet = authSetHashMap.get(accountOwnerRole);
			authSet.informFieldAddition(newField);
		}
	}
	
	public void informAbFieldAddition(AbstractField field){
		informFieldAddition(field);
	}





	public void informIaAddition(Role role) {
		// 全ての対アカウント権限について、新たに出現したIA-Definedロールに対するIAアサイン権限を初期化
		Iterator iterator = authSetHashMap.keySet().iterator();
		while(iterator.hasNext()) {
			NormalRole accountOwnerRole = (NormalRole)iterator.next();
			AuthoritySet authSet = authSetHashMap.get(accountOwnerRole);
			authSet.initIaAssignAuth(role);
		}
	}
	public void informIaDeletion(Role role) {
		// 全ての対アカウント権限について、IA-Definedでなくなったロールに対するIAアサイン権限を除去
		Iterator iterator = authSetHashMap.keySet().iterator();
		while(iterator.hasNext()) {
			NormalRole accountOwnerRole = (NormalRole)iterator.next();
			AuthoritySet authSet = authSetHashMap.get(accountOwnerRole);
			authSet.removeIaAssignAuth(role);
		}
	}
}

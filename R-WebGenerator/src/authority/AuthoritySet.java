package authority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import debug.Debug;

import role.Role;
import role.RoleManager;
import table.*;
import table.field.Field;

public class AuthoritySet implements Serializable {
	public Role role;
	public SuperTable table;
	public Role accountOwnerRole;
	
	// ロール権限
	public boolean createAuth;
	public boolean raDeleteAuth;
	public HashMap<Field, Boolean> raReadAuthHashMap;
	public HashMap<Field, Boolean> raWriteAuthHashMap;
	public HashMap<Field, Boolean> raExWriteAuthHashMap;

	// 個人権限
	public boolean iaDeleteAuth;
	public HashMap<Field, Boolean> iaReadAuthHashMap;
	public HashMap<Field, Boolean> iaWriteAuthHashMap;
	public HashMap<Field, Boolean> iaExWriteAuthHashMap;

	// 個人権限を有するか否かを表す（デフォルトでfalse）
	public boolean havingIa = false;

	// どのロールに対し個人権限をアサインできるかを表す
	public HashMap<Role, Boolean> iaAssignAuth = new HashMap<Role, Boolean>();




	// accountOwnerRole ... tableがアカウントテーブルの場合に、どのアカウントに対する権限セットかを指定するもの。データテーブルの場合にはnullを渡す
	public AuthoritySet(Role role, SuperTable table, Role accountOwnerRole) {
		this.role = role;
		this.table = table;
		this.accountOwnerRole = accountOwnerRole;
		
		this.raReadAuthHashMap		= new HashMap<Field, Boolean>();
		this.raWriteAuthHashMap		= new HashMap<Field, Boolean>();
		this.raExWriteAuthHashMap	= new HashMap<Field, Boolean>();
		this.iaReadAuthHashMap		= new HashMap<Field, Boolean>();
		this.iaWriteAuthHashMap		= new HashMap<Field, Boolean>();
		this.iaExWriteAuthHashMap	= new HashMap<Field, Boolean>();

		
		// 対フィールド権限の初期化（Read, Write, ExWrite, とも、RA・IA問わず全フィールドに対しfalseとする）
		for(int i=0; i<table.getFieldNum(); i++) {
			Field field = table.getField(i);
			raReadAuthHashMap.put(field, new Boolean(false));
			raWriteAuthHashMap.put(field, new Boolean(false));
			raExWriteAuthHashMap.put(field, new Boolean(false));
			iaReadAuthHashMap.put(field, new Boolean(false));
			iaWriteAuthHashMap.put(field, new Boolean(false));
			iaExWriteAuthHashMap.put(field, new Boolean(false));
		}

		
		// IAアサイン権限の初期化（データテーブルの場合のみ。データテーブルに対しIA-Definedな全ロールに対し、IAアサイン権限を初期化する。デフォルトはfalse）
		if(table instanceof DataTable) {
			ArrayList<Role> iaHavingRoles = AuthorityManager.getInstance().getIaDefinedRolesOfDataTable((DataTable)table);
			for(int i=0; i<iaHavingRoles.size(); i++) {
				Role iaHavingRole = iaHavingRoles.get(i);
				iaAssignAuth.put(iaHavingRole, new Boolean(false));
			}
		}

		
		// この権限セットが、role自身のアカウントに対する権限セットである場合、自動でIA-Definedとする
		if(table instanceof AccountTable && accountOwnerRole==role) {
			havingIa = true;
		}
	}






	public ArrayList<Role> getIaAssignableTargetRoles() {
		ArrayList<Role> roles = new ArrayList<Role>();
		
		for(int i=0; i<RoleManager.getInstance().getRoleNum(); i++) {
			Role role = RoleManager.getInstance().getRole(i);
			if(!iaAssignAuth.containsKey(role)) {
				continue;
			}
			if(iaAssignAuth.get(role).booleanValue()) {
				roles.add(role);
			}
		}

		return roles;
	}






	/*
	 * PURPOSE :
	 * 指定したロールに対しIAアサイン権限があるか否かを返す
	 */
	public boolean checkAssignable(Role role) {
		if(!iaAssignAuth.containsKey(role)) {
			return false;
		}
		return iaAssignAuth.get(role);
	}





	public void informFieldAddition(Field newField) {
		raReadAuthHashMap.put(newField, new Boolean(false));
		raWriteAuthHashMap.put(newField, new Boolean(false));
		raExWriteAuthHashMap.put(newField, new Boolean(false));
		iaReadAuthHashMap.put(newField, new Boolean(false));
		iaWriteAuthHashMap.put(newField, new Boolean(false));
		iaExWriteAuthHashMap.put(newField, new Boolean(false));
	}

	
	
	
	
	// あるロールへのIAアサイン権限をON/OFFにする
	public void enableIaAssignAuth(Role role) {
		iaAssignAuth.remove(role);
		iaAssignAuth.put(role, new Boolean(true));
	}
	public void disableIaAssignAuth(Role role) {
		iaAssignAuth.remove(role);
		iaAssignAuth.put(role, new Boolean(false));
	}
	




	/*
	 * PURPOSE :
	 * あるロールがテーブルに対しIA-Definedとなったことを受け、そのロールに対するIAアサイン権限をfalseの状態で追加
	 * 
	 * NOTICE :
	 * ・IAアサイン権限を「trueにする」のは enableIaAssignAuth()である
	 */
	public void initIaAssignAuth(Role newIaDefinedRole) {
		iaAssignAuth.put(newIaDefinedRole, new Boolean(false));
	}
	// あるロールが削除されたり、IA-Definedでなくなったことを受け、そのロールへのIAアサイン権限そのものを削除する（ONだろうがOFFだろうが）
	public void removeIaAssignAuth(Role role) {
		iaAssignAuth.remove(role);
	}





	public boolean getCreate() {
		return createAuth;
	}

	public void setCreate(boolean auth) {
		createAuth = auth;
	}
	
	public boolean getDelete() {
		return raDeleteAuth;
	}
	
	public void setRaDelete(boolean auth) {
		raDeleteAuth = auth;
	}

	public boolean getIaDelete() {
		return iaDeleteAuth;
	}

	public void setIaDelete(boolean auth) {
		iaDeleteAuth = auth;
	}

	
	// 対フィールドRA権限ゲット
	public boolean getRaRead(int offset) {
		Field field = this.table.getField(offset);
		Boolean permission = raReadAuthHashMap.get(field);
		return permission.booleanValue();
	}
	public boolean getRaWrite(int offset) {
		Field field = this.table.getField(offset);
		Boolean permission = raWriteAuthHashMap.get(field);
		return permission.booleanValue();
	}
	public boolean getRaExWrite(int offset) {
		Field field = this.table.getField(offset);
		Boolean permission = raExWriteAuthHashMap.get(field);
		return permission.booleanValue();
	}

	
	// 対フィールドRA権限セット
	public void setRaRead(int offset, boolean auth) {
		Field field = this.table.getField(offset);
		raReadAuthHashMap.put(field, new Boolean(auth));
	}
	public void setRaWrite(int offset, boolean auth) {
		Field field = this.table.getField(offset);
		raWriteAuthHashMap.put(field, new Boolean(auth));
	}
	public void setRaExWrite(int offset, boolean auth) {
		Field field = this.table.getField(offset);
		raExWriteAuthHashMap.put(field, new Boolean(auth));
	}

	
	// 対フィールドIA権限ゲット
	public boolean getIaRead(int offset) {
		Field field = this.table.getField(offset);
		Boolean permission = iaReadAuthHashMap.get(field);
		return permission.booleanValue();
	}
	public boolean getIaWrite(int offset) {
		Field field = this.table.getField(offset);
		Boolean permission = iaWriteAuthHashMap.get(field);
		return permission.booleanValue();
	}
	public boolean getIaExWrite(int offset) {
		Field field = this.table.getField(offset);
		Boolean permission = iaExWriteAuthHashMap.get(field);
		return permission.booleanValue();
	}

	
	// 対フィールドIA権限セット
	public void setIaRead(int offset, boolean auth) {
		Field field = this.table.getField(offset);
		iaReadAuthHashMap.put(field, new Boolean(auth));
	}
	public void setIaWrite(int offset, boolean auth) {
		Field field = this.table.getField(offset);
		iaWriteAuthHashMap.put(field, new Boolean(auth));
	}
	public void setIaExWrite(int offset, boolean auth) {
		Field field = this.table.getField(offset);
		iaExWriteAuthHashMap.put(field, new Boolean(auth));
	}


	public boolean haveIa() {
		return havingIa;
	}

	public void setHavingIa(boolean auth) {
		havingIa = auth;
	}





}

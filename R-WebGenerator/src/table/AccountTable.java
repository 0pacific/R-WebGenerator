package table;

import java.util.ArrayList;

import gui.*;
import gui.arrow.*;

import role.NormalRole;
import role.Role;
import table.*;
import table.field.Field;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

public class AccountTable extends SuperTable {
	private ArrayList<NormalRole> normalRoles;
	
	public AccountTable(String name, ArrayList<NormalRole> normalRoles) {
		this.name = name;
		this.normalRoles = normalRoles;

		for(int i=0; i<normalRoles.size(); i++) {
			NormalRole nr = normalRoles.get(i);
			nr.setAccountTable(this);
		}
	}

	public void addField(Field field) {
		fieldArray.add(field);
		field.setTable(this);
		TableManager.getInstance().informFieldAddition(field);
	}

	public NormalRole getNormalRole(int index) {
		return normalRoles.get(index);
	}

	public void removeNormalRole(NormalRole normalRole) {
		normalRoles.remove(normalRole);

		// ロールのアカウントテーブルもNULLにしておく
		normalRole.setAccountTable(null);
	}
	
	public int getNormalRoleNum() {
		return normalRoles.size();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * このアカウントテーブルが、指定したロールのアカウントテーブルであるか否かを返す
	 */
	public boolean isAccountOwnerRole(Role role) {
		for(int i=0; i<getNormalRoleNum(); i++) {
			Role r = (Role)getNormalRole(i);
			if(r==role) {
				return true;
			}
		}
		
		return false;
	}
}

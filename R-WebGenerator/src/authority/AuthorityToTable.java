package authority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import debug.Debug;


import role.Role;
import role.RoleManager;
import table.*;
import table.field.Field;

public abstract class AuthorityToTable implements Serializable {
	Role role;
	SuperTable table;

	

	
	
	public AuthorityToTable(Role role, SuperTable table) {
		this.role = role;
		this.table = table;
	}

	
	
	
	
	public Role getRole() {
		return role;
	}

	public SuperTable getTable() {
		return table;
	}





	public abstract void informFieldAddition(Field field);
	public abstract void informIaAddition(Role role);
	public abstract void informIaDeletion(Role role);
}

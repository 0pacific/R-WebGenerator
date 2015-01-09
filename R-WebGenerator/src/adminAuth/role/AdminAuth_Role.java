package adminAuth.role;

import java.util.HashMap;

import role.Role;
import table.field.Field;

public class AdminAuth_Role {

	public Role role;
	//各フィールドごとに変更可能か設定。trueが可能、falseが不可能
	public HashMap<Field, Boolean> adminAuthRoleHash;
	
	
	public AdminAuth_Role(Role role){
		this.role = role;
		adminAuthRoleHash = new HashMap<Field,Boolean>();
	}
	
	public void setField(Field field){
		adminAuthRoleHash.put(field,false);
	}
	
	public void setField_Editable(Field field){
		adminAuthRoleHash.put(field, true);
	}
	
	public boolean isEditable(Field field){
		return adminAuthRoleHash.get(field);
	}
}

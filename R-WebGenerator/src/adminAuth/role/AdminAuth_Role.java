package adminAuth.role;

import java.util.HashMap;

import role.Role;
import table.field.Field;

public class AdminAuth_Role {

	public Role role;
	//�e�t�B�[���h���ƂɕύX�\���ݒ�Btrue���\�Afalse���s�\
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

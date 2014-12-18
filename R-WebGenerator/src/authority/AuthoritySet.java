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
	
	// ���[������
	public boolean createAuth;
	public boolean raDeleteAuth;
	public HashMap<Field, Boolean> raReadAuthHashMap;
	public HashMap<Field, Boolean> raWriteAuthHashMap;
	public HashMap<Field, Boolean> raExWriteAuthHashMap;

	// �l����
	public boolean iaDeleteAuth;
	public HashMap<Field, Boolean> iaReadAuthHashMap;
	public HashMap<Field, Boolean> iaWriteAuthHashMap;
	public HashMap<Field, Boolean> iaExWriteAuthHashMap;

	// �l������L���邩�ۂ���\���i�f�t�H���g��false�j
	public boolean havingIa = false;

	// �ǂ̃��[���ɑ΂��l�������A�T�C���ł��邩��\��
	public HashMap<Role, Boolean> iaAssignAuth = new HashMap<Role, Boolean>();




	// accountOwnerRole ... table���A�J�E���g�e�[�u���̏ꍇ�ɁA�ǂ̃A�J�E���g�ɑ΂��錠���Z�b�g�����w�肷����́B�f�[�^�e�[�u���̏ꍇ�ɂ�null��n��
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

		
		// �΃t�B�[���h�����̏������iRead, Write, ExWrite, �Ƃ��ARA�EIA��킸�S�t�B�[���h�ɑ΂�false�Ƃ���j
		for(int i=0; i<table.getFieldNum(); i++) {
			Field field = table.getField(i);
			raReadAuthHashMap.put(field, new Boolean(false));
			raWriteAuthHashMap.put(field, new Boolean(false));
			raExWriteAuthHashMap.put(field, new Boolean(false));
			iaReadAuthHashMap.put(field, new Boolean(false));
			iaWriteAuthHashMap.put(field, new Boolean(false));
			iaExWriteAuthHashMap.put(field, new Boolean(false));
		}

		
		// IA�A�T�C�������̏������i�f�[�^�e�[�u���̏ꍇ�̂݁B�f�[�^�e�[�u���ɑ΂�IA-Defined�ȑS���[���ɑ΂��AIA�A�T�C������������������B�f�t�H���g��false�j
		if(table instanceof DataTable) {
			ArrayList<Role> iaHavingRoles = AuthorityManager.getInstance().getIaDefinedRolesOfDataTable((DataTable)table);
			for(int i=0; i<iaHavingRoles.size(); i++) {
				Role iaHavingRole = iaHavingRoles.get(i);
				iaAssignAuth.put(iaHavingRole, new Boolean(false));
			}
		}

		
		// ���̌����Z�b�g���Arole���g�̃A�J�E���g�ɑ΂��錠���Z�b�g�ł���ꍇ�A������IA-Defined�Ƃ���
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
	 * �w�肵�����[���ɑ΂�IA�A�T�C�����������邩�ۂ���Ԃ�
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

	
	
	
	
	// ���郍�[���ւ�IA�A�T�C��������ON/OFF�ɂ���
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
	 * ���郍�[�����e�[�u���ɑ΂�IA-Defined�ƂȂ������Ƃ��󂯁A���̃��[���ɑ΂���IA�A�T�C��������false�̏�ԂŒǉ�
	 * 
	 * NOTICE :
	 * �EIA�A�T�C���������utrue�ɂ���v�̂� enableIaAssignAuth()�ł���
	 */
	public void initIaAssignAuth(Role newIaDefinedRole) {
		iaAssignAuth.put(newIaDefinedRole, new Boolean(false));
	}
	// ���郍�[�����폜���ꂽ��AIA-Defined�łȂ��Ȃ������Ƃ��󂯁A���̃��[���ւ�IA�A�T�C���������̂��̂��폜����iON���낤��OFF���낤���j
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

	
	// �΃t�B�[���hRA�����Q�b�g
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

	
	// �΃t�B�[���hRA�����Z�b�g
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

	
	// �΃t�B�[���hIA�����Q�b�g
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

	
	// �΃t�B�[���hIA�����Z�b�g
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

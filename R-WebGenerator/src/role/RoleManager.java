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
	 * �C���X�^���X�͍Ō�I
	 */
	private static RoleManager obj = new RoleManager();
	
	
	
	private RoleManager() {
		// �f�t�H���g�ŃQ�X�g���[�������Ă���
		Role guestRole = new GuestRole(GeneratorProperty.japanese()?"�Q�X�g":"Guest");
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

		// �G���[
		Debug.error("���[����������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
	
	
	
	
	public Role getRoleByName(String name) {
		for(int i=0; i<getRoleNum(); i++) {
			Role role = getRole(i);
			if(role.getRoleName().equals(name)) {
				return role;
			}
		}

		// ��ERROR : ���[����������Ȃ�
		Debug.error("RoleManager�C���X�^���X : getRoleByName() : �w�肵�����O�������[����������܂���");
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
				Debug.today("�m�[�}�����[���u"+nr.getRoleName()+"�v�̓A�J�E���g�e�[�u�����`���Ă���悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				continue;
			}
			
			Debug.today("�m�[�}�����[���u"+nr.getRoleName()+"�v�̓A�J�E���g�e�[�u���������Ȃ��悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return true;
		}
		
		return false;
	}
	
	public ArrayList<String> getNoAtNormalRolesNames() {
		if(!someNormalRolesHaveNoAt()) {
			// �G���[
			Debug.error("���ׂẴm�[�}�����[�����A�J�E���g�e�[�u�������̂�getNoAtNormalRolesNames()���Ă΂�܂����B�s���ł��B");
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

		// �A�J�E���g�e�[�u������`����Ă��Ȃ����� -> �������Ȃ�
		if(at==null) {
		}
		// �A�J�E���g�e�[�u������`����Ă��āA���[������Ƃ��폜����ׂ��ł���ꍇ
		else if(shouldRemoveAtIfRoleRemoved(role)) {
			TableManager.getInstance().removeAccountTable(at);
		}
		// �A�J�E���g�e�[�u���͒�`����Ă������A�P�ɂ��̃��[�����A�J�E���g�I�[�i�[���[���ꗗ��������΂����ꍇ
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
			if(accOwnerRoleNum==1) {	// ���̃��[���̃A�J�E���g�����Ǘ����ĂȂ�
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

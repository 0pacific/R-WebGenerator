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
	 * �C���X�^���X�Q�ƕϐ��͍Ō�I
	 */
	private static AuthorityManager obj = new AuthorityManager();
	

	
	private AuthorityManager() {
	}
	

	/*
	 * PURPOSE :
	 * �V�����e�[�u������`���ꂽ���Ƃ�m��A���̃e�[�u���ɑ΂���S���[���̌������������A�Ȍ�Ǘ�����
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
	 * �e�[�u�����폜���ꂽ���Ƃ�m��A���̃e�[�u���ɑ΂���S���[���̌������폜
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
	 * �f�[�^�e�[�u���i�A�J�E���g�e�[�u���j�ɐV�����t�B�[���h���ǉ����ꂽ���Ƃ��󂯁A
	 * �S���[���̂��̃f�[�^�e�[�u���i�A�J�E���g�e�[�u���j�ւ̌��������o��
	 * �����Ă����ցA�V�����t�B�[���h�ւ�Read�EWrite�EExWrite�����iRA�EIA�Ƃ��j��false�Œǉ�����
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
	 * �V�������[������`���ꂽ���Ƃ�m��A���̃��[���̑S�e�[�u���A�S�J�ڂɑ΂��錠�����������A�Ȍ�Ǘ�����
	 */
	public void informRoleAddition(Role role) {
		TableManager tableManager = TableManager.getInstance();

		// �S�f�[�^�e�[�u���ɑ΂��錠��
		int dataTableNum = tableManager.getDataTableNum();
		for(int i=0; i<dataTableNum; i++) {
			DataTable dt = tableManager.getDataTable(i);
			AuthorityToDataTable tableAuth = new AuthorityToDataTable(role, dt);
			tableAuthArray.add(tableAuth);
		}

		// �S�A�J�E���g�e�[�u���ɑ΂��錠��
		int accountTableNum = tableManager.getAccountTableNum();
		for(int i=0; i<accountTableNum; i++) {
			AccountTable at = tableManager.getAccountTable(i);
			AuthorityToAccountTable tableAuth = new AuthorityToAccountTable(role, at);
			tableAuthArray.add(tableAuth);
		}

		// �S�J�ڂɑ΂���J�ڌ���
		TransitionManager transManager = TransitionManager.getInstance();
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionAuth newTransAuth = new TransitionAuth(role, trans);
			this.transAuthArray.add(newTransAuth);
		}
	}

	
	
	public void informRoleRemovement(NormalRole role) {
		TableManager tableManager = TableManager.getInstance();

		// �S�f�[�^�e�[�u���ɑ΂��錠�����폜
		int dataTableNum = tableManager.getDataTableNum();
		for(int i=0; i<dataTableNum; i++) {
			DataTable dt = tableManager.getDataTable(i);
			removeTableAuth(role, dt);
		}

		// �S�A�J�E���g�e�[�u���ɑ΂��錠�����폜
		int accountTableNum = tableManager.getAccountTableNum();
		for(int i=0; i<accountTableNum; i++) {
			AccountTable at = tableManager.getAccountTable(i);
			removeTableAuth(role, at);
		}

		// �S�J�ڂɑ΂���J�ڌ������폜
		TransitionManager transManager = TransitionManager.getInstance();
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			removeTransAuth(role, trans);
		}
	}
	
	
	
	
	/*
	 * SUMMARY :
	 * �V�����J�ڂ���`���ꂽ���Ƃ�m��A�S���[���̂��̑J�ڂɑ΂��錠��������������
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
	 * ���郍�[��������e�[�u����IA-Defined�ƂȂ������Ƃ��󂯁A�S���[���̂��̃e�[�u���ɑ΂��錠���iAuthorityToData/AccountTable�C���X�^���X�j�ɕ�
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
	 * ���郍�[���̂���e�[�u���ɑ΂���IA�����������Ƃ�m��A���f������
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
	 * �w�肵���f�[�^�e�[�u���ɑ΂��l�����������[���B��ArrayList�ŕԋp
	 */
	public ArrayList<Role> getIaDefinedRolesOfDataTable(DataTable dataTable) {
		ArrayList<Role> roles = new ArrayList<Role>();

		// �Ǘ����Ă���S�Ă̑΃e�[�u�������ɂ��ď���
		for(int i=0; i<getAuthorityNum(); i++) {
			AuthorityToTable auth = tableAuthArray.get(i);

			// �ړ��Ẵf�[�^�e�[�u���ɑ΂��錠���ȊO�͔�΂�
			if(auth.getTable()!=dataTable) {
				continue;
			}

			AuthorityToDataTable authToDt = (AuthorityToDataTable)auth;
			Role role = authToDt.role;
			
			// ���̃��[�����ړ��Ẵf�[�^�e�[�u���ɂ���IA-Defined -> �ǉ�
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
		
		// ��ERROR : �w�肵�����[�����e�[�u���ɑΉ����錠����������Ȃ�����
		Debug.error("AuthorityManager�C���X�^���X�������̔����Ɏ��s���܂���");
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
		
		// ��ERROR : �w�肵�����[�����e�[�u���ɑΉ����錠����������Ȃ�����
		Debug.error("AuthorityManager�C���X�^���X�������̔����Ɏ��s���܂���");
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
		
		Debug.error("�w�肳�ꂽ���[���E�J�ڂɊ�Â�TransitionAuth�C���X�^���X��T���܂�����������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
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
				Debug.error("�폜���悤�Ƃ����J�ڌ�����������܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
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
		
		Debug.error("�w�肳�ꂽ���[���E�J�ڂɊ�Â�TransitionAuth�C���X�^���X��T���܂�����������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
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

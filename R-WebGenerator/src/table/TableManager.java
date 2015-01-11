package table;

import authority.AuthorityManager;
import gui.*;
import gui.arrow.*;
import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.*;
import table.field.AbstractField;
import table.field.Field;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

import java.io.Serializable;
import java.util.ArrayList;

import pageElement.PageElementObserver;
import debug.Debug;

/*
 * DUTY :
 * �f�[�^�e�[�u���E�A�J�E���g�e�[�u�����ꊇ�Ǘ�
 * 
 * NOTICE :
 * Singleton
 */
public class TableManager implements Serializable {
	private ArrayList<DataTable> dataTables;
	private ArrayList<AccountTable> accountTables;
	/*
	 * �C���X�^���X�ϐ��͍Ō�I
	 */
	private static TableManager obj = new TableManager();
	
	
	
	private TableManager() {
		dataTables = new ArrayList<DataTable>();
		accountTables = new ArrayList<AccountTable>();
	}

	public void addDataTable(DataTable dataTable) {
		dataTables.add(dataTable);
		AuthorityManager.getInstance().informTableAddition(dataTable);
	}

	public void addAccountTable(AccountTable accountTable) {
		accountTables.add(accountTable);
		AuthorityManager.getInstance().informTableAddition(accountTable);
	}

	public void removeAccountTable(AccountTable accountTable) {
		accountTables.remove(accountTable);

		// �A�J�E���g�e�[�u���ŃA�J�E���g���Ǘ�����Ă����S���[���ɂ��āA�A�J�E���g�e�[�u����NULL�Ƃ���
		for(int i=0; i<accountTable.getNormalRoleNum(); i++) {
			NormalRole accOwnerRole = accountTable.getNormalRole(i);
			accOwnerRole.setAccountTable(null);
		}

		PageElementObserver.getInstance().informTableRemovement(accountTable);
		AuthorityManager.getInstance().informTableRemovement(accountTable);
	}
	
	public DataTable getDataTable(int index) {
		return dataTables.get(index);
	}

	public AccountTable getAccountTable(int index) {
		return accountTables.get(index);
	}
	
	
	public SuperTable getTableByName(String name) {
		for(int i=0; i<getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt.getTableName().equals(name)) {
				return dt;
			}
		}
		for(int i=0; i<getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.getTableName().equals(name)) {
				return at;
			}
		}
		
		// ��ERROR : �e�[�u����������Ȃ�����
		Debug.error("�e�[�u���u"+name+"�v��������܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	public int getDataTableNum() {
		return dataTables.size();
	}

	public int getAccountTableNum() {
		return accountTables.size();
	}

	
	
	public void informFieldAddition(Field newField) {
		AuthorityManager.getInstance().informFieldAddition(newField);
	}
	
	public void informAbFieldAddition(AbstractField newAbField){
		AuthorityManager.getInstance().informAbFieldAddition(newAbField);
	}
	
	

	/*
	 * SUMMARY :
	 * �e�[�u���̃C���f�b�N�X�i�ԍ��j���擾
	 * 
	 * NOTICE :
	 * ���C���f�b�N�X�́A�O�Ԃ���f�[�^�e�[�u�������сA�y�f�[�^�e�[�u�����z�Ԗڂ���A�J�E���g�e�[�u��������
	 */
	public int getTableIndex(SuperTable argTable) {
		for(int i=0; i<getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt==argTable) {
				return i;
			}
		}
		for(int j=0; j<getAccountTableNum(); j++) {
			AccountTable at = getAccountTable(j);
			if(at==argTable) {
				// �f�[�^�e�[�u�����𑫂����Ƃɒ���
				return getDataTableNum() + j;
			}
		}

		Debug.error("�e�[�u����������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}


	
	
	
	public String[] getDataTableNames() {
		String[] names = new String[getDataTableNum()];
		for(int i=0; i<getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			names[i] = dt.getTableName();
		}
		return names;
	}

	public String[] getAccountTableNames() {
		String[] names = new String[getAccountTableNum()];
		for(int i=0; i<getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			names[i] = at.getTableName();
		}
		return names;
	}

	public ArrayList<String> getTableNames() {
		String[] dtNameArray = getDataTableNames();
		int dtNum = dtNameArray.length;
		String[] atNameArray = getAccountTableNames();
		int atNum = atNameArray.length;

		ArrayList<String> names = new ArrayList<String>();
		for(int i=0; i<dtNum; i++) {
			names.add(dtNameArray[i]);
		}
		for(int j=0; j<atNum; j++) {
			names.add(atNameArray[j]);
		}

		return names;
	}

	
	
	
	/*
	 * �w�肵�����[���̃A�J�E���g�e�[�u����T���o���ĕԋp�i��Q�X�g���[���ł��邱�Ƃ��O��j
	 */
	public AccountTable getAccountTableByRole(Role role) {
		if(!(role instanceof NormalRole)) {
			Debug.error("�w�肳�ꂽ���[���͔�Q�X�g���[���ł͂���܂���i�Q�X�g���[�����Ǝv���܂��j", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		
		for(int i=0; i<getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.isAccountOwnerRole(role)) {
				return at;
			}
		}

		Debug.error("�w�肵�����[���̃A�J�E���g�e�[�u����������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	public SuperTable getTableByField(Field field) {
		for(int i=0; i<this.getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt.containsThisField(field)) {
				return dt;
			}
		}
		for(int i=0; i<this.getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.containsThisField(field)) {
				return at;
			}
		}

		Debug.error("�e�[�u����������܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	//���ۃt�B�[���h���܂�
	public SuperTable getTableByFieldAllArray(Field field) {
		for(int i=0; i<this.getDataTableNum(); i++) {
			DataTable dt = getDataTable(i);
			if(dt.containsThisFieldAll(field)) {
				return dt;
			}
		}
		for(int i=0; i<this.getAccountTableNum(); i++) {
			AccountTable at = getAccountTable(i);
			if(at.containsThisFieldAll(field)) {
				return at;
			}
		}

		Debug.error("�e�[�u����������܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	
	
	public static TableManager getInstance() {
		return TableManager.obj;
	}




	public static void updateInstance(TableManager newObject) {
		TableManager.obj = newObject;
	}
}
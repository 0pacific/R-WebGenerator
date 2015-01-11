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
			Debug.today("���[���u"+role.getRoleName()+"�v�́A�A�J�E���g�I�[�i�[�I�[���u"+accountOwnerRole.getRoleName()+"�v�̃A�J�E���g�ɑ΂��錠���Z�b�g�����������܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			AuthoritySet authSetToAccount = new AuthoritySet(this.role, this.table, accountOwnerRole);
			authSetHashMap.put(accountOwnerRole, authSetToAccount);
		}
	}
	
	
	
	public AuthoritySet getAuthorityToAccountByAccountOwnerRoleName(String roleName) {
		// �S�Ă̑΃A�J�E���g�����̒�����Y��������̂�T��
		Iterator iterator = authSetHashMap.keySet().iterator();
		while(iterator.hasNext()) {
			NormalRole accountOwnerRole = (NormalRole)iterator.next();
			AuthoritySet authSet = authSetHashMap.get(accountOwnerRole);
			if(authSet.accountOwnerRole.getRoleName().equals(roleName)) {
				return authSet;
			}
		}

		Debug.error("�A�J�E���g�I�[�i�[���[�������w�肳��A�A�J�E���g�I�[�i�[���[���̃A�J�E���g�ɑ΂��錠�������o�����Ƃ��܂������A������܂���ł����B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	




	public void informFieldAddition(Field newField) {
		// �S�Ă̑΃A�J�E���g�����Ƀt�B�[���h�̒ǉ����
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
		// �S�Ă̑΃A�J�E���g�����ɂ��āA�V���ɏo������IA-Defined���[���ɑ΂���IA�A�T�C��������������
		Iterator iterator = authSetHashMap.keySet().iterator();
		while(iterator.hasNext()) {
			NormalRole accountOwnerRole = (NormalRole)iterator.next();
			AuthoritySet authSet = authSetHashMap.get(accountOwnerRole);
			authSet.initIaAssignAuth(role);
		}
	}
	public void informIaDeletion(Role role) {
		// �S�Ă̑΃A�J�E���g�����ɂ��āAIA-Defined�łȂ��Ȃ������[���ɑ΂���IA�A�T�C������������
		Iterator iterator = authSetHashMap.keySet().iterator();
		while(iterator.hasNext()) {
			NormalRole accountOwnerRole = (NormalRole)iterator.next();
			AuthoritySet authSet = authSetHashMap.get(accountOwnerRole);
			authSet.removeIaAssignAuth(role);
		}
	}
}

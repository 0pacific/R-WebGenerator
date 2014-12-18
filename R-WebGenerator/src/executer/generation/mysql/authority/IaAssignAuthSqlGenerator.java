package executer.generation.mysql.authority;

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





public class IaAssignAuthSqlGenerator {
	public String getSql() {
		String sql = "";

		// �e�[�u��"ia_assign_auth"���쐬����SQL
		sql +=	"CREATE TABLE IF NOT EXISTS `ia_assign_auth` (\n" +
				"`tableNumber` int(11) NOT NULL default '0',\n" +
				"`accOwnRoleNumber` int(11) default NULL,\n" +
				"`assignerRoleNumber` int(11) NOT NULL default '0' COMMENT '�A�T�C�����鑤�̃��[���̔ԍ�',\n" +
				"`assignedRoleNumber` int(11) NOT NULL default '0' COMMENT '�A�T�C������郍�[���̔ԍ�',\n" +
				"`permission` enum('YES','NO') NOT NULL default 'NO'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";
				
		String insertSql = "INSERT INTO `ia_assign_auth` (`tableNumber`, `accOwnRoleNumber`, `assignerRoleNumber`, `assignedRoleNumber`, `permission`) VALUES\n";

		AuthorityManager authManager = AuthorityManager.getInstance();
		TableManager tableManager = TableManager.getInstance();
		RoleManager roleManager = RoleManager.getInstance();

		// ��`����Ă���IA�̐��𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalIaNum = 0;

		// �e�f�[�^�e�[�u���ɂ��ď���
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int dtNumber = dt.getTableNumber();

			// �e���[���ɂ��ď���
			for(int j=0; j<roleManager.getRoleNum(); j++) {
				Role role = roleManager.getRole(j);
				int roleNumber = role.getRoleNumber();
				
				AuthorityToDataTable authToDt = (AuthorityToDataTable)authManager.getTableAuth(role, dt);
				AuthoritySet auth = authToDt.authSet;

				Iterator iterator = auth.iaAssignAuth.keySet().iterator();
				// ���̃f�[�^�e�[�u���ɑ΂���IA-Defined�Ȋe���[���ɂ��ď���
				while(iterator.hasNext()) {
					totalIaNum++;
					
					Role iaDefinedRole = (Role)iterator.next();
					boolean iaAssignAuthToThisRole = auth.iaAssignAuth.get(iaDefinedRole);
					insertSql +=	"(" +
							dtNumber + ", " +
							"-1, " +
							roleNumber + ", " +
							iaDefinedRole.getRoleNumber() + ", " +
							"'" + convBoolToYesNo(iaAssignAuthToThisRole) + "'" +
							"),\n";
				}
			}
		}

		
		/*
		 * �A�J�E���g�e�[�u���Ɋւ��ẮAIA�A�T�C�������̒�`���̂��K�v�Ȃ�
		 * Create�����������ۂ��̖��ł���
		 * ���s�v���O�����ł́A�A�J�E���g�e�[�u���̃��R�[�h���쐬���ꂽ�ۂɁA���̎�����������I�Ƀ��R�[�h�֑Ή��t����悤�ɂȂ��Ă���
		 * �����ăA�J�E���g�I�[�i�[���[���ȊO�̃��[���ւ�IA�͑��݂����Ȃ��i��`�ł��Ȃ��j���߁A
		 * IA�A�T�C���������̂���`�s�v�Ƃ������ƂɂȂ�
		 */

		
		if(totalIaNum==0) {
			insertSql = "";
		}
		else {
			// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		
		return sql+insertSql;
	}






	public String convBoolToYesNo(boolean bool) {
		if(bool) {
			return "YES";
		}

		return "NO";
	}
}

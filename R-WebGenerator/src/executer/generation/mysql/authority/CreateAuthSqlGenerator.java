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





public class CreateAuthSqlGenerator {
	public String getSql() {
		String sql = "";

		// �e�[�u��"role"���쐬����SQL
		sql +=	"CREATE TABLE IF NOT EXISTS `create_auth` (\n" +
				"`tableNumber` tinyint(4) NOT NULL default '0',\n" +
				"`accOwnRoleNumber` int(11) default NULL,\n" +
				"`roleNumber` tinyint(4) NOT NULL default '0',\n" +
				"`permission` enum('YES','NO') NOT NULL default 'NO'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		sql +=	"INSERT INTO `create_auth` (`tableNumber`, `accOwnRoleNumber`, `roleNumber`, `permission`) VALUES\n";

		
		AuthorityManager authManager = AuthorityManager.getInstance();
		TableManager tableManager = TableManager.getInstance();
		RoleManager roleManager = RoleManager.getInstance();
		
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int dtNumber = dt.getTableNumber();

			for(int j=0; j<roleManager.getRoleNum(); j++) {
				Role role = roleManager.getRole(j);
				int roleNumber = role.getRoleNumber();
				
				AuthorityToDataTable authToDt = (AuthorityToDataTable)authManager.getTableAuth(role, dt);
				AuthoritySet auth = authToDt.authSet;

				boolean createAuth = auth.createAuth;
				
				sql +=	"(" +
						dtNumber + ", " +
						"NULL, " +
						roleNumber + ", ";
				if(createAuth)
					sql += "'YES'),\n";
				else
					sql += "'NO'),\n";
			}
		}

		
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			int atNumber = at.getTableNumber();

			for(int j=0; j<roleManager.getRoleNum(); j++) {
				Role role = roleManager.getRole(j);
				int roleNumber = role.getRoleNumber();

				// �܂��̓��[��role�́A�A�J�E���g�e�[�u��at�ɑ΂��錠�����擾
				AuthorityToAccountTable authToAt = (AuthorityToAccountTable)authManager.getTableAuth(role, at);

				// ��������A�e�A�J�E���g�I�[�i�[���[���̃A�J�E���g�ɑ΂��錠�������o��
				for(int k=0; k<at.getNormalRoleNum(); k++) {
					NormalRole accountOwnerRole = at.getNormalRole(k);
					AuthoritySet auth = authToAt.authSetHashMap.get(accountOwnerRole);

					boolean createAuth = auth.createAuth;
					
					sql +=	"(" +
							atNumber + ", " +
							accountOwnerRole.getRoleNumber() + ", " +
							roleNumber + ", ";
					if(createAuth)
						sql += "'YES'),\n";
					else
						sql += "'NO'),\n";
				}
			}
		}

		
		// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}
}

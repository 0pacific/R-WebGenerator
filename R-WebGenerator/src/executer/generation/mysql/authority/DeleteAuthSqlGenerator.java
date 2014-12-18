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





public class DeleteAuthSqlGenerator {
	public String getSql() {
		String sql = "";

		// テーブル"delete_auth"を作成するSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `delete_auth` (\n" +
				"`tableNumber` tinyint(4) NOT NULL default '0',\n" +
				"`accOwnRoleNumber` int(11) default NULL COMMENT '対象アカウントを持つロールの番号',\n" +
				"`roleNumber` tinyint(4) NOT NULL default '0',\n" +
				"`status` enum('self','role') NOT NULL default 'role',\n" +
				"`permission` enum('YES','NO') NOT NULL default 'NO'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";
				
		sql +=	"INSERT INTO `delete_auth` (`tableNumber`, `accOwnRoleNumber`, `roleNumber`, `status`, `permission`) VALUES\n";

		
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

				// RA-Deleteのレコード
				boolean raDeleteAuth = auth.raDeleteAuth;
				sql +=	"(" +
						dtNumber + ", " +
						"NULL, " +
						roleNumber + ", " +
						"'role', ";
				if(raDeleteAuth)
					sql += "'YES'),\n";
				else
					sql += "'NO'),\n";

				// IA-Deleteのレコード（IA-Definedなロールの場合のみ）
				if(auth.havingIa) {
					boolean iaDeleteAuth = auth.iaDeleteAuth;
					sql +=	"(" +
							dtNumber + ", " +
							"NULL, " +
							roleNumber + ", " +
							"'self', ";
					if(iaDeleteAuth)
						sql += "'YES'),\n";
					else
						sql += "'NO'),\n";
				}
			}
		}

		
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			int atNumber = at.getTableNumber();

			for(int j=0; j<roleManager.getRoleNum(); j++) {
				Role role = roleManager.getRole(j);
				int roleNumber = role.getRoleNumber();

				// まずはロールroleの、アカウントテーブルatに対する権限を取得
				AuthorityToAccountTable authToAt = (AuthorityToAccountTable)authManager.getTableAuth(role, at);

				// そこから、各アカウントオーナーロールのアカウントに対する権限を取り出す
				for(int k=0; k<at.getNormalRoleNum(); k++) {
					NormalRole accountOwnerRole = at.getNormalRole(k);
					AuthoritySet auth = authToAt.authSetHashMap.get(accountOwnerRole);

					// RA-Deleteのレコード
					boolean raDeleteAuth = auth.raDeleteAuth;
					sql +=	"(" +
							atNumber + ", " +
							accountOwnerRole.getRoleNumber() + ", " +
							roleNumber + ", " +
							"'role', ";
					if(raDeleteAuth)
						sql += "'YES'),\n";
					else
						sql += "'NO'),\n";

					// IA-Deleteのレコード（IA-Definedなロールの場合のみ）
					if(auth.havingIa) {
						boolean iaDeleteAuth = auth.iaDeleteAuth;
						sql +=	"(" +
								atNumber + ", " +
								accountOwnerRole.getRoleNumber() + ", " +
								roleNumber + ", " +
								"'self', ";
						if(iaDeleteAuth)
							sql += "'YES'),\n";
						else
							sql += "'NO'),\n";
					}
				}
			}
		}

		
		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}
}

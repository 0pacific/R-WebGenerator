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





public class FieldAuthSqlGenerator {
	public String getSql() {
		String sql = "";

		// テーブル"field_auth"を作成するSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `field_auth` (\n" +
				"`roleNumber` int(11) NOT NULL default '0',\n" +
				"`tableNumber` tinyint(4) NOT NULL default '0',\n" +
				"`accOwnRoleNumber` int(11) default NULL COMMENT '対象アカウントを持つロールの番号',\n" +
				"`fieldNumber` tinyint(4) NOT NULL default '0',\n" +
				"`status` enum('self','role') NOT NULL default 'role',\n" +
				"`ra` enum('YES','NO') NOT NULL default 'NO',\n" +
				"`wa` enum('YES','NO') NOT NULL default 'NO',\n" +
				"`ea` enum('YES','NO') NOT NULL default 'NO'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";
				
		sql +=	"INSERT INTO `field_auth` (`roleNumber`, `tableNumber`, `accOwnRoleNumber`, `fieldNumber`, `status`, `ra`, `wa`, `ea`) VALUES\n";

		
		AuthorityManager authManager = AuthorityManager.getInstance();
		TableManager tableManager = TableManager.getInstance();
		RoleManager roleManager = RoleManager.getInstance();
		
		// 各データテーブルについて処理
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int dtNumber = dt.getTableNumber();

			// 各ロールについて処理
			for(int j=0; j<roleManager.getRoleNum(); j++) {
				Role role = roleManager.getRole(j);
				int roleNumber = role.getRoleNumber();
				
				AuthorityToDataTable authToDt = (AuthorityToDataTable)authManager.getTableAuth(role, dt);
				AuthoritySet auth = authToDt.authSet;

				// 各フィールドについて処理
				for(int offset=0; offset<dt.getFieldNum(); offset++) {
					Field field = dt.getField(offset);

					// RAのレコード
					boolean raReadAuth = auth.raReadAuthHashMap.get(field).booleanValue();
					boolean raWriteAuth = auth.raWriteAuthHashMap.get(field).booleanValue();
					boolean raExWriteAuth = auth.raExWriteAuthHashMap.get(field).booleanValue();
					sql +=	"(" +
							roleNumber + ", " +
							dtNumber + ", " +
							"NULL, " +
							offset + ", " +
							"'role', " +
							"'" + convBoolToYesNo(raReadAuth) + "', " +
							"'" + convBoolToYesNo(raWriteAuth) + "', " +
							"'" + convBoolToYesNo(raExWriteAuth) + "'),\n";

					// IAのレコード（IA-Definedなロールの場合のみ）
					if(auth.havingIa) {
						boolean iaReadAuth = auth.iaReadAuthHashMap.get(field).booleanValue();
						boolean iaWriteAuth = auth.iaWriteAuthHashMap.get(field).booleanValue();
						boolean iaExWriteAuth = auth.iaExWriteAuthHashMap.get(field).booleanValue();
						sql +=	"(" +
								roleNumber + ", " +
								dtNumber + ", " +
								"NULL, " +
								offset + ", " +
								"'self', " +
								"'" + convBoolToYesNo(iaReadAuth) + "', " +
								"'" + convBoolToYesNo(iaWriteAuth) + "', " +
								"'" + convBoolToYesNo(iaExWriteAuth) + "'),\n";
					}
				}
			}
		}

		
		// 各アカウントテーブルについて処理
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

					// 各フィールドについて処理
					for(int offset=0; offset<at.getFieldNum(); offset++) {
						Field field = at.getField(offset);

						// RAのレコード
						boolean raReadAuth = auth.raReadAuthHashMap.get(field).booleanValue();
						boolean raWriteAuth = auth.raWriteAuthHashMap.get(field).booleanValue();
						boolean raExWriteAuth = auth.raExWriteAuthHashMap.get(field).booleanValue();
						sql +=	"(" +
								roleNumber + ", " +
								atNumber + ", " +
								accountOwnerRole.getRoleNumber() + ", " +
								offset + ", " +
								"'role', " +
								"'" + convBoolToYesNo(raReadAuth) + "', " +
								"'" + convBoolToYesNo(raWriteAuth) + "', " +
								"'" + convBoolToYesNo(raExWriteAuth) + "'),\n";

						// IAのレコード（IA-Definedなロールの場合のみ）
						if(auth.havingIa) {
							boolean iaReadAuth = auth.iaReadAuthHashMap.get(field).booleanValue();
							boolean iaWriteAuth = auth.iaWriteAuthHashMap.get(field).booleanValue();
							boolean iaExWriteAuth = auth.iaExWriteAuthHashMap.get(field).booleanValue();
							sql +=	"(" +
									roleNumber + ", " +
									atNumber + ", " +
									accountOwnerRole.getRoleNumber() + ", " +
									offset + ", " +
									"'self', " +
									"'" + convBoolToYesNo(iaReadAuth) + "', " +
									"'" + convBoolToYesNo(iaWriteAuth) + "', " +
									"'" + convBoolToYesNo(iaExWriteAuth) + "'),\n";
						}
					}
				}
			}
		}

		
		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}

	
	
	
	
	public String convBoolToYesNo(boolean bool) {
		if(bool) {
			return "YES";
		}

		return "NO";
	}
}
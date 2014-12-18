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

		// テーブル"ia_assign_auth"を作成するSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `ia_assign_auth` (\n" +
				"`tableNumber` int(11) NOT NULL default '0',\n" +
				"`accOwnRoleNumber` int(11) default NULL,\n" +
				"`assignerRoleNumber` int(11) NOT NULL default '0' COMMENT 'アサインする側のロールの番号',\n" +
				"`assignedRoleNumber` int(11) NOT NULL default '0' COMMENT 'アサインされるロールの番号',\n" +
				"`permission` enum('YES','NO') NOT NULL default 'NO'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";
				
		String insertSql = "INSERT INTO `ia_assign_auth` (`tableNumber`, `accOwnRoleNumber`, `assignerRoleNumber`, `assignedRoleNumber`, `permission`) VALUES\n";

		AuthorityManager authManager = AuthorityManager.getInstance();
		TableManager tableManager = TableManager.getInstance();
		RoleManager roleManager = RoleManager.getInstance();

		// 定義されているIAの数を数えておく（0のときにINSERT文の部分を消すため）
		int totalIaNum = 0;

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

				Iterator iterator = auth.iaAssignAuth.keySet().iterator();
				// このデータテーブルに対してIA-Definedな各ロールについて処理
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
		 * アカウントテーブルに関しては、IAアサイン権限の定義自体が必要ない
		 * Create権限を持つか否かの問題である
		 * 実行プログラムでは、アカウントテーブルのレコードが作成された際に、その持ち主を自動的にレコードへ対応付けるようになっている
		 * そしてアカウントオーナーロール以外のロールへのIAは存在し得ない（定義できない）ため、
		 * IAアサイン権限自体が定義不要ということになる
		 */

		
		if(totalIaNum==0) {
			insertSql = "";
		}
		else {
			// 最後の",\n"をカットし、";\n"とする
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

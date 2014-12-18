package executer.generation.mysql;

import javax.swing.*;

import authority.AuthorityManager;
import authority.TransitionAuth;

import java.awt.*;
import java.util.*;
import java.io.*;

import debug.Debug;
import gui.*;
import gui.arrow.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import transition.TransitionManager;
import utility.BooleanYesNoConverter;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;
import executer.generation.mysql.*;





public class TransAuthSqlGenerator {
	public String getSql() {
		String sql = "";
		TransitionManager transManager = TransitionManager.getInstance();
		RoleManager roleManager = RoleManager.getInstance();
		AuthorityManager authorityManager = AuthorityManager.getInstance();
		
		// テーブル"transition_auth"のSQLを作成
		sql +=	"CREATE TABLE IF NOT EXISTS `transition_auth` (\n" +
				"`transPrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT '遷移（遷移プロセス）の主キー',\n" +
				"`roleNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`permission` enum('YES','NO') NOT NULL DEFAULT 'YES'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		String insertSql = "INSERT INTO `transition_auth` (`transPrimaryKey`, `roleNumber`, `permission`) VALUES\n";

		// 遷移権限の数を数える（ロールはゲストが必ずいるが遷移の数が０だと、レコードは０でINSERT文を消さなきゃならんので・・・）
		int transAuthNum = 0;
		
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			int transKey = trans.transKey;

			for(int j=0; j<roleManager.getRoleNum(); j++) {
				Role role = roleManager.getRole(j);
				TransitionAuth transAuth = authorityManager.getTransAuth(role, trans);

				insertSql +=	"(" +
								transKey + ", " +
								role.getRoleNumber() + ", " +
								"'" + BooleanYesNoConverter.convBoolToYesNo(transAuth.transAuth) + "'" +
								"),\n";

				transAuthNum++;
			}
		}

		if(transAuthNum==0) {
			insertSql = "";
		}
		else {
			// 最後の",\n"をカットし、";\n"とする
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		

		return sql + insertSql;
	}
}

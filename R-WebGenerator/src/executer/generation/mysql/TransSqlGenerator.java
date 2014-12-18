package executer.generation.mysql;

import javax.swing.*;

import authority.AuthorityManager;

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





public class TransSqlGenerator {
	/*
	 * 全ページエレメント、全遷移に主キーがあたえてあることが前提
	 */
	public String getSql() {
		String sql = "";
		TransitionManager transManager = TransitionManager.getInstance();

		// テーブル"transition"のSQLを作成
		sql +=	"CREATE TABLE `transition` (\n" +
				"`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0' COMMENT '遷移元ページのページエレメントの主キー',\n" +
				"`tepNumber` int(11) NOT NULL DEFAULT '0' COMMENT '遷移先ページの番号',\n" +
				"`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '遷移プロセスの主キー',\n" +
				"PRIMARY KEY (`primaryKey`),\n" +
				"UNIQUE KEY `index_pePrimaryKey` (`pePrimaryKey`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		sql += "INSERT INTO `transition` (`pePrimaryKey`, `tepNumber`, `primaryKey`) VALUES\n";

		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);

			WebPage startPage = trans.startPage;
			WebPage endPage = trans.endPage;
			PageElement triggerPe = trans.triggerPe;

			int transKey = trans.transKey;
			
			sql +=	"(" +
					triggerPe.pePrimaryKey + ", " +
					endPage.getWebPageNumber() + ", " +
					transKey + 
					"),\n";
		}
		
		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";

		return sql;
	}
}

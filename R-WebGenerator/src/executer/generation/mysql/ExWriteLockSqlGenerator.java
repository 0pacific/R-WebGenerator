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
import tpp.service.Service;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
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





public class ExWriteLockSqlGenerator {
	/*
	 * 全ページエレメント、全遷移、全TPPに主キーがあたえてあることが前提（TPPの主キーとはTPPN）
	 */
	public String getSql() {
		String sql = "";

		// テーブル"exwrite_lock"のSQLを作成
		sql +=	"CREATE TABLE IF NOT EXISTS `exwrite_lock` (\n" +
				"`tblNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`recordPrimKey` bigint(20) NOT NULL DEFAULT '0',\n" +
				"`offset` int(11) NOT NULL DEFAULT '0',\n" +
				"`roleNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`userNumber` int(11) NOT NULL DEFAULT '0' COMMENT '非ゲストロールなので必ず持つはず'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
		
		return sql;
	}
}

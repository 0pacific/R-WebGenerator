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





public class IaAssignSqlGenerator {
	public String getSql() {
		String sql = "";

		// ÉeÅ[ÉuÉã"ia_assign"ÇçÏê¨Ç∑ÇÈSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `ia_assign` (\n" +
				"`tableNumber` tinyint(4) NOT NULL default '0',\n" +
				"`recordPrk` tinyint(4) NOT NULL default '0',\n" +
				"`roleNumber` tinyint(4) NOT NULL default '0',\n" +
				"`userNumber` int(11) NOT NULL default '0',\n" +
				"PRIMARY KEY  (`tableNumber`,`recordPrk`,`roleNumber`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";
		
		return sql;
	}
}

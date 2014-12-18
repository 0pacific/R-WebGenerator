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
	 * �S�y�[�W�G�������g�A�S�J�ڂɎ�L�[���������Ă��邱�Ƃ��O��
	 */
	public String getSql() {
		String sql = "";
		TransitionManager transManager = TransitionManager.getInstance();

		// �e�[�u��"transition"��SQL���쐬
		sql +=	"CREATE TABLE `transition` (\n" +
				"`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0' COMMENT '�J�ڌ��y�[�W�̃y�[�W�G�������g�̎�L�[',\n" +
				"`tepNumber` int(11) NOT NULL DEFAULT '0' COMMENT '�J�ڐ�y�[�W�̔ԍ�',\n" +
				"`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '�J�ڃv���Z�X�̎�L�[',\n" +
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
		
		// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";

		return sql;
	}
}

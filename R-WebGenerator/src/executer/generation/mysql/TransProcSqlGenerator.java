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





public class TransProcSqlGenerator {
	/*
	 * �S�y�[�W�G�������g�A�S�J�ځA�STPP�Ɏ�L�[���������Ă��邱�Ƃ��O��iTPP�̎�L�[�Ƃ�TPPN�j
	 */
	public String getSql() {
		String sql = "";
		TransitionManager transManager = TransitionManager.getInstance();

		// �e�[�u��"tpp"��SQL���쐬
		sql +=	"CREATE TABLE `tpp` (\n" +
				"`transitionPrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT '�J�ڃv���Z�X�̎�L�[',\n" +
				"`tppn` int(11) NOT NULL AUTO_INCREMENT,\n" +
				"`tppKind` enum('DDT Load','Create Form Reflection','Create Reflection','Update Reflection','Delete Reflection','Update Form Reflection','Delete Form Reflection','IA Reflection','Service Execution','Display Area Input','Const Array Int','Const Array String') NOT NULL DEFAULT 'DDT Load',\n" +
				"PRIMARY KEY (`tppn`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";
		
		String insertSql = "INSERT INTO `tpp` (`transitionPrimaryKey`, `tppn`, `tppKind`) VALUES\n";

		// �S�J�ڃv���Z�X��TPP�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalTppNum = 0;
		
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			int transKey = trans.transKey;

		
			TransitionProcess transProc = trans.transProc;
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// �܂��J�ڂ̎�L�[
				insertSql += "(" + transKey + ", ";

				// TPPN
				insertSql += tpp.tppn + ", ";
				
				// TPP�̎��
				insertSql += "'";
				if(tpp instanceof TppTableReading) {
					insertSql += "DDT Load";
				}
				else if(tpp instanceof TppConstArrayInt || tpp instanceof TppConstInt) {
					insertSql += "Const Array Int";
				}
				else if(tpp instanceof TppConstArrayString || tpp instanceof TppConstString) {
					insertSql += "Const Array String";
				}
				else if(tpp instanceof Service) {
					insertSql += "Service Execution";
				}
				else if(tpp instanceof TppCreateFormReflection) {
					insertSql += "Create Form Reflection";
				}
				else if(tpp instanceof TppUpdateFormReflection) {
					insertSql += "Update Form Reflection";
				}
				else if(tpp instanceof TppCreateReflection) {
					insertSql += "Create Reflection";
				}
				else if(tpp instanceof TppUpdateReflection) {
					insertSql += "Update Reflection";
				}
				else if(tpp instanceof TppDeleteReflection) {
					insertSql += "Delete Reflection";
				}
				else if(tpp instanceof TppDeleteFormReflection) {
					insertSql += "Delete Form Reflection";
				}
				else if(tpp instanceof TppIAReflection) {
					insertSql += "IA Reflection";
				}
				else {
					Debug.error("tpp�e�[�u����SQL�쐬���A�z��O��TPP��������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					return null;
				}
				insertSql += "'),\n";
			}

			totalTppNum += transProc.getTppNum();
		}
		// �����S�J�ڃv���Z�X���̂ǂ��ɂ�TPP���Ȃ��i�܂�����Ǝv�����j�Ȃ�AINSERT���͂Ȃ�
		if(totalTppNum==0) {
			insertSql = "";
		}
		// �P�ł�����Ȃ�E�E�E
		else {
			// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		

		// INSERT����t�����ĕԋp
		sql += insertSql;
		return sql;
	}
}

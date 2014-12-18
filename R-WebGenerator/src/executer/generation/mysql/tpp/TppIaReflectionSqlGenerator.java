package executer.generation.mysql.tpp;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import transition.*;
import utility.*;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;






public class TppIaReflectionSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_ia_reflection` (\n" +
				"`tppn` int(11) NOT NULL DEFAULT '0',\n" +
				"`ddtNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`ddtTfdTppn` int(11) NOT NULL DEFAULT '0',\n" +
				"`atNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`atTfdTppn` int(11) NOT NULL DEFAULT '0',\n" +
				"PRIMARY KEY (`tppn`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();
		

		// �S�J�ڃv���Z�X���́A�Y������TPP�̑����𐔂��Ă����i0�̂Ƃ���INSERT���̕������������߁j
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_ia_reflection` (`tppn`, `ddtNumber`, `ddtTfdTppn`, `atNumber`, `atTfdTppn`) VALUES\n";

		// ���݂���S�J�ڃv���Z�X�ɂ��ď���
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// ���̑J�ڃv���Z�X�̊eTPP�ɂ��ď���
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// IA Reflection ��������
				if(tpp instanceof TppIAReflection) {
					TppIAReflection iaReflection = (TppIAReflection)tpp;

					// ����IA Reflection�̃f�[�^�e�[�u���pTFD�Ƃ��ēn�����ATFD�o�͌^TPP���擾
					TfdOutputer tfdOutputerAsDtTfd = iaReflection.getTfdOutputerAsDataTableTfdIfExists();
					if(tfdOutputerAsDtTfd==null) {
						Debug.notice("���͂���f�[�^�e�[�u���pTFD������`��IA���t���N�V���������݂���悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
						JOptionPane.showMessageDialog(MainFrame.getInstance(), "���͂���f�[�^�e�[�u���\�f�[�^������`�ȁu�l�����A�T�C�������v�����݂���悤�ł��B�m�F���ĉ������B");
						return null;
					}
					
					// ����IA Reflection�̃A�J�E���g�e�[�u���pTFD�Ƃ��ēn�����ATFD�o�͌^TPP���擾
					TfdOutputer tfdOutputerAsAtTfd = iaReflection.getTfdOutputerAsAccountTableTfdIfExists();
					if(tfdOutputerAsAtTfd==null) {
						Debug.notice("���͂���A�J�E���g�e�[�u���pTFD������`��IA���t���N�V���������݂���悤�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
						JOptionPane.showMessageDialog(MainFrame.getInstance(), "���͂���A�J�E���g�e�[�u���\�f�[�^������`�ȁu�l�����A�T�C�������v�����݂���悤�ł��B�m�F���ĉ������B");
						return null;
					}

					// ����IA Reflection�̃��R�[�h���쐬����
					insertSql +=	"(" +
							Integer.toString(iaReflection.tppn) + ", " +
							Integer.toString(iaReflection.dataTable.getTableNumber()) + ", " +
							Integer.toString(((TransitionProcessPart)tfdOutputerAsDtTfd).tppn) + ", " +
							Integer.toString(iaReflection.accountTable.getTableNumber()) + ", " +
							Integer.toString(((TransitionProcessPart)tfdOutputerAsAtTfd).tppn) +
 							"),\n";

					totalThisKindTppNum++;
				}
			}
		}
		// �����S�J�ڃv���Z�X���̂ǂ��ɂ����̎��TPP���Ȃ��Ȃ�AINSERT���͂Ȃ�
		if(totalThisKindTppNum==0) {
			insertSql = "";
		}
		// �P�ł�����Ȃ�E�E�E
		else {
			// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		
		return sql + insertSql;
	}
}

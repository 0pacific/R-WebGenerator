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
	 * 全ページエレメント、全遷移、全TPPに主キーがあたえてあることが前提（TPPの主キーとはTPPN）
	 */
	public String getSql() {
		String sql = "";
		TransitionManager transManager = TransitionManager.getInstance();

		// テーブル"tpp"のSQLを作成
		sql +=	"CREATE TABLE `tpp` (\n" +
				"`transitionPrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT '遷移プロセスの主キー',\n" +
				"`tppn` int(11) NOT NULL AUTO_INCREMENT,\n" +
				"`tppKind` enum('DDT Load','Create Form Reflection','Create Reflection','Update Reflection','Delete Reflection','Update Form Reflection','Delete Form Reflection','IA Reflection','Service Execution','Display Area Input','Const Array Int','Const Array String') NOT NULL DEFAULT 'DDT Load',\n" +
				"PRIMARY KEY (`tppn`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";
		
		String insertSql = "INSERT INTO `tpp` (`transitionPrimaryKey`, `tppn`, `tppKind`) VALUES\n";

		// 全遷移プロセスのTPPの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalTppNum = 0;
		
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			int transKey = trans.transKey;

		
			TransitionProcess transProc = trans.transProc;
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// まず遷移の主キー
				insertSql += "(" + transKey + ", ";

				// TPPN
				insertSql += tpp.tppn + ", ";
				
				// TPPの種類
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
					Debug.error("tppテーブルのSQL作成中、想定外のTPPが見つかりました", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					return null;
				}
				insertSql += "'),\n";
			}

			totalTppNum += transProc.getTppNum();
		}
		// もし全遷移プロセス中のどこにもTPPがない（まずあると思うが）なら、INSERT文はなし
		if(totalTppNum==0) {
			insertSql = "";
		}
		// １つでもあるなら・・・
		else {
			// 最後の",\n"をカットし、";\n"とする
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		

		// INSERT文を付加して返却
		sql += insertSql;
		return sql;
	}
}

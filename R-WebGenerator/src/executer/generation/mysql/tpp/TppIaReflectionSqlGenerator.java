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
		

		// 全遷移プロセス中の、該当するTPPの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_ia_reflection` (`tppn`, `ddtNumber`, `ddtTfdTppn`, `atNumber`, `atTfdTppn`) VALUES\n";

		// 存在する全遷移プロセスについて処理
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// この遷移プロセスの各TPPについて処理
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// IA Reflection だけ着目
				if(tpp instanceof TppIAReflection) {
					TppIAReflection iaReflection = (TppIAReflection)tpp;

					// このIA Reflectionのデータテーブル用TFDとして渡される、TFD出力型TPPを取得
					TfdOutputer tfdOutputerAsDtTfd = iaReflection.getTfdOutputerAsDataTableTfdIfExists();
					if(tfdOutputerAsDtTfd==null) {
						Debug.notice("入力するデータテーブル用TFDが未定義なIAリフレクションが存在するようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
						JOptionPane.showMessageDialog(MainFrame.getInstance(), "入力するデータテーブル表データが未定義な「個人権限アサイン処理」が存在するようです。確認して下さい。");
						return null;
					}
					
					// このIA Reflectionのアカウントテーブル用TFDとして渡される、TFD出力型TPPを取得
					TfdOutputer tfdOutputerAsAtTfd = iaReflection.getTfdOutputerAsAccountTableTfdIfExists();
					if(tfdOutputerAsAtTfd==null) {
						Debug.notice("入力するアカウントテーブル用TFDが未定義なIAリフレクションが存在するようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
						JOptionPane.showMessageDialog(MainFrame.getInstance(), "入力するアカウントテーブル表データが未定義な「個人権限アサイン処理」が存在するようです。確認して下さい。");
						return null;
					}

					// このIA Reflectionのレコードを作成する
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
		// もし全遷移プロセス中のどこにもこの種のTPPがないなら、INSERT文はなし
		if(totalThisKindTppNum==0) {
			insertSql = "";
		}
		// １つでもあるなら・・・
		else {
			// 最後の",\n"をカットし、";\n"とする
			insertSql = insertSql.substring(0, insertSql.length()-2);
			insertSql += ";\n";
		}
		
		return sql + insertSql;
	}
}

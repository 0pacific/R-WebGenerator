package executer.generation.mysql.tpp;

import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppTableReading;
import tpp.TppUpdateFormReflection;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppUpdateFormReflectionSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_update_form_reflection` (\n" +
				"`tppn` bigint(20) NOT NULL DEFAULT '0' COMMENT 'TPPの主キー',\n" +
				"`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ページエレメント「Update Form」の主キー',\n" +
				"PRIMARY KEY (`tppn`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();
		

		// 全遷移プロセス中の、該当するTPPの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_update_form_reflection` (`tppn`, `pePrimaryKey`) VALUES\n";

		// 存在する全遷移プロセスについて処理
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// この遷移プロセスの各TPPについて処理
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// Update Form Reflection だけ着目
				if(tpp instanceof TppUpdateFormReflection) {
					TppUpdateFormReflection updateFormReflection = (TppUpdateFormReflection)tpp;
					
					// このUpdate Form Reflectionのレコードを作成する
					insertSql +=	"(" +
							Integer.toString(updateFormReflection.tppn) + ", " +
							Integer.toString(updateFormReflection.updateForm.pePrimaryKey) +
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

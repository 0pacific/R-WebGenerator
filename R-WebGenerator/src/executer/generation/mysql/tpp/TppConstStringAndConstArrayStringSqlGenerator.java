package executer.generation.mysql.tpp;

import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppConstArrayInt;
import tpp.TppConstArrayString;
import tpp.TppConstInt;
import tpp.TppConstString;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppConstStringAndConstArrayStringSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_constant_array_string` (\n" +
				"`tppn` int(11) NOT NULL DEFAULT '0',\n" +
				"`elementNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`value` varchar(255) NOT NULL DEFAULT ''\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();

		// 全遷移プロセス中の、該当するTPPの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_constant_array_string` (`tppn`, `elementNumber`, `value`) VALUES\n";

		// 存在する全ての遷移プロセスについて処理
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// この遷移プロセスの各TPPについて処理
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// STRINGモノ定数およびSTRING定数配列 だけ着目
				if(tpp instanceof TppConstString || tpp instanceof TppConstArrayString) {
					if(tpp instanceof TppConstString) {
						TppConstString constString = (TppConstString)tpp;

						// このSTRINGモノ定数のレコードを作成する（要素数１の配列として見る）
						insertSql +=	"(" +
										Integer.toString(constString.tppn) + ", " +
										Integer.toString(0) + ", " +
										"'" + constString.value + "'" +
										"),\n";
					}
					else if(tpp instanceof TppConstArrayString) {
						TppConstArrayString constArrayString = (TppConstArrayString)tpp;

						// STRING定数配列の各要素について処理
						for(int k=0; k<constArrayString.getStringNum(); k++) {
							String stringElement = constArrayString.getStringValue(k);

							// この要素のレコードを作成する
							insertSql +=	"(" +
											Integer.toString(constArrayString.tppn) + ", " +
											Integer.toString(k) + ", " +
											"'" + stringElement + "'" +
											"),\n";
						}
					}

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

package executer.generation.mysql.tpp;

import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppConstArrayInt;
import tpp.TppConstInt;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppConstIntAndConstArrayIntSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_constant_array_int` (" +
				"`tppn` int(11) NOT NULL DEFAULT '0'," +
				"`elementNumber` int(11) NOT NULL DEFAULT '0'," +
				"`value` int(11) NOT NULL DEFAULT '0'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();

		// 全遷移プロセス中の、該当するTPPの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_constant_array_int` (`tppn`, `elementNumber`, `value`) VALUES\n";

		// 存在する全ての遷移プロセスについて処理
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// この遷移プロセスの各TPPについて処理
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// INTモノ定数およびINT定数配列 だけ着目
				if(tpp instanceof TppConstInt || tpp instanceof TppConstArrayInt) {
					if(tpp instanceof TppConstInt) {
						TppConstInt constInt = (TppConstInt)tpp;

						// このINTモノ定数のレコードを作成する（要素数１の配列として見る）
						insertSql +=	"(" +
										Integer.toString(constInt.tppn) + ", " +
										Integer.toString(0) + ", " +
										Integer.toString(constInt.value) +
										"),\n";
					}
					else if(tpp instanceof TppConstArrayInt) {
						TppConstArrayInt constArrayInt = (TppConstArrayInt)tpp;

						// INT定数配列の各要素について処理
						for(int k=0; k<constArrayInt.getIntNum(); k++) {
							int intElement = constArrayInt.getIntValue(k);

							// この要素のレコードを作成する
							insertSql +=	"(" +
											Integer.toString(constArrayInt.tppn) + ", " +
											Integer.toString(k) + ", " +
											Integer.toString(intElement) +
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

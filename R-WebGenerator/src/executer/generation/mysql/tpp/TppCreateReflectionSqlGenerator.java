package executer.generation.mysql.tpp;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;
import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TfdOutputer;
import tpp.TppCreateFormReflection;
import tpp.TppCreateReflection;
import tpp.TppTableReading;
import tpp.TppUpdateFormReflection;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppCreateReflectionSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_create_reflection` (\n" +
				"`tppn` int(11) NOT NULL DEFAULT '0',\n" +
				"`tableNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`tfdTppn` int(11) NOT NULL DEFAULT '0',\n" +
				"PRIMARY KEY (`tppn`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		TransitionManager transManager = TransitionManager.getInstance();
		

		// 全遷移プロセス中の、該当するTPPの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_create_reflection` (`tppn`, `tableNumber`, `tfdTppn`) VALUES\n";

		// 存在する全遷移プロセスについて処理
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// この遷移プロセスの各TPPについて処理
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// Create Reflection だけ着目
				if(tpp instanceof TppCreateReflection) {
					TppCreateReflection createReflection = (TppCreateReflection)tpp;

					// このCreateReflectionに渡される、TFD出力型TPPを取得
					TfdOutputer tfdOutputer = createReflection.getInputTfdOutputerIfExists();
					if(tfdOutputer==null) {
						Debug.notice("入力するTFDが未定義なCreateリフレクションが存在するようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
						JOptionPane.showMessageDialog(MainFrame.getInstance(), "入力する表データが未定義な「レコード作成処理」が存在するようです。確認して下さい。");
						return null;
					}
					
					// このCreate Reflectionのレコードを作成する
					insertSql +=	"(" +
							Integer.toString(createReflection.tppn) + ", " +
							Integer.toString(createReflection.table.getTableNumber()) + ", " +
							Integer.toString(((TransitionProcessPart)tfdOutputer).tppn) +
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

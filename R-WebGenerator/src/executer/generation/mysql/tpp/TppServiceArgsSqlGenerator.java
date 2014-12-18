package executer.generation.mysql.tpp;

import debug.Debug;
import pageElement.PageElement;
import pageElement.PageElementTableDisplay;
import tpp.TppConstArrayInt;
import tpp.TppTableReading;
import tpp.TransitionProcessPart;
import tpp.service.Service;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;
import webPage.WebPageManager;

public class TppServiceArgsSqlGenerator {
	public String getSql() {
		String sql = "";
		
		// CREATE TABLE
		sql +=	"CREATE TABLE IF NOT EXISTS `tpp_service_execution_argument` (`tppn` int(11) NOT NULL DEFAULT '0' COMMENT 'Service実行TPPの主キー',`argNumber` int(11) NOT NULL DEFAULT '0',`argTppn` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n";

		TransitionManager transManager = TransitionManager.getInstance();

		// 全遷移プロセス中の、該当するTPPの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindTppNum = 0;

		String insertSql = "INSERT INTO `tpp_service_execution_argument` (`tppn`, `argNumber`, `argTppn`) VALUES\n";

		// 存在する全ての遷移プロセスについて処理
		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// この遷移プロセスの各TPPについて処理
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);

				// サービス実行 だけ着目
				if(tpp instanceof Service) {
					Service service = (Service)tpp;
					
					// サービスの各引数について処理
					for(int k=0; k<service.getArgNum(); k++) {
						TransitionProcessPart argTpp = service.getArgTppIfExists(k);
						if(argTpp==null) {
							Debug.error("サービス「" + service.serviceName + "」の第" + k + "引数（0始まり）が未定義です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
							return null;
						}
						int argTppn = argTpp.tppn;
						
						insertSql +=	"(" +
										Integer.toString(service.tppn) + ", " +
										Integer.toString(k) + ", " +
										Integer.toString(argTppn) +
										"),\n";
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

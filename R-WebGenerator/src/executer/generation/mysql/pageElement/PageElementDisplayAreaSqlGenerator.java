package executer.generation.mysql.pageElement;

import java.util.ArrayList;

import debug.Debug;

import pageElement.PageElement;
import pageElement.PageElementDisplayArea;
import pageElement.PageElement_HyperLink;
import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementDisplayAreaSqlGenerator {
	/*
	 * SUMMARY :
	 * テーブル"page_element_da"を作成するSQLを作成、返却
	 */
	public String getSql() {
		String sql = "";

		// page_element_daテーブルを作るSQL
		sql +=	"CREATE TABLE `page_element_da` (" +
				"`pePrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT 'page_elementテーブルの主キー'," +
				"`transitionPrimaryKey` int(11) NOT NULL COMMENT '遷移の主キー（どの遷移プロセスを通った場合か）'," +
				"`tppn` int(11) NOT NULL DEFAULT '0'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManagerインスタンス
		WebPageManager wpm = WebPageManager.getInstance();

		// 全Webページ中の、該当するページエレメントの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_da` (`pePrimaryKey`, `transitionPrimaryKey`, `tppn`) VALUES\n";

		// 存在する全てのWebページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// このWebページの各ページエレメントについて処理
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// Display Areaのページエレメントだけ着目
				if(pe instanceof PageElementDisplayArea) {
					PageElementDisplayArea displayArea = (PageElementDisplayArea)pe;

					// Display Areaが置かれるページへ向かう遷移をすべて洗い出す
					WebPage parentPage = displayArea.parentPage;
					ArrayList<Transition> comingTransArray = TransitionManager.getInstance().getTransArrayByEndPage(parentPage);
					
					// その全ての遷移（の遷移プロセス）について、Display Areaに結果表示すべきTPPのTPPNを調べ、１レコードを作る
					for(int k=0; k<comingTransArray.size(); k++) {
						// この遷移の主キー
						Transition trans = comingTransArray.get(k);
						int transKey = trans.transKey;

						// この遷移の遷移プロセスを通った際にDisplay Areaが結果表示するTPPのTPPNを取得
						TransitionProcessPart inputTpp = displayArea.getInputTppIfExists(trans.transProc);
						if(inputTpp==null) {
							Debug.error("SQLの生成に失敗しました。入力の定義されていないDisplay Areaがあるようです。どの遷移プロセスにおいて入力されてないのかは割愛", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
							return null;
						}
						int inputTppn = inputTpp.tppn;
						
						insertSql +=	"(" +
								Integer.toString(displayArea.pePrimaryKey) + ", " +
								Integer.toString(transKey) + ", " +
								Integer.toString(inputTppn) +
								"),\n";
					}

					totalThisKindPeNum++;
				}
				
			}
		}
		// もし全Webページ中のどこにもこの種のページエレメントがないなら、INSERT文はなし
		if(totalThisKindPeNum==0) {
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

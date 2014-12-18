package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import pageElement.PageElementText;
import pageElement.PageElement_HyperLink;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementTextSqlGenerator {
	/*
	 * SUMMARY :
	 * テーブル"page_element_text"を作成するSQLを作成、返却
	 */
	public String getSql() {
		String sql = "";

		// page_element_textテーブルを作るSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `page_element_text` (" +
				"`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0'," +
				"`text` text NOT NULL" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManagerインスタンス
		WebPageManager wpm = WebPageManager.getInstance();

		// 全Webページ中の、該当するページエレメントの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_text` (`pePrimaryKey`, `text`) VALUES\n";

		// 存在する全てのWebページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// このWebページの各ページエレメントについて処理
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// テキストページエレメントだけ着目
				if(pe instanceof PageElementText) {
					PageElementText textPe = (PageElementText)pe;
					
					// このテキストページエレメントのレコードを作成する
					insertSql +=	"(" +
							Integer.toString(textPe.pePrimaryKey) + ", " +
							"'" + textPe.containingText + "'" +
							"),\n";

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

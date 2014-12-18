package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import pageElement.PageElementLoginForm;
import pageElement.PageElement_HyperLink;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementLoginFormSqlGenerator {
	/*
	 * SUMMARY :
	 * テーブル"page_element_login_form"を作成するSQLを作成、返却
	 */
	public String getSql() {
		String sql = "";

		// page_element_login_formテーブルを作るSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `page_element_login_form` (" +
				"`pePrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT 'page_elementテーブルの主キー'," +
				"`accountTableNumber` int(11) NOT NULL DEFAULT '0' COMMENT '何番のアカウントテーブルのログインフォームか'," +
				"`destPageNumber` int(11) NOT NULL DEFAULT '0' COMMENT 'ログイン成功時に行くページの番号'" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManagerインスタンス
		WebPageManager wpm = WebPageManager.getInstance();

		// 全Webページ中の、該当するページエレメントの総数を数えておく（0のときにINSERT文の部分を消すため）
		int totalThisKindPeNum = 0;

		String insertSql = "INSERT INTO `page_element_login_form` (`pePrimaryKey`, `accountTableNumber`, `destPageNumber`) VALUES\n";

		// 存在する全てのWebページについて処理
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);

			// このWebページの各ページエレメントについて処理
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);

				// ログインフォームのページエレメントだけ着目
				if(pe instanceof PageElementLoginForm) {
					PageElementLoginForm loginForm = (PageElementLoginForm)pe;
					
					// このログインフォームのレコードを作成する
					insertSql +=	"(" +
							Integer.toString(loginForm.pePrimaryKey) + ", " +
							Integer.toString(loginForm.accountTable.getTableNumber()) + ", " +
							Integer.toString(loginForm.destWebPage.getWebPageNumber()) +
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

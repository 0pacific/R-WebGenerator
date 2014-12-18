package executer.generation.mysql;

import webPage.WebPage;
import webPage.WebPageManager;

public class PageSqlCreator {
	public PageSqlCreator() {
	}

	
	
	
	
	/*
	 * SUMMARY :
	 * �e�[�u��"pages"���쐬����SQL���쐬�A�ԋp
	 */
	public String getSql() {
		String sql = "";

		// pages�e�[�u�������SQL
		sql += "CREATE TABLE `pages` (\n"
				+ "`pageNumber` int(11) NOT NULL DEFAULT '0',\n"
				+ "`fileName` varchar(255) NOT NULL DEFAULT '',\n"
				+ "PRIMARY KEY (`pageNumber`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"
				+ "\n";
				
		// WebPageManager�C���X�^���X
		WebPageManager wpm = WebPageManager.getInstance();

		// ���݂���S�Ă�Web�y�[�W�ɂ��ď���
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);
			int pageNumber = webPage.getWebPageNumber();
			String pageFileName = webPage.pageFileName;

			// ����Web�y�[�W�̔ԍ��ƃt�@�C�������L�ڂ������R�[�h�@���쐬����INSERT�������A�t������
			String sql_i = "insert into pages (pageNumber, fileName) values("
							+ pageNumber  + ","
							+ "'" + pageFileName + "'"
							+ ");\n";
			sql += sql_i;
		}

		return sql;
	}
}

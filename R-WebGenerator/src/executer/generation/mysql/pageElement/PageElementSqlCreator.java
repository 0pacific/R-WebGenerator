package executer.generation.mysql.pageElement;

import pageElement.PageElement;
import webPage.WebPage;
import webPage.WebPageManager;

public class PageElementSqlCreator {
	/*
	 * SUMMARY :
	 * �e�[�u��"page_element"���쐬����SQL���쐬�A�ԋp
	 */
	public String getSql() {
		String sql = "";

		// page_element�e�[�u�������SQL
		sql +=	"CREATE TABLE `page_element` (\n" +
				"`pageNumber` tinyint(4) NOT NULL default '0',\n" +
				"`peNumber` tinyint(4) NOT NULL default '0',\n" +
				"`peKind` enum('Login Form','Table Display','Create Form','Update Form','Display Area','Service Argument Input Form','IA Assignment Form','Text','Hyper Link') NOT NULL default 'Display Area',\n" +
				"`primaryKey` int(11) NOT NULL default '0' COMMENT 'Page Element �̎�L�[',\n" +
				"UNIQUE KEY `page_pe` (`pageNumber`,`peNumber`),\n" +
				"UNIQUE KEY `primaryKey` (`primaryKey`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		// WebPageManager�C���X�^���X
		WebPageManager wpm = WebPageManager.getInstance();

		sql +=	"INSERT INTO `page_element` (`pageNumber`, `peNumber`, `peKind`, `primaryKey`) VALUES\n";

		// ���݂���S�Ă�Web�y�[�W�ɂ��ď���
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);
			int pageNumber = webPage.getWebPageNumber();

			// ����Web�y�[�W�̊e�y�[�W�G�������g�ɂ��ď���
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);
				
				// ���̃y�[�W�G�������g�̃��R�[�h���쐬����
				sql +=	"(" +
						Integer.toString(pageNumber) + ", " +
						Integer.toString(j) + ", " +
						"'" + PageElement.givePeExpression(pe) + "', " +
						Integer.toString(pe.pePrimaryKey) +
						"),\n";
			}
		}

		// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}
}

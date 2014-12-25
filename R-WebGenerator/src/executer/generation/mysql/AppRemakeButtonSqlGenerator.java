package executer.generation.mysql;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import role.GuestRole;
import role.Role;
import role.RoleManager;
import webPage.WebPage;
import webPage.WebPageManager;





public class AppRemakeButtonSqlGenerator {//【追加】
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `application_remake_button` (\n" +
				"`pageNumber` int(11) NOT NULL default '0'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		WebPageManager webPageManager = WebPageManager.getInstance();
		WebPage ARButtonPage = webPageManager.getARButtonPage();
		if(ARButtonPage==null) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "アプリ再編集ボタンが配置されていません。確認して下さい。");
			return null;
		}
		sql +=	"INSERT INTO `application_remake_button` (`pageNumber`) VALUES (" + ARButtonPage.getWebPageNumber() + ");\n";

		return sql;
	}
}

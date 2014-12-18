package executer.generation.mysql;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import role.GuestRole;
import role.Role;
import role.RoleManager;
import webPage.WebPage;
import webPage.WebPageManager;





public class LogoutDestPageSqlGenerator {
	public String getSql() {
		String sql = "";
		
		sql +=	"CREATE TABLE IF NOT EXISTS `logout_trans` (\n" +
				"`destPageNumber` int(11) NOT NULL default '0'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		WebPageManager webPageManager = WebPageManager.getInstance();
		WebPage logoutDestPage = webPageManager.getLogoutDestPage();
		if(logoutDestPage==null) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "���O�A�E�g���̑J�ڐ悪����`�̂悤�ł��B�m�F���ĉ������B");
			return null;
		}
		sql +=	"INSERT INTO `logout_trans` (`destPageNumber`) VALUES (" + logoutDestPage.getWebPageNumber() + ");\n";

		return sql;
	}
}

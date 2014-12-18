package executer.generation.mysql;

import role.GuestRole;
import role.Role;
import role.RoleManager;





public class GeneralRoleSqlGenerator {
	public String getSql() {
		String sql =	"-- general_role --\n" +
						"\n";
		
		sql +=	"CREATE TABLE `general_role` (\n" +
						"`roleNumber` tinyint(4) NOT NULL DEFAULT '0'\n" +
						") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
						"\n";

		RoleManager roleManager = RoleManager.getInstance();
		for(int i=0; i<roleManager.getRoleNum(); i++) {
			Role role = roleManager.getRole(i);
			if(role instanceof GuestRole) {
				sql +=	"INSERT INTO `general_role` (`roleNumber`) VALUES (" + Integer.toString(role.getRoleNumber()) + ");\n";
			}
		}

		return sql;
	}
}

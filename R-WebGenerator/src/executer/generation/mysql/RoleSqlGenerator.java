package executer.generation.mysql;

import debug.Debug;
import role.NormalRole;
import role.Role;
import role.RoleManager;
import role.GuestRole;
import table.AccountTable;

public class RoleSqlGenerator {
	public RoleSqlGenerator() {
	}

	
	
	
	
	public String getSql() {
		String sql = "";

		// テーブル"role"を作成するSQL
		sql +=	"CREATE TABLE `role` (\n" +
				"`number` tinyint(4) NOT NULL DEFAULT '0',\n" +
				"`name` varchar(32) NOT NULL DEFAULT '',\n" +
				"`at_number` tinyint(4) DEFAULT '0' COMMENT '対応するアカウントテーブルの番号'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		sql +=	"INSERT INTO `role` (`number`, `name`, `at_number`) VALUES\n";

		RoleManager roleManager = RoleManager.getInstance();
		for(int i=0; i<roleManager.getRoleNum(); i++) {
			Role role = roleManager.getRole(i);
			sql += "(" + Integer.toString(role.getRoleNumber()) + ", '" + role.getRoleName() + "', ";
			
			// 非ゲストロールなら、アカウントテーブルの番号を調べて記入する（ゲストロールはNULL）
			if(role instanceof NormalRole) {
				// NormalRoleにキャスト
				NormalRole normalRole = (NormalRole)role;
				
				//　エラーチェック
				if(!normalRole.accountTableDefined()) {
					Debug.error("ロール「" + normalRole.getRoleName() + "」のアカウントテーブルが定義されていません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					return null;
				}

				AccountTable at = normalRole.getAccountTable();
				sql += Integer.toString(at.getTableNumber());
			}
			else if(role instanceof GuestRole) {
				sql += "NULL";
			}
			else {
				Debug.error("ロールを表すインスタンスの型が想定外です", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return null;
			}

			sql += "),\n";
		}

		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";

		return sql;
	}
}

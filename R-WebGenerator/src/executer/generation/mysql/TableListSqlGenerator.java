package executer.generation.mysql;

import role.GuestRole;
import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.AccountTable;
import table.DataTable;
import table.TableManager;
import debug.Debug;

public class TableListSqlGenerator {
	public String getSql() {
		String sql = "";

		// テーブル"table_list"を作成するSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `table_list` (\n" +
				"`primaryKey` tinyint(4) NOT NULL DEFAULT '0',\n" +
				"`type` enum('data table','account table') NOT NULL DEFAULT 'data table',\n" +
				"`name` varchar(32) NOT NULL DEFAULT '',\n" +
				"`nameOnWeb` varchar(32) NOT NULL DEFAULT '',\n" +
				"PRIMARY KEY (`primaryKey`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		sql +=	"INSERT INTO `table_list` (`primaryKey`, `type`, `name`, `nameOnWeb`) VALUES\n";

		TableManager tableManager = TableManager.getInstance();
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int tableNumber = dt.getTableNumber();

			sql +=	"(" +
					Integer.toString(tableNumber) + ", " +
					"'data table', " +
					"'dt" + Integer.toString(tableNumber) + "', " +
					"'" + dt.getTableName() + "'" +
					"),\n";
		}
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			int tableNumber = at.getTableNumber();

			sql +=	"(" +
					Integer.toString(tableNumber) + ", " +
					"'account table', " +
					"'at" + Integer.toString(tableNumber) + "', " +
					"'" + at.getTableName() + "'" +
					"),\n";
		}

		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}
}

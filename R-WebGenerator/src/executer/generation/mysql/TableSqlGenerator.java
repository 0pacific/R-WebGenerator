package executer.generation.mysql;

import role.GuestRole;
import role.NormalRole;
import role.Role;
import role.RoleManager;
import table.AccountTable;
import table.DataTable;
import table.TableManager;
import table.field.Field;
import debug.Debug;

public class TableSqlGenerator {
	public String getSql() {
		String sql = "";

		TableManager tableManager = TableManager.getInstance();

		// 各データテーブルについて処理
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int tableNumber = dt.getTableNumber();

			// このデータテーブルが既に存在していたら削除
			sql +=	"DROP TABLE IF EXISTS `dt" + Integer.toString(tableNumber) + "`;\n";
			
			// このデータテーブルを作成するSQL
			sql +=	"CREATE TABLE IF NOT EXISTS `dt" + Integer.toString(tableNumber) + "` (\n";

			// 各DDFについて処理
			for(int j=0; j<dt.getFieldNum(); j++) {
				Field field = dt.getField(j);
				
				sql +=	getDdfDescriptionPartOfCreateTableSql(field, j) + ",\n";	// DB上でのフィールド名、データタイプ、NULL許可、デフォルト値（データタイプによって異なってくる）
			}

			// 最後に主キーフィールドを付け足す
			sql += 	"`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
					"PRIMARY KEY (`primaryKey`)\n" +
					") ENGINE=InnoDB  DEFAULT CHARSET=utf8;\n" +
					"\n";
		}
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			int tableNumber = at.getTableNumber();

			// このアカウントテーブルが既に存在していたら削除
			sql +=	"DROP TABLE IF EXISTS `at" + Integer.toString(tableNumber) + "`;\n";

			// このアカウントテーブルを作成するSQL
			sql +=	"CREATE TABLE IF NOT EXISTS `at" + Integer.toString(tableNumber) + "` (\n";

			// 各DDFについて処理
			for(int j=0; j<at.getFieldNum(); j++) {
				Field field = at.getField(j);
				
				sql +=	getDdfDescriptionPartOfCreateTableSql(field, j) + ",\n";	// DB上でのフィールド名、データタイプ、NULL許可、デフォルト値（データタイプによって異なってくる）
			}

			// 最後に主キーフィールドと、◆アカウントオーナーロール番号を記録するフィールド◆を付け足す
			sql += 	"`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
					"`roleNumber` int(11) NOT NULL default '0',\n" +
					"PRIMARY KEY (`primaryKey`)\n" +
					") ENGINE=InnoDB  DEFAULT CHARSET=utf8;\n" +
					"\n";
		}

		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}




	public String getDdfDescriptionPartOfCreateTableSql(Field field, int offset) {
		String sql = "";
		
		sql += "`field" + Integer.toString(offset) + "` ";

		if(field.dataType.equals(Field.DATATYPE_INT)) {
			sql += "int(11) DEFAULT NULL";
		}
		else if(field.dataType.equals(Field.DATATYPE_VARCHAR)) {
			sql += "varchar(" + field.getMax() + ") NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_FILE)) {
			sql += "varchar(255) NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_DATETIME)) {
			sql += "varchar(255) NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_DATE)) {
			sql += "varchar(255) NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_TIME)) {
			sql += "varchar(255) NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_MAIL)) {
			sql += "varchar(255) NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_USERID)) {
			sql += "varchar(" + field.getMax() + ") NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_MAIL_AUTH)) {
			sql += "varchar(255) NOT NULL DEFAULT ''";
		}
		else if(field.dataType.equals(Field.DATATYPE_PASSWORD)) {
			sql += "blob NOT NULL";
		}
		else if(field.dataType.equals(Field.DATATYPE_ROLE_NAME)) {
			sql += "varchar(255) NOT NULL DEFAULT ''";
		}
		else {
			Debug.error("想定外のデータ型です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		return sql;
	}
}

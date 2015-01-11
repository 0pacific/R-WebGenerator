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

public class FieldInfoSqlGenerator {
	public String getSql() {
		String sql = "";

		// テーブル"field_info"を作成するSQL
		sql +=	"CREATE TABLE IF NOT EXISTS `field_info` (\n" +
				"`tblNumber` int(11) NOT NULL DEFAULT '0',\n" +
				"`offset` int(11) NOT NULL DEFAULT '0',\n" +
				"`developerDefined` enum('YES','NO') NOT NULL DEFAULT 'YES',\n" +
				"`dataType` enum('int','varchar','datetime','date','time','enum','file','mail','mail_auth','user_id','password','primaryKey','role_name','role_number') NOT NULL DEFAULT 'int',\n" +
				"`nameOnWeb` varchar(255) DEFAULT NULL,\n" +
				"`min` int(11) DEFAULT NULL,\n" +
				"`max` int(11) DEFAULT NULL\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		sql +=	"INSERT INTO `field_info` (`tblNumber`, `offset`, `developerDefined`, `dataType`, `nameOnWeb`, `min`, `max`) VALUES\n";

		TableManager tableManager = TableManager.getInstance();

		// 各データテーブルについて処理
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int tableNumber = dt.getTableNumber();

			// 各DDFについて処理
			for(int j=0; j<dt.getFieldNum(); j++) {
				Field field = dt.getField(j);
				
				//抽象フィールドは無視【追加】
				if(!field.isFixed) continue;
				
				sql +=	"(" +
				Integer.toString(tableNumber) + ", " +
				Integer.toString(j) + ", " +
				"'YES', " +
				"'" + field.dataType + "', " +		// Fieldクラスのstatic定数が入っているはず。このstatic定数は、filed_infoのdataTypeカラムに入るべき値で統一してあるので大丈夫（ENUMの場合別か？）
				"'" + field.getFieldName() + "', " +
				Integer.toString(field.getMin()) + ", " +
				Integer.toString(field.getMax()) +
				"),\n";
			}
			// RDF
			sql +=	"(" +
			Integer.toString(tableNumber) + ", " +
			Integer.toString(dt.getFieldNum()) + ", " +
			"'NO', " +
			"'" + Field.DATATYPE_PRIMARY_KEY + "', " +		// "primaryKey"
			"null, " +
			"null, " +
			"null" +
			"),\n";
		}

		// 各アカウントテーブルについて処理
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			int tableNumber = at.getTableNumber();

			// 各DDFについて処理
			for(int j=0; j<at.getFieldNum(); j++) {
				Field field = at.getField(j);
				
				//抽象フィールドは無視【追加】 おそらく不要だが・・・
				if(!field.isFixed) continue;
				
				sql +=	"(" +
				Integer.toString(tableNumber) + ", " +
				Integer.toString(j) + ", " +
				"'YES', " +
				"'" + field.dataType + "', " +		// Fieldクラスのstatic定数が入っているはず。このstatic定数は、filed_infoのdataTypeカラムに入るべき値で統一してあるので大丈夫（ENUMの場合別か？）
				"'" + field.getFieldName() + "', " +
				Integer.toString(field.getMin()) + ", " +
				Integer.toString(field.getMax()) +
				"),\n";
			}
			// 主キーフィールド（RDF）
			sql +=	"(" +
			Integer.toString(tableNumber) + ", " +
			Integer.toString(at.getFieldNum()) + ", " +
			"'NO', " +
			"'" + Field.DATATYPE_PRIMARY_KEY + "', " +		// "primaryKey"
			"null, " +
			"null, " +
			"null" +
			"),\n";

			// アカウントオーナーロール番号フィールド（RDF）
			sql +=	"(" +
			Integer.toString(tableNumber) + ", " +
			Integer.toString(at.getFieldNum()+1) + ", " +
			"'NO', " +
			"'" + Field.DATATYPE_ROLE_NUMBER + "', " +		// "roleNumber"
			"null, " +
			"null, " +
			"null" +
			"),\n";
		}

		// 最後の",\n"をカットし、";\n"とする
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}
}

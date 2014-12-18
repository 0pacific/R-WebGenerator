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

		// �e�f�[�^�e�[�u���ɂ��ď���
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int tableNumber = dt.getTableNumber();

			// ���̃f�[�^�e�[�u�������ɑ��݂��Ă�����폜
			sql +=	"DROP TABLE IF EXISTS `dt" + Integer.toString(tableNumber) + "`;\n";
			
			// ���̃f�[�^�e�[�u�����쐬����SQL
			sql +=	"CREATE TABLE IF NOT EXISTS `dt" + Integer.toString(tableNumber) + "` (\n";

			// �eDDF�ɂ��ď���
			for(int j=0; j<dt.getFieldNum(); j++) {
				Field field = dt.getField(j);
				
				sql +=	getDdfDescriptionPartOfCreateTableSql(field, j) + ",\n";	// DB��ł̃t�B�[���h���A�f�[�^�^�C�v�ANULL���A�f�t�H���g�l�i�f�[�^�^�C�v�ɂ���ĈقȂ��Ă���j
			}

			// �Ō�Ɏ�L�[�t�B�[���h��t������
			sql += 	"`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
					"PRIMARY KEY (`primaryKey`)\n" +
					") ENGINE=InnoDB  DEFAULT CHARSET=utf8;\n" +
					"\n";
		}
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			int tableNumber = at.getTableNumber();

			// ���̃A�J�E���g�e�[�u�������ɑ��݂��Ă�����폜
			sql +=	"DROP TABLE IF EXISTS `at" + Integer.toString(tableNumber) + "`;\n";

			// ���̃A�J�E���g�e�[�u�����쐬����SQL
			sql +=	"CREATE TABLE IF NOT EXISTS `at" + Integer.toString(tableNumber) + "` (\n";

			// �eDDF�ɂ��ď���
			for(int j=0; j<at.getFieldNum(); j++) {
				Field field = at.getField(j);
				
				sql +=	getDdfDescriptionPartOfCreateTableSql(field, j) + ",\n";	// DB��ł̃t�B�[���h���A�f�[�^�^�C�v�ANULL���A�f�t�H���g�l�i�f�[�^�^�C�v�ɂ���ĈقȂ��Ă���j
			}

			// �Ō�Ɏ�L�[�t�B�[���h�ƁA���A�J�E���g�I�[�i�[���[���ԍ����L�^����t�B�[���h����t������
			sql += 	"`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
					"`roleNumber` int(11) NOT NULL default '0',\n" +
					"PRIMARY KEY (`primaryKey`)\n" +
					") ENGINE=InnoDB  DEFAULT CHARSET=utf8;\n" +
					"\n";
		}

		// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
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
			Debug.error("�z��O�̃f�[�^�^�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}

		return sql;
	}
}

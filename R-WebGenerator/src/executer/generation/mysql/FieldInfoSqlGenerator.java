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

		// �e�[�u��"field_info"���쐬����SQL
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

		// �e�f�[�^�e�[�u���ɂ��ď���
		for(int i=0; i<tableManager.getDataTableNum(); i++) {
			DataTable dt = tableManager.getDataTable(i);
			int tableNumber = dt.getTableNumber();

			// �eDDF�ɂ��ď���
			for(int j=0; j<dt.getFieldNum(); j++) {
				Field field = dt.getField(j);
				
				//���ۃt�B�[���h�͖����y�ǉ��z
				if(!field.isFixed) continue;
				
				sql +=	"(" +
				Integer.toString(tableNumber) + ", " +
				Integer.toString(j) + ", " +
				"'YES', " +
				"'" + field.dataType + "', " +		// Field�N���X��static�萔�������Ă���͂��B����static�萔�́Afiled_info��dataType�J�����ɓ���ׂ��l�œ��ꂵ�Ă���̂ő��v�iENUM�̏ꍇ�ʂ��H�j
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

		// �e�A�J�E���g�e�[�u���ɂ��ď���
		for(int i=0; i<tableManager.getAccountTableNum(); i++) {
			AccountTable at = tableManager.getAccountTable(i);
			int tableNumber = at.getTableNumber();

			// �eDDF�ɂ��ď���
			for(int j=0; j<at.getFieldNum(); j++) {
				Field field = at.getField(j);
				
				//���ۃt�B�[���h�͖����y�ǉ��z �����炭�s�v�����E�E�E
				if(!field.isFixed) continue;
				
				sql +=	"(" +
				Integer.toString(tableNumber) + ", " +
				Integer.toString(j) + ", " +
				"'YES', " +
				"'" + field.dataType + "', " +		// Field�N���X��static�萔�������Ă���͂��B����static�萔�́Afiled_info��dataType�J�����ɓ���ׂ��l�œ��ꂵ�Ă���̂ő��v�iENUM�̏ꍇ�ʂ��H�j
				"'" + field.getFieldName() + "', " +
				Integer.toString(field.getMin()) + ", " +
				Integer.toString(field.getMax()) +
				"),\n";
			}
			// ��L�[�t�B�[���h�iRDF�j
			sql +=	"(" +
			Integer.toString(tableNumber) + ", " +
			Integer.toString(at.getFieldNum()) + ", " +
			"'NO', " +
			"'" + Field.DATATYPE_PRIMARY_KEY + "', " +		// "primaryKey"
			"null, " +
			"null, " +
			"null" +
			"),\n";

			// �A�J�E���g�I�[�i�[���[���ԍ��t�B�[���h�iRDF�j
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

		// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";
		
		return sql;
	}
}

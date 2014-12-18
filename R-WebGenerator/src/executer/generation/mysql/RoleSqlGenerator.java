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

		// �e�[�u��"role"���쐬����SQL
		sql +=	"CREATE TABLE `role` (\n" +
				"`number` tinyint(4) NOT NULL DEFAULT '0',\n" +
				"`name` varchar(32) NOT NULL DEFAULT '',\n" +
				"`at_number` tinyint(4) DEFAULT '0' COMMENT '�Ή�����A�J�E���g�e�[�u���̔ԍ�'\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
				"\n";

		sql +=	"INSERT INTO `role` (`number`, `name`, `at_number`) VALUES\n";

		RoleManager roleManager = RoleManager.getInstance();
		for(int i=0; i<roleManager.getRoleNum(); i++) {
			Role role = roleManager.getRole(i);
			sql += "(" + Integer.toString(role.getRoleNumber()) + ", '" + role.getRoleName() + "', ";
			
			// ��Q�X�g���[���Ȃ�A�A�J�E���g�e�[�u���̔ԍ��𒲂ׂċL������i�Q�X�g���[����NULL�j
			if(role instanceof NormalRole) {
				// NormalRole�ɃL���X�g
				NormalRole normalRole = (NormalRole)role;
				
				//�@�G���[�`�F�b�N
				if(!normalRole.accountTableDefined()) {
					Debug.error("���[���u" + normalRole.getRoleName() + "�v�̃A�J�E���g�e�[�u������`����Ă��܂���B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
					return null;
				}

				AccountTable at = normalRole.getAccountTable();
				sql += Integer.toString(at.getTableNumber());
			}
			else if(role instanceof GuestRole) {
				sql += "NULL";
			}
			else {
				Debug.error("���[����\���C���X�^���X�̌^���z��O�ł�", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return null;
			}

			sql += "),\n";
		}

		// �Ō��",\n"���J�b�g���A";\n"�Ƃ���
		sql = sql.substring(0, sql.length()-2);
		sql += ";\n";

		return sql;
	}
}

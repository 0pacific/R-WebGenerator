package role;

import table.AccountTable;





/*
 * ��Q�X�g���[����\���N���X
 */
public class NormalRole extends Role {
	// �A�J�E���g�e�[�u�����܂���`����Ă��Ȃ��Ȃ�NULL�A����Ă����AccountTable�C���X�^���X
	private AccountTable accountTable = null;

	
	
	
	
	public NormalRole(String name) {
		super(name);
	}
	
	
	
	
	
	public AccountTable getAccountTable() {
		return accountTable;
	}

	
	
	
	
	public void setAccountTable(AccountTable accountTable) {
		this.accountTable = accountTable;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * ���ɃA�J�E���g�e�[�u�����w�肳��Ă��邩�ۂ���Ԃ�
	 */
	public boolean accountTableDefined() {
		return (this.accountTable!=null);
	}
}

package account;

public class Account {

	//�y�ǉ��z
	//�A�J�E���g�̓��e��ۊǂ���N���X
	
	private String ID;
	private String password;
	
	public Account(){
		this.ID = "";
		this.password = "";
	}
	
	public void setAccount(String ID,String password){
		this.ID = ID;
		this.password = password;
	}
	
	public void setID(String ID){
		this.ID = ID;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getID(){
		return this.ID;
	}
	
	public String getPassword(){
		return this.password;
	}
}

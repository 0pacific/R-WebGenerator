package role;

import java.io.Serializable;

import gui.arrow.*;
import gui.*;

import table.*;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

public class Role implements Serializable {
	private String roleName;
	
	public Role(String name) {
		this.roleName = name;
	}

	public String getRoleName() {
		return roleName;
	}





	public int getRoleNumber() {
		return RoleManager.getInstance().getRoleIndex(this);
	}
}
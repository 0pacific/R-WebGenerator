package authority;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.AbstractField;
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import utility.*;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class AuthorityToDataTable extends AuthorityToTable implements Serializable {
	public AuthoritySet authSet;

	
	
	
	
	public AuthorityToDataTable(Role role, DataTable dataTable) {
		super(role, dataTable);
		initAuthSet();
	}




	public void initAuthSet() {
		authSet = new AuthoritySet(this.role, this.table, null);
	}





	public void informFieldAddition(Field newField) {
		authSet.informFieldAddition(newField);
	}
	
	
	
	public void informAbFieldAddition(AbstractField newField) {
		informFieldAddition(newField);
	}





	public void informIaAddition(Role role) {
		authSet.initIaAssignAuth(role);
	}
	public void informIaDeletion(Role role) {
		authSet.removeIaAssignAuth(role);
	}
}

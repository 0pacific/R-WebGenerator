package table;

import gui.*;
import gui.arrow.*;
import table.*;
import table.field.AbstractField;
import table.field.Field;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

public class DataTable extends SuperTable {
	public DataTable(String name) {
		this.name = name;
	}
	
	public void addField(Field field) {
		fieldArray.add(field);
		field.setTable(this);
		//TableManager.getInstance().informFieldAddition(field);
	}
	
	public void addFieldAllArray(Field field){
		allFieldArray.add(field);
		field.setTable(this);
		TableManager.getInstance().informFieldAddition(field);
	}
}
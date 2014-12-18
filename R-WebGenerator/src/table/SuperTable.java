package table;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;

import gui.*;
import gui.arrow.*;

import table.*;
import table.field.Field;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;

public abstract class SuperTable implements Serializable {
	protected String name;
	protected ArrayList<Field> fieldArray;
	
	public SuperTable() {
		fieldArray = new ArrayList<Field>();
	}
	
	public String getTableName() {
		return name;
	}

	public Field getField(int index) {
		return fieldArray.get(index);
	}

	public int getFieldNum() {
		return fieldArray.size();
	}
	
	public int getTableNumber() {
		return TableManager.getInstance().getTableIndex(this);
	}

	public Field getFieldByName(String fieldName) {
		for(int i=0; i<getFieldNum(); i++) {
			Field field = getField(i);
			if(field.name==fieldName) {
				return field;
			}
		}

		Debug.error("フィールドが見つかりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	public int getFieldOffset(Field argField) {
		for(int i=0; i<getFieldNum(); i++) {
			Field field = getField(i);
			if(field==argField) {
				return i;
			}
		}

		Debug.error("フィールドが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
	
	public ArrayList<String> getFieldNameArray() {
		ArrayList<String> nameArray = new ArrayList<String>();
		
		for(int i=0; i<getFieldNum(); i++) {
			Field field = getField(i);
			String fieldName = field.name;
			nameArray.add(fieldName);
		}

		return nameArray;
	}

	public boolean containsThisField(Field argField) {
		for(int i=0; i<this.getFieldNum(); i++) {
			Field field = getField(i);
			if(field==argField) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeField(Field field) {
		boolean remove = fieldArray.remove(field);
		if(!remove) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
			Debug.error("削除しようとしたフィールドが見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}
	
	public abstract void addField(Field field);
}
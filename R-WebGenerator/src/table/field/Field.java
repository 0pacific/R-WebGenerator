package table.field;

import java.io.Serializable;

import property.GeneratorProperty;
import debug.Debug;

import table.SuperTable;

public class Field implements Serializable {
	public String name;
	public String dataType;

	public int max;
	public int min;

	public SuperTable table;
	
	public boolean adminEditable;//�y�ǉ��z�Ǘ��҂��ύX�ł��邩�ǂ����itrue�ŕύX�B�f�t�H���g��false�j
	
	
	// DB���̃e�[�u��"field_info"��"dataType"�J�����ɓ���ׂ��l�ɂ��邱��
	public static final String DATATYPE_INT = "int";
	public static final String DATATYPE_VARCHAR = "varchar";
	public static final String DATATYPE_TEXT = "text";
	public static final String DATATYPE_DATETIME = "datetime";
	public static final String DATATYPE_DATE = "date";
	public static final String DATATYPE_TIME = "time";
	public static final String DATATYPE_FILE = "file";
	public static final String DATATYPE_MAIL = "mail";
	public static final String DATATYPE_MAIL_AUTH = "mail_auth";
	public static final String DATATYPE_USERID = "user_id";
	public static final String DATATYPE_PASSWORD = "password";
	public static final String DATATYPE_ROLE_NAME = "role_name";
	public static final String DATATYPE_PRIMARY_KEY = "primaryKey";		// RDF�Ȃ̂Œ���

	// DB��̃A�J�E���g�e�[�u���́A�A�J�E���g�I�[�i�[���[���ԍ����L�^����t�B�[���h�̖��O�i�����RDF�j
	public static final String DATATYPE_ROLE_NUMBER = "role_number";
	
	
	
	
	public Field(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
		this.adminEditable = false;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^�i�ő�l�E�ŏ��l�ݒ�j
	 */
	public Field(String name, String dataType, int min, int max) {
		this.name = name;
		this.dataType = dataType;
		this.min = min;
		this.max = max;
		this.adminEditable = false;
	}

	
	public static String getDataTypeExpression(String TYPE) {
		boolean japanese = GeneratorProperty.japanese();
		
		if(TYPE.equals(Field.DATATYPE_INT)) {
			return japanese ? "����" : "INTEGER";
		}
		else if(TYPE.equals(Field.DATATYPE_VARCHAR)) {
			return japanese ? "������" : "STRING";
		}
		else if(TYPE.equals(Field.DATATYPE_TEXT)) {
			return japanese ? "����" : "LONG TEXT";
		}
		else if(TYPE.equals(Field.DATATYPE_DATETIME)) {
			return japanese ? "���t�{����" : "DATE&TIME";
		}
		else if(TYPE.equals(Field.DATATYPE_DATE)) {
			return japanese ? "���t" : "DATE";
		}
		else if(TYPE.equals(Field.DATATYPE_TIME)) {
			return japanese ? "����" : "TIME";
		}
		else if(TYPE.equals(Field.DATATYPE_MAIL)) {
			return japanese ? "���[���A�h���X" : "MAIL";
		}
		else if(TYPE.equals(Field.DATATYPE_FILE)) {
			return japanese ? "�t�@�C��" : "FILE";
		}
		else if(TYPE.equals(Field.DATATYPE_USERID)) {
			return japanese ? "���[�UID" : "USER ID";
		}
		else if(TYPE.equals(Field.DATATYPE_MAIL_AUTH)) {
			return japanese ? "�F�ؗp���[���A�h���X" : "MAIL(FOR AUTHENTICATION)";
		}
		else if(TYPE.equals(Field.DATATYPE_PASSWORD)) {
			return japanese ? "�p�X���[�h" : "PASSWORD";
		}
		else if(TYPE.equals(Field.DATATYPE_ROLE_NAME)) {
			return japanese ? "���[����" : "ROLE NAME";
		}

		// �G���[
		Debug.error("�z��O�̃f�[�^�^�C�v�ł�", "FieldDataType", Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	
	public int getOffset() {
		return this.table.getFieldOffset(this);
	}
	
	public String getFieldName() {
		return name;
	}
	
	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public SuperTable getTable() {
		return this.table;
	}
	
	public void setTable(SuperTable table) {
		this.table = table;
	}
	
	public boolean isIntField() {
		return (dataType==Field.DATATYPE_INT);
	}

	public boolean isVarcharField() {
		return (dataType==Field.DATATYPE_VARCHAR);
	}
	
	public void setAdminEditable(){
		this.adminEditable = true;
	}
	
	public void setAdminNotEditable(){
		this.adminEditable = false;
	}
	
	public boolean getAdminEditable(){
		return this.adminEditable;
	}
}
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
	
	public boolean adminEditable;//【追加】管理者が変更できるかどうか（trueで変更可。デフォルトはfalse）
	
	
	// DB中のテーブル"field_info"の"dataType"カラムに入るべき値にすること
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
	public static final String DATATYPE_PRIMARY_KEY = "primaryKey";		// RDFなので注意

	// DB上のアカウントテーブルの、アカウントオーナーロール番号を記録するフィールドの名前（これもRDF）
	public static final String DATATYPE_ROLE_NUMBER = "role_number";
	
	
	
	
	public Field(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
		this.adminEditable = false;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ（最大値・最小値設定）
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
			return japanese ? "整数" : "INTEGER";
		}
		else if(TYPE.equals(Field.DATATYPE_VARCHAR)) {
			return japanese ? "文字列" : "STRING";
		}
		else if(TYPE.equals(Field.DATATYPE_TEXT)) {
			return japanese ? "長文" : "LONG TEXT";
		}
		else if(TYPE.equals(Field.DATATYPE_DATETIME)) {
			return japanese ? "日付＋時刻" : "DATE&TIME";
		}
		else if(TYPE.equals(Field.DATATYPE_DATE)) {
			return japanese ? "日付" : "DATE";
		}
		else if(TYPE.equals(Field.DATATYPE_TIME)) {
			return japanese ? "時刻" : "TIME";
		}
		else if(TYPE.equals(Field.DATATYPE_MAIL)) {
			return japanese ? "メールアドレス" : "MAIL";
		}
		else if(TYPE.equals(Field.DATATYPE_FILE)) {
			return japanese ? "ファイル" : "FILE";
		}
		else if(TYPE.equals(Field.DATATYPE_USERID)) {
			return japanese ? "ユーザID" : "USER ID";
		}
		else if(TYPE.equals(Field.DATATYPE_MAIL_AUTH)) {
			return japanese ? "認証用メールアドレス" : "MAIL(FOR AUTHENTICATION)";
		}
		else if(TYPE.equals(Field.DATATYPE_PASSWORD)) {
			return japanese ? "パスワード" : "PASSWORD";
		}
		else if(TYPE.equals(Field.DATATYPE_ROLE_NAME)) {
			return japanese ? "ロール名" : "ROLE NAME";
		}

		// エラー
		Debug.error("想定外のデータタイプです", "FieldDataType", Thread.currentThread().getStackTrace()[1].getMethodName());
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
<?php
/*
 * Created on 2010/11/15
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class FieldInformation {
	var $developerDefined;
	var $nameOnDb;
	var $nameOnWeb;

	// フィールドが属するDDTの番号
	var $tableNumber;

	// DataType「名」なので注意
	var $dataTypeName;

	var $min;
	var $max;
	var $offset;



	public function __construct($tableNumber, $offset) {
		logg_debug_call(__FUNCTION__, "FieldInformation");

		$this->tableNumber = $tableNumber;
		$this->offset = $offset;

		// フィールド情報テーブル　全体取得
		$query = "select * from field_info where tblNumber=$tableNumber and offset=$offset";
		$result = mysql_query_logg($query);

		// エラー処理
		if(mysql_num_rows($result)!==1) {
			logg_debug_error("MySQL結果セットがちょうど１行になってません", "FieldInformation", __FUNCTION__);
		}



		$queryF = "select * from field_info where tblNumber=$tableNumber and offset=$offset";
		$resultF = mysql_query_logg($queryF);
		
		// エラー処理
		if(mysql_num_rows($resultF)!==1) {
			logg_debug_error("MySQL結果セットがちょうど１行になってません", "FieldInformation", __FUNCTION__);
		}
		
		$this->developerDefined = mysql_result($resultF, 0, "developerDefined");
		$this->nameOnWeb = mysql_result($resultF, 0, "nameOnWeb");
		$this->min = mysql_result($resultF, 0, "min");
		$this->max = mysql_result($resultF, 0, "max");

		// 結果セットのデータタイプの欄をチェックし、DataTypeクラスのstatic定数（データタイプを表現）の中から当てはまるものを選びインスタンスにセット
		$dt = mysql_result($resultF, 0, "dataType");
		if($dt==="int") {
			$this->dataTypeName = DataType::TYPE_INT;
		}
		else if($dt==="varchar") {
			$this->dataTypeName = DataType::TYPE_VARCHAR;
		}
		else if($dt==="datatime") {
			$this->dataTypeName = DataType::TYPE_DATETIME;
		}
		else if($dt==="date") {
			$this->dataTypeName = DataType::TYPE_DATE;
		}
		else if($dt==="time") {
			$this->dataTypeName = DataType::TYPE_TIME;
		}
		else if($dt==="enum") {	
			$this->dataTypeName = DataType::TYPE_ENUM;
		}
		else if($dt==="file") {
			$this->dataTypeName = DataType::TYPE_FILE;
		}
		else if($dt==="mail") {
			$this->dataTypeName = DataType::TYPE_MAIL;
		}
		else if($dt==="mail_auth") {
			$this->dataTypeName = DataType::TYPE_MAILAUTH;
		}
		else if($dt==="user_id") {
			$this->dataTypeName = DataType::TYPE_USERID;
		}
		else if($dt==="password") {
			$this->dataTypeName = DataType::TYPE_PASSWORD;
		}
		else if($dt==="primaryKey") {
			$this->dataTypeName = DataType::TYPE_PRIMKEY;
		}
		else if($dt==="role_name") {
			$this->dataTypeName = DataType::TYPE_ROLE_NAME;
		}
		else if($dt==="role_number") {
			$this->dataTypeName = DataType::TYPE_ROLE_NUMBER;
		}
		else {
			logg_debug_error("フィールド一覧テーブルの結果セットから、想定外のデータタイプが検出されました。", "FieldInformation", __FUNCTION__);
		}

		// DB上でのフィールドの名前をインスタンスにセット
		// 通常は"field"+オフセットだが、主キーの場合は"primaryKey";
		if($this->dataTypeName===DataType::TYPE_PRIMKEY) {
			$this->nameOnDb = PRIMARY_KEY;
		}
		else {
			$this->nameOnDb = "field$offset";
		}

		logg_debug_return(__FUNCTION__, "FieldInformation");
	}
}
?>

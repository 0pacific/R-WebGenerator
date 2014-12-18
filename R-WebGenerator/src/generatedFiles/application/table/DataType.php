<?php

class DataType {
	const TYPE_INT = "int";
	const TYPE_VARCHAR = "varchar";
	const TYPE_DATETIME = "datetime";
	const TYPE_DATE = "date";
	const TYPE_TIME = "time";
	const TYPE_ENUM = "enum";
	const TYPE_FILE = "file";
	const TYPE_MAIL = "mail";
	const TYPE_MAILAUTH = "mail_auth";
	const TYPE_USERID	= "user_id";
	const TYPE_PASSWORD = "password";
	const TYPE_PRIMKEY = PRIMARY_KEY;
	const TYPE_ROLE_NAME = "role_name";
	const TYPE_ROLE_NUMBER = "role_number";
	const TYPE_BLANK = "blank";

	var $dataTypeName;

	var $max_value;
	var $min_value;

	/*
	SUMMARY : コンストラクタ
	          メタデータテーブルに記述されているデータタイプ（＋付加情報）を受け取り、解釈してデータタイプをラッピングする
	MODIFY : 全てのデータタイプについて処理を記述しなければならない。
	*/
	public function __construct($dataType_str, $min_value, $max_value) {
		logg_debug_call(__FUNCTION__, "DataType");
		debug_varDump(get_class($this).'->'.__FUNCTION__, '$dataType_str', $dataType_str);

		$this->dataTypeName = $dataType_str;

		if($this->dataTypeName===DataType::TYPE_INT) {
			$this->max_value = $max_value;
			$this->min_value = $min_value;
		}
		else if($this->dataTypeName===DataType::TYPE_VARCHAR) {
			$this->max_value = $max_value;
			$this->min_value = $min_value;
		}
		else if($this->dataTypeName===DataType::TYPE_DATETIME) {
		}
		else if($this->dataTypeName===DataType::TYPE_DATE) {
		}
		else if($this->dataTypeName===DataType::TYPE_TIME) {
		}
		else if($this->dataTypeName===DataType::TYPE_ENUM) {
		}
		else if($this->dataTypeName===DataType::TYPE_FILE) {
			$this->max_value = $max_value;
		}
		else if($this->dataTypeName===DataType::TYPE_MAIL) {
		}
		else if($this->dataTypeName===DataType::TYPE_MAILAUTH) {
		}
		else if($this->dataTypeName===DataType::TYPE_USERID) {
		}
		else if($this->dataTypeName===DataType::TYPE_PASSWORD) {
			$this->max_value = $max_value;
			$this->min_value = $min_value;
		}
		else if($this->dataTypeName===DataType::TYPE_ROLE_NAME) {
		}
		else if($this->dataTypeName===DataType::TYPE_PRIMKEY) {
		}
		else if($this->dataTypeName===DataType::TYPE_ROLE_NUMBER) {
		}
		else if($this->dataTypeName===DataType::TYPE_BLANK) {
		}
		else {
			logg_debug_error("'".$this->dataTypeName."'は想定外のデータタイプのようです", "DataType", __FUNCTION__);
		}

		logg_debug_return(__FUNCTION__, "DataType");
	}





	public function getDataType() {
		logg_debug_call(__FUNCTION__, "DataType");

		logg_debug_return(__FUNCTION__, "DataType");
		return $this->dataTypeName;
	}





	/*
	SUMMARY : テーブル番号を受け取り、対応するテーブルのデータタイプラッパー配列を返却する
	          データタイプラッパーとは、フィールドのデータタイプ・最大値・最小値などをラッピングしたオブジェクト
	*/
	public static function getDtraFromTableNumber($tableNumber) {
		logg_debug_call(__FUNCTION__, "DataType");

		$query = 'SELECT ' . TABLE_DETAIL_FIELD_MDT . ' FROM table_list'
					. ' WHERE ' . TABLE_DETAIL_FIELD_NUMBER . '=' . $tableNumber;
		$result = mysql_query_logg($query);

		// エラー
		if(mysql_num_rows($result)!==1) {
			error_logg(__FUNCTION__ . '() : ただ１つのレコードが得られませんでした');
		}

		// メタデータテーブルの名称抽出
		$mdt_name = mysql_result($result, 0, 0);

		// DUPLICATION FINISHES


		// Field Information 抽出
		$query_readDataType = 'SELECT ' . TBL_FIT_TYPE . ',' . TBL_FIT_MAX . ',' . TBL_FIT_MIN
								. ' FROM ' . $mdt_name
								. ' ORDER BY ' . TBL_FIT_OFFSET . ' ASC';
		$result_readDataType = mysql_query_logg($query_readDataType);

		// データタイプラッパー配列作成
		$dataTypeArray = array();
		$fieldNum = mysql_num_rows($result_readDataType);
		for($i=0; $i<$fieldNum; $i++) {
			$type = mysql_result($result_readDataType, $i, 0);
			$max = mysql_result($result_readDataType, $i, 1);
			$min = mysql_result($result_readDataType, $i, 2);
			$dataTypeArray[$i] = new DataType($type, $max, $min);
		}

		logg_debug_return(__FUNCTION__, "DataType");
		return $dataTypeArray;
	}
}
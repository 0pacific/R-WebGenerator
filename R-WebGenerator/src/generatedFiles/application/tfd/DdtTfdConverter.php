<?php

class DdtTfdConverter {
	const MODE_ALL_RECORDS = 0;	// とにかく全レコードをTFDに変換する
	const MODE_PRIMKEYS = 1;		// $this->recordPrimKeyArrayに設定してある各主キーに対応したレコードだけ、TFDに変換する

	// DDT中のレコードをどのように選び抜きTFDに変換するかというモード（上に定義してある定数が格納される）
	var $recordSelectionMode;

	// MODE_PRIMKEYSの場合に用いるレコード主キー配列
	var $recordPrimKeyArray;





	public function __construct() {
		// デフォルトのモード（全レコード変換）に設定
		$this->recordSelectionMode = DdtTfdConverter::MODE_ALL_RECORDS;
	}




	/*
	 * SUMMARY :
	 * 指定したDeveloper Defined Table 全体のデータを TabularFormData インスタンスに変換し、返却する
	 * 
	 * NOTICE :
	 * ・ページエレメント 'Table Display' の場合、TPPNはないが、TPPNとして-1を入れること
	 * TableReadingにはTPPNはつく
	 * 
	 * MODIFY :
	 * ・全てのデータタイプについて処理を記述しなければならない。
	 * ・Field Information Table の Web 上表示名のフィールドの値について、「書いてない」と判断するには「''またはnull」でよいものか
	*/
	public function convDdtToTfd($tableNumber, $tppn) {
		logg_debug_call(__FUNCTION__, "DdtTfdConverter");

		$tblInfo = new TableInformation($tableNumber);

		$ddfNum = $tblInfo->getDdfNum();

		// DDTの各DDFについて調べていき、
		// DataTypeインスタンス配列 $dataTypeArray をセット
		$dataTypeArray = array();
		for($i=0; $i<$ddfNum; $i++) {
			// このフィールドの情報を持つFieldInformationインスタンス
			$fieldInfo = $tblInfo->fieldInfoArray[$i];

			// データタイプ名
			$dataTypeName = $tblInfo->fieldInfoArray[$i]->dataTypeName;

			// 最小値・最大値
			$min = $fieldInfo->min;
			$max = $fieldInfo->max;

			// ENUM
			if($dataTypeName===DataType::TYPE_ENUM) {
				// ◆未実装
			}
			// ENUM以外
			else {
				$dataTypeArray[$i] = new DataType($dataTypeName, $min, $max);
			}
		}


		// Developer Defined Fieldを調べていき、Web表示名配列 $webNameArray をセット
		$webNameArray = array();
		for($i=0; $i<$ddfNum; $i++) {
			// このフィールドの情報を持つFieldInformationインスタンス
			$fieldInfo = $tblInfo->fieldInfoArray[$i];

			$webNameArray[$i] = $fieldInfo->nameOnWeb;
		}


		$dataTableName_db = $tblInfo->nameOnDb;

		// デフォルトモード（全レコード抽出）の場合のクエリ
		if($this->recordSelectionMode===DdtTfdConverter::MODE_ALL_RECORDS) {
			$query2 = 'SELECT * FROM ' . $dataTableName_db;
		}
		// 指定した主キー（複数可）のレコードだけ抽出する場合のクエリ
		else if($this->recordSelectionMode===DdtTfdConverter::MODE_PRIMKEYS) {
			$query2 = "select * from " . $dataTableName_db . " where ";
			for($i=0; $i<count($this->recordPrimKeyArray); $i++) {
				$query2 .= PRIMARY_KEY . "=" . $this->recordPrimKeyArray[$i] . " or ";
			}

			// 最後の" or "をカット
			$query2 = substr($query2, 0, -4);
		}
		$resource = mysql_query_logg($query2);

		// モードに応じてDDTの中からレコードを選び抜き、各DDFの値を取得
		// TFDバリュー配列 $valueTable へ格納
		$valueTable = array();
		$recordNum = mysql_num_rows($resource);

		// アカウントテーブルの場合は、UserInfoインスタンスを作っていく必要がある
		if($tblInfo->tableType==="account table") {
			$userInfoArray = array();
		}

		for($i=0; $i<$recordNum; $i++) {
			$valueTable[$i] = array();
			for($j=0; $j<$ddfNum; $j++) {
				$val = mysql_result($resource, $i, $j);

				// FILE型 -> 値はFileValueインスタンスとして格納
				if($dataTypeArray[$j]->getDataType()===DataType::TYPE_FILE) {
					$primKey = mysql_result($resource, $i, PRIMARY_KEY);
					$cellLocation = new CellLocation($tableNumber, $primKey, $j);
					$fileValue = new FileValue($val, $cellLocation);
					$valueTable[$i][$j] = $fileValue;
				}
				// FILE型以外 -> DDT から得た値をそのまま格納
				else {
					$valueTable[$i][$j] = $val;
				}
			}

			// アカウントテーブルの場合には、このレコード（アカウント）の持ち主のことをUserInfoインスタンスという形で記録する

			if($tblInfo->tableType==="account table") {
				$roleNumber = mysql_result($resource, $i, "roleNumber");
				$userNumber = mysql_result($resource, $i, PRIMARY_KEY);
				$userInfoArray[$i] = new UserInfo($roleNumber, $userNumber);
			}
		}


		// 各TFDレコードのレコードヒストリーを作成、 $recordHistoryArray へ格納
		$recordHistoryArray = array();
		for($i=0; $i<$recordNum; $i++) {
			// このレコードのレコードロケーションを求める
			$primKey = mysql_result($resource, $i, PRIMARY_KEY);
			$recordLocation = new RecordLocation($tableNumber, $primKey);		

			$recordHistoryArray[$i] = new RecordHistory();
			$recordHistoryArray[$i]->setHistory($tppn, array($recordLocation));
		}


		// TabularFormDataオブジェクトの作成
		if($tblInfo->tableType==="data table") {
			$td = new TabularFormData($valueTable, $recordHistoryArray, $dataTypeArray, $webNameArray);
		}
		else if($tblInfo->tableType==="account table") {
			$td = new TabularFormData($valueTable, $recordHistoryArray, $dataTypeArray, $webNameArray, $userInfoArray);
		}

		logg_debug_return(__FUNCTION__, "DdtTfdConverter");
		return $td;
	}





	public function shiftToPrimKeysMode($primKeyArray) {
		$this->recordSelectionMode = DdtTfdConverter::MODE_PRIMKEYS;
		$this->recordPrimKeyArray = $primKeyArray;
	}





	public static function convDdtToTfdForTableDisplay($tableNumber) {
		$dtc = new DdtTfdConverter();
		return $dtc->convDdtToTfd($tableNumber, -1);
	}
}

?>
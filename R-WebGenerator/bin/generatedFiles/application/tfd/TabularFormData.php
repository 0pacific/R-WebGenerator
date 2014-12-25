<?php

class TabularFormData {
	// TabularFormDataRecordインスタンス配列
	var $recordArray;

	// DataTypeオブジェクト配列
	var $dataTypeArray;

	// 各 User Friendly Field のWeb表示名を格納する配列
	var $webNameArray;

	// レコード数、フィールド数
	var $recordNum;
	var $fieldNum;





	/*
	 * SUMMARY :
	 * コンストラクタ
	 * 
	 * ARGUMENTS :
	 * $valueTable           : TFDバリュー配列（各セルの値を並べた２次元配列）
	 * $recordHistoryArray   : RecordHistoryインスタンス配列（0,1,2...番目のRecordHistoryインスタンスは、0,1,2...番目のTabularFormDataRecordインスタンスのコンストラクタに与えられる）
	 * $dataTypeArray        : 各フィールドのデータタイプを表す、DataTypeインスタンス配列
	 * $webNameArray         : 各UFFの表示名を表す配列
	*/
	public function __construct($valueTable, $recordHistoryArray, $dataTypeArray, $webNameArray, $userInfoArray=null) {
		logg_debug_call(__FUNCTION__, "TabularFormData");

		// レコード数・フィールド数セット
		$recordNum = count($valueTable);
		$this->recordNum = $recordNum;
		$fieldNum = count($valueTable[0]);
		$this->fieldNum = $fieldNum;

		// 各レコード（TabularFormDataRecordオブジェクト）の初期化
		$this->recordArray = array();
		for($i=0; $i<$recordNum; $i++) {
			$this->recordArray[$i] = new TabularFormDataRecord($fieldNum, $recordHistoryArray[$i]);

			// もしもUserInfoインスタンス配列（第５引数）が渡されているなら、各TFDレコードにUserInfoインスタンスをセットしていく
			if($userInfoArray!==null) {
				$this->recordArray[$i]->setUserInfo($userInfoArray[$i]);
			}

			// 各セル（TabularFormDataCellオブジェクト）の初期化
			for($j=0; $j<$fieldNum; $j++) {
				$this->recordArray[$i]->setCell($j, $valueTable[$i][$j]);
			}
		}

		$this->dataTypeArray = $dataTypeArray;
		$this->webNameArray = $webNameArray;

		logg_debug_return(__FUNCTION__, "TabularFormData");
	}



	/*
	 * SUMMARY :
	 * User Friendly Field の総数を返す（つまりこのTFDのフィールド数を返す）
	*/
	public function getVisibleFieldNum() {
		return count($this->webNameArray);
	}

	public function getValue($recordNumber, $fieldNumber) {
		return $this->recordArray[$recordNumber]->cellArray[$fieldNumber]->value;
	}

	public function getDataTypeArray() {
		return $this->dataTypeArray;
	}

	public function getDataType($index) {
		return $this->dataTypeArray[$index];
	}

	public function getWebNameArray() {
		return $this->webNameArray;
	}

	public function getRecord($index) {
		return $this->recordArray[$index];
	}

	public function getRecordNum() {
		return $this->recordNum;
	}
}



class TabularFormDataRecord {
	// セル配列
	var $cellArray;

	var	$fieldNum;

	// このTFDレコードに、ある１ユーザのアカウントの内容がそのまま入っているとき（DDT LoadやCreate FormからのTFD変換）にのみ使う
	// 誰のアカウントなのかUserInfoインスタンスとして記録するためのインスタンス変数である
	var $userInfo;

	// このTFDレコードに、これから作成するアカウントの内容が入っている場合にのみ使う（つまりアカウントテーブルの標準Create FormからTFDに変換した場合）
	var $accOwnRoleNumber;

	// RecordHistoryインスタンス
	var $recordHistory;
	
	
	
	
	
	/*
	 * SUMMARY :
	 * コンストラクタ
	 */
	public function __construct($fieldNum, $recordHistory) {
		$this->fieldNum = $fieldNum;
		$this->recordHistory = $recordHistory;
	}

	public function setCell($index, $value) {
		$this->cellArray[$index] = new TabularFormDataCell($value);
	}

	public function getCell($index) {
		return $this->cellArray[$index];
	}

	public function getFieldNum() {
		return count($this->fieldNum);
	}

	public function setAccOwnRoleNumber($accOwnRoleNumber) {
		$this->accOwnRoleNumber = $accOwnRoleNumber;
	}

	public function setUserInfo($userInfo) {
		$this->userInfo = $userInfo;
	}
}



class TabularFormDataCell {
	/*
	 * セルの値
	 */
	var $value;

	/*
	 * SUMMARY :
	 * コンストラクタ
	 */
	public function __construct($value) {
		$this->value = $value;
	}

	public function getValue() {
		return $this->value;
	}
}
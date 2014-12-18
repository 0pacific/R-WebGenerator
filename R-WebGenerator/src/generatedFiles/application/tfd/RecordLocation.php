<?php
/*
 * Created on 2010/08/03
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class RecordLocation {
	var $tableNumber;
	var $primaryKey;
	
	/*
	 * SUMMARY :
	 * コンストラクタ
	 */
	public function __construct($tableNumber, $primaryKey) {
		$this->tableNumber = $tableNumber;
		$this->primaryKey = $primaryKey;
	}






	/*
	 * ２つのRecordLocationインスタンスが等価であるか否かをbooleanで返す
	 * 等価とは、RecordLocationインスタンスがインスタンス変数として持つテーブル番号・レコード主キー双方がそれぞれ等しいということである
	 */
	public static function equals($recordLocation1, $recordLocation2) {
		if($recordLocation1->getTableNumber()!==$recordLocation2->getTableNumber()) {
			return false;
		}
		else if($recordLocation1->getPrimKey()!==$recordLocation2->getPrimKey()) {
			return false;
		}
		return true;		
	}





	/*
	 * SUMMARY :
	 * 第一引数のRecordLocationインスタンス配列の中に、第二引数のRecordLocationインスタンスと等価なものがあるか否かをbooleanで返す
	 */
	public static function findSame($recordLocationArray, $recordLocation) {
		for($i=0; $i<count($recordLocationArray); $i++) {
			if(RecordLocation::equals($recordLocationArray[$i], $recordLocation)) {
				return true;
			}
		}
		return false;
	}





	public function getTableNumber() {
		return $this->tableNumber;
	}

	public function getPrimKey() {
		return $this->primaryKey;
	}
}

?>
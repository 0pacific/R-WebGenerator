<?php
/*
 * Created on 2010/08/03
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class CellLocation {
	var $tableNumber;
	var $primaryKey;
	var $offset;
	
	/*
	 * SUMMARY :
	 * コンストラクタ
	 */
	public function __construct($tableNumber, $primaryKey, $offset) {
		$this->tableNumber = $tableNumber;
		$this->primaryKey = $primaryKey;
		$this->offset = $offset;
	}






	/*
	 * ２つのCellLocationインスタンスが等価であるか否かをbooleanで返す
	 * 等価とは、CellLocationインスタンスがインスタンス変数として持つテーブル番号・レコード主キー・DDFオフセット全てが等しいということである
	 */
	public static function equals($cl1, $cl2) {
		if($cl1->getTableNumber()!==$cl2->getTableNumber()) {
			return false;
		}
		else if($cl1->getPrimKey()!==$cl2->getPrimKey()) {
			return false;
		}
		else if($cl1->getOffset()!==$cl2->getOffset()) {
			return false;
		}
		return true;		
	}





	/*
	 * SUMMARY :
	 * 第一引数のCellLocationインスタンス配列の中に、第二引数のCellLocationインスタンスと等価なものがあるか否かをbooleanで返す
	 */
	public static function findSame($cellLocationArray, $cellLocation) {
		for($i=0; $i<count($cellLocationArray); $i++) {
			if(CellLocation::equals($cellLocationArray[$i], $cellLocation)) {
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

	public function getOffset() {
		return $this->offset;
	}
}

?>
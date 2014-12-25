<?php
/*
 * Created on 2010/09/06
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class RecordHistory {
	/*
	 * TPP の実行履歴を表すハッシュ
	 * キー : TPPN
	 * 値 : その TPP に対応する、DDTの全レコードのレコードロケーション（RecordLocationオブジェクト配列）
	 */
	var $tppnRecordLocationsHash;





	public function __construct() {
		$this->tppnRecordLocationsHash = array();
	}





	/*
	 * SUMMARY :
	 * 指定したTPPNに対応する履歴として、RecordLocationインスタンス配列を渡し履歴ハッシュにセット
	 * そのTPPNの履歴が既にあった場合上書きされるので注意
	 */
	public function setHistory($tppn, $recordLocationArray) {
		$this->tppnRecordLocationsHash[$tppn] = $recordLocationArray;
	}





	public function getRecordLocationArrayByTppn($tppn) {
		return $this->tppnRecordLocationsHash[$tppn];
	}





	public function addRecordLocationIfNotExists($recordLocation, $tppn) {
		// インデックス$tppnが存在しないなら作る
		$this->putTppnAsIndexIfNotExists($tppn);

		// $recordLocationと等価なRecordLocationインスタンスが、$tppnと対になったRecordLocationインスタンス配列中にある -> 何もしない
		if(RecordLocation::findSame($this->tppnRecordLocationsHash[$tppn], $recordLocation)) {
			return;
		}
		// ないのであれば、追加する
		array_push($this->tppnRecordLocationsHash[$tppn], $recordLocation);
	}





	public function addRecordLocationArray($recordLocArray, $tppn) {
		// インデックス$tppnが存在しないなら作る
		$this->putTppnAsIndexIfNotExists($tppn);

		// 渡されたRecordLocationインスタンス配列の各要素を、$tppnに対応した履歴（RecordLocationインスタンス配列）に「まだないのなら」追加する
		for($i=0; $i<count($recordLocArray); $i++) {
			$recordLoc = $recordLocArray[$i];
			$this->addRecordLocationIfNotExists($recordLoc, $tppn);
		}
	}




	public function putTppnAsIndexIfNotExists($tppn) {
		if(!array_key_exists($tppn, $this->tppnRecordLocationsHash)) {
			$this->tppnRecordLocationsHash[$tppn] = array();
		}
	}




	/*
	 * SUMMARY :
	 * ２つのRecordHistoryインスタンスの持つ履歴の和集合（重複排除）を履歴として持つ、新しいRecordHistoryインスタンスを返す
	 */
	public static function joinRecordHistory($recHist1, $recHist2) {
		// 最後に返却するRecordHistoryインスタンス
		$recHist3 = new RecordHistory();

		// まず、１つ目のRecordHistoryインスタンスの持つ履歴をそのままコピー
		$recHist3->tppnRecordLocationsHash = $recHist1->tppnRecordLocationsHash;

		// そこへ、２つ目のRecordHistoryインスタンスの持つ全ての履歴を重複なく付け足していく
		foreach($recHist2->tppnRecordLocationsHash as $tppn => $recLocArray) {
			for($i=0; $i<count($recLocArray); $i++) {
				$recHist3->addRecordLocationIfNotExists($recLocArray[$i], $tppn);
			}
		}

		return $recHist3;
	}




	/*
	 * SUMMARY :
	 * このレコードヒストリーにある全RecordLocationを重複なく集め、配列で返す
	 * 
	 * NOTICE :
	 * ・どういう順番でRecordLocationオブジェクトを並べるかは考慮しない
	 */
	public function getAllRecordLocations() {
		// この配列にRecordLocationインスタンスを１つ１つ格納していき、最後に返却
		$array = array();

		// 各TPPについて繰り返し
		foreach($this->tppnRecordLocationsHash as $tppn => $recLocArray) {
			for($i=0; $i<count($recLocArray); $i++) {
				// 「$tppnに対応したRecordLocationインスタンス配列の$i番目のインスタンス」と等価なRecordLocationインスタンスが$arrayに含まれるか？
				$fs = RecordLocation::findSame($array, $recLocArray[$i]);

				// 含まれる -> スキップ
				if($fs===true) {
				}
				// 含まれない -> 末尾に追加
				if($fs===false) {
					array_push($array, $recLocArray[$i]);
				}
			}
		}

		return $array;
	}





	/*
	 * SUMMARY :
	 * このレコードヒストリーの中から、指定したDDTのレコードのレコードロケーションを重複なく集め配列で返す
	 */
	public function getDdtRecordLocations($ddtNumber) {
		$ddtRecLocArray = array();

		foreach($this->tppnRecordLocationsHash as $tppn => $recLocArray) {
			for($i=0; $i<count($recLocArray); $i++) {
				$recLoc = $recLocArray[$i];
				if($recLoc->tableNumber===$ddtNumber && !RecordLocation::findSame($ddtRecLocArray, $recLoc)) {
					array_push($ddtRecLocArray, $recLoc);
				}
			}
		}

		return $ddtRecLocArray;
	}
}

?>
<?php
/*
 * Created on 2010/12/01
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class RelatedRecordFinder {
	var $tfd;
	var $ddtNumber;





	public function __construct($tfd, $ddtNumber) {
		$this->tfd = $tfd;
		$this->ddtNumber = $ddtNumber;
	}





	/*
	 * SUMMARY :
	 * DDTの中から、TFDに対応している全レコードを見つけ、RecordLocationインスタンス配列で返す
	 * 
	 * NOTICE :
	 * 返すレコードロケーションの数がTFDのレコード数と同じになることを前提としている。（そうでない場合けっこう困るので、現時点ではエラーを出力している）
	 * 返すレコードロケーション配列は、TFDの１レコード目に対応した１DDTレコードのロケーション、TFDの２レコード目に対応した１DDTレコードのロケーション・・・という順番になる
	 */
	public function getRecordLocations() {
		// この配列にRecordLocationインスタンス配列を格納し、最後に返却
		$recLocs = array();

		$tfdRecordNum = $this->tfd->getRecordNum();
		for($i=0; $i<$tfdRecordNum; $i++) {
			$tfdRecord = $this->tfd->getRecord($i);

			// TFDレコードのレコードヒストリー
			$recordHist = $tfdRecord->recordHistory;

			// 目当てのDDTの全レコードの中から、このTFDレコードに対応するものを全部見つけ、配列で取得
			$ddtRecLocs = $recordHist->getDdtRecordLocations($this->ddtNumber);

			// エラー：このTFDレコードに対応すると判断されたDDTレコードがちょうど１つではなかった
			if(count($ddtRecLocs)!==1) {
				logg_debug_error("1201:TDF中の１レコードに対応した、".$this->ddtNumber."番DDTの１レコードを発見しようとしましたが、発見されたのは０個ないし２個以上でした", "RelatedRecordFinder", __FUNCTION__);
			}

			// このTFDレコードに対応するDDT中の１レコードのロケーション
			$ddtRecLoc = $ddtRecLocs[0];

			// もしも他のTFDレコードの場合と同じDDTレコードだったならばエラー
			// （まずそうはならないと思うが・・・もしこうなると、どっちのTFDレコードをリフレクションすればいいかわからない）
			if(RecordLocation::findSame($recLocs, $ddtRecLoc)) {
				logg_debug_error("1201:TDF中の２つのレコードについて、対応するDDTレコードがかぶっています。", "RelatedRecordFinder", __FUNCTION__);
			}

			array_push($recLocs, $ddtRecLoc);
		}

		// 一応、取得したDDTレコードロケーションの数がTFDレコード数と一致するかチェック
		if(count($recLocs)===$tfdRecordNum) {
			logg_debug("1201:TDFレコードの数と、TFDに対応すると判断されたDDTレコードの数はちゃんと一致しています。", "RelatedRecordFinder", __FUNCTION__);
		}
		else {
			logg_debug_error("1201:TDFレコードの数と、TFDに対応すると判断されたDDTレコードの数が不一致です。", "RelatedRecordFinder", __FUNCTION__);
		}

		return $recLocs;
	}
}
?>

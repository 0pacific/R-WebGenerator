<?php

class ServiceExecuter {
	var $tppn;

	


	public function __construct($tppn) {
		$this->tppn = $tppn;
	}





	public function getServiceFunctionName($index) {
		return "service" . $index;
	}





	/*
	 * SUMMARY :
	 * サービス「フィールド抽出」
	 * TFDを受け取り、オフセットで指定された１つ以上のフィールドを抽出して、新たなTFDを作成・出力する
	 */
	public function service1($inputTfd, $offsetArray) {
		logg_debug_call(__FUNCTION__, "ServiceExecuter");
	
		// 入力TFDのDataTypeインスタンス配列とWeb表示名配列
		$dataTypeArray_input = $inputTfd->getDataTypeArray();
		$webNameArray_input = $inputTfd->webNameArray;

		// 出力する TabularFormData の行数・フィールド数
		$outputTfdRecordNum = $inputTfd->getRecordNum();
		$outputTfdFieldNum = count($offsetArray);
	
		// 出力するTFDのTFDバリュー配列と、各レコードのレコードヒストリーを並べた配列（今から作っていく）
		$outputValueTable = array();
		$recordHistoryArray = array();

		// レコード数だけ繰り返し
		for($i=0; $i<$outputTfdRecordNum; $i++) {
			// 入力TFD中のTFDレコード（TabularFormDataRecordインスタンス）
			$inputTfdRecord = $inputTfd->getRecord($i);

			// まず、入力TFD中のTFDレコードのレコードヒストリーをそのままコピー
			$recordHistoryArray[$i] = clone $inputTfdRecord->recordHistory;

			// 入力TFD中のTFDレコードのレコードヒストリーから、全レコードロケーションを重複なくかき集め、
			// それらのレコードロケーションを今のサービス実行に対応した履歴とする
			$allRecLocs = $inputTfdRecord->recordHistory->getAllRecordLocations();
			$recordHistoryArray[$i]->setHistory($this->tppn, $allRecLocs);

			// 入力TFD中の各セルの値を読み出し、TFDバリュー配列にセットする
			$outputValueTable[$i] = array();
			for($offset=0; $offset<$outputTfdFieldNum; $offset++) {
				$cell = $inputTfdRecord->getCell($offsetArray[$offset]);
				$outputValueTable[$i][$offset] = $cell->getValue();
			}
		}
	
		// 出力TFDのDataTypeインスタンス配列・Web表示名配列を作成
		$dataTypeArray_output = array();
		$webNameArray_output = array();
		for($i=0; $i<$outputTfdFieldNum; $i++) {
			$dataTypeArray_output[$i] = $dataTypeArray_input[$offsetArray[$i]];
			$webNameArray_output[$i] = $webNameArray_input[$offsetArray[$i]];
		}

		$tdf = new TabularFormData($outputValueTable, $recordHistoryArray, $dataTypeArray_output, $webNameArray_output);
		
		logg_debug_return(__FUNCTION__, "ServiceExecuter");
		return $tdf;
	}





	/*
	 * SUMMARY :
	 * サービス「合計計算」
	 * TFDを受け取り、オフセットで指定された１つ以上のINTフィールドから、「その合計値を格納した１つのフィールド」を表す１列のTFDを作成・返却する
	 * その１つのフィールドのWeb表示名は、第三引数として受け取る（文字列ではなく要素数１の配列なので注意）
	 */
	public function service2($inputTfd, $offsetArray, $webNameMonoArray) {
		logg_debug_call(__FUNCTION__, "ServiceExecuter");

		/*
		 * ◆未実装　入力TFD中の、指定されたフィールド達の中にINT型でないものが１つでもあったらエラー　これをチェックする
		 */
	
		// 入力TFDのDataTypeインスタンス配列とWeb表示名配列
		$dataTypeArray_input = $inputTfd->getDataTypeArray();
		$webNameArray_input = $inputTfd->webNameArray;

		// 出力する TabularFormData の行数・フィールド数
		$outputTfdRecordNum = $inputTfd->getRecordNum();
		$outputTfdFieldNum = 1;
	
		// 出力するTFDのTFDバリュー配列と、各レコードのレコードヒストリーを並べた配列（今から作っていく）
		$outputValueTable = array();
		$recordHistoryArray = array();

		// レコード数だけ繰り返し
		for($recordOrder=0; $recordOrder<$outputTfdRecordNum; $recordOrder++) {
			// 入力TFD中のTFDレコード（TabularFormDataRecordインスタンス）
			$inputTfdRecord = $inputTfd->getRecord($recordOrder);

			// まず、入力TFD中のTFDレコードのレコードヒストリーをそのままコピー
			// 入力TFD中のTFDレコードのレコードヒストリーから、全レコードロケーションを重複なくかき集め、
			// それらのレコードロケーションを今のサービス実行に対応した履歴とする
			$recordHistoryArray[$recordOrder] = clone $inputTfdRecord->recordHistory;
			$allRecLocs = $inputTfdRecord->recordHistory->getAllRecordLocations();
			$recordHistoryArray[$recordOrder]->setHistory($this->tppn, $allRecLocs);
	

			$outputValueTable[$recordOrder] = array();

			// このレコードの合計値（これから指定されたフィールドのセルを１つずつ見ていき、値を足していく）
			$sumCellValue = 0;
			for($i=0; $i<count($offsetArray); $i++) {		// 指定されている各フィールド（のセル）を見ていく
				$cell = $inputTfdRecord->getCell($offsetArray[$i]);
				$cellValue = $cell->value;
				$sumCellValue += $cellValue;
			}
			// 出力するTFD中の$recordOrder番目のレコード（セルは１つだけ）の値が決定した 
			$outputValueTable[$recordOrder][0] = $sumCellValue;
		}

		// 出力TFDのDataTypeインスタンス配列・Web表示名配列を作成
		$dataTypeArray_output = array();
		$dataTypeArray_output[0] = new DataType(DataType::TYPE_INT, null, null);	// ◆本当はminもmaxも計算すべき

		$tdf = new TabularFormData($outputValueTable, $recordHistoryArray, $dataTypeArray_output, $webNameMonoArray);
		
		logg_debug_return(__FUNCTION__, "ServiceExecuter");
		return $tdf;
	}





	/*
	 * SUMMARY :
	 * サービス「表結合」
	 * ２つのTFDを受け取り、１つ目の右側に２つ目を結合する
	 */
	public function service3($tfdLeft, $tfdRight) {
		logg_debug_call(__FUNCTION__, "ServiceExecuter");
	
		// ２つの入力TFDのDataTypeインスタンス配列とWeb表示名配列
		$dataTypeArrayLeft = $tfdLeft->getDataTypeArray();
		$dataTypeArrayRight = $tfdRight->getDataTypeArray();
		$webNameArrayLeft = $tfdLeft->webNameArray;
		$webNameArrayRight = $tfdRight->webNameArray;

		// エラーチェック
		if($tfdLeft->getRecordNum()!==$tfdRight->getRecordNum()) {
			logg_debug_error("サービス「表結合」が受け取った２つのTFDのレコード数が違っています", "ServiceExecuter", __FUNCTION__);
		}

		// 出力する TabularFormData の行数・フィールド数
		$outputTfdRecordNum = $tfdLeft->getRecordNum();
		$outputTfdFieldNum = $tfdLeft->getVisibleFieldNum() + $tfdRight->getVisibleFieldNum();
	
		// 出力するTFDのTFDバリュー配列と、各レコードのレコードヒストリーを並べた配列（今から作っていく）
		$outputValueTable = array();
		$recordHistoryArray = array();

		for($i=0; $i<$outputTfdRecordNum; $i++) {
			// 左側のTFDレコードと右側のTFDレコードのセルを１つ１つ見ていき、TFDバリュー配列へ値を書き写していく	
			$outputValueTable[$i] = array();
			$leftTfdRecord = $tfdLeft->getRecord($i);
			for($offsetL=0; $offsetL<$tfdLeft->getVisibleFieldNum(); $offsetL++) {
				$cell = $leftTfdRecord->getCell($offsetL);
				$outputValueTable[$i][$offsetL] = $cell->value;
			}
			$rightTfdRecord = $tfdRight->getRecord($i);
			for($offsetR=0; $offsetR<$tfdRight->getVisibleFieldNum(); $offsetR++) {
				$cell = $rightTfdRecord->getCell($offsetR);
				$outputValueTable[$i][$tfdLeft->getVisibleFieldNum()+$offsetR] = $cell->value;
			}

			// このTFDレコードのレコードヒストリーの作成
			// まず左側のTFDレコード、右側のTFDレコードそれぞれのレコードヒストリーを統合したものを作る
			// 更に、そこに含まれる全レコードロケーションを、今のサービス実行のTPPNに対応した履歴として追加したものが、本TFDレコードのレコードヒストリーとなる
			$recordHistoryArray[$i] = RecordHistory::joinRecordHistory($leftTfdRecord->recordHistory, $rightTfdRecord->recordHistory);
			$recordHistoryArray[$i]->setHistory($this->tppn, $recordHistoryArray[$i]->getAllRecordLocations());
		}
	
		// 出力TFDのDataTypeインスタンス配列・Web表示名配列を作成
		$dataTypeArray_output = array();
		$webNameArray_output = array();
		for($offsetL=0; $offsetL<$tfdLeft->getVisibleFieldNum(); $offsetL++) {
			$dataTypeArray_output[$offsetL] = $dataTypeArrayLeft[$offsetL];
			$webNameArray_output[$offsetL] = $webNameArrayLeft[$offsetL];
		}
		for($offsetR=0; $offsetR<$tfdRight->getVisibleFieldNum(); $offsetR++) {
			$dataTypeArray_output[$tfdLeft->getVisibleFieldNum()+$offsetR] = $dataTypeArrayRight[$offsetR];
			$webNameArray_output[$tfdLeft->getVisibleFieldNum()+$offsetR] = $webNameArrayRight[$offsetR];
		}

		$tdf = new TabularFormData($outputValueTable, $recordHistoryArray, $dataTypeArray_output, $webNameArray_output);
		
		logg_debug_return(__FUNCTION__, "ServiceExecuter");
		return $tdf;
	}





	/*
	 * SUMMARY :
	 * サービス「ブランク表作成」
	 * 行数と列数を受け取り、ブランクの表を作成する
	 * 全セルの値はNullDataインスタンスであり、全フィールドの値はDataType::TYPE_BLANKである
	 * Web表示名配列を受け取るが、これの要素数は当然、受け取った列数と等しくなければならない
	 */
	public function service4($recordNumMonoArray, $fieldNumMonoArray, $webNameArray) {
		logg_debug_call(__FUNCTION__, "ServiceExecuter");
	
		$recordNum = $recordNumMonoArray[0];
		$fieldNum = $fieldNumMonoArray[0];

		// エラーチェック
		if($fieldNum!=count($webNameArray)) {
			logg_debug_error("ブランク表作成サービスにてエラーです。Web表示名配列の要素数（" . count($webNameArray) . "）と列数（" . $fieldNum . "）が等しくありません。", "ServiceExecuter", __FUNCTION__);
		}

		$outputValueTable = array();
		$recordHistoryArray = array();

		// 指定された行数だけ繰り返し
		for($i=0; $i<$recordNum; $i++) {
			$outputValueTable[$i] = array();

			// レコードヒストリーは空
			$recordHistoryArray[$i] = new RecordHistory();
	
			// 指定された列数だけ繰り返し
			for($offset=0; $offset<$fieldNum; $offset++) {
				// このTFDセルの値として、NullDataインスタンスをTFDバリュー配列に格納 
				$outputValueTable[$i][$offset] = new NullData();
			}
		}
	
		// 出力TFDのDataTypeインスタンス配列・Web表示名配列を作成
		$dataTypeArray_output = array();
		for($j=0; $j<$fieldNum; $j++) {
			$dataTypeArray_output[$j] = new DataType(DataType::TYPE_BLANK, null, null);
		}
		$webNameArray_output = $webNameArray;

		$tdf = new TabularFormData($outputValueTable, $recordHistoryArray, $dataTypeArray_output, $webNameArray_output);
		
		logg_debug_return(__FUNCTION__, "ServiceExecuter");
		return $tdf;
	}
}

?>
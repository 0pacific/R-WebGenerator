<?php
/*
 * SUMMARY :
 * Updateリフレクションを実行するクラス
 * 
 * Created on 2010/11/12
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

 class UpdateReflectionExecuter {
	var $tfd;

	// TableInformationインスタンス（Update対象のテーブルに関する情報）
	var $tableInformation;

	var $offsetRelation = array();




	
 	function __construct($tfd, $tableNumber) {
 		$this->tfd = $tfd;
		$this->tableInformation = new TableInformation($tableNumber);
 	}





	/*
	 * SUMMARY :
	 * 「TFDのこのフィールドでDeveloper Defined Tableのこのフィールドを更新する」という対応を１つ追加する
	 */
	function addOffsetRelation($tfdOffset, $tableOffset) {
		$this->offsetRelation[$tfdOffset] = $tableOffset;
	}





	/*
	 * SUMMARY :
	 * ページエレメント「更新フォーム」の主キーからそのページエレメントを割り出し、
	 * そこから送信された全リクエストを受信してTFDに変換する（このTFDは後でexecute()に渡し更新を行う）
	 */
	public static function convUpdFormToTfd($pePrimKey) {
		$query = "select page_element_update_form.tblNumber from page_element join page_element_update_form where "
					. "page_element." . PRIMARY_KEY . "=" . $pePrimKey
					. " and page_element." . PRIMARY_KEY . "=page_element_update_form.pePrimaryKey"
					. " and peKind='Update Form'";
		$result = mysql_query_logg($query);
		oneResultCheck($result, "UpdateReflectionExecuter", __FUNCTION__);

		// 何番テーブルの更新フォームなのか
		$tblNumber = mysql_result($result, 0, "tblNumber");

		$tblInfo = new TableInformation($tblNumber);

		// Developer Defined Table 全体の結果セットを取得
		$queryDdt = "select * from " . $tblInfo->nameOnDb;
		$resultDdt = mysql_query_logg($queryDdt);


		// DataTypeインスタンス配列とWeb表示名配列の初期化
		$dataTypeArray = array();
		$webNameArray = array();
		for($i=0; $i<$tblInfo->getDdfNum(); $i++) {
			$ddfInfo = $tblInfo->fieldInfoArray[$i];

			// $dataTypeArray初期化
			$dataTypeName = $ddfInfo->dataTypeName;
			$min = $ddfInfo->min;
			$max = $ddfInfo->max;
			$dataTypeArray[$i] = new DataType($dataTypeName, $min, $max);

			// $webNameArray初期化
			$webNameArray[$i] = $ddfInfo->nameOnWeb;
		}


		// DDTの全セル（ただしDDFのみ）について、リクエストが送られていないかチェックする
		// TFDの値テーブルとして使う $valueTable の対応セルには、
		// リクエストが送られていればその値をセット、送られていなければNoUpdateインスタンスをセット
		$valueTable = array();
		$curRecordNum = mysql_num_rows($resultDdt);
		for($i=0; $i<$curRecordNum; $i++) {
			// DDT中の上から$i番目のレコードの主キー
			$recordPrimKey = mysql_result($resultDdt, $i, PRIMARY_KEY);

			$valueTable[$i] = array();
			for($ddfOffset=0; $ddfOffset<$tblInfo->getDdfNum(); $ddfOffset++) {
				logg_debug("テーブル「".$tblInfo->nameOnWeb."」の、主キー".$recordPrimKey."のレコードの".$ddfOffset."番セルの更新値がPOSTされていないか調べます。", "UpdateReflectionExecuter", __FUNCTION__);

				// このセルが属するフィールドの情報
				$fieldInfo = $tblInfo->fieldInfoArray[$ddfOffset];

				// このセルのデータタイプ
				$dataTypeName = $fieldInfo->dataTypeName;

				$cellReqName = UpdateFormGenerator::makeCellReqName($pePrimKey, $tblNumber, $ddfOffset, $recordPrimKey);

				// このセルがPASSWORD型の場合
				if($dataTypeName===DataType::TYPE_PASSWORD) {
					// 現在セルに格納されているパスワード（暗号化状態）を取得
					$queryCurPass = "select * from " . $tblInfo->nameOnDb . " where " . PRIMARY_KEY . "=" . $recordPrimKey;
					$resultCurPass = mysql_query_logg($queryCurPass);
					oneResultCheck($resultCurPass, "UpdateReflectioniExecuter", __FUNCTION__);
					$curEncPass = mysql_result($resultCurPass, 0, $ddfOffset);


					// 「新しいパスワード」と「その確認入力」がともにリクエストとして来ている -> 一致していれば更新値として認め、不一致ならエラーを返却
					if(array_key_exists($cellReqName, $_POST) && array_key_exists($cellReqName."_confirm", $_POST)) {
						// 「現在のパスワードの入力が求められるケースか」つまり「アクセスユーザのパスワードを変更しようとしているのか」を調べる
						// $_POSTに「現在のパスワード」としてのリクエストが来ているならそうだと判断する（◆本当はそれじゃだめ！）
						if(array_key_exists($cellReqName."_current", $_POST)) {
							$reqCurPass = $_POST[$cellReqName."_current"];
							if(md5($reqCurPass)!==$curEncPass) {
								$error = new CurrentPasswordMismatchError($fieldInfo->nameOnWeb);
								return $error;
							}
						}

						$reqPass = $_POST[$cellReqName];
						$reqPassConfirm = $_POST[$cellReqName."_confirm"];
						if($reqPass!==$reqPassConfirm) {
							$error = new PasswordDiscordError($tblInfo->fieldInfoArray[$ddfOffset]->nameOnWeb);
							return $error;
						}

						$valueTable[$i][$ddfOffset] = $reqPass;
					}
					// そうでない -> このセルは更新しないと判断する
					else {
						$valueTable[$i][$ddfOffset] = new NoUpdate();
					}
				}
				// それ以外で、$_POSTにこのセルのリクエストが来ている場合 -> 少なくともFILE型ではない
				else if(array_key_exists($cellReqName, $_POST)) {
					logg_debug("このセルに対する更新値として、単一リクエストデータタイプの値が来ているようです。リクエスト名：".$cellReqName, "UpdateReflectionExecuter", __FUNCTION__);

					// NullDataインスタンスを格納するべきケース
					if($dataTypeName===DataType::TYPE_INT && $_POST[$cellReqName]==="") {
						$valueTable[$i][$ddfOffset] = new NullData();
					}
					// リクエストの値をそのままセットするケース
					else {
						$valueTable[$i][$ddfOffset] = $_POST[$cellReqName];
					}
				}
				// $_FILESにこのセルのリクエストが来ている場合 -> このセルはFILE型のはず
				// 注：ファイルが選択されていてもいなくても、ファイルのフォームさえあれば$_FILESへリクエストは入る
				else if(array_key_exists($cellReqName, $_FILES)) {
					logg_debug("このセルに対する更新値として、FILE型の値が来ているようです。（ファイルが選択されているとは限りませんよ）リクエスト名：".$cellReqName, "UpdateReflectionExecuter", __FUNCTION__);

					// 一時アップロードファイルのパス
					$tempFilePath = $_FILES[$cellReqName]['tmp_name'];

					// 一時アップロードファイルのパスが空文字列 -> ファイルが選択されていないので、TFDのこのセルの部分にはNoUpdateインスタンスを格納して次のセルへ
					if($tempFilePath==='') {
						$valueTable[$i][$ddfOffset] = new NoUpdate();
						continue;
					}

					// 一時アップロードファイルの名前
					$fileName = $_FILES[$cellReqName]['name'];
					logg_debug("一時的にアップロードされたファイルのパス：".$tempFilePath, "UpdateReflectionExecuter", __FUNCTION__);

					// DDT内でのこのセルの位置を示す、CellLocationインスタンス
					$cellLoc = new CellLocation($tblNumber, $recordPrimKey, $ddfOffset);

					// FileValueインスタンス（一時アップロードファイルのパスを記録しておく）
					$fileValue = new FileValue($fileName, $cellLoc);
					$fileValue->setTempFilePath($tempFilePath);

					// FileValueインスタンスを、TFDの値テーブル $valueTable のセルにセット
					$valueTable[$i][$ddfOffset] = $fileValue;
				}
				/*
				 * ◆未実装
				 * すべてのデータタイプについて処理を記述（DATETIMEなど）
				 */

				// 後は、下記のケースと考えられるので、NoUpdateインスタンスを格納する
				// ・Write権限もExWrite権限もないためこのセルの更新フォームが与えられなかった
				// 
				else {
					logg_debug("このセルに対する更新値はありませんでした。Write権限もExWrite権限もないために更新フォームが与えられなかったものと判断します。", "UpdateReflectionExecuter", __FUNCTION__);
					$valueTable[$i][$ddfOffset] = new NoUpdate();
				}
			}
		}
		
		// ◆レコードヒストリー配列は、とりあえず「履歴を全く持たないRecordHistoryインスタンス」を並べた表にしておく
		// Updateならこれでいいと思う
		$recordHistoryArray = array();
		for($i=0; $i<$curRecordNum; $i++) {
			$recordHistoryArray[$i] = new RecordHistory();
		}

		$tabularFormData = new TabularFormData($valueTable, $recordHistoryArray, $dataTypeArray, $webNameArray);
		debug_varDump(__FUNCTION__, "\$tabularFormData", $tabularFormData);
		return $tabularFormData;
	}





	/*
	 * SUMMARY :
	 * 更新を実行する
	 * 
	 * NOTICE :
	 * 現在の仕様・・・TFDの上から１，２、・・・番目でDeveloper Defined Tableの１，２、・・・番目を更新するというもの
	 * 本来の仕様・・・どのTPPに対応したレコードを更新するのか指定できねばならない
	 */
	function execute() {
		$tblInfo = $this->tableInformation;

		// トランザクション開始
		$querySt = "start transaction";
		mysql_query_logg($querySt);

		// TFDの各レコードを見ていく
		for($recordOrder=0; $recordOrder<$this->tfd->getRecordNum(); $recordOrder++) {
			// TabularFormDataRecordインスタンス
			$record = $this->tfd->getRecord($recordOrder);

			$query = "update " . $tblInfo->nameOnDb . " set ";

			// Developer Defined Tableの上から$recordOrder番目のレコードの主キーを取得しておく
			$queryS = "select * from $tblInfo->nameOnDb";
			$resultS = mysql_query_logg($queryS);
			$ddtRecordPrimKey = mysql_result($resultS, $recordOrder, PRIMARY_KEY);

			// 各セル
			foreach($this->offsetRelation as $tfdOffset => $ddtOffset) {
				$cell = $record->getCell($tfdOffset);
				$cellValue = $cell->value;

				// このセルが属するフィールドの値で、DDTの対応フィールドを更新する必要がある場合
				if(array_key_exists($tfdOffset, $this->offsetRelation)) {
					logg_debug("DDT「" . $tblInfo->nameOnWeb . "」の[$recordOrder, $ddtOffset]のセルを更新するPOSTリクエストがあるか調べます。");

					// TFDのセルにNoUpdateインスタンスが入っている -> DDTの対応するセルを更新する必要なし
					if($cellValue instanceof NoUpdate) {
						logg_debug("TFDの[$recordOrder, $tfdOffset]の値はNoUpdateインスタンスなので、更新不要と判断します。");
						continue;
					}

					logg_debug("更新が必要のようです。");
					
					// Updateするフィールドの、DB上の名前
					$fieldName = $tblInfo->fieldInfoArray[$ddtOffset]->nameOnDb;
					
					// このセルだけを更新するクエリを作成していく
					// ◆未実装　各データタイプに応じて適切な更新のしかたをするべき
					$query = "update $tblInfo->nameOnDb set $fieldName=";
					$fieldInfo = $tblInfo->fieldInfoArray[$ddtOffset];
					// INT
					if($fieldInfo->dataTypeName===DataType::TYPE_INT) {
						logg_debug("更新に用いるデータ（これから書き込むデータ）を並べたTFDの中に、INT型のセルを見つけました", "UpdateReflectionExecuter", __FUNCTION__);
						// 空列が送られてきた -> NULL
						if($cellValue instanceof NullData) {
							$query .= "null";
						}
						// 値が送られてきた -> その値
						else {
							$query .= $cellValue;
						}
					}
					// VARCHARその他
					else if($fieldInfo->dataTypeName===DataType::TYPE_VARCHAR
							|| $fieldInfo->dataTypeName===DataType::TYPE_DATETIME
							|| $fieldInfo->dataTypeName===DataType::TYPE_DATE
							|| $fieldInfo->dataTypeName===DataType::TYPE_TIME) {
						logg_debug("更新に用いるデータ（これから書き込むデータ）を並べたTFDの中に、VARCHAR型がそれに類する型のセルを見つけました", "UpdateReflectionExecuter", __FUNCTION__);
						$query .= "'" . $cellValue . "'";
					}
					// PASSWORD
					else if($fieldInfo->dataTypeName===DataType::TYPE_PASSWORD) {
						$query .= "md5('" .$cellValue . "')";
					}
					// FILE
					else if($fieldInfo->dataTypeName===DataType::TYPE_FILE) {
						logg_debug("更新に用いるデータ（これから書き込むデータ）を並べたTFDの中に、FILE型のセルを見つけました", "UpdateReflectionExecuter", __FUNCTION__);

						// TFDの方のセルには、値としてFileValueインスタンスが格納されているはず
						$fileValue = $cellValue;

						// エラー（そうなっていない）
						if(!($fileValue instanceof FileValue)) {
							logg_debug_error("DDTのFILE型のセルを上書きしようとしていますが、上書きに使うTFDの対応セルの値がFileValueインスタンスではありません。", "ReflectioinExecuterUpdate", __FUNCTION__);
						}

						$query .= "'" . $fileValue->fileName . "'";
					}
					// エラー
					else {
						logg_debug_error("1203:想定外のデータタイプです。", "UpdateReflectionExecuter", __FUNCTION__);
					}


					$query .= " where " . PRIMARY_KEY . "=" . $ddtRecordPrimKey;

					// このセルだけを更新するクエリを実行
					$updateResult = mysql_query_logg($query);

					// 更新成功 -> もしFILE型なら、続けて一時アップロードファイルを正規の場所へコピー
					if($updateResult===TRUE) {
						if($fieldInfo->dataTypeName===DataType::TYPE_FILE) {
							$fileValue = $cellValue;
							$cellLoc = $fileValue->cellLocation;

							// TFDセル中のFileValueインスタンスに入っている、一時アップロードファイルのパスと名前
							$tempFilePath = $fileValue->tempFilePath;
							$fileName = $fileValue->fileName;

							// ファイルを格納すべきディレクトリが存在しないなら作る
							$fileValue->makeDirectoryIfNotExists();

							// これから作ろうとするファイルのパス
							$newFilePath = $fileValue->getFilePath();

							// 一時アップロードファイルをそこへコピー
							$resultFlUp = move_uploaded_file($tempFilePath, $newFilePath);
							if($resultFlUp===true) {
								logg_debug("セルへのファイルのアップロードが完了しました（一時ファイルを正規の場所へコピーできました");

								/*
								 * ◆未実装：更に、それまでDDT中のこのセルに入っていたファイルを削除する必要がある
								 */
							}
							// 一時アップロードファイルコピー失敗 -> ロールバックして関数を抜ける
							else {
								$queryRb = "rollback";
								mysql_query_logg($queryRb);

								logg_debug_error("セルへのファイルのアップロードに失敗したので、ロールバックしました。正規の場所へコピーされた他の一時アップロードファイルはそのままですよorz");
								return;
							}
						}
					}
					// 更新失敗 -> ロールバックして関数を抜ける
					else {
						$queryRb = "rollback";
						mysql_query_logg($queryRb);

						logg_debug_error("テーブルの更新に失敗したので、ロールバックします。正規の場所へコピーされた一時アップロードファイルはそのままですよorz", "UpdateReflectionExecuter", __FUNCTION__);
						return;
					}
				}
				else {
					logg_debug("TFDの[$recordOrder, $tfdOffset]のセルの値で、、DDT「" . $tblInfo->nameOnWeb . "」の[$recordOrder, $ddtOffset]のセルは更新しませんでした。");
				}
				// １セル分の更新作業が無事終了
			}
			// １レコード分の更新作業が無事終了
		}

		// 全レコード分の更新作業が無事終了したので、コミットする
		$queryCom = "commit";
		mysql_query_logg($queryCom);
	}
 }
?>

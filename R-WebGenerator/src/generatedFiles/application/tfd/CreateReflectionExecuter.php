<?php
/*
 * SUMMARY :
 * Createリフレクションを実行するクラス
 * 
 * Created on 2010/11/12
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 
 
 
 
class CreateReflectionExecuter {
	var $tfd;

	// TableInformationインスタンス（Create対象のテーブルに関する情報）
	var $tableInformation;




	
 	function __construct($tfd, $tableNumber) {
 		$this->tfd = $tfd;
		$this->tableInformation = new TableInformation($tableNumber);
 	}





	/*
	 * SUMMARY :
	 * ページエレメント「作成フォーム」の主キーからそのページエレメントを割り出し、
	 * そこから送信された全リクエストを受信してTFDに変換する（このTFDは後でexecute()に渡し作成を行う）
	 */
	public static function convCreFormToTfd($pePrimKey) {
		$query = "select * from page_element join page_element_create_form where "
					. "page_element." . PRIMARY_KEY . "=" . $pePrimKey
					. " and page_element." . PRIMARY_KEY . "=page_element_create_form.pePrimaryKey"
					. " and peKind='Create Form'";
		$result = mysql_query_logg($query);
		oneResultCheck($result, "CreateReflectionExecuter", __FUNCTION__);

		// 何番テーブルの作成フォームなのか
		$tblNumber = mysql_result($result, 0, "tableNumber");

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
		// リクエストが送られていればその値をセット、送られていなければエラー
		$valueTable = array();

		// 作成しようとしている１つ以上のレコードについて、（アカウントテーブルの場合は）アカウントオーナーロール選択プルダウンメニューからロール番号をリクエストとして受け取るが、
		// この配列にはそれらロール番号を順番に格納していく。
		// 最後にTabularFormDataインスタンスを作成した後、その中の各TabularFormDataRecordインスタンスのインスタンス変数$accOwnRoleNumberに、この配列の中身を順番にセットしていく
		// かなりややこしい実装だが・・・
		$accOwnRoleNumberArray = array();

		// ◆未実装：レコードがいくつ作られようとしているか調べる
		$newRecordNum = 1;

		for($newRecOrd=0; $newRecOrd<$newRecordNum; $newRecOrd++) {
			$valueTable[$newRecOrd] = array();

			// アカウントテーブルの場合 -> アカウントオーナーロール選択プルダウンメニューからのリクエスト（アカウントオーナーロール番号）を取得、配列に格納
			if($tblInfo->tableType==="account table") {
				$aorReqName = "accOwnRoleNumber_pePrimKeyIs" . $pePrimKey;
				if(!array_key_exists($aorReqName, $_POST)) {
					logg_debug_error("アカウントオーナーロール選択プルダウンメニューからのリクエストが存在しないようです。", "CreateReflectionExecuter", __FUNCTION__);
				}
				$accOwnRoleNumber = $_POST[$aorReqName][$newRecOrd];
				$accOwnRoleNumberArray[$newRecOrd] = $accOwnRoleNumber;
			}

			// 各DDFについて、リクエストが来てないか調べる
			for($ddfOffset=0; $ddfOffset<$tblInfo->getDdfNum(); $ddfOffset++) {
				logg_debug("テーブル「".$tblInfo->nameOnWeb."」の、新しく作られようとしている".$newRecOrd."個目（0始まり）のレコードの".$ddfOffset."番セルのリクエストを調べます。全DDF分来ているはずです", "CreateReflectionExecuter", __FUNCTION__);

				// このセルのデータタイプ名
				$ddfInfo = $tblInfo->fieldInfoArray[$ddfOffset];
				$dataTypeName = $ddfInfo->dataTypeName;

				$cellReqName = CreationFormMaker::makeCellReqName($pePrimKey, $tblNumber, $ddfOffset);

				// PASSWORD型
				if($dataTypeName===DataType::TYPE_PASSWORD) {
					logg_debug("PASSWORD型のセルに対する作成値がちゃんと来ているか調べます。", "CreateReflectionExecuter", __FUNCTION__);

					if(!array_key_exists($cellReqName, $_POST) || !array_key_exists($cellReqName."_confirm", $_POST)) {
						logg_debug_error("PASSWORD型のセルに対する２つの作成値（パスワードと確認入力）の片方ないし両方が存在しないようです。", "CreateReflectionExecuter", __FUNCTION__);
					}

					$pass = $_POST[$cellReqName][$newRecOrd];
					$passConf = $_POST[$cellReqName."_confirm"][$newRecOrd];


					// 確認入力したパスワードが不一致の場合 -> PasswordDiscordErrorインスタンス返却
					if($pass!==$passConf) {
						$error = new PasswordDiscordError($ddfInfo->nameOnWeb);
						logg_debug_return(__FUNCTION__, "CreateReflectionExecuter");
						return $error;
					}

					// 文字数が不正 -> LengthImproperErrorインスタンス返却
					$inputPassLen = strlen($pass);
					$passMin = $ddfInfo->min;
					$passMax = $ddfInfo->max;
					if($inputPassLen<$passMin || $inputPassLen>$passMax) {
						$error = new LengthImproperError($ddfInfo->nameOnWeb, $passMin, $passMax);
						logg_debug_return(__FUNCTION__, "CreateReflectionExecuter");
						return $error;
					}

					// 値をセット
					$valueTable[$newRecOrd][$ddfOffset] = $pass;
				}
				// ROLE_NAME型（アカウントテーブルの場合しかありえない）
				else if($dataTypeName===DataType::TYPE_ROLE_NAME) {
					$accOwnRoleInfo = new RoleInfo($accOwnRoleNumber);
					$accOwnRoleName = $accOwnRoleInfo->name;

					// 値をセット
					$valueTable[$newRecOrd][$ddfOffset] = $accOwnRoleName;
				}
				// $_POSTにこのセルのリクエストが来ている場合（このセルのデータタイプは単一リクエストデータタイプのはず）
				else if(array_key_exists($cellReqName, $_POST)) {
					logg_debug("このセルに対する作成値として、単一リクエストデータタイプの値が来ているようです。リクエスト名：".$cellReqName, "CreateReflectionExecuter", __FUNCTION__);

					// NullDataインスタンスを格納するべきケース
					if($dataTypeName===DataType::TYPE_INT && $_POST[$cellReqName][$newRecOrd]==="") {
						$valueTable[$newRecOrd][$ddfOffset] = new NullData();
					}
					// リクエストの値をそのままセットするケース
					else {
						$valueTable[$newRecOrd][$ddfOffset] = $_POST[$cellReqName][$newRecOrd];
					}
				}
				// $_FILESにこのセルのリクエストが来ている場合 -> このセルはFILE型のはず
				// 注：ファイルが選択されていてもいなくても、ファイルのフォームさえあれば$_FILESへリクエストは入る
				else if(array_key_exists($cellReqName, $_FILES)) {
					logg_debug("このセルに対する作成値として、FILE型の値が来ているようです。（ファイルが選択されているとは限りませんよ）リクエスト名：".$cellReqName, "CreateReflectionExecuter", __FUNCTION__);

					// 一時的にアップロードされたファイルのパス
					$tempFilePath = $_FILES[$cellReqName]['tmp_name'][$newRecOrd];

					// 空列 -> ファイルが選択されていないので、TFDのこのセルの部分にはNullDataインスタンスを格納して次のセルへ
					if($tempFilePath==='') {
						$valueTable[$newRecOrd][$ddfOffset] = new NullData();
						continue;
					}

					$fileName = $_FILES[$cellReqName]['name'][$newRecOrd];
					logg_debug("一時的にアップロードされたファイルのパス：".$tempFilePath, "CreateReflectionExecuter", __FUNCTION__);

					// DDT内でのこのセルの位置を示す、CellLocationインスタンス
					// ◆注：Createの場合はNULLとしている（まだ作られていないセルのセルロケーションなどわからないため。予測するのも危険であろう）
					$cellLoc = null;

					// FileValueインスタンス（一時的にアップロードされたファイルのパスを記録しておく）
					$fileValue = new FileValue($fileName, $cellLoc);
					$fileValue->setTempFilePath($tempFilePath);

					// FileValueインスタンスを、TFDの値テーブル $valueTable のセルにセット
					$valueTable[$newRecOrd][$ddfOffset] = $fileValue;
				}
				/*
				 * ◆未実装
				 * すべてのデータタイプについて処理を記述（DATETIMEなど）
				 */

				// エラー
				else {
					logg_debug_error("Createフォームからのリクエストに基づきTFDを作成していたところ、想定外のケースが発生。単一リクエストデータタイプ、FILE型、いずれのリクエストもキャッチできませんでした", "CreateReflectionExecuter", __FUNCTION__);
				}
			}

			$newRecOrd++;
		}
		
		// ◆レコードヒストリー配列は、とりあえず「履歴を全く持たないRecordHistoryインスタンス」を並べた表にしておく
		// Createならこれでいいと思う
		$recordHistoryArray = array();
		for($i=0; $i<$newRecordNum; $i++) {
			$recordHistoryArray[$i] = new RecordHistory();
		}

		// TFDのインスタンスを作成
		$tabularFormData = new TabularFormData($valueTable, $recordHistoryArray, $dataTypeArray, $webNameArray);

		// アカウントテーブルの場合には、アカウントオーナーロール番号を、配列$accOwnRoleNumberArrayからTFDの各TabularFormDataRecordインスタンスへ書き写す
		if($tblInfo->tableType==="account table") {
			for($i=0; $i<$newRecordNum; $i++) {
				$tfdRecord = $tabularFormData->getRecord($i);
				$tfdRecord->setAccOwnRoleNumber($accOwnRoleNumberArray[$i]);
			}
		}

		debug_varDump(__FUNCTION__, "\$tabularFormData", $tabularFormData);
		return $tabularFormData;
	}





	/*
	 * SUMMARY :
	 * レコード作成を実行し、作成した全レコードの主キーを配列にして返却する
	 */
	function execute() {
		$tblInfo = $this->tableInformation;

		// 作成されたレコードの主キーをこの配列に格納していき、最後に返却する
		$createdRecPrimKeyArray = array();

		// トランザクション開始
		$querySt = "start transaction";
		mysql_query_logg($querySt);

		// TFDの各レコードを見ていく
		for($recordOrder=0; $recordOrder<$this->tfd->getRecordNum(); $recordOrder++) {
			// TabularFormDataRecordインスタンス
			$record = $this->tfd->getRecord($recordOrder);

			$queryFldNames = "(";
			$queryValues = "values (";

			/*
			 * ◆TFDのフィールド数とDDTのDDF数が一致しているかチェックした方がいいだろう
			 */

			for($tfdOffset=0; $tfdOffset<$tblInfo->getDdfNum(); $tfdOffset++) {
				// TFD中のセルの値を取り出す
				$cell = $record->getCell($tfdOffset);
				$cellValue = $cell->value;

				// このセルのフィールドの、DB上の名前
				$fieldInfo = $tblInfo->fieldInfoArray[$tfdOffset];
				$fieldName = $tblInfo->fieldInfoArray[$tfdOffset]->nameOnDb;

				$queryFldNames .= $fieldName . ",";

				// TFDのセルにNullDataインスタンスが入っている -> 値としてNULLを指定して次のセルへ
				if($cellValue instanceof NullData) {
					$queryValues .= "NULL,";
					continue;
				}

				
				/*
				 * ◆未実装　各データタイプに応じて適切な更新のしかたをするべき
				 */
				// INT
				if($fieldInfo->dataTypeName===DataType::TYPE_INT) {
					$queryValues .= $cellValue;
				}
				// VARCHARその他
				else if($fieldInfo->dataTypeName===DataType::TYPE_VARCHAR
						|| $fieldInfo->dataTypeName===DataType::TYPE_DATETIME
						|| $fieldInfo->dataTypeName===DataType::TYPE_DATE
						|| $fieldInfo->dataTypeName===DataType::TYPE_TIME
						|| $fieldInfo->dataTypeName===DataType::TYPE_ENUM
						|| $fieldInfo->dataTypeName===DataType::TYPE_MAIL
						|| $fieldInfo->dataTypeName===DataType::TYPE_MAILAUTH
						|| $fieldInfo->dataTypeName===DataType::TYPE_USERID
						|| $fieldInfo->dataTypeName===DataType::TYPE_ROLE_NAME) {
					$queryValues .= "'" . mysql_real_escape_string($cellValue) . "'";
				}
				// PASSWORD
				else if($fieldInfo->dataTypeName===DataType::TYPE_PASSWORD) {
					$queryValues .= "md5('" . mysql_real_escape_string($cellValue) . "')";
				}
				// FILE
				else if($fieldInfo->dataTypeName===DataType::TYPE_FILE) {
					// TFDの方のセルには、値としてFileValueインスタンスが格納されているはず
					$fileValue = $cellValue;

					// エラー（そうなっていない）
					if(!($fileValue instanceof FileValue)) {
						logg_debug_error("TFDを基にCreateをしようとしていますが、FILE型DDFに対応したTFDセルの値がFileValueインスタンスではありません。", "ReflectioinExecuterCreate", __FUNCTION__);
					}

					$queryValues .= "'" . $fileValue->fileName . "'";
				}

				$queryValues .= ",";
			}

			// アカウントテーブルの場合は、アカウントオーナーロール番号フィールドの値もクエリに含む必要がある
			if($this->tableInformation->tableType==="account table") {
				$queryFldNames .= "roleNumber,";
				$queryValues .= $record->accOwnRoleNumber . ",";
			}

			// 最後の","をカットし、")"を付け加える
			$queryFldNames = substr($queryFldNames, 0, -1);
			$queryFldNames .= ")";
			$queryValues = substr($queryValues, 0, -1);
			$queryValues .= ")";

			// １レコード分のCreateを実行
			$queryCre = "insert into " . $tblInfo->nameOnDb . " " . $queryFldNames . " " . $queryValues;
			$resultCre = mysql_query_logg($queryCre);

			// Create成功
			if($resultCre===TRUE) {
				// Createしたレコードの主キーを取得、配列の要素として格納
				$queryId = "select last_insert_id()";
				$resultId = mysql_query_logg($queryId);
				$lastInsertId = mysql_result($resultId, 0, 0);

				logg_debug("last_insert_id を取得...値は".$lastInsertId."です。", get_class($this), __FUNCTION__);

				$createdRecPrimKeyArray[$recordOrder] = $lastInsertId;

				/*
				 * ◆未実装：全一時アップロードファイルを正規の場所へコピーする
				 */

				/*
				if($fieldInfo->dataTypeName===DataType::TYPE_FILE) {
					$fileValue = $cellValue;
					$cellLoc = $fileValue->cellLocation;

					$tempFilePath = $fileValue->tempFilePath;
					$fileName = $fileValue->fileName;
					$filePath = DIRECTORY_FILES
								. "table_" . $cellLoc->tableNumber
								. "/primKey_" . $cellLoc->primaryKey
								. "/offset_" . $cellLoc->offset
								. "/".$fileName;
					$resultFlUp = move_uploaded_file($tempFilePath, $filePath);
					if($resultFlUp===true) {
						logg_debug("セルへのファイルのアップロードが完了しました（一時ファイルを正規の場所へコピーできました");
					}
					// 一時アップロードファイルコピー失敗 -> ロールバックして関数を抜ける
					else {
						$queryRb = "rollback";
						mysql_query_logg($queryRb);

						logg_debug_error("セルへのファイルのアップロードに失敗したので、ロールバックしました。正規の場所へコピーされた他の一時アップロードファイルはそのままですよorz");
						return;
					}
				}
				*/

				// アカウントテーブルの場合には、今アカウントを１つ作成したわけなので、IAのアサインを同時に行う
				if($tblInfo->tableType==="account table") {
					// たった今作成した１アカウントのレコードを結果セットとして取得
					$queryFindAcc = "select * from " . $tblInfo->nameOnDb . " where " . PRIMARY_KEY . "=" . $lastInsertId;
					$resultFindAcc = mysql_query_logg($queryFindAcc);
					oneResultCheck($resultFindAcc, "CreateReflectionExecuter", __FUNCTION__);

					// たった今作成したアカウントの持ち主の、ロール番号とユーザ番号（ユーザ番号＝このアカウントの主キー）
					$thisAccRoleNumber = mysql_result($resultFindAcc, 0, "roleNumber");
					$thisAccUserNumber = mysql_result($resultFindAcc, 0, PRIMARY_KEY);

					// IaAssignerインスタンスにIAのアサインを行ってもらう（ユーザ番号と主キーは同一となる）
					$iaAssigner = new IaAssigner($thisAccRoleNumber, $thisAccUserNumber, $tblInfo->tableNumber, $thisAccUserNumber);
					$iaAssigner->assign();
				}
			}
			// Create失敗 -> ロールバックして関数を抜ける
			else {
				$queryRb = "rollback";
				mysql_query_logg($queryRb);

				logg_debug_error("レコードのCreateに失敗したので、ロールバックします。正規の場所へコピーされた一時アップロードファイルはそのままですよorz", "CreateReflectionExecuter", __FUNCTION__);
				$error = new CreationFailureError();
				return $error;
			}
		}

		// 全レコードの作成が無事終了したので、コミットする
		$queryCom = "commit";
		mysql_query_logg($queryCom);

		return $createdRecPrimKeyArray;
	}
}
?>

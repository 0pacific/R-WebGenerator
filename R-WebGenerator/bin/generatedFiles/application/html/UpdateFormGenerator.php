<?php
/*
 * Created on 2010/11/16
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */


/*
 * Read, Write, ExWrite権限の真偽の組み合わせを表す定数
 * 組み合わせによってセルの中のHTMLを作りわける処理があるので、どんな組み合わせか簡単にわかるようにこれら定数を設けている
 */
define("AUTH_NNN", 0);
define("AUTH_NNE", 1);
define("AUTH_NWN", 2);
define("AUTH_NWE", 3);
define("AUTH_RNN", 4);
define("AUTH_RNE", 5);
define("AUTH_RWN", 6);
define("AUTH_RWE", 7);





class UpdateFormGenerator {
	var $clientInfo;
	var $pePrimKey;
	var $destPageNumber;

	// 更新対象のDeveloperDefinedTableの番号
	var $tableNumber;

	// フィールド限定で選ばれているDDFそれぞれのオフセットを並べた配列
	var $offsetArray;




	public function __construct($clientInfo, $pePrimKey, $destPageNumber, $offsetArray) {
		$this->clientInfo = $clientInfo;
		$this->pePrimKey = $pePrimKey;
		$this->destPageNumber = $destPageNumber;

		$this->offsetArray = $offsetArray;

		$query = "select * from page_element_update_form where pePrimaryKey=" . $this->pePrimKey;
		$result = mysql_query_logg($query);
		oneResultCheck($result, "UpdateFormGenerator", __FUNCTION__);
		$this->tableNumber = mysql_result($result, 0, "tblNumber");
	}





	public function generateTableHtml() {
		$tableInfo = new TableInformation($this->tableNumber);
		$tableDbName = $tableInfo->nameOnDb;

		// Developer Defined Table 全体（RDF含む）をSELECTで取得
		$querySelect = "select * from " . $tableDbName;
		$resultSelect = mysql_query_logg($querySelect);

		// レコード数、Developer Defined Field数
		$recordNum = mysql_num_rows($resultSelect);
		$ddfNum = $tableInfo->getDdfNum();

		// 遷移先ページのファイル名を割り出す
		$fpm = new FilePathManager();
		$destPageFileName = $fpm->getPageFileName($this->destPageNumber);

		$html = "";

		$html .= "<table border='1'>\n";
		
		
		// カラム名の部分
		$html .= "<tr>\n";
		for($j=0; $j<$ddfNum; $j++) {
			// フィールド限定によって排除されているフィールド -> 無視
			if(!in_array($j, $this->offsetArray)) {
				continue;
			}

			// セルが属すフィールドの情報
			$fieldInfo = $tableInfo->fieldInfoArray[$j];

			// ROLE_NAME型フィールドはUpdate不可能なので無視
			if($fieldInfo->dataTypeName===DataType::TYPE_ROLE_NAME) {
				continue;
			}

			$html .= "<td>" . $fieldInfo->nameOnWeb . "</td>\n";
		}
		$html .= "</tr>\n";


		$authGetter = new AuthorityGetter(new clientInfo(), $this->tableNumber);
		$tableAuth = $authGetter->getAuthToTable();

		// 結果セットから各セルの値を取り出し、各セルのHTMLを作成していく
		for($i=0; $i<$recordNum; $i++) {
			$recordHtml = "<tr>\n";

			// HTMLテーブルの、このレコードに対応した行が、全セル空欄（全角スペース１つ）となるかどうかチェックするための変数
			// この変数には最終的に、全セル空欄である場合の１行分のHTMLを格納する
			$blankRecCheckHtml = "<tr>\n";

			// このレコードの主キー
			$recordPrimKey = mysql_result($resultSelect, $i, PRIMARY_KEY);

			for($ddfOffset=0; $ddfOffset<$ddfNum; $ddfOffset++) {
				// フィールド限定によって排除されているフィールド -> 無視
				if(!in_array($ddfOffset, $this->offsetArray)) {
					continue;
				}

				// このセルが属すフィールドの情報
				$fieldInfo = $tableInfo->fieldInfoArray[$ddfOffset];

				// ROLE_NAME型フィールドはUpdate不可能なので無視
				if($fieldInfo->dataTypeName===DataType::TYPE_ROLE_NAME) {
					continue;
				}

				$recordHtml .= "<td>";

				// ◆未実装　ExWriteロックされているかどうか、されているなら自分かも調べること

				// このセルのセルロケーション
				$cellLoc = new CellLocation($this->tableNumber, $recordPrimKey, $ddfOffset);

				// このセルの現在の値（FILE型でかつファイルが格納されている場合は、FileValueインスタンスとなる
				$curValue = mysql_result($resultSelect, $i, $ddfOffset);
				if($fieldInfo->dataTypeName===DataType::TYPE_FILE) {
					$curValue = new FileValue($curValue, $cellLoc);
				}

				// このセルに対するアクセスユーザのフィールド権限セットを取得
				$cellAuthGetter = new CellAuthGetter($this->clientInfo);
				$fieldAuthSet = $cellAuthGetter->check($cellLoc);

				// フィールド権限セットをからRead, Write, ExWrite 権限の真偽を調べ、それぞれの真偽の組み合わせに対応した定数を取得
				// （AUTH_NNN～AUTH_RWEの８つのどれか）
				$fieldAuthSetConst = $this->convRweToConst($fieldAuthSet->readAuth, $fieldAuthSet->writeAuth, $fieldAuthSet->exWriteAuth);

				// <td>と</td>の間のHTML作成
				$recordHtml .= $this->generateCellHtml($fieldInfo, $curValue, $recordPrimKey, $fieldAuthSetConst);

				$recordHtml .= "</td>\n";
				$blankRecCheckHtml .= "<td>　</td>\n";
			}

			$recordHtml .= "</tr>\n";
			$blankRecCheckHtml .= "</tr>\n";

			if($recordHtml===$blankRecCheckHtml) {
				logg_debug("このレコードは全セルが空欄なので、更新フォームを表示しません", "UpdateFormGenerator", __FUNCTION__);
				$recordHtml = "";
			}

			// １レコード分のHTMLができあがったので、付け足す（<tr>～</tr>かもしれないし、空列かもしれない）
			$html .= $recordHtml;
		}
		
		$html .= "</table>\n"
					. "<br>\n";

		// 遷移ボタン
		$action = $destPageFileName."?pepk=".$this->pePrimKey;
		$javaScriptCode = "document.PageElementsData.action=\"".htmlspecialchars($action)."\";"
							. "document.PageElementsData.submit();";
		if(JAPANESE)
			$html .= "<input type='button' onClick='".$javaScriptCode."' value='以上の内容で更新を行う'>";
		else
			$html .= "<input type='button' onClick='".$javaScriptCode."' value='Update'>";
		
		return $html;
	}




	/*
	 * SUMMARY :
	 * １つのセルのUpdateフォームのHTMLを生成、返却する（<td>と</td>の間のHTML）
	 * 
	 * ARGUMENT :
	 * $fieldInfo : セルが属すフィールドの情報を持ったFieldInformationインスタンス
	 * $curValue : Developer Defined Table のセルに現在書き込まれている値（データベースから取り出した値をそのまま渡す）
	 * $rweAuth : アクセスユーザのR,W,E権限（上で定義されている８通りの定数を渡す）
	 */
	public function generateCellHtml($fieldInfo, $curValue, $recordPrimKey, $rweAuth) {
		$dataTypeName = $fieldInfo->dataTypeName;

		// Read, Write, ExWrite いずれも不可な場合 -> 空欄（全角スペース１つ）を返す
		if($rweAuth===AUTH_NNN) {
			return "　";
		}

		// Readしかできない場合
		// -> 普通に値を表示する。つまりTFDのセルの値を表示する場合と同じようにすればよいので、
		// TfdHtmlCreatorクラスに現在の値とデータタイプを投げ、セルのHTMLを作ってもらえばよい
		if($rweAuth===AUTH_RNN) {
			$cellReadHtml = null;

			// FILE型のセルでかつ現在ファイルが格納されている（現在の値としてFileValueインスタンスが渡されてきた）場合は、
			// FileValueインスタンスからセルロケーションを取り出しておく
			if($dataTypeName===DataType::TYPE_FILE && $curValue instanceof FileValue) {
				$cellLoc = $curValue->cellLocation;
				$cellReadHtml = TfdHtmlCreator::makeCellHtml($curValue, $dataTypeName, $cellLoc);
			}
			else {
				$cellReadHtml = TfdHtmlCreator::makeCellHtml($curValue, $dataTypeName, null); 
			}
			
			return $cellReadHtml;
		}



		/*
		 * ここから下は、WriteとExWriteの少なくとも一方が可能 -> よってこのセルを更新するHTMLを作成する
		 */
		$html = "";



		/*
		 * ◆未実装　ExWriteのロックのチェックボックスのHTMLを作る
		 */

		// 「値の書換えが可能でかつ現在の値が読める」かどうか
		$readableUpdate = ($rweAuth===AUTH_RWN || $rweAuth===AUTH_RWE || $rweAuth===AUTH_RNE);


		/*
		 * ◆未実装　すべてのデータタイプについて記述せねばならない
		 */
		$tableInfo = new TableInformation($this->tableNumber);
		$cellReqName = UpdateFormGenerator::makeCellReqName($this->pePrimKey, $this->tableNumber, $fieldInfo->offset, $recordPrimKey);

		// INT
		if($dataTypeName===DataType::TYPE_INT) {
			$html .= "<input type='text' size='4' name='" . $cellReqName . "' value='";
			if($readableUpdate) {
				$html .= $curValue;
			}
			$html .= "'>";
		}
		// VARCHAR
		else if($dataTypeName===DataType::TYPE_VARCHAR) {
			$html .= "<input type='text' size='" . $fieldInfo->max . "' name='" . $cellReqName . "' value='";
			if($readableUpdate) {
				$html .= $curValue;
			}
			$html .= "'>";
		}
		// FILE
		else if($dataTypeName===DataType::TYPE_FILE) {
			$html .= "<input type='file' name='" . $cellReqName . "'>";

			/*
			 * ◆未実装　ファイル削除のチェックボックスを設ける
			 */
		}
		// PASSWORD（ちょっとややこしいぞ）
		else if($dataTypeName===DataType::TYPE_PASSWORD) {
			/*
			 * このPASSWORD型セルが、アクセスユーザのアカウント中のものならば、現在のパスワードも入力させる必要がある
			 */

			// このPASSWORD型セルが含まれているアカウント（レコード）を結果セットとして取得
			$queryFindAcc = "select * from " . $tableInfo->nameOnDb . " where " . PRIMARY_KEY . "=" . $recordPrimKey;
			$resultFindAcc = mysql_query_logg($queryFindAcc);
			oneResultCheck($resultFindAcc, "UpdateFormGenerator", __FUNCTION__);

			// アカウントの持ち主の、ロール番号とユーザ番号を割り出す
			$accRoleNumber = mysql_result($resultFindAcc, 0, "roleNumber");
			$accUserNumber = mysql_result($resultFindAcc, 0, PRIMARY_KEY);		// もっとも、$recordPrimKeyと等しいが

			// 持ち主のロール番号・ユーザ番号ともにアクセスユーザのそれと一致した場合
			// -> アクセスユーザが自分のパスワードを変更しようとしているので、現在のパスワードも入力させる
			if($accRoleNumber===$this->clientInfo->roleNumber && $accUserNumber===$this->clientInfo->userNumber) {
				$html .= "現在のパスワード：<br>\n"
							. "<input type='password' name='" . $cellReqName . "_current'><br>\n";
			}

			$html .= "新しいパスワード：<br>\n"
						. "<input type='password' name='" . $cellReqName . "'><br>\n"
						. "確認のため再入力<br>\n"
						. "<input type='password' name='" . $cellReqName . "_confirm'><br>\n";
		}

		return $html;
	}





	/*
	 * SUMMARY :
	 * Read・Write・ExWrite の３つの権限を受け取り、それぞれのYES・NOの組み合わせを表現した定数を返す
	 */
	public function convRweToConst($rAuth, $wAuth, $eAuth) {
		if(		$rAuth===0	&&	$wAuth===0	&&	$eAuth===0) {
			return AUTH_NNN;
		}
		else if($rAuth===0	&&	$wAuth===0	&&	$eAuth===1) {
			return AUTH_NNE;
		}
		else if($rAuth===0	&&	$wAuth===1	&&	$eAuth===0) {
			return AUTH_NWN;
		}
		else if($rAuth===0	&&	$wAuth===1	&&	$eAuth===1) {
			return AUTH_NWE;
		}
		else if($rAuth===1	&&	$wAuth===0	&&	$eAuth===0) {
			return AUTH_RNN;
		}
		else if($rAuth===1	&&	$wAuth===0	&&	$eAuth===1) {
			return AUTH_RNE;
		}
		else if($rAuth===1	&&	$wAuth===1	&&	$eAuth===0) {
			return AUTH_RWN;
		}
		else if($rAuth===1	&&	$wAuth===1	&&	$eAuth===1) {
			return AUTH_RWE;
		}
	}





	public static function makeCellReqName($pePrimKey, $tableNumber, $offset, $recordPrimKey) {
		$reqName = "updateForm_pePrimKey_" . $pePrimKey . "_tblNumber_" . $tableNumber
					. "_offset_" . $offset . "_primaryKey_" . $recordPrimKey;
		return $reqName;
	}
}
?>

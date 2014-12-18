<?php
/*
 * Created on 2010/09/05
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */


class TfdHtmlCreator {
	// ClientInfoインスタンス
	var $clientInfo;

	// TFD
	var $tfd;

	// フィールド限定で選ばれているDDFそれぞれのオフセットを並べた配列
	var $offsetArray;	

	// 権限を反映させるか否か（Table Displayの出力ではtrue、Display Areaの出力ではfalseにする）
	var $authReflect;



	public function __construct($tfd, $clientInfo) {
		$this->tfd = $tfd;
		$this->clientInfo = $clientInfo;

		// デフォルトとして、全User Friendly Field を表示するよう設定する
		// （TableDisplayではフィールド限定が必要なので、setOffsetArray()で設定し直す）
		$this->initOffsetArray();

		$this->authReflect = true;
	}





	/*
	 * SUMMARY :
	 * TabularFormDataオブジェクトを基に、表データを表すHTMLテーブルを作成、返却する
	 * 
	 * MODIFY :
	 * ・各データタイプに応じた表示にする
	*/
	public function makeTableHtml() {
		// 最後に返却するHTML（この後どんどん末尾に付け足していく）
		$html = '<table border="1" style="empty-cells:show">';



		// フィールド名表示部分
		$html .= '<tr>';
		for($i=0; $i<$this->tfd->getVisibleFieldNum(); $i++) {
			// フィールド限定によって排除されているフィールド -> 無視
			if(!in_array($i, $this->offsetArray)) {
				continue;
			}

			$html .= '<td>' . $this->tfd->webNameArray[$i] . '</td>';
		}
		$html .= '</tr>';



		// 各レコード
		for($i=0; $i<$this->tfd->recordNum; $i++) {
			$recordHtml = '<tr>';

			// HTMLテーブルの、このレコードに対応した行が、全セル空欄（全角スペース１つ）となるかどうかチェックするための変数
			// この変数には最終的に、全セル空欄である場合の１行分のHTMLを格納する
			$blankRecCheckHtml = "<tr>";

			// 各User Friendly Fieldについて繰り返し
			for($uffOffset=0; $uffOffset<$this->tfd->getVisibleFieldNum(); $uffOffset++) {
				// フィールド限定によって排除されているフィールド -> 無視
				if(!in_array($uffOffset, $this->offsetArray)) {
					continue;
				}

				$recordHtml .= '<td>';
				
				// 権限を反映する場合（Table Display の場合。$this->tfdはDDT全体をそのままTFDにしたもののはず）
				// -> アクセスユーザの、このセルに対するRead権限をチェックしたい
				if($this->authReflect===true) {
					/*
					 * このレコードのレコードロケーションを割り出す
					 * （TFD中のこのTFDレコードのレコードヒストリーは、TPPNとRecordLocationインスタンス配列の組が１つだけで、
					 * -1 => このレコードのレコードロケーションだけからなるRecordLocation配列、となっているはず）
					 * ※この操作は、今いるfor分（$uffOffsetを使っているfor文）の外でできそうだができない。
					 * それだとif($this->authReflect===true)の外側にいってしまい、権限を反映しない（$this->authReflect===false）場合も通過してしまう
					 * 権限を反映しない、つまりTable Displayではなく結果表示の場合には、$tfdRecord->recordHistory->getRecordLocationArrayByTppn(-1)でエラーとなるのである
					 * （-1なんていうインデックスが存在しないはず）
					 */ 
					$tfdRecord = $this->tfd->recordArray[$i];
					debug_varDump(__FUNCTION__, "1130:\$tfdRecord", $tfdRecord);
					$recordHistoryRecordLocArray = $tfdRecord->recordHistory->getRecordLocationArrayByTppn(-1);
					$recordLocation = $recordHistoryRecordLocArray[0];

					$matcher = new CellAuthGetter($this->clientInfo);
					$cellLoc = new CellLocation($recordLocation->tableNumber, $recordLocation->primaryKey, $uffOffset);
					$cliFieldAuth = $matcher->check($cellLoc);
					$cliReadAuth = $cliFieldAuth->readAuth;

					// このセルのデータタイプ
					$dataTypeName = $this->tfd->getDataType($uffOffset)->dataTypeName;



					// Read権限あり -> TFDから対応セルの値を引っ張り出し、<TD>と</TD>の間に入れる
					if($cliReadAuth===1) {
						$value = $this->tfd->getValue($i, $uffOffset);

						debug_varDump(__FUNCTION__, "\$dataTypeName", $dataTypeName);

						$recordHtml .= TfdHtmlCreator::makeCellHtml($value, $dataTypeName, $cellLoc);
						$blankRecCheckHtml .= "<td>　</td>";
					}
					// Read権限なし -> スペース１つ（ただしPASSWORD型は"********"とする。PASSWORD型のRead権限は必ずNOである）
					else if ($cliReadAuth===0) {
						if($dataTypeName===DataType::TYPE_PASSWORD) {
							$recordHtml .= "********";
							$blankRecCheckHtml .= "<td>********</td>";
						}
						else {
							$recordHtml .= "　";
							$blankRecCheckHtml .= "<td>　</td>";
						}
					}
					// エラー
					else {
						logg_debug_error("テーブルのセルに対するRead権限を調べましたが、導いたRead権限の値が不正です", "TfdHtmlCreator", __FUNCTION__);
						debug_varDump(__FUNCTION__, "\$cliReadAuth", $cliReadAuth);
					}
				}
				// Read権限を反映しない場合（Display Area）の場合 ... 普通に値を表示
				else {
					$value = $this->tfd->getValue($i, $uffOffset);
					$dataTypeName = $this->tfd->getDataType($uffOffset)->dataTypeName;

					debug_varDump(__FUNCTION__, "\$dataTypeName", $dataTypeName);

					// このセルがFILE型でかつFileValueインスタンスが格納されている場合のみ、
					// 格納されているFileValueインスタンスが持つセルロケーションを渡す必要がある
					$cellLoc = null;
					if($dataTypeName===DataType::TYPE_FILE && $value instanceof FileValue) {
						$cellLoc = $value->cellLocation;
					}
					$recordHtml .= TfdHtmlCreator::makeCellHtml($value, $dataTypeName, $cellLoc);

					$blankRecCheckHtml .= "<td>　</td>";
				}

				$recordHtml .= '</td>';
			}

			$recordHtml .= '</tr>';
			$blankRecCheckHtml .= "</tr>";


			/*
			 * このレコードの全セルが"　"（全角スペース１つ）であった場合は、次のようにみなす
			 * 「$this->authReflectがTRUEで、アクセスユーザはいずれのセルにもRead権限を持たないためこうなった」
			 * ◆厳密にはそうとは限らないが、そうみなすことにする
			 * この場合、このレコードは出力しないので、このレコードの分のHTMLは空列となる
			 */
			logg_debug("\$blankRecCheckHtmlの値は".$blankRecCheckHtml."ですよおおお", "TfdHtmlCreator", __FUNCTION__);
			logg_debug("\$recordHtmlの値は".$recordHtml."ですよおおお", "TfdHtmlCreator", __FUNCTION__);
			if($recordHtml===$blankRecCheckHtml) {
				logg_debug("このレコードは全セルが空欄なので、表示しません", "TfdHtmlCreator", __FUNCTION__);
				$recordHtml = "";
			}

			// このレコードの分のHTMLが決定したので、付け足す
			$html .= $recordHtml;
		}



		$html .= '</table>';



		return $html;
	}




	
	/*
	 * SUMMARY :
	 * データタイプラッパー・値を受取、セル内の HTML を作成(<TD>と</TD>の間)
	 * 
	 * MODIFY :
	 * ENUM の分ができてない
	 * 
	 * NOTICE :
	 * ・ENUM の選択肢は DataType オブジェクトに内包されている（未実装）
	 * ・$cellLocation は FILE の場合だけ必要
	 */
	public static function makeCellHtml($value, $dataTypeName, $cellLocation=null) {
		if($dataTypeName===DataType::TYPE_INT) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_VARCHAR) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_DATETIME) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_DATE) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_TIME) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_ENUM) {
		}
		// FILE
		else if($dataTypeName===DataType::TYPE_FILE) {
			// $valueはFileValueインスタンスのはずである
			$fileName = $value->getFileName();

			if($fileName===NULL || $fileName==="") {
				$html = "　　";
			}
			else {
				$html = '<a href="' . htmlspecialchars($value->getFilePath()) . '">'
				      . htmlspecialchars($value->getFileName())
				      . '</a>';
			}
		}
		else if($dataTypeName===DataType::TYPE_MAIL) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_MAILAUTH) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_USERID) {
			$html = htmlspecialchars($value);
		}
		else if($dataTypeName===DataType::TYPE_PASSWORD) {
			// PASSWORD型のセルのHTMLはmakeTableHtml()の方で"********"にするはずなので、ここに来るのはおかしい
			logg_debug_error("PASSWORD型のセルのHTMLはmakeTableHtml()の方で'********'にするはずです。", "TdfHtmlCreator", __FUNCTION__);
		}
		else {
			error_logg(__FUNCTION__ . '()メソッド : 想定外のデータタイプです');
			return_logg(__FUNCTION__);

			return null;
		}

		return $html;
	}





	/*
	 * SUMMARY :
	 * フィールド限定のオフセット配列を初期化する
	 * TFDの全DDFのオフセットを含むようにする
	 * いくつかのフィールドを選ぶ場合は、setOffsetArray()でセットする
	 */
	public function initOffsetArray() {
		$this->offsetArray = array();

		$ddfNum = $this->tfd->getVisibleFieldNum();
		for($i=0; $i<$ddfNum; $i++) {
			$this->offsetArray[$i] = $i;
		}
	}





	public function setOffsetArray($offsetArray) {
		$this->offsetArray = $offsetArray;
	}
}

?>

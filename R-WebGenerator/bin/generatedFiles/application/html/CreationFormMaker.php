<?php

class CreationFormMaker {
	var $clientInfo;
	var $tableInfo;
	var $destPageNumber;
	var $pePrimKey;




	/*
	 * ◆修正点：$pePrimKeyがあれば、そこから$tableNumberは導出できるはずである
	 */
	public function __construct($clientInfo, $pePrimKey) {
		$this->clientInfo = $clientInfo;
		$this->pePrimKey = $pePrimKey;

		$query = "select * from page_element_create_form where pePrimaryKey=" . $this->pePrimKey;
		$result = mysql_query_logg($query);
		oneResultCheck($result, "CreationFormMaker", __FUNCTION__);
		$tableNumber = mysql_result($result, 0, "tableNumber");
		$this->tableInfo = new TableInformation($tableNumber);

		$queryDpn = "select * from transition where pePrimaryKey=".$this->pePrimKey;
		$resultDpn = mysql_query_logg($queryDpn);
		oneResultCheck($resultDpn, "CreationFormMaker", __FUNCTION__);
		$destPageNumber = mysql_result($resultDpn, 0, "tepNumber");
		$this->destPageNumber = $destPageNumber;
	}





	/*
	 * SUMMARY : レコード作成フォームのHTMLを作成・返却する
	 */
	public function makeCreateFormHtml() {
		logg_debug_call(__FUNCTION__, "CreationFormMaker");

		$query = 'SELECT * FROM table_list WHERE '.PRIMARY_KEY."=".$this->tableInfo->tableNumber;
		$result = mysql_query_logg($query);
		oneResultCheck($result, "CreationFormMaker", __FUNCTION__);

		// フォームHTML作成
		if(JAPANESE)
			$html = "各項目の値を入力し、「以上の内容で作成」ボタンを押して下さい。<br><br>\n";
		else
			$html = "Input each value and click creation button.<br><br>\n";
		for($i=0; $i<$this->tableInfo->getDdfNum(); $i++) {
			// このDDFの分のHTMLを作って付け足す
			$fieldInfo = $this->tableInfo->fieldInfoArray[$i];
			$html .= $this->makeFieldHtml($fieldInfo, $this->pePrimKey)."<br><br>\n";
		}

		// アカウントテーブルの場合、どのロールのアカウントなのか選択させるプルダウンメニューを表示する
		if($this->tableInfo->tableType==="account table") {
			$html .= "<select name='accOwnRoleNumber_pePrimKeyIs" . $this->pePrimKey . "[]'>\n"
						. "<option value='not selected'>";
			if(JAPANESE)
				$html .= "▼ロール";
			else
				$html .= "▼Which Role's Account?";
			$html .=  "</option>\n";

			$accOwnRoleNumberArray = $this->tableInfo->accOwnRoleNumberArray;
			for($accOwnRoleOrd=0; $accOwnRoleOrd<$this->tableInfo->getAccOwnRoleNum(); $accOwnRoleOrd++) {
				$accOwnRoleNumber = $accOwnRoleNumberArray[$accOwnRoleOrd];
				$accOwnRoleInfo = new RoleInfo($accOwnRoleNumber);
				$accOwnRoleName = $accOwnRoleInfo->name;

				$html .= "<option value='" . $accOwnRoleNumber . "'>" . $accOwnRoleName . "</option>\n";
			}

			$html .= "</select><br><br>\n";
		}

		// 遷移先ページのファイル名を割り出す
		$fpm = new FilePathManager();
		$destPageFileName = $fpm->getPageFileName($this->destPageNumber);

		// 遷移ボタン
		$action = $destPageFileName."?pepk=".$this->pePrimKey;
		$javaScriptCode = "document.PageElementsData.action=\"".htmlspecialchars($action)."\";"
							. "document.PageElementsData.submit();";
		if(JAPANESE)
			$buttonValue = "以上の内容で作成";
		else
			$buttonValue = "Creation";
		$html .= "<input type='button' onClick='".$javaScriptCode."' value='".$buttonValue."'><br>";

		logg_debug_return(__FUNCTION__, "CreationFormMaker");
		return $html;
	}






	public function makeFieldHtml($fieldInfo, $creFormPePrimKey) {
		$paramName = CreationFormMaker::makeCellReqName($creFormPePrimKey, $this->tableInfo->tableNumber, $fieldInfo->offset);
		$dataTypeName = $fieldInfo->dataTypeName;

		$html = htmlspecialchars($fieldInfo->nameOnWeb) . "：<br>";

		// INT
		if($dataTypeName===DataType::TYPE_INT) {
			$html .= "<input type='text' size='6' name='".$paramName."[]'><br>";
		}
		// VARCHAR
		else if($dataTypeName===DataType::TYPE_VARCHAR) {
			$html .= "<input type='text' size='".$fieldInfo->max."' name='".$paramName."[]'><br>";
		}
		// MAIL
		else if($dataTypeName===DataType::TYPE_MAIL) {
			$html .= "<input type='text' size='32' name='".$paramName."[]'><br>";
		}
		// MAILAUTH
		else if($dataTypeName===DataType::TYPE_MAILAUTH) {
			$html .= "<input type='text' size='32' name='".$paramName."[]'><br>";
		}
		// USERID
		else if($dataTypeName===DataType::TYPE_USERID) {
			$html .= "<input type='text' size='".$fieldInfo->max."' name='".$paramName."[]'><br>";
		}
		// PASSWORD
		else if($dataTypeName===DataType::TYPE_PASSWORD) {
			$html .= "<input type='password' size='24' name='".$paramName."[]'><br>\n";
			if(JAPANESE)
				$html .= "確認のため再入力して下さい：<br>\n";
			else
				$html .= "Input again for confirmation :<br>\n";
			$html .= "<input type='password' size='24' name='".$paramName."_confirm[]'><br>\n";
		}
		// ROLE_NAME
		else if($dataTypeName===DataType::TYPE_ROLE_NAME) {
			// makeCreateFormHtml()でロール名を選択するプルダウンメニューのHTMLを作ってあるが、
			// このプルダウンメニューからのリクエストを基に、TFD中のROLE_NAME型の値は決まるので、ここでは何もしなくてよい
		}
		// エラー
		else {
			logg_debug_error("想定外のデータタイプです", "CreationFormMaker", __FUNCTION__);
		}

		return $html;
	}





	public static function makeCellReqName($pePrimKey, $tblNumber, $ddfOffset) {
		$reqName = "createForm_pePrimKey_" . $pePrimKey . "_tblNumber_" . $tblNumber
					. "_offset_" . $ddfOffset;
		return $reqName;
	}
}
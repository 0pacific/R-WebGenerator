<?php
/*
 * Created on 2010/11/12
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class TableInformation {
	var $tableNumber;
	var $tableType;
	var $nameOnDb;
	var $nameOnWeb;
	var $fieldInfoArray = array();
	var $accOwnRoleNumberArray;


	/*
	 * SUMMARY :
	 * コンストラクタ
	 */
	public function __construct($tableNumber) {
		logg_debug_call(__FUNCTION__, "TableInformation");

		// テーブル一覧テーブル　全体取得
		$queryT = "select * from table_list where primaryKey=$tableNumber";
		$result = mysql_query_logg($queryT);
		oneResultCheck($result, "TableInformation", __FUNCTION__);

		// 結果セットから情報を取り出しインスタンスにセット
		$this->tableNumber = mysql_result($result, 0, PRIMARY_KEY);
		$this->tableType = mysql_result($result, 0, "type");	// "data table" or "account table"
		$this->nameOnDb = mysql_result($result, 0, "name");
		$this->nameOnWeb = mysql_result($result, 0, "nameOnWeb");

		// アカウントテーブルである場合には、どのロールのアカウントを管理するのか記録するようにする
		if($this->tableType==="account table") {
			$this->initAccOwnRoleNumberArray();
		}

		$this->initFieldInfoArray();

		logg_debug_return(__FUNCTION__, "TableInformation");
	}





	/*
	 * SUMMARY :
	 * 対応するアカウントオーナーロールを調べ上げ、それらロールの番号を配列にしたもので$this->accOwnRoleNumberArrayを初期化
	 */
	public function initAccOwnRoleNumberArray() {
		$query = "select * from role where at_number=".$this->tableNumber." order by number";
		$result = mysql_query_logg($query);

		logg_debug("1126:アカウントオーナーロールの数は".mysql_num_rows($result)."です。", "TableInformation", __FUNCTION__);

		$this->accOwnRoleNumberArray = array();
		for($i=0; $i<mysql_num_rows($result); $i++) {
			$this->accOwnRoleNumberArray[$i] = mysql_result($result, $i, "number");
		}
	}





	/*
	 * SUMMARY :
	 * テーブルの各フィールドの情報を調べ、インスタンスにセットする
	 */
	public function initFieldInfoArray() {
		logg_debug_call(__FUNCTION__, "TableInformation");

		for($i=0; $i<$this->getAllFieldsNum(); $i++) {
			$this->fieldInfoArray[$i] = new FieldInformation($this->tableNumber, $i);
		}

		logg_debug_return(__FUNCTION__, "TableInformation");
	}





	/*
	 * SUMMARY :
	 * 全フィールド数を返す（DeveloperDefinedであるものないもの全て）
	 */
	public function getAllFieldsNum() {
		logg_debug_call(__FUNCTION__, "TableInformation");

		// フィールド一覧テーブルから結果セット取得
		$query = "select * from field_info "
					. "where tblNumber=$this->tableNumber "
					. "order by offset asc";
		$result = mysql_query_logg($query);

		// 結果セットの行数がそのままフィールド（DDF・RFD問わず）の数
		$fieldsNum = mysql_num_rows($result);
		logg_debug("テーブル「'$this->nameOnWeb'」の全フィールド数(DDF・RDFの総数)：$fieldsNum");
		logg_debug_return(__FUNCTION__, "TableInformation");
		return $fieldsNum;
	}





	/*
	 * SUMMARY :
	 * DeveloperDefinedなフィールドの総数を返す
	 */
	public function getDdfNum() {
		logg_debug_call(__FUNCTION__, "TableInformation");

		$num = 0;

		for($i=0; $i<count($this->fieldInfoArray); $i++) {
			$fieldInfo = $this->fieldInfoArray[$i];
			if($fieldInfo->developerDefined==="YES") {
				$num++;
			}
			else if($fieldInfo->developerDefined==="NO") {
				// 何もしない
			}
			else {
				logg_debug_error("FieldInformationインスタンスの、developerDefined変数の値が異常です。", "TableInformation", __FUNCTION__);
			}
		}

		logg_debug("DeveloperDefinedFieldの数：$num", "TableInformation", __FUNCTION__);
		logg_debug_return(__FUNCTION__, "TableInformation");
		return $num;
	}





	public function getCurRecordNum() {
		$query = "select * from " . $this->nameOnDb;
		$result = mysql_query_logg($query);
		$num = mysql_num_rows($result);
		return $num;
	}





	public function getAccOwnRoleNum() {
		return count($this->accOwnRoleNumberArray);
	}
}
?>

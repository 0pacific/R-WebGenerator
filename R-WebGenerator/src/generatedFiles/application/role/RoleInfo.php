<?php

class RoleInfo {
	var $number;
	var $name;
	var $at_number;
	
	
	
	
	
	public function __construct($roleNumber) {
		logg_call(get_class($this) . '::' . __FUNCTION__);

		$this->number = $roleNumber;
		$this->name = RoleInfo::getName($roleNumber);
		$this->at_number = RoleInfo::getAtNumber($roleNumber);

		return_logg(get_class($this) . '::' . __FUNCTION__);
	}





	/*
	SUMMARY : ロール名をDBから取得する
	*/
	public static function getName($roleNumber) {
		$query = 'SELECT ' . ROLE_TABLE_FIELD_NAME . ' FROM ' . ROLE_TABLE
					. ' WHERE ' . ROLE_TABLE_FIELD_ROLE_NUMBER . '=' . $roleNumber;
		$result = mysql_query_logg($query);

		// エラー : 結果の行数が１でない
		if(mysql_num_rows($result)!==1) {
			error_logg(__FUNCTION__ . '() : ただ１行の結果リソースが得られませんでした');
			return_logg(__FUNCTION__);
		}

		$name = mysql_result($result, 0, 0);
		return $name;
	}





	/*
	SUMMARY : ロール番号を基に、対応するアカウントテーブルの番号をDBから取得する
	*/
	public static function getAtNumber($roleNumber) {
		logg_call(__FUNCTION__);

		// アカウントテーブル番号抽出
		$query = 'SELECT ' . ROLE_TABLE_FIELD_AT_NUMBER . ' FROM ' . ROLE_TABLE
					. ' WHERE ' . ROLE_TABLE_FIELD_ROLE_NUMBER . '=' . $roleNumber;
		$result = mysql_query_logg($query);

		// エラー : 結果の行数が１でない
		if(mysql_num_rows($result)!==1) {
			error_logg('アカウントテーブル番号抽出失敗');
		}
		$at_number = mysql_result($result, 0, 0);

		return_logg(__FUNCTION__);
		return $at_number;
	}





	/*
	SUMMARY : 指定したロールが、指定したテーブルに対しIAを有するかチェックする
	*/
	public static function hasIA($tableNumber, $roleNumber) {
		logg_debug_call(__FUNCTION__);

		$query = 'SELECT ' . IT_FIELD_HAS_IA . ' FROM ' . IA_TABLE . ' WHERE '
					. IT_FIELD_TABLE_NUMBER . '=' . $tableNumber . ' AND '
					. IT_FIELD_ROLE_NUMBER . '=' . $roleNumber;
		$result = mysql_query_logg($query);

		// エラー : 結果の行数が１でない
		if(mysql_num_rows($result)!==1) {
			logg_debug_error(__FUNCTION__ . '()メソッド : 得られた結果リソースの行数が１ではありません');
		}

		$hasIA = mysql_result($result, 0, 0);
		// IAを有する
		if($hasIA===IT_FIELD_HAS_IA_TRUE) {
			logg_debug_return(__FUNCTION__);
			return true;
		}
		// IAを有さない
		else if($hasIA===IT_FIELD_HAS_IA_FALSE) {
			logg_debug_return(__FUNCTION__);
			return false;
		}

		// エラー
		logg_debug_error(__FUNCTION__ . '()メソッド : 得られた真偽の値が不正です');
		logg_debug_return(__FUNCTION__);
		return null;
	}
}

?>
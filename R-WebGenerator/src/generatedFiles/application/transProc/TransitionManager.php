<?php
/*
 * Created on 2011/01/10
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class TransitionManager {
	/*
	 * SUMMARY :
	 * 遷移トリガーページエレメントの主キーから、遷移の番号を取得
	 */
	public function getTransNumberByTriggerPePrimKey($pePrimKey) {
		$query = "select * from transition where pePrimaryKey=".$pePrimKey;
		$result = mysql_query_logg($query);
		oneResultCheck($result, get_class($this), __FUNCTION__);

		$transNumber = mysql_result($result, 0, PRIMARY_KEY);
		return $transNumber;
	}
}
?>

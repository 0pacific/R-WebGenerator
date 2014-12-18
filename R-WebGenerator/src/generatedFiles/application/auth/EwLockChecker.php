<?php
/*
 * Created on 2010/11/18
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class EwLockChecker {
	public function __construct() {
	}





	/*
	 * SUMMARY :
	 * テーブル番号・レコード主キー・DDFオフセットを受け取って１つのセルを特定し、
	 * そのセルにExWriteしているユーザがいれば（１人のはず）そのロール番号・ユーザ番号を連想配列で返す
	 * いなければロール番号・ユーザ番号ともにNULLの連想配列を返す
	 * $hash['roleNumber'] <- ロール番号
	 * $hash['userNumber'] <- ユーザ番号
	 */
	public function check($tableNumber, $recordPrimKey, $offset) {
		$query = "select * from exwrite_lock where "
					. "tblNumber=" . $tableNumber . " and "
					. "recordPrimKey=" . $recordPrimKey . " and "
					. "offset=" . $offset;
		$result = mysql_query_logg($query);

		$hash = array();
		$recordNum = mysql_num_rows($result);

		// 誰もExWriteしていない
		if($recordNum===0) {
			$hash["roleNumber"] = null;
			$hash["userNumber"] = null;
		}
		// １人のユーザがExWriteしている
		else if($recordNum===1) {
			$roleNumber = mysql_result($result, 0, "roleNumber");
			$userNumber = mysql_result($result, 0, "userNumber");
			$hash["roleNumber"] = $roleNumber;
			$hash["userNumber"] = $userNumber;
		}
		// エラー（２人以上のユーザがExWriteしている）
		else {
			logg_debug_error("１つのセルにExWriteしているユーザが２人以上います", "EwLockChecker", __FUNCTION__);
		}

		return $hash;
	}
}
?>

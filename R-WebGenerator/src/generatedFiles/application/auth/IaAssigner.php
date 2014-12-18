<?php
/*
 * Created on 2010/11/30
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class IaAssigner {
	var $roleNumber;
	var $userNumber;
	var $tableNumber;
	var $recordPrimKey;

	public function __construct($roleNumber, $userNumber, $tableNumber, $recordPrimKey) {
		$this->roleNumber = $roleNumber;
		$this->userNumber = $userNumber;
		$this->tableNumber = $tableNumber;
		$this->recordPrimKey = $recordPrimKey;
	}

	public function assign() {
		$query = "insert into ia_assign (roleNumber, userNumber, tableNumber, recordPrk) values ("
					. $this->roleNumber . ","
					. $this->userNumber . ","
					. $this->tableNumber . ","
					. $this->recordPrimKey . ")";
		$result = mysql_query_logg($query);
		if($result===false) {
			logg_debug_error("IAのアサインに失敗しました。", "IaAssigner", __FUNCTION__);
			return false;
		}

		logg_debug("IAのアサインに成功しました！", "IaAssigner", __FUNCTION__);
		return true;
	}
}
?>

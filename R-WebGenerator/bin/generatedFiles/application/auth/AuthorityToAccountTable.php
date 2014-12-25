<?php
/*
 * Created on 2010/11/19
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class AuthorityToAccountTable {
	var $authSetHash;

	public function __construct() {
		$authSetHash = array();
	}




	/*
	 * 指定したロールのアカウントに対する権限セット（AuthoritySetインスタンス）を設定する
	 */
	public function addAuthSetToAccount($accOwnRoleNumber, $authSet) {
		$this->authSetHash[$accOwnRoleNumber] = $authSet;
	}





	public function getAuthToAccount($accOwnRoleNumber) {
		return $this->authSetHash[$accOwnRoleNumber];
	}





	public function getAuthSetNum() {
		return count($this->authSetHash);
	}
}
?>

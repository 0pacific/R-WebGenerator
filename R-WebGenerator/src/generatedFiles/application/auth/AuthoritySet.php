<?php
/*
 * Created on 2010/11/19
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class AuthoritySet {
	// 下記それぞれのインスタンス変数の値は0 or 1 となっているので注意
	// true or false だと デバッグ出力時に false が空列になってしまうので仕方なく 0 or 1 としている
	var $create;
	var $raReadArray;
	var $raWriteArray;
	var $raExWriteArray;
	var $raDelete;
	var $iaReadArray;
	var $iaWriteArray;
	var $iaExWriteArray;
	var $iaDelete;
	var $iaAssignHash;





	public function __construct($create,
	                            $raReadArray, $raWriteArray, $raExWriteArray,
	                            $raDelete,
	                            $iaReadArray, $iaWriteArray, $iaExWriteArray,
	                            $iaDelete,
	                            $iaAssignHash) {
		$this->create = $create;
		$this->raReadArray = $raReadArray;
		$this->raWriteArray = $raWriteArray;
		$this->raExWriteArray = $raExWriteArray;
		$this->raDelete = $raDelete;
		$this->iaReadArray = $iaReadArray;
		$this->iaWriteArray = $iaWriteArray;
		$this->iaExWriteArray = $iaExWriteArray;
		$this->iaDelete = $iaDelete;
		$this->iaAssignHash = $iaAssignHash;
	}
}
?>

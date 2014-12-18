<?php
/*
 * Created on 2010/11/22
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class FieldAuthSet {
	// 値としては 0 or 1 が格納されるので注意
	var $readAuth;
	var $writeAuth;
	var $exWriteAuth;



	public function __construct($readAuth, $writeAuth, $exWriteAuth) {
		$this->readAuth = $readAuth;
		$this->writeAuth = $writeAuth;
		$this->exWriteAuth = $exWriteAuth;
	}
}
?>

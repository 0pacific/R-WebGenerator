<?php
/*
 * Created on 2010/11/29
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class CreationFailureError extends Error {
	public function __construct() {
	}

	public function getMessage() {
		return "原因不明のエラーにより、レコードの作成に失敗しました。";
	}
}
?>

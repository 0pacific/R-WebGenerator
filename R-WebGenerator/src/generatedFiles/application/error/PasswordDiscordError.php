<?php
/*
 * Created on 2010/11/29
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class PasswordDiscordError extends Error {
	var $ddfNameOnWeb;

	public function __construct($ddfNameOnWeb) {
		$this->ddfNameOnWeb = $ddfNameOnWeb;
	}

	public function getMessage() {
		return "項目「".$this->ddfNameOnWeb."」の値として入力されたパスワードが不一致です。もう一度正確に入力して下さい。";
	}
}
?>

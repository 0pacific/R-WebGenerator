<?php
/*
 * Created on 2010/12/03
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class CurrentPasswordMismatchError extends Error {
	var $ddfNameOnWeb;

	public function __construct($ddfNameOnWeb) {
		$this->ddfNameOnWeb = $ddfNameOnWeb;
	}

	public function getMessage() {
		return "項目「".$this->ddfNameOnWeb."」に入力された現在のパスワードが間違っています。もう一度正確に入力して下さい。";
	}
}

?>

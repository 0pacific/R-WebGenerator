<?php
/*
 * Created on 2010/11/29
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class LengthImproperError extends Error {
	var $ddfNameOnWeb;
	var $minLen;
	var $maxLen;



	public function __construct($ddfNameOnWeb, $minLen, $maxLen) {
		$this->ddfNameOnWeb = $ddfNameOnWeb;
		$this->minLen = $minLen;
		$this->maxLen = $maxLen;
	}



	public function getMessage() {
		return "項目「".$this->ddfNameOnWeb."」の値は".$this->minLen."字以上.".$this->maxLen."字以下でなければなりません。";
	}
}

?>

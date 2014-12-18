<?php
/*
 * Created on 2010/11/19
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class TextHtmlCreater {
	var $pePrimKey;





	public function __construct($pePrimKey) {
		$this->pePrimKey = $pePrimKey;
	}





	public function generate() {
		$queryTxt = "select * from page_element_text where pePrimaryKey=" . $this->pePrimKey;
		$resultTxt = mysql_query_logg($queryTxt);
		oneResultCheck($resultTxt, "TextHtmlCreater", __FUNCTION__);

		$text = mysql_result($resultTxt, 0, "text");

		$html = str_replace("\n", "<br>\n", $text);
		return $html;
	}	
}
?>

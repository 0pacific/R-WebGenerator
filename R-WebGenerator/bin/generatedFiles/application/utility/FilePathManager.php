<?php
/*
 * Created on 2010/11/15
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class FilePathManager {
	public function __construct() {
	}




	public function getPageFileName($pageNumber) {
		$query = "select * from pages where pageNumber=" . $pageNumber;
		$result = mysql_query_logg($query);
		$fileName = mysql_result($result, 0, "fileName");
		return $fileName;
	}
}
?>

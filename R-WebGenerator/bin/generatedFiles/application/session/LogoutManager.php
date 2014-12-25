<?php
/*
 * Created on 2011/01/04
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class LogoutManager {
	public function getLogoutDestPageName() {
		$destPageNumber = $this->getLogoutDestPageNumber();

		// その番号からページ名を割り出す
		$filePathManager = new FilePathManager();
		$destPageName = $filePathManager->getPageFileName($destPageNumber);

		return $destPageName;
	}




	public function updateSessionIfLogoutAccessed() {
		// executeLogoutというGETリクエストがないなら、ログアウトはしない
		if(!array_key_exists("executeLogout", $_GET)) {
			return false;
		}

		// executeLogoutというGETリクエストがあれば、それでログアウトとみなす

		logg_debug("ユーザがログアウトを試みました。", get_class($this), __FUNCTION__);

		$sessionManager = new SessionManager();
		$sessionManager->logout();
	}





	public function getLogoutDestPageNumber() {
		// "logout_trans"テーブルから、ログアウト時遷移先ページの番号を取得
		$query = "SELECT * FROM  `logout_trans`";
		$result = mysql_query_logg($query);
		oneResultCheck($result, get_class($this), __FUNCTION__);
		$destPageNumber = mysql_result($result, 0, "destPageNumber");

		return $destPageNumber;
	}





	public function isLogoutDestPageNumber($pageNumber) {
		return ($pageNumber==$this->getLogoutDestPageNumber());
	}
}

?>

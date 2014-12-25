<?php

class SessionManager {
	/*
	ユーザがアクセスしてきた際に、セッション情報を検証し正当なユーザか調べる
	Guestロールの場合はスルーする
	*/
	public static function confirmUserValidity() {
		logg_call(__FUNCTION__);

		// Guestロールの場合はOK
		if(!array_key_exists(SESSION_ROLE_NUMBER, $_SESSION) || !array_key_exists(SESSION_USER_NUMBER, $_SESSION)) {
			return true;
		}

		$roleNumber = $_SESSION[SESSION_ROLE_NUMBER];
		$userNumber = $_SESSION[SESSION_USER_NUMBER];

		// アカウントテーブル番号抽出
		$ria = new RoleInfo($roleNumber);
		$at_number = $ria->at_number;
		
		// アカウントテーブル名抽出
		$atd = new ExistingTableDetail($at_number);
		$at_name = $atd->name_db;
		
		// ユーザの存在確認
		$result = mysql_query_logg('SELECT * FROM ' . $at_name . ' WHERE userNumber=' . $userNumber);
		if(mysql_num_rows($result)!==1) {
			error_logg('ユーザ認証失敗');
			return false;
		}
		
		return_logg(__FUNCTION__);
		return true;
	}





	public function logout() {
		$_SESSION[SESSION_ROLE_NUMBER] = getGuestRoleNumber();
		$_SESSION[SESSION_USER_NUMBER] = null;

		logg_debug("ログアウトのためにセッション情報を書き換えました...ロール番号：".$_SESSION[SESSION_ROLE_NUMBER]."、ユーザ番号：".$_SESSION[SESSION_USER_NUMBER], get_class($this), __FUNCTION__);
	}
}

?>
<?php


/*
 * SUMMARY :
 * セッションを開始
 * ゲストロールによる初回アクセス時に限り、セッション情報にゲストロール番号記録
 */
function startSession() {
	session_start();
	
	if(isFirstAccess()) {
		logg_debug('$_SESSION[' . SESSION_ROLE_NUMBER . ']が未セットなのでゲストロールの番号を取得します。', "functions_guest.php", __FUNCTION__);

		$guestRoleNumber = getGuestRoleNumber();
		$_SESSION[SESSION_ROLE_NUMBER] = $guestRoleNumber;

		logg_debug('$_SESSION[' . SESSION_ROLE_NUMBER . ']の値を' . $guestRoleNumber . 'にしました。', "functions_guest.php", __FUNCTION__);
	}
	else {
	}
}

/*
 * SUMMARY :
 *　ユーザがセッションを「持っていない」か否かを報告
 */
function isFirstAccess() {
	if(!array_key_exists(SESSION_ROLE_NUMBER, $_SESSION)) {
		return true;
	}
	else if($_SESSION[SESSION_ROLE_NUMBER]===null
	        || $_SESSION[SESSION_ROLE_NUMBER]===''
	        || $_SESSION[SESSION_ROLE_NUMBER]===false) {
		return true;
	}

	logg_debug("初回アクセスではないようです。セッションが存在します。", "", "");
	return false;
}

?>
<?php

/*
 * 前提require :
 * property.php
 */

class ClientInfo {
	// アクセスユーザのロール番号
	var $roleNumber;

	// アクセスユーザのロール名
	var $roleName;

	// アクセスユーザのユーザ番号（ロール内での識別番号）
	var $userNumber;



	/*
	SUMMARY : コンストラクタ
	*/
	public function __construct() {
		logg_debug_call(__FUNCTION__, "ClientInfo");

		// エラー : セッション情報中にロール番号がない
		if(!array_key_exists(SESSION_ROLE_NUMBER, $_SESSION)) {
			error_logg(__FUNCTION__ . '() : インスタンス化に失敗しました');
		}

		// アクセス中のユーザのロール番号取得
		$this->roleNumber = $_SESSION[SESSION_ROLE_NUMBER];
		debug_line('アクセスしているユーザのロール番号を確認 : ' . $this->roleNumber);

		// Guestロールでなければ、ユーザ番号も取得
		if(array_key_exists(SESSION_USER_NUMBER, $_SESSION)) {
			$this->userNumber = $_SESSION[SESSION_USER_NUMBER];
			debug_line('アクセスしているユーザのユーザ番号を確認 : ' . $this->userNumber);
		}
		// ゲストロールならNULL
		else {
			$this->userNumber = null;
		}

		$this->initRoleName();

		logg_debug_return(__FUNCTION__, "ClientInfo");
	}



	/*
	SUMMARY : アクセス中のユーザのロール名を返す
	          ロール名はRoleInfoオブジェクトを生成しそこから取得する
	*/
	public function initRoleName() {
		logg_debug_call(__FUNCTION__, "ClientInfo");

		$roleInfo = $this->getRoleInfo();
		debug_varDump(get_class($this).'->'.__FUNCTION__, '$roleInfo', $roleInfo);
		$this->roleName = $roleInfo->name;

		logg_debug_return(__FUNCTION__, "ClientInfo");
	}

	/*
	SUMMARY : アクセス中のユーザがゲストロールであるか否かをboolean値で返す
	*/
	public function isGuest() {
		logg_debug_call(__FUNCTION__, "ClientInfo");

		if($this->roleNumber===getGuestRoleNumber()) {
			return true;
		}

		logg_debug_return(__FUNCTION__, "ClientInfo");
		return false;
	}

	public function getRoleNumber() {
		logg_debug_call(__FUNCTION__, "ClientInfo");

		logg_debug_return(__FUNCTION__, "ClientInfo");
		return $this->roleNumber;
	}

	public function getRoleInfo() {
		logg_debug_call(__FUNCTION__, "ClientInfo");
		logg_debug_return(__FUNCTION__, "ClientInfo");
		return new RoleInfo($this->roleNumber);
	}
}
<?php
/*
 * Created on 2010/09/06
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class CellAuthGetter {
	var $clientInfo;





	public function __construct($clientInfo) {
		$this->clientInfo = $clientInfo;
	}





	/*
	 * SUMMARY :
	 * アクセスユーザの、渡されたセルロケーションにあるセルに対する対フィールド権限（Read・Write・ExWrite）を、FieldAuthSetインスタンスで返す）
	 */
	public function check($cellLocation) {
		$authGetter = new AuthorityGetter($this->clientInfo, $cellLocation->tableNumber);
		$authToTable = $authGetter->getAuthToTable();

		$tableInfo = new TableInformation($cellLocation->tableNumber);
		$tableType = $tableInfo->tableType;

		$recordPrimKey = $cellLocation->primaryKey;
		$iaAssCh = new IaAssignmentChecker();
		$cliIsIaAssigned = $iaAssCh->check($cellLocation->tableNumber, $cellLocation->primaryKey, $this->clientInfo);

		if($tableType==="data table") {
			$authSet = $authToTable->authSet;
			
			// アクセスユーザはこのレコードに関しIA-Assigned
			if($cliIsIaAssigned===true) {
				$readAuth = $authSet->iaReadArray[$cellLocation->offset];
				$writeAuth = $authSet->iaWriteArray[$cellLocation->offset];
				$exWriteAuth = $authSet->iaExWriteArray[$cellLocation->offset];
				return new FieldAuthSet($readAuth, $writeAuth, $exWriteAuth);
			}
			// IA-Assignedでない
			else if($cliIsIaAssigned===false) {
				$readAuth = $authSet->raReadArray[$cellLocation->offset];
				$writeAuth = $authSet->raWriteArray[$cellLocation->offset];
				$exWriteAuth = $authSet->raExWriteArray[$cellLocation->offset];
				return new FieldAuthSet($readAuth, $writeAuth, $exWriteAuth);
			}
			// エラー
			else {
				logg_debug_error("boolean値が不正です", "CellAuthGetter", __FUNCTION__);
				return null;
			}
		}
		// アカウントテーブルの場合（$authToTableはAuthorityToAccountTableインスタンスのはず）
		else if($tableType==="account table") {
			logg_debug("1128:アカウントテーブルのセルのようです");

			// $cellLocationの表すセルがあるレコードは、何番ロールのアカウントなのか、を割り出す
			$query = "select * from " . $tableInfo->nameOnDb . " where " . PRIMARY_KEY . "=" . $recordPrimKey;
			$result = mysql_query_logg($query);
			oneResultCheck($result, "CellAuthGetter", __FUNCTION__);
			$accOwnRoleNumber = mysql_result($result, 0, "roleNumber");

			logg_debug("1128:更に、".$accOwnRoleNumber."番ロールのアカウントレコード中のセルのようです");

			// 対アカウントテーブル権限$authToTableから、$accOwnRoleNumber番ロールのアカウントに対する権限セットを取り出す
			$authSet = $authToTable->getAuthToAccount($accOwnRoleNumber);

			debug_varDump(__FUNCTION__, "1128:".$accOwnRoleNumber."番ロールのアカウントに対する、アクセスユーザの権限セットを出力します", $authSet);

			// アクセスユーザはこのレコードに関しIA-Assigned
			if($cliIsIaAssigned===true) {
				logg_debug("1128:アクセスユーザはこのアカウントレコードに対しIA-Assignedです。");

				$readAuth = $authSet->iaReadArray[$cellLocation->offset];
				$writeAuth = $authSet->iaWriteArray[$cellLocation->offset];
				$exWriteAuth = $authSet->iaExWriteArray[$cellLocation->offset];
				return new FieldAuthSet($readAuth, $writeAuth, $exWriteAuth);
			}
			// IA-Assignedでない
			else if($cliIsIaAssigned===false) {
				logg_debug("1128:アクセスユーザはこのアカウントレコードに対しIA-Assignedではありません。");

				$readAuth = $authSet->raReadArray[$cellLocation->offset];
				$writeAuth = $authSet->raWriteArray[$cellLocation->offset];
				$exWriteAuth = $authSet->raExWriteArray[$cellLocation->offset];
				return new FieldAuthSet($readAuth, $writeAuth, $exWriteAuth);
			}
			// エラー
			else {
				logg_debug_error("boolean値が不正です", "CellAuthGetter", __FUNCTION__);
				return null;
			}
		}
		logg_debug_error("テーブルタイプが不正です", "CellAuthGetter", __FUNCTION__);
		return null;
	}
}

?>

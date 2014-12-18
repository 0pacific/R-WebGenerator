<?php

class IaAssignmentChecker {
	/*
	 * SUMMARY : 指定したレコードへのIAが、アクセスユーザに与えられているかチェックする
	 */
	public function check($tableNumber, $recordNumber, $clientInfo) {
		logg_debug_call(__FUNCTION__, "IaAssignmentChecker");

		// ゲストロールの場合、IAは持ち得ない
		if($clientInfo->isGuest()) {
			logg_debug("アクセスユーザはゲストロールのため、IA-Assignedではないと判断します", "IaAssignmentChecker", __FUNCTION__);
			return false;
		}

		$roleNumber = $clientInfo->roleNumber;
		$userNumber = $clientInfo->userNumber;

		$query = 'SELECT * FROM ia_assign where '
					. "tableNumber=".$tableNumber.' AND '
					. "recordPrk=".$recordNumber.' AND '
					. "roleNumber=".$roleNumber.' AND '
					. "userNumber=".$userNumber;
		$result = mysql_query_logg($query);
		$rowNum = mysql_num_rows($result);


		// IA-Assigned
		if($rowNum===1) {
			logg_debug("1128:".$tableNumber."番テーブルの、主キー".$recordNumber."のレコードに対し、アクセスユーザはIA-Assignedです。");
			logg_debug_return(__FUNCTION__, "IaAssignmentChecker");
			return true;
		}
		// IA-Assignedでない
		else if($rowNum===0) {
			logg_debug("1128:".$tableNumber."番テーブルの、主キー".$recordNumber."のレコードに対し、アクセスユーザはIA-Assignedではありません。");
			logg_debug_return(__FUNCTION__, "IaAssignmentChecker");
			return false;
		}
		// エラー : 結果リソースの行数が2以上
		else if($rowNum > 1) {
			logg_debug_error('1128:MySQL結果セットの行数が2以上となっています。', "IaAssignmentChecker", __FUNCTION__);
			logg_debug_return(__FUNCTION__, "IaAssignmentChecker");
			return null;
		}

		// その他のエラー
		logg_debug_error('1128:MySQL結果セットの取得に失敗しました', "IaAssignmentChecker", __FUNCTION__);
		logg_debug_return(__FUNCTION__, "IaAssignmentChecker");
		return null;
	}
}
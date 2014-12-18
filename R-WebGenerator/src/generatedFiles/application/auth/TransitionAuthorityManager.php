<?php
/*
 * Created on 2011/01/10
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class TransitionAuthorityManager {
	/*
	 * RETURN : 0 or 1
	 */
	public function getTransAuthByClientInfoAndTrggerPePrimKey($clientInfo, $triggerPePrimKey) {
		// 遷移トリガーPEによる遷移の番号
		$transitionManager = new TransitionManager();
		$transitionNumber = $transitionManager->getTransNumberByTriggerPePrimKey($triggerPePrimKey);

		// その遷移に対するアクセスユーザの権限
		$queryGetAuth =	"select * from transition_auth where transPrimaryKey=" . $transitionNumber
						. " and roleNumber=" . $clientInfo->getRoleNumber();
		$resultGetAuth = mysql_query_logg($queryGetAuth);
		oneResultCheck($resultGetAuth, get_class($this), __FUNCTION__);
		$transAuthYesOrNo = mysql_result($resultGetAuth, 0, "permission");
		$transAuth = convAuthToZeroOne($transAuthYesOrNo);

		return $transAuth;
	}
} 
?>

<?php

/*
 * SUMMARY :
 * アクセスユーザのテーブルに対するすべての権限を取得するクラス
 */
class AuthorityGetter {
	// ClientInfoインスタンス
	var $clientInfo;

	// テーブル番号
	var $tableNumber;





	public function __construct($clientInfo, $tableNumber) {
		logg_debug_call(__FUNCTION__, "AuthorityGetter");

		$this->clientInfo = $clientInfo;
		$this->tableNumber = $tableNumber;

		logg_debug_return(__FUNCTION__, "AuthorityGetter");
	}





	/*
	 * SUMMARY :
	 * ClientInfoインスタンスに基づき、データテーブルないしアカウントテーブルに対する権限を取得
	 */
	public function getAuthToTable() {
		logg_debug_call(__FUNCTION__, "AuthorityGetter");

		$tableInfo = new TableInformation($this->tableNumber);
		if($tableInfo->tableType==="data table") {
			return $this->getAuthToDataTable();
		}
		else if($tableInfo->tableType==="account table") {
			return $this->getAuthToAccountTable();
		}

		// エラー
		logg_debug_error("テーブルのタイプが不正です。", "AuthorityGetter", __FUNCTION__);

		logg_debug_return(__FUNCTION__, "AuthorityGetter");
		return null;
	}





	public function getAuthToDataTable() {
		logg_debug_call(__FUNCTION__, "AuthorityGetter");

		// アクセスユーザのロール番号
		$cliRoleNumber = $this->clientInfo->roleNumber;

		// テーブルの情報（TableInformationインスタンス）
		$tableInfo = new TableInformation($this->tableNumber);

		// これから取得するAuthoritySetインスタンスを格納していく配列（対データテーブルの場合は１つだけ、対アカウントテーブルの場合は１つ「以上」格納する）
		$authSetArray = array();


		/*
		 * アクセスユーザのロールがIA-Definedかチェック
		 */
		logg_debug("アクセスユーザのロールがIA-Definedかチェックします", "AuthorityGetter", __FUNCTION__);
		$queryIad = "select * from ia_possesion where tableNumber=".$this->tableNumber
					." and roleNumber=".$cliRoleNumber;
		$resultIad = mysql_query_logg($queryIad);
		oneResultCheck($resultIad, "AuthorityGetter", __FUNCTION__);
		$valIad = mysql_result($resultIad, 0, "hasIA");
		$cliRoleIsIaDefined = convAuthToZeroOne($valIad);



		/*
		 * Create
		 */
		logg_debug("Create権限をチェックします", "AuthorityGetter", __FUNCTION__);

		$queryC = 'select * from create_auth where roleNumber='.$cliRoleNumber
					.' and tableNumber='.$this->tableNumber; 
		$resultC = mysql_query_logg($queryC);
		oneResultCheck($resultC, "AuthorityGetter", __FUNCTION__);

		$permissionC = mysql_result($resultC, 0, "permission");
		$cAuth = convAuthToZeroOne($permissionC);



		/*
		 * Read・Write・ExWrite（RA）
		 */
		logg_debug("RA-Read, RA-Write, RA-ExWrite権限をチェックします", "AuthorityGetter", __FUNCTION__);
		$query_ra = 'select * from field_auth'
					.' where tableNumber='.$this->tableNumber
					.' and roleNumber='.$cliRoleNumber
					.' and status="role"'
					.' order by fieldNumber asc';
		$result_ra = mysql_query_logg($query_ra);
		$raReadArray = array();
		$raWriteArray = array();
		$raExWriteArray = array();
		$ddfNum = $tableInfo->getDdfNum();
		for($i=0; $i<$ddfNum; $i++) {
			$raReadArray[$i]    = convAuthToZeroOne(mysql_result($result_ra, $i, "ra"));
			$raWriteArray[$i]   = convAuthToZeroOne(mysql_result($result_ra, $i, "wa"));
			$raExWriteArray[$i] = convAuthToZeroOne(mysql_result($result_ra, $i, "ea"));
		}



		/*
		 * Read・Write・ExWrite（IA）
		 */
		// アクセスユーザのロールはIA-Definedである
		if($cliRoleIsIaDefined) {
			logg_debug("アクセスユーザのロールはIA-Definedなので、IA-Read, IA-Write, IA-ExWrite権限をチェックします", "AuthorityGetter", __FUNCTION__);

			$iaReadArray = array();
			$iaWriteArray = array();
			$iaExWriteArray = array();

			$query_ia = 'select * from field_auth'
						.' where tableNumber='.$this->tableNumber
						.' and roleNumber='.$cliRoleNumber
						.' and status="self"'
						.' order by fieldNumber asc';
			$result_ia = mysql_query_logg($query_ia);

			for($i=0; $i<$ddfNum; $i++) {
				$iaReadArray[$i]    = convAuthToZeroOne(mysql_result($result_ia, $i, "ra"));
				$iaWriteArray[$i]   = convAuthToZeroOne(mysql_result($result_ia, $i, "wa"));
				$iaExWriteArray[$i] = convAuthToZeroOne(mysql_result($result_ia, $i, "ea"));
			}
		}
		// アクセスユーザのロールはIA-Definedでない
		else {
			logg_debug("アクセスユーザのロールはIA-Definedでないので、IA-Read, IA-Write, IA-ExWrite権限はチェックしません", "AuthorityGetter", __FUNCTION__);

			$iaReadArray = null;
			$iaWriteArray = null;
			$iaExWriteArray = null;
		}



		/*
		 * Delete
		 */
		// このクエリを実行すると、RA-Deleteのレコードだけとれるか、加えてIA-Deleteのレコードもとれるはず（上からRA-Delete, IA-Deleteの順になる）
		$queryD = 'select * from delete_auth'
		       .' where roleNumber='.$cliRoleNumber
		       .' and tableNumber='.$this->tableNumber
		       .' order by status desc';	
		$resultD = mysql_query_logg($queryD);

		// RA-Deleteは必ず取得する
		logg_debug("RA-Delete権限をチェックします", "AuthorityGetter", __FUNCTION__);
		$permissionDR = mysql_result($resultD, 0, "permission");
		$raDelete = convAuthToZeroOne($permissionDR);

		// IA-Deleteは、アクセスユーザのロールがIA-Definedなら取得、違うならnull
		if($cliRoleIsIaDefined) {
			logg_debug("アクセスユーザのロールはIA-Definedなので、IA-Delete権限をチェックします", "AuthorityGetter", __FUNCTION__);
			$permissionDI = mysql_result($resultD, 1, "permission");
			$iaDelete = convAuthToZeroOne($permissionDI);
		}
		else {
			logg_debug("アクセスユーザのロールはIA-Definedでないので、IA-Delete権限はチェックしません", "AuthorityGetter", __FUNCTION__);
			$iaDelete = null;
		}



		/*
		 * IA割当権限
		 */
		logg_debug("まず、データテーブル「".$tableInfo->nameOnWeb."」に対しIA-Definedなロールがいくついるかチェックします", "AuthorityGetter", __FUNCTION__);

		// このデータテーブルについてIA-Definedなロールをリストアップ、各ロールに対しアクセスユーザのロールがIA割当権限を持つか否かチェック
		$queryIAAA = 'SELECT'
						.' ia_possesion.roleNumber AS iaDefinedRoleNumber,'
		           		.' ia_assign_auth.permission AS permission'
		           		.' FROM ia_possesion JOIN ia_assign_auth'
		           		.' WHERE ia_possesion.tableNumber='.$this->tableNumber
						// アカウントテーブルの場合、ここにアカウントオーナーロール番号のチェックが入る
		           		.' AND ia_assign_auth.assignerRoleNumber='.$cliRoleNumber
		           		.' AND ia_possesion.tableNumber=ia_assign_auth.tableNumber'
		           		.' AND ia_possesion.accOwnRoleNumber=ia_assign_auth.accOwnRoleNumber'
		           		.' AND ia_possesion.roleNumber=ia_assign_auth.assignedRoleNumber'
		           		.' ORDER BY ia_possesion.roleNumber ASC';
		$resultIAAA = mysql_query_logg($queryIAAA);
		$iaDefinedRoleNum = mysql_num_rows($resultIAAA);

		logg_debug("データテーブルに対しIA-Definedなロールは".$iaDefinedRoleNum."ついるようです。", "AuthorityGetter", __FUNCTION__);

		// IAアサイン権限を表すハッシュ（IA-Definedなロールがいない場合はNULLのまま）
		$iaAssignHash = null;

		// データテーブルに対しIA-Definedなロールが１つでもいる -> IA割当権限を取得
		if($iaDefinedRoleNum > 0) {
			$iaAssignHash = array();
			for($i=0; $i<$iaDefinedRoleNum; $i++) {
				$iaDefinedRoleNumber = mysql_result($resultIAAA, $i, 'iaDefinedRoleNumber');
				$permission = mysql_result($resultIAAA, $i, 'permission');
				$iaAssignHash[$iaDefinedRoleNumber] = convAuthToZeroOne($permission);
			}
		}


		// AuthorityToDataTableインスタンス生成・返却
		$authSet = new AuthoritySet(
			$cAuth,
			$raReadArray, $raWriteArray, $raExWriteArray,
			$raDelete,
			$iaReadArray, $iaWriteArray, $iaExWriteArray,
			$iaDelete,
			$iaAssignHash
		);
		$authToDt = new AuthorityToDataTable($authSet);

		debug_varDump(__FUNCTION__, "\$authToDt", $authToDt);
		
		logg_debug_return(__FUNCTION__, "AuthorityGetter");
		return $authToDt;
	}





	public function getAuthToAccountTable() {
		// getAuthToDataTable() 重複 ここから



		logg_debug_call(__FUNCTION__, "AuthorityGetter");

		// アクセスユーザのロール番号
		$cliRoleNumber = $this->clientInfo->roleNumber;

		// テーブルの情報（TableInformationインスタンス）
		$tableInfo = new TableInformation($this->tableNumber);

		// これから取得するAuthoritySetインスタンスを格納していく配列（対データテーブルの場合は１つだけ、対アカウントテーブルの場合は１つ「以上」格納する）
		$authSetArray = array();



		// getAuthToDataTable() 重複 ここまで

		
		
		// データテーブルの場合はこれは使わない
		$authToAt = new AuthorityToAccountTable();

		// 対象アカウントテーブルで管理するアカウントの種類の数だけ、繰り返し処理
		for($accOwnRoleOrd=0; $accOwnRoleOrd<count($tableInfo->accOwnRoleNumberArray); $accOwnRoleOrd++) {
			$accOwnRoleNumber = $tableInfo->accOwnRoleNumberArray[$accOwnRoleOrd];


			// getAuthToDataTable() ほぼ重複 ここから

			/*
			 * アクセスユーザのロールがIA-Definedかチェック
			 */
			logg_debug("アクセスユーザのロールがIA-Definedかチェックします", "AuthorityGetter", __FUNCTION__);
			$queryIad = "select * from ia_possesion where tableNumber=".$this->tableNumber
							." and accOwnRoleNumber=".$accOwnRoleNumber		// getAuthToDataTable()の場合に比べて、この行だけ足しただけ
							." and roleNumber=".$cliRoleNumber;
			$resultIad = mysql_query_logg($queryIad);
			oneResultCheck($resultIad, "AuthorityGetter", __FUNCTION__);
			$valIad = mysql_result($resultIad, 0, "hasIA");
			$cliRoleIsIaDefined = convAuthToZeroOne($valIad);



			/*
			 * Create
			 */
			logg_debug("Create権限をチェックします", "AuthorityGetter", __FUNCTION__);
	
			$queryC = 'select * from create_auth where roleNumber='.$cliRoleNumber
						." and accOwnRoleNumber=".$accOwnRoleNumber		// getAuthToDataTable()の場合に比べて、この行を足した（まだある）
						.' and tableNumber='.$this->tableNumber; 
			$resultC = mysql_query_logg($queryC);
			oneResultCheck($resultC, "AuthorityGetter", __FUNCTION__);
	
			$permissionC = mysql_result($resultC, 0, "permission");
			$cAuth = convAuthToZeroOne($permissionC);



			/*
			 * Read・Write・ExWrite（RA）
			 */
			logg_debug("RA-Read, RA-Write, RA-ExWrite権限をチェックします", "AuthorityGetter", __FUNCTION__);
			$query_ra = 'select * from field_auth'
						.' where tableNumber='.$this->tableNumber
						.' and roleNumber='.$cliRoleNumber
						." and accOwnRoleNumber=".$accOwnRoleNumber		// getAuthToDataTable()の場合に比べて、この行を足した（まだある）
						.' and status="role"'
						.' order by fieldNumber asc';
			$result_ra = mysql_query_logg($query_ra);
			$raReadArray = array();
			$raWriteArray = array();
			$raExWriteArray = array();
			$ddfNum = $tableInfo->getDdfNum();
			for($i=0; $i<$ddfNum; $i++) {
				$raReadArray[$i]    = convAuthToZeroOne(mysql_result($result_ra, $i, "ra"));
				$raWriteArray[$i]   = convAuthToZeroOne(mysql_result($result_ra, $i, "wa"));
				$raExWriteArray[$i] = convAuthToZeroOne(mysql_result($result_ra, $i, "ea"));
			}



			/*
			 * Read・Write・ExWrite（IA）
			 */
			// アクセスユーザのロールはIA-Definedである
			if($cliRoleIsIaDefined) {
				logg_debug("アクセスユーザのロールはIA-Definedなので、IA-Read, IA-Write, IA-ExWrite権限をチェックします", "AuthorityGetter", __FUNCTION__);
	
				$iaReadArray = array();
				$iaWriteArray = array();
				$iaExWriteArray = array();
	
				$query_ia = 'select * from field_auth'
							.' where tableNumber='.$this->tableNumber
							.' and roleNumber='.$cliRoleNumber
							." and accOwnRoleNumber=".$accOwnRoleNumber		// getAuthToDataTable()の場合に比べて、この行を足した（まだある）
							.' and status="self"'
							.' order by fieldNumber asc';
				$result_ia = mysql_query_logg($query_ia);
	
				for($i=0; $i<$ddfNum; $i++) {
					$iaReadArray[$i]    = convAuthToZeroOne(mysql_result($result_ia, $i, "ra"));
					$iaWriteArray[$i]   = convAuthToZeroOne(mysql_result($result_ia, $i, "wa"));
					$iaExWriteArray[$i] = convAuthToZeroOne(mysql_result($result_ia, $i, "ea"));
				}
			}
			// アクセスユーザのロールはIA-Definedでない
			else {
				logg_debug("アクセスユーザのロールはIA-Definedでないので、IA-Read, IA-Write, IA-ExWrite権限はチェックしません", "AuthorityGetter", __FUNCTION__);
	
				$iaReadArray = null;
				$iaWriteArray = null;
				$iaExWriteArray = null;
			}



			/*
			 * Delete
			 */
			// このクエリを実行すると、RA-Deleteのレコードだけとれるか、加えてIA-Deleteのレコードもとれるはず（上からRA-Delete, IA-Deleteの順になる）
			$queryD = 'select * from delete_auth'
						.' where roleNumber='.$cliRoleNumber
						.' and tableNumber='.$this->tableNumber
						." and accOwnRoleNumber=".$accOwnRoleNumber		// getAuthToDataTable()の場合に比べて、この行を足した（まだある）
						.' order by status desc';	
			$resultD = mysql_query_logg($queryD);
    
			// RA-Deleteは必ず取得する
			logg_debug("RA-Delete権限をチェックします", "AuthorityGetter", __FUNCTION__);
			$permissionDR = mysql_result($resultD, 0, "permission");
			$raDelete = convAuthToZeroOne($permissionDR);
    
			// IA-Deleteは、アクセスユーザのロールがIA-Definedなら取得、違うならnull
			if($cliRoleIsIaDefined) {
				logg_debug("アクセスユーザのロールはIA-Definedなので、IA-Delete権限をチェックします", "AuthorityGetter", __FUNCTION__);
				$permissionDI = mysql_result($resultD, 1, "permission");
				$iaDelete = convAuthToZeroOne($permissionDI);
			}
			else {
				logg_debug("アクセスユーザのロールはIA-Definedでないので、IA-Delete権限はチェックしません", "AuthorityGetter", __FUNCTION__);
				$iaDelete = null;
			}
    
    
    
			/*
			 * IA割当権限
			 */
			logg_debug("まず、データテーブル「".$tableInfo->nameOnWeb."」に対しIA-Definedなロールがいくついるかチェックします", "AuthorityGetter", __FUNCTION__);
    
			// このアカウントについてIA-Definedなロールをリストアップ、各ロールに対しアクセスユーザのロールがIA割当権限を持つか否かチェック
			$queryIAAA = 'SELECT'
							.' ia_possesion.roleNumber AS iaDefinedRoleNumber,'
			           		.' ia_assign_auth.permission AS permission'
			           		.' FROM ia_possesion JOIN ia_assign_auth'
			           		.' WHERE ia_possesion.tableNumber='.$this->tableNumber
							.' AND ia_possesion.accOwnRoleNumber='.$accOwnRoleNumber				// getAuthToDataTable()の場合に比べて、この行を足した（まだある）
							.' AND ia_possesion.accOwnRoleNumber=ia_assign_auth.accOwnRoleNumber'	// getAuthToDataTable()の場合に比べて、この行を足した（まだある）
			           		.' AND ia_assign_auth.assignerRoleNumber='.$cliRoleNumber
			           		.' AND ia_possesion.tableNumber=ia_assign_auth.tableNumber'
			           		.' AND ia_possesion.accOwnRoleNumber=ia_assign_auth.accOwnRoleNumber'
			           		.' AND ia_possesion.roleNumber=ia_assign_auth.assignedRoleNumber'
			           		.' ORDER BY ia_possesion.roleNumber ASC';
			$resultIAAA = mysql_query_logg($queryIAAA);
			$iaDefinedRoleNum = mysql_num_rows($resultIAAA);
    
			logg_debug("データテーブルに対しIA-Definedなロールは".$iaDefinedRoleNum."ついるようです。", "AuthorityGetter", __FUNCTION__);
    
			// IAアサイン権限を表すハッシュ（IA-Definedなロールがいない場合はNULLのまま）
			$iaAssignHash = null;
    
			// アカウントに対しIA-Definedなロールが１つでもいる -> IA割当権限を取得
			if($iaDefinedRoleNum > 0) {
				$iaAssignHash = array();
				for($i=0; $i<$iaDefinedRoleNum; $i++) {
					$iaDefinedRoleNumber = mysql_result($resultIAAA, $i, 'iaDefinedRoleNumber');
					$permission = mysql_result($resultIAAA, $i, 'permission');
					$iaAssignHash[$iaDefinedRoleNumber] = convAuthToZeroOne($permission);
				}
			}



			// getAuthToDataTable() ほぼ重複 ここまで



			// このアカウント（ないしデータテーブル）に対する権限セット（AuthoritySetインスタンス）を作成し、配列に格納
			$authSet = new AuthoritySet(
				$cAuth,
				$raReadArray, $raWriteArray, $raExWriteArray,
				$raDelete,
				$iaReadArray, $iaWriteArray, $iaExWriteArray,
				$iaDelete,
				$iaAssignHash
			);

			$authSetArray[$accOwnRoleOrd] = $authSet;

			// データテーブルの場合 -> 最初のループにいるはずで、上で取得したAuthoritySetインスタンスを使ってコンストラクタし、それを返して終了
			if($tableInfo->tableType==="data table") {
				if($accOwnRoleOrder!==0) {
					logg_debug_error("データテーブルに対するただ１つの権限セット（AuthoritySetインスタンス）を格納した要素数１の配列を取得したはずなのに、なんか変です。", "AuthorityGetter", __FUNCTION__);
				}
	
				$authToDt = new AuthorityToDataTable($authSet);
				logg_debug_return(__FUNCTION__, "AuthorityGetter");
				return $authToDt;
			}

			// アカウントテーブルの場合 -> $accOwnRoleNumber番ロールのアカウントに対する権限セットを取得できたので、AuthorityToAccountTableインスタンスのハッシュ（インスタンス変数）にキーと値を渡してやる
			// キーは$accOwnRoleNumber、値は上のAuthoritySetインスタンス
			$authToAt->addAuthSetToAccount($accOwnRoleNumber, $authSet);
		}

		// ここまで実行されているなら、「対アカウントテーブル権限を取得しようとしていて、全種類のアカウントに対するAuthoritySetインスタンスを取得しAuthorityToAccountTableインスタンスにセットし終わった」という状態のはず
		// -> あとは返すだけ
		return $authToAt;
	}
}
?>

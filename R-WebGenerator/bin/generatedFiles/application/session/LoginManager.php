<?php






// セッション変数名称
define('SESSION_ROLE_NUMBER', 'roleNumber');
define('SESSION_USER_NUMBER', 'userNumber');



// ログイン成功・失敗
define('LOGIN_SUCCESS', 0);
define('LOGIN_ERROR', 1);
define('LOGIN_UNTRIED', 2);




class LoginManager {
	public function __construct() {
	}





	/*
	 * SUMMARY :
	 * ログインアクセス中かチェックし、そうならば認証を行う。違うなら何もしない
	 */
	public function authenticateIfTried($accountTableNumber, $destPagePath) {
		logg_debug_call(__FUNCTION__, "LoginManager");

		$loginTried = $this->checkLoginTried($accountTableNumber);

		// ログインアクセス中の場合（認証フィールド・パスワードフィールドのテキストボックスの値がPOSTで渡されてきた）
		if($loginTried===true) {
			$res = $this->executeLogin($accountTableNumber);

			// ログイン成功
			if($res===LOGIN_SUCCESS) {
				logg_debug_return(__FUNCTION__, "LoginManager");
				header("Location: " . $destPagePath);
				return;	// いらないと思うけど一応
			}
			// 失敗
			else if($res===LOGIN_ERROR) {
				logg_debug_return(__FUNCTION__, "LoginManager");
				return LOGIN_ERROR;
			}

			logg_debug_error("LoginManager::executeLogin() が不正な値をreturnしました", "LoginManager", __FUNCTION__);
			logg_debug_return(__FUNCTION__, "LoginManager");
			return NULL;
		}

		// ログインアクセス中でない
		logg_debug_return(__FUNCTION__, "LoginManager");
		return LOGIN_UNTRIED;
	}





	/*
	 * SUMMARY :
	 * ユーザがログインアクセス中かをbooleanで返却する
	 * 正確には、認証フィールドのテキストボックスと、パスワードフィールドのテキストボックス、この２つの値がPOSTされているかを返却
	 * 
	 * NOTICE :
	 * この関数は、ログインフォームが設置されたWebページで実行する（違うページではない！）
	*/
	public function checkLoginTried($at_number) {
		logg_debug_call(__FUNCTION__, "LoginManager");

		// 認証フィールド、パスワードフィールドのオフセット取得
		$ato = new AccountTableOperator($at_number);
		$idFieldOffset = $ato->findIdentificationField();
		$passFieldOffset = $ato->findPasswordField();

		// 認証フィールド・パスワードフィールドのテキストボックスの値がPOSTされている -> true
		if(array_key_exists(encryptOffset($idFieldOffset), $_POST) &&
			array_key_exists(encryptOffset($passFieldOffset), $_POST)) {
			logg_debug_return(__FUNCTION__, "LoginManager");
			return true;
		}

		// POSTされていない -> false
		logg_debug_return(__FUNCTION__, "LoginManager");
		return false;
	}





	/*
	 * SUMMARY :
	 * ログインフォームのHTMLを作成、返却
	 * 
	 * NOTICE :
	 * $currentWebPagePathには、必ずログインフォームを出力するページのPHPファイルのパスを渡す
	 * 
	 * MODIFY : $html の値に \n を付加するようにしたい
	*/
	public function makeLoginFormHtml($at_number, $currentWebPagePath, $pePrimKey) {
		logg_debug_call(__FUNCTION__, "LoginManager");

		// TableInformationインスタンス
		$atInfo = new TableInformation($at_number);

		// 認証フィールド、パスワードフィールドのオフセット取得
		$ato = new AccountTableOperator($at_number);
		$idFieldOffset = $ato->findIdentificationField();
		$passFieldOffset = $ato->findPasswordField();

		// 認証フィールド・パスワードフィールドの入力テキストボックスを出力する
		// それぞれのリクエストとしての名前は、アカウントテーブルの認証フィールド・パスワードフィールドのオフセットをencryptOffset()で暗号化したもの
		$html = $atInfo->fieldInfoArray[$idFieldOffset]->nameOnWeb . '：<br>'
				. '<input type="text" name="' . encryptOffset($idFieldOffset) . '"><br>'
				. $atInfo->fieldInfoArray[$passFieldOffset]->nameOnWeb . '：<br>'
				. '<input type="password" name="' . encryptOffset($passFieldOffset) . '"><br>'
				. '<br>';

		$action = $currentWebPagePath."?pepk=".$pePrimKey;
		$javaScriptCode = "(function(){"
							. "document.PageElementsData.action=\"".htmlspecialchars($action)."\";"
							. "document.PageElementsData.submit();"
							. "})()";
		$html .= "<input type='button' value='Login' onClick='".$javaScriptCode."'><br>"	;

		logg_debug_return(__FUNCTION__, "LoginManager");
		return $html;
	}





	/*
	 * SUMMARY :
	 * 入力されたID・パスを受け取りログイン認証を行う
	 */
	public function executeLogin($at_number) {
		logg_debug_call(__FUNCTION__, "LoginManager");

		// アカウントテーブルの情報を持ったTableInformationインスタンスを取得
		$ato = new AccountTableOperator($at_number);
		$atd = $ato->tableDetail;

		// 認証フィールド、パスワードフィールドのオフセットと名前（DB上のもの）を取得
		$idFieldOffset = $ato->findIdentificationField();
		$passFieldOffset = $ato->findPasswordField();
		$idFieldName_db = $atd->fieldInfoArray[$idFieldOffset]->nameOnDb;
		$passFieldName_db = $atd->fieldInfoArray[$passFieldOffset]->nameOnDb;

		// POSTデータ取得
		$inputId = $_POST[encryptOffset($idFieldOffset)];
		$inputPass = $_POST[encryptOffset($passFieldOffset)];

		// 認証クエリ作成・実行
		$query = "SELECT * FROM ".$atd->nameOnDb." WHERE "
					.$idFieldName_db."='".$inputId."' "
					."and ".$passFieldName_db."='".encryptPassword($inputPass)."'";
		$result = mysql_query_logg($query);

		// POSTされた認証フィールド・パスワードフィールド値に一致するレコードが、ただ１つ取得できた -> ログイン成功
		if(mysql_num_rows($result)===1) {
			$roleNumber = mysql_result($result, 0, "roleNumber");
			$userNumber = mysql_result($result, 0, PRIMARY_KEY);

			logg_debug('ログイン成功', "LoginManager", __FUNCTION__);
			logg_debug('ロール番号 : '.$roleNumber, "LoginManager", __FUNCTION__);
			logg_debug('ユーザ番号 : '.$userNumber, "LoginManager", __FUNCTION__);

			$_SESSION[SESSION_ROLE_NUMBER] = $roleNumber;
			$_SESSION[SESSION_USER_NUMBER] = $userNumber;

			logg_debug_return(__FUNCTION__, "LoginManager");
			return LOGIN_SUCCESS;
		}

		// ログイン失敗
		logg_debug('ログイン失敗。認証パラメータかパスワードが違います', "LoginManager", __FUNCTION__);

		logg_debug_return(__FUNCTION__, "LoginManager");
		return LOGIN_ERROR;
	}
}

?>
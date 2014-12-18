<?php



/*
 * SUMMARY :
 * アカウントテーブル特有の情報を調べ、返却してくれるクラス
 * 例：認証メールアドレス（orユーザID）のフィールドのオフセットや、パスワードフィールドのオフセット
 */
class AccountTableOperator {
	// テーブルの詳細情報
	var $tableDetail;





	/*
	 * SUMMARY :
	 * コンストラクタ
	 */
	public function __construct($tableNumber) {
		logg_debug_call(__FUNCTION__, "AccountTableOperator");

		$this->tableDetail = new TableInformation($tableNumber);

		/*
		 * 本当にアカウントテーブルかチェックを入れたいところ
		 */

		logg_debug_return(__FUNCTION__, "AccountTableOperator");
	}
	
	



	/*
	 * SUMMARY :
	 * 認証メールアドレス or ユーザID　のフィールドのオフセットを返却
	*/
	public function findIdentificationField() {
		logg_debug_call(__FUNCTION__, "AccountTableOperator");

		for($i=0; $i<$this->tableDetail->getDdfNum(); $i++) {
			$type = $this->tableDetail->fieldInfoArray[$i]->dataTypeName;

			// 認証メールアドレス型 or ユーザID型 のフィールドだった -> オフセットを返却
			if($type===DataType::TYPE_MAILAUTH || $type===DataType::TYPE_USERID) {
				logg_debug_return(__FUNCTION__, "AccountTableOperator");
				return $i;
			}
		}

		// エラー : アカウントテーブルであるにも関わらず、認証メールアドレス or ユーザID　のフィールドが見つからない
		logg_debug_error('認証メールアドレス or ユーザID　のフィールドが発見できません', "AccountTableOperator", __FUNCTION__);
		logg_debug_return(__FUNCTION__, "AccountTableOperator");
	}



	/*
	 * SUMMARY :
	 * パスワード型のフィールドのオフセット返却
	*/
	public function findPasswordField() {
		logg_debug_call(__FUNCTION__, "AccountTableOperator");

		for($i=0; $i<$this->tableDetail->getDdfNum(); $i++) {
			$type = $this->tableDetail->fieldInfoArray[$i]->dataTypeName;

			// パスワード型 のフィールドだった -> オフセットを返却
			if($type===DataType::TYPE_PASSWORD) {
				logg_debug_return(__FUNCTION__, "AccountTableOperator");
				return $i;
			}
		}

		// エラー : アカウントテーブルであるにも関わらず、パスワードのフィールドが見つからない
		logg_debug_error('パスワードフィールドが発見できません', "AccountTableOperator", __FUNCTION__);
		logg_debug_return(__FUNCTION__, "AccountTableOperator");
	}
}
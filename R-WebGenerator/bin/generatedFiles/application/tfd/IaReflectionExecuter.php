<?php
/*
 * Created on 2010/12/01
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class IaReflectionExecuter {
	// どのDDTのレコード達に対するIAをアサインしたいのか、その番号
	var $ddtNumber;
	// そのDDTに対応するTFD（そのDDTをDDT Loadして得たTFDや、そのDDTへのCreateリフレクションでできたレコード達を示すTFD（つまりCreateリフレクションの出力）など）
	var $tfdFromDdt;

	// どのアカウントテーブルのユーザ達にIAをアサインしたいのか、その番号
	var $atNumber;
	// そのアカウントテーブルに対応するTFD
	var $tfdFromAt;

	/*
	 * ２つのTFDの行数は同じでなければならないので注意
	 */



	
 	function __construct($ddtNumber, $atNumber, $tfdFromDdt, $tfdFromAt) {
		$this->ddtNumber = $ddtNumber;
		$this->atNumber = $atNumber;
		$this->tfdFromDdt = $tfdFromDdt;
		$this->tfdFromAt = $tfdFromAt;
 	}





	/*
	 * SUMMARY :
	 * IAのアサインを行う。正確には、
	 * １．$ddtNumber番のDDTの中から、$tfdFromDdtに対応するレコードを全て見つけ出し、
	 * ２．$atNumber番のアカウントテーブルの中から$tfdFromAtに対応するアカウントを全て見つけ出し、
	 * ２．の上から0,1,2...番目のユーザに、１．の上から0,1,2...番目のレコードに対するIAをアサインする。
	 */
	public function execute() {
		// DDT中の、IAの対象となるレコード達のロケーションを割り出す
		$ddtRrf = new RelatedRecordFinder($this->tfdFromDdt, $this->ddtNumber);
		$ddtRecordLocationArray = $ddtRrf->getRecordLocations();

		// アカウントテーブル中の、IAを持たせるユーザ達のアカウント（レコード）達のロケーションを割り出す
		$atRrf = new RelatedRecordFinder($this->tfdFromAt, $this->atNumber);
		$atRecordLocationArray = $atRrf->getRecordLocations();

		// ２つのレコード群のレコード数は等しくなるはずである。そうでないならエラー
		if(count($ddtRecordLocationArray)!==count($atRecordLocationArray)) {
			logg_debug_error("1201:IAリフレクションを実行しようとしていますが、IAの対象となるDDTレコードの数と、IAを持たせるユーザ達の数が等しくありません", "IaReflectionExecuter", __FUNCTION__);
		}

		// DDTとアカウントテーブルの情報
		$ddtInfo = new TableInformation($this->ddtNumber);
		$atInfo = new TableInformation($this->atNumber);

		for($i=0; $i<count($ddtRecordLocationArray); $i++) {
			$ddtRecordLocation = $ddtRecordLocationArray[$i];
			$ddtRecordPrimKey = $ddtRecordLocation->primaryKey;
			$atRecordLocation = $atRecordLocationArray[$i];
			$atRecordPrimKey = $atRecordLocation->primaryKey;

			// IAをアサインするユーザのロール番号・ユーザ番号を割り出す（もっとも、$atRecordPrimKeyはまさしくユーザ番号なのだが）
			$queryUserInfo = "select * from " . $atInfo->nameOnDb . " where " . PRIMARY_KEY . "=" . $atRecordPrimKey;
			$resultUserInfo = mysql_query_logg($queryUserInfo);
			oneResultCheck($resultUserInfo, "IaReflectionExecuter", __FUNCTION__);
			$roleNumber = mysql_result($resultUserInfo, 0, "roleNumber");
			$userNumber = mysql_result($resultUserInfo, 0, PRIMARY_KEY);

			// IAのアサインを実行
			$iaAssigner = new IaAssigner($roleNumber, $userNumber, $this->ddtNumber, $ddtRecordPrimKey);
			$iaAssigner->assign();
		}
	}
}
?>

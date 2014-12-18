<?php
/*
 * Created on 2010/08/16
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */


class TransitionProcessExecuter {
	// 遷移プロセスの主キー
	var $transProcNumber;

	/*
	 * TPP結果バッファ
	 * 各TPPの実行結果を保持しておくバッファ
	 * 
	 * 例えば
	 * ・DBからDDT全体を読み出し、それを基に作ったTFD（DdtTfdConverterインスタンスがこれを行う）
	 * ・サービスの出力
	 * 
	 * このバッファの実体はハッシュ
	 * キー：Transition Process Part Number
	 * 値：その Transition Process Part の実行結果
	 */
	var $tppOutBuffHash;





	public function __construct($transProcNumber) {
		$this->transProcNumber = $transProcNumber;
		$this->tppOutBuffHash = array();
	}





	public function execute($permitDbInfection) {
		logg_debug_call(__FUNCTION__, "TransitionProcessExecuter");

		// 当遷移プロセスのTPP一覧を取得（TPPN順に上から並べたMySQL結果セット）
		$query = 'SELECT * FROM tpp WHERE'
					. " transitionPrimaryKey=".$this->transProcNumber
					. ' ORDER BY tppn ASC';
		$result = mysql_query_logg($query);

		// TPP総数
		$tppQuantity = mysql_num_rows($result);

		logg_debug("遷移プロセスを実行します。遷移プロセスの番号は".$this->transProcNumber."です。", "TransitionProcessExecuter", __FUNCTION__);
		logg_debug("この遷移プロセス中のTPPは".$tppQuantity."つあります", "TransitionProcessExecuter", __FUNCTION__);

		for($tppOrder=0; $tppOrder<$tppQuantity; $tppOrder++) {
			$tppn = mysql_result($result, $tppOrder, "tppn");
			$kind = mysql_result($result, $tppOrder, "tppKind");

			logg_debug("TPPO ".$tppOrder." のTPPを実行します　種類は ".$kind." です。", "TransitionProcessExecuter", __FUNCTION__);

			// DDT Load
			if($kind==="DDT Load") {
				// DDT番号取得
				$queryDdtLoad = "select * from tpp_et_load where tppn=".$tppn;
				$resultDdtLoad = mysql_query_logg($queryDdtLoad);
				oneResultCheck($resultDdtLoad, "TransitionProcessExecuter", __FUNCTION__);
				$ddtNumber = mysql_result($resultDdtLoad, 0, "etNumber");

				// 読出、バッファへ格納
				$dtc = new DdtTfdConverter();
				$tabularFormData = $dtc->convDdtToTfd($ddtNumber, $tppn);
				$this->tppOutBuffHash[$tppn] = $tabularFormData;
			}
			// 通常のCreateリフレクション
			else if($kind==="Create Reflection") {
				// リフレクションなので（データベースに変更が生じるので）、
				// 「さっきいたページから現在アクセスしているページへ来るときの遷移プロセス」中でだけ実行する
				if($permitDbInfection===true) {
					logg_debug("1201:Create Reflection の準備をしています。", "TransitionProcessExecuter", __FUNCTION__);

					// このTPPに関する情報を、Create Reflection一覧テーブルから取得
					$queryCfr = "select * from tpp_create_reflection where tppn=".$tppn;
					$resultCfr = mysql_query_logg($queryCfr);
					oneResultCheck($resultCfr, "TransitionProcessExecuter", __FUNCTION__);
    
					// Createリフレクション対象のDDTの番号
					$ddtNumber = mysql_result($resultCfr, 0, "tableNumber");

					// Createリフレクションに用いるTFDのTPPN
					$createTfdTppn = mysql_result($resultCfr, 0, "tfdTppn");
					$createTfd = $this->tppOutBuffHash[$createTfdTppn];
    
					// レコード作成実行（作成された全レコードの主キーが配列で返ってくるので格納する）
					$executer = new CreateReflectionExecuter($createTfd, $ddtNumber);
					$createdRecPrimKeyArray = $executer->execute();

					// 作成された全レコードを改めてDDTから読み出し、TFDにする。そしてTPP結果バッファへ格納
					$dtc = new DdtTfdConverter();
					$dtc->shiftToPrimKeysMode($createdRecPrimKeyArray);
					$this->tppOutBuffHash[$tppn] = $dtc->convDdtToTfd($ddtNumber, $tppn);

					logg_debug("1201:Create Reflection を完了しました。", "TransitionProcessExecuter", __FUNCTION__);
				}
			}
			// 標準Createフォームを用いたCreateリフレクション
			else if($kind==="Create Form Reflection") {
				// リフレクションなので（データベースに変更が生じるので）、
				// 「さっきいたページから現在アクセスしているページへ来るときの遷移プロセス」中でだけ実行する
				if($permitDbInfection===true) {
					logg_debug("1201:Create Form Reflection の準備をしています。", "TransitionProcessExecuter", __FUNCTION__);

					// このTPPに関する情報を、Create Form Reflection一覧テーブルから取得
					$queryCfr = "select * from tpp_create_form_reflection where tppn=".$tppn;
					$resultCfr = mysql_query_logg($queryCfr);
					oneResultCheck($resultCfr, "TransitionProcessExecuter", __FUNCTION__);
    
					// Create Form（ページエレメント）の主キーを取得
					$createFormPrimKey = mysql_result($resultCfr, 0, "pePrimaryKey");
    
					// Create Form からの全リクエストを受け取り、TFDに変換
					$cfRequestsTfd = CreateReflectionExecuter::convCreFormToTfd($createFormPrimKey);
    
					// レコードを作成するDDTの番号取得
					$queryDdtNumber = "select * from page_element_create_form where pePrimaryKey=".$createFormPrimKey;
					$resultDdtNumber = mysql_query_logg($queryDdtNumber);
					oneResultCheck($resultDdtNumber, "TransitionProcessExecuter", __FUNCTION__);
					$ddtNumber = mysql_result($resultDdtNumber, 0, "tableNumber");
    
					// レコード作成実行（作成された全レコードの主キーが配列で返ってくるので格納する
					$executer = new CreateReflectionExecuter($cfRequestsTfd, $ddtNumber);
					$createdRecPrimKeyArray = $executer->execute();

					// 作成された全レコードを改めてDDTから読み出し、TFDにする。そしてTPP結果バッファへ格納
					$dtc = new DdtTfdConverter();
					$dtc->shiftToPrimKeysMode($createdRecPrimKeyArray);
					$this->tppOutBuffHash[$tppn] = $dtc->convDdtToTfd($ddtNumber, $tppn);

					logg_debug("1201:Create Form Reflection を完了しました。", "TransitionProcessExecuter", __FUNCTION__);
				}
			}
			// 標準Updateフォームを用いたUpdateリフレクション
			else if($kind==="Update Form Reflection") {
				// リフレクションなので（データベースに変更が生じるので）、
				// 「さっきいたページから現在アクセスしているページへ来るときの遷移プロセス」中でだけ実行する
				if($permitDbInfection===true) {
					logg_debug("Update Form Reflection の準備をしています。", "TransitionProcessExecuter", __FUNCTION__);

					// このTPPに関する情報を、Update Form Reflection一覧テーブルから取得
					$queryUfr = "select * from tpp_update_form_reflection where tppn=".$tppn;
					$resultUfr = mysql_query_logg($queryUfr);
					oneResultCheck($resultUfr, "TransitionProcessExecuter", __FUNCTION__);
    
					// Update Form（ページエレメント）の主キーを取得
					$updateFormPrimKey = mysql_result($resultUfr, 0, "pePrimaryKey");
    
					// Update Form からの全リクエストを受け取り、TFDに変換
					$ufRequestsTfd = UpdateReflectionExecuter::convUpdFormToTfd($updateFormPrimKey);
    
					// 更新対象のDDTの番号取得
					$queryDdtNumber = "select * from page_element_update_form where pePrimaryKey=".$updateFormPrimKey;
					$resultDdtNumber = mysql_query_logg($queryDdtNumber);
					oneResultCheck($resultDdtNumber, "TransitionProcessExecuter", __FUNCTION__);
					$ddtNumber = mysql_result($resultDdtNumber, 0, "tblNumber");
    
					/*
					 * 以下、TFDの内容で対象のDDTを更新する
					 */

					$executer = new UpdateReflectionExecuter($ufRequestsTfd, $ddtNumber);

					// Updateフォームから作り出したTFDの行数・列数は、DDTのレコード数・DDF数に一致しているはず（そうでないならエラー）
					if($ufRequestsTfd->getVisibleFieldNum()!==$executer->tableInformation->getDdfNum()) {
						logg_debug_error("TPP 'Update Form Reflection'の実行準備中ですが、Updateフォームから作り出したTFDの列数とDDTのDDF数が一致しません", "TransitionProcessExecuter", __FUNCTION__);
					}
					// TFDの0,1,2...番目の列でDDTの0,1,2...番目のDDFを更新するよう設定する
					for($i=0; $i<$ufRequestsTfd->getVisibleFieldNum(); $i++) {
						$executer->addOffsetRelation($i, $i);
					}
					$executer->execute();

					logg_debug("Update Form Reflection を完了しました。", "TransitionProcessExecuter", __FUNCTION__);
				}
			}
			// IAリフレクション
			else if($kind==="IA Reflection") {
				// リフレクションなので（データベースに変更が生じるので）、
				// 「さっきいたページから現在アクセスしているページへ来るときの遷移プロセス」中でだけ実行する
				if($permitDbInfection===true) {
					logg_debug("1201:IA Reflection の準備をしています。", "TransitionProcessExecuter", __FUNCTION__);

					// このTPPに関する情報を、tpp_ia_reflectionテーブルから取得
					$queryIar = "select * from tpp_ia_reflection where tppn=".$tppn;
					$resultIar = mysql_query_logg($queryIar);
					oneResultCheck($resultIar, "TransitionProcessExecuter", __FUNCTION__);
    
					$ddtNumber = mysql_result($resultIar, 0, "ddtNumber");
					$ddtTfdTppn = mysql_result($resultIar, 0, "ddtTfdTppn");
					$ddtTfd = $this->tppOutBuffHash[$ddtTfdTppn];
					$atNumber = mysql_result($resultIar, 0, "atNumber");
					$atTfdTppn = mysql_result($resultIar, 0, "atTfdTppn");
					$atTfd = $this->tppOutBuffHash[$atTfdTppn];

					$ire = new IaReflectionExecuter($ddtNumber, $atNumber, $ddtTfd, $atTfd);
					$ire->execute();
    
					logg_debug("1201:IA Reflection を完了しました。", "TransitionProcessExecuter", __FUNCTION__);
				}
			}
			// サービス実行
			else if($kind===TBL_TPP_KIND_SERVICEEXECUTION) {
				// サービス番号取得
				$query_showTppnSe = "select serviceNumber from tpp_service_execution"
				                  . " where tppn=".$tppn;
				$result_showTppnSe = mysql_query_logg($query_showTppnSe);
				$serviceNumber = mysql_result($result_showTppnSe, 0, 0);

				$query_showArguments = "select argTppn from tpp_service_execution_argument"
				                     . " where tppn=" . $tppn
				                     . " order by argNumber ASC";
				$result_showArguments = mysql_query_logg($query_showArguments);
				$argNum = mysql_num_rows($result_showArguments);

				$argArray = array();
				for($j=0; $j<$argNum; $j++) {
					$argTppn = mysql_result($result_showArguments, $j, 0);
					$argArray[$j] = $this->getTppResult($argTppn);
				}

				// Service 実行ステートメント作成、eval()で実行
				$cse = new ServiceExecuter($tppn);
				$serviceOutput = null;
				$serviceName = $cse->getServiceFunctionName($serviceNumber);
				$exeState = '$serviceOutput = $cse->' . $serviceName . '(';
				for($j=0; $j<$argNum; $j++) {
					$exeState .= '$argArray[' . $j . '],';
				}
				$exeState = substr($exeState, 0, -1);	// 最後の , をカット
				$exeState .= ');';

				// Service実行、結果格納
				eval($exeState);
				$this->tppOutBuffHash[$tppn] = $serviceOutput;
			}
			// Display Area の Tabular Form Data を再導出
			else if($kind===TBL_TPP_KIND_DAINPUT) {
				// Display Area のインプットであるTPPのTPPNを取得
				$query_getDaInputTppn = 'select ' . TBL_PEDA . '.' . TBL_PEDA_TPPN
				                      . ' from ' . TBL_TPPDAINPUT . ' join ' . TBL_PEDA
				                      . ' where ' . TBL_TPPDAINPUT . '.' . TBL_TPPDAINPUT_PEPRIMKEY
				                      . '=' . TBL_PEDA . '.' . TBL_PEDA_PEPRIMKEY;
				$result_getDaInputTppn = mysql_query_logg($query_getDaInputTppn);
				$daInputTppn = mysql_result($result_getDaInputTppn, 0, 0);

				// その TPPが属する（つまり一つ前の）Transition Process を実行
				// ただしDBへの副作用はもたらさないよう false を渡す
				$prevTpNumber = TransitionProcessExecuter::getTpNumberOfTpp($daInputTppn);
				$prevTpe = new TransitionProcessExecuter($prevTpNumber);
				$prevTpe->execute(false);

				// 再計算されたインプットをハッシュにセット
				$recalculatedResult = $prevTpe->getTppResult($daInputTppn);
				$this->tppOutBuffHash[$tppn] = $recalculatedResult;
			}
			// INT定数配列の読出
			else if($kind==="Const Array Int") {
				// tpp_constant_array_intテーブルからINT定数配列の全要素の情報を取得
				$queryCai = "select * from tpp_constant_array_int"
				                    . " where tppn=".$tppn
				                    . " order by elementNumber asc";
				$resultCai = mysql_query_logg($queryCai);

				$intArray = array();
				$elementNum = mysql_num_rows($resultCai);
				for($intElemOrd=0; $intElemOrd<$elementNum; $intElemOrd++) {
					$value = mysql_result($resultCai, $intElemOrd, "value");
					$intArray[$intElemOrd] = $value;
				}

				$this->tppOutBuffHash[$tppn] = $intArray;
			}
			// STRING定数配列の読出
			else if($kind==="Const Array String") {
				// tpp_constant_array_stringテーブルからSTRING定数配列の全要素の情報を取得
				$queryCas = "select * from tpp_constant_array_string"
				                    . " where tppn=".$tppn
				                    . " order by elementNumber asc";
				$resultCas = mysql_query_logg($queryCas);

				$stringArray = array();
				$elementNum = mysql_num_rows($resultCas);
				for($strElemOrd=0; $strElemOrd<$elementNum; $strElemOrd++) {
					$value = mysql_result($resultCas, $strElemOrd, "value");
					$stringArray[$strElemOrd] = $value;
				}

				$this->tppOutBuffHash[$tppn] = $stringArray;
			}
			


			/*
			 * NOTICE :
			 * DBに副作用をもたらすTPPは、$permitDbInfectionがTRUEのときのみ実行
			 */
		}

		logg_debug_return(__FUNCTION__, "TransitionProcessExecuter");
	}





	/*
	 * SUMMARY :
	 * 指定した Transition Process Part の実行結果を返す
	 * 
	 * ASSUMPTION :
	 * execute() を実行済みであること
	 */
	public function getTppResult($tppn) {
		return $this->tppOutBuffHash[$tppn];
	}





	/*
	 * SUMMARY :
	 * 特定の TPP が属する Transition Process の番号を返す
	 */
	public static function getTpNumberOfTpp($tppn) {
		$query = 'select ' . TBL_TPP_TRANSITION_PRIMARY_KEY . ' from ' . TBL_TPP
		       . ' where ' . TBL_TPP_TPPN . '=' . $tppn;
		$result = mysql_query_logg($query);
		$number = mysql_result($result, 0, 0);
		
		return $number;
	}
}
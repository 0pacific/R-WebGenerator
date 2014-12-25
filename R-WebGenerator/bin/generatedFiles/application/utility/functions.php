<?php

/******************************************************************************************************
このファイルを取り込む場合、続けてproperty.phpも取り込むこと。
（property.phpでdefineされている定数を用いるため）
******************************************************************************************************/



/*
 * ログ取り＋デバッグ複合関数群
 */
function logg_debug($msg, $className=NULL, $funcName=NULL) {
	logg($msg, $className, $funcName);
	debug_line($msg, $className, $funcName);
}
function logg_debug_error($msg, $className=NULL, $funcName=NULL) {
	error_logg($msg, $className, $funcName);
	debug_error($msg, $className, $funcName);
}
function logg_debug_call($funcName, $className=NULL) {
	if(DEBUG_CALL_RETURN===1) {
		logg_call($funcName, $className);
		debug_call($funcName, $className);
	}
}
function logg_debug_return($funcName, $className=NULL) {
	if(DEBUG_CALL_RETURN===1) {
		return_logg($funcName, $className);
		debug_return($funcName, $className);
	}
}





/*
 * デバッグ関数群
 */
function debug_line($msg, $className=NULL, $funcName=NULL) {
	if(DEBUG===1) {
		if($className!==NULL && $funcName!==NULL) {
			$msg = $className . " : " . $funcName . "() ... " . $msg;
		}
		echoBr('<font color="' . DEBUG_OUTPUT_COLOR . '">' . $msg . '</font>');
	}
}
function debug_error($msg, $className=NULL, $funcName=NULL) {
	debug_line('◆ERROR : ' . $msg . " ... $className : $funcName()");
}
function debug_call($funcName, $className=NULL) {
	debug_line('◇CALL : ' . $funcName . '() ' . $className);
}
function debug_return($funcName, $className=NULL) {
	debug_line('◇RETURN : ' . $funcName . '() ' . $className);
}
function debug_mcl($funcName) {
	debug_line('MODULE CALL : ' . $funcName . '()');
}
function debug_mrl($funcName) {
	debug_line('MODULE RETURN : ' . $funcName . '()');
}
function debug_varDump($funcName, $varName, $var) {
	if(DEBUG===1) {
		if(DEBUG_VARDUMP===1) {
			echo '<font color="' . DEBUG_OUTPUT_COLOR . '">';
			echo 'DEBUG : ' . $funcName . '()内の変数 ' . $varName;
			echo '<pre>';
			print_r($var);
			echo '</pre>';
			echo '</font>';
			echo '<br>';
		}
	}
}






/*
 * ログ取り関数群
 */
function logg($message, $className=NULL, $funcName=NULL) {
	if($className!==NULL && $funcName!==NULL) {
		$message .= " ... ".$className."::".$funcName."()";
	}

	// グローバル変数 $pearLogger は、require.php で定義されている
	// これをロガーとして使っていくようにしている
	//$GLOBALS["pearLogger"]->log($message);
}

// SQLクエリ実行 + ログ取り
function mysql_query_logg($query) {
	$result = mysql_query($query);

	logg('mysql_query() : ' . $query);
	debug_line('QUERY : ' . htmlspecialchars($query));

	if($result===FALSE) {
		logg_debug_error('mysql_query() returned FALSE... Message : ' . mysql_error(), NULL, NULL);
	}

	return $result;
}


function oneResultCheck($result, $className, $funcName) {
	if(mysql_num_rows($result)!==1) {
		logg_debug_error("MySQL結果セットがちょうど１行になってません", $className, $funcName);
	}
}


function error_logg($message, $className=null, $funcName=null) {
	$msg = '◆ERROR : ';
	if($className!==null && $funcName!==null) {
		$msg = $className . '::' . $funcName . '() : ';
	}
	$msg .= $message;
	logg($msg);
}

function logg_call($funcName) {
	logg('CALL : ' . $funcName . '()');
}

function return_logg($funcName) {
	logg('RETURN : ' . $funcName . '()');
}

function module_logg_call($funcName) {
	logg('MODULE CALL : ' . $funcName . '()');
}

function module_return_logg($funcName) {
	logg('MODULE RETURN : ' . $funcName . '()');
}









function encryptPassword($password) {
	return md5($password);
}

function encryptOffset($offset) {
	return md5($offset);
}

// MySQLコネクション確立
function get_mysql_link() {
	$mysql_link = mysql_connect('localhost', DB_USER, DB_PASS);
	mysql_select_db(DB_NAME);
	mysql_query("SET NAMES utf8");

	return $mysql_link;
}



// <br>を付加して出力
function echoBr($str='') {
	echo $str . "<br>\n";
}

function echoPre() {
	echo "<pre>";
}

function echoPreEnd() {
	echo "</pre>";
}



function convKaigyoToBr($str) {
	return str_replace("\n", "<br>", $str);
}


function convAuthToZeroOne($value) {
	if($value==='YES') {
		return 1;
	}
	else if($value==='NO') {
		return 0;
	}

	// エラー
	logg_debug_error("YES, NO の値が不正です。", null, __FUNCTION__);
	return null;
}

?>
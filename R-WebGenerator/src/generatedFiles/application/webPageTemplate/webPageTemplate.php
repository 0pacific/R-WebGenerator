<?php
/*
 * Created on 2010/11/19
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

require "./auth/AuthorityGetter.php";
require "./auth/AuthoritySet.php";
require "./auth/AuthorityToDataTable.php";
require "./auth/AuthorityToAccountTable.php";
require "./auth/CellAuthGetter.php";
require "./auth/FieldAuthSet.php";
require "./auth/IaAssignmentChecker.php";
require "./auth/IaAssigner.php";
require "./auth/TransitionAuthorityManager.php";
require "./computation/ServiceExecuter.php";
require "./error/Error.php";
require "./error/CreationFailureError.php";
require "./error/CurrentPasswordMismatchError.php";
require "./error/LengthImproperError.php";
require "./error/PasswordDiscordError.php";
require "./html/CreationFormMaker.php";
require "./html/HyperLinkHtmlCreater.php";
require "./html/PageHtmlGenerator.php";
require "./html/TextHtmlCreater.php";
require "./html/TfdHtmlCreator.php";
require "./html/UpdateFormGenerator.php";
require "./table/AccountTableOperator.php";
require "./table/DataType.php";
require "./table/FieldInformation.php";
require "./table/TableInformation.php";
require "./tfd/CellLocation.php";
require "./tfd/CreateReflectionExecuter.php";
require "./tfd/DdtTfdConverter.php";
require "./tfd/NoUpdate.php";
require "./tfd/NullData.php";
require "./tfd/TabularFormData.php";
require "./tfd/UpdateReflectionExecuter.php";
require "./tfd/IaReflectionExecuter.php";
require "./tfd/RecordHistory.php";
require "./tfd/RecordLocation.php";
require "./tfd/RelatedRecordFinder.php";
require "./tfd/UserInfo.php";
require "./transProc/TransitionProcessExecuter.php";
require "./transProc/TransitionManager.php";
require "./role/RoleInfo.php";
require "./session/ClientInfo.php";
require "./session/LoginManager.php";
require "./session/LogoutManager.php";
require "./session/SessionManager.php";
require "./tfd/FileValue.php";
require "./utility/FilePathManager.php";
require "./utility/functions.php";
require "./utility/functions_guest.php";
require "./utility/functions_session.php";
require "./utility/property.php";





// ログ取り
require_once "Log.php";
define("LOG_FILE", "utility/log.txt");
$pearLogger = &Log::factory("file", LOG_FILE, "PEAR::Log");

// MySQLコネクション確立
get_mysql_link();

// セッション開始ないし復元
startSession();

// もしもログアウトアクセスならば、セッション情報からロール番号やユーザ番号を消し去り、ゲストロール番号を書き込む
$logoutManager = new LogoutManager();
$logoutManager->updateSessionIfLogoutAccessed();


$htmlGenerator = new PageHtmlGenerator(PAGE_NUMBER);
$html = $htmlGenerator->generate();
echo $html;


?>

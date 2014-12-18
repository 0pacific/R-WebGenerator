<?php

// データベースの名称、ユーザ、パスワード
// MySQLのデータベース名やrootパスワードがここに記されているものと一致していないと動作しません
define("DB_NAME", "hoge");
define("DB_USER", "root");
define("DB_PASS", "ttlab");

// 日本語モード（trueで日本語、falseで英語になります）
define("JAPANESE", false);

// デバッグモード
define('DEBUG', 0);
define('DEBUG_CALL_RETURN', 0);
define('DEBUG_VARDUMP', 0);

// Webページへのデバッグ出力の文字色
define('DEBUG_OUTPUT_COLOR', 'lightgray');


// あらゆる処理の成功を表す数値
define('SUCCESS', 0);

// 下記は各種エラーを表す数値



// DB上で、主キーを格納するフィールドは、必ずこの名前にしている
define('PRIMARY_KEY', "primaryKey");

// テーブル詳細情報を記録するテーブル
define('DB_TBL_TABLE_LIST', 'table_list');
define('TABLE_DETAIL_FIELD_NUMBER', 'number');
define('TABLE_DETAIL_FIELD_MDT', 'metadata_table');

// Create権限を記載したテーブル
define('TABLE_CREATE_AUTH', 'create_auth');
define('TCA_FIELD_TABLE_NUMBER', 'tableNumber');
define('TCA_FIELD_ROLE_NUMBER', 'roleNumber');
define('TCA_FIELD_PERMISSION', 'permission');
define('TCA_PERMISSION_TRUE', 'YES');
define('TCA_PERMISSION_FALSE', 'NO');

// Delete権限を記載したテーブル
define('TABLE_DELETE_AUTH', 'delete_auth');
define('TDA_FIELD_TABLE_NUMBER', 'tableNumber');
define('TDA_FIELD_ROLE_NUMBER', 'roleNumber');
define('TDA_FIELD_STATUS', 'status');
define('TDA_STATUS_SELF', 'self');
define('TDA_STATUS_ROLE', 'role');
define('TDA_FIELD_PERMISSION', 'permission');
define('TDA_PERMISSION_TRUE', 'YES');
define('TDA_PERMISSION_FALSE', 'NO');

// 対フィールド権限（Read,Write,exWrite）を記載したテーブル
define('TBL_FAUTH', 'field_auth');
define('TBL_FAUTH_TABLE_NUMBER', 'tableNumber');
define('TBL_FAUTH_FIELD_NUMBER', 'fieldNumber');
define('TBL_FAUTH_ROLE_NUMBER', 'roleNumber');
define('TBL_FAUTH_READ', 'ra');
define('TBL_FAUTH_WRITE', 'wa');
define('TBL_FAUTH_EX_WRITE', 'ea');
define('TBL_FAUTH_STATUS', 'status');
define('TBL_FAUTH_STATUS_SELF', 'self');
define('TBL_FAUTH_STATUS_ROLE', 'role');
define('TBL_FAUTH_PERMISSION_TRUE', 'YES');
define('TBL_FAUTH_PERMISSION_FALSE', 'NO');

// ロールに関する大まかな情報を記録するテーブル
define('ROLE_TABLE', 'role');
define('ROLE_TABLE_FIELD_ROLE_NUMBER', 'number');
define('ROLE_TABLE_FIELD_NAME', 'name');
define('ROLE_TABLE_FIELD_AT_NUMBER', 'at_number');

// ゲストロールの番号を記録するテーブル
define('TABLE_GUEST_ROLE_NUMBER', 'general_role');

// Field Information Table の各フィールドの名称等
define('TBL_FIT_OFFSET', 'offset');
define('TBL_FIT_DDF', 'DDF');
define('TBL_FIT_DDF_TRUE', 'YES');
define('TBL_FIT_DDF_FALSE', 'NO');
define('TBL_FIT_TYPE', 'type');
define('TBL_FIT_MAX', 'max');
define('TBL_FIT_MIN', 'min');
define('TBL_FIT_WEBNAME', 'name_web');

// ロール・テーブル間のIAの有無を記録するテーブル
define('IA_TABLE', 'ia');
define('IT_FIELD_TABLE_NUMBER', 'tableNumber');
define('IT_FIELD_ROLE_NUMBER', 'roleNumber');
define('IT_FIELD_HAS_IA', 'hasIA');
define('IT_FIELD_HAS_IA_TRUE', 'YES');
define('IT_FIELD_HAS_IA_FALSE', 'NO');

// IAのアサインを記録するテーブル
define('TABLE_IA_ASSIGNMENT', 'ia_assign');
define('IAT_FIELD_TABLE_NUMBER', 'tableNumber');
define('IAT_FIELD_RECORD_NUMBER', 'recordPrk');
define('IAT_FIELD_ROLE_NUMBER', 'roleNumber');
define('IAT_FIELD_USER_NUMBER', 'userNumber');

// 各 Transition Process 内の Transition Process Part について、順序と種類を記録するテーブル
define('TBL_TPP', 'tpp');
define('TBL_TPP_TRANSITION_PRIMARY_KEY', 'transitionPrimaryKey');
define('TBL_TPP_TPPN', 'tppn');
define('TBL_TPP_TPP_KIND', 'kind');
define('TBL_TPP_KIND_SERVICEEXECUTION', 'Service Execution');
define('TBL_TPP_KIND_DAINPUT', 'Display Area Input');
define('TBL_TPP_KIND_CONST_ARRAY_INT', 'Const Array Int');

// テーブル "tpp_et_load"
define('TBL_TPPETLOAD', 'tpp_et_load');
define('TBL_TPPETLOAD_TPPN', 'tppn');
define('TBL_TPPETLOAD_ETNUMBER', 'etNumber');

// テーブル "tpp_da_input"
define('TBL_TPPDAINPUT', 'tpp_da_input');
define('TBL_TPPDAINPUT_TPPN', 'tppn');
define('TBL_TPPDAINPUT_PEPRIMKEY', 'pePrimaryKey');

// テーブル "tpp_const_array_int"
define('TBL_TPPCAI', 'tpp_constant_array_int');
define('TBL_TPPCAI_TPPN', 'tppn');
define('TBL_TPPCAI_ELEMENTNUMBER', 'elementNumber');
define('TBL_TPPCAI_VALUE', 'value');

// テーブル "page_element"
define('TBL_PE', 'page_element');
define('TBL_PE_PAGENUMBER', 'pageNumber');
define('TBL_PE_PENUMBER', 'peNumber');
define('TBL_PE_KIND', 'kind');
define('TBL_PE_KIND_DA', 'Display Area');
define('TBL_PE_KIND_LF', 'Login Form');
define('TBL_PE_KIND_IAAF', 'IA Assignment Form');
define('TBL_PE_PRIMKEY', 'primaryKey');

// テーブル "page_element_da"
define('TBL_PEDA', 'page_element_da');
define('TBL_PEDA_PEPRIMKEY', 'pePrimaryKey');
define('TBL_PEDA_TPPN', 'tppn');

// テーブル "transition"
define('TBL_TRANS', 'transition');
define('TBL_TRANS_PRIMKEY', 'primaryKey');
define('TBL_TRANS_TSPNUMBER', 'tspNumber');
define('TBL_TRANS_TSPPENUMBER', 'tspPeNumber');
define('TBL_TRANS_TEPNUMBER', 'tepNumber');

// INT型テキストボックスのサイズ
define('SIZE_TEXTBOX_INT', 5);

// Field Information Table におけるデータタイプ表記
define('FIT_DATATYPE_INT'      , 'int');
define('FIT_DATATYPE_VARCHAR'  , 'varchar');
define('FIT_DATATYPE_DATETIME' , 'datetime');
define('FIT_DATATYPE_DATE'     , 'date');
define('FIT_DATATYPE_TIME'     , 'time');
define('FIT_DATATYPE_ENUM'     , 'enum');
define('FIT_DATATYPE_FILE'     , 'file');
define('FIT_DATATYPE_PASSWORD' , 'password');
define('FIT_DATATYPE_MAIL'     , 'mail');
define('FIT_DATATYPE_MAILAUTH' , 'mail_authentication');
define('FIT_DATATYPE_PRIMKEY'  , 'primaryKey');

// テーブル 'create_auth'
define('TBL_CAUTH', 'create_auth');
define('TBL_CAUTH_TABLENUMBER', 'tableNumber');
define('TBL_CAUTH_ROLENUMBER', 'roleNumber');
define('TBL_CAUTH_PERMISSION', 'permission');

// テーブル 'delete_auth'
define('TBL_DAUTH', 'delete_auth');
define('TBL_DAUTH_TABLENUMBER', 'tableNumber');
define('TBL_DAUTH_ROLENUMBER', 'roleNumber');
define('TBL_DAUTH_STATUS', 'status');
define('TBL_DAUTH_STATUS_SELF', 'self');
define('TBL_DAUTH_STATUS_ROLE', 'role');
define('TBL_DAUTH_PERMISSION', 'permission');

// テーブル 'ia_possesion'
define('TBL_IAPOSS', 'ia_possesion');
define('TBL_IAPOSS_TABLENUMBER', 'tableNumber');
define('TBL_IAPOSS_ROLENUMBER', 'roleNumber');
define('TBL_IAPOSS_VALUE', 'hasIA');
define('TBL_IAPOSS_VALUE_TRUE', 'YES');
define('TBL_IAPOSS_VALUE_FALSE', 'NO');
define('TBL_IAPOSS_PRIMKEY', 'primKey');

// テーブル 'ia_assign_auth'
define('TBL_IAAA', 'ia_assign_auth');
define('TBL_IAAA_TRPRIMKEY', 'tableRolePrimKey');
define('TBL_IAAA_ROLENUMBER', 'roleNumber');
define('TBL_IAAA_PERMISSION', 'permission');


// ファイル格納ディレクトリのパス
define('DIRECTORY_FILES', 'Files/');

?>
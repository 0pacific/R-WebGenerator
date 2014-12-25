-- 全テーブルを（存在するなら）削除 --

drop table if exists create_auth, delete_auth, exwrite_lock, field_auth, field_info, general_role, ia_assign, ia_assign_auth, ia_possesion, logout_trans, pages, page_element, page_element_create_form, page_element_da, page_element_hyper_link, page_element_login_form,page_element_table_display, page_element_table_display_field, page_element_text, page_element_update_form,page_element_update_form_field, page_element_saif, role, table_list, tpp, tpp_constant_array_int, tpp_constant_array_string, tpp_create_form_reflection, tpp_create_reflection, tpp_da_input, tpp_et_load, tpp_ia_reflection, tpp_service_execution, tpp_service_execution_argument, tpp_update_form_reflection, tpp_update_reflection, transition, transition_auth, dt0, at0, dt1, at1, dt2, at2, dt3, at3, dt4, at4, dt5, at5, dt6, at6, dt7, at7, dt8, at8, dt9, at9, dt10, at10, dt11, at11, dt12, at12, dt13, at13, dt14, at14, dt15, at15, dt16, at16, dt17, at17, dt18, at18, dt19, at19, dt20, at20, dt21, at21, dt22, at22, dt23, at23, dt24, at24, dt25, at25, dt26, at26, dt27, at27, dt28, at28, dt29, at29, dt30, at30, dt31, at31, dt32, at32, dt33, at33, dt34, at34, dt35, at35, dt36, at36, dt37, at37, dt38, at38, dt39, at39, dt40, at40, dt41, at41, dt42, at42, dt43, at43, dt44, at44, dt45, at45, dt46, at46, dt47, at47, dt48, at48, dt49, at49, dt50, at50, dt51, at51, dt52, at52, dt53, at53, dt54, at54, dt55, at55, dt56, at56, dt57, at57, dt58, at58, dt59, at59, dt60, at60, dt61, at61, dt62, at62, dt63, at63, dt64, at64, dt65, at65, dt66, at66, dt67, at67, dt68, at68, dt69, at69, dt70, at70, dt71, at71, dt72, at72, dt73, at73, dt74, at74, dt75, at75, dt76, at76, dt77, at77, dt78, at78, dt79, at79, dt80, at80, dt81, at81, dt82, at82, dt83, at83, dt84, at84, dt85, at85, dt86, at86, dt87, at87, dt88, at88, dt89, at89, dt90, at90, dt91, at91, dt92, at92, dt93, at93, dt94, at94, dt95, at95, dt96, at96, dt97, at97, dt98, at98, dt99, at99;


CREATE TABLE `pages` (
`pageNumber` int(11) NOT NULL DEFAULT '0',
`fileName` varchar(255) NOT NULL DEFAULT '',
PRIMARY KEY (`pageNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into pages (pageNumber, fileName) values(0,'index.php');
insert into pages (pageNumber, fileName) values(1,'read.php');
insert into pages (pageNumber, fileName) values(2,'sum.php');
insert into pages (pageNumber, fileName) values(3,'register.php');
insert into pages (pageNumber, fileName) values(4,'login.php');
insert into pages (pageNumber, fileName) values(5,'answer.php');
insert into pages (pageNumber, fileName) values(6,'test.php');


CREATE TABLE IF NOT EXISTS `logout_trans` (
`destPageNumber` int(11) NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `logout_trans` (`destPageNumber`) VALUES (0);


CREATE TABLE IF NOT EXISTS `table_list` (
`primaryKey` tinyint(4) NOT NULL DEFAULT '0',
`type` enum('data table','account table') NOT NULL DEFAULT 'data table',
`name` varchar(32) NOT NULL DEFAULT '',
`nameOnWeb` varchar(32) NOT NULL DEFAULT '',
PRIMARY KEY (`primaryKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `table_list` (`primaryKey`, `type`, `name`, `nameOnWeb`) VALUES
(0, 'data table', 'dt0', 'Questionnaire'),
(1, 'account table', 'at1', 'Answerer Account');


DROP TABLE IF EXISTS `dt0`;
CREATE TABLE IF NOT EXISTS `dt0` (
`field0` varchar(16) NOT NULL DEFAULT '',
`field1` int(11) DEFAULT NULL,
`field2` int(11) DEFAULT NULL,
`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (`primaryKey`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `at1`;
CREATE TABLE IF NOT EXISTS `at1` (
`field0` varchar(16) NOT NULL DEFAULT '',
`field1` blob NOT NULL,
`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT,
`roleNumber` int(11) NOT NULL default '0',
PRIMARY KEY (`primaryKey`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;;


CREATE TABLE IF NOT EXISTS `field_info` (
`tblNumber` int(11) NOT NULL DEFAULT '0',
`offset` int(11) NOT NULL DEFAULT '0',
`developerDefined` enum('YES','NO') NOT NULL DEFAULT 'YES',
`dataType` enum('int','varchar','datetime','date','time','enum','file','mail','mail_auth','user_id','password','primaryKey','role_name','role_number') NOT NULL DEFAULT 'int',
`nameOnWeb` varchar(255) DEFAULT NULL,
`min` int(11) DEFAULT NULL,
`max` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `field_info` (`tblNumber`, `offset`, `developerDefined`, `dataType`, `nameOnWeb`, `min`, `max`) VALUES
(0, 0, 'YES', 'varchar', 'User Name', 4, 16),
(0, 1, 'YES', 'int', 'C Experience', 0, 50),
(0, 2, 'YES', 'int', 'Java Experience', 0, 50),
(0, 3, 'NO', 'primaryKey', null, null, null),
(1, 0, 'YES', 'user_id', 'User Name', 4, 16),
(1, 1, 'YES', 'password', 'Password', 4, 16),
(1, 2, 'NO', 'primaryKey', null, null, null),
(1, 3, 'NO', 'role_number', null, null, null);


CREATE TABLE `role` (
`number` tinyint(4) NOT NULL DEFAULT '0',
`name` varchar(32) NOT NULL DEFAULT '',
`at_number` tinyint(4) DEFAULT '0' COMMENT '対応するアカウントテーブルの番号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `role` (`number`, `name`, `at_number`) VALUES
(0, 'Guest', NULL),
(1, 'Answerer', 1);


-- general_role --

CREATE TABLE `general_role` (
`roleNumber` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `general_role` (`roleNumber`) VALUES (0);


CREATE TABLE IF NOT EXISTS `create_auth` (
`tableNumber` tinyint(4) NOT NULL default '0',
`accOwnRoleNumber` int(11) default NULL,
`roleNumber` tinyint(4) NOT NULL default '0',
`permission` enum('YES','NO') NOT NULL default 'NO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `create_auth` (`tableNumber`, `accOwnRoleNumber`, `roleNumber`, `permission`) VALUES
(0, NULL, 0, 'NO'),
(0, NULL, 1, 'NO'),
(1, 1, 0, 'YES'),
(1, 1, 1, 'NO');


CREATE TABLE IF NOT EXISTS `delete_auth` (
`tableNumber` tinyint(4) NOT NULL default '0',
`accOwnRoleNumber` int(11) default NULL COMMENT '対象アカウントを持つロールの番号',
`roleNumber` tinyint(4) NOT NULL default '0',
`status` enum('self','role') NOT NULL default 'role',
`permission` enum('YES','NO') NOT NULL default 'NO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `delete_auth` (`tableNumber`, `accOwnRoleNumber`, `roleNumber`, `status`, `permission`) VALUES
(0, NULL, 0, 'role', 'NO'),
(0, NULL, 1, 'role', 'NO'),
(0, NULL, 1, 'self', 'NO'),
(1, 1, 0, 'role', 'NO'),
(1, 1, 1, 'role', 'NO'),
(1, 1, 1, 'self', 'NO');


CREATE TABLE IF NOT EXISTS `field_auth` (
`roleNumber` int(11) NOT NULL default '0',
`tableNumber` tinyint(4) NOT NULL default '0',
`accOwnRoleNumber` int(11) default NULL COMMENT '対象アカウントを持つロールの番号',
`fieldNumber` tinyint(4) NOT NULL default '0',
`status` enum('self','role') NOT NULL default 'role',
`ra` enum('YES','NO') NOT NULL default 'NO',
`wa` enum('YES','NO') NOT NULL default 'NO',
`ea` enum('YES','NO') NOT NULL default 'NO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `field_auth` (`roleNumber`, `tableNumber`, `accOwnRoleNumber`, `fieldNumber`, `status`, `ra`, `wa`, `ea`) VALUES
(0, 0, NULL, 0, 'role', 'YES', 'NO', 'NO'),
(0, 0, NULL, 1, 'role', 'YES', 'NO', 'NO'),
(0, 0, NULL, 2, 'role', 'YES', 'NO', 'NO'),
(1, 0, NULL, 0, 'role', 'YES', 'NO', 'NO'),
(1, 0, NULL, 0, 'self', 'YES', 'NO', 'NO'),
(1, 0, NULL, 1, 'role', 'YES', 'NO', 'NO'),
(1, 0, NULL, 1, 'self', 'YES', 'YES', 'NO'),
(1, 0, NULL, 2, 'role', 'YES', 'NO', 'NO'),
(1, 0, NULL, 2, 'self', 'YES', 'YES', 'NO'),
(0, 1, 1, 0, 'role', 'NO', 'NO', 'NO'),
(0, 1, 1, 1, 'role', 'NO', 'NO', 'NO'),
(1, 1, 1, 0, 'role', 'NO', 'NO', 'NO'),
(1, 1, 1, 0, 'self', 'NO', 'NO', 'NO'),
(1, 1, 1, 1, 'role', 'NO', 'NO', 'NO'),
(1, 1, 1, 1, 'self', 'NO', 'NO', 'NO');


CREATE TABLE IF NOT EXISTS `ia_assign` (
`tableNumber` tinyint(4) NOT NULL default '0',
`recordPrk` tinyint(4) NOT NULL default '0',
`roleNumber` tinyint(4) NOT NULL default '0',
`userNumber` int(11) NOT NULL default '0',
PRIMARY KEY  (`tableNumber`,`recordPrk`,`roleNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `ia_possesion` (
`tableNumber` tinyint(4) NOT NULL default '0',
`accOwnRoleNumber` int(11) default NULL COMMENT '対象アカウントのロール番号',
`roleNumber` tinyint(4) NOT NULL default '0',
`hasIA` enum('YES','NO') NOT NULL default 'YES',
`primaryKey` int(11) NOT NULL auto_increment,
PRIMARY KEY  (`primaryKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `ia_possesion` (`tableNumber`, `accOwnRoleNumber`, `roleNumber`, `hasIA`, `primaryKey`) VALUES
(0, NULL, 0, 'NO', NULL),
(0, NULL, 1, 'YES', NULL),
(1, 1, 0, 'NO', NULL),
(1, 1, 1, 'YES', NULL);


CREATE TABLE IF NOT EXISTS `ia_assign_auth` (
`tableNumber` int(11) NOT NULL default '0',
`accOwnRoleNumber` int(11) default NULL,
`assignerRoleNumber` int(11) NOT NULL default '0' COMMENT 'アサインする側のロールの番号',
`assignedRoleNumber` int(11) NOT NULL default '0' COMMENT 'アサインされるロールの番号',
`permission` enum('YES','NO') NOT NULL default 'NO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `transition_auth` (
`transPrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT '遷移（遷移プロセス）の主キー',
`roleNumber` int(11) NOT NULL DEFAULT '0',
`permission` enum('YES','NO') NOT NULL DEFAULT 'YES'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `transition_auth` (`transPrimaryKey`, `roleNumber`, `permission`) VALUES
(1, 0, 'YES'),
(1, 1, 'YES'),
(2, 0, 'NO'),
(2, 1, 'YES'),
(3, 0, 'YES'),
(3, 1, 'NO'),
(4, 0, 'YES'),
(4, 1, 'NO'),
(5, 0, 'YES'),
(5, 1, 'YES'),
(6, 0, 'YES'),
(6, 1, 'YES'),
(7, 0, 'YES'),
(7, 1, 'NO'),
(8, 0, 'YES'),
(8, 1, 'NO'),
(9, 0, 'YES'),
(9, 1, 'YES'),
(10, 0, 'YES'),
(10, 1, 'YES'),
(11, 0, 'YES'),
(11, 1, 'NO'),
(12, 0, 'YES'),
(12, 1, 'NO'),
(13, 0, 'NO'),
(13, 1, 'YES'),
(14, 0, 'NO'),
(14, 1, 'YES'),
(15, 0, 'YES'),
(15, 1, 'YES'),
(16, 0, 'YES'),
(16, 1, 'YES');


CREATE TABLE IF NOT EXISTS `exwrite_lock` (
`tblNumber` int(11) NOT NULL DEFAULT '0',
`recordPrimKey` bigint(20) NOT NULL DEFAULT '0',
`offset` int(11) NOT NULL DEFAULT '0',
`roleNumber` int(11) NOT NULL DEFAULT '0',
`userNumber` int(11) NOT NULL DEFAULT '0' COMMENT '非ゲストロールなので必ず持つはず'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `page_element` (
`pageNumber` tinyint(4) NOT NULL default '0',
`peNumber` tinyint(4) NOT NULL default '0',
`peKind` enum('Login Form','Table Display','Create Form','Update Form','Display Area','Service Argument Input Form','IA Assignment Form','Text','Hyper Link') NOT NULL default 'Display Area',
`primaryKey` int(11) NOT NULL default '0' COMMENT 'Page Element の主キー',
UNIQUE KEY `page_pe` (`pageNumber`,`peNumber`),
UNIQUE KEY `primaryKey` (`primaryKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element` (`pageNumber`, `peNumber`, `peKind`, `primaryKey`) VALUES
(0, 0, 'Hyper Link', 0),
(0, 1, 'Hyper Link', 1),
(0, 2, 'Hyper Link', 2),
(0, 3, 'Hyper Link', 3),
(0, 4, 'Hyper Link', 4),
(1, 0, 'Table Display', 5),
(1, 1, 'Hyper Link', 6),
(1, 2, 'Hyper Link', 7),
(2, 0, 'Display Area', 8),
(2, 1, 'Hyper Link', 9),
(2, 2, 'Hyper Link', 10),
(3, 0, 'Create Form', 11),
(3, 1, 'Hyper Link', 12),
(4, 0, 'Login Form', 13),
(4, 1, 'Hyper Link', 14),
(5, 0, 'Update Form', 15),
(5, 1, 'Hyper Link', 16),
(6, 0, 'Hyper Link', 17);


CREATE TABLE `page_element_hyper_link` (`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0',`destPageNumber` int(11) NOT NULL DEFAULT '0',`text` varchar(255) NOT NULL DEFAULT '') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element_hyper_link` (`pePrimaryKey`, `destPageNumber`, `text`) VALUES
(0, 1, 'Read Questionnaire Answers'),
(1, 5, 'Answer The Questionnaire'),
(2, 3, 'Register Your Account'),
(3, 4, 'Login'),
(4, 6, 'test'),
(6, 2, 'Show Total Years Of Experience'),
(7, 0, 'Top Page'),
(9, 1, 'Show Answers'),
(10, 0, 'Top Page'),
(12, 0, 'Top Page'),
(14, 0, 'Top Page'),
(16, 0, 'Top Page'),
(17, 0, 'test');


CREATE TABLE IF NOT EXISTS `page_element_saif` (
`pePrimaryKey` int(11) NOT NULL default '0',
`saifName` varchar(255) NOT NULL default '',`kind` enum('int','varchar','text','datetime','date','time','mail','enum') NOT NULL default 'int'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `page_element_da` (`pePrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT 'page_elementテーブルの主キー',`transitionPrimaryKey` int(11) NOT NULL COMMENT '遷移の主キー（どの遷移プロセスを通った場合か）',`tppn` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element_da` (`pePrimaryKey`, `transitionPrimaryKey`, `tppn`) VALUES
(8, 5, 5006);


CREATE TABLE IF NOT EXISTS `page_element_login_form` (`pePrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT 'page_elementテーブルの主キー',`accountTableNumber` int(11) NOT NULL DEFAULT '0' COMMENT '何番のアカウントテーブルのログインフォームか',`destPageNumber` int(11) NOT NULL DEFAULT '0' COMMENT 'ログイン成功時に行くページの番号') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element_login_form` (`pePrimaryKey`, `accountTableNumber`, `destPageNumber`) VALUES
(13, 1, 0);


CREATE TABLE IF NOT EXISTS `page_element_text` (`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0',`text` text NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `page_element_table_display` (`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0',`tblNumber` int(11) NOT NULL DEFAULT '0',`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT,PRIMARY KEY (`primaryKey`)) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

INSERT INTO `page_element_table_display` (`pePrimaryKey`, `tblNumber`, `primaryKey`) VALUES
(5, 0, null);


CREATE TABLE IF NOT EXISTS `page_element_table_display_field` (
`pePrimKey` bigint(20) NOT NULL DEFAULT '0',
`offset` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element_table_display_field` (`pePrimKey`, `offset`) VALUES
(5, 0),
(5, 1),
(5, 2);


CREATE TABLE IF NOT EXISTS `page_element_create_form` (
`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ページエレメント「Craeteフォーム」主キー',
`tableNumber` int(11) NOT NULL DEFAULT '0' COMMENT 'Create対象のテーブルの番号',
PRIMARY KEY (`pePrimaryKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element_create_form` (`pePrimaryKey`, `tableNumber`) VALUES
(11, 1);


CREATE TABLE IF NOT EXISTS `page_element_update_form` (
`pePrimaryKey` int(11) NOT NULL DEFAULT '0',
`tblNumber` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element_update_form` (`pePrimaryKey`, `tblNumber`) VALUES
(15, 0);


CREATE TABLE IF NOT EXISTS `page_element_update_form_field` (
`pePrimKey` bigint(20) NOT NULL DEFAULT '0',
`offset` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `page_element_update_form_field` (`pePrimKey`, `offset`) VALUES
(15, 0),
(15, 1),
(15, 2);


CREATE TABLE `transition` (
`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0' COMMENT '遷移元ページのページエレメントの主キー',
`tepNumber` int(11) NOT NULL DEFAULT '0' COMMENT '遷移先ページの番号',
`primaryKey` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '遷移プロセスの主キー',
PRIMARY KEY (`primaryKey`),
UNIQUE KEY `index_pePrimaryKey` (`pePrimaryKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `transition` (`pePrimaryKey`, `tepNumber`, `primaryKey`) VALUES
(0, 1, 1),
(1, 5, 2),
(2, 3, 3),
(3, 4, 4),
(6, 2, 5),
(7, 0, 6),
(11, 0, 7),
(12, 0, 8),
(9, 1, 9),
(10, 0, 10),
(13, 0, 11),
(14, 0, 12),
(15, 0, 13),
(16, 0, 14),
(4, 6, 15),
(17, 0, 16);


CREATE TABLE `tpp` (
`transitionPrimaryKey` int(11) NOT NULL DEFAULT '0' COMMENT '遷移プロセスの主キー',
`tppn` int(11) NOT NULL AUTO_INCREMENT,
`tppKind` enum('DDT Load','Create Form Reflection','Create Reflection','Update Reflection','Delete Reflection','Update Form Reflection','Delete Form Reflection','IA Reflection','Service Execution','Display Area Input','Const Array Int','Const Array String') NOT NULL DEFAULT 'DDT Load',
PRIMARY KEY (`tppn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp` (`transitionPrimaryKey`, `tppn`, `tppKind`) VALUES
(5, 5000, 'DDT Load'),
(5, 5001, 'Const Array Int'),
(5, 5002, 'Service Execution'),
(5, 5003, 'Const Array Int'),
(5, 5004, 'Const Array String'),
(5, 5005, 'Service Execution'),
(5, 5006, 'Service Execution'),
(7, 7000, 'Create Form Reflection'),
(7, 7001, 'Const Array Int'),
(7, 7002, 'Service Execution'),
(7, 7003, 'Const Array Int'),
(7, 7004, 'Const Array Int'),
(7, 7005, 'Const Array String'),
(7, 7006, 'Service Execution'),
(7, 7007, 'Service Execution'),
(7, 7008, 'Create Reflection'),
(7, 7009, 'IA Reflection'),
(13, 13000, 'Update Form Reflection');


CREATE TABLE IF NOT EXISTS `tpp_et_load` (`tppn` int(11) NOT NULL DEFAULT '0',`etNumber` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_et_load` (`tppn`, `etNumber`) VALUES
(5000, 0);


CREATE TABLE IF NOT EXISTS `tpp_update_form_reflection` (
`tppn` bigint(20) NOT NULL DEFAULT '0' COMMENT 'TPPの主キー',
`pePrimaryKey` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ページエレメント「Update Form」の主キー',
PRIMARY KEY (`tppn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_update_form_reflection` (`tppn`, `pePrimaryKey`) VALUES
(13000, 15);


CREATE TABLE IF NOT EXISTS `tpp_create_form_reflection` (
`tppn` int(11) NOT NULL DEFAULT '0',
`pePrimaryKey` int(11) NOT NULL DEFAULT '0',
PRIMARY KEY (`tppn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_create_form_reflection` (`tppn`, `pePrimaryKey`) VALUES
(7000, 11);


CREATE TABLE IF NOT EXISTS `tpp_create_reflection` (
`tppn` int(11) NOT NULL DEFAULT '0',
`tableNumber` int(11) NOT NULL DEFAULT '0',
`tfdTppn` int(11) NOT NULL DEFAULT '0',
PRIMARY KEY (`tppn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_create_reflection` (`tppn`, `tableNumber`, `tfdTppn`) VALUES
(7008, 0, 7007);


CREATE TABLE IF NOT EXISTS `tpp_ia_reflection` (
`tppn` int(11) NOT NULL DEFAULT '0',
`ddtNumber` int(11) NOT NULL DEFAULT '0',
`ddtTfdTppn` int(11) NOT NULL DEFAULT '0',
`atNumber` int(11) NOT NULL DEFAULT '0',
`atTfdTppn` int(11) NOT NULL DEFAULT '0',
PRIMARY KEY (`tppn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_ia_reflection` (`tppn`, `ddtNumber`, `ddtTfdTppn`, `atNumber`, `atTfdTppn`) VALUES
(7009, 0, 7008, 1, 7000);


CREATE TABLE IF NOT EXISTS `tpp_service_execution` (`tppn` int(11) NOT NULL DEFAULT '0',`serviceNumber` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_service_execution` (`tppn`, `serviceNumber`) VALUES
(5002, 1),
(5005, 2),
(5006, 3),
(7002, 1),
(7006, 4),
(7007, 3);


CREATE TABLE IF NOT EXISTS `tpp_service_execution_argument` (`tppn` int(11) NOT NULL DEFAULT '0' COMMENT 'Service実行TPPの主キー',`argNumber` int(11) NOT NULL DEFAULT '0',`argTppn` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_service_execution_argument` (`tppn`, `argNumber`, `argTppn`) VALUES
(5002, 0, 5000),
(5002, 1, 5001),
(5005, 0, 5000),
(5005, 1, 5003),
(5005, 2, 5004),
(5006, 0, 5002),
(5006, 1, 5005),
(7002, 0, 7000),
(7002, 1, 7001),
(7006, 0, 7003),
(7006, 1, 7004),
(7006, 2, 7005),
(7007, 0, 7002),
(7007, 1, 7006);


CREATE TABLE IF NOT EXISTS `tpp_constant_array_int` (`tppn` int(11) NOT NULL DEFAULT '0',`elementNumber` int(11) NOT NULL DEFAULT '0',`value` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_constant_array_int` (`tppn`, `elementNumber`, `value`) VALUES
(5001, 0, 0),
(5003, 0, 1),
(5003, 1, 2),
(7001, 0, 0),
(7003, 0, 1),
(7004, 0, 2);


CREATE TABLE IF NOT EXISTS `tpp_constant_array_string` (
`tppn` int(11) NOT NULL DEFAULT '0',
`elementNumber` int(11) NOT NULL DEFAULT '0',
`value` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tpp_constant_array_string` (`tppn`, `elementNumber`, `value`) VALUES
(5004, 0, 'Total Experience'),
(7005, 0, 'C Experience'),
(7005, 1, 'Java Experience');



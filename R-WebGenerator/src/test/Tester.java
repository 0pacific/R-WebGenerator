package test;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import property.GeneratorProperty;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import tpp.portTrans.*;
import tpp.service.*;
import transition.*;
import utility.*;
import utility.serialize.Serializer;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;


public class Tester implements Serializable {
	public WebPageManager webPageManager;
	public RoleManager roleManager;
	public MainFrame mainFrame;
	public TableManager tableManager;
	
	
	
	
	public Tester() {
		this.webPageManager = WebPageManager.getInstance();
		this.mainFrame = MainFrame.getInstance();
		this.roleManager = RoleManager.getInstance();
		this.tableManager = TableManager.getInstance();
	}

	

	public void test_debug() {
		addWebPage("index.php", 200, 70);
		addWebPage("read.php", 450, 40);
		addHyperLink("index.php", "Read Questionnaire Answers", "read.php");
	}
	
	
	
	public void test_questionnaire() {
		boolean japanese = GeneratorProperty.japanese();
		
		String QUESTIONNAIRE = japanese?"�A���P�[�g":"Questionnaire";
		String ACCOUNT = japanese?"�񓚎҃A�J�E���g":"Answerer Account";
		String ANSWERER = japanese?"�񓚎�":"Answerer";
		String USER_NAME = japanese?"���[�U��":"User Name";
		String C_YEAR = japanese?"C����o���N��":"C Experience";
		String JAVA_YEAR = japanese?"Java�o���N��":"Java Experience";
		String PASSWORD = japanese?"�p�X���[�h":"Password";
		
		/*
		 * Web�y�[�W
		 */

		addWebPage("index.php", 200, 70);
		addWebPage("read.php", 450, 40);
		addWebPage("sum.php", 550, 120);
		addWebPage("register.php", 100, 300);
		addWebPage("login.php", 300, 300);
		addWebPage("answer.php", 450, 250);

		
		// ���[���ǉ�
		addRole(ANSWERER);
		
		// �f�[�^�e�[�u�����P�ǉ�
		addDataTable(QUESTIONNAIRE);
		addFieldToDataTable(QUESTIONNAIRE, new Field(USER_NAME, Field.DATATYPE_VARCHAR, 4, 16));
		addFieldToDataTable(QUESTIONNAIRE, new Field(C_YEAR, Field.DATATYPE_INT, 0, 50));
		addFieldToDataTable(QUESTIONNAIRE, new Field(JAVA_YEAR, Field.DATATYPE_INT, 0, 50));

		// �A�J�E���g�e�[�u���u�񓚎҃A�J�E���g�v�ǉ�
		String[] rootRoleNames = {ANSWERER};
		addAccountTable(ACCOUNT, rootRoleNames);
		addFieldToAccountTable(ACCOUNT, new Field(USER_NAME, Field.DATATYPE_USERID, 4, 16));
		addFieldToAccountTable(ACCOUNT, new Field(PASSWORD, Field.DATATYPE_PASSWORD, 4, 16));

		
		/*
		 * ���쌠��
		 */

		// �Q�X�g -> �A���P�[�g
		String guest = GeneratorProperty.japanese()?"�Q�X�g":"Guest";
		setDtFieldAuth(guest, QUESTIONNAIRE, "RA", USER_NAME, "Read", true);
		setDtFieldAuth(guest, QUESTIONNAIRE, "RA", C_YEAR, "Read", true);
		setDtFieldAuth(guest, QUESTIONNAIRE, "RA", JAVA_YEAR, "Read", true);

		// �񓚎� -> �A���P�[�g
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "RA", USER_NAME, "Read", true);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "RA", C_YEAR, "Read", true);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "RA", JAVA_YEAR, "Read", true);
		setIaDefined(ANSWERER, QUESTIONNAIRE, true);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "IA", USER_NAME, "Read", true);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "IA", C_YEAR, "Read", true);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "IA", JAVA_YEAR, "Read", true);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "IA", USER_NAME, "", false);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "IA", C_YEAR, "Write", true);
		setDtFieldAuth(ANSWERER, QUESTIONNAIRE, "IA", JAVA_YEAR, "Write", true);

		// �Q�X�g -> �񓚎҃A�J�E���g
		setAtCreateAuth(guest, ACCOUNT, ANSWERER, true);
		
		/*
		 * �y�[�W�G�������g
		 */
		
		// index.php
		addHyperLink("index.php", "Read Questionnaire Answers", "read.php");
		addHyperLink("index.php", "Answer The Questionnaire", "answer.php");
		addHyperLink("index.php", "Register Your Account", "register.php");
		addHyperLink("index.php", "Login", "login.php");
		setLogoutDestPage("index.php");
		
		// read.php
		addTableDisplay("read.php", QUESTIONNAIRE, new int[]{0, 1, 2});
		addHyperLink("read.php", "Show Total Years Of Experience", "sum.php");
		addHyperLink("read.php", "Top Page", "index.php");

		// register.php
		addCreateForm("register.php", ACCOUNT, "index.php");
		addHyperLink("register.php", "Top Page", "index.php");
		//addSaif("register.php", "SAIF�i�e�X�g1�j", PageElementSaif.KIND_INT);
		//addSaif("register.php", "SAIF�i�e�X�g2�j", PageElementSaif.KIND_VARCHAR);
		//addSaif("register.php", "SAIF�i�e�X�g3�j", PageElementSaif.KIND_DATETIME);
		//addSaif("register.php", "SAIF�i�e�X�g4�j", PageElementSaif.KIND_DATE);
		//addSaif("register.php", "SAIF�i�e�X�g5�j", PageElementSaif.KIND_TIME);
		
		// sum.php
		addDisplayArea("sum.php");
		addHyperLink("sum.php", "Show Answers", "read.php");
		addHyperLink("sum.php", "Top Page", "index.php");

		// login.php
		addLoginForm("login.php", ACCOUNT, "index.php");
		addHyperLink("login.php", "Top Page", "index.php");

		// answer.php
		addUpdateForm("answer.php", QUESTIONNAIRE, new int[]{0, 1, 2}, "index.php");
		addHyperLink("answer.php", "Top Page", "index.php");

		
		/*
		 * �J�ڌ���
		 */
		
		setTransAuth(guest,	"index.php",	0,	true);
		setTransAuth(guest,	"index.php",	1,	false);	// �Q�X�g�͉񓚃y�[�W�ɐi�߂Ȃ�
		setTransAuth(guest,	"index.php",	2,	true);
		setTransAuth(guest,	"index.php",	3,	true);
		setTransAuth(ANSWERER,	"index.php",	0,	true);
		setTransAuth(ANSWERER,	"index.php",	1,	true);
		setTransAuth(ANSWERER,	"index.php",	2,	false);		// ���O�C�����̓A�J�E���g�o�^�ł��Ȃ�
		setTransAuth(ANSWERER,	"index.php",	3,	false);		// ���O�C�����̓��O�C����ʂɍs���Ȃ�
		setTransAuth(guest,	"read.php",		1,	true);
		setTransAuth(guest,	"read.php",		2,	true);
		setTransAuth(ANSWERER,	"read.php",		1,	true);
		setTransAuth(ANSWERER,	"read.php",		2,	true);
		setTransAuth(guest,	"register.php",	0,	true);
		setTransAuth(guest,	"register.php",	1,	true);
		setTransAuth(ANSWERER,	"register.php",	0,	false);		//
		setTransAuth(ANSWERER,	"register.php",	1,	false);		// ���������񓚎҂͂��̃y�[�W�ɗ���Ȃ��͂�
		setTransAuth(guest,	"sum.php",		1,	true);
		setTransAuth(guest,	"sum.php",		2,	true);
		setTransAuth(ANSWERER,	"sum.php",		1,	true);
		setTransAuth(ANSWERER,	"sum.php",		2,	true);
		setTransAuth(guest,	"login.php",	0,	true);
		setTransAuth(guest,	"login.php",	1,	true);
		setTransAuth(ANSWERER,	"login.php",	0,	false);
		setTransAuth(ANSWERER,	"login.php",	1,	false);
		setTransAuth(guest,	"answer.php",	0,	false);
		setTransAuth(guest,	"answer.php",	1,	false);
		setTransAuth(ANSWERER,	"answer.php",	0,	true);
		setTransAuth(ANSWERER,	"answer.php",	1,	true);


		/*
		 * �J�ڃv���Z�X
		 */

		TransitionProcess transProc;
		
		// �A�J�E���g�o�^�̑J�ڃv���Z�X
		transProc = getTransProc("register.php", 0);

		TppCreateFormReflection cfr = getCreateFormReflection("register.php", 0);

		Service serviceA = addService(transProc, japanese?"�t�B�[���h���o":"Fields Extraction", 200, 50);
		defineServiceArgTfdOutputer(serviceA, 0, (TfdOutputer)cfr);
		defineServiceArgConstIntArray(serviceA, 1, new int[]{0});
		
		Service serviceB = addService(transProc, japanese?"�u�����N�\�f�[�^�쐬":"Blank Table", 200, 250);
		defineServiceArgConstInt(serviceB, 0, 1);
		defineServiceArgConstInt(serviceB, 1, 2);
		defineServiceArgConstStringArray(serviceB, 2, new String[]{C_YEAR, JAVA_YEAR});
		
		Service serviceC = addService(transProc, japanese?"�\����":"Join", 400, 250);
		defineServiceArgTfdOutputer(serviceC, 0, (TfdOutputer)serviceA);
		defineServiceArgTfdOutputer(serviceC, 1, (TfdOutputer)serviceB);

		// �b��
		//Service serviceD = addService(transProc, japanese?"���t�A�ԃt�B�[���h�쐬":"Date Range Field", 400, 30);

		TppCreateReflection cr = addCreateReflection(transProc, QUESTIONNAIRE, 450, 100);
		defineTfdOutputerForCreateReflection(cr, (TfdOutputer)serviceC);
		
		addIaReflection(transProc, QUESTIONNAIRE, (TfdOutputer)cr, ACCOUNT, cfr, 600, 300);
		
		
		// ���v�l�v�Z�̑J�ڃv���Z�X
		transProc = getTransProc("read.php", 1);

		TppTableReading trQuestionnaire = addTableReading(transProc, QUESTIONNAIRE, 30, 200);

		Service service1 = addService(transProc, japanese?"�t�B�[���h���o":"Fields Extraction", 100, 150);
		defineServiceArgTfdOutputer(service1, 0, (TfdOutputer)trQuestionnaire);
		defineServiceArgConstIntArray(service1, 1, new int[]{0});

		Service service2 = addService(transProc, japanese?"�t�B�[���h���v�l�v�Z":"Fields Summing", 100, 250);
		defineServiceArgTfdOutputer(service2, 0, (TfdOutputer)trQuestionnaire);
		defineServiceArgConstIntArray(service2, 1, new int[]{1, 2});
		defineServiceArgConstString(service2, 2, japanese?"���v�o���N��":"Total Experience");
		
		Service service3 = addService(transProc, japanese?"�\����":"Join", 300, 250);
		defineServiceArgTfdOutputer(service3, 0, (TfdOutputer)service1);
		defineServiceArgTfdOutputer(service3, 1, (TfdOutputer)service2);

		PageElementDisplayArea da = (PageElementDisplayArea)getPeByPageNameAndPeIndex("sum.php", 0);
		defineDisplayAreaInputTfdOutputer(transProc, da, (TfdOutputer)service3);
	}
	
	
	
	public void test_reportSystem() {
		// Web�y�[�W��`
		addWebPage("login_root.php", 10, 10);
		addWebPage("top_root.php", 80, 10);
		addWebPage("read_root.php", 150, 10);
		addWebPage("read_root_sum.php", 250, 10);
		addWebPage("create_lecturer_account.php", 450, 50);
		
		addWebPage("login_lecturer.php", 10, 80);
		addWebPage("top_lecturer.php", 80, 80);
		addWebPage("read_lecturer.php", 150, 80);
		addWebPage("evaluate.php", 250, 80);
		addWebPage("read_lecturer_account.php", 90, 150);
		addWebPage("lecturer_password_change.php", 190, 150);
		addWebPage("lecturer_password_change_finish.php", 250, 150);

		addWebPage("top.php", 350, 10);
		addWebPage("register.php", 420, 10);
		addWebPage("student_register_finish.php", 490, 10);

		addWebPage("login_student.php", 360, 120);
		addWebPage("top_student.php", 450, 120);
		addWebPage("report_confirm1.php", 540, 120);
		addWebPage("report_confirm2.php", 540, 190);
		addWebPage("report_submit1.php", 610, 120);
		addWebPage("report_submit2.php", 610, 190);
		addWebPage("studAcc.php", 350, 250);
		addWebPage("student_account.php", 420, 250);
		addWebPage("student_password_change.php", 490, 250);
		addWebPage("student_password_change_finish.php", 560, 250);

		// ���[���ǉ�
		addRole("�S������");
		addRole("�u�t�P");
		addRole("�u�t�Q");
		addRole("�w��");
		
		// �f�[�^�e�[�u�����Q�ǉ����Ă���
		addDataTable("���|�[�g��o�\");
		addDataTable("�e�X�g�p�f�[�^�e�[�u��");
		addFieldToDataTable("���|�[�g��o�\", new Field("�w�Дԍ�", Field.DATATYPE_VARCHAR, 8, 8));
		addFieldToDataTable("���|�[�g��o�\", new Field("����", Field.DATATYPE_VARCHAR, 1, 32));
		addFieldToDataTable("���|�[�g��o�\", new Field("���|�[�g�P", Field.DATATYPE_FILE, -1, 3000));
		addFieldToDataTable("���|�[�g��o�\", new Field("���_�P", Field.DATATYPE_INT, 0, 100));
		addFieldToDataTable("���|�[�g��o�\", new Field("���|�[�g�Q", Field.DATATYPE_FILE, -1, 3000));
		addFieldToDataTable("���|�[�g��o�\", new Field("���_�Q", Field.DATATYPE_INT, 0, 100));

		// �A�J�E���g�e�[�u���u�S�������A�J�E���g�v�ǉ�
		String[] rootRoleNames = {"�S������"};
		addAccountTable("�S�������A�J�E���g", rootRoleNames);
		addFieldToAccountTable("�S�������A�J�E���g", new Field("���[���A�h���X", Field.DATATYPE_MAIL_AUTH, -1, -1));
		addFieldToAccountTable("�S�������A�J�E���g", new Field("�S�������p�X���[�h", Field.DATATYPE_PASSWORD, 4, 16));
		addFieldToAccountTable("�S�������A�J�E���g", new Field("����", Field.DATATYPE_VARCHAR, 1, 32));

		// �A�J�E���g�e�[�u���u�u�t�A�J�E���g�v�ǉ�
		String[] lectRoleNames = {"�u�t�P", "�u�t�Q"};
		addAccountTable("�u�t�A�J�E���g", lectRoleNames);
		addFieldToAccountTable("�u�t�A�J�E���g", new Field("���[���A�h���X", Field.DATATYPE_MAIL_AUTH, -1, -1));
		addFieldToAccountTable("�u�t�A�J�E���g", new Field("�u�t�p�X���[�h", Field.DATATYPE_PASSWORD, 4, 16));
		addFieldToAccountTable("�u�t�A�J�E���g", new Field("����", Field.DATATYPE_VARCHAR, 1, 32));
		addFieldToAccountTable("�u�t�A�J�E���g", new Field("���[����", Field.DATATYPE_ROLE_NAME, -1, -1));

		// �A�J�E���g�e�[�u���u�w���A�J�E���g�v�ǉ�
		String[] studRoleNames = {"�w��"};
		addAccountTable("�w���A�J�E���g", studRoleNames);
		addFieldToAccountTable("�w���A�J�E���g", new Field("�w�Дԍ�", Field.DATATYPE_USERID, 8, 8));
		addFieldToAccountTable("�w���A�J�E���g", new Field("�p�X���[�h", Field.DATATYPE_PASSWORD, 4, 16));
		addFieldToAccountTable("�w���A�J�E���g", new Field("����", Field.DATATYPE_VARCHAR, 1, 32));
		addFieldToAccountTable("�w���A�J�E���g", new Field("���[���A�h���X", Field.DATATYPE_MAIL));
		
		// top_root.php
		addHyperLink("top_root.php", "���|�[�g��o�󋵉{��", "read_root.php");
		addHyperLink("top_root.php", "�u�t�A�J�E���g�쐬", "create_lecturer_account.php");
		
		// login_root.php�̃y�[�W�G�������g
		addTextPageElement("login_root.php", "���[���A�h���X�ƃp�X���[�h����͂��A���O�C�����ĉ������B");
		addHyperLink("login_root.php", "�u�`�g�b�v�y�[�W", "top.php");

		// read_root.php�̃y�[�W�G�������g
		addTableDisplay("read_root.php", "���|�[�g��o�\", new int[]{0,1,2,3,4,5});
		addHyperLink("read_root.php", "���v�_��\��", "read_root_sum.php");
		addHyperLink("read_root.php", "�g�b�v�֖߂�", "top_root.php");

		// register.php
		addCreateForm("register.php", "�w���A�J�E���g", "student_register_finish.php");
		addHyperLink("register.php", "�g�b�v�y�[�W��", "top.php");
		
		// read_root_sum.php�̃y�[�W�G�������g
		// tester.addDisplayArea("read_root_sum.php");
	}
	
	// Web�y�[�W�ǉ�
	public void addWebPage(String newPageName, int panelX, int panelY) {
		webPageManager.addNewPage(newPageName);
		WebPage newPage = webPageManager.getPageByName(newPageName);
		WebPagePanelManager.getInstance().move(newPage, panelX, panelY);
	}

	
	
	
	// �y�[�W�G�������g�ҏW��ʂֈړ��i�y�[�W���w��j
	public void gotoPeEdit(String webPageName) {
		WebPage webPage = webPageManager.getPageByName(webPageName);
		mainFrame.shiftToPeEdit(webPage);
	}
	
	public void gotoTransProcEdit(String webPageName, int triggerPeNumber) {
		MainFrame.getInstance().shiftToTpEdit(getTransition(webPageName, triggerPeNumber));
	}




	// ���[���ǉ�
	public void addRole(String roleName) {
		roleManager.addRole(new NormalRole(roleName));
		Panel_RoleEdit_Above.getInstance().refreshRoleTable();
	}


	

	
	public void setIaDefined(String roleName, String dtName, boolean defined) {
		AuthoritySet authToDt = getAuthToDt(roleName, dtName);
		authToDt.setHavingIa(defined);
	}
	
	
	public AuthoritySet getAuthToDt(String roleName, String dtName) {
		Role role = RoleManager.getInstance().getRoleByName(roleName);
		DataTable dataTable = (DataTable)TableManager.getInstance().getTableByName(dtName);
		AuthorityToDataTable authToDt = (AuthorityToDataTable)AuthorityManager.getInstance().getTableAuth(role, dataTable);
		return authToDt.authSet;
	}
	public AuthoritySet getAuthToAccount(String roleName, String atName, String accOwnRoleName) {
		Role role = RoleManager.getInstance().getRoleByName(roleName);
		AccountTable accountTable = (AccountTable)TableManager.getInstance().getTableByName(atName);
		AuthorityToAccountTable authToAt = (AuthorityToAccountTable)AuthorityManager.getInstance().getTableAuth(role, accountTable);
		AuthoritySet authToAccount = authToAt.getAuthorityToAccountByAccountOwnerRoleName(accOwnRoleName);
		return authToAccount;
	}
	
	
	public void setDtFieldAuth(String roleName, String dtName, String authType, String fieldName, String operation, boolean auth) {
		DataTable dataTable = (DataTable)tableManager.getTableByName(dtName);
		AuthoritySet authToDt = getAuthToDt(roleName, dtName);

		Field field = dataTable.getFieldByName(fieldName);
		if(authType.equals("RA")) {
			if(operation.equals("Read")) {
				authToDt.setRaRead(field.getOffset(), auth);
			}
			else if(operation.equals("Write")) {
				authToDt.setRaWrite(field.getOffset(), auth);
			}
			else if(operation.equals("ExWrite")) {
				authToDt.setRaExWrite(field.getOffset(), auth);
			}
		}
		else if(authType.equals("IA")) {
			if(operation.equals("Read")) {
				authToDt.setIaRead(field.getOffset(), auth);
			}
			else if(operation.equals("Write")) {
				authToDt.setIaWrite(field.getOffset(), auth);
			}
			else if(operation.equals("ExWrite")) {
				authToDt.setIaExWrite(field.getOffset(), auth);
			}
		}
	}
	public void setAtFieldAuth(String roleName, String atName, String accOwnRoleName, String authType, String fieldName, String operation, boolean auth) {
		AccountTable accountTable = (AccountTable)TableManager.getInstance().getTableByName(atName);
		AuthoritySet authToAccount = getAuthToAccount(roleName, atName, accOwnRoleName);

		Field field = accountTable.getFieldByName(fieldName);
		if(authType.equals("RA")) {
			if(operation.equals("Read")) {
				authToAccount.setRaRead(field.getOffset(), auth);
			}
			else if(operation.equals("Write")) {
				authToAccount.setRaWrite(field.getOffset(), auth);
			}
			else if(operation.equals("ExWrite")) {
				authToAccount.setRaExWrite(field.getOffset(), auth);
			}
		}
		else if(authType.equals("IA")) {
			if(operation.equals("Read")) {
				authToAccount.setIaRead(field.getOffset(), auth);
			}
			else if(operation.equals("Write")) {
				authToAccount.setIaWrite(field.getOffset(), auth);
			}
			else if(operation.equals("ExWrite")) {
				authToAccount.setIaExWrite(field.getOffset(), auth);
			}
		}
	}
	public void setDtCreateAuth(String roleName, String dtName, boolean auth) {
		AuthoritySet authToDt = getAuthToDt(roleName, dtName);
		authToDt.setCreate(auth);
	}
	public void setAtCreateAuth(String roleName, String atName, String accOwnRoleName, boolean auth) {
		AuthoritySet authToAccount = getAuthToAccount(roleName, atName, accOwnRoleName);
		authToAccount.setCreate(auth);
	}



	public void addDataTable(String dtName) {
		DataTable newTable = new DataTable(dtName);
		TableManager.getInstance().addDataTable(newTable);
	}





	public void addAccountTable(String atName, String[] roleNames) {
		ArrayList<NormalRole> normalRoles = new ArrayList<NormalRole>();
		for(int i=0; i<roleNames.length; i++) {
			NormalRole normalRole = (NormalRole)roleManager.getRoleByName(roleNames[i]);
			normalRoles.add(normalRole);
		}
		AccountTable newTable = new AccountTable(atName, normalRoles);
		TableManager.getInstance().addAccountTable(newTable);
	}

	
	
	
	
	public void addFieldToDataTable(String dtName, Field field) {
		DataTable dt = (DataTable)tableManager.getTableByName(dtName);
		dt.addField(field);
	}





	public void addFieldToAccountTable(String atName, Field field) {
		AccountTable at = (AccountTable)tableManager.getTableByName(atName);
		at.addField(field);
	}




	
	public void addTextPageElement(String pageName, String text) {
		WebPage parentPage = webPageManager.getPageByName(pageName);

		PageElementText peText = new PageElementText(parentPage, text);
		parentPage.addPageElement(peText);
	}

	
	
	
	public void addCreateForm(String pageName, String tableName, String destPageName) {
		WebPage parentPage = webPageManager.getPageByName(pageName);
		WebPage destPage = webPageManager.getPageByName(destPageName);
		SuperTable table = tableManager.getTableByName(tableName);
		
		PageElementCreateForm createForm = new PageElementCreateForm(parentPage, table, false, destPage);
		parentPage.addPageElement(createForm);
	}
	
	

	public void addSaif(String pageName, String saifName, String saifDataType) {
		WebPage parentPage = webPageManager.getPageByName(pageName);

		PageElementSaif saif = new PageElementSaif(parentPage, saifName, saifDataType);
		parentPage.addPageElement(saif);
	}

	
	public void addLoginForm(String pageName, String accountTableName, String destPageName) {
		WebPage parentPage = webPageManager.getPageByName(pageName);
		AccountTable accountTable = (AccountTable)TableManager.getInstance().getTableByName(accountTableName);
		WebPage destPage = webPageManager.getPageByName(destPageName);

		PageElementLoginForm hl = new PageElementLoginForm(parentPage, accountTable, destPage);
		parentPage.addPageElement(hl);
	}
	
	

	public void addHyperLink(String pageName, String text, String destPageName) {
		WebPage parentPage = webPageManager.getPageByName(pageName);
		WebPage destPage = webPageManager.getPageByName(destPageName);

		PageElement_HyperLink hl = new PageElement_HyperLink(parentPage, destPage, text);
		parentPage.addPageElement(hl);
	}





	public void addTableDisplay(String pageName, String tableName, int[] limitedOffsets) {
		SuperTable table = tableManager.getTableByName(tableName);
		WebPage parentPage = webPageManager.getPageByName(pageName);
		PageElementTableDisplay td = new PageElementTableDisplay(parentPage, table, limitedOffsets);
		
		parentPage.addPageElement(td);
	}


	
	
	public void addUpdateForm(String pageName, String tableName, int[] limitedOffsets, String destPageName) {
		SuperTable table = tableManager.getTableByName(tableName);
		WebPage parentPage = webPageManager.getPageByName(pageName);
		WebPage destPage = webPageManager.getPageByName(destPageName);

		PageElementUpdateForm updateForm = new PageElementUpdateForm(parentPage, table, limitedOffsets, destPage);
		
		parentPage.addPageElement(updateForm);
	}
	
	
	
	
	public PageElementDisplayArea addDisplayArea(String pageName) {
		WebPage parentPage = webPageManager.getPageByName(pageName);
		PageElementDisplayArea da = new PageElementDisplayArea(parentPage);
		
		parentPage.addPageElement(da);

		return da;
	}
	
	
	
	
	public PageElement getPeByPageNameAndPeIndex(String pageName, int peIndex) {
		WebPage parentPage = webPageManager.getPageByName(pageName);
		PageElement pe = parentPage.getPageElement(peIndex);
		return pe;
	}

	
	
	public TppCreateFormReflection getCreateFormReflection(String pageName, int createFormPeIndex) {
		PageElement createForm = getPeByPageNameAndPeIndex(pageName, createFormPeIndex);
		Transition transByCreateForm = TransitionManager.getInstance().getTransitionByTriggerPe(createForm);
		TransitionProcess transProc = transByCreateForm.transProc;
		
		// �ŏ���TPP��Create�t�H�[�����t���N�V�����ɂȂ��Ă���͂��ł���
		TppCreateFormReflection cfr = (TppCreateFormReflection)transProc.getTpp(0);
		return cfr;
	}
	
	
	
	public void setLogoutDestPage(String webPageName) {
		WebPage destPage = WebPageManager.getInstance().getPageByName(webPageName);
		WebPageManager.getInstance().setLogoutDestPage(destPage);
	}
	
	
	
	public void addTransition(WebPage parentPage, PageElement triggerPe, WebPage destPage) {
		Transition trans = new Transition(parentPage, triggerPe, destPage);
		TransitionManager.getInstance().addTransition(trans);
	}




	public TransitionProcess getTransProc(String pageName, int triggerPeNumber) {
		return getTransition(pageName, triggerPeNumber).transProc;
	}

	
	
	
	public Transition getTransition(String pageName, int triggerPeNumber) {
		WebPage webPage = WebPageManager.getInstance().getPageByName(pageName);
		PageElement triggerPe = webPage.getPageElement(triggerPeNumber);

		Transition trans = TransitionManager.getInstance().getTransitionByTriggerPe(triggerPe);
		return trans;
	}
	
	
	
	
	public void setTransAuth(String roleName, String pageName, int triggerPeNumber, boolean auth) {
		Role role = RoleManager.getInstance().getRoleByName(roleName);
		Transition trans = getTransition(pageName, triggerPeNumber);
		TransitionAuth transAuth = AuthorityManager.getInstance().getTransAuth(role, trans);
		transAuth.changeTransAuth(auth);
	}
	
	
	
	
	
	public TppTableReading addTableReading(TransitionProcess transProc, String tableName, int panelPosX, int panelPosY) {
		SuperTable table = TableManager.getInstance().getTableByName(tableName);
		TppTableReading tableReading = new TppTableReading(transProc, table);

		transProc.addTpp(tableReading);
		
		Panel_TpEdit_Above.getInstance().addTableReadingPanelByTableReading(tableReading, panelPosX, panelPosY);

		return tableReading;
	}
	
	
	
	
	public TppCreateReflection addCreateReflection(TransitionProcess transProc, String tableName, int panelPosX, int panelPosY) {
		SuperTable table = TableManager.getInstance().getTableByName(tableName);
		TppCreateReflection reflection = new TppCreateReflection(transProc, table);
		transProc.addTpp(reflection);

		// �쐬����Create���t���N�V������\���p�l�������p�l���̑J�ڃv���Z�X�}�֒ǉ�
		Panel_TpEdit_Above.getInstance().addCreateReflectionPanel(reflection, panelPosX, panelPosY);

		return reflection;
	}

	
	
	public void defineTfdOutputerForCreateReflection(TppCreateReflection createReflection, TfdOutputer to) {
		DataPort outPort = ((TransitionProcessPart)to).getOutputPort();
		PortTransManager portTransManager = createReflection.transProc.portTransManager;
		
		portTransManager.addPortTrans(outPort, createReflection.getTfdInputPort());
	}
	
	
	
	
	
	
	
	public void addIaReflection(TransitionProcess transProc, String dtName, TfdOutputer dtTfdOutputer, String atName, TfdOutputer atTfdOutputer, int panelPosX, int panelPosY) {
		// IA���t���N�V�������쐬�A�J�ڃv���Z�X�֍Ō��TPP�Ƃ��Ēǉ�
		TppIAReflection iaReflection = new TppIAReflection(transProc);
		transProc.addTpp(iaReflection);

		// IA���t���N�V�����p�l���̍쐬�����p�l���ֈ˗�
		Panel_TpEdit_Above.getInstance().addIaReflectionPanel(iaReflection, panelPosX, panelPosY);

		DataTable dataTable = (DataTable)TableManager.getInstance().getTableByName(dtName);
		iaReflection.dataTable = dataTable;
		
		AccountTable accountTable = (AccountTable)TableManager.getInstance().getTableByName(atName);
		iaReflection.accountTable = accountTable;

		
		PortTransManager portTransManager = transProc.portTransManager;
		
		DataPort dtTfdOutputerOutPort = ((TransitionProcessPart)dtTfdOutputer).getOutputPort();
		portTransManager.addPortTrans(dtTfdOutputerOutPort, iaReflection.getDataTableTfdInputPort());

		DataPort atTfdOutputerOutPort = ((TransitionProcessPart)atTfdOutputer).getOutputPort();
		portTransManager.addPortTrans(atTfdOutputerOutPort, iaReflection.getAccountTableTfdInputPort());
	}
	
	
	
	
	
	
	public Service addService(TransitionProcess transProc, String serviceName, int panelPosX, int panelPosY) {
		Service service = Service.getInstanceByServiceName(transProc, serviceName);

		transProc.addTpp(service);

		Panel_TpEdit_Above.getInstance().addServicePanelByService(service, panelPosX, panelPosY);

		return service;
	}





	public void defineServiceArgTfdOutputer(Service service, int argIndex, TfdOutputer to) {
		DataPort outPort = ((TransitionProcessPart)to).getOutputPort();
		PortTransManager portTransManager = service.transProc.portTransManager;

		service.removeArgIfDefined(argIndex);
		portTransManager.addPortTrans(outPort, service.getInputPort(argIndex));
	}


	
	
	public void defineDisplayAreaInputTfdOutputer(TransitionProcess transProc, PageElementDisplayArea da, TfdOutputer to) {
		PageElementPort daInputPort = da.inputPePortsHashMap.get(transProc);

		// �uTPP�̏o�̓|�[�g -> DisplayArea�̓��̓|�[�g�v�Ƃ����|�[�g�g�����X�~�b�V�����ǉ�
		transProc.portTransManager.addPortTrans(((TransitionProcessPart)to).outputPort, daInputPort);
	}
	



	
	
	
	public void defineServiceArgConstInt(Service service, int argIndex, int value) {
		TppConstInt newConstInt = new TppConstInt(service.transProc, value);

		// �J�ڃv���Z�X��TPP�z��ɁA���̃��m�萔��ǉ��i�T�[�r�X�̂Ƃ���ɑ}�����A�T�[�r�X�ȍ~���E�։����o���j
		service.transProc.addTpp(service.getTppo(), newConstInt);
		
		// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
		TppPort startPort = newConstInt.outputPort;
		TppPort endPort = service.getInputPort(argIndex);
		service.transProc.portTransManager.addPortTrans(startPort, endPort);
	}
	public void defineServiceArgConstString(Service service, int argIndex, String value) {
		TppConstString newConstString = new TppConstString(service.transProc, value);

		// �J�ڃv���Z�X��TPP�z��ɁA���̃��m�萔��ǉ��i�T�[�r�X�̂Ƃ���ɑ}�����A�T�[�r�X�ȍ~���E�։����o���j
		service.transProc.addTpp(service.getTppo(), newConstString);
		
		// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
		TppPort startPort = newConstString.outputPort;
		TppPort endPort = service.getInputPort(argIndex);
		service.transProc.portTransManager.addPortTrans(startPort, endPort);
	}
	public void defineServiceArgConstIntArray(Service service, int argIndex, int[] intArray) {
		TransitionProcess transProc = service.transProc;
		TppConstArrayInt tpp = new TppConstArrayInt(transProc, intArray);
		transProc.addTpp(service.getTppo(), tpp);
		
		// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
		TppPort startPort = tpp.outputPort;
		TppPort endPort = service.getInputPort(argIndex);
		transProc.portTransManager.addPortTrans(startPort, endPort);
	}
	public void defineServiceArgConstStringArray(Service service, int argIndex, String[] stringArray) {
		TransitionProcess transProc = service.transProc;
		TppConstArrayString tpp = new TppConstArrayString(transProc, stringArray);
		transProc.addTpp(service.getTppo(), tpp);
		
		// �Q�̃|�[�g���w�肵�A�V���ȃ|�[�g�g�����X�~�b�V������ǉ�
		TppPort startPort = tpp.outputPort;
		TppPort endPort = service.getInputPort(argIndex);
		transProc.portTransManager.addPortTrans(startPort, endPort);
	}
}


